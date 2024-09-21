package com.sky.exception;

/**
 * @author 帅的被人砍
 * @create 2024-09-21 10:42
 */

/**
 * 账号已经存在异常
 */
public class AccountAlreadyExitException extends BaseException {
    public AccountAlreadyExitException() {}

    public AccountAlreadyExitException(String message) {
        super(message);
    }
}
