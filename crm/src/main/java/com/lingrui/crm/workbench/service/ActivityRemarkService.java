package com.lingrui.crm.workbench.service;

import com.lingrui.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-08 16:01
 * @ Version 1.0
 */
public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

    int saveCreateActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);
}
