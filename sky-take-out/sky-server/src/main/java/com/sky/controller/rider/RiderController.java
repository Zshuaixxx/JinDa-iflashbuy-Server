package com.sky.controller.rider;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 13:59
 */

import com.sky.dto.RiderPasswordLoginDTO;
import com.sky.dto.RiderRegisterDTO;
import com.sky.dto.RiderWeixinLoginDTO;
import com.sky.result.Result;
import com.sky.service.RiderService;
import com.sky.vo.RiderLoginVO;
import com.sky.vo.RiderProfileVO;
import com.sky.vo.RiderRegisterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 骑手微信登录接口
     * @param riderWeixinLoginDTO 包含微信登录必要信息的请求体数据传输对象
     * @return 返回包含骑手登录信息的Result对象，其中数据对象为RiderLoginVO类型
     */
    @PostMapping("/rider/login/weixin")
    public Result<RiderLoginVO> riderWeixinLogin(@RequestBody RiderWeixinLoginDTO riderWeixinLoginDTO){
        log.info("骑手微信登录：{}",riderWeixinLoginDTO);
        RiderLoginVO riderLoginVO=riderService.riderWeixinLogin(riderWeixinLoginDTO);
        return Result.success(riderLoginVO);
    }

    /**
     * 骑手注册接口
     * @param riderRegisterDTO 骑手注册请求数据传输对象，包含骑手注册所需的信息
     * @return 返回一个Result对象，封装了骑手注册操作的结果和一个RiderRegisterVO对象，其中包含注册后的数据
     */
    @PostMapping("/rider/register")
    public Result<RiderRegisterVO> riderRegister(@RequestBody RiderRegisterDTO riderRegisterDTO){
        log.info("骑手注册：{}",riderRegisterDTO);
        return Result.success(riderService.riderRegister(riderRegisterDTO));
    }

    /**
     * 骑手简要信息查询接口
     * @param riderId
     * @return
     */
    @GetMapping("/rider/riderProfile/{riderId}")
    public Result<RiderProfileVO> userProfile(@PathVariable Long riderId){
        RiderProfileVO riderProfileVO=riderService.riderProfile(riderId);
        return Result.success(riderProfileVO);
    }
}
