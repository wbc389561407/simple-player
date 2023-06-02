package com.nobug.util;

import com.nobug.config.VideoEnum;

import javax.swing.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-22
 */
public class AESFile {



    private static String[] passwordList = {"","123456","10086","666"};

//    /**
//     *  V3 V2 解密 回复原文件名称
//     * @param path
//     * @return
//     */
//    public static String decrypt(String path, JLabel show, String outFile) {
//        String password= "";
//        password = getPassword(password);
//        show.setText("正在读取文件......");
//        List<FileEncUtilBean> fileEncUtilBeans = FileIOUtil.fileByteReader(path, 10256);
//        show.setText("文件读取成功......,开始解析文件...");
//
//        long l = System.currentTimeMillis();
//        for (FileEncUtilBean fileEncUtilBean : fileEncUtilBeans) {
//            byte[] bytes = fileEncUtilBean.getBytes();
//            byte[] decrypt = new byte[0];
//            try {
//                decrypt = AESUtil.decrypt(bytes, password);
//            } catch (Exception e) {
//                throw new RuntimeException("解密失败！");
//            }
//            fileEncUtilBean.setBytes(decrypt);
//            fileEncUtilBean.setLen(decrypt.length);
//        }
//        show.setText("解析完成用时："+(System.currentTimeMillis() - l)+"毫秒，开始写出文件...");
//
//        FileEncUtilBean remove = fileEncUtilBeans.remove(0);
//        byte[] bytes = remove.getBytes();
//        String data = new String(bytes, StandardCharsets.UTF_8).trim();
//        String[] split = data.split(",");
//        String fileName = split[0];
//
//
//        String outPath = FileUtil.getOutPath(path,fileName);
//        outPath = FileUtil.reFileNamePath(outPath);
//        FileIOUtil.fileByteWriter(fileEncUtilBeans,outFile);
//        show.setText("解密成功！");
//        return outFile;
//    }


    /**
     *  通用解密 带头
     * @param path
     * @return
     */
    public static String decryptTY(String path, JLabel inShow, String outFile) {
        JLabel show = new JLabel();
        if(inShow != null){
            show = inShow;
        }
        show.setText("正在读取文件......");
        List<FileEncUtilBean> fileEncUtilBeans = FileIOUtil.fileByteReader(path, 10256);
        show.setText("文件读取成功......,开始解析文件...");

        long l = System.currentTimeMillis();
        String password = null;
        for (FileEncUtilBean fileEncUtilBean : fileEncUtilBeans) {
            byte[] bytes = fileEncUtilBean.getBytes();
            byte[] decrypt = null;

            //测试密码
            if(password == null){
                for (String s : passwordList) {
                    try {
                        decrypt = AESUtil.decrypt(bytes, getPassword(s));
                        if(decrypt != null){
                            password = getPassword(s);
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("密码：【"+s+"】失败！");
                    }
                }
                //如果都循环结束都没有设置好密码 说明无法解密直接报错
                if(password == null){
                    System.out.println("无法播放");
                    throw new RuntimeException("无法播放");
                }
            }else {
                try {
                    decrypt = AESUtil.decrypt(bytes, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fileEncUtilBean.setBytes(decrypt);
            fileEncUtilBean.setLen(decrypt.length);
        }
        show.setText("解析完成用时："+(System.currentTimeMillis() - l)+"毫秒，开始写出文件...");

        FileEncUtilBean remove = fileEncUtilBeans.get(0);
        byte[] bytes = remove.getBytes();
        String data = new String(bytes, StandardCharsets.UTF_8).trim();
        String[] split = data.split(",");
        String fileName = split[0];
        System.out.println(fileName);
        if(VideoEnum.getInstance(fileName) != VideoEnum.NOT){
            fileEncUtilBeans.remove(remove);
        }

        String outPath = outFile;

        FileIOUtil.fileByteWriter(fileEncUtilBeans,outPath);
        show.setText("解密成功！");
        return outPath;
    }


    private static String getPassword(String password) {
        return HashUtil.md5(password);
    }


}
