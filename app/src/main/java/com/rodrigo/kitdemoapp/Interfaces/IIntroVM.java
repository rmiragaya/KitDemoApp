package com.rodrigo.kitdemoapp.Interfaces;

import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Models.DemoRepoResponse;

public interface IIntroVM {

    LiveData<String> getTokenSavedResponseLiveData();
    void searchDemo(String token);
    LiveData<DemoRepoResponse> getDemoFromToken();
    void updateSavedToken(String newToken);
}
