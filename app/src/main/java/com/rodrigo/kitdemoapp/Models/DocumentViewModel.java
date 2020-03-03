package com.rodrigo.kitdemoapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DocumentViewModel implements Parcelable {
    private String id;
    private String serieName;
    private String demoId;
    private String filePath;
    private String client;
    @SerializedName("metadataViewModel")
    private MetadataClient metadataClient;

    public DocumentViewModel(String id, String serieName, String demoId, String filePath, String client, MetadataClient metadataClient) {
        this.id = id;
        this.serieName = serieName;
        this.demoId = demoId;
        this.filePath = filePath;
        this.client = client;
        this.metadataClient = metadataClient;
    }

    protected DocumentViewModel(Parcel in) {
        id = in.readString();
        serieName = in.readString();
        demoId = in.readString();
        filePath = in.readString();
        client = in.readString();
    }

    public static final Creator<DocumentViewModel> CREATOR = new Creator<DocumentViewModel>() {
        @Override
        public DocumentViewModel createFromParcel(Parcel in) {
            return new DocumentViewModel(in);
        }

        @Override
        public DocumentViewModel[] newArray(int size) {
            return new DocumentViewModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public String getDemoId() {
        return demoId;
    }

    public void setDemoId(String demoId) {
        this.demoId = demoId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public MetadataClient getMetadataClient() {
        return metadataClient;
    }

    public void setMetadataClient(MetadataClient metadataClient) {
        this.metadataClient = metadataClient;
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

    @Override
    public String toString() {
        return "DocumentViewModel{" +
                "id='" + id + '\'' +
                ", serieName='" + serieName + '\'' +
                ", demoId='" + demoId + '\'' +
                ", filePath='" + filePath + '\'' +
                ", client='" + client + '\'' +
                ", metadataClient=" + metadataClient +
                '}';
    }
}
