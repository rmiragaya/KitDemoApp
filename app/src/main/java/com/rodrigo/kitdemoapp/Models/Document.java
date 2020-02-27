package com.rodrigo.kitdemoapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Document implements Parcelable {
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

    protected Document(Parcel in) {
        id = in.readString();
        serieName = in.readString();
        demoId = in.readString();
        filePath = in.readString();
        client = in.readString();
    }

    public static final Creator<Document> CREATOR = new Creator<Document>() {
        @Override
        public Document createFromParcel(Parcel in) {
            return new Document(in);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };

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

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", serieName='" + serieName + '\'' +
                ", demoId='" + demoId + '\'' +
                ", filePath='" + filePath + '\'' +
                ", client='" + client + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(serieName);
        dest.writeString(demoId);
        dest.writeString(filePath);
        dest.writeString(client);
    }
}
