package com.rodrigo.kitdemoapp.Repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rodrigo.kitdemoapp.Models.SliderItem;
import com.rodrigo.kitdemoapp.R;

import java.util.ArrayList;
import java.util.List;

public class SliderItemImageRepo {
    private static final String TAG = "SliderItemImageRepo";

    private static SliderItemImageRepo instance;
    private List<SliderItem> sliderItemList;

    public static SliderItemImageRepo getInstance() {
        if (instance == null ){
            instance = new SliderItemImageRepo();
        }
        return instance;
    }

    private SliderItemImageRepo() {
        sliderItemList =  new ArrayList<>();
    }

    public void addImage(SliderItem sliderItem){
//        sliderItemList.add(new SliderItem(R.drawable.firmaimage));
//        sliderItemList.add(new SliderItem(R.drawable.chekimage));
//        sliderItemList.add(new SliderItem(R.drawable.bandera_chile));
//        sliderItemList.add(new SliderItem(R.drawable.firmaimage));
//        sliderItemList.add(new SliderItem(R.drawable.chekimage));
//        sliderItemList.add(new SliderItem(R.drawable.bandera_peru));
//        sliderItemList.add(sliderItem);
    }

    public MutableLiveData<List<SliderItem>> getSliderItemList() {
        final MutableLiveData<List<SliderItem>> data = new MutableLiveData<>();
        data.setValue(sliderItemList);
        return data;
    }
}
