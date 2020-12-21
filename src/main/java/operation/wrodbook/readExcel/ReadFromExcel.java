package operation.wrodbook.readExcel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static patten.JudgmentType.isDouble;
import static patten.JudgmentType.isInteger;


public class ReadFromExcel {

    public void ReadExcel(String fileName, String sheetName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));

        XSSFWorkbook workbook = new XSSFWorkbook(bis);

        //指定需要读取的sheet
        XSSFSheet sheet = workbook.getSheet(sheetName);

        //迭代遍历每一行
        for (int rowNum = 0; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
            String str = "";

            XSSFRow headRow = sheet.getRow(rowNum);

            int physicalNumberOfCells = sheet.getRow(rowNum).getPhysicalNumberOfCells();

            for (int cellNum = 0; cellNum < physicalNumberOfCells; cellNum++) {
                XSSFCell cell = headRow.getCell(cellNum); //获取每个cell的value

                if (rowNum == 0) { //Excel 第一行为表头。属性默认为String
                    String.format(str += cell.getStringCellValue() + " ");
                } else { //除去表头为Excel内容
                    if (cellNum == 0) { //首行以外，定义每行首列为id 默认导入为Integer类型
                        String.format(str += (int) cell.getNumericCellValue() + " ");
                    } else { //XSSFCell 的数字类型获取后默认为浮点型
                        if (isDouble(cell.toString())) {
                            String.format(str += cell.getNumericCellValue() + " ");
                        } else {
                            String.format(str += cell.getStringCellValue() + " ");
                        }
                    }
                }
            }

            System.out.println(str);
        }
    }
}
