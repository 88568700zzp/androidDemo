package com.zzp.applicationkotlin

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.zzp.applicationkotlin.view.cell.CellInfo
import com.zzp.applicationkotlin.view.cell.CellMergeInfo
import com.zzp.applicationkotlin.view.doll.dp
import kotlinx.android.synthetic.main.activity_format.*
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun

class FormatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_format)

        getWord()

        parseExcel()
    }

    private fun getWord(){
        try {
            var docx =  XWPFDocument(assets.open("simple.docx"))
            docx.paragraphs.forEach { paragraph->
                addText(paragraph)
            }
        }catch (e:Exception){

        }
    }

    private fun addText(paragraph : XWPFParagraph){
        val runs: List<XWPFRun> = paragraph.runs

       var stringBuilder = if(runs.isEmpty()) {
           SpannableStringBuilder(paragraph.text)
       }else{
           SpannableStringBuilder()
       }
        if(runs.isNotEmpty()) {
            for (run in runs) {
                var spanStr = SpannableString(run.text())
                if(run.color != null) {
                    spanStr.setSpan(ForegroundColorSpan(Color.parseColor("#" + run.color)),0,run.text().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                if(run.fontSize > 0){
                    spanStr.setSpan(AbsoluteSizeSpan(run.fontSize,true),0,run.text().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                }

                stringBuilder.append(spanStr)

                //println("run fontName:" + run.getFontFamily(XWPFRun.FontCharRange.hAnsi) + " fontSize:" + run.fontSize + " color:" + run.color + " space:" + run.characterSpacing)
            }
        }


        var editText =  EditText(this)
        editText.background = null
        editText.textSize = 13f
        editText.gravity = if(paragraph.alignment == ParagraphAlignment.RIGHT){
            Gravity.RIGHT
        }else if(paragraph.alignment == ParagraphAlignment.CENTER){
            Gravity.CENTER
        }else{
            Gravity.LEFT
        }

        editText.text = stringBuilder

        content.addView(editText,LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
    }

    private fun parseExcel() {
        try {
            val workbook = XSSFWorkbook(assets.open("format.xlsx"))
            val iterator = workbook.sheetIterator()
            while (iterator.hasNext()) {
                parseSheet(iterator.next(), workbook)
            }
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    private fun parseSheet(sheet: Sheet, workbook: XSSFWorkbook) {
        var cellWidth = 100.dp
        var cellHeight = 50.dp

        var cellInfos = ArrayList<CellInfo>()

        val firstRowNum = sheet.firstRowNum
        val lastRowNum = sheet.lastRowNum
        var maxColumn = 0;
        for (i in firstRowNum..lastRowNum) {
            val row = sheet.getRow(i) ?: continue
            for (j in row.firstCellNum until row.lastCellNum) {
                if(maxColumn < row.lastCellNum){
                    maxColumn = row.lastCellNum.toInt();
                }
                val cell = row.getCell(j)
                when (cell.cellTypeEnum) {
                    CellType.STRING -> {
                        val cellStyle = cell.cellStyle
                        val font = sheet.workbook.getFontAt(cellStyle.fontIndex) as XSSFFont

                        var cellInfo = CellInfo(i,j)
                        cellInfo.setCellWidthAndHeight(cellWidth,
                            cellHeight)
                        cellInfo.content = cell.stringCellValue


                        cellInfo.textColor = Color.parseColor("#" + font.xssfColor.argbHex)
                        cellInfo.textSize = font.fontHeightInPoints.toFloat()

                        cellInfos.add(cellInfo)
                        /*println(cell.address.toString() + " STRING:" + cell.stringCellValue)
                        println(
                            "cellStyle h:" + cellStyle.alignmentEnum + " v:" + cellStyle.verticalAlignmentEnum + " width:" + sheet.getColumnWidth(
                                j
                            )
                        )
                        println("color:" + font.xssfColor.argbHex + " fontHeight:" + font.fontHeightInPoints)*/
                    }
                    /*CellType.NUMERIC -> println(cell.address.toString() + " NUMERIC:" + cell.numericCellValue)
                    CellType.BOOLEAN -> println(cell.address.toString() + " BOOLEAN:" + cell.booleanCellValue)
                    CellType.FORMULA -> println(cell.address.toString() + " FORMULA:" + cell.cellFormula)
                    CellType.BLANK -> println(cell.address.toString() + " BLANK")*/
                }
            }
        }

        var mergeInfos = ArrayList<CellMergeInfo>();

        for (cellRangeAddress in sheet.mergedRegions) {
            mergeInfos.add(CellMergeInfo(cellRangeAddress.firstRow,cellRangeAddress.firstColumn,cellRangeAddress.lastRow,cellRangeAddress.lastColumn))
            for( cellInfo in cellInfos){
                if(cellInfo.rowIndex == cellRangeAddress.firstRow && cellInfo.columnIndex == cellRangeAddress.firstColumn){
                    cellInfo.setCellWidthAndHeight(cellWidth * (cellRangeAddress.lastColumn - cellRangeAddress.firstColumn + 1),
                        cellHeight * (cellRangeAddress.lastRow - cellRangeAddress.firstRow + 1))
                    break;
                }
            }
        }

        cellFormatLayout.setDataInfo(lastRowNum+ 1,maxColumn + 1,cellInfos,mergeInfos)
    }


}