package com.sky.properties;

/**
 * @author 帅的被人砍
 * @create 2024-09-22 10:38
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯地图配置类
 */
@Component
@ConfigurationProperties(prefix = "sky.qqmap")
@Data
public class QQmapProperties {
    //密钥key
    private String key;
}
