package com.nobug.component;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nobug.PlayerMain;
import com.nobug.config.GlobalConfig;
import com.nobug.config.PropertiesUtil;
import com.nobug.util.LocalMac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 头部菜单栏
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-28
 */
public class WMenuBar extends JMenuBar {

    private JMenu mnFile;  //文件菜单
    private JMenu bjFile;  //操作
    private JMenu helpFile;  //帮助
    public static JLabel jLabel;

    private JMenuItem startFile;// 调试模式
    private JMenuItem activate; //激活按钮

    int aboutIndex = 0;

    public WMenuBar(){

        mnFile=new JMenu("文件");
        add(mnFile);
        JMenuItem mnOpenVideo =new JMenuItem("打开文件");
        mnFile.add(mnOpenVideo);


        bjFile = new JMenu("操作");
        add(bjFile);
        JMenuItem full =new JMenuItem("全屏");
        bjFile.add(full);

        JMenuItem mnExit =new JMenuItem("退出");
        bjFile.add(mnExit);

        activate =new JMenuItem("激活");
        bjFile.add(activate);



        helpFile = new JMenu("帮助");
        add(helpFile);
        JMenuItem aboutFile =new JMenuItem("关于我们");
        helpFile.add(aboutFile);
        startFile =new JMenuItem("关闭调试模式");
//        helpFile.add(startFile);

        jLabel = new JLabel();
        add(jLabel);

        //打开文件
        mnOpenVideo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PlayerMain.openVideo();
            }
        });

        //退出
        mnExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PlayerMain.exit();
            }
        });


        //激活
        activate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String cdk = JOptionPane.showInputDialog("请输入激活码：");
                if(cdk != null){
                    String localMac = LocalMac.getLocalMac();
                    Map<String, Object> map = new HashMap<>();
                    map.put("cdk",cdk);
                    map.put("mac",localMac);
                    try {
                        cdk = HttpUtil.post(PropertiesUtil.getValue("avt_url"), JSON.toJSONString(map), 5000);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null,"网络问题，请稍后重试!");
                    }
                    System.out.println(cdk);
                    JSONObject jsonObject = JSONObject.parseObject(cdk);
                    int code = jsonObject.getIntValue("code");
                    if(code == 0){
                        useNotSimpleTitle();
                        JOptionPane.showMessageDialog(null,"激活成功！");
                    }else {
                        String msg = jsonObject.getString("msg");
                        JOptionPane.showMessageDialog(null,msg);
                    }
                }

            }
        });

        //关于我们
        aboutFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(VideoPane.tsMode){
                    JOptionPane.showMessageDialog(null,"这个播放器不简单！\n版本号："+ GlobalConfig.VERSION);
                }else {
                    if(aboutIndex++ == 4){
                        //切换为不简单的模式
                        useNotSimpleTitle();
                    }
                    JOptionPane.showMessageDialog(null,"版本号："+ GlobalConfig.VERSION);
                }
            }
        });
        //开启调试模式
        startFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(VideoPane.tsMode){
                    VideoPane.tsMode = false;
                    PlayerMain.frame.setTitle(GlobalConfig.SIMPLE_TITLE);
                    PlayerMain.frame.setIconImage(Toolkit.getDefaultToolkit().createImage(GlobalConfig.SIMPLE_ICON_IMAGE));
                    aboutIndex = 0;
                    startFile.setText("开启调试模式");
                }else {
                    VideoPane.tsMode = true;
                    PlayerMain.frame.setTitle(GlobalConfig.NOT_SIMPLE_TITLE);
                    PlayerMain.frame.setIconImage(Toolkit.getDefaultToolkit().createImage(GlobalConfig.NOT_SIMPLE_ICON_IMAGE));
                    startFile.setText("关闭调试模式");
                }

            }
        });
        full.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //全屏操作
                PlayerMain.frame.mouseClickedTwice();

            }
        });
    }

    //切换为不简单的模式
    public void useNotSimpleTitle() {
        VideoPane.tsMode = true;
        PlayerMain.frame.setTitle(GlobalConfig.NOT_SIMPLE_TITLE);
        PlayerMain.frame.setIconImage(Toolkit.getDefaultToolkit().createImage(GlobalConfig.NOT_SIMPLE_ICON_IMAGE));
        helpFile.add(startFile);
        bjFile.remove(activate);
    }


    public static void end() {
        new Thread(() ->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jLabel.setText("");
        }).start();
    }
}
