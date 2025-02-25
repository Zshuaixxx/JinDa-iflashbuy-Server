package com.sky.mapper;

import com.sky.entity.AdminLoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 帅的被人砍
 * @create 2025-02-25 20:32
 */
@Mapper
public interface AdminLoginLogMapper {
    @Insert("insert into admin_login_log (admin_id,login_time,login_ip,device_info,login_status) " +
            "values (#{adminId},#{loginTime},#{ip},#{deviceInfo},#{loginStatus})")
    void insertLog(AdminLoginLog adminLoginLog);
}
