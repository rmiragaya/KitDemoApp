package com.rodrigo.kitdemoapp.ViewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Interfaces.IAperturaCuentaVM;
import com.rodrigo.kitdemoapp.Models.ClientRepoResponse;
import com.rodrigo.kitdemoapp.Models.Cliente;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.MetadataClient;
import com.rodrigo.kitdemoapp.Repo.ClienteRepo;
import com.rodrigo.kitdemoapp.Utils.Tools;

public class AperturaCuentaVM extends AndroidViewModel implements IAperturaCuentaVM {
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

//    public void saveDemo(Demo demo, Context ctx){
    public void saveDemo(Demo demo){
        Tools.saveDemoOnSharePreference(getApplication(),demo);
    }

    public Demo getDemo(){
        return Tools.getDemoFromSharePreference(getApplication());
    }

    public void saveMetadata(MetadataClient metadataClient){
        Tools.saveMetadataClientSharePreference(getApplication(), metadataClient);
    }

    public MetadataClient getMetadataClient(){
        return Tools.getMetadataClientSharePreference(getApplication());
    }
}
