package com.zzp.lib.algorithm;

/**
 * Created by samzhang on 2021/8/16.
 */
public class Code2 {
    public static void bubble(int[] nums){
        for(int i = 0;i <nums.length - 1;i++){
            for(int j = nums.length - 1;j > i;j--){
                if(nums[j] < nums[j-1]) {
                    swap(nums, j-1, j);
                }

            }
        }
    }

    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void quickSort(int[] nums,int startIndex,int endIndex){
        int index = (endIndex - startIndex)/2;
        int data = nums[index];
        for(int i = startIndex;i < endIndex;i++){
            if(i <= index){
                if(nums[i] > data){

                }
            }else{
                if(nums[i] < data){

                }
            }
        }
    }
}
