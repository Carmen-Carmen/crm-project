package com.lingrui.crm.workbench.web.controller;

import com.lingrui.crm.common.constants.Constants;
import com.lingrui.crm.common.domain.ObjectForReturn;
import com.lingrui.crm.common.utils.DateUtils;
import com.lingrui.crm.common.utils.UUIDUtils;
import com.lingrui.crm.settings.domain.User;
import com.lingrui.crm.settings.service.UserService;
import com.lingrui.crm.workbench.domain.Activity;
import com.lingrui.crm.workbench.service.ActivityService;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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

    /**
     * @param activity: 直接用实体类对象接收参数
     * @param session:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 保存创建市场活动
     * @date 2022/5/28 18:16
     */
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody//返回json字符串
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        //使用实体类对象作为参数，直接封装表单提交的字段成一个对象
        //1、还要封装其他参数
        activity.setId(UUIDUtils.generateUUID());//生成一个32位uuid作为id
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));//以当前时间转化为字符串作为创建时间，注意需要的是日期+时间的datetime
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
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);//不要把后台报错的理由写的太清楚
            }
        } catch (Exception e) {
            e.printStackTrace();

            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }


        return objectForReturn;
    }

    /**
     * @param name:
     * @param owner:
     * @param startDate:
     * @param endDate:
     * @param pageNo:
     * @param pageSize:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 分页，按条件查询出市场活动列表
     * @date 2022/5/28 18:17
     */
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(
            //查询条件
            String name,
            String owner,
            String startDate,
            String endDate,
            //分页信息
            int pageNo,
            int pageSize
    ) {
        //封装参数，key要和sql语句中占位符的一致
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("beginNo", (pageNo - 1) * pageSize);//计算出开始显示的条数，作为sql语句中limit的第一个参数

        //调用service，获取数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        int totalPages;//计算总页数，扔给前端
        if (totalRows % pageSize == 0) {
            totalPages = totalRows / pageSize;
        } else {
            totalPages = totalRows / pageSize + 1;
        }

        //根据查询结果，生成响应信息
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("activityList", activityList);
        resultMap.put("totalRows", totalRows);
        resultMap.put("totalPages", totalPages);

        return resultMap;
    }

    /**
     * @param id:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 前端发出请求中"id=xxx&...&id=xxx"会被dispatcherServlet转化为变量名为id的String数组
     * @date 2022/5/28 18:18
     */
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id) {
        ObjectForReturn objectForReturn = new ObjectForReturn();
        try {
            //调用service层方法，删除市场活动
            int affectedRows = activityService.deleteActivityByIds(id);
            //把判断删除条数是否与传入数组长度一致的判断放到service层，一旦出错就会被这里catch到
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
        } catch (Exception e) {
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            e.printStackTrace();
        }

        return objectForReturn;
    }

    /**
     * @param id:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 根据id查询市场活动，返回给前端页面用于修改modal窗口的数据回显
     * @date 2022/5/28 18:19
     */
    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        //调用Service方法，传入接收到的参数，获取市场活动实体类对象
        Activity activity = activityService.queryActivityById(id);
        //根据查询结果，返回响应值
        return activity;
    }


    /**
     * @param activity: 直接用实体类对象接收参数
     * @return Object
     * @author xulingrui
     * @description TODO
     * @date 2022/5/28 18:21
     */
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        //1、补全activity中缺少的数据
        //获取当前用户，将其id设为市场活动的editBy属性
        User user = (User) session.getAttribute(Constants.SESSION_USER);//一定能获取到的，因为做过登录验证了
        activity.setEditBy(user.getId());
        //将当前时间转化为字符串，设为editTime属性
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        int affectedRows = 0;

        ObjectForReturn objectForReturn = new ObjectForReturn();
        try {
        //2、调用service方法，保存修改后的市场活动
            affectedRows = activityService.saveEditActivity(activity);

        //3、根据处理结果生成响应信息
            if (affectedRows == 1) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            e.printStackTrace();
        }

        return objectForReturn;
    }
}
