package com.lingrui.crm.test.poi;

import com.lingrui.crm.common.utils.POIUtils;
import com.lingrui.crm.workbench.domain.Activity;
import com.lingrui.crm.workbench.mapper.ActivityMapper;
import com.lingrui.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Description
 * 使用apache-poi生成excel文件
 * @ Author Carmen
 * @ Date 2022-05-30 11:44
 * @ Version 1.0
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mvc.xml"})
@RunWith(value = SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class CreateExcelTest {
    @Autowired
    ActivityService activityService;

    @Test
    public void testCreateExcel() {
        //获取市场活动列表
        List<Activity> activityList = activityService.queryAllActivity();

//        //创建excel对象，HSSFWorkbook对应的是旧版本excel格式.xls
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet();
//        //填入数据
//        //创建表头的行，第一行，下标为0
//        HSSFRow header = sheet.createRow(0);//行的下标
//        //使用反射填入表头的数据名
//        Class<Activity> activityClass = Activity.class;
//        Field[] declaredFields = activityClass.getDeclaredFields();
//        for (int i = 0; i < declaredFields.length; i++) {
//            HSSFCell cell = header.createCell(i);
//            cell.setCellValue(declaredFields[i].getName());
//        }
//
//        //表体
//        try {
//            int rowCount = 1;
//            for (Activity activity : activityList) {
//                //创建行
//                HSSFRow row = sheet.createRow(rowCount++);//参数是列的脚标
//
//                for (int i = 0; i < declaredFields.length; i++) {
//                    //创建列
//                    HSSFCell cell = row.createCell(i);
//                    //获取数据
//                    declaredFields[i].setAccessible(true);
//                    String val = (String) declaredFields[i].get(activity);
//                    declaredFields[i].setAccessible(false);
//                    //填入数据
//                    cell.setCellValue(val);
//                }
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        HSSFWorkbook workbook = null;
        try {
            workbook = POIUtils.generateWorkbookByList(activityList, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //HSSFCellStyle修饰样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //其他的set都可以修改各种各样的

        //输出
        FileOutputStream fos = null;
        try {
            //目录必须是创建好的；文件如果不存在没关系
            File file = new File("/Users/xulingrui/Downloads/output1.xls");//HSFF对应的是旧版excel格式.xls
//            if (!file.exists()) file.createNewFile();
            fos = new FileOutputStream(file);
            //调用工具函数write(OutputStream)，也可以将File作为参数传入，内部也会创建一个输出流的
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("==================== Create Ok ====================");
    }
}
