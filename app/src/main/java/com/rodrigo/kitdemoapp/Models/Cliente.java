package com.rodrigo.kitdemoapp.Models;

public class Cliente {
    private String clientId;
    private String businessName;
    private String email;

    public Cliente(String clientId, String businessName, String email) {
        this.clientId = clientId;
        this.businessName = businessName;
        this.email = email;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "clientId='" + clientId + '\'' +
                ", businessName='" + businessName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
