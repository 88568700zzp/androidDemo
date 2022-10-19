package com.zzp.lib.document;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;



/**
 * A simple WOrdprocessingML document created by POI XWPF API
 */
public class SimpleDocument {

    public static void main(String[] args) throws Exception {
        //parseWord();
        //parseExcel();
    }

    private static void parseWord() throws Exception{
        String url = "C://android//file//simple.docx";
        String output_url = "C://android//file//simple1.docx";
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(url))) {

            XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
            if(headerFooterPolicy != null) {
                //获取页眉
                /*String header = headerFooterPolicy.getDefaultHeader().getText();
                System.out.println("***页眉 ***" + header);
                //获取页脚
                String footer = headerFooterPolicy.getDefaultFooter().getText();
                System.out.println("***页脚 ***" + footer);*/
            }


            try{
                int pageCount = doc.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();//总页数
                System.out.println("page:" + pageCount);
            }catch (Exception e){

            }

            int i = 0;

            for(XWPFParagraph xwpfParagraph:doc.getParagraphs()){
                i++;
                System.out.println(i + ":" + xwpfParagraph.getText() + " alignment:" + xwpfParagraph.getAlignment());
                System.out.println("between:" + xwpfParagraph.getSpacingBetween() + " spacingBefore:" + xwpfParagraph.getSpacingBefore() + " beforeLine:" + xwpfParagraph.getSpacingBeforeLines());
                List<XWPFRun> runs = xwpfParagraph.getRuns();
                if(runs.size() > 0) {
                    System.out.println();
                    for (XWPFRun run : runs) {
                        System.out.println("run fontName:" + run.getFontFamily(XWPFRun.FontCharRange.hAnsi) + " fontSize:" + run.getFontSize() + " color:" + run.getColor() + " space:" + run.getCharacterSpacing());
                        System.out.println("run text:" + run.toString());
                    }
                    System.out.println();
                }
            }

            /*try {
                System.out.println("add:"+doc.addPictureData(new FileInputStream("C://android//icon.png"), PICTURE_TYPE_PNG));
            }catch (Exception e){
                System.out.println(e);
            }*/

            for(XWPFPictureData pictureData : doc.getAllPictures()){
                System.out.println(pictureData);
            }

            /*XWPFParagraph createXWPFParagraph = doc.createParagraph();
            XWPFRun createXWPFRun1 = createXWPFParagraph.createRun();
            XWPFPicture picture = createXWPFRun1.addPicture(new FileInputStream("C://android//icon.png"), PICTURE_TYPE_PNG,"icon.png",133,136);
            createXWPFRun1.setText("标题1");
            createXWPFRun1.setBold(true);

            XWPFRun createXWPFRun2 = createXWPFParagraph.createRun();
            createXWPFRun2.setText("标题2");
            createXWPFRun2.setFontSize(20);*/


            //doc.write(new FileOutputStream(output_url));

        }
    }

    private static void parseExcel() throws Exception{
        String url = "C://android//file//format.xlsx";
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new File(url));
            Iterator<Sheet> iterator = workbook.sheetIterator();
            while (iterator.hasNext()){
                parseSheet(iterator.next(),workbook);
            }
        }catch (Exception e){
            throw e;
        }
    }

    private static void parseSheet(Sheet sheet, XSSFWorkbook workbook){
        int firstRowNum =  sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();

        System.out.println("sheet:" + sheet.getSheetName());
        System.out.println("activeCell:" + sheet.getActiveCell().toString() + " firstRowNum:" + firstRowNum + " lastRowNum:" + lastRowNum);
        for(int i = firstRowNum;i <= lastRowNum;i++){
            Row row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            System.out.println("row:" + row.getHeightInPoints());
            System.out.println();
            for(int j = row.getFirstCellNum();j < row.getLastCellNum();j++){
                Cell cell = row.getCell(j);
                switch (cell.getCellTypeEnum()) { // 不同的数据类型
                    case STRING:
                        CellStyle cellStyle = cell.getCellStyle();
                        XSSFFont font = (XSSFFont) sheet.getWorkbook().getFontAt(cellStyle.getFontIndex());
                        System.out.println(cell.getAddress() + " STRING:" + cell.getStringCellValue());
                        System.out.println("cellStyle h:" + cellStyle.getAlignmentEnum() + " v:" + cellStyle.getVerticalAlignmentEnum() + " width:" + sheet.getColumnWidthInPixels(j));
                        System.out.println( "color:" + font.getXSSFColor().getARGBHex()  + " fontHeight:" + font.getFontHeightInPoints());
                        break; // 字符串类型
                    case NUMERIC:
                        System.out.println(cell.getAddress() + " NUMERIC:" + cell.getNumericCellValue());
                        break; // 数值类型
                    case BOOLEAN:
                        System.out.println(cell.getAddress() + " BOOLEAN:" + cell.getBooleanCellValue());
                        break; // 布尔类型
                    case FORMULA:
                        System.out.println(cell.getAddress() + " FORMULA:" + cell.getCellFormula());
                        break; // 公式类型
                    case BLANK:
                        System.out.println(cell.getAddress() + " BLANK");
                        break; // 空白类型
                }
            }
            System.out.println();
        }

        for(CellRangeAddress cellRangeAddress :sheet.getMergedRegions()){
            System.out.println(cellRangeAddress.toString());
        }
    }



}