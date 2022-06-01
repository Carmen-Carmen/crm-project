package com.lingrui.crm.common.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-30 15:12
 * @ Version 1.0
 */
public class POIUtils {
    /**
     * @param list: 要生成数据表的List
     * @param sheetName: 给"sheet1"改名字
     * @return HSSFWorkbook
     * @author xulingrui
     * @description TODO
     * 使用反射，将List中的实体类对象数据写入.xls格式的Excel文档对象
     * @date 2022/6/1 16:50
     */
    public static <T> HSSFWorkbook generateWorkbookByList(List<T> list, String sheetName) throws IllegalAccessException {
        if (list == null || list.size() == 0) return null;//先判断传入的list是否含有元素

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = (sheetName != null)? workbook.createSheet(sheetName) : workbook.createSheet();

        Field[] fields = list.get(0).getClass().getDeclaredFields();//这个时候List里一定有数据了

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

    /**
     * @param workbook: 符合格式的excel文档生成的HSSFWorkbook
     * @param targetClass:
     * @param fieldNameList: 也可自己传入指定的对应实体类属性名的List
     * @return List
     * @author xulingrui
     * @description TODO
     * 对格式的要求
     *      顶着第一格写数据
     *      首行为表头，表头单元格中数据就是targetClass的对象的属性名
     *      数据只写在第一个sheet里
     * @date 2022/6/1 19:01
     */
    public static List parseWorkbookToList(HSSFWorkbook workbook, Class targetClass, List<String> fieldNameList) throws Exception {
        //判断是否传入了有效的属性名List
        Boolean isFieldNameKnown = false;
        if (fieldNameList != null && fieldNameList.size() != 0) {
            isFieldNameKnown = true;
        } else {//没传
            fieldNameList = new ArrayList<>();
        }

        List itemList = new ArrayList();
        //解析workbook
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = null;
        String cellValue = null;
        for (int rowIndex = 0; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
            row = sheet.getRow(rowIndex);
            //生成这一行对应的实体类
            Object item = targetClass.getConstructor().newInstance();
            for (int fieldIndex = 0; fieldIndex < row.getLastCellNum(); fieldIndex++) {
                cellValue = getCellValue(row.getCell(fieldIndex));

                if (rowIndex == 0 && !isFieldNameKnown) {
                    //table header
                    fieldNameList.add(cellValue);
                } else {
                    //table body
                    Field field = targetClass.getDeclaredField(fieldNameList.get(fieldIndex));
                    field.setAccessible(true);
                    field.set(item, cellValue);
                    field.setAccessible(false);
                }
            }
            if (rowIndex != 0) itemList.add(item);
        }

        return itemList;
    }

    /**
     * @param cell:
     * @return String
     * @author xulingrui
     * @description TODO
     * 将HSSFCell中的值全部以字符串返回
     * @date 2022/6/1 18:55
     */
    public static String getCellValue(HSSFCell cell) {
        int cellType = cell.getCellType();
        String cellValue = "";
        if (cellType == HSSFCell.CELL_TYPE_STRING) {
            cellValue = cell.getStringCellValue();
        } else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
            cellValue = cell.getNumericCellValue() + "";
        } else if (cellType == HSSFCell.CELL_TYPE_BOOLEAN) {
            cellValue = cell.getBooleanCellValue() + "";
        } else if (cellType == HSSFCell.CELL_TYPE_FORMULA) {
            cellValue = cell.getCellFormula();
        } else {
            cellValue = "";
        }

        return cellValue;
    }
}
