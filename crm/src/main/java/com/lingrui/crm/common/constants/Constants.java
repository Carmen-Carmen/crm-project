package com.lingrui.crm.common.constants;

import com.lingrui.crm.workbench.domain.Activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-20 22:22
 * @ Version 1.0
 */
public class Constants {
    //ObjectForReturn的code值
    public static final String OBJECT_FOR_RETURN_SUCCESS = "1";//成功
    public static final String OBJECT_FOR_RETURN_FAIL = "0";//失败

    //session中保存当前用户的，attribute中的key
    public static final String SESSION_USER = "sessionUser";

    //一般错误的提示信息
    public static final String COMMON_ERROR_MSG = "系统忙，请稍后重试...";

    //服务器生成的文件存储位置
    public static final String SERVER_FILE_PATH = "/Users/xulingrui/Desktop/java_learning/CRM_project/serverDir/";

    //导入市场活动模板的文件名
    public static final String IMPORT_ACTIVITY_TEMPLATE_FILE_NAME = "activity_import_template.xls";

    //导入市场活动时，需要的市场活动字段名
    public static final List<String> IMPORT_ACTIVITY_FIELD_NAME_LIST = new ArrayList();
    static {
        IMPORT_ACTIVITY_FIELD_NAME_LIST.add("name");
        IMPORT_ACTIVITY_FIELD_NAME_LIST.add("startDate");
        IMPORT_ACTIVITY_FIELD_NAME_LIST.add("endDate");
        IMPORT_ACTIVITY_FIELD_NAME_LIST.add("cost");
        IMPORT_ACTIVITY_FIELD_NAME_LIST.add("description");
    }

    //导出市场活动时，表头的字段名
    public static final List<String> EXPORT_ACTIVITY_FIELD_NAME_LIST = new ArrayList<>();
    static {
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("ID");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("拥有者");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("名称");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("开始日期");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("结束日期");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("成本");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("描述");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("创建时间");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("创建者");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("修改时间");
        EXPORT_ACTIVITY_FIELD_NAME_LIST.add("修改者");
    }
}
