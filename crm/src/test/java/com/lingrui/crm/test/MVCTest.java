package com.lingrui.crm.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lingrui.crm.common.domain.ObjectForReturn;
import org.apache.logging.log4j.core.net.MimeMessageBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-27 21:06
 * @ Version 1.0
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mvc.xml"})
@RunWith(value = SpringJUnit4ClassRunner.class)
@WebAppConfiguration

//配置事务回滚，对数据库的增删改查都会回滚，便于测试用例的循环利用
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class MVCTest {
    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testDeleteActivityByIds() throws Exception {
//        String json = mockMvc.perform(MockMvcRequestBuilders.post("/workbench/activity/deleteActivityByIds.do?id=1&id=2")).andDo(print()).andReturn().getResponse().getContentAsString();
//
//        JsonFactory jsonFactory = new JsonFactory();
//        JsonParser parser = jsonFactory.createParser(json);
//        JsonToken jsonToken = parser.nextToken();
//        System.out.println(jsonToken);
    }
}
