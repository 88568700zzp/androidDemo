package com.zzp.lib.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 查找最大矩形数
 * Created by samzhang on 2021/8/16.
 */
public class Code6 {
    public static List<List<Integer>> threeSum(int[] nums) {
        int temp = 0;
        int index = Integer.MIN_VALUE;

        List<List<Integer>> result = new ArrayList();
        if(nums.length < 3){
            return result;
        }

        for(int i = 0;i < nums.length;i++){
            for(int j =i + 1;j< nums.length;j++){
                if(nums[i] > nums[j]){
                    temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }
            if(nums[i] <= 0){
                index = i;
            }
        }

        if(nums[0] == 0 && nums[nums.length - 1] == 0){
            List<Integer> list = new ArrayList();
            list.add(0);
            list.add(0);
            list.add(0);
            result.add(list);
            return result;
        }

        int lastNumI = -1;
        int lastNumJ = -1;
        int lastNumZ = -1;

        System.out.println(Arrays.toString(nums) + " zeroindex:" + index);

        int lastHitZIndex = 0;
        for(int i = 0;i <= index;i++){
            if(lastNumI >= 0 && lastNumJ > i && lastNumZ > i && nums[lastNumI] == nums[i]){
                continue;
            }
            lastHitZIndex = i + 1;
            for(int j = nums.length - 1;j >= index;j--){
                if(lastNumJ >= 0 && lastNumZ < j && lastNumI < j && nums[lastNumJ] == nums[j]){
                    System.out.println("lastNumI:" + lastNumI + " lastNumZ:" + lastNumZ + " lastNumJ:" + lastNumJ + " j:" + j);
                    System.out.println("lastNumI:" + nums[lastNumI] + " lastNumZ:" + nums[lastNumZ] + " lastNumJ:" + nums[lastNumJ]);
                    continue;
                }
                int startIndex = Math.max(i + 1,lastHitZIndex);
                int endIndex = j - 1;
                for(int z = startIndex;z <= endIndex;z++){
                    int total = nums[i] + nums[j] + nums[z];
                    if(total == 0){
                        lastHitZIndex = z;
                        List<Integer> list = new ArrayList();
                        list.add(nums[i]);
                        list.add(nums[z]);
                        list.add(nums[j]);
                        result.add(list);
                        lastNumI = i;
                        lastNumJ= j;
                        lastNumZ= z;
                        break;
                    }else if(total > 0){
                        lastHitZIndex = z;
                        break;
                    }
                }
            }
        }

        System.out.println(Arrays.toString(result.toArray()));

        return result;
    }
}
