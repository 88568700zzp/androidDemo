package com.zzp.lib.util;


import com.sun.tools.javac.util.ArrayUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * so加密工具类
 */
public class SoEncryptUtil {

    public static boolean soEncrypt = true;

    private static final String key = "1234567890abcdef";
    private static final String initVector = "1234567890abcdef";

    // 最大的加密明文长度
    public static final int MAX_ENCRYPT_BLOCK = 245;


    public static byte[] encryptAec(byte[] value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value);
            return Base64.getEncoder().encode(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptAec(byte[] encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return original;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 加密
     * @param inputByte      明文
     * @param key       私钥/密钥
     * @return 密文
     */
    private static byte[] RSAEncrypt(byte[] inputByte, Key key){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            int inputLen = inputByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            //对数据分段加密
            while (inputLen - offSet > 0){
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK){
                    cache = cipher.doFinal(inputByte, offSet,MAX_ENCRYPT_BLOCK);
                }else {
                    cache = cipher.doFinal(inputByte,offSet,inputLen - offSet);
                }
                out.write(cache,0,cache.length);
                i++;
                offSet = i*MAX_ENCRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    /**
     * 加密
     * @param bytes      明文
     * @param key       私钥/密钥
     * @return 密文
     */
    private static byte[] RSADecrypt(byte[] bytes, Key key){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);

            int inputLen = bytes.length;
            int offset = 0;
            byte[] cache;
            int i = 0;
            int MAX_DECRYPT_BLOCK = 256;
            // 对数据分段解密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(bytes, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(bytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            return out.toByteArray();
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


    public static void main(String[] args) {

        for (Provider provider : Security.getProviders()) {
            System.out.println(provider.toString());
        }

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("rsa");
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            System.out.println("getPrivate:" + Arrays.toString(keyPair.getPrivate().getEncoded()));
            System.out.println("getPublic:" + Arrays.toString(keyPair.getPublic().getEncoded()));

            long time = System.currentTimeMillis();
            //byte[] encryptByte = RSAEncrypt(file2Byte("lib2/libscanner.so"),keyPair.getPublic());
            byte[] encryptByte = encryptAec(file2Byte("lib2/libscanner.so"));
            System.out.println("cost time1:" + ( System.currentTimeMillis() - time));
            time = System.currentTimeMillis();
            byte2File(encryptByte,"lib2/libscanner1.so");
            //byte[] decryptByte = RSADecrypt(encryptByte,keyPair.getPrivate());
            byte[] decryptByte = decryptAec(encryptByte);
            System.out.println("cost time2:" + (System.currentTimeMillis() - time));
            byte2File(decryptByte,"lib2/libscanner2.so");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] file2Byte(String filePath){
        ByteArrayOutputStream bos=null;
        BufferedInputStream in=null;
        try{
            File file=new File(filePath);
            if(!file.exists()){
                throw new FileNotFoundException("file not exists");
            }
            bos=new ByteArrayOutputStream((int)file.length());
            in=new BufferedInputStream(new FileInputStream(file));
            int buf_size=1024;
            byte[] buffer=new byte[buf_size];
            int len=0;
            while(-1 != (len=in.read(buffer,0,buf_size))){
                bos.write(buffer,0,len);
            }
            return bos.toByteArray();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        finally{
            try{
                if(in!=null){
                    in.close();
                }
                if(bos!=null){
                    bos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据byte数组，生成文件
     * @param bfile 文件数组
     * @param filePath 文件存放路径
     */
    public static void byte2File(byte[] bfile,String filePath){
        BufferedOutputStream bos=null;
        FileOutputStream fos=null;
        File file=null;
        try{
            file=new File(filePath);
            fos=new FileOutputStream(file);
            bos=new BufferedOutputStream(fos);
            bos.write(bfile);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally{
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null){
                    fos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
