package com.zzp.lib.algorithm;

import java.util.Arrays;

/**
 * Created by samzhang on 2021/8/3.
 */
public class Code1 {

    private int peopleCount = 4;

    private int[] data = new int[4];

    public void mainCode(){
        int num = 6;
        float totalCount = 0;

        getLeft(num,4);
    }

    private void getLeft(int currentNum,int count){
        //if(currentNum > 0) {
        if (count == 1) {//只剩一个人分配给他
            data[peopleCount - 1] = currentNum;
            //System.out.println("setData:" + (peopleCount - count + i));
            System.out.println("userNum:" + currentNum + " count:" + 1);
            System.out.println(Arrays.toString(data));
        }else {
            System.out.println("into for count currentNum:" + currentNum + " count:" + count);
            for (int i = peopleCount - count; i < peopleCount; i++) {
                {
                    int newLeftNum = currentNum;
                    System.out.println("into for num i:" + i + " currentNum:" + currentNum + " count:" + count);
                    for (int num = 0; num <= currentNum; num++) {
                        data[i] = num;
                        //System.out.println("setData1:" + (peopleCount - count + i));
                        int leftNum = currentNum - num;
                        System.out.println("leftNum:" + currentNum + " userNum:" + num + " count:" + count);
                        getLeft(leftNum, count - 1);
                    }
                }
            }
        }
       /* }else{
            for (int i = 0; i < count; i++){
                data[peopleCount - count + i] = 0;
            }
            System.out.println(Arrays.toString(data));
        }*/
    }

    private float getCode1(int code){
        switch (code){
            case 1:
                return 2f;
            case 2:
                return 5f;
            case 3:
                return 6.5f;
            case 4:
                return 8f;
            case 5:
                return 8.5f;
            case 6:
                return 8.5f;
        }
        return 0f;
    }

    private float getCode2(int code){
        switch (code){
            case 1:
                return 2f;
            case 2:
                return 4f;
            case 3:
                return 5f;
            case 4:
                return 5.5f;
            case 5:
                return 6f;
            case 6:
                return 6.5f;
        }
        return 0f;
    }

    private float getCode3(int code){
        switch (code){
            case 1:
                return 2.5f;
            case 2:
                return 6f;
            case 3:
                return 8.5f;
            case 4:
                return 10f;
            case 5:
                return 11f;
            case 6:
                return 11.5f;
        }
        return 0f;
    }

    private float getCode4(int code){
        switch (code){
            case 1:
                return 2.5f;
            case 2:
                return 4f;
            case 3:
                return 5f;
            case 4:
                return 6f;
            case 5:
                return 6.5f;
            case 6:
                return 7f;
        }
        return 0f;
    }
}
