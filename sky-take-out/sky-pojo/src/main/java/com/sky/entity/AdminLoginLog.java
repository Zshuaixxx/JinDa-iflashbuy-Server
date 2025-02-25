package com.sky.entity;

/**
 * @author 帅的被人砍
 * @create 2025-02-25 19:59
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员登录日志
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminLoginLog {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 管理员id 外键
     */
    private Long adminId;
    /**
     * 登录ip地址
     */
    private String ip;
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 登录设备信息
     */
    private String deviceInfo;
    /**
     * 登录状态
     */
    private boolean loginStatus;
}
