package com.sky.controller.user;

import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.UserMerchantService;
import com.sky.vo.MerchantVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sky.dto.UserMerchantPageQueryDTO;

/**
 * 用户端商家相关接口
 */
@RestController("userMerchantController")
@RequestMapping("/user/merchant")
@Api(tags = "用户端商家接口")
@Slf4j
public class MerchantController {

    @Autowired
    private UserMerchantService userMerchantService;

    /**
     * 分页查询商家列表
     *
     * @param merchantPageQueryDTO 分页查询参数
     * @return 商家列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询商家列表")
    public Result<PageResult> page(UserMerchantPageQueryDTO merchantPageQueryDTO) {
        log.info("用户端分页查询商家列表: {}", merchantPageQueryDTO);
        PageResult pageResult = userMerchantService.pageQuery(merchantPageQueryDTO);
        return Result.success(pageResult);
    }
}