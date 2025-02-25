package com.sky.controller.admin;

import com.sky.annotation.Log;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 管理端登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO, HttpServletRequest request) {
        log.info("员工登录：{}", employeeLoginDTO);

        // 获取http请求中可获取的AdminLoginLog信息
        String userAgent = request.getHeader("User-Agent");
        String loginIp = getClientIp(request);

        Employee employee = employeeService.login(employeeLoginDTO,loginIp,userAgent);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
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

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
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
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @Log
    @PostMapping()
    private Result addEmp(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工{}",employeeDTO);
        employeeService.addEmp(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> pageViewEmp(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询员工参数{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.pageViewEmp(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 设置员工状态
     * @param status
     * @param id
     * @return
     */
    @Log
    @PostMapping("/status/{status}")
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
    @Log
    @PutMapping
    public Result updateEmpInfo(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑用户信息{}",employeeDTO);
        employeeService.updateEmpInfo(employeeDTO);
        return Result.success();
    }
}
