package com.qys.training.biz.ftp.mapper;

import com.qys.training.biz.ftp.entity.File;
import com.qys.training.biz.ftp.entity.QueryFileParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zed, shadowl91@163.com
 * @date 14:25 2020/8/4
 */
public interface FtpMapper {
    int insertFile(File file);
    String getFilePath(long id);
    void updateFile(File file);
    String getFileName(long id);
    void deleteFile(long id);


    List<File> selectFileDB(QueryFileParam param);
    List<String> selectBatchPath(@Param("idList")List<Long> idList);
}
