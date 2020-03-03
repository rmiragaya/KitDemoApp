package com.rodrigo.kitdemoapp.Models;

import androidx.annotation.NonNull;

import com.rodrigo.kitdemoapp.StatusResponse;

import java.util.ArrayList;
import java.util.List;

public class DocumentsRepoResponse {
    private List<Document> documentList;
    private StatusResponse statusResponse;
    private String mensajeError;

    public DocumentsRepoResponse(ArrayList<Document> documentList, @NonNull StatusResponse statusResponse, String mensajeError) {
        this.documentList = documentList;
        this.statusResponse = statusResponse;
        this.mensajeError = mensajeError;
    }

    public DocumentsRepoResponse(ArrayList<Document> documentList, @NonNull StatusResponse statusResponse) {
        this.documentList = documentList;
        this.statusResponse = statusResponse;
        this.mensajeError = null;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
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
