package com.lingrui.crm.workbench.service.impl;

import com.lingrui.crm.workbench.domain.Activity;
import com.lingrui.crm.workbench.mapper.ActivityMapper;
import com.lingrui.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-23 17:53
 * @ Version 1.0
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    /**
     * @param map:
     * @return List<Activity>
     * @author xulingrui
     * @description TODO
     * 查询所有符合条件市场活动的方法
     * @date 2022/6/1 16:38
     */
    @Override
    public List<Activity> queryActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectActivityByCondition(map);
    }

    /**
     * @param ids:
     * @return int
     * @author xulingrui
     * @description TODO
     * 根据数据库返回的受影响行数，判断实际删除的是否与要求删除的条数符合
     * 若不符合则抛出异常，事务回滚
     * @date 2022/5/28 17:16
     */
    @Override
    public int deleteActivityByIds(String[] ids) {
        int affectedRows = activityMapper.deleteActivityByIds(ids);
        if (affectedRows == ids.length) {
            return affectedRows;
        } else {
            System.out.println("111");
            throw new RuntimeException("实际删除记录数与目标不符...");
        }
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public List<Activity> queryAllActivity() {
        return activityMapper.selectAllActivity();
    }

    @Override
    public List<Activity> queryActivityByIds(String[] ids) {
        return activityMapper.selectActivityByIds(ids);
    }

    @Override
    public int saveActivityByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }
}
