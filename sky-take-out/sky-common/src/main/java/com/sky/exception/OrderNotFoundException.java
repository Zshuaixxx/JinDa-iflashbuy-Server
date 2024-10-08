package com.sky.exception;

/**
 * @author 帅的被人砍
 * @create 2024-10-08 15:50
 */

/**
 * 订单不存在异常
 */
public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException() {
    }
    public OrderNotFoundException(String message) {
        super(message);
    }
}
