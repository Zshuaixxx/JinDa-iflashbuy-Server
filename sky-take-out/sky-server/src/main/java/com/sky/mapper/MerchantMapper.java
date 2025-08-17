package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商家Mapper接口
 */
@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {

    /**
     * 根据状态查询商家列表
     * @param status 商家状态
     * @return 商家列表
     */
    List<Merchant> getMerchantListByStatus(Integer status);

    /**
     * 根据ID查询商家详情
     * @param id 商家ID
     * @return 商家详情
     */
    Merchant getMerchantById(Long id);

    /**
     * 根据商家名称查询商家
     * @param name 商家名称
     * @return 商家信息
     */
    Merchant getMerchantByName(String name);

    /**
     * 更新商家状态
     * @param id 商家ID
     * @param status 商家状态
     * @return 影响行数
     */
    int updateMerchantStatus(Long id, Integer status);

}