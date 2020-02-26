package com.application.huawei.util;

import org.apache.catalina.Host;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Auther: 10199
 * @Date: 2019/12/10 23:11
 * @Description: Redis端口检测，防止疏忽，没启动Redis服务
 */

public class PortUtil {
    public static boolean testPort(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
            return true;
        } catch (IOException e) {
            return false;
        }finally {
            try {
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void checkPort(String host, int port, String server, boolean shutdown) {
        if(!testPort(host, port)) {
            if(shutdown) {
                String message =String.format("在端口 %d 未检查得到 %s 启动%n",port,server);
                JOptionPane.showMessageDialog(null, message);
                System.exit(1);
            }
            else {
                String message =String.format("在端口 %d 未检查得到 %s 启动%n,是否继续?",port,server);
                if(JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null, message))
                    System.exit(1);
            }
        }
    }
}
