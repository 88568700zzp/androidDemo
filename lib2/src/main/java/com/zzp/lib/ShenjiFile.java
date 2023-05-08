package com.zzp.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ShenjiFile {

    String diffFile = "C:\\android\\check.txt";

    String inputParentFile = "C:\\android\\AsrSdk_Android";
    String outputParentFile = "C:\\android\\AsrSdk_Android_Output";

    String[] codes = new String[]{"192.","221.","168.","121.","1234","pwd","passwd","password"};

    public void doJob(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(diffFile));
            String line = null;
            while((line = reader.readLine()) != null){
                File readFile = new File(inputParentFile + File.separator + line);
                System.out.println(line + " " + readFile.exists());
                try {

                    File outFile = new File(outputParentFile + File.separator + line);
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    if(!outFile.getParentFile().exists()){
                        outFile.getParentFile().mkdirs();
                    }
                    checkFile(readFile);
                    copyFileByStream(readFile,outFile);
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("failed");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void checkFile(File readFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(readFile));
        String line = null;
        while((line = reader.readLine()) != null){
            for(String code:codes){
                if(line.contains(code)){
                    System.out.println("checkFile:" + code + " " + readFile.getAbsoluteFile());
                }
            }
        }
    }

    public void copyFileByStream(File file, File fileTo) throws IOException {
        InputStream in = new FileInputStream(file);
        OutputStream out = new FileOutputStream(fileTo);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
    }

}
