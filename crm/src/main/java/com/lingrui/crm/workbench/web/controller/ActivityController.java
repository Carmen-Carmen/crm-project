package com.lingrui.crm.workbench.web.controller;

import com.lingrui.crm.common.constants.Constants;
import com.lingrui.crm.common.domain.ObjectForReturn;
import com.lingrui.crm.common.utils.DateUtils;
import com.lingrui.crm.common.utils.UUIDUtils;
import com.lingrui.crm.settings.domain.User;
import com.lingrui.crm.settings.service.UserService;
import com.lingrui.crm.workbench.domain.Activity;
import com.lingrui.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private ActivityService activityService;

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

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody//返回json字符串
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        //使用实体类对象作为参数，直接封装表单提交的字段成一个对象
        //1、还要封装其他参数
        activity.setId(UUIDUtils.generateUUID());//生成一个32位uuid作为id
        activity.setCreateTime(DateUtils.formatTime(new Date()));//以当前时间转化为字符串作为创建时间
        User sessionUser = (User) session.getAttribute(Constants.SESSION_USER);//从session中取出当前用户
        activity.setCreateBy(sessionUser.getId());//数据库中市场活动的create_by字段理论上引用的也是用户id

        ObjectForReturn objectForReturn = new ObjectForReturn();
        try {
        //2、调用activityService，保存创建的市场活动
            int influencedRows = activityService.saveCreateActivity(activity);

        //3、根据处理结果，生成响应
            if (influencedRows > 0) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage("系统忙，请稍后重试....");//不要把后台报错的理由写的太清楚
            }
        } catch (Exception e) {
            e.printStackTrace();

            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage("系统忙，请稍后重试....");
        }


        return objectForReturn;
    }
}
