package com.lingrui.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ Description
 * 对应workbench/index.jsp相关请求
 * @ Author Carmen
 * @ Date 2022-05-20 21:36
 * @ Version 1.0
 */
@Controller
public class WorkbenchIndexController {
    @RequestMapping("/workbench/index.do")
    public String index() {
        return "workbench/index";//跳转到/ workbench/index. jsp
    }
}
