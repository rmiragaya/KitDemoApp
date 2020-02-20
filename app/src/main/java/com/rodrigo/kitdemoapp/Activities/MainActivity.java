package com.rodrigo.kitdemoapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rodrigo.kitdemoapp.ConvertImageInTiff;
import com.rodrigo.kitdemoapp.Retrofit.KitDemoApi;
import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.beyka.tiffbitmapfactory.TiffBitmapFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private int PERMISSION_CODE = 2;
    private ImageView view;
    private Uri imageUri, resultUri;
    private Bitmap bitmapSelected;
    private File file;

    //RetroFit
    private KitDemoApi kitDemoApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                                 || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                        //permission denied
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA};
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        selectImage();
                    }
                }
                else{
                    //system OS is < M
                    selectImage();
                }
            }
        });

        binding.siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertImage();
            }
        });

        view = findViewById(R.id.imagenImageView);

        binding.imagenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "imagen", Toast.LENGTH_SHORT).show();
                postDocument();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apikitdemohp.soluciones.com.ar/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        kitDemoApi = retrofit.create(KitDemoApi.class);

        getDocuments("4af61b2e-b130-45b7-beec-6ec41dac5317");
    }

    private void getDocuments(String token){
        Log.d(TAG, "getDocuments: ");
        Call<List<Document>> call = kitDemoApi.getDocuments(token);

        call.enqueue(new Callback<List<Document>>() {
            @Override
            public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
                if (!response.isSuccessful()){
                    Log.d(TAG, "code: " + response.code());
                    return;
                }

                List<Document> documents = response.body();

                int x = 1;
                for (Document document : documents){
                    Log.d(TAG, "Documento " + x + ": "
                            + document.getDemoId()
                            + " " + document.getClient()
                            + " " + document.getSerieName());
                    x++;
                }
            }

            @Override
            public void onFailure(Call<List<Document>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.fillInStackTrace());
            }
        });
    }

    private void selectImage(){
        CropImage.startPickImageActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "PICK_IMAGE_CHOOSER: call");
            imageUri = CropImage.getPickImageResultUri(this, data);
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "CROP_IMAGE_ACTIVITY: call");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
               resultUri = result.getUri();


                Picasso.get().load(resultUri)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(binding.imagenImageView);


                try {
                    bitmapSelected = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "error: " + error.toString());
            }
        }
    }

    public void onImageConvert(String imageTiffPath){
        Log.d(TAG, "imageTiffPath: " + imageTiffPath);


//        Picasso.get().load("/sdcard/outjpg.jpg")
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .error(R.drawable.ic_launcher_foreground)
//                .into(binding.imagenImageView);


        showTiffFile(imageTiffPath);
//        File file = new File("/sdcard/out1.tif");
//        TiffBitmapFactory.Options options = new TiffBitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        TiffBitmapFactory.decodeFile(file, options);
//
//        Bitmap bmp = TiffBitmapFactory.decodeFile(file, options);
//        binding.imagenImageView.setImageBitmap(bmp);

    }

    private void showTiffFile(String tiffFilePath) {
        Log.d(TAG, "tiffFilePath: " + tiffFilePath);
        file = new File(tiffFilePath);
        Log.d(TAG, "showTiffFile: file: " + file);

//Read data about image to Options object
        TiffBitmapFactory.Options options = new TiffBitmapFactory.Options();
        options.inJustDecodeBounds = true;
        TiffBitmapFactory.decodeFile(file, options);

        int dirCount = options.outDirectoryCount;

//Read and process all images in file
        for (int i = 0; i < dirCount; i++) {
            options.inDirectoryNumber = i;
            TiffBitmapFactory.decodeFile(file, options);
            int curDir = options.outCurDirectoryNumber;
            int width = options.outWidth;
            int height = options.outHeight;
            //Change sample size if width or height bigger than required width or height
            int inSampleSize = 1;
            if (height > 300 || width > 300) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > 200
                        && (halfWidth / inSampleSize) > 200) {
                    inSampleSize *= 2;
                }
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;

            // Specify the amount of memory available for the final bitmap and temporary storage.
            options.inAvailableMemory = 20000000; // bytes

            Bitmap bmp = TiffBitmapFactory.decodeFile(file, options);
            Log.d(TAG, "showTiffFile: bmp: " + bmp);
            binding.imagenImageView.setImageBitmap(bmp);
        }
    }

    private void convertImage(){
        if (imageUri == null){
            Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        new ConvertImageInTiff(this, bitmapSelected).execute();
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(this);
    }

    private void postDocument(){
        Document documentToPost = new Document("2233", "Filiation", "demoIdPost", "filePathPost", "ClientPost");
        Gson g = new Gson();
        String documentToPostString = g.toJson(documentToPost);

        RequestBody modelParttoString = RequestBody.create(MultipartBody.FORM, "{\"client\":\"12345\",\"serieName\":\"Modification\",\"metadataViewModel\":{\"businessName\":\"Felix Lopez\",\"country\":\"Argentina\",\"date\":\"2020-02-14T12:38:36.391Z\",\"documentName\":\"ty\",\"email\":\"Felix@mailreal.com\",\"sex\":\"No especifica\"},\"demoId\":1}");
        RequestBody filePart = RequestBody.create(MultipartBody.FORM, file);

        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData("File", "testFileName", filePart);


        Call<ResponseBody> call = kitDemoApi.createDocument("4af61b2e-b130-45b7-beec-6ec41dac5317",modelParttoString, filePart2);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: ok");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: call");
            }
        });

//        RequestBody modelPart = RequestBody.create(getContentResolver().getType());

    }
}
