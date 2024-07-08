package com.nobug.component;

import com.nobug.PlayerMain;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 监听键盘事件
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-29
 */
public class KeyPressedListener extends KeyAdapter {


    private static EmbeddedMediaPlayerComponent playerComponent;

    public KeyPressedListener() {
        playerComponent = Window.getPlayerComponent();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        //向左
        if(37 == keyCode){

            // 回退5秒
            setProgress(-5);

        }
        //上
        if(38 == keyCode){
            setVol(5);
        }
        //右边
        if(39 == keyCode){
            // 进10秒
            setProgress(10);
        }
        //下
        if(40 == keyCode){
            setVol(-5);
        }
    }

    private static void setVol(int num) {
        int value = Window.getSlider().getValue();
        value += num;
        Window.getSlider().setValue(value);
        playerComponent.getMediaPlayer().setVolume(value);
    }

    private synchronized void setProgress(int i) {
        i *=1000;
        long time = playerComponent.getMediaPlayer().getTime();
        time += i;
        if(time<0){
            time = 0;
        }
        long length = playerComponent.getMediaPlayer().getLength();

        playerComponent.getMediaPlayer().setTime(time);
        JProgressBar progressBar = PlayerMain.frame.getProgressBar();
        if(length == -1){
            progressBar.setValue(0);
        }else {
            double num = time/(length*0.01);
            progressBar.setValue((int) num);
        }
    }


}
