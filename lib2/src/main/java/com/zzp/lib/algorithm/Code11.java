package com.zzp.lib.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 四数之和
 */
public class Code11 {

    public int minimumTotal(List<List<Integer>> triangle) {
        for(int i = 0;i < triangle.size();i++){
            List<Integer> value = triangle.get(i);
            for(int j = 0;j < value.size();j++){
                if(j == 0 && i == 0){
                    continue;
                }else if(j == 0){
                    int data = value.get(0) + triangle.get(i - 1).get(0);
                    value.set(0,data);
                }
            }
        }
        return 10;
    }

}
