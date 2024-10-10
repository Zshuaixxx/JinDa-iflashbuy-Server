package com.sky.mapper;

//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Rider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 14:30
 */

/**
 * 骑手
 */
@Mapper
//public interface RiderMapper extends BaseMapper<Rider> {
public interface RiderMapper  {

    /**
     * 通过phone查询骑手
     * @param phone
     * @return
     */
    @Select("select * from rider where phone=#{phone}")
    Rider selectByPhone(String phone);

    /**
     * 根据openid查询骑手
     * @param openid
     * @return
     */
    @Select("select * from rider where openid=#{openid}")
    Rider selectByOpenid(String openid);

    /**
     * 骑手表新增
     * @param rider
     */
    void insert(Rider rider);

    /**
     * 根据id查询骑手
     * @param riderId
     * @return
     */
    Rider selectById(Long riderId);
}
