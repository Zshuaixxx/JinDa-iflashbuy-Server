package com.sky.controller.admin;

/**
 * @author 帅的被人砍
 * @create 2024-10-13 13:27
 */

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端 平台管理相关接口
 */
@RestController("adminPlatformController")
@Slf4j
public class PlatformController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置‘关于我们’信息
     * @param content
     * @return
     */
    @PostMapping("/admin/aboutUs")
    public Result setAboutUs(@RequestBody  String content) {
        log.info("设置关于我们信息：{}",content);
        redisTemplate.opsForValue().set("aboutUs",content);
        return Result.success();
    }


}
