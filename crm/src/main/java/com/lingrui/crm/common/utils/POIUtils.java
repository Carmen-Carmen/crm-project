package com.lingrui.crm.common.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-30 15:12
 * @ Version 1.0
 */
public class POIUtils {
    public static <T> HSSFWorkbook generateWorkbookByList(List<T> list, String sheetName) throws IllegalAccessException {
        if (list == null || list.size() == 0) return null;//先判断传入的list是否含有元素

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = (sheetName != null)? workbook.createSheet(sheetName) : workbook.createSheet();

        Field[] fields = list.get(0).getClass().getDeclaredFields();

        //表头
        HSSFRow header = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            HSSFCell cell = header.createCell(i);
            cell.setCellValue(fields[i].getName());
        }

        //table body
        int rowCount = 1;
        for (T t : list) {
            HSSFRow row = sheet.createRow(rowCount++);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                String val = (String) field.get(t);
                field.setAccessible(false);

                HSSFCell cell = row.createCell(i);
                cell.setCellValue(val);
            }
        }

        return workbook;
    }
}
