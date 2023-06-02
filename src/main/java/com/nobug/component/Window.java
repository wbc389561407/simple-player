package com.nobug.component;

/**
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-28
 */

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.nobug.PlayerMain;
import com.nobug.config.GlobalConfig;
import com.nobug.util.LocalMac;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 窗口
 */
public class Window extends JFrame{

    private static JPanel contentPane; //顶层容器，整个播放页面的容器
    JPanel panel;   //控制区域容器
    private JProgressBar progress;  //进度条
    private JLabel jLabelL; //左边时间
    private JLabel jLabelR;//右边时间
    private JPanel progressPanel;   //进度条容器
    private JPanel controlPanel;    //控制按钮容器
    private JButton btnStop,btnPlay,btnPause;   //控制按钮，停止、播放、暂停
    public static JSlider slider;     //声音控制块

    private static JPanel videoPane;

    public WMenuBar getwMenuBar() {
        return wMenuBar;
    }

    WMenuBar wMenuBar;


    static EmbeddedMediaPlayerComponent playerComponent;   //媒体播放器组件


    //MainWindow构造方法，创建视屏播放的主界面
    public Window(){

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("关闭");
                Window.windowClosing();
            }
        });

        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defaultToolkit.getScreenSize();
        setTitle(GlobalConfig.SIMPLE_TITLE);
        setIconImage(defaultToolkit.createImage(GlobalConfig.SIMPLE_ICON_IMAGE));
        setBounds(screenSize.width/4,screenSize.height/4,screenSize.width/2,screenSize.height/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane=new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0,0));
        setContentPane(contentPane);
        setVisible(true);

        wMenuBar = new WMenuBar();
        //添加头部菜单栏
        setJMenuBar(wMenuBar);

        //主体
        videoPane = new VideoPane();


        contentPane.add(videoPane, BorderLayout.CENTER);
        videoPane.setLayout(new BorderLayout(0,0));

        playerComponent = new EmbeddedMediaPlayerComponent();
        playerComponent.getMediaPlayer().setFullScreenStrategy(new DefaultAdaptiveRuntimeFullScreenStrategy(this));
        playerComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        videoPane.add(playerComponent);
        Canvas canvas = playerComponent.getVideoSurface();
        canvas.addMouseListener(new VideoClickListener());
        canvas.addKeyListener(new KeyPressedListener());



        //底部面板
        panel=new JPanel();     //实例化控制区域容器
        videoPane.add(panel,BorderLayout.SOUTH);

        progressPanel=new JPanel(); //实例化进度条容器
        panel.add(progressPanel, BorderLayout.NORTH);


        progress = new JProgressBar();

        jLabelL = new JLabel("");
        jLabelR = new JLabel("");
        progressPanel.add(jLabelL,BorderLayout.WEST);

        progressPanel.add(progress);

        progressPanel.add(jLabelR,BorderLayout.WEST);

//        panel.add(progress,BorderLayout.NORTH);
        progress.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){     //点击进度条调整视屏播放进度
                int x=e.getX();
                PlayerMain.jumpTo((float)x/progress.getWidth());
            }
        });
        progress.setStringPainted(true);

//        JLabel jLabel = new JLabel("进度条");
//        panel.add(jLabel,BorderLayout.WEST);

        controlPanel = new JPanel();      //实例化控制按钮容器
        panel.add(controlPanel,BorderLayout.SOUTH);


        //添加停止按钮
        btnStop=new JButton("停止");
        btnStop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PlayerMain.stop();
            }
        });
        controlPanel.add(btnStop);

        //添加播放按钮
        btnPlay=new JButton("播放");
        btnPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PlayerMain.play();
            }
        });
        controlPanel.add(btnPlay);

        //添加暂停按钮
        btnPause=new JButton("暂停");
        btnPause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PlayerMain.pause();
            }
        });
        controlPanel.add(btnPause);

        //添加声音控制块
        slider = new JSlider();
        slider.setValue(50);
        slider.setMaximum(100);
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                PlayerMain.setVol(slider.getValue());
            }
        });
        controlPanel.add(slider);
//        System.out.println("初始化成功");
//        String localMac = LocalMac.getLocalMac();
//        Map<String, Object> map = new HashMap<>();
//        map.put("mac",localMac);
//        String post = HttpUtil.post("http://localhost:8888/ck", JSON.toJSONString(map), 5000);
//        System.out.println(post);
//        if("true".equals(post)){
//            wMenuBar.useNotSimpleTitle();
//        }
    }

    public static void windowClosing() {
        playerComponent.getMediaPlayer().stop();
        videoPane.remove(playerComponent);
        File file = new File(GlobalConfig.TEMP);
        if(file.exists()){
            File[] files = file.listFiles();
            for (File file1 : files) {
                boolean delete = file1.delete();
                while (!delete){
                    delete = file1.delete();
                }
            }
        }
    }

    public static void lock() {
//        System.out.println("正在加载");
        playerComponent.setVisible(false);
    }

    public static void unLock() {
//        System.out.println("加载结束");
        playerComponent.setVisible(true);

    }

    //获取播放媒体
    public EmbeddedMediaPlayer getMediaPlayer() {
        return playerComponent.getMediaPlayer();
    }

    //获取进度条实例
    public JProgressBar getProgressBar() {
        return progress;
    }

    public void setTime(long curr, long total) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String format = simpleDateFormat.format(curr - 8*60*60*1000+1);
        String format1 = simpleDateFormat.format(total - 8*60*60*1000+1);

        jLabelL.setText(format);
        jLabelR.setText(format1);
    }

    public void mouseClickedTwice() {
        // 双击事件
        EmbeddedMediaPlayerComponent media = playerComponent;
        if (media.getMediaPlayer().isFullScreen()) {
            panel.setVisible(true);
            wMenuBar.setVisible(true);
            media.getMediaPlayer().setFullScreen(false);
        } else {
            wMenuBar.setVisible(false);
            panel.setVisible(false);
            media.getMediaPlayer().setFullScreen(true);
        }
    }
}