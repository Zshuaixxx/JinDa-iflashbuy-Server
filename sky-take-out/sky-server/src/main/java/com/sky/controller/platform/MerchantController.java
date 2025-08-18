package com.sky.controller.platform;

import com.sky.constant.MessageConstant;
import com.sky.dto.MerchantDTO;
import com.sky.dto.MerchantPageQueryDTO;
import com.sky.dto.MerchantWithEmployeeDTO;
import com.sky.entity.Merchant;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商家管理控制器
 */
@RestController
@RequestMapping("/platform/merchant")
@Api(tags = "商家管理接口")
@Slf4j
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    /**
     * 商家注册
     * @param merchantWithEmployeeDTO 商家注册信息和默认员工信息
     * @return 操作结果
     */
    @PostMapping("/register")
    @ApiOperation("新增商家和默认员工账号")
    public Result register(@RequestBody MerchantWithEmployeeDTO merchantWithEmployeeDTO) {
        log.info("新增商家和默认员工账号: {}", merchantWithEmployeeDTO);
        merchantService.registerWithDefaultEmployee(merchantWithEmployeeDTO);
        return Result.success();
    }

    /**
     * 分页查询商家
     * @param merchantPageQueryDTO 分页查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @ApiOperation("分页查询商家")
    public Result<PageResult> pageQuery(MerchantPageQueryDTO merchantPageQueryDTO) {
        log.info("分页查询商家: {}", merchantPageQueryDTO);
        PageResult pageResult = merchantService.pageQuery(merchantPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询商家
     * @param id 商家ID
     * @return 商家详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询商家")
    public Result<Merchant> getById(@PathVariable Long id) {
        log.info("根据ID查询商家: {}", id);
        Merchant merchant = merchantService.getById(id);
        return Result.success(merchant);
    }

    /**
     * 更新商家信息
     * @param merchantDTO 商家更新信息
     * @return 操作结果
     */
    @PutMapping
    @ApiOperation("更新商家信息")
    public Result update(@RequestBody MerchantDTO merchantDTO) {
        log.info("更新商家信息: {}", merchantDTO);
        merchantService.update(merchantDTO);
        return Result.success();
    }

    /**
     * 更新商家状态
     * @param id 商家ID
     * @param status 商家状态
     * @return 操作结果
     */
    @PutMapping("/status/{id}")
    @ApiOperation("更新商家状态")
    public Result updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("更新商家状态: id={}, status={}", id, status);
        merchantService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 根据状态查询商家列表
     * @param status 商家状态
     * @return 商家列表
     */
    @GetMapping("/list")
    @ApiOperation("根据状态查询商家列表")
    public Result<List<Merchant>> getByStatus(@RequestParam Integer status) {
        log.info("根据状态查询商家列表: status={}", status);
        List<Merchant> merchantList = merchantService.getByStatus(status);
        return Result.success(merchantList);
    }

    /**
     * 上传商家营业执照
     * @param id 商家ID
     * @param file 营业执照图片文件
     * @return 操作结果
     */
    @PostMapping("/{id}/business-license")
    @ApiOperation("上传商家营业执照")
    public Result uploadBusinessLicense(@PathVariable Long id, @RequestParam MultipartFile file) {
        log.info("上传商家营业执照: id={}", id);
        // 这里简化处理，实际项目中需要上传文件到文件服务器
        String businessLicense = "https://example.com/business-license/" + id + ".jpg";
        merchantService.uploadBusinessLicense(id, businessLicense);
        return Result.success();
    }

}