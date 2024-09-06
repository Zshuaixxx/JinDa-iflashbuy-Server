package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 帅的被人砍
 * @create 2024-09-06 17:58
 */
@Mapper
public interface UserMapper {
    /**
     * 通过openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User getUserByOpenid(String openid);

    /**
     * 向数据库添加用户
     * @param newuser
     */
    void addNewUser(User newuser);
}
