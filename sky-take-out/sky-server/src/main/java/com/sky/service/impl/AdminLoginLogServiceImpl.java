package com.sky.service.impl;

import com.sky.entity.AdminLoginLog;
import com.sky.mapper.AdminLoginLogMapper;
import com.sky.service.AdminLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author 帅的被人砍
 * @create 2025-02-25 20:22
 */
@Service
public class AdminLoginLogServiceImpl implements AdminLoginLogService {
    @Autowired
    private AdminLoginLogMapper adminLoginLogMapper;
    /**
     * 新增登录日志
     *
     * @param adminLoginLog
     */
    @Async
    @Override
    public void insertLog(AdminLoginLog adminLoginLog) {
        adminLoginLogMapper.insertLog(adminLoginLog);
    }
}
