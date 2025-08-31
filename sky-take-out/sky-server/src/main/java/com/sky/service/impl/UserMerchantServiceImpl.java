package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.UserMerchantPageQueryDTO;
import com.sky.entity.Merchant;
import com.sky.mapper.MerchantMapper;
import com.sky.result.PageResult;
import com.sky.service.UserMerchantService;
import com.sky.vo.MerchantVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户端商家服务实现类
 */
@Service
@Slf4j
public class UserMerchantServiceImpl implements UserMerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 用户端分页查询商家列表
     * @param merchantPageQueryDTO 分页查询条件
     * @return 商家列表分页结果
     */
    @Override
    public PageResult pageQuery(UserMerchantPageQueryDTO merchantPageQueryDTO) {
        log.info("用户端分页查询商家列表: {}", merchantPageQueryDTO);

        // 构建分页对象
        Page<Merchant> page = new Page<>(merchantPageQueryDTO.getPage(), merchantPageQueryDTO.getPageSize());

        // 构建查询条件
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        // 只查询启用状态的商家
        queryWrapper.eq("status", 1);

        // 关键词搜索
        if (StringUtils.hasText(merchantPageQueryDTO.getKeyword())) {
            queryWrapper.like("name", merchantPageQueryDTO.getKeyword());
        }

        // 排序
        String sortBy = merchantPageQueryDTO.getSortBy();
        if ("distance".equals(sortBy) && 
            merchantPageQueryDTO.getLatitude() != null && 
            merchantPageQueryDTO.getLongitude() != null) {
            // TODO:距离排序优化
            // 按距离排序
            // 使用MySQL的空间函数计算距离并排序
            // 假设location字段存储格式为"纬度,经度"
            String distanceExpression = String.format(
                "ST_Distance_Sphere(POINT(%f, %f), POINT(SUBSTRING_INDEX(location, ',', -1), SUBSTRING_INDEX(location, ',', 1)))",
                merchantPageQueryDTO.getLongitude(), merchantPageQueryDTO.getLatitude());
            queryWrapper.orderByAsc(distanceExpression);
        } else if ("time".equals(sortBy)) {
            // 按时间排序
            queryWrapper.orderByDesc("create_time");
        } else {
            // 默认按ID排序
            queryWrapper.orderByDesc("id");
        }

        // 执行分页查询
        merchantMapper.selectPage(page, queryWrapper);

        // 转换为VO对象
        List<MerchantVO> merchantVOList = page.getRecords().stream().map(merchant -> {
            MerchantVO merchantVO = new MerchantVO();
            BeanUtils.copyProperties(merchant, merchantVO);
            // 将地址信息复制到描述字段
            merchantVO.setDescription(merchant.getAddress());
            // 设置默认配送时间
            merchantVO.setDeliveryTime(30);
            return merchantVO;
        }).collect(Collectors.toList());

        // 封装分页结果
        return new PageResult(page.getTotal(), merchantVOList);
    }
}