package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.dto.RiderPasswordLoginDTO;
import com.sky.dto.RiderRegisterDTO;
import com.sky.dto.RiderWeixinLoginDTO;
import com.sky.entity.Orders;
import com.sky.entity.Rider;
import com.sky.exception.AccountAlreadyExitException;
import com.sky.exception.AccountNotExit;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.RiderMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.RiderService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.RiderLoginVO;
import com.sky.vo.RiderProfileVO;
import com.sky.vo.RiderRegisterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 14:10
 */
@Slf4j
@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    private RiderMapper riderMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private WeChatProperties weChatProperties;

    private static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 骑手密码登录
     * @param riderPasswordLoginDTO
     */
    @Override
    public RiderLoginVO riderPasswordLogin(RiderPasswordLoginDTO riderPasswordLoginDTO) {
        //先判断有没有这个用户
        Rider rider=riderMapper.selectByPhone(riderPasswordLoginDTO.getPhone());
        if(rider==null){
            throw new AccountNotExit(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //校验密码 TO完成DO 注册功能开发后密码需加密校验
        String password =DigestUtils.md5DigestAsHex(riderPasswordLoginDTO.getPassword().getBytes());
        if(!password.equals(rider.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //分配Token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.RIDER_ID,rider.getId());
        String token = JwtUtil.createJWT(jwtProperties.getRiderSecretKey(), jwtProperties.getRiderTtl(), claims);
        RiderLoginVO riderLoginVO = RiderLoginVO.builder()
                .id(rider.getId())
                .token(token)
                .build();
        return riderLoginVO;
    }

    /**
     * 骑手微信登录
     * @param riderWeixinLoginDTO
     * @return
     */
    @Override
    public RiderLoginVO riderWeixinLogin(RiderWeixinLoginDTO riderWeixinLoginDTO) {
        //根据code获取openid
        String openid=getopenid(riderWeixinLoginDTO.getCode());
        //判断有没有该骑手
        Rider rider=riderMapper.selectByOpenid(openid);
        if(rider == null){
            rider=Rider.builder()
                    .openid(openid)
                    .avatar(riderWeixinLoginDTO.getAvatar())
                    .name(riderWeixinLoginDTO.getName())
                    .registerTime(LocalDateTime.now())
                    .build();
            rider.setName("微信用户"+rider.getId());
            riderMapper.insert(rider);
        }
        //生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.RIDER_ID,rider.getId());
        String token = JwtUtil.createJWT(jwtProperties.getRiderSecretKey(), jwtProperties.getRiderTtl(), claims);
        //返回结果
        return RiderLoginVO.builder()
                .id(rider.getId())
                .openid(openid)
                .token(token)
                .build();
    }

    /**
     * 骑手注册
     * @param riderRegisterDTO
     * @return
     */
    @Override
    public RiderRegisterVO riderRegister(RiderRegisterDTO riderRegisterDTO) {
        //判断手机号是否已经注册
        Rider rider=riderMapper.selectByPhone(riderRegisterDTO.getPhone());
        if(rider!=null){
            throw new AccountAlreadyExitException(MessageConstant.ACCOUNT_ALREADY_EXIT);
        }
        //注册
        //密码需要加密
        rider = new Rider();
        BeanUtils.copyProperties(riderRegisterDTO,rider);
        rider.setPassword(DigestUtils.md5DigestAsHex(rider.getPassword().getBytes()));
        rider.setRegisterTime(LocalDateTime.now());
        riderMapper.insert(rider);
        //返回注册结果
        return RiderRegisterVO.builder()
                .id(rider.getId())
                .build();
    }

    /**
     * 调用微信api获取openid
     * @param code
     * @return
     */
    private String getopenid(String code) {
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }

    /**
     * 骑手简要信息查询
     * @param riderId
     * @return
     */
    @Override
    public RiderProfileVO riderProfile(Long riderId) {
        Rider rider=riderMapper.selectById(riderId);
        RiderProfileVO riderProfileVO =new RiderProfileVO();
        BeanUtils.copyProperties(rider,riderProfileVO);
        //查询骑手今日收益和月收益
        LocalDate now = LocalDate.now();
        LocalDateTime begin = now.atStartOfDay();
        LocalDateTime end = now.plusDays(1).atStartOfDay();
        Map dayMap = new HashMap();
        dayMap.put("begin",begin);
        dayMap.put("end",end);
        dayMap.put("riderId",riderId);
        BigDecimal todayIncome = orderMapper.sumRiderTodayIncome(dayMap);
        todayIncome = todayIncome == null ? new BigDecimal(0) : todayIncome;

        YearMonth currentMonth = YearMonth.now();
        LocalDate beginOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.plusMonths(1).atDay(1).minusDays(1);
        Map<String, Object> monthMap = new HashMap<>();
        monthMap.put("begin", beginOfMonth);
        monthMap.put("end", endOfMonth);
        monthMap.put("riderId", riderId);
        BigDecimal monthIncome = orderMapper.sumRiderTodayIncome(monthMap);
        riderProfileVO.setTodayIncome(todayIncome);
        riderProfileVO.setMonthIncome(monthIncome);
        return riderProfileVO;
    }

    /**
     * 骑手详情信息查询
     * @param riderId
     * @return
     */
    @Override
    public Rider riderDetail(Long riderId) {
        Rider rider = riderMapper.selectById(riderId);
        rider.setPassword("******");
        rider.setOpenid("******");
        return rider;
    }
}
