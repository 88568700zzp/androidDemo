package com.zzp.lib;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelTest {

    public void test(){
        try {
            File file = new File("C:\\Users\\88568\\1570588970411233280\\1570589054456696832.xlsx");

            //将输出的流对象引入到解析excel文件的对象
            XSSFWorkbook wb = new XSSFWorkbook(file);

            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                XSSFSheet sheet = wb.getSheetAt(i);
                Row row = null;
                int lastRowNum = sheet.getLastRowNum();
                List<CellRangeAddress> regions = sheet.getMergedRegions();
                for(CellRangeAddress address:regions){
                    System.out.println(address.toString());
                }

                for (int y = 0; y < lastRowNum; y++) {
                    row = sheet.getRow(y);
                    if (null != row) {
                        //获取每一列值
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            CellAddress cellAddress = cell.getAddress();
                            String value = new String(cell.getStringCellValue().getBytes(StandardCharsets.UTF_8),Charset.forName("gbk"));
                            System.out.println("rowIndex:" + cellAddress.getRow() + " columnIndex:" + cellAddress.getColumn() + " value:" + value);
                        }
                        //System.out.println(row);
                    }
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }


    }
}
