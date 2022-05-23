package com.lingrui.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-19 16:23
 * @ Version 1.0
 */
@Controller
public class IndexController {

    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * 给这个Controller方法分配url     http://127.0.0.1:8080/crm/
     * @date 2022/5/19 16:31
     */
    @RequestMapping("/")
    public String index() {
        //请求转发；如果是redirect的话是改变url访问，这样访问不到WEB-INF下资源
        return "index";//视图解析器中自动加上前缀和后缀/WEB-INF/pages/  index  .jsp
    }
}
