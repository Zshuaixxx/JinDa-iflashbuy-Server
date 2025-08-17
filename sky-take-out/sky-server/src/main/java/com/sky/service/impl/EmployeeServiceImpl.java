package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.context.MerchantContext;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.result.PageResult;
import com.sky.constant.StatusConstant;
import com.sky.context.EmployeeContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.AdminLoginLog;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.AdminLoginLogService;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private AdminLoginLogService adminLoginLogService;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @param ip
     * @param userAgent
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO,String ip,String userAgent) {
        String merchantName = employeeLoginDTO.getMerchantName();
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据商家名称和用户名查询数据库中的数据
        Employee employee = employeeMapper.getByMerchantNameAndUsername(merchantName, username);

        AdminLoginLog adminLoginLog = AdminLoginLog.builder()
                .loginTime(LocalDateTime.now())
                .ip(ip)
                .deviceInfo(userAgent)
                .loginStatus(false)
                .adminId(0L)
                .build();

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            adminLoginLogService.insertLog(adminLoginLog);
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        adminLoginLog.setAdminId(employee.getId());
        //密码比对
        // TO完成DO 后期需要进行md5加密，然后再进行比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            adminLoginLogService.insertLog(adminLoginLog);
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            adminLoginLogService.insertLog(adminLoginLog);
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        adminLoginLog.setLoginStatus(true);
        adminLoginLogService.insertLog(adminLoginLog);
        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @Override
    public void addEmp(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        // 对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        // 设置账号状态
        employee.setStatus(StatusConstant.ENABLE);
        // 设置密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        employee.setCreateTime(now);
        employee.setUpdateTime(now);
        // 设置操作人id
        employee.setCreateUser(EmployeeContext.getCurrentId());
        employee.setUpdateUser(EmployeeContext.getCurrentId());
        // 设置商家ID
        employee.setMerchantId(employeeDTO.getMerchantId());
        employeeMapper.addEmp(employee);
    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageViewEmp(EmployeePageQueryDTO employeePageQueryDTO) {
//        分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        Page<Employee> page=employeeMapper.pageViewEmp(employeePageQueryDTO);
        long total= page.getTotal();
        List<Employee> records=page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 设置员工状态
     * @param status
     * @param id
     * @return
     */
    @Override
    public void setEmpStatus(Integer status, Long id) {
        // 验证员工是否属于当前商家
        Employee employee = employeeMapper.getEmpByIdAndMerchantId(id, MerchantContext.getCurrentId());
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        
        employee = Employee.builder()
                .status(status)
                .id(id)
                .updateTime(LocalDateTime.now())
                .updateUser(EmployeeContext.getCurrentId())
                .build();
        employeeMapper.updataEmp(employee);
    }

    /**
     * 编辑员工
     * @param employeeDTO
     * @return
     */
    @Override
    public void updateEmpInfo(EmployeeDTO employeeDTO) {
        // 验证员工是否属于当前商家
        Employee employee = employeeMapper.getEmpByIdAndMerchantId(employeeDTO.getId(), MerchantContext.getCurrentId());
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        
        employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setMerchantId(employeeDTO.getMerchantId());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(EmployeeContext.getCurrentId());
        employeeMapper.updataEmp(employee);
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getEmpById(Long id) {
        // 验证员工是否属于当前商家
        Employee employee = employeeMapper.getEmpByIdAndMerchantId(id, MerchantContext.getCurrentId());
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        
        employee.setPassword("******");
        return employee;
    }
}