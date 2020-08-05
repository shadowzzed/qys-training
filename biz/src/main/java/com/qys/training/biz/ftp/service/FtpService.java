package com.qys.training.biz.ftp.service;

import com.qys.training.biz.ftp.entity.File;
import com.qys.training.biz.ftp.entity.QueryFileParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:22 2020/8/4
 */
public interface FtpService {
    /**
     * 上传
     * @param file
     * @return
     */
    long upload(MultipartFile file);

    /**
     * 更新
     * @param file
     * @param fileId
     */
    void update(MultipartFile file, long fileId);

    /**
     * 下载
     * @param id
     * @param resp
     * @throws UnsupportedEncodingException
     */
    void download(long id, HttpServletResponse resp) throws UnsupportedEncodingException;

    /**
     * 删除
     * @param id
     */
    void delete(long id);

    /**
     * 按照指定参数组合查询
     * @param param
     * @return
     */
    List<File> selectFileDB(QueryFileParam param);

    /**
     * 多文件压缩下载
     * @param list
     * @param resp
     * @throws IOException
     */
    void downloadZIP(List<Long> list, HttpServletResponse resp) throws IOException;
}
