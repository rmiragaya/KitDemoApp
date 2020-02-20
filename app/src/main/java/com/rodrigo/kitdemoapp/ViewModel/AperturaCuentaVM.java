package com.rodrigo.kitdemoapp.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Models.ClientRepoResponse;
import com.rodrigo.kitdemoapp.Models.Cliente;
import com.rodrigo.kitdemoapp.Repo.ClienteRepo;
import com.rodrigo.kitdemoapp.Utils.Tools;

public class AperturaCuentaVM extends AndroidViewModel {
    private static final String TAG = "AperturaCuentaVM";

    private ClienteRepo clienteRepo;
    private LiveData<ClientRepoResponse> clienteResponseLiveData;
    private String token;


    public AperturaCuentaVM(@NonNull Application application) {
        super(application);
        this.clienteRepo = new ClienteRepo();
        this.token = Tools.getTokenFromPreference(application);
        Log.d(TAG, "token: " + this.token);
    }

    public void searchCliente(String idCliente){
        clienteResponseLiveData = clienteRepo.getClient(token, idCliente);
    }

    public LiveData<ClientRepoResponse> getCliente() {
        return clienteResponseLiveData;
    }
}
