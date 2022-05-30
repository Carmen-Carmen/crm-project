package com.lingrui.crm.test.uuid;

import com.lingrui.crm.common.utils.UUIDUtils;
import org.junit.Test;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-30 16:01
 * @ Version 1.0
 */
public class UUIDTest {
    @Test
    public void testGenerateUUID() {
        System.out.println(UUIDUtils.generateUUID());
    }
}
