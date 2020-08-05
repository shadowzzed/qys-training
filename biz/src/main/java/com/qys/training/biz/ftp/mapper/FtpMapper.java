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
    /**
     * 插入一条数据
     * @param file
     * @return
     */
    int insertFile(File file);

    /**
     * 获得指定ID文件路径
     * @param id
     * @return
     */
    String getFilePath(long id);

    /**
     * 更新文件
     * @param file
     */
    void updateFile(File file);

    /**
     * 获取指定ID文件名称
     * @param id
     * @return
     */
    String getFileName(long id);

    /**
     * 删除文件
     * @param id
     */
    void deleteFile(long id);

    /**
     * size范围，分页，名称组合查询
     * @param param
     * @return
     */
    List<File> selectFileDB(QueryFileParam param);

    /**
     * 批量查询文件路径，打包压缩下载
     * @param idList
     * @return
     */
    List<File> selectBatchPath(@Param("idList")List<Long> idList);
}
