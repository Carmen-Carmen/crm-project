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

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(String noteContent, String activityId, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Constants.SESSION_USER);

        //封装参数，前端能提供的：noteContent和activityId
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(UUIDUtils.generateUUID());//id
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        activityRemark.setCreateBy(sessionUser.getId());
        activityRemark.setEditFlag(Constants.ACTIVITY_REMARK_NOT_EDITED);
        activityRemark.setActivityId(activityId);

        ObjectForReturn objectForReturn = null;
        try {
            //调用service层，添加记录
            int affectedRows = activityRemarkService.saveCreateActivityRemark(activityRemark);
            //查出新的activityRemarkList
            List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(activityId);

            //根据结果，返回响应信息
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
}
