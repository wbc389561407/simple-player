package com.nobug;

/**
 * 启动类
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-28
 */

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nobug.component.WSwingWorker;
import com.nobug.component.Window;
import com.nobug.config.GlobalConfig;
import com.nobug.config.PropertiesUtil;
import com.nobug.util.LocalMac;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayerMain {

    public static Window frame;
    // 是否播放
    public static boolean flag = true;

    public static void main(String[] args) {


        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),GlobalConfig.LIB_PATH); // 导入的路径是vlc的安装路径
//        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
//         System.out.println(LibVlc.INSTANCE.libvlc_get_version());

        // 创建主程序界面运行窗体
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    frame = new Window();
                    frame.setVisible(true);

                    WSwingWorker wSwingWorker = new WSwingWorker();
                    wSwingWorker.execute();

                    //改变模式，查询是否为激活
                    String localMac = LocalMac.getLocalMac();
                    Map<String, Object> map = new HashMap<>();
                    map.put("mac",localMac);

                    try {
                        String post = HttpUtil.post(PropertiesUtil.getValue("login_url"), JSON.toJSONString(map), 800);
                        JSONObject jsonObject = JSONObject.parseObject(post);
                        int code = jsonObject.getIntValue("code");

                        if(code == 0){
                            frame.getwMenuBar().useNotSimpleTitle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                } catch (Exception e) {
                    System.exit(0);
                }
            }

        });
    }

    //关闭播放器释放资源
    public static void dispose(){
        flag=false;
        frame.getMediaPlayer().stop();
        frame.dispose();
    }

    // 打开文件
    public static void openVideo() {
        JFileChooser chooser = new JFileChooser();
        int v = chooser.showOpenDialog(null);
        if (v == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            frame.getMediaPlayer().playMedia(file.getAbsolutePath());
        }
    }

    // 退出播放
    public static void exit() {
        Window.windowClosing();
        frame.getMediaPlayer().release();
        System.exit(0);
    }

    // 实现播放按钮的方法
    public static void play() {
        if(!flag){
            flag = true;
            WSwingWorker wSwingWorker = new WSwingWorker();
            wSwingWorker.execute();
        }
        frame.getMediaPlayer().play();
    }

    // 实现暂停按钮的方法
    public static void pause() {
        frame.getMediaPlayer().pause();
    }

    // 实现停止按钮的方法
    public static void stop() {
        flag = false;
        frame.getMediaPlayer().stop();
    }

    // 实现点击进度条跳转的方法
    public static void jumpTo(float to) {
        frame.getMediaPlayer().setTime((long) (to * frame.getMediaPlayer().getLength()));
    }

    // 实现控制声音的方法
    public static void setVol(int v) {
        frame.getMediaPlayer().setVolume(v);
    }


}