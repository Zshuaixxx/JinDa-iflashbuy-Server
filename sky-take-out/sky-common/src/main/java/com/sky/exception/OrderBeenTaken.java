package com.sky.exception;

/**
 * @author 帅的被人砍
 * @create 2024-10-07 23:20
 */

/**
 * 订单已被接单异常
 */
public class OrderBeenTaken extends BaseException {
    public OrderBeenTaken() {
    }
    public OrderBeenTaken(String message) {
        super(message);
    }
}
