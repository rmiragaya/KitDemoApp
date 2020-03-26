package com.rodrigo.kitdemoapp.Repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.DemoRepoResponse;
import com.rodrigo.kitdemoapp.Retrofit.KitDemoApi;
import com.rodrigo.kitdemoapp.Retrofit.RetrofitRequest;
import com.rodrigo.kitdemoapp.StatusResponse;
import com.rodrigo.kitdemoapp.Utils.Tools;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemoRepo {
    private static final String TAG = "DemoRepo";
    private KitDemoApi apiRequest;
    private DemoRepoResponse demoRepoResponse;

    public DemoRepo(){
        apiRequest = RetrofitRequest.getRetrofitInstance().create(KitDemoApi.class);
    }

    public LiveData<DemoRepoResponse> getDemo(final String token){

        final MutableLiveData<DemoRepoResponse> data = new MutableLiveData<>();

        apiRequest.getDemo(token).enqueue(new Callback<Demo>() {
            @Override
            public void onResponse(Call<Demo> call, Response<Demo> response) {
                Log.d(TAG, "onResponse: call");
                Log.d(TAG, "response.code(): " + response.code());
                if (response.code() > 400){
                    demoRepoResponse = new DemoRepoResponse(null, StatusResponse.ERROR_DEMO, "Error Inesperado");
                    Log.d(TAG, "respuesa con error " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.errorBody: " + response.errorBody());
                    data.setValue(demoRepoResponse);
                    return;
                }

                if (response.body() !=null){
                    demoRepoResponse = new DemoRepoResponse(response.body(), StatusResponse.OK);

//                     /* FOR TESTING */
//                    demoRepoResponse.getDemo().setActive(false);
////                    demoRepoResponse.getDemo().setExpired(true);

                    //verify if demo is expire or not active
                    demoRepoResponse = Tools.validarDemo(demoRepoResponse);

                    data.setValue(demoRepoResponse);
                    Log.d(TAG, "Demo.toString: " + response.body().toString());
                    return;
                }

                data.setValue(new DemoRepoResponse(null, StatusResponse.ERROR_CONEXION));
            }

            @Override
            public void onFailure(Call<Demo> call, Throwable t) {
                demoRepoResponse = new DemoRepoResponse(null, StatusResponse.ERROR_CONEXION, "Verifique la red y vuelva a intentarlo");
                Log.d(TAG, "onFailure: call");
                Log.d(TAG, "t.getMessage(): " + t.getMessage());
                Log.d(TAG, "t.getCause(): " + t.getCause());
                Log.d(TAG, " t.toString(): " + t.toString());
                t.fillInStackTrace();
                data.setValue(demoRepoResponse);
            }
        });
        return data;
    }



}
