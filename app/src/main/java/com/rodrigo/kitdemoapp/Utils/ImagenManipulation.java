package com.rodrigo.kitdemoapp.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.rodrigo.kitdemoapp.Models.SliderItem;
import com.rodrigo.kitdemoapp.R;

public class ImagenManipulation {

        public static Bitmap loadImage(String logoEnString){

            byte[] decodeString = Base64.decode(logoEnString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString, 0 , decodeString.length);

            return  decodedByte;
        }

        public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
            if (maxHeight > 0 && maxWidth > 0) {
                int width = image.getWidth();
                int height = image.getHeight();
                float ratioBitmap = (float) width / (float) height;
                float ratioMax = (float) maxWidth / (float) maxHeight;

                int finalWidth = maxWidth;
                int finalHeight = maxHeight;
                if (ratioMax > ratioBitmap) {
                    finalWidth = (int) ((float)maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float)maxWidth / ratioBitmap);
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
                return image;
            } else {
                return image;
            }
        }

        public static SliderItem convertBitmapToSliderItem(Bitmap bitmapToConvert){


            SliderItem sliderItemconvert = new SliderItem(bitmapToConvert);
            return sliderItemconvert;
        }
}