package com.qys.training.biz.heartbeat;

import com.qys.training.biz.hazelcast.entity.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zed, shadowl91@163.com
 * @date 16:58 2020/8/5
 */
public class HeartBeatRunnable implements Runnable {

    private static final List<String> addList = new ArrayList<>();

    private static final Pattern pattern = Pattern.compile("[0-9]{4,}");

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
                if (isAddToList && !StringUtils.isEmpty(add))
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
//            logger.info(startLine);
            if (startLine.startsWith("GET")) {
                final Matcher matcher = pattern.matcher(startLine);
                String substring = null;
                if (matcher.find()) {
                    substring = matcher.group();
                }
                if (StringUtils.isEmpty(substring)) {
                    logger.error("没有获取端口号，检测客户机的端口号");
                    return null;
                }
//                final String[] strings = startLine.split("/");
//                logger.info("分组结果");
//                Arrays.stream(strings).forEach(logger::info);
//                logger.info("分组结果");
//                final String substring = strings[1].substring(1, 5);
//                logger.info("substring" + substring);
                final String response = this.getResponse();
                logger.info("keep alive {}:{}",client.getInetAddress(), Integer.valueOf(substring));
                outputStream.write(response.getBytes());
                outputStream.flush();
                return "http:/" + client.getInetAddress() + ":" + Integer.valueOf(substring);
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
