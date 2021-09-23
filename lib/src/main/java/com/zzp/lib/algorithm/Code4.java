package com.zzp.lib.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找最短路径
 * Created by samzhang on 2021/8/16.
 */
public class Code4 {
    public static void getNode(){
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");

        nodeF.addDistance(new Distance(nodeE,4));

        nodeE.addDistance(new Distance(nodeC,5));
        nodeE.addDistance(new Distance(nodeD,6));

        nodeB.addDistance(new Distance(nodeA,2));
        nodeC.addDistance(new Distance(nodeA,10));
        nodeD.addDistance(new Distance(nodeA,5));

        caculateMinDis(nodeF,nodeA);

        System.out.println(nodeA);
        System.out.println(nodeB);
        System.out.println(nodeC);
        System.out.println(nodeD);
        System.out.println(nodeE);
        System.out.println(nodeF);
    }

    private static int caculateMinDis(Node startNode,Node targetNode){
        System.out.println("caculateMinDis:" + startNode);
        if(startNode.mPreNodeList.size() > 0){
            for(Distance distance : startNode.mPreNodeList){
                if(distance.node != targetNode){
                    caculateMinDis(distance.node,targetNode);
                    if(distance.node.minValue > 0) {
                        if (startNode.minValue == 0) {
                            startNode.minValue = distance.node.minValue + distance.distance;
                        } else {
                            startNode.minValue = Math.min(startNode.minValue, (distance.node.minValue + distance.distance));
                        }
                    }else{
                        startNode.minValue = 0;
                    }
                }else{
                    startNode.minValue = distance.distance;
                    return 1;
                }
            }
        }
        return -1;
    }

    private static class Node{

        Node(String name){
            this.name = name;
        }

        int minValue = 0;//目标点到当前节点的距离
        String name;
        List<Distance> mPreNodeList= new ArrayList<>();

        public void addDistance(Distance distance){
            mPreNodeList.add(distance);
        }

        @Override
        public String toString() {
            return "name:" + name + " minValue:" + minValue;
        }
    }

    private static class Distance{
        Node node;
        int distance;

        public Distance(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

}
