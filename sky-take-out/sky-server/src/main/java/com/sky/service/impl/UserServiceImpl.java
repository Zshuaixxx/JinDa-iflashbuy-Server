package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Dish;
import com.sky.entity.User;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 帅的被人砍
 * @create 2024-09-06 17:50
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    private static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {

        //调用微信api获取登录信息
        String openid=getopenid(userLoginDTO.getCode());

        User user=userMapper.getUserByOpenid(openid);
        //检查是否是新用户
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.addNewUser(user);
        }
        return user;
    }

    /**
     * 查询分类id下的所有菜品和对应的口味
     * @param categoryId
     * @return
     */
    @Override
    public DishVO[] getDishAndFlavorsByCategoryId(Long categoryId) {
        //先查出菜品数组
        List<Dish> dishList = dishMapper.getDishByCategoryId(categoryId);
        List<DishVO> dishVOList=new ArrayList<>();
        //循环数组查询对应的口味信息
        dishList.forEach(dish -> {
            DishVO dishvo=new DishVO();
            BeanUtils.copyProperties(dish,dishvo);
            dishvo.setFlavors(dishFlavorMapper.getFloverByDishId(dish.getId()));
            dishVOList.add(dishvo);
        });
        return dishVOList.toArray(new DishVO[0]);
    }

    /**
     * 调用微信api获取openid
     * @param code
     * @return
     */
    private String getopenid(String code) {
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }

}
