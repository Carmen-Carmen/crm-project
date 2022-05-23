package com.lingrui.crm.workbench.web.controller;

import com.lingrui.crm.settings.domain.User;
import com.lingrui.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-23 15:27
 * @ Version 1.0
 */
@Controller
public class ActivityController {
    @Autowired
    private UserService userService;

    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     *      查出市场活动主页面所需动态数据：所有用户信息
     *      并放到作用域里，保存到request作用域即可
     *      跳转到市场活动的主页面的index.jsp
     * @date 2022/5/23 15:28
     */
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        //1、查询所有用户信息并存到request作用域
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);

        //2、请求转发到市场活动的主页面
        return "workbench/activity/index";
    }
}
