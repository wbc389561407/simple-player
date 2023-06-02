package com.nobug.util;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-12-18
 */
public class LocalMac {

    public static void main(String[] args){

        String localMac = getLocalMac();
        System.out.println(localMac);

    }

    /**
     * 获取本地mac地址
     * 注意：物理地址是48位，别和ipv6搞错了
     * @return 本地mac地址
     */
    public static String getLocalMac() {
        try {
            //获取网卡，获取地址
            byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                //字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception exception) {
        }
        return null;
    }
}
