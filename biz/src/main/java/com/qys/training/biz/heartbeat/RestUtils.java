package com.qys.training.biz.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zed, shadowl91@163.com
 * @date 17:12 2020/8/5
 */
public class RestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RestUtils.class);

    private static final String request = "GET / HTTP/1.1";

    public static boolean sendGet(String host, int myPort) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Object> uriVar = new HashMap<>();
            uriVar.put("port", myPort);
            restTemplate.getForEntity(host + "?" + myPort, Object.class, uriVar);
            return true;
        } catch (Exception e) {
            logger.error("无法通信");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendGet(String host, Socket client) {
        try (final OutputStream outputStream = client.getOutputStream()) {
            outputStream.write(request.getBytes());
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
