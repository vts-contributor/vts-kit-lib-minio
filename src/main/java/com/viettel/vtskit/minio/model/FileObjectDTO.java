package com.viettel.vtskit.minio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.vtskit.minio.mapper.BaseDTO;

public class FileObjectDTO extends BaseDTO {
    private String name;
    private boolean isPublicMode;
    private String contentType;
    private long size;
    private String lastModifiedText;
    private Long lastModifiedVal;
    private boolean isDirectory;
    private String versionId;

    @JsonIgnore
    private byte[] fileData;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublicMode() {
        return isPublicMode;
    }

    public void setPublicMode(boolean publicMode) {
        isPublicMode = publicMode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }

    public Long getLastModifiedVal() {
        return lastModifiedVal;
    }

    public void setLastModifiedVal(Long lastModifiedVal) {
        this.lastModifiedVal = lastModifiedVal;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
}
