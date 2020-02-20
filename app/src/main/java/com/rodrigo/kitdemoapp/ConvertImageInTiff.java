package com.rodrigo.kitdemoapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.rodrigo.kitdemoapp.Activities.MainActivity;

import org.beyka.tiffbitmapfactory.CompressionScheme;
import org.beyka.tiffbitmapfactory.Orientation;
import org.beyka.tiffbitmapfactory.TiffSaver;

import java.lang.ref.WeakReference;

public class ConvertImageInTiff extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "ConvertImageInTiff";
    private static final String FILE_TIFF_PATH = Environment.getExternalStorageDirectory().getPath() + "/out1.tif";
    private final WeakReference<MainActivity> mContext;

    private Bitmap bitmap;

    public ConvertImageInTiff(final MainActivity context, Bitmap bitmap) {
        this.mContext = new WeakReference<>(context);
        this.bitmap = bitmap;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");
        //Open some image

        bitmap = resize(bitmap, 2480,3508);

//Create options for saving
        TiffSaver.SaveOptions options = new TiffSaver.SaveOptions();
//By default compression mode is none
        options.compressionScheme = CompressionScheme.CCITTFAX4;
//By default orientation is top left
        options.orientation = Orientation.LEFT_TOP;
//Add new directory to existing file or create new file. If image saved succesfull true will be returned
        Log.d(TAG, "bitmap antes de convertir a Tiff:\nBitMapWidth: " + bitmap.getWidth() + " BitmapHeight: " + bitmap.getHeight() );
        boolean saved = TiffSaver.appendBitmap(FILE_TIFF_PATH, bitmap, options);

        Log.d(TAG, "se guardó?: " + saved);
        Log.d(TAG, "en: " + FILE_TIFF_PATH);


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(TAG, "onPostExecute: call");
        super.onPostExecute(aVoid);
        mContext.get().onImageConvert(FILE_TIFF_PATH);
    }

    private Bitmap convertToMono(Bitmap bitmapColor){
        Log.d(TAG, "convertToMono: call\nBitMapWidth: " + bitmapColor.getWidth() + " BitmapHeight: " + bitmapColor.getHeight() );
        Bitmap bmpMonochrome = Bitmap.createBitmap(bitmapColor.getWidth(), bitmapColor.getHeight(), Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bitmapColor, 0, 0, paint);
        return bitmapColor;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
