package com.sky.exception;

/**
 * @author 帅的被人砍
 * @create 2024-09-21 16:19
 */

/**
 * 地址不存在异常
 */
public class AddressNotExit extends BaseException {
    public AddressNotExit() {
    }
    public AddressNotExit(String message) {
        super(message);
    }
}
