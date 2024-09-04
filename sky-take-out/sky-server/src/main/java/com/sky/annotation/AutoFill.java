package com.sky.annotation;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 21:28
 */

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 自定义注解 表示需要为公共字段填充的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //数据库操作类型  insert update
    OperationType value();
}
