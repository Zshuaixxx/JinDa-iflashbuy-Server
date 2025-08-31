package com.sky.controller.user;

/**
 * @author 帅的被人砍
 * @create 2024-09-06 17:26
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.SetmealDishService;
import com.sky.service.SetmealService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.utils.RedisLockUtil;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户相关接口
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisLockUtil redisLockUtil;

    /**
     * 用户端登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/user/user/login")
    public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户微信登录{}",userLoginDTO);

        //微信登录
        User user = userService.wxlogin(userLoginDTO);

        //生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginvo = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginvo);
    }

    /**
     * 用户端查询分类和套餐信息
     *
     * @param type
     * @param merchantId
     * @return
     */
    @GetMapping("/user/category/list")
    public Result<Category[]> getCategoryByType(Integer type, Long merchantId){
        log.info("用户端查询分类和套餐信息{}",type);
        Category[] categories= categoryService.getCateListByType(type, merchantId);
        return Result.success(categories);
    }

    /**
     * 查询菜品分类id下的所有菜品和对应的口味
     * @param categoryId
     * @return
     */
    @GetMapping("/user/dish/list")
    public Result<DishVO[]> getDishAndFlavorsByCategoryId(Long categoryId){
        log.info("用户查询菜品分类id下的所有菜品和对应的口味{}",categoryId);
        //构造redis中的key，规则：dish_分类id
        String key = "dish_"+categoryId;

        //1.查询redis中是否存在菜品数据
        DishVO[] list = new DishVO[0];
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj instanceof ArrayList) {
            List<DishVO> dishVOList = objectMapper.convertValue(obj, new TypeReference<List<DishVO>>() {});
            log.error(dishVOList.get(0).toString());
            list = dishVOList.toArray(new DishVO[0]);
        }
        if(list != null && list.length> 0) {
            //如果存在，直接返回，无须查询数据库
            return Result.success(list);
        }

        //2.如果不存在，查询数据库
        String lockKey = "lock_dish_" + categoryId;
        String lockValue = UUID.randomUUID().toString();
        boolean isLocked = redisLockUtil.getLock(stringRedisTemplate, lockKey, lockValue, 1); // 锁的过期时间设置为 10 秒
        if (isLocked) {
            try {
                // 再次检查 Redis 中是否存在菜品数据
                obj = redisTemplate.opsForValue().get(key);
                if (obj != null) {
                    try {
                        List<DishVO> dishVOList = objectMapper.convertValue(obj, new TypeReference<List<DishVO>>() {});
                        list = dishVOList.toArray(new DishVO[0]);
                    } catch (Exception e) {
                        log.error("从 Redis 中转换 DishVO 列表时出错", e);
                    }
                }
                if (list.length == 0) {
                    // 查询数据库
                    list = userService.getDishAndFlavorsByCategoryId(categoryId);
                    if (list.length > 0) {
                        try {
                            redisTemplate.opsForValue().set(key, list);
                        } catch (Exception e) {
                            log.error("存储 DishVO 列表到 Redis 时出错", e);
                        }
                    }
                }
            }finally {
                redisLockUtil.releaseLock(stringRedisTemplate, lockKey, lockValue);
            }
        }else{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("线程等待时出错", e);
            }
            return getDishAndFlavorsByCategoryId(categoryId);
        }
        return Result.success(list);
    }

    /**
     * 根据分类中套餐分类的id查询套餐信息
     * @param categoryId
     * @return
     */
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId")
    @GetMapping("/user/setmeal/list")
    public Result<Setmeal[]> getSetmealByCategoryId(Long categoryId){
        log.info("用户根据分类中套餐分类的id查询套餐信息{}",categoryId);
        Setmeal[] setmeals=setmealService.getSetmealByCategoryId(categoryId);
        return Result.success(setmeals);
    }

    /**
     * 根据套餐id查询对应的菜品信息
     * @param id
     * @return
     */
    @GetMapping("/user/setmeal/dish/{id}")
    public Result<DishItemVO[]> getDishBySetmealId(@PathVariable Long id){
        log.info("根据套餐id查询对应的菜品信息{}",id);
        return Result.success(setmealDishService.getDishBySetmealId(id));
    }
}
