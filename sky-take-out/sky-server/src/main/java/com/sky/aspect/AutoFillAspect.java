package com.sky.aspect;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 21:32
 */

import com.sky.annotation.AutoFill;
import com.sky.context.EmployeeContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;


/**
 * 自定义切面类， 实现公共字段的填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    //前置通知
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始自动填充公共字段{}",joinPoint.getSignature().getName());

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature =(MethodSignature)joinPoint.getSignature();//方法签名对象
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType=autoFill.value();//获得数据库操作类型
        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) {
            return;
        }

        Object entity= args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId= EmployeeContext.getCurrentId();
        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType==OperationType.INSERT) {
            //为4个公共字段赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now) ;
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity,now) ;
                setUpdateUser.invoke(entity,currentId) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE) {
            //为2个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity,now) ;
                setUpdateUser.invoke(entity,currentId) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
