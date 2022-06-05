package com.lingrui.crm.test.poi;

import com.lingrui.crm.common.constants.Constants;
import com.lingrui.crm.common.utils.POIUtils;
import com.lingrui.crm.workbench.domain.Activity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-01 18:13
 * @ Version 1.0
 */
public class ParseExcelTest {
    @Test
    public void testParseExcel() throws Exception{
        List<Activity> activityList = new ArrayList<>();
        List<String> fieldNameList = new ArrayList<>();

        //根据excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
        FileInputStream fis = new FileInputStream(Constants.SERVER_FILE_PATH + "2c5a55178e794853aacdeea9bb81d929.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
//        //根据workbook获取HSSFSheet对象，封装了一页的所有信息
//        HSSFSheet sheet = workbook.getSheetAt(0);//页的下标，从0开始依次增加
//        //根据sheet获取HSSFRow对象，封装了一行的所有信息
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        Object cellValue = null;
//        for (int rowIndex = 0; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {//sheet.getLaseRowNum()最后一行的下标！
//            row = sheet.getRow(rowIndex);
//        //根据row获取HSSFCell对象，封装了单元格信息
//            Activity activity = Activity.class.getConstructor().newInstance();
//            for (int fieldIndex = 0; fieldIndex < row.getLastCellNum(); fieldIndex++) {//有毛病，row.getLastCellNum()反而是最后列的编号（下标+1）
//                cell = row.getCell(fieldIndex);
//                int cellType = cell.getCellType();
//                //根据单元格类型，调用不同方法取
//                if (cellType == HSSFCell.CELL_TYPE_STRING) {
//                    cellValue = cell.getStringCellValue();
//                } else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
//                    cellValue = cell.getNumericCellValue();
//                } else if (cellType == HSSFCell.CELL_TYPE_BOOLEAN) {
//                    cellValue = cell.getBooleanCellValue();
//                } else if (cellType == HSSFCell.CELL_TYPE_FORMULA) {
//                    cellValue = cell.getCellFormula();
//                } else {
//                    cellValue = "";
//                }
//
//                if (rowIndex == 0) {
//                    //表头
//                    fieldNameList.add((String) cellValue);
//                } else {
//                    //表体部分
//                    //利用反射填入数据
//                    Field field = Activity.class.getDeclaredField(fieldNameList.get(fieldIndex));
//                    field.setAccessible(true);
//                    field.set(activity, cellValue);
//                    field.setAccessible(false);
//                }
//            }
//            //放入List中
//            if (rowIndex != 0) {
//                activityList.add(activity);
//            }
//        }

        activityList = POIUtils.parseWorkbookToList(workbook, Activity.class, null);

        for (Activity activity : activityList) {
            System.out.println(activity);
        }
    }

    @Test
    public void testWorkbookToList() throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(Constants.SERVER_FILE_PATH + "activity_template.xls"));
        List<Activity> activityList = POIUtils.parseWorkbookToList(workbook, Activity.class, Arrays.asList(Constants.ACTIVITY_FIELD_NAME_LIST));
        for (Activity activity : activityList) {
            System.out.println(activity);
        }

    }
}
