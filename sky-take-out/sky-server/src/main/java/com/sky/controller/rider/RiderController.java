package com.sky.controller.rider;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 13:59
 */

import com.sky.dto.RiderPasswordLoginDTO;
import com.sky.dto.RiderWeixinLoginDTO;
import com.sky.result.Result;
import com.sky.service.RiderService;
import com.sky.vo.RiderLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 骑手相关接口
 */
@RestController
@Slf4j
public class RiderController {


    @Autowired
    private RiderService riderService;

    /**
     * 骑手密码登录
     */
    @PostMapping("/rider/login/password")
    public Result<RiderLoginVO> riderPasswordLogin(@RequestBody RiderPasswordLoginDTO riderPasswordLoginDTO){
        log.info("骑手密码登录：{}", riderPasswordLoginDTO);
        RiderLoginVO riderLoginVO=riderService.riderPasswordLogin(riderPasswordLoginDTO);
        return Result.success(riderLoginVO);
    }

    /**
     * 骑手微信登录
     * @param riderWeixinLoginDTO
     * @return
     */
    @PostMapping("/rider/login/weixin")
    public Result<RiderLoginVO> riderWeixinLogin(@RequestBody RiderWeixinLoginDTO riderWeixinLoginDTO){
        log.info("骑手微信登录：{}",riderWeixinLoginDTO);
        RiderLoginVO riderLoginVO=riderService.riderWeixinLogin(riderWeixinLoginDTO);
        return Result.success(riderLoginVO);
    }
}
