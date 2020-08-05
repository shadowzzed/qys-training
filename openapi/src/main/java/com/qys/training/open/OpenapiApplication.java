package com.qys.training.open;

import com.qys.training.open.heartbeat.HearBeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:49 2020/8/5
 */
@SpringBootApplication
public class OpenapiApplication {

    private static final Logger logger = LoggerFactory.getLogger(OpenapiApplication.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(OpenapiApplication.class).web(WebApplicationType.SERVLET).build().run(args);
    }


    @Bean
    public HearBeat hearBeat() {
        new Thread(new HearBeatRunnable()).start();
        return null;
    }

    class HearBeatRunnable implements Runnable {

        @Override
        public void run() {
            try {
                ServerSocket socket = new ServerSocket(8888);
                while (true) {
                    final Socket client = socket.accept();
                    heartBeatSocket(client);
                    Thread.sleep(1000L);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void heartBeatSocket(Socket client) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             final OutputStream outputStream = client.getOutputStream()) {
            final String startLine = br.readLine();
            if (startLine.startsWith("GET")) {
                final String response = this.getResponse();
                logger.info("{}",client.getInetAddress());
                outputStream.write(response.getBytes());
                outputStream.flush();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getResponse() {
        return "HTTP/1.1 200 OK\r\nContent-type: text/html;charset=ISO-8859-1\r\n\r\n";
    }

}
