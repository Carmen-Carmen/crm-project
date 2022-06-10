package com.lingrui.crm.workbench.web.controller;

import com.lingrui.crm.common.constants.Constants;
import com.lingrui.crm.common.domain.ObjectForReturn;
import com.lingrui.crm.common.utils.DateUtils;
import com.lingrui.crm.common.utils.UUIDUtils;
import com.lingrui.crm.settings.domain.User;
import com.lingrui.crm.workbench.domain.ActivityRemark;
import com.lingrui.crm.workbench.mapper.ActivityRemarkMapper;
import com.lingrui.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-08 18:13
 * 处理市场活动备注的controller层
 * 理论上也可以写在ActivityController里，但是这样就太多行了，因此新建一个class
 * @ Version 1.0
 */
@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;

    /**
     * @param noteContent:
     * @param activityId:
     * @param session:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 收集前台传来的两个参数，封装进ActivityRemark实体类中，传给Service层一路往下
     * 也可以把noteContent和activityId用ActivityRemark实体类来接收
     * @date 2022/6/10 19:31
     */
    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(String noteContent, String activityId, HttpSession session) {
        //从session中获取当前用户
        User sessionUser = (User) session.getAttribute(Constants.SESSION_USER);

        //1、封装参数，前端能提供的：noteContent和activityId
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(UUIDUtils.generateUUID());//id
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        activityRemark.setCreateBy(sessionUser.getId());
        activityRemark.setEditFlag(Constants.ACTIVITY_REMARK_NOT_EDITED);//刚创建的activityRemark没有修改过
        activityRemark.setActivityId(activityId);

        ObjectForReturn objectForReturn = null;
        try {//涉及向数据库中写数据的，最好都try-catch一下
        //2、调用service层，添加记录
            int affectedRows = activityRemarkService.saveCreateActivityRemark(activityRemark);
            //查出新的activityRemarkList
            List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(activityId);

        //3、根据结果，返回响应信息
            objectForReturn = new ObjectForReturn();
            if (affectedRows == 1) {
                //插入了一条
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
                objectForReturn.setReturnData(remarkList);
            } else {
                //影响条数不对
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

    /**
     * @param remarkId:
     * @param activityId:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 处理浏览器发来删除市场活动备注的请求
     * @date 2022/6/10 23:12
     */
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String remarkId, String activityId) {
        ObjectForReturn objectForReturn = null;
        try {
        //1、调用service层执行删除
            int affectedRows = activityRemarkService.deleteActivityRemarkById(remarkId);
            List<ActivityRemark> activityRemarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(activityId);

        //2、根据结果生成响应信息
            objectForReturn = new ObjectForReturn();
            if (affectedRows == 1) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
                objectForReturn.setReturnData(activityRemarkList);
            } else {
                //没删掉或是其他情况
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

    
}
