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



    private static String[] passwordList = {"","666","123456","10086"};
//    private static String[] passwordList = {""};

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
//                if(password == null){
//                    String pass = JOptionPane.showInputDialog("请输入密码：");
//                    try {
//                        decrypt = AESUtil.decrypt(bytes, getPassword(pass));
//                        if(decrypt != null){
//                            password = getPassword(pass);
//                        }
//                    } catch (Exception e) {
//                        System.out.println("密码：【"+pass+"】失败！");
//                    }
//                }
                //如果都循环结束都没有设置好密码 说明无法解密直接报错
                if(password == null){
                    System.out.println("无法播放");
                    return null;
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
        //获取到加密时的文件名和加密模式
        String[] split = data.split(",");
        String fileName = split[0];
        System.out.println(fileName);
        if(VideoEnum.getInstance(fileName) != VideoEnum.NOT){
            fileEncUtilBeans.remove(remove);
        }

        String outPath = outFile;

        FileIOUtil.fileByteWriter(fileEncUtilBeans,outPath);
        show.setText("解密成功！");
        return fileName;
    }

    /**
     *  通用解密 带头 还原文件名称
     * @param path
     * @return
     */
    public static String decrypt(String path, JLabel inShow, String outFile) {
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
        //获取到加密时的文件名和加密模式
        String[] split = data.split(",");
        String fileName = split[0];
        System.out.println(fileName);
        if(VideoEnum.getInstance(fileName) != VideoEnum.NOT){
            fileEncUtilBeans.remove(remove);
        }

        File file = new File(outFile);
        String parent = file.getParent();
        String outPath = parent + File.separator + fileName;

        FileIOUtil.fileByteWriter(fileEncUtilBeans,outPath);
        show.setText("解密成功！");
        return outPath;
    }

    /**
     * 根据 mode 对于输出文件的改变
     *@param path
     * @param password
     * @return
     */
    public static String encrypt(String path, String password, String mode, JLabel show) {
        password = getPassword(password);
        System.out.println("开始读取文件......");
        List<FileEncUtilBean> fileEncUtilBeans = FileIOUtil.fileByteReader(path, 1024 * 10);
        show.setText("读取成功......,开始加密文件......");

        // 添加一些数据到头部
        File file = Paths.get(path).toFile();
        StringBuilder sb = new StringBuilder();
        String fileName = file.getName();
        sb.append(fileName);
        sb.append(",");
        sb.append(mode);
        byte[] headByte = stringToBytes(sb.toString(),1024*10);
        FileEncUtilBean bean = new FileEncUtilBean(headByte, headByte.length);
        fileEncUtilBeans.add(0,bean);
        // 添加一些数据到头部


        long l = System.currentTimeMillis();

        for (FileEncUtilBean fileEncUtilBean : fileEncUtilBeans) {
            byte[] bytes = fileEncUtilBean.getBytes();
            byte[] encrypt = new byte[0];
            try {
                encrypt = AESUtil.encrypt(bytes, password);
            } catch (Exception e) {
                show.setText("加密失败！");
                throw new RuntimeException("加密失败！");
            }
            fileEncUtilBean.setBytes(encrypt);
            fileEncUtilBean.setLen(encrypt.length);
        }
        show.setText("加密完成用时："+(System.currentTimeMillis() - l)+"毫秒，开始写出文件...");

        String outPath = path;

        //输出文件改为 MD5
        if("R2M".equals(mode)){
            outPath = FileUtil.getDecryptNameMD5(path);
        }
        //输出文件改为 时间戳
        if("R2T".equals(mode)){
            outPath = FileUtil.getDecryptNameTime(path);
        }

        if("RT2TM".equals(mode)){
            outPath = FileUtil.getDecryptNameTime(path)+".mp4";
        }

        if("RT2MM".equals(mode)){
            outPath = FileUtil.getDecryptNameMD5(path)+".mp4";
        }

        if("V2Z".equals(mode)){
            outPath = FileUtil.replaceType(path, "zybfq");
        }


        outPath = FileUtil.reFileNamePath(outPath);
        FileIOUtil.fileByteWriter(fileEncUtilBeans,outPath);
        show.setText("写出成功！");

        return outPath;
    }


    private static String getPassword(String password) {
        return HashUtil.md5(password);
    }


    private static byte[] stringToBytes(String fileName, int len) {
        byte[] bytes = fileName.getBytes(StandardCharsets.UTF_8);
        byte[] headByte = new byte[len];
        for (int i = 0; i < bytes.length; i++) {
            headByte[i] = bytes[i];
        }
        return headByte;
    }

    public static void setPassword(String number) {
        passwordList[0] = number;
    }
}
