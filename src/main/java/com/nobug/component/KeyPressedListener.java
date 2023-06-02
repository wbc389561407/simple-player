package com.nobug.component;

import com.nobug.PlayerMain;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-29
 */
public class KeyPressedListener extends KeyAdapter {

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
            JSlider jSlider = Window.slider;
            int value = jSlider.getValue();
            value += 5;
            jSlider.setValue(value);
            PlayerMain.setVol(value);
        }
        //右边
        if(39 == keyCode){
            // 进10秒
            setProgress(10);
        }
        //下
        if(40 == keyCode){
            JSlider jSlider = Window.slider;
            int value = jSlider.getValue();
            value -= 5;
            jSlider.setValue(value);
            PlayerMain.setVol(value);
        }
    }

    private synchronized void setProgress(int i) {
        i *=1000;
        EmbeddedMediaPlayer mediaPlayer = PlayerMain.frame.getMediaPlayer();
        long time = mediaPlayer.getTime();
        time += i;
        if(time<0){
            time = 0;
        }
        long length = mediaPlayer.getLength();

        mediaPlayer.setTime(time);
        JProgressBar progressBar = PlayerMain.frame.getProgressBar();
        if(length == -1){
            progressBar.setValue(0);
        }else {
            double num = time/(length*0.01);
            progressBar.setValue((int) num);
        }
    }


}
