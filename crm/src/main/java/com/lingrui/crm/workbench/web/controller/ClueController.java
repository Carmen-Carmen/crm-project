package com.lingrui.crm.workbench.web.controller;

import com.lingrui.crm.settings.domain.DictValue;
import com.lingrui.crm.settings.domain.User;
import com.lingrui.crm.settings.service.DictValueService;
import com.lingrui.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ Description
 * 处理与线索相关的控制器
 * @ Author Carmen
 * @ Date 2022-06-27 16:48
 * @ Version 1.0
 */
@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private DictValueService dictValueService;

    /**
     * @param request:
     * @return String
     * @author xulingrui
     * @description TODO
     * 控制跳转至线索主页面的控制器方法
     * workbench/clue/index.jsp
     * 线索主页面需要用到以下数据
     *      用户列表
     *      称呼字典值列表
     *      线索状态字典值列表
     *      线索来源字典值列表
     * 都放到request作用域里
     * @date 2022/6/27 16:54
     */
    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request) {
        //调用service层，查询所需数据
        List<User> userList = userService.queryAllUsers();
        List<DictValue> appellationList = dictValueService.queryDictValueByTypeCode("appellation");
        List<DictValue> clueStateList = dictValueService.queryDictValueByTypeCode("clueState");
        List<DictValue> sourceList = dictValueService.queryDictValueByTypeCode("source");

        //放入requestScope
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);

        return "workbench/clue/index";
    }
}
