package com.rodrigo.kitdemoapp.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;

import android.util.Log;


import com.rodrigo.kitdemoapp.Activities.ScanActivityTemplate;

import org.beyka.tiffbitmapfactory.CompressionScheme;
import org.beyka.tiffbitmapfactory.Orientation;
import org.beyka.tiffbitmapfactory.TiffSaver;

import java.util.List;

/**
 * Convierte una lista de Bitmaps en TIFF, necesario para enviar la imagen a la API
 * */
public class ConvertImageInTiff extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "ConvertImageInTiff";

    private String pathFileName;
    private List<Bitmap> bitmap;
    private IImageConvertedCallback listener;

    public ConvertImageInTiff(final ScanActivityTemplate context, List<Bitmap> bitmap, String fileName) {
        this.bitmap = bitmap;
        this.pathFileName = context.getExternalFilesDir(null).getAbsolutePath() + "/" + fileName + ".tif";
        this.listener = context;
    }

    public interface IImageConvertedCallback{
        void onImageConvert(String path);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");
        //Open some image
        //Create options for saving
        TiffSaver.SaveOptions options = new TiffSaver.SaveOptions();
        //By default compression mode is none
        options.compressionScheme = CompressionScheme.CCITTFAX4;
        //By default orientation is top left
        options.orientation = Orientation.LEFT_TOP;

        for (Bitmap b : this.bitmap){
            Bitmap current;
            current = b;
            //Add new directory to existing file or create new file. If image saved succesfull true will be returned
            Log.d(TAG, "bitmap antes de convertir a Tiff:\nBitMapWidth: " + current.getWidth() + " BitmapHeight: " + current.getHeight() );
            boolean saved = TiffSaver.appendBitmap(this.pathFileName, current, options);

            Log.d(TAG, "se guardó?: " + saved);
            Log.d(TAG, "en: " + this.pathFileName);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(TAG, "onPostExecute: call");
        super.onPostExecute(aVoid);
        listener.onImageConvert(this.pathFileName);
    }

}
