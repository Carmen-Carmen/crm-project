package com.lingrui.crm.test.mapper;

import com.lingrui.crm.workbench.mapper.ActivityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-08 13:15
 * @ Version 1.0
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mvc.xml"})
@RunWith(value = SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ActivityMapperTest {
    @Autowired
    ActivityMapper activityMapper;

    @Test
    public void testSelectActivityForDetailById() {
        System.out.println(activityMapper.selectActivityForDetailById("50f57c666db24e069974a760f7426dfa"));
    }
}
