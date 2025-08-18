package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "商家和默认员工信息")
public class MerchantWithEmployeeDTO implements Serializable {
    
    @ApiModelProperty("商家信息")
    private MerchantDTO merchantDTO;
    
    @ApiModelProperty("默认员工用户名")
    private String defaultEmployeeUsername;
    
    @ApiModelProperty("默认员工姓名")
    private String defaultEmployeeName;
    
    @ApiModelProperty("默认员工密码")
    private String defaultEmployeePassword;
    
    @ApiModelProperty("默认员工手机号")
    private String defaultEmployeePhone;
    
    @ApiModelProperty("默认员工身份证号")
    private String defaultEmployeeIdNumber;
    
    @ApiModelProperty("默认员工性别")
    private String defaultEmployeeSex;
}