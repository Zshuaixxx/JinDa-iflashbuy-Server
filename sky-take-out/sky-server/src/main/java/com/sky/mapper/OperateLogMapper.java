package com.sky.mapper;

import com.sky.entity.OperateLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 帅的被人砍
 * @create 2024-09-15 10:52
 */
@Mapper
public interface OperateLogMapper {
    /**
     * 插入一条日志到数据库
     * @param operateLog 日志实体类
     */
    void insertLog(OperateLog operateLog);
}
