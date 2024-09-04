package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 16:20
 */
@Mapper
public interface CategoryMapper {

    /**
     * 分页查询分类信息
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> getPageCate(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param category
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user) VALUES " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void addCategory(Category category);

    /**
     * 更新分类信息
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateCate(Category category);

    /**
     * 删除分类
     * @param id
     */
    @Delete("delete from category where id=#{id}")
    void deleteCate(Long id);

    /**
     *根据类型查询分类
     * @param type
     * @return
     */
    @Select("select * from category where type=#{type}")
    Category[] getCateListByType(Integer type);
}
