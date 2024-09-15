package com.sky.aspect;

import com.alibaba.fastjson.JSON;
import com.sky.context.BaseContext;
import com.sky.entity.OperateLog;
import com.sky.mapper.OperateLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 帅的被人砍
 * @create 2024-09-15 10:37
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.sky.annotation.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("开始记录日志");

        //获取方法类名
        String operateClass = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String operateMethod = joinPoint.getSignature().getName();
        //获取方法参数
        String operateParams = JSON.toJSONString(joinPoint.getArgs());

        long begin= System.currentTimeMillis();
        //执行目标方法
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        //获取方法返回值
        String methodResult = JSON.toJSONString(result);
        OperateLog operateLog = OperateLog.builder()
                .employeeId(BaseContext.getCurrentId())
                .operateTime(LocalDateTime.now())
                .operateClass(operateClass)
                .operateMethod(operateMethod)
                .operateParams(operateParams)
                .methodResult(methodResult)
                .costTime((int) (end - begin))
                .build();

        //将日志记录到数据库
        operateLogMapper.insertLog(operateLog);

        return result;
    }
}
