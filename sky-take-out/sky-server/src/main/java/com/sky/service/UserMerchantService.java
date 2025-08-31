package com.sky.service;

import com.sky.dto.UserMerchantPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.MerchantVO;

/**
 * 用户端商家服务接口
 */
public interface UserMerchantService {

    /**
     * 用户端分页查询商家列表
     * @param merchantPageQueryDTO 分页查询条件
     * @return 商家列表分页结果
     */
    PageResult pageQuery(UserMerchantPageQueryDTO merchantPageQueryDTO);
}