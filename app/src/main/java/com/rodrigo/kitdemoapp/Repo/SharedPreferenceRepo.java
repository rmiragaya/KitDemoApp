package com.rodrigo.kitdemoapp.Repo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rodrigo.kitdemoapp.Utils.Tools;

public class SharedPreferenceRepo {
    private static final String TAG = "SharedPreferenceRepo";

    public LiveData<String> getSharePref(Context ctx){
        final MutableLiveData<String> data = new MutableLiveData<>();
        data.setValue(Tools.getTokenFromPreference(ctx));
        return data;
    }

}
