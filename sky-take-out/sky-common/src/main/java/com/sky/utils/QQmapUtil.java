package com.sky.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.exception.AddressNotExit;
import com.sky.properties.QQmapProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 帅的被人砍
 * @create 2024-09-22 10:42
 */

/**
 * 腾讯地图工具类
 */
@Component
@Slf4j
public class QQmapUtil {

    @Autowired
    private QQmapProperties qqmapProperties;

    //地址转经纬度URL
    private static final String getLocationURL="https://apis.map.qq.com/ws/geocoder/v1/";
    //距离矩阵运算URL
    private static final String getDistanceMatrixURL="https://apis.map.qq.com/ws/distance/v1/matrix";

    /**
     * 根据地址获取经纬度 和行政区划编码
     * @param address
     * @return
     */
    public List<String> getLocation(String address){
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("key",qqmapProperties.getKey());
        paramMap.put("address",address);
        String jsonString=HttpClientUtil.doGet(getLocationURL,paramMap);
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        log.info("腾讯地图返回的经纬度信息{}",jsonObject);
        if(jsonObject.getInteger("status")==0) {
            String location= jsonObject.getJSONObject("result").getJSONObject("location").getString("lat")+","+
                    jsonObject.getJSONObject("result").getJSONObject("location").getString("lng");
            String adcode=jsonObject.getJSONObject("result").getJSONObject("ad_info").getString("adcode");
            return List.of(location,adcode);
        }else {
            throw new AddressNotExit(MessageConstant.ADDRESS_NOT_EXIT);
        }
    }

    /**
     * 批量计算距离和时间
     */
    public List<List<Double>> getDistance(String from,String to,String mode){
        List<List<Double>> answer=new ArrayList<>();
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("key",qqmapProperties.getKey());
        paramMap.put("from",from);
        paramMap.put("to",to);
        paramMap.put("mode",mode);
        String jsonString=HttpClientUtil.doGet(getDistanceMatrixURL,paramMap);
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        log.info("腾讯地图返回的距离和时间信息{}",jsonObject);
        if(jsonObject.getInteger("status")==0) {
            JSONArray elements = jsonObject.getJSONObject("result").getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
            Object[] array = elements.toArray();
            for (Object element : array) {
                JSONObject jsonObject1 = (JSONObject) element;
                answer.add(List.of(jsonObject1.getDouble("distance")));
            }
        }
        log.info("腾讯地图矩阵距离计算的结果{}",answer);
        return answer;
    }
}
