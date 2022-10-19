package com.zzp.lib.algorithm;

/**
 * Created by samzhang on 2021/8/16.
 */
public class Code3 {
    public static void getRounts(int row,int column){
        int[][] routes = new int[row][column];
        for(int i =0 ;i < row;i++){
            routes[i][0] = 1;
        }
        for(int i =0 ;i < column;i++){
            routes[0][i] = 1;
        }
        for(int i = 1;i < row;i++){
            for(int j = 1;j < column;j++){
                routes[i][j] = routes[i - 1][j] + routes[i][j - 1];
            }
        }
        System.out.println(routes[row - 1][column - 1]);
    }


}
