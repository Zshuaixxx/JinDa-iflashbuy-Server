package com.sky.properties;

/**
 * @author 帅的被人砍
 * @create 2024-09-21 15:49
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 高德地图配置类
 */
@Component
@ConfigurationProperties(prefix = "sky.amap")
@Data
public class AmapProperties {
    //应用密钥key
    private String key;
}
