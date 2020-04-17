package com.rodrigo.kitdemoapp.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.rodrigo.kitdemoapp.Models.SliderItem;
import com.rodrigo.kitdemoapp.R;

/**
 *
 */
public class ImagenManipulation {

    /**
     * Pasa de string a bitmap
     * @param logoEnString el logo de la empresa en String
     * @return Bitmap
     */
        public static Bitmap loadImage(String logoEnString){
            byte[] decodeString = Base64.decode(logoEnString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodeString, 0 , decodeString.length);
        }

    /**
     * La imagen que le pasemos, la ajusta a los tamaños maximos SIN perder el aspect ratio
     * y de fondo le pone una imagen blanca tamaño A4 sobre la cual pone la imagen centrada
     * @param originalImage imagen original para modificar
     * @param width ancho maximo
     * @param height alto maximo
     * @return imagen con los tamaños maximos (A4) con fondo blanco
     */
        public static Bitmap resize(Bitmap originalImage, int width, int height) {
            Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            float originalWidth = originalImage.getWidth();
            float originalHeight = originalImage.getHeight();

            Canvas canvas = new Canvas(background);

            float scale = width / originalWidth;

            float xTranslation = 0.0f;
            float yTranslation = (height - originalHeight * scale) / 2.0f;

            Matrix transformation = new Matrix();
            transformation.postTranslate(xTranslation, yTranslation);
            transformation.preScale(scale, scale);

            Paint paint = new Paint();
            paint.setFilterBitmap(true);

            canvas.drawBitmap(originalImage, transformation, paint);

            return background;
        }

    /**
     *Da la imagen scale,sin cortarla, pero no rellena los espacios en blanco.
     */
    public static Bitmap scaleBitmapAndKeepRation(Bitmap targetBmp,int reqWidthInPixels,int reqHeightInPixels)
    {
        Matrix matrix = new Matrix();
        matrix .setRectToRect(new RectF(0, 0, targetBmp.getWidth(), targetBmp.getHeight()), new RectF(0, 0, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        Bitmap scaledBitmap = Bitmap.createBitmap(targetBmp, 0, 0, targetBmp.getWidth(), targetBmp.getHeight(), matrix, true);
        return overlay(scaledBitmap);
    }

    /**
     *Combina 2 Bitmaps centrados (rellena lso espacios en blanco)
     */
    private static Bitmap overlay(Bitmap bmp2) {
            Bitmap bmp1 = Bitmap.createBitmap((int)794, (int)1123, Bitmap.Config.ARGB_8888);
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        float with = (794 - bmp2.getWidth()) / 2;
        float higth = (1123 - bmp2.getHeight()) / 2;
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, with,higth, null);
        return bmOverlay;
    }

}