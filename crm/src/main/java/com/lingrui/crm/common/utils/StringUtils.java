package com.lingrui.crm.common.utils;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-05 15:07
 * @ Version 1.0
 */
public class StringUtils {
    public static boolean isNull(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isNotNull(String s) {
        return !isNull(s);
    }
}
