package com.rodrigo.kitdemoapp.Interfaces;

import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Models.DocumentsRepoResponse;

public interface ISelectAppVM {

    void init();
    LiveData<DocumentsRepoResponse> getClienteResponseLiveData();
}
