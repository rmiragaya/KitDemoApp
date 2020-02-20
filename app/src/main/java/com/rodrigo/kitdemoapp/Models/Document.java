package com.rodrigo.kitdemoapp.Models;

public class Document {
    private String id;
    private String serieName;
    private String demoId;
    private String filePath;
    private String client;

    public Document(String id, String serieName, String demoId, String filePath, String client) {
        this.id = id;
        this.serieName = serieName;
        this.demoId = demoId;
        this.filePath = filePath;
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public String getSerieName() {
        return serieName;
    }

    public String getDemoId() {
        return demoId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getClient() {
        return client;
    }
}
