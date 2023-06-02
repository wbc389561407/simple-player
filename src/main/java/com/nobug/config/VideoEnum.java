package com.nobug.config;

import java.util.Locale;

/**
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-25
 */
public enum VideoEnum {

    MUSIC,VIDEO,PASSWORD_FILE,NOT;

    private static final String[] VIDEO_LIST = {"ts","mp4","3ga","669","a52","acc","ac3","adt","adts","aif","aiff","amr","aob","ape","awb","caf","dts","it","kar","m4a","m4b","m4p","m5p","mid","mka","mlp","mod","mpa","mp1","mp2","mpc","mpga","mus","oga","oma","opus","qcp","ra","rmi","s3m","sid","spx","thd","tta","voc","vqf","w64","wma","wv","xa","xm"};
    private static final String[] MUSIC_LIST = {"mp3","ogg","wav","flac"};
    private static final String[] PASSWORD_FILE_LIST = {"zybfq"};

    VideoEnum(){

    }

    public static VideoEnum getInstance(String path){
        String pa = path.toLowerCase(Locale.ROOT);
        for (String type : VIDEO_LIST) {
            if(pa.endsWith("." + type)){
                return VIDEO;
            }
        }
        for (String type : MUSIC_LIST) {
            if(pa.endsWith("." + type)){
                return MUSIC;
            }
        }
        for (String type : PASSWORD_FILE_LIST) {
            if(pa.endsWith("." + type)){
                return PASSWORD_FILE;
            }
        }
        return NOT;
    }

}
