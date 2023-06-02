package com.nobug.component;

import com.nobug.PlayerMain;
import com.nobug.config.GlobalConfig;
import com.nobug.util.AESFile;
import com.nobug.util.HashUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;

/**
 * 播放器面板
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-28
 */
public class VideoPane extends JPanel {

    public static boolean tsMode = false;


    private static Image image = Toolkit.getDefaultToolkit().createImage(GlobalConfig.LOGIN);

    public VideoPane() {
        setBackground(Color.CYAN);
        setVisible(true);
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE,
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
                                                Window.lock();
                                                String absolute = decryptTY(absolutePath);
                                                PlayerMain.frame.getMediaPlayer().playMedia(absolute);
                                                Window.unLock();
                                            }).start();


                                        }else {
                                            PlayerMain.frame.getMediaPlayer().playMedia(absolutePath);
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

                    private String decryptTY(String absolutePath) {
                        File file1 = new File(GlobalConfig.TEMP);
                        String absolutePath1 = file1.getAbsolutePath()+"/"+HashUtil.md5(absolutePath);
                        if(!new File(absolutePath1).exists()){
                            AESFile.decryptTY(absolutePath, WMenuBar.jLabel, absolutePath1);
//                            AESFile.decrypt(absolutePath, WMenuBar.jLabel, absolutePath1);
                        }
                        WMenuBar.end();
                        return absolutePath1;
                    }

                });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0,this.getWidth(),this.getHeight(),null);
    }
}
