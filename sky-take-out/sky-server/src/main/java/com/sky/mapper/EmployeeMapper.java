package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据商家名称和用户名查询员工
     * @param merchantName 商家名称
     * @param username 用户名
     * @return 员工信息
     */
    @Select("SELECT e.* FROM employee e " +
            "JOIN merchant m ON e.merchant_id = m.id " +
            "WHERE m.name = #{merchantName} AND e.username = #{username}")
    Employee getByMerchantNameAndUsername(@Param("merchantName") String merchantName, @Param("username") String username);

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee(name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user)" +
            " VALUES (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void addEmp(Employee employee);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageViewEmp(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工信息
     * @param employee
     */
    @AutoFill(OperationType.UPDATE)
    void updataEmp(Employee employee);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Select("select * from employee where id=#{id}")
    Employee getEmpById(Long id);

    /**
     * 根据id和商家id查询员工信息
     * @param id 员工ID
     * @param merchantId 商家ID
     * @return 员工信息
     */
    @Select("select * from employee where id=#{id} and merchant_id=#{merchantId}")
    Employee getEmpByIdAndMerchantId(@Param("id") Long id, @Param("merchantId") Long merchantId);
}
