package com.jd.lib;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReleaseRar {

    public void doTest(){
        File zipFile = new File("C:\\android\\file\\libcom.jd.lib.babel.so");
        File outPutFile = new File("C:\\android\\file\\test");
        decompressZip2Files(zipFile,outPutFile);
    }

    /**
     * 将 zip 压缩包解压成文件到指定文件夹下
     *
     * @param zipFile   待解压的压缩文件。亲测  .zip 文件有效；.7z 压缩解压时抛异常。
     * @param targetDir 解压后文件存放的目的地. 此目录必须存在，否则异常。
     * @return 是否成功
     */
    public static boolean decompressZip2Files(File zipFile, File targetDir) {
        InputStream inputStream = null;//源文件输入流，用于构建 ZipArchiveInputStream
        OutputStream outputStream = null;//解压缩的文件输出流
        ZipArchiveInputStream zipArchiveInputStream = null;//zip 文件输入流
        ArchiveEntry archiveEntry = null;//压缩文件实体.
        try {
            inputStream = new FileInputStream(zipFile);//创建输入流，然后转压缩文件输入流
            zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8");
            //遍历解压每一个文件.
            while (null != (archiveEntry = zipArchiveInputStream.getNextEntry())) {
                String archiveEntryFileName = archiveEntry.getName();//获取文件名
                File entryFile = new File(targetDir, archiveEntryFileName);//把解压出来的文件写到指定路径
                byte[] buffer = new byte[1024 * 5];
                try {
                    outputStream = new FileOutputStream(entryFile);
                    int length = -1;
                    while ((length = zipArchiveInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.flush();
                }catch (Exception e){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != zipArchiveInputStream) {
                    zipArchiveInputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


}
