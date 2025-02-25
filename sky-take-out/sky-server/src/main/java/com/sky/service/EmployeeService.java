package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @param ip
     * @param userAgent
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO,String ip,String userAgent);

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    void addEmp(EmployeeDTO employeeDTO);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageViewEmp(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 设置员工状态
     * @param status
     * @param id
     * @return
     */
    void setEmpStatus(Integer status, Long id);

    /**
     * 编辑员工
     * @param employeeDTO
     * @return
     */
    void updateEmpInfo(EmployeeDTO employeeDTO);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Employee getEmpById(Long id);
}
