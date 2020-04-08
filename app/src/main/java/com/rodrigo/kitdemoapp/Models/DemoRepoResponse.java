package com.rodrigo.kitdemoapp.Models;

import androidx.annotation.NonNull;

import com.rodrigo.kitdemoapp.Utils.StatusResponse;

public class DemoRepoResponse {
    private Demo demo;
    private StatusResponse statusResponse;
    private String mensajeError;

    public DemoRepoResponse(Demo demo, @NonNull StatusResponse statusResponse, String mensajeError) {
        this.demo = demo;
        this.statusResponse = statusResponse;
        this.mensajeError = mensajeError;
    }

    public DemoRepoResponse(Demo demo, StatusResponse statusResponse) {
        this.demo = demo;
        this.statusResponse = statusResponse;
        this.mensajeError = null;
    }

    public Demo getDemo() {
        return demo;
    }

    public void setDemo(Demo demo) {
        this.demo = demo;
    }

    public StatusResponse getStatusResponse() {
        return statusResponse;
    }

    public void setStatusResponse(StatusResponse statusResponse) {
        this.statusResponse = statusResponse;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
}
