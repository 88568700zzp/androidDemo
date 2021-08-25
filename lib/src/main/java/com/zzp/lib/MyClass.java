package com.zzp.lib;

import com.zzp.lib.algorithm.Code1;
import com.zzp.lib.algorithm.Code2;

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
import java.util.concurrent.Executors;

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
        char a = 'a';
        char b = 'a';
        System.out.println(a == b);
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