package com.sky.controller.rider;

/**
 * @author 帅的被人砍
 * @create 2024-10-13 13:37
 */

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 骑手端 平台相关接口
 */
@Slf4j
@RestController("riderPlatformController")
public class PlatformController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取关于我们信息
     * @return
     */
    @GetMapping("/rider/aboutUs")
    public Result<String> getAboutUs(){
        log.info("获取关于我们信息");
        String aboutUs = (String) redisTemplate.opsForValue().get("aboutUs");
        return Result.success(aboutUs);
    }
}
