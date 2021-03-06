package com.rodrigo.kitdemoapp.Models;

import android.util.Base64;

import com.rodrigo.kitdemoapp.Utils.Tools;

import java.util.Date;

public class Demo {

    private int id;
    private int tcId;
    private String name;
    private String clientName;
    private String description;
    private String logo;
    private String token;
    private String tokenExpirationTime;
    private boolean isExpired;
    private boolean isActive;
    private String creationTime;
    private String updateTime;
    private String url;
    private String workPathConfig;
    private String oxpdConfig;
    private String clientNameNew;

    public Demo(int id, int tcId, String name, String clientName, String description, Base64 logo, String token, Date tokenExpirationTime, boolean isExpired, boolean isActive, Date creationTime, Date updateTime, String url, String workPathConfig, String oxpdConfig) {
        this.id = id;
        this.tcId = tcId;
        this.name = name;
        this.clientName = clientName;
        this.description = description;
        this.logo = logo.toString();
        this.token = token;
        this.tokenExpirationTime = Tools.convertDateTimeInDateString(tokenExpirationTime.toString());
        this.isExpired = isExpired;
        this.isActive = isActive;
        this.creationTime = Tools.convertDateTimeInDateString(creationTime.toString());
        this.updateTime = updateTime.toString();
        this.url = url;
        this.workPathConfig = workPathConfig;
        this.oxpdConfig = oxpdConfig;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTcId() {
        return tcId;
    }

    public void setTcId(int tcId) {
        this.tcId = tcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenExpirationTime() {
        return Tools.convertDateTimeInDateString(tokenExpirationTime);
    }

    public void setTokenExpirationTime(Date tokenExpirationTime) {
        this.tokenExpirationTime = Tools.convertDateTimeInDateString(tokenExpirationTime.toString());
    }

    public String getCreationTime() {
        return Tools.convertDateTimeInDateString(creationTime);
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = Tools.convertDateTimeInDateString(creationTime.toString());
    }

    public String getClient() {
        return clientName;
    }

//    public void setClient(String client) {
//        this.client = client;
//    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWorkPathConfig() {
        return workPathConfig;
    }

    public void setWorkPathConfig(String workPathConfig) {
        this.workPathConfig = workPathConfig;
    }

    public String getOxpdConfig() {
        return oxpdConfig;
    }

    public void setOxpdConfig(String oxpdConfig) {
        this.oxpdConfig = oxpdConfig;
    }

    public String getClientNameNew() {
        return clientNameNew;
    }

    public void setClientNameNew(String clientNameNew) {
        this.clientNameNew = clientNameNew;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", newName=" + clientNameNew +
                ", tcId=" + tcId +
                ", name='" + name + '\'' +
                ", client='" + clientName + '\'' +
                ", description='" + description + '\'' +
//                ", logo='" + logo + '\'' +
                ", token='" + token + '\'' +
                ", tokenExpirationTime='" + tokenExpirationTime + '\'' +
                ", isExpired=" + isExpired +
                ", isActive=" + isActive +
                ", creationTime='" + creationTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", url='" + url + '\'' +
                ", workPathConfig='" + workPathConfig + '\'' +
                ", oxpdConfig='" + oxpdConfig + '\'' +
                '}';
    }
}
