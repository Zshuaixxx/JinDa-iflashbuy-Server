package com.sky.service.impl;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.RiderPasswordLoginDTO;
import com.sky.entity.Rider;
import com.sky.exception.AccountNotExit;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.RiderMapper;
import com.sky.properties.JwtProperties;
import com.sky.service.RiderService;
import com.sky.utils.JwtUtil;
import com.sky.vo.RiderLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 14:10
 */
@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    private RiderMapper riderMapper;
    @Autowired
    private JwtProperties jwtProperties;

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
        //校验密码 TODO 注册功能开发后密码需加密校验
        if(!rider.getPassword().equals(riderPasswordLoginDTO.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //分配Token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,rider.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        RiderLoginVO riderLoginVO = RiderLoginVO.builder()
                .id(rider.getId())
                .token(token)
                .build();
        return riderLoginVO;
    }
}
