package com.lingrui.crm.workbench.mapper;

import com.lingrui.crm.workbench.domain.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Mon May 23 17:28:15 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Mon May 23 17:28:15 CST 2022
     */
    int insert(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Mon May 23 17:28:15 CST 2022
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Mon May 23 17:28:15 CST 2022
     */
    Activity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Mon May 23 17:28:15 CST 2022
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Mon May 23 17:28:15 CST 2022
     */
    int updateByPrimaryKey(Activity record);

    /**
     * @param activity:
     * @return int
     * @author xulingrui
     * @description TODO
     * 自己书写的新增市场活动
     * @date 2022/5/23 17:50
     */
    int insertActivity(Activity activity);

    /**
     * @param map: 以map形式存储查询条件
     * @return List<Activity>
     * @author xulingrui
     * @description TODO
     *
     * @date 2022/5/25 21:16
     */
    List<Activity> selectActivityByConditionForPage(Map<String, Object> map);

    /**
     * @param map: 以map形式存储查询条件
     * @return List<Activity>
     * @author xulingrui
     * @description TODO
     * 查询所有符合条件的市场活动；分页交给PageHelper来做
     * @date 2022/5/25 21:16
     */
    List<Activity> selectActivityByCondition(Map<String, Object> map);

    /**
     * @param map:以map形式存储查询条件
     * @return int
     * @author xulingrui
     * @description TODO
     * @date 2022/5/25 21:16
     */
    int selectCountOfActivityByCondition(Map<String, Object> map);

    /**
     * @param ids: 字符串数组
     * @return int
     * @author xulingrui
     * @description TODO
     * 根据ids字符串数组批量删除市场活动
     * @date 2022/5/27 20:42
     */
    int deleteActivityByIds(String[] ids);

    /**
     * @param id:
     * @return Activity
     * @author xulingrui
     * @description TODO
     * 根据id查询市场活动详细信息
     * @date 2022/5/28 17:10
     */
    Activity selectActivityById(String id);

    /**
     * @param activity: 封装为Activity实体类对象的参数
     * @return int
     * @author xulingrui
     * @description TODO
     * 保存修改过后的市场活动
     * @date 2022/5/28 18:06
     */
    int updateActivity(Activity activity);

    /**
     * @param :
     * @return List<Activity>
     * @author xulingrui
     * @description TODO
     * 从数据库中查询所有的市场活动，用于"批量导出市场活动"功能
     * @date 2022/5/30 14:46
     */
    List<Activity> selectAllActivity();

    /**
     * @param ids:
     * @return List<Activity>
     * @author xulingrui
     * @description TODO
     * 根据ids字符串数组查询市场活动
     * @date 2022/5/30 16:32
     */
    List<Activity> selectActivityByIds(String[] ids);

    /**
     * @param activityList:
     * @return int
     * @author xulingrui
     * @description TODO
     * 批量保存创建的市场活动
     * @date 2022/6/5 12:17
     */
    int insertActivityByList(List<Activity> activityList);

    /**
     * @param id:
     * @return Activity
     * @author xulingrui
     * @description TODO
     * 根据id查询市场活动的详细信息
     * @date 2022/6/8 13:07
     */
    Activity selectActivityForDetailById(String id);
}