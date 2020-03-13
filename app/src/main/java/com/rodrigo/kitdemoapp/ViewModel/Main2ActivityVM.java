package com.rodrigo.kitdemoapp.ViewModel;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.DocumentViewModelResponse;
import com.rodrigo.kitdemoapp.Models.SliderItem;
import com.rodrigo.kitdemoapp.Repo.DocumentRepo;
import com.rodrigo.kitdemoapp.Repo.SliderItemImageRepo;
import com.rodrigo.kitdemoapp.Utils.Tools;

import java.io.File;
import java.util.List;

public class Main2ActivityVM extends AndroidViewModel {
    private static final String TAG = "Main2ActivityVM";

    private MutableLiveData<List<SliderItem>> listSliderLiveData;
    private MutableLiveData<DocumentViewModelResponse> documentViewModelResponseMutableLiveData;
    private SliderItemImageRepo sliderRemo;
    private DocumentRepo documentRepo;


    public Main2ActivityVM(@NonNull Application application) {
        super(application);

        sliderRemo = SliderItemImageRepo.getInstance();
        documentRepo = new DocumentRepo();
    }

    public void init(){
        listSliderLiveData = sliderRemo.getSliderItemList();
    }

    public LiveData<List<SliderItem>> getSliderImagesList(){
        return listSliderLiveData;
    }

    public void addImage(Bitmap imageToAdd){
        List<SliderItem> current = listSliderLiveData.getValue();
        Log.d(TAG, "current.size pre add: " + current.size());
        current.add(new SliderItem(imageToAdd));
        listSliderLiveData.postValue(current);
    }

    public void deleteImage(int position){
        List<SliderItem> current = listSliderLiveData.getValue();
        if ( current == null||current.isEmpty()){
            return;
        }
        current.remove(position);
        listSliderLiveData.postValue(current);
    }

    public void vaciarLista(){
        List<SliderItem> current = listSliderLiveData.getValue();
        if (current!=null){
            current.clear();
            listSliderLiveData.postValue(current);
        }
    }

    public void postDocument(DocumentViewModel dvm, File file){
        documentViewModelResponseMutableLiveData = documentRepo.postDocument(Tools.getTokenFromPreference(getApplication()), dvm, file);
    }

    public LiveData<DocumentViewModelResponse> getDocumentViewModelResponseMutableLiveData() {
        return documentViewModelResponseMutableLiveData;
    }
}
