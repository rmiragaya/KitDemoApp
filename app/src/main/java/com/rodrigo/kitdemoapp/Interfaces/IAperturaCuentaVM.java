package com.rodrigo.kitdemoapp.Interfaces;

import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Models.ClientRepoResponse;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.MetadataClient;

public interface IAperturaCuentaVM {

    void searchCliente(String idCliente);
    LiveData<ClientRepoResponse> getCliente();
    void saveDemo(Demo demo);
    Demo getDemo();
    void saveMetadata(MetadataClient metadataClient);
}
