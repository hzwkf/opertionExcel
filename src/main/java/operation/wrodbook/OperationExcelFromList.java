package operation.wrodbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import patten.JudgmentType.*;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static patten.JudgmentType.isDouble;
import static patten.JudgmentType.isInteger;

public class OperationExcelFromList {
    XSSFWorkbook workbook = new XSSFWorkbook();

    public void ExcelFromList(String fileName, List userInfos) {
        //Workbook[] wbs = new Workbook[] { new HSSFWorkbook(), new XSSFWorkbook() };


        //新建一个sheet
        XSSFSheet sheet = workbook.createSheet("sheet-1");//带参和不带参两个方法，这个选择带参方法

        //新建一个Row对象：行
        Row row = null;
        //新建一个Cell对象：单元格
        Cell cell = null;

        //定义一个style 单元格样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFDataFormat dataFormat = workbook.createDataFormat();

        //创建字体对象
        XSSFFont font = workbook.createFont();
        //设置单元格字体
        font.setFontHeightInPoints((short) 15); //字体高度(以磅为单位)
        font.setBold(true); //加粗
        font.setFontHeight(17); //设置字体高度
        font.setColor(IndexedColors.RED.index); //设置字体颜色

        //为单元格设置字体
        cellStyle.setFont(font);
        cellStyle.setDataFormat(dataFormat.getFormat("#,##0.0"));


        //循环录入数据
        XSSFRow headRow = sheet.createRow(0); //第一行设置为表头
        Field[] headSize = UserInfo.class.getDeclaredFields(); //获取对象属性值，返回一个数组

        for (int i = 0; i < headSize.length; i++) {


            String fieldsName = headSize[i].getName();
            //定义表头
            headRow.createCell(i).setCellValue(fieldsName);
        }

        //循环写入数据
        for (int j = 0, rowNum = 1; j < userInfos.size(); j++, rowNum++) {

            XSSFRow dataRow = sheet.createRow(rowNum);
            Object userInfo = userInfos.get(j);
            for (int k = 0; k < headSize.length; k++) {

                String fieldsName = headSize[k].getName();
                //dataRow.setRowStyle(cellStyle);

                String res = getFieldValueByName(fieldsName, userInfo);



                if (isInteger(res)) { //判断插入数据是否是整型
                    dataRow.createCell(k).setCellValue(Integer.parseInt(res));
                } else if (isDouble(res)) { //判断插入数据是否是浮点型
                    dataRow.createCell(k).setCellValue(Double.parseDouble(res));
                } else { //其余类型全部按照字符串插入
                    dataRow.createCell(k).setCellValue(res);
                }
            }
        }

        //数据写入 Excel
        writeToExcel(fileName);

    }


    //根据对象名获取属性值
    private static String getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);

            Method method = o.getClass().getMethod(getter);

            String value = method.invoke(o, new Object[]{}).toString();

            return value;
        } catch (Exception e) {
            System.out.println("获取属性值失败！" + e.getMessage());
        }
        return null;
    }

    public void writeToExcel(String fileName) {

        if (workbook instanceof XSSFWorkbook) {
            fileName = fileName + ".xlsx";
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
            workbook.write(bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
