package com.zzp.lib;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
        MainKt mainKt = new MainKt();
        mainKt.main();
        mainKt.invoke("mainkt");
        mainKt.example();
    }
}