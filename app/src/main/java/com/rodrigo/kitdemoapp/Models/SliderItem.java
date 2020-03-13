package com.rodrigo.kitdemoapp.Models;

import android.graphics.Bitmap;

public class SliderItem {

//    private int image;
//
//    public SliderItem(int image) {
//        this.image = image;
//    }
//
//    public int getImage() {
//        return image;
//    }

    private Bitmap image;

    public SliderItem(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
