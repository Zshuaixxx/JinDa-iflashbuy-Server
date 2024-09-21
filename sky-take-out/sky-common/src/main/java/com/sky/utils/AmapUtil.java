package com.sky.utils;

/**
 * @author 帅的被人砍
 * @create 2024-09-21 15:54
 */

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.exception.AddressNotExit;
import com.sky.properties.AmapProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 高德地图工具类
 */
@Slf4j
@Component
public class AmapUtil {

    @Autowired
    private AmapProperties amapProperties;

    public static String getLocationURL="https://restapi.amap.com/v3/geocode/geo";

    /**
     * 根据地理信息获取经纬度
     */
    public  String getLocation(String address) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("key",amapProperties.getKey());
        paramMap.put("address",address);
        String jsonString = HttpClientUtil.doGet(getLocationURL, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        log.info("高德地图返回的经纬度信息{}",jsonObject);
        if(jsonObject.getInteger("status")==1){
            return jsonObject.getJSONArray("geocodes").getJSONObject(0).getString("location");
        }else{
            throw new AddressNotExit(MessageConstant.ADDRESS_NOT_EXIT);
        }
    }
}
