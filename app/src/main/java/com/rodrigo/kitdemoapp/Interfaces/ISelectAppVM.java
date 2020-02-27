package com.rodrigo.kitdemoapp.Interfaces;

import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Models.DocumentRepoResponse;

public interface ISelectAppVM {

    void init();
    LiveData<DocumentRepoResponse> getClienteResponseLiveData();
}
