package com.sky.exception;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 14:50
 */

/**
 * 账号不存在异常
 */
public class AccountNotExit extends BaseException {
    public AccountNotExit() {
    }
    public AccountNotExit(String message) {
        super(message);
    }
}
