package com.nobug.component;

import com.nobug.PlayerMain;
import com.nobug.config.GlobalConfig;
import com.nobug.util.AESFile;
import com.nobug.util.FileIOUtil;
import com.nobug.util.HashUtil;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;

/**
 * @Author: wangbingchen
 * @CreateTime: 2024-07-08
 */
public class WDropTarget{

    public static boolean tsMode = false;

    //拖入文件 解密后的路径
    public static String dropFilePath = "";
    public static String fileName = "";

    public static void listen(JPanel videoPane) {
        new DropTarget(videoPane, DnDConstants.ACTION_COPY_OR_MOVE,
                new DropTargetAdapter() {
                    @Override
                    public void drop(DropTargetDropEvent dropEvent) {
                        try {
                            // 如果拖入的文件格式受支持
                            if (dropEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
//                                // 接收拖拽来的数据
                                dropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                                @SuppressWarnings("unchecked")
                                java.util.List<File> list = (java.util.List<File>) (dropEvent.getTransferable()
                                        .getTransferData(DataFlavor.javaFileListFlavor));

                                for (File file : list) {
                                    if(file.isDirectory()){
                                        //文件夹
                                        break;
                                    }else {
                                        String absolutePath = file.getAbsolutePath();
                                        if(tsMode){
                                            //转换
                                            System.out.println("转换:"+absolutePath);
                                            //关闭当前播放内容
                                            PlayerMain.frame.getMediaPlayer().stop();
                                            //锁住页面
                                            File file1 = new File(GlobalConfig.TEMP);
                                            if(!file1.exists()){
                                                file1.mkdirs();
                                            }
                                            new Thread(() ->{
                                                //解密
                                                dropFilePath = decryptTY(absolutePath);
                                                //播放解密后的文件
                                                PlayerMain.frame.getMediaPlayer().playMedia(dropFilePath);
                                                PlayerMain.play();
                                            }).start();

                                        }else {
                                            PlayerMain.frame.getMediaPlayer().playMedia(absolutePath);
                                            PlayerMain.play();

                                        }
                                    }
                                }
                                // 指示拖拽操作已完成
                                dropEvent.dropComplete(true);
                            } else {
                                // 拒绝拖拽来的数据
                                dropEvent.rejectDrop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //解密
                    private String decryptTY(String absolutePath) {
                        File file1 = new File(GlobalConfig.TEMP);
                        String absolutePath1 = file1.getAbsolutePath()+"/"+ HashUtil.md5(absolutePath);
                        if(!new File(absolutePath1).exists()){
                            fileName = AESFile.decryptTY(absolutePath, WMenuBar.jLabel, absolutePath1);
                            System.out.println(fileName);
                        }
                        WMenuBar.end();
                        return absolutePath1;
                    }

                });
    }

    public static void save(String newPath) {
        FileIOUtil.copyFile(dropFilePath, newPath+"/"+fileName);
    }
}
