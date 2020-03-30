package com.rodrigo.kitdemoapp.Activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.rodrigo.kitdemoapp.Adapter.SliderAdapter;
import com.rodrigo.kitdemoapp.ConvertImageInTiff;
import com.rodrigo.kitdemoapp.Models.SliderItem;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.Utils.Constant;
import com.rodrigo.kitdemoapp.Utils.ImagenManipulation;
import com.rodrigo.kitdemoapp.Utils.Tools;
import com.rodrigo.kitdemoapp.ViewModel.Main2ActivityVM;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//public class Main2ActivityBorrrar extends AppCompatActivity implements SliderAdapter.OnItemClickListener {
//    private static final String TAG = "Main2ActivityBorrrar";
//
//    private int PERMISSION_CODE = 2;
//    private final int FROMPHOTO = 1000;
//    private final int FROMGALLERY = 1001;
////    private ImageView imageView;
//    private Uri imageUri;
//    private List<Bitmap> listaIamgenes = new ArrayList<>();
//
//    //flehca
//    private ConstraintLayout flecha;
//
//    //viewPager2
//    private ViewPager2 viewPager2;
//    private SliderAdapter adapter;
//    private List<SliderItem> list = new ArrayList<>();
//    private Main2ActivityVM main2ActivityVM;
//
//    private String destino;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2_borrrar);
//
//        //initSlider
//        initSlider();
//        //initSlider
//
//        flecha = findViewById(R.id.tutorialFlechaLayout);
//        CardView camara = findViewById(R.id.camaraBtn);
//        CardView galeria = findViewById(R.id.galeriaBtn);
//        Button siguienteBtn = findViewById(R.id.siguienteBtn);
//        siguienteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                siguiente();
//            }
//        });
//
//        galeria.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
//                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//                        //permission denied
//                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                        //show popup to request runtime permission
//                        requestPermissions(permissions, PERMISSION_CODE);
//                    }
//                    else{
//                        //permission already granted
//                        openGallery();
//                    }
//                }
//                else{
//                    //system OS is < M
//                    openGallery();
//                }
//            }
//        });
//
//        camara.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
//                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
//                            || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
//                        //permission denied
//                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.CAMERA};
//                        //show popup to request runtime permission
//                        requestPermissions(permissions, PERMISSION_CODE);
//                    }
//                    else{
//                        //permission already granted
//                        openCamara();
//                    }
//                }
//                else{
//                    //system OS is < M
//                    openCamara();
//                }
//            }
//        });
//
//        ImageView borrar = findViewById(R.id.borrarBtn);
//        borrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteImage(viewPager2.getCurrentItem());
//            }
//        });
//
//        initVM();
//
//        getDestino(getIntent());
//
//    }
//
//    /**
//     * Dependiendo del string que traiga el intent, se generan
//     * las diferentes rutas para llegar a los destinos, sea
//     * Filiatorio Productos QrBarcode o Recorte de Firma
//     * @param intent: contiene el extra diferenciando el destino
//     */
//    private void getDestino(Intent intent) {
//        destino = intent.getStringExtra("destino");
//
//        if (destino == null) throw new AssertionError();
//
//        if(destino.equalsIgnoreCase(Constant.FILIATORIOS)){
//            Log.d(TAG, "Destino Filiatorio");
//            //todo: crear dialogs y pedir los 3 documentos
//        } else if (destino.equalsIgnoreCase(Constant.PRODUCTO)){
//            Log.d(TAG, "Destino PRODUCTO");
//            //todo: poner Alta Baja Modificacion
//        } else if (destino.equalsIgnoreCase(Constant.QRBARCODE)){
//            Log.d(TAG, "Destino QRBARCODE");
//            //todo: poner Alta Baja Modificacion
//        } else if (destino.equalsIgnoreCase(Constant.RECORTEFIRMA)){
//            Log.d(TAG, "Destino RECORTEFIRMA");
//            //todo: poner Alta Baja Modificacion
//        }
//    }
//
//    private void siguiente() {
//        Log.d(TAG, "siguiente: call");
//        Log.d(TAG, "BitmapList.size(): " + listaIamgenes.size());
//        convertImageInTiff();
//    }
//
//    private void convertImageInTiff(){
//        //todo cambiar el Asynktask por VM
////        new ConvertImageInTiff(this,listaIamgenes, "test" ).execute();
//    }
//
//    //todo: esto debiera venir por interface
//    public void onImageConvert(String vuelta){
//        Log.d(TAG, "onImageConvert: " + vuelta);
//    }
//
//    private void initVM() {
//        main2ActivityVM = ViewModelProviders.of(this).get(Main2ActivityVM.class);
//        main2ActivityVM.init();
//        main2ActivityVM.getSliderImagesList().observe(this, new Observer<List<SliderItem>>() {
//            @Override
//            public void onChanged(List<SliderItem> sliderItems) {
//                if (sliderItems.isEmpty()){
//                    flecha.setVisibility(View.VISIBLE);
//                } else {
//                    flecha.setVisibility(View.GONE);
//                }
//                Log.d(TAG, "onChanged: call");
//                Log.d(TAG, "sliderItem: " + sliderItems.size());
//                list = sliderItems;
//                adapter.updateData(sliderItems);
//            }
//        });
//    }
//
//    private void addImage(Bitmap imageToAdd){
//        main2ActivityVM.addImage(imageToAdd);
//        viewPager2.setCurrentItem(list.size());
//    }
//
//    private void deleteImage(int position){
//        Log.d(TAG, "deleteImage: en la posicion " + position);
//        main2ActivityVM.deleteImage(position);
//        int setCurrentItem = position-1;
//        if (setCurrentItem < 0){
//            setCurrentItem = 0;
//        }
//        viewPager2.setCurrentItem(setCurrentItem);
//    }
//
//    private void initSlider() {
//        adapter = new SliderAdapter(list);
//        adapter.setOnItemClickListener(Main2ActivityBorrrar.this);
//        viewPager2 = findViewById(R.id.imagesViewPAger);
//        viewPager2.setAdapter(adapter);
//        viewPager2.setClipToPadding(false);
//        viewPager2.setClipChildren(false);
//        viewPager2.setOffscreenPageLimit(3);
//        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r = 1 - Math.abs(position);
//                page.setScaleY(0.85f + r * 0.15f);
//            }
//        });
//        viewPager2.setPageTransformer(compositePageTransformer);
//
//    }
//
//    private void openGallery(){
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, FROMGALLERY);
//    }
//
//    private void openCamara(){
//        ContentValues values = new ContentValues();
//        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(takePicture, FROMPHOTO);//zero can be replaced with any action code (called requestCode)
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Log.d(TAG, "onActivityResult: call. RequestCode: " + requestCode + " resultCode: " + resultCode );
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//
//        switch (requestCode) {
//            case FROMPHOTO:
//                Log.d(TAG, "case FROMPHOTO");
//                if (resultCode == RESULT_OK) {
//                    Log.d(TAG, "result_ok");
//                    startCropImageActivity(imageUri);
//                }
//                break;
//            case FROMGALLERY:
//                //si vuelve sin imagen
//                if (data==null){
//                    return;
//                }
//                Log.d(TAG, "case FROMGALLERY");
//                startCropImageActivity(data.getData());
//                break;
//            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                Log.d(TAG, "CROP_IMAGE_ACTIVITY: call ok");
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (result == null){
//                    break;
//                }
//                imageUri = result.getUri();
//                addImageToList(imageUri);
//                break;
//        }
//    }
//
//    private void addImageToList(Uri uriSelected){
//        Bitmap bitmap;
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriSelected);
//            bitmap = ImagenManipulation.resize(bitmap, 794,1123);
//            listaIamgenes.add(bitmap);
//            addImage(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Log.d(TAG, "listaImagenes size " + listaIamgenes.size());
//    }
//
//    private void startCropImageActivity(Uri imageUri) {
//        CropImage.activity(imageUri)
//                .start(this);
//    }
//
//    @Override
//    public void onItemClick(int position) {
////        deleteImage(position);
//    }
//
//    /**
//     * Elimina cuando se termina la actividad
//     */
//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy: call");
//        super.onDestroy();
//        if (!isChangingConfigurations()){
//            main2ActivityVM.vaciarLista();
//            Tools.deleteAllFiles(this);
//        }
//
//    }
//

//}




























