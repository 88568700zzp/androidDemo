package com.zzp.lib.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 四数之和
 */
public class Code10 {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        int tmp = 0;
        for(int i = 0;i < candidates.length;i++){
            for(int j = i + 1;j < candidates.length;j++){
                if(candidates[i] > candidates[j]){
                    tmp = candidates[i];
                    candidates[i] = candidates[j];
                    candidates[j] = tmp;
                }
            }
        }

        List<List<Integer>> result = new ArrayList<>();
        if(target < candidates[0]){
            return result;
        }

        catchLeft(result,new ArrayList<Integer>(),candidates,target,0);

        return result;
    }

    private void catchLeft(List<List<Integer>> result,List<Integer> nums,int[] candidates, int target,int startIndex){
        for(int i = startIndex;i < candidates.length;i++){
            int num = candidates[i];
            if(num < target){
                int left = target - num;
                List<Integer> newNums = new ArrayList<>(nums);
                newNums.add(num);
                catchLeft(result,newNums,candidates,left,i);
            }else if(num == target){
                nums.add(num);
                result.add(nums);
            }

            if(num >= target){
                break;
            }
        }
    }
}
