package com.qys.training.biz.ftp.entity;

import com.qys.training.base.entity.BaseEntity;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:48 2020/8/4
 */
public class File extends BaseEntity {
    private String fileName;
    private Long fileSize;
    private String fileHash;
    private String filePath;

    public File() {
    }

    @Override
    public String toString() {
        return "File{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileHash='" + fileHash + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
