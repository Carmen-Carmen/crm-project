package com.lingrui.crm.settings.mapper;

import com.lingrui.crm.settings.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Fri May 20 20:11:30 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Fri May 20 20:11:30 CST 2022
     */
    int insert(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Fri May 20 20:11:30 CST 2022
     */
    int insertSelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Fri May 20 20:11:30 CST 2022
     */
    User selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Fri May 20 20:11:30 CST 2022
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Fri May 20 20:11:30 CST 2022
     */
    int updateByPrimaryKey(User record);

    /**
     * @param map:
     * @return User
     * @author xulingrui
     * @description TODO
     * 根据封装的map中的用户名与密码，向数据库中查询用户
     * @date 2022/5/20 20:18
     */
    User selectByLoginActAndPwd(Map<String, Object> map);

    /**
     * @param :
     * @return List<User>
     * @author xulingrui
     * @description TODO
     * 查询所有的用户
     * @date 2022/5/23 15:44
     */
    List<User> selectAllUsers();
}