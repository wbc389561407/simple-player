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

    private static Image login = Toolkit.getDefaultToolkit().createImage(GlobalConfig.LOGIN);

    public VideoPane() {
        System.out.println("初始化");
        setVisible(true);
    }

    private Image image;

    public VideoPane(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

}
