package com.nobug.config;


import java.awt.*;

public interface GlobalConfig {

    String VERSION = PropertiesUtil.getValue("vs");

    String ROOT_PATH = "res/";
    String TEMP = ROOT_PATH+"temp";  //缓存文件
    String LIB_PATH = ROOT_PATH+"libvlc";  // 脚本
    String SIMPLE_ICON_IMAGE = ROOT_PATH+"icon/simple.png";  // 头像
    String NOT_SIMPLE_ICON_IMAGE = ROOT_PATH+"icon/123.jpg";  // 头像
    String LOGIN = ROOT_PATH+"icon/login.jpg";  // 头像

    String PHOTO_PATH = "res/photo";  //截图
    String FILE_LIST = "res/fileList";  //播放列表


    Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
    int WIDTH = SCREEN.width;
    int HEIGHT = SCREEN.height;

    //播放器大小
    double SIZE = 0.6;


    String SIMPLE_TITLE = "简单的播放器   版本号："+ GlobalConfig.VERSION;
    String NOT_SIMPLE_TITLE = "不简单的播放器   版本号："+ GlobalConfig.VERSION;
}
