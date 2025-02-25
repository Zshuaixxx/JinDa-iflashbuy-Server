package com.sky.service;

import com.sky.entity.AdminLoginLog;

/**
 * @author 帅的被人砍
 * @create 2025-02-25 20:21
 */

public interface AdminLoginLogService {
    /**
     * 新增登录日志
     * @param adminLoginLog
     */
    void insertLog(AdminLoginLog adminLoginLog);
}
