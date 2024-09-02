package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

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
    void updataEmp(Employee employee);
}
