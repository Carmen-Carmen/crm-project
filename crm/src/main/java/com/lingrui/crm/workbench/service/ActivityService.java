package com.lingrui.crm.workbench.service;

import com.lingrui.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-23 17:52
 * @ Version 1.0
 */
public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    int queryCountOfActivityByCondition(Map<String, Object> map);

    int deleteActivityByIds(String[] ids);
}
