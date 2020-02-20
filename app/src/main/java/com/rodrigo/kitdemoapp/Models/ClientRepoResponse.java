package com.rodrigo.kitdemoapp.Models;

import androidx.annotation.NonNull;

import com.rodrigo.kitdemoapp.StatusResponse;

public class ClientRepoResponse {
    private Cliente cliente;
    private StatusResponse statusResponse;
    private String mensajeError;


    public ClientRepoResponse(Cliente cliente, @NonNull StatusResponse statusResponse, String mensajeError) {
        this.cliente = cliente;
        this.statusResponse = statusResponse;
        this.mensajeError = mensajeError;
    }

    public ClientRepoResponse(Cliente cliente, @NonNull StatusResponse statusResponse) {
        this.cliente = cliente;
        this.statusResponse = statusResponse;
        this.mensajeError = null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
