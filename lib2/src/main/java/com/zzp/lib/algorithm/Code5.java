package com.zzp.lib.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找最大矩形数
 * Created by samzhang on 2021/8/16.
 */
public class Code5 {
    public static void getMaxRectCount(int row,int column,int hitRow,int hitColumn){

        long time = System.currentTimeMillis();

        int totalColumn = 0;
        if(hitColumn == 0){
            totalColumn = column ;
        }else if(hitColumn == (column - 1)){
            totalColumn = column ;
        }else{
            totalColumn = (hitColumn + 1) * (column - hitColumn);
        }

        int totalRow = 0;
        if(hitRow == 0){
            totalRow = row ;
        }else if(hitRow == (row - 1)){
            totalRow = row;
        }else{
            totalRow = (hitRow + 1) * (row - hitRow);
        }


        System.out.println("newTotalCount:" + (totalColumn * totalRow)+ " costTime:" + (System.currentTimeMillis() - time));
    }

    public static void checkMaxRectCount(){
        int row = 1000;
        int column = 40;

        int hitRow = 5;
        int hitColumn = 1;

        int totalCount = 0;

        long time = System.currentTimeMillis();
        for(int startRow = 0;startRow < row;startRow++) {
            for(int endRow =startRow;endRow < row;endRow++) {
                for (int startColumn = 0; startColumn < column; startColumn++) {
                    for (int endColumn = startColumn; endColumn < column; endColumn++) {
                        if((endColumn - startColumn) != (endRow - startRow) && hitColumn >= startColumn && hitColumn <= endColumn && hitRow >= startRow && hitRow <= endRow){
                            totalCount++;
                        }
                    }
                }
            }
        }
        System.out.println("totalCount:" + totalCount + " costTime:" + (System.currentTimeMillis() - time));

        getMaxRectCount(row,column,hitRow,hitColumn);

    }

}
