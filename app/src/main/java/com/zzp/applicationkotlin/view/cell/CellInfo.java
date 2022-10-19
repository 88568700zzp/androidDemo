package com.zzp.applicationkotlin.view.cell;

import android.graphics.Color;
import android.view.Gravity;

public class CellInfo {

    final int rowIndex;

    final int columnIndex;

    String content;

    float textSize = 11f;

    int textColor = Color.BLACK;

    int gravity = Gravity.NO_GRAVITY;

    int cellWidth = 50;

    int cellHeight = 50;

    boolean isMerge = false;
    int endRowIndex;
    int endColumnIndex;

    public CellInfo(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public CellInfo(int startRowIndex, int startColumnIndex,int endRowIndex,int endColumnIndex) {
        this.rowIndex = startRowIndex;
        this.columnIndex = startColumnIndex;
        this.endRowIndex = endRowIndex;
        this.endColumnIndex = endColumnIndex;
        this.isMerge = true;
    }

    public void setCellWidthAndHeight(int cellWidth,int cellHeight){
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }



    public int getRowIndex() {
        return rowIndex;
    }


    public int getColumnIndex() {
        return columnIndex;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }
}
