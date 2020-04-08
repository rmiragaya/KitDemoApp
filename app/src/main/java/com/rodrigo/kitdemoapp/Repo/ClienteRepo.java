package com.rodrigo.kitdemoapp.Repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rodrigo.kitdemoapp.Models.ClientRepoResponse;
import com.rodrigo.kitdemoapp.Models.Cliente;
import com.rodrigo.kitdemoapp.Retrofit.KitDemoApi;
import com.rodrigo.kitdemoapp.Retrofit.RetrofitRequest;
import com.rodrigo.kitdemoapp.Utils.StatusResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepo {
    private static final String TAG = "ClienteRepo";
    private KitDemoApi apiRequest;
    private ClientRepoResponse clientRepoResponse;

    public ClienteRepo(){
        apiRequest = RetrofitRequest.getRetrofitInstance().create(KitDemoApi.class);
    }

    public LiveData<ClientRepoResponse> getClient ( final String token, final String clientId){

        final MutableLiveData<ClientRepoResponse> data = new MutableLiveData<>();

        apiRequest.getClient(token, clientId).enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {

                if (response.code() >= 400){
                    clientRepoResponse = new ClientRepoResponse(null, StatusResponse.ERROR_CONEXION, "Error Inesperado");
                    Log.d(TAG, "respuesa con error " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.errorBody: " + response.errorBody());
                }

                if (response.body() !=null){
                    clientRepoResponse = new ClientRepoResponse(response.body(),StatusResponse.OK);
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }

                data.setValue(clientRepoResponse);
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                clientRepoResponse = new ClientRepoResponse(null, StatusResponse.ERROR_CLIENTE, t.getMessage() );
                t.fillInStackTrace();
                data.setValue(clientRepoResponse);
            }
        });

        return data;
    }
}
