package operation.wrodbook;

import operation.wrodbook.readExcel.ReadFromExcel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.jws.soap.SOAPBinding;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OperationExcel {


    //创建一个空的excel 并在指定的行列写入Hello World
    public static void createEmptyExcel(String wordName) throws IOException {

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:\\file\\data.xlsx"));

        XSSFWorkbook workbook = new XSSFWorkbook();




        XSSFSheet sheet1 = workbook.createSheet("my-sheet-1");

        XSSFRow row = sheet1.createRow(1);

        XSSFCell cell = row.createCell(1);

        XSSFCellStyle cellStyle = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setColor(IndexedColors.RED.getIndex());
        font.setFontHeightInPoints((short) 15);
        font.setBold(true);

        cellStyle.setFont(font);

        cell.setCellValue("Hello World!!!");

        workbook.write(bos);
        workbook.close();
        bos.flush();
        bos.close();

        //官方方法
        Workbook[] wbs = new Workbook[] { new HSSFWorkbook(), new XSSFWorkbook() };
        for(int i=0; i<wbs.length; i++) {
            Workbook wb = wbs[i];
            CreationHelper createHelper = wb.getCreationHelper();
            // create a new sheet
            Sheet s = wb.createSheet();
            // declare a row object reference
            Row r = null;
            // declare a cell object reference
            Cell c = null;
            // create 2 cell styles
            CellStyle cs = wb.createCellStyle();
            CellStyle cs2 = wb.createCellStyle();
            DataFormat df = wb.createDataFormat();
            // create 2 fonts objects
            Font f = wb.createFont();
            Font f2 = wb.createFont();
            // Set font 1 to 12 point type, blue and bold
            f.setFontHeightInPoints((short) 12);
            f.setColor( IndexedColors.RED.getIndex() );
            f.setBold(true);
            // Set font 2 to 10 point type, red and bold
            f2.setFontHeightInPoints((short) 10);
            f2.setColor( IndexedColors.RED.getIndex() );
            f2.setBold(true);
            // Set cell style and formatting
            cs.setFont(f);
            cs.setDataFormat(df.getFormat("#,##0.0"));
            // Set the other cell style and formatting
            cs2.setDataFormat(df.getFormat("text"));
            cs2.setFont(f2);
            // Define a few rows
            for(int rownum = 0; rownum < 30; rownum++) {
                r = s.createRow(rownum);
                for(int cellnum = 0; cellnum < 10; cellnum += 2) {
                    c = r.createCell(cellnum);
                    Cell c2 = r.createCell(cellnum+1);

                    c.setCellValue((double)rownum + (cellnum/10));
                    c2.setCellValue(
                            createHelper.createRichTextString("Hello! " + cellnum)
                    );
                }
            }

            // Save
            String filename = "D:\\file\\workbook.xls";
            if(wb instanceof XSSFWorkbook) {
                filename = filename + "x";
            }

            FileOutputStream out = new FileOutputStream(filename);
            wb.write(out);
            out.close();
        }



    }

    public static void main(String[] args) {

        OperationExcelFromList operationExcelFromList = new OperationExcelFromList();
        ReadFromExcel readFromExcel = new ReadFromExcel();

        //写excele
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(new UserInfo(1, "abc", "北京", " ", 162.1));
        userInfos.add(new UserInfo(2, "mh", "上海", "2020-12-12", 159.7));
        userInfos.add(new UserInfo(3, "lol", "深圳", "2020-12-13", 168.0));
        userInfos.add(new UserInfo(4, "kl", "杭州", "2020-12-14", 166.0));
        operationExcelFromList.ExcelFromList("D:\\\\file\\\\test",userInfos);


        //读excel
        try {
            readFromExcel.ReadExcel("D:\\\\file\\\\test.xlsx","sheet-1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
