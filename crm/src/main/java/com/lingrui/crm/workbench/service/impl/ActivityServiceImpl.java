package com.lingrui.crm.workbench.service.impl;

import com.lingrui.crm.workbench.domain.Activity;
import com.lingrui.crm.workbench.mapper.ActivityMapper;
import com.lingrui.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
