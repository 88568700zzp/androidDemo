package com.zzp.lib.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 四数之和
 */
public class Code9 {

    public void test(){
        int[] nums = {0,0,0,-1000000000,-1000000000,-1000000000,-1000000000 };
        List<List<Integer>> result = fourSum(nums, -1000000000);
        for(List<Integer> data : result){
            System.out.println(Arrays.toString(data.toArray()));
        }

    }

    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums.length < 4){
            return result;
        }
        int head = 0;
        for(int i = 0;i < nums.length - 1;i++){
            for(int j = i + 1;j < nums.length ;j++){
                if(nums[i] > nums[j]){
                    head = nums[i];
                    nums[i] = nums[j];
                    nums[j] = head;
                }
            }
        }
        //System.out.println(Arrays.toString(nums));
        long minHead = nums[0] + nums[1];

        List<Info> firsts = new ArrayList<Info>();
        List<Info> data = new ArrayList<>();
        for(int i = 0;i < nums.length-1;i++){
            for (int j = i + 1; j < nums.length; j++) {
                long total = nums[i] + nums[j];
                if(total + minHead > target){
                    break;
                }else {
                    data.add(new Info(i, j, nums[i] + nums[j]));
                }
            }
        }
        int preV1 = Integer.MIN_VALUE;
        int preV2 = Integer.MIN_VALUE;
        int preV3 = Integer.MIN_VALUE;
        int preV4 = Integer.MIN_VALUE;

        for (int i = 0; i < data.size() - 1;i ++){
            Info first = data.get(i);
            if(!contain(nums,firsts,first)) {
                for (int j = i + 1; j < data.size(); j++) {
                    Info sec = data.get(j);
                    long total = first.total+ sec.total;
                    if (sec.position1 > first.position2 ) {
                        if ((total == target) && !(preV1 == nums[first.position1] && preV2 == nums[first.position2] && preV3 == nums[sec.position1] && preV4 == nums[sec.position2])) {
                            preV1 = nums[first.position1];
                            preV2 = nums[first.position2];
                            preV3 = nums[sec.position1];
                            preV4 = nums[sec.position2];

                            List<Integer> ints = new ArrayList<>();
                            ints.add(nums[first.position1]);
                            ints.add(nums[first.position2]);
                            ints.add(nums[sec.position1]);
                            ints.add(nums[sec.position2]);
                            result.add(ints);

                            firsts.add(first);
                        }
                    }
                }
            }
        }

        return result;
    }

    public boolean contain(int[] nums, List<Info> firsts,Info data){
        if(firsts.isEmpty()){
            return false;
        }
        for(Info first :firsts){
            if(nums[first.position1] == nums[data.position1] && nums[first.position2] == nums[data.position2]){
                return true;
            }
        }
        return false;
    }


    class Info{
        int position1;
        int position2;
        long total;

        public Info(int position1, int position2, long total) {
            this.position1 = position1;
            this.position2 = position2;
            this.total = total;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "position1=" + position1 +
                    ", position2=" + position2 +
                    ", total=" + total +
                    '}';
        }
    }
}
