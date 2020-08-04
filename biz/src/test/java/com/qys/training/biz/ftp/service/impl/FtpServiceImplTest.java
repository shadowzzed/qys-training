package com.qys.training.biz.ftp.service.impl;

import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:23 2020/8/4
 */
public class FtpServiceImplTest {
    @Test
    public void testInstant() {
        ZonedDateTime instant = ZonedDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String date = formatter.format(instant);
        System.out.println(date);
    }

    @Test
    public void testMD5() throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
//        String str = "112222222222222222222222222222222222222222222222222222222221";
//        final byte[] bytes = str.getBytes();
//        md.update(bytes);
//        final byte[] by = md.digest();
//        final BigInteger bigInteger = new BigInteger(1, by);
//        System.out.println(bigInteger.toString(16));
        try (InputStream inputStream = new FileInputStream(new File("D:\\劳动合同参数模版-亘岩网络.pdf"))) {
            byte[] bytes = new byte[1024];
            int n = 0;
            while ((n = inputStream.read(bytes)) != -1) {
                md.update(bytes, 0, n);
            }
            final byte[] newBytes = md.digest();
            final BigInteger bigInteger = new BigInteger(1, newBytes);
            System.out.println(bigInteger.toString(16));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}