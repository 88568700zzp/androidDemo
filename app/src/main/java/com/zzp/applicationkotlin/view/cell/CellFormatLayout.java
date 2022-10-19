package com.zzp.applicationkotlin.view.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zzp.applicationkotlin.view.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格布局
 */
public class CellFormatLayout extends ViewGroup implements View.OnFocusChangeListener {

    private int mDefaultWidth;
    private int mDefaultHeight;

    private List<CellInfo> mData = new ArrayList<>();

    private int mRowNum = 0;
    private int mColumnNum = 0;

    private float[] mRowHeights;
    private float[] mColumnWidths;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mStrokeWidth = 1;
    private int mLineColor = Color.BLACK;

    private int mFocusBorderWidth = 1;

    private Rect mFocusRect = new Rect();
    private float mFocusStrokeWidth = 1f;
    private int mFocusLineColor = Color.YELLOW;

    private List<CellMergeInfo> mCellMergeInfoList = new ArrayList<>();
    private List<Position> mPositionList = new ArrayList<>();

    public CellFormatLayout(Context context) {
        super(context);
        init();
    }

    public CellFormatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CellFormatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mDefaultWidth = (int) ScreenUtils.dpToPx(100);
        mDefaultHeight = (int) ScreenUtils.dpToPx(50);
        mFocusBorderWidth = (int) ScreenUtils.dpToPx(2);

        mStrokeWidth = (int) ScreenUtils.dpToPx(1);
        mFocusStrokeWidth = 2 * mStrokeWidth;

        mPaint.setStyle(Paint.Style.STROKE);

    }

    private void testData3(){
        mRowNum = 3;
        mColumnNum = 3;

        mData.clear();

        for(int row = 0;row < 2;row++){
            for(int column = 0;column < 3;column++){
                CellInfo cellInfo = new CellInfo(row,column);
                cellInfo.setCellWidthAndHeight((int) mColumnWidths[column],(int) mRowHeights[row]);
                cellInfo.setContent("row:" + row + " column:" + column);

                mData.add(cellInfo);
            }
        }

        {

            CellInfo cellInfo1 = new CellInfo(2, 0);
            cellInfo1.setCellWidthAndHeight(mDefaultWidth, mDefaultHeight);
            cellInfo1.setContent("row:2" +" column:0");
            mData.add(cellInfo1);

            CellInfo cellInfo = new CellInfo(2, 1);
            cellInfo.setCellWidthAndHeight(mDefaultWidth * 2, mDefaultHeight);
            cellInfo.setContent("merge");

            mData.add(cellInfo);

        }

        mCellMergeInfoList.add(new CellMergeInfo(2,1,2,2));
    }

    public void setDataInfo(int rowNum,int columnNum,List<CellInfo> cellInfos,List<CellMergeInfo> mergeInfos){
        mRowNum = rowNum;
        mColumnNum = columnNum;

        mData.clear();
        mData.addAll(cellInfos);

        mCellMergeInfoList.clear();
        mCellMergeInfoList.addAll(mergeInfos);

        generateFormat();
    }

    private void initRowSizeInfo(){
        mRowHeights = new float[mRowNum];
        mColumnWidths = new float[mColumnNum];
        for(int i = 0; i < mRowNum;i++){
            mRowHeights[i] = mDefaultHeight;
        }
        for(int i = 0; i < mColumnNum;i++){
            mColumnWidths[i] = mDefaultWidth;
        }
    }

    private void generateFormat(){
        initRowSizeInfo();

        removeAllViews();

        for(int i = 0;i < mData.size();i++) {
            CellInfo cellInfo = mData.get(i);

            LayoutParams layoutParams = new LayoutParams(cellInfo.getCellWidth(), cellInfo.getCellHeight(), cellInfo);
            EditText editText = new EditText(getContext());
            editText.setTag(cellInfo);
            editText.setText(cellInfo.getContent());
            editText.setGravity(cellInfo.getGravity());
            editText.setBackground(null);
            editText.setTextSize(cellInfo.getTextSize());
            editText.setTextColor(cellInfo.getTextColor());
            editText.setPadding(mFocusBorderWidth, mFocusBorderWidth, mFocusBorderWidth, mFocusBorderWidth);
            editText.setOnFocusChangeListener(this);

            addView(editText, layoutParams);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isEmptyData()){
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }else {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);

                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

                child.measure(MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY));
            }

            setMeasuredDimension(getContentWidth() + getLeftSpace() + getRightSpace(), getContentHeight() + getTopSpace() + getBottomSpace());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        if(isEmptyData()){
            return;
        }
        for(int i = 0;i < getChildCount();i++){
            View child = getChildAt(i);
            if(child.getVisibility() == View.GONE){
                continue;
            }

            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            CellInfo cellInfo = layoutParams.cellInfo;

            int startX = (calculateStartXByIndex(cellInfo.columnIndex));
            int startY = (calculateStartYByIndex(cellInfo.rowIndex));

            child.layout(startX ,startY ,startX + child.getMeasuredWidth(),startY + child.getMeasuredHeight());

        }
    }

    private boolean isEmptyData(){
        return mData.size() == 0 || mRowNum == 0 || mColumnNum == 0;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(isEmptyData()){
            super.dispatchDraw(canvas);
        }else {
            drawHorizontalLine(canvas);
            drawVerticalLine(canvas);

            super.dispatchDraw(canvas);

            drawFocusRect(canvas);
        }
    }

    //画水平线
    private void drawHorizontalLine(Canvas canvas){
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mLineColor);

        for(int i = 0;i <= mRowNum;i++){

            float hy = calculateStartYByIndex(i);

            getColumnMergeList(i);

            for(Position position:mPositionList){
                float startX = calculateStartXByIndex(position.index1);
                float endX = calculateStartXByIndex(position.index2);
                canvas.drawLine(startX, hy, endX, hy, mPaint);
            }
        }
    }



    /**
     * 获取合并表格拆分数组
     * @param rowIndex
     * @return 列索引数组列表
     */
    private List<Position> getColumnMergeList(int rowIndex){
        mPositionList.clear();

        int startIndex = 0;

        for(CellMergeInfo cellMergeInfo: mCellMergeInfoList){
            if(cellMergeInfo.getStartRowIndex() < rowIndex && rowIndex <= cellMergeInfo.getEndRowIndex()){
                if(cellMergeInfo.getStartColumnIndex() > 0) {
                    mPositionList.add(new Position(startIndex, cellMergeInfo.getStartColumnIndex()));
                }
                startIndex = cellMergeInfo.getEndColumnIndex() + 1;
            }
        }
        if(startIndex != mColumnNum) {
            mPositionList.add(new Position(startIndex, mColumnNum));
        }
        return mPositionList;
    }

    /**
     * 获取合并表格拆分数组
     * @param columnIndex
     * @return 行索引数组列表
     */
    private List<Position> getRowMergeList(int columnIndex){
        mPositionList.clear();

        int startIndex = 0;


        for(CellMergeInfo cellMergeInfo: mCellMergeInfoList){
            if(cellMergeInfo.getStartColumnIndex() < columnIndex && columnIndex <= cellMergeInfo.getEndColumnIndex()){
                if(cellMergeInfo.getStartRowIndex() > 0){
                    mPositionList.add(new Position(startIndex,cellMergeInfo.getStartRowIndex()));
                }
                startIndex = cellMergeInfo.getEndRowIndex() + 1;
            }
        }
        if(startIndex != mRowNum){
            mPositionList.add(new Position(startIndex, mRowNum));
        }
        return mPositionList;
    }

    //画竖直线
    private void drawVerticalLine(Canvas canvas){
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mLineColor);

        for(int i = 0;i <= mColumnNum;i++){
            float hx = calculateStartXByIndex(i);

            getRowMergeList(i);

            for(Position position:mPositionList){
                float startY = calculateStartYByIndex(position.index1);
                float endY = calculateStartYByIndex(position.index2);
                canvas.drawLine(hx,startY,hx,endY ,mPaint);
            }

        }
    }

    private void drawFocusRect(Canvas canvas){
        if(!mFocusRect.isEmpty()){
            mPaint.setStrokeWidth(mFocusStrokeWidth);
            mPaint.setColor(mFocusLineColor);
            canvas.drawRect(mFocusRect,mPaint);
        }
    }

    public int getContentWidth(){
        return calculateWidthByIndex(mColumnNum - 1);
    }

    public int getContentHeight(){
        return  calculateHeightByIndex(mRowNum - 1);
    }

    private int getLeftSpace(){
        return mStrokeWidth + getPaddingLeft();
    }

    private int getTopSpace(){
        return mStrokeWidth + getPaddingTop();
    }

    private int getRightSpace(){
        return mStrokeWidth + getPaddingRight();
    }

    private int getBottomSpace(){
        return mStrokeWidth + getPaddingBottom();
    }

    private int calculateWidthByIndex(int columnIndex){
        int sum = 0;
        for(int i = 0;i <= columnIndex;i++){
            sum += mColumnWidths[i];
        }
        return sum;
    }



    private int calculateHeightByIndex(int rowIndex){
        int sum = 0;
        for(int i = 0;i <= rowIndex;i++){
            sum += mRowHeights[i];
        }
        return sum;
    }

    private int calculateStartXByIndex(int columnIndex){
        int sum = getLeftSpace();
        for(int i = 0;i < columnIndex;i++){
            sum += mColumnWidths[i];
        }
        return sum;
    }

    private int calculateStartYByIndex(int rowIndex){
        int sum = getTopSpace();
        for(int i = 0;i < rowIndex;i++){
            sum += mRowHeights[i];
        }
        return sum;
    }

    public void clearData(){
        mRowNum = 0;
        mColumnNum = 0;
        mCellMergeInfoList.clear();
        mData.clear();
    }

    @Override
    public void onFocusChange(View editText, boolean hasFocus) {
        if(hasFocus){
            mFocusRect.set(editText.getLeft(),editText.getTop(),editText.getRight(),editText.getBottom());
        }else{
            mFocusRect.setEmpty();
        }
        postInvalidate();
    }

    class LayoutParams extends ViewGroup.LayoutParams{

        public  CellInfo cellInfo;

        public LayoutParams(int width, int height,CellInfo cellInfo) {
            super(width, height);
            this.cellInfo = cellInfo;
        }

    }

    class Position{
        int index1;
        int index2;

        public Position(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }
    }
}
