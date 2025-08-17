package com.sky.service;

import com.sky.dto.MerchantDTO;
import com.sky.dto.MerchantPageQueryDTO;
import com.sky.entity.Merchant;
import com.sky.result.PageResult;

import java.util.List;

/**
 * 商家服务接口
 */
public interface MerchantService {

    /**
     * 商家注册
     * @param merchantDTO 商家注册信息
     */
    void register(MerchantDTO merchantDTO);

    /**
     * 分页查询商家
     * @param merchantPageQueryDTO 分页查询条件
     * @return 分页结果
     */
    PageResult pageQuery(MerchantPageQueryDTO merchantPageQueryDTO);

    /**
     * 根据ID查询商家
     * @param id 商家ID
     * @return 商家详情
     */
    Merchant getById(Long id);

    /**
     * 更新商家信息
     * @param merchantDTO 商家更新信息
     */
    void update(MerchantDTO merchantDTO);

    /**
     * 更新商家状态
     * @param id 商家ID
     * @param status 商家状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据状态查询商家列表
     * @param status 商家状态
     * @return 商家列表
     */
    List<Merchant> getByStatus(Integer status);

    /**
     * 上传商家营业执照
     * @param id 商家ID
     * @param businessLicense 营业执照图片地址
     */
    void uploadBusinessLicense(Long id, String businessLicense);

}