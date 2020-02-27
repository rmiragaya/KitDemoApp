package com.rodrigo.kitdemoapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Interfaces.ISelectAppVM;
import com.rodrigo.kitdemoapp.Models.DocumentRepoResponse;
import com.rodrigo.kitdemoapp.Repo.DocumentRepo;
import com.rodrigo.kitdemoapp.Utils.Tools;

public class SelectAppVM extends AndroidViewModel implements ISelectAppVM {

    private DocumentRepo documentRepo;
    private LiveData<DocumentRepoResponse> clienteResponseLiveData;
    private String token;

    public SelectAppVM(@NonNull Application application) {
        super(application);
        this.documentRepo = new DocumentRepo();
        this.token = Tools.getTokenFromPreference(application);
    }

    public void init(){
        clienteResponseLiveData = documentRepo.getDocuments(token);
    }

    public LiveData<DocumentRepoResponse> getClienteResponseLiveData() {
        return clienteResponseLiveData;
    }
}
