package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.MerchantDTO;
import com.sky.dto.MerchantPageQueryDTO;
import com.sky.entity.Merchant;
import com.sky.mapper.MerchantMapper;
import com.sky.result.PageResult;
import com.sky.service.MerchantService;
import com.sky.context.EmployeeContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商家服务实现类
 */
@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 商家注册
     * @param merchantDTO 商家注册信息
     */
    @Override
    public void register(MerchantDTO merchantDTO) {
        log.info("商家注册: {}", merchantDTO);

        // 检查商家名称是否已存在
        Merchant merchant = merchantMapper.getMerchantByName(merchantDTO.getName());
        if (merchant != null) {
            throw new RuntimeException("商家名称已存在");
        }

        // 创建商家实体
        Merchant newMerchant = new Merchant();
        BeanUtils.copyProperties(merchantDTO, newMerchant);

        // 设置初始状态为未审核
        newMerchant.setStatus(0);

        // 设置创建时间和更新时间
        newMerchant.setCreateTime(LocalDateTime.now());
        newMerchant.setUpdateTime(LocalDateTime.now());

        // 设置创建人和更新人
        newMerchant.setCreateUser(EmployeeContext.getCurrentId());
        newMerchant.setUpdateUser(EmployeeContext.getCurrentId());

        // 插入数据库
        merchantMapper.insert(newMerchant);
    }

    /**
     * 分页查询商家
     * @param merchantPageQueryDTO 分页查询条件
     * @return 分页结果
     */
    @Override
    public PageResult pageQuery(MerchantPageQueryDTO merchantPageQueryDTO) {
        log.info("分页查询商家: {}", merchantPageQueryDTO);

        // 构建分页对象
        Page<Merchant> page = new Page<>(merchantPageQueryDTO.getPage(), merchantPageQueryDTO.getPageSize());

        // 构建查询条件
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(merchantPageQueryDTO.getName())) {
            queryWrapper.like("name", merchantPageQueryDTO.getName());
        }

        if (merchantPageQueryDTO.getStatus() != null) {
            queryWrapper.eq("status", merchantPageQueryDTO.getStatus());
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc("create_time");

        // 执行分页查询
        merchantMapper.selectPage(page, queryWrapper);

        // 封装分页结果
        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 根据ID查询商家
     * @param id 商家ID
     * @return 商家详情
     */
    @Override
    public Merchant getById(Long id) {
        log.info("根据ID查询商家: {}", id);
        return merchantMapper.getMerchantById(id);
    }

    /**
     * 更新商家信息
     * @param merchantDTO 商家更新信息
     */
    @Override
    public void update(MerchantDTO merchantDTO) {
        log.info("更新商家信息: {}", merchantDTO);

        // 检查商家是否存在
        Merchant merchant = merchantMapper.getMerchantById(merchantDTO.getId());
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }

        // 更新商家信息
        BeanUtils.copyProperties(merchantDTO, merchant, "status");

        // 更新更新时间和更新人
        merchant.setUpdateTime(LocalDateTime.now());
        merchant.setUpdateUser(EmployeeContext.getCurrentId());

        // 执行更新
        merchantMapper.updateById(merchant);
    }

    /**
     * 更新商家状态
     * @param id 商家ID
     * @param status 商家状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        log.info("更新商家状态: id={}, status={}", id, status);

        // 检查商家是否存在
        Merchant merchant = merchantMapper.getMerchantById(id);
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }

        // 更新状态
        merchant.setStatus(status);
        merchant.setUpdateTime(LocalDateTime.now());
        merchant.setUpdateUser(EmployeeContext.getCurrentId());

        // 执行更新
        merchantMapper.updateById(merchant);
    }

    /**
     * 根据状态查询商家列表
     * @param status 商家状态
     * @return 商家列表
     */
    @Override
    public List<Merchant> getByStatus(Integer status) {
        log.info("根据状态查询商家列表: status={}", status);
        return merchantMapper.getMerchantListByStatus(status);
    }

    /**
     * 上传商家营业执照
     * @param id 商家ID
     * @param businessLicense 营业执照图片地址
     */
    @Override
    public void uploadBusinessLicense(Long id, String businessLicense) {
        log.info("上传商家营业执照: id={}, businessLicense={}", id, businessLicense);

        // 检查商家是否存在
        Merchant merchant = merchantMapper.getMerchantById(id);
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }

        // 更新营业执照
        merchant.setBusinessLicense(businessLicense);
        merchant.setUpdateTime(LocalDateTime.now());
        merchant.setUpdateUser(EmployeeContext.getCurrentId());

        // 执行更新
        merchantMapper.updateById(merchant);
    }

}