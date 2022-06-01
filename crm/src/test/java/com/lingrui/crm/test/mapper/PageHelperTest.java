package com.lingrui.crm.test.mapper;

import com.github.pagehelper.PageHelper;
import com.lingrui.crm.workbench.domain.Activity;
import com.lingrui.crm.workbench.mapper.ActivityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-01 16:28
 * @ Version 1.0
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mvc.xml"})
@RunWith(value = SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class PageHelperTest {
    @Autowired
    ActivityMapper activityMapper;

    @Test
    public void testSelectActivityWithPageHelper() {
        PageHelper.startPage(0, 2);
        List<Activity> activityList = activityMapper.selectAllActivity();
        System.out.println(activityList.size());

    }
}
