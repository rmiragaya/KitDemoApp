package com.rodrigo.kitdemoapp.Models;

import androidx.annotation.NonNull;

import com.rodrigo.kitdemoapp.Utils.StatusResponse;

public class DocumentViewModelResponse {
    private DocumentViewModel documentViewModel;
    private StatusResponse statusResponse;
    private String mensajeError;

    public DocumentViewModelResponse(DocumentViewModel documentViewModel, @NonNull StatusResponse statusResponse, String mensajeError) {
        this.documentViewModel = documentViewModel;
        this.statusResponse = statusResponse;
        this.mensajeError = mensajeError;
    }

    public DocumentViewModelResponse(DocumentViewModel documentViewModel, @NonNull StatusResponse statusResponse) {
        this.documentViewModel = documentViewModel;
        this.statusResponse = statusResponse;
        this.mensajeError = null;
    }

    public DocumentViewModel getDocumentViewModel() {
        return documentViewModel;
    }

    public void setDocumentViewModel(DocumentViewModel documentViewModel) {
        this.documentViewModel = documentViewModel;
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
