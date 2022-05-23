package com.lingrui.crm.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ Description
 * 对Date类型数据进行处理的工具类
 * @ Author Carmen
 * @ Date 2022-05-20 22:10
 * @ Version 1.0
 */
public class DateUtils {
    private static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static String datePattern = "yyyy-MM-dd";
    private static String timePattern = "HH:mm:ss";

    /**
     * @param datetime:
     * @return String
     * @author xulingrui
     * @description TODO
     * 将指定的Date对象转换为统一的字符串
     * @date 2022/5/20 22:12
     */
    public static String formatDateTime(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimePattern);
        return sdf.format(datetime);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        return sdf.format(date);
    }

    public static String formatTime(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
        return sdf.format(time);
    }

}
