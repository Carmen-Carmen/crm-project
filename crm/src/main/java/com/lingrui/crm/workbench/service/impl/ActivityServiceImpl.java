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
}
