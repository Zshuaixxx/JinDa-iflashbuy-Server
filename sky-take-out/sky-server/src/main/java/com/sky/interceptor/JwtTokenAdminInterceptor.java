package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.EmployeeContext;
import com.sky.context.MerchantContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器 管理端
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId;
            if(claims.get(JwtClaimsConstant.EMP_ID) != null){
                empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            }else{
                 empId = Long.valueOf(claims.get(JwtClaimsConstant.RIDER_ID).toString());
            }
            log.info("当前员工id：{}", empId);

            // 获取商家ID并存储到MerchantContext中
            Long merchantId = null;
            if (claims.get(JwtClaimsConstant.MERCHANT_ID) != null) {
                merchantId = Long.valueOf(claims.get(JwtClaimsConstant.MERCHANT_ID).toString());
                log.info("当前商家id：{}", merchantId);
                MerchantContext.setCurrentId(merchantId);
            }

            //查询该id在redis中对应的token是否正确
            String redisToken = (String) redisTemplate.opsForValue().get(JwtClaimsConstant.LOGIN_ADMIN_ID+empId);
            if(redisToken == null){
                //登录状态过期
                response.setStatus(401);
                return false;
            }else if(!redisToken.equals(token)){
                //账号在其他设备登录
                response.setStatus(402);
                return false;
            }else{
                //正确登录状态
                // 将当前id存到线程中
                EmployeeContext.setCurrentId(empId);
                //3、通过，放行
                return true;
            }


        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}