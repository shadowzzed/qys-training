package com.qys.training.biz.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zed, shadowl91@163.com
 * @date 16:58 2020/8/5
 */
public class HeartBeatRunnable implements Runnable {

    private static final List<String> addList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatRunnable.class);

    private int port;

    private Boolean isAddToList;

    public static List<String> getAddList() {
        return addList;
    }

    public HeartBeatRunnable(int port, Boolean isAddToList) {
        this.port = port;
        this.isAddToList = isAddToList;
    }

    @Override
    public void run() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (socket == null) {
            logger.error("端口不符合规则，开启监听失败");
            return;
        }
        while (true) {
            try {
                final Socket client = socket.accept();
                final String add = heartBeatSocket(client);
                if (isAddToList)
                    addList.add(add);
                Thread.sleep(100L);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    private String heartBeatSocket(Socket client) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             final OutputStream outputStream = client.getOutputStream()) {
            final String startLine = br.readLine();
            if (startLine.startsWith("GET")) {
                final String[] strings = startLine.split("/");
                final String response = this.getResponse();
                logger.info("{}",client.getInetAddress());
                outputStream.write(response.getBytes());
                outputStream.flush();
                return "http:/" + client.getInetAddress() + ":" + client.getPort();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResponse() {
        return "HTTP/1.1 200 OK\r\nContent-type: text/html;charset=ISO-8859-1\r\n\r\n";
    }

}
