package com.sky.controller.merchant;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.entity.Merchant;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.AdminLoginLogService;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import com.sky.context.MerchantContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private com.sky.mapper.MerchantMapper merchantMapper;

    private static final int MAX_REQUEST_COUNT = 5; // 最大请求次数
    private static final long TIME_WINDOW = 60; // 时间窗口（秒）
    /**
     * 管理端登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO, HttpServletRequest request) {
        log.info("员工登录：{}", employeeLoginDTO);

        if (employeeLoginDTO.getUsername() != null) {
            String key = "admin_login_request:" + employeeLoginDTO.getUsername();
            // 指定 RedisTemplate 的泛型类型
            RedisTemplate<String, Integer> typedRedisTemplate = redisTemplate;
            Integer count = typedRedisTemplate.opsForValue().get(key);
            log.info("拦截管理端登录：{},{}", employeeLoginDTO.getUsername(), count);
            if (count == null) {
                // 首次请求，设置请求次数为 1，并设置过期时间
                typedRedisTemplate.opsForValue().set(key, 1, TIME_WINDOW, TimeUnit.SECONDS);
            } else if (count < MAX_REQUEST_COUNT) {
                // 未超过最大请求次数，增加请求次数
                typedRedisTemplate.opsForValue().increment(key);
            } else {
                // 超过最大请求次数，返回错误信息
                return Result.error(MessageConstant.TOO_MANY_REQUESTS);
            }
        }else{
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 获取http请求中可获取的AdminLoginLog信息
        String userAgent = request.getHeader("User-Agent");
        String loginIp = getClientIp(request);

        Employee employee = employeeService.login(employeeLoginDTO,loginIp,userAgent);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(JwtClaimsConstant.MERCHANT_ID, employee.getMerchantId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //将token储存到redis中并设置过期时间
        redisTemplate.opsForValue().set(
                JwtClaimsConstant.LOGIN_ADMIN_ID + employee.getId(),
                token,
                jwtProperties.getAdminTtl()/1000,
                TimeUnit.SECONDS
        );

        // 获取商家信息
        Merchant merchant = merchantMapper.getMerchantById(employee.getMerchantId());

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .merchantId(employee.getMerchantId())
                .merchantName(merchant.getName())
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 获取当前登录http请求中登录用户的ip地址
     * @param request http请求
     * @return ip
     */
    private String getClientIp(HttpServletRequest request) {
        String xffHeader = request.getHeader("X-Forwarded-For");
        if (xffHeader == null) {
            return request.getRemoteAddr();
        }
        // 如果有多个 IP 地址，第一个 IP 是客户端的真实 IP
        return xffHeader.split(",")[0].trim();
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "新增员工")
    public Result addEmp(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工{}",employeeDTO);
        // 从MerchantContext中获取商家ID
        employeeDTO.setMerchantId(MerchantContext.getCurrentId());
        employeeService.addEmp(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询")
    public Result<PageResult> pageViewEmp(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询员工参数{}",employeePageQueryDTO);
        // 设置商家ID
        employeePageQueryDTO.setMerchantId(MerchantContext.getCurrentId());
        PageResult pageResult=employeeService.pageViewEmp(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 设置员工状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启用禁用员工账号")
    public Result setEmpStatus(@PathVariable Integer status ,Long id){
        employeeService.setEmpStatus(status,id);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询员工信息")
    public Result<Employee> getEmpById(@PathVariable Long id){
        log.info("根据id查询员工信息{}",id);
        Employee employee=employeeService.getEmpById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "编辑员工信息")
    public Result updateEmpInfo(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑用户信息{}",employeeDTO);
        employeeService.updateEmpInfo(employeeDTO);
        return Result.success();
    }
    
}