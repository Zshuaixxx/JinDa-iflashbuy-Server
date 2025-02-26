package com.sky.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.dto.EmployeeLoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.concurrent.TimeUnit;

/**
 * @author 帅的被人砍
 * @create 2025-02-26 13:56
 */

/**
 * 管理端登录拦截器
 */
@Component
@Slf4j
public class AdminLoginInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUEST_COUNT = 5; // 最大请求次数
    private static final long TIME_WINDOW = 60; // 时间窗口（秒）

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 拦截管理端登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = null;
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            try (BufferedReader reader = request.getReader()) {
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                EmployeeLoginDTO employeeLoginDTO = objectMapper.readValue(json.toString(), EmployeeLoginDTO.class);
                username = employeeLoginDTO.getUsername();
            }
        }
        if (username != null) {
            String key = "admin_login_request:" + username;
            // 指定 RedisTemplate 的泛型类型
            RedisTemplate<String, Integer> typedRedisTemplate = redisTemplate;
            Integer count = typedRedisTemplate.opsForValue().get(key);
            log.info("拦截管理端登录：{},{}", username, count);
            if (count == null) {
                // 首次请求，设置请求次数为 1，并设置过期时间
                typedRedisTemplate.opsForValue().set(key, 1, TIME_WINDOW, TimeUnit.SECONDS);
            } else if (count < MAX_REQUEST_COUNT) {
                // 未超过最大请求次数，增加请求次数
                typedRedisTemplate.opsForValue().increment(key);
            } else {
                // 超过最大请求次数，返回错误信息
                response.setStatus(429);
                return false;
            }
        }
        return true;
    }
}
