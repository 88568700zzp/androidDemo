package com.zzp.applicationkotlin.view.cell;

public class CellMergeInfo {

    private int startRowIndex;
    private int startColumnIndex;

    private int endRowIndex;
    private int endColumnIndex;

    public CellMergeInfo(int startRowIndex, int startColumnIndex, int endRowIndex, int endColumnIndex) {
        this.startRowIndex = startRowIndex;
        this.startColumnIndex = startColumnIndex;
        this.endRowIndex = endRowIndex;
        this.endColumnIndex = endColumnIndex;
    }

    public int getStartRowIndex() {
        return startRowIndex;
    }

    public int getStartColumnIndex() {
        return startColumnIndex;
    }

    public int getEndRowIndex() {
        return endRowIndex;
    }

    public int getEndColumnIndex() {
        return endColumnIndex;
    }
}
