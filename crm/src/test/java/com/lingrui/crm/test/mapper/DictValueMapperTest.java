package com.lingrui.crm.test.mapper;

import com.lingrui.crm.settings.mapper.DictValueMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-18 23:55
 * @ Version 1.0
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mvc.xml"})
@RunWith(value = SpringJUnit4ClassRunner.class)
public class DictValueMapperTest {
    @Autowired
    private DictValueMapper dictValueMapper;

    @Test
    public void testSelectDictValueByTypeCode() {
        System.out.println(dictValueMapper.selectDictValueByTypeCode("source"));
    }
}
