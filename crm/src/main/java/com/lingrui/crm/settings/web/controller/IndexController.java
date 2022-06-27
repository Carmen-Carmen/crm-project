package com.lingrui.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-18 20:59
 * @ Version 1.0
 */
@Controller("SettingsIndexController")//必须重新命名，因为已经存在一个IndexController了
public class IndexController {
    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * 控制跳转到/WEB-INF/pages/  index  .jsp
     * @date 2022/6/18 21:02
     */
    @RequestMapping("/settings/index.do")
    public String index() {
        return "settings/index";
    }
}
