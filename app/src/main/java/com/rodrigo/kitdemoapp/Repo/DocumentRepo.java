package com.rodrigo.kitdemoapp.Repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rodrigo.kitdemoapp.Models.ClientRepoResponse;
import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.Models.DocumentRepoResponse;
import com.rodrigo.kitdemoapp.Retrofit.KitDemoApi;
import com.rodrigo.kitdemoapp.Retrofit.RetrofitRequest;
import com.rodrigo.kitdemoapp.StatusResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentRepo {
    private static final String TAG = "DocumentRepo";
    private KitDemoApi apiRequest;
    private DocumentRepoResponse documentRepoResponse;

    public DocumentRepo() {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(KitDemoApi.class);
    }

    public LiveData<DocumentRepoResponse> getDocuments( final String token){
        final MutableLiveData<DocumentRepoResponse> data = new MutableLiveData<>();

        apiRequest.getDocuments(token).enqueue(new Callback<List<Document>>() {
            @Override
            public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {

                if (response.code() >= 400){
                    documentRepoResponse = new DocumentRepoResponse(null, StatusResponse.ERROR_CONEXION, "Error Inesperado");
                    Log.d(TAG, "respuesa con error " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.errorBody: " + response.errorBody());
                }

                if (response.body() !=null){
                    ArrayList<Document> documentArrayList = new ArrayList<>(response.body());
                    documentRepoResponse = new DocumentRepoResponse(documentArrayList,StatusResponse.OK);
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }

                data.setValue(documentRepoResponse);

            }

            @Override
            public void onFailure(Call<List<Document>> call, Throwable t) {
                documentRepoResponse = new DocumentRepoResponse(null, StatusResponse.ERROR_CLIENTE, t.getMessage() );
                t.fillInStackTrace();
                data.setValue(documentRepoResponse);
            }
        });
        return data;
    }
}
