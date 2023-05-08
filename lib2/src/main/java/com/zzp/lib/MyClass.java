package com.zzp.lib;

import com.zzp.lib.algorithm.Code1;
import com.zzp.lib.algorithm.Code10;
import com.zzp.lib.algorithm.Code2;
import com.zzp.lib.algorithm.Code3;
import com.zzp.lib.algorithm.Code4;
import com.zzp.lib.algorithm.Code5;
import com.zzp.lib.algorithm.Code6;
import com.zzp.lib.algorithm.Code7;
import com.zzp.lib.algorithm.Code8;
import com.zzp.lib.design.DesignMain;

import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class MyClass {

    public static void main(String[] args) {
        //System.out.println("zzp");
        /*long time = 0;
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 3306);
            System.out.println(socketAddress);
            InetAddress inetAddress = socketAddress.getAddress();
            System.out.println("主机信息:"+inetAddress);
            int port = socketAddress.getPort();
            System.out.println("端口号:"+port);
            String hostName = socketAddress.getHostName();
            System.out.println("主机名:"+hostName);
            time = System.currentTimeMillis();
            Socket socket = new Socket("www.baidu.com",80);
            //InetAddress.getAllByName("www.djs-media.com");
            System.out.println("socket:" + " time:" + (System.currentTimeMillis() - time));
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException time:" + (System.currentTimeMillis() - time));
            e.printStackTrace();
            System.out.println("UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
        }*/
        /*try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\zhangzhipeng\\Downloads\\mipmap-xxhdpi\\push.png"));
            System.out.println("width:" + bufferedImage.getWidth() + " height:" + bufferedImage.getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*MainKt mainKt = new MainKt();
        mainKt.main();
        mainKt.invoke("mainkt");
        mainKt.example();*/
       /* PVTest test = new PVTest("main",2);
        test.doJob();

        TestObject.INSTANCE.zzp(0);*/

        /*final Cat cat = new Cat();

        Object newObject = Proxy.newProxyInstance(cat.getClass().getClassLoader(),Cat.class.getInterfaces(),new InvocationHandler(){

            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                System.out.println("---------before-------" + method.getName());
                Object invoke = method.invoke(cat, objects);
                System.out.println("---------after-------" + method.getName());

                return invoke;

            }
        });
        System.out.println(newObject.getClass());
        ((ICat)newObject).doPrint();*/
        /*String text = null;
        try {
            byte[] result = "夸".getBytes("Unicode");
            System.out.println(bytesToHexString(result));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        /*Code1 code1 = new Code1();
        code1.mainCode();*/
        /*Cat cat = new Cat();
        cat.doPrint();
        System.out.println("cat:" + cat);
        Class clazz = cat.runnable.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("field:" + field.getName());
            try {
                System.out.println(field.get(cat.runnable));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        System.out.println(cat.runnable);

        try {
            Field this$0Field = clazz.getDeclaredField("this$0");
            Object reflectCat = this$0Field.get(cat.runnable);
            System.out.println("result = "+reflectCat + " is:" + (reflectCat instanceof ICat));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }*/
        /*int[] codes = new int[]{0,10,20,30,23,12};
        Code2.bubble(codes);
        System.out.println(Arrays.toString(codes));*/

        /*for(int i = 0;i < 6;i++) {
            final int finalI = i;
            ThreadPool.mInstance.executor(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50);
                        System.out.println("index:" + finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        ThreadPool.mInstance.quit();*/
       /* Deque<String> stringDeque = new ArrayDeque<>();
        stringDeque.add("str1");
        stringDeque.add("str2");
        stringDeque.add("str3");
        stringDeque.add("str4");
        for (Iterator<String> i = stringDeque.iterator(); i.hasNext(); ){
            System.out.println(i.next());
        }*/
        //System.out.println(reverse(-123));
        //System.out.println("str:" + Integer.toBinaryString(Integer.MIN_VALUE) + " toUnsignedString:" + Integer.toUnsignedString(Integer.MIN_VALUE));
       /* final ThreadLocal<String> local = new ThreadLocal<String>(){
            @Override
            protected String initialValue() {
                return "noValue";
            }
        };
        local.set("zzp");
        new Thread(){
            @Override
            public void run() {
                System.out.println(local.get());
                local.set("php");
                System.out.println(local.get());
            }
        }.start();*/
        //Code5.checkMaxRectCount();
        //TestJavapoet.testMain();
        /*Deque<String> queue = new LinkedList<>();
        //添加元素
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.offer("e");
        queue.push("zzp123");

        System.out.println("remove:" + queue.poll());

        for(String q : queue){
            System.out.println(q);
        }*/
        //TestObject.INSTANCE.zzp(1000);
        //new WaitTest().doTest();
       /* SwipeTest.doTest();
        new Code7().doJob();*/
        //System.out.println(new UUID("321321321".hashCode(),"fsdafdsafdsafd".hashCode()).toString());

        //new Code8().test();
        //int[] nums = new int[]{7,3,9,6};
        //new Code10().combinationSum(nums ,6);

        //int[][] nums = new int[][]{{1,2,3},{4,5,6}};
        //System.out.println(Arrays.toString(nums[1]));
        //PvKt.Companion.doJob();
        new ShenjiFile().doJob();
        //caculate();
        //testThreadPool();
        //new DesignMain().doMain();
    }

    public static void caculate(){
        float total = 0f;
        for(int i = 0;i < 300;i++){
            total = total + 1000;
            total = total * (1 + 0.03f/12);
        }
        System.out.println("result:" + total);
    }

    public static void testThreadPool(){
        BlockingQueue<Runnable> queue = new PriorityBlockingQueue<>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,10,0L, TimeUnit.SECONDS,queue, new ThreadFactory() {
            int index = 0;

            @Override
            public Thread newThread(@NotNull Runnable runnable) {
                index++;
                return new Thread(runnable,"name-" + index);
            }
        });
        for(int i = 1;i <= 100;i++) {
            executor.execute(new CCRunnbale(i));
                    /*try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
        }
    }

    static class CCRunnbale implements Runnable,Comparable<CCRunnbale>{
        public int index;

        public CCRunnbale(int index) {
            this.index = index;
        }

        @Override
        public int compareTo(@NotNull CCRunnbale runnbale) {
            return runnbale.index - index;
        }

        @Override
        public void run() {
            System.out.println("thread:" + Thread.currentThread().getName() + " index:" + index);
        }
    }

    private static int reverse(int x){
        if(x == 0){
            return x;
        }
        boolean up = true;
        if(x < 0){
            up = false;
            x = x * -1;
        }
        long result = 0;
        while(x > 0){
            result = result * 10 + x % 10;
            x = x / 10;
        }
        if(!up){
            result = result * -1;
        }
        if(result < Integer.MIN_VALUE || result > Integer.MAX_VALUE){
            result = 0;
        }
        return (int)result;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            stringBuilder.append("0x");

            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            if (i != src.length-1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

}