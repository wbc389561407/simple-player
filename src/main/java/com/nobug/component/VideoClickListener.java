package com.nobug.component;


import com.nobug.PlayerMain;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * 监听双击事件
 */
public class VideoClickListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        //检测鼠标双击事件
        if (e.getClickCount() == 2) {
            PlayerMain.frame.mouseClickedTwice();
        }
    }

    private void mouseClickedOnce() {
   
    }

}
