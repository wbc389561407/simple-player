package com.nobug.component;

import com.nobug.PlayerMain;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.util.List;

/**
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-28
 */
public class WSwingWorker extends SwingWorker<String, Integer> {

    @Override
    protected String doInBackground() throws Exception {
        while (PlayerMain.flag) {
            EmbeddedMediaPlayer mediaPlayer = PlayerMain.frame.getMediaPlayer();
            //获取视频总长度
            long total = mediaPlayer.getLength();
            //当前长度
            long curr = mediaPlayer.getTime();

            PlayerMain.frame.setTime(curr,total);


            if(curr == -1){
                publish(0);
            }else {
                float percent = (float) curr / total;
                publish((int) (percent * 100));
            }

            Thread.sleep(100);
        }
        publish(0);
        return null;
    }

    protected void process(List<Integer> chunks) {
        for (int v : chunks) {
            PlayerMain.frame.getProgressBar().setValue(v);
        }
    }
}
