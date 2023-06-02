package com.nobug.component;


import com.nobug.PlayerMain;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 监听双击事件
 */
public class VideoClickListener extends MouseAdapter {
    private static  boolean flag = false;		//双击事件已执行时置为真
    private static int clickNum = 1;		//指示鼠标点击次数，默认为单击
    @Override
    public void mouseClicked(MouseEvent e) {
            VideoClickListener.flag = false;
            if (VideoClickListener.clickNum == 2) {
                //鼠标点击次数为2调用双击事件
                this.mouseClickedTwice();
                //调用完毕clickNum置为1
                VideoClickListener.clickNum = 1;
                VideoClickListener.flag = true;
                return;
            }
            //新建定时器，双击检测间隔为500ms
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                //指示定时器执行次数
                int num = 0;
                @Override
                public void run() {
                    // 双击事件已经执行，取消定时器任务
                    if(VideoClickListener.flag) {
                        num = 0;
                        VideoClickListener.clickNum = 1;
                        this.cancel();
                        return;
                    }
                    //定时器再次执行，调用单击事件，然后取消定时器任务
                    if (num == 1) {
                        mouseClickedOnce();
                        VideoClickListener.flag = true;
                        VideoClickListener.clickNum = 1;
                        num = 0;
                        this.cancel();
                        return;
                    }
                    clickNum++;
                    num++;
                }
            },new Date(), 200);
    }

    private void mouseClickedOnce() {
   
    }


    private void mouseClickedTwice() {
        // 双击事件
        EmbeddedMediaPlayerComponent media = PlayerMain.frame.playerComponent;
        if (media.getMediaPlayer().isFullScreen()) {
            PlayerMain.frame.panel.setVisible(true);
            PlayerMain.frame.wMenuBar.setVisible(true);
            media.getMediaPlayer().setFullScreen(false);
        } else {
            PlayerMain.frame.panel.setVisible(false);
            PlayerMain.frame.wMenuBar.setVisible(false);
            media.getMediaPlayer().setFullScreen(true);
        }
    }

}
