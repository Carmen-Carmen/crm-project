package com.lingrui.crm.common.utils;

import java.util.UUID;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-23 19:03
 * @ Version 1.0
 */
public class UUIDUtils {
    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * 生成不带连字符的32位uuid的方法，用于主键
     * @date 2022/5/23 19:04
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
