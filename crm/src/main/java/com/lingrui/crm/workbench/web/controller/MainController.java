package com.lingrui.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-23 14:40
 * @ Version 1.0
 */
@Controller
public class MainController {
    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * 跳转到/workbench/main/index.jsp
     * @date 2022/5/23 14:42
     */
    @RequestMapping("/workbench/main/index.do")
    public String index() {
        return "workbench/main/index";
    }
}
