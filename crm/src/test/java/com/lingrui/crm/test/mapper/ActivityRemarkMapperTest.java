package com.lingrui.crm.test.mapper;

import com.lingrui.crm.common.utils.DateUtils;
import com.lingrui.crm.common.utils.UUIDUtils;
import com.lingrui.crm.workbench.domain.ActivityRemark;
import com.lingrui.crm.workbench.mapper.ActivityRemarkMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-08 15:59
 * @ Version 1.0
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mvc.xml"})
@RunWith(value = SpringJUnit4ClassRunner.class)
public class ActivityRemarkMapperTest {
    @Autowired
    ActivityRemarkMapper activityRemarkMapper;

    @Test
    public void testSelectActivityRemarkForDetailByActivityId() {
        System.out.println(activityRemarkMapper.selectActivityRemarkForDetailByActivityId(""));
    }

    @Test
    public void testInsertActivityRemark() {
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(UUIDUtils.generateUUID());
        activityRemark.setNoteContent("test mapper");
        activityRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        activityRemark.setCreateBy("e9b162c1b7de45d6bd6e8c730b2bf178");
        activityRemark.setEditFlag("0");
        activityRemark.setActivityId("50f57c666db24e069974a760f7426dfa");

        activityRemarkMapper.insertActivityRemark(activityRemark);
    }
}
