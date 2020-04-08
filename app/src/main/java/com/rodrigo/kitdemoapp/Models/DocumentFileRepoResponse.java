package com.rodrigo.kitdemoapp.Models;

import androidx.annotation.NonNull;

import com.rodrigo.kitdemoapp.Utils.StatusResponse;

import java.io.File;

public class DocumentFileRepoResponse {
    private File docuFile;
    private StatusResponse statusResponse;
    private String mensajeError;

    public DocumentFileRepoResponse(File file, @NonNull StatusResponse statusResponse, String mensajeError) {
        this.docuFile = file;
        this.statusResponse = statusResponse;
        this.mensajeError = mensajeError;
    }

    public DocumentFileRepoResponse(File file, @NonNull StatusResponse statusResponse) {
        this.docuFile = file;
        this.statusResponse = statusResponse;
        this.mensajeError = null;
    }

    public File getDocumentFile() {
        return docuFile;
    }

    public void setDocumentList(File file) {
        this.docuFile = file;
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
