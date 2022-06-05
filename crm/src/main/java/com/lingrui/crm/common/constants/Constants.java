package com.lingrui.crm.common.constants;

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

    //Activity实体类的属性名
    public static final String[] ACTIVITY_FIELD_NAME_LIST = new String[] {"id", "owner", "name", "startDate", "endDate", "cost", "description", "createTime", "createBy", "editTime", "editBy"};
}
