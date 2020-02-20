package com.rodrigo.kitdemoapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Models.DemoRepoResponse;
import com.rodrigo.kitdemoapp.Repo.DemoRepo;
import com.rodrigo.kitdemoapp.Repo.SharedPreferenceRepo;
import com.rodrigo.kitdemoapp.Utils.Tools;

public class IntroVM extends AndroidViewModel {

    private SharedPreferenceRepo sharedPreferenceRepo;
    private LiveData<String> tokenSavedResponseLiveData;

    private DemoRepo demoRepo;
    private LiveData<DemoRepoResponse> demoLiveData;

    public IntroVM(@NonNull Application application) {
        super(application);
        sharedPreferenceRepo = new SharedPreferenceRepo();
        demoRepo = new DemoRepo();
        this.tokenSavedResponseLiveData = sharedPreferenceRepo.getSharePref(application);

    }

    public LiveData<String> getTokenSavedResponseLiveData(){
        return tokenSavedResponseLiveData;
    }

    public void updateSavedToken(String newToken){
        Tools.saveTokenOnSharePreference(getApplication(),newToken);
        this.tokenSavedResponseLiveData = sharedPreferenceRepo.getSharePref(getApplication());
    }

    public void searchDemo(String token){
        this.demoLiveData = demoRepo.getDemo(token);
    }

    public LiveData<DemoRepoResponse> getDemoFromToken(){
        return demoLiveData;
    }


}
