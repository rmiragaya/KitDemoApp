package com.rodrigo.kitdemoapp.Activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.rodrigo.kitdemoapp.Adapter.SliderAdapter;
import com.rodrigo.kitdemoapp.ConvertImageInTiff;
import com.rodrigo.kitdemoapp.Dialogs.DemoErrorDialog;
import com.rodrigo.kitdemoapp.Dialogs.FinalizacionDeTrabajoDialog;
import com.rodrigo.kitdemoapp.Models.DemoRepoResponse;
import com.rodrigo.kitdemoapp.Models.DocumentVMandFile;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.DocumentViewModelResponse;
import com.rodrigo.kitdemoapp.Models.SliderItem;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.StatusResponse;
import com.rodrigo.kitdemoapp.Utils.ImagenManipulation;
import com.rodrigo.kitdemoapp.Utils.Tools;
import com.rodrigo.kitdemoapp.ViewModel.Main2ActivityVM;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ScanActivityTemplate extends AppCompatActivity implements SliderAdapter.OnItemClickListener,
        FinalizacionDeTrabajoDialog.FinalizacionDeTrabajoListener,
        ConvertImageInTiff.IImageConvertedCallback {
    private static final String TAG = "ScanActivityTemplate";


    private int PERMISSION_CODE = 2;
    private final int FROMPHOTO = 1000;
    private final int FROMGALLERY = 1001;

    //private ImageView imageView;
    private Uri imageUri;
    private List<Bitmap> listaIamgenes = new ArrayList<>();

    //flehca
    private ConstraintLayout flecha;

    //viewPager2
    private ViewPager2 viewPager2;
    private SliderAdapter adapter;
    private List<SliderItem> list = new ArrayList<>();
    private Main2ActivityVM main2ActivityVM;

    private ConstraintLayout progressBar;

    private TextView titulo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_borrrar);

        //initSlider
        initSlider();

        titulo = findViewById(R.id.tituliId);
        progressBar = findViewById(R.id.llProgressBar);
        flecha = findViewById(R.id.tutorialFlechaLayout);
        CardView camara = findViewById(R.id.camaraBtn);
        CardView galeria = findViewById(R.id.galeriaBtn);
        Button siguienteBtn = findViewById(R.id.siguienteBtn);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getListaIamgenes().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Agregue una imagen", Toast.LENGTH_SHORT).show();
                    return;
                }
//                siguiente();
//                changeTitle();
                convert();
            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //permission denied
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        openGallery();
                    }
                }
                else{
                    //system OS is < M
                    openGallery();
                }
            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
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
                        openCamara();
                    }
                }
                else{
                    //system OS is < M
                    openCamara();
                }
            }
        });

        ImageView borrar = findViewById(R.id.borrarBtn);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(viewPager2.getCurrentItem());
            }
        });

        changeTitle();
        initVM();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTitle();
    }

    /**
     * call when the file was uploaded
     */
    public abstract void archivoSubidoListener();

    /**
     * calls when image is converted
     * @param pathOfFileCreated gives file converted Path
     * return Boolean when all the documents are converted
     * and needs to start the upload
     */
    public abstract Boolean imagesConvertedAndEmptyList(String pathOfFileCreated);

    /**
     * @return the file name to convert the file
     */
    public abstract String getFileName();


    /**
     *
     * @return all the DocumentVMandFile to upload
     */
    public abstract List<DocumentVMandFile> getAllDocumentsAndFiles();


    /**
     *
     * @return the string to change the title of the activity
     */
    public abstract String getActivityTitle();


    private void convert(){
        //todo cambiar el Asynktask por VM
        String fileName = getFileName();
        new ConvertImageInTiff(this,getListaIamgenes(), fileName).execute();
    }

    @Override
    public void onImageConvert(String vuelta){
        Log.d(TAG, "onImageConvert: " + vuelta);
        main2ActivityVM.vaciarLista();
        listaIamgenes.clear();

        //if true, start convert and post
        if (imagesConvertedAndEmptyList(vuelta)){
            Log.d(TAG, "Load Todo los Archivos");
            List<DocumentVMandFile> documentVMandFile = getAllDocumentsAndFiles();
            for (DocumentVMandFile dvm : documentVMandFile){
                createAndPostDocument(dvm.getDvm(), dvm.getFile());
            }
        }
    }

    private void initVM() {
        main2ActivityVM = ViewModelProviders.of(this).get(Main2ActivityVM.class);
        main2ActivityVM.init();
        main2ActivityVM.getSliderImagesList().observe(this, new Observer<List<SliderItem>>() {
            @Override
            public void onChanged(List<SliderItem> sliderItems) {
                if (sliderItems.isEmpty()){
                    flecha.setVisibility(View.VISIBLE);
                } else {
                    flecha.setVisibility(View.GONE);
                }
                Log.d(TAG, "onChanged: call");
                Log.d(TAG, "sliderItem: " + sliderItems.size());
                list = sliderItems;
                adapter.updateData(sliderItems);
            }
        });
    }

    private void addImage(Bitmap imageToAdd){
        main2ActivityVM.addImage(imageToAdd);
        viewPager2.setCurrentItem(list.size());
    }

    private void deleteImage(int position){
        Log.d(TAG, "deleteImage: en la posicion " + position);
        main2ActivityVM.deleteImage(position);
        int setCurrentItem = position-1;
        if (setCurrentItem < 0){
            setCurrentItem = 0;
        }
        viewPager2.setCurrentItem(setCurrentItem);
    }

    private void initSlider() {
        adapter = new SliderAdapter(list);
        adapter.setOnItemClickListener(ScanActivityTemplate.this);
        viewPager2 = findViewById(R.id.imagesViewPAger);
        viewPager2.setAdapter(adapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, FROMGALLERY);
    }

    private void openCamara(){
        ContentValues values = new ContentValues();
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takePicture, FROMPHOTO);//zero can be replaced with any action code (called requestCode)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: call. RequestCode: " + requestCode + " resultCode: " + resultCode );
        super.onActivityResult(requestCode, resultCode, data);



        switch (requestCode) {
            case FROMPHOTO:
                Log.d(TAG, "case FROMPHOTO");
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "result_ok");
                    startCropImageActivity(imageUri);
                }
                break;
            case FROMGALLERY:
                //si vuelve sin imagen
                if (data==null){
                    return;
                }
                Log.d(TAG, "case FROMGALLERY");
                startCropImageActivity(data.getData());
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                Log.d(TAG, "CROP_IMAGE_ACTIVITY: call ok");
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result == null){
                    break;
                }
                imageUri = result.getUri();
                addImageToList(imageUri);
                break;
        }
    }

    private void addImageToList(Uri uriSelected){
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriSelected);
            bitmap = ImagenManipulation.resize(bitmap, 2480,3508);
            listaIamgenes.add(bitmap);
            addImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "listaImagenes size " + listaIamgenes.size());
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(this);
    }

    @Override
    public void onItemClick(int position) {
//        deleteImage(position);
    }

    /**
     * Elimina cuando se termina la actividad
     */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: call");
        super.onDestroy();
        if (!isChangingConfigurations()){
            main2ActivityVM.vaciarLista();
            Tools.deleteAllFiles(this);
        }
    }

    public List<Bitmap> getListaIamgenes() {
        return listaIamgenes;
    }

    public void createAndPostDocument(DocumentViewModel dvm, File file){
        main2ActivityVM.postDocument(dvm, file);
        main2ActivityVM.getDocumentViewModelResponseMutableLiveData().observe(this, new Observer<DocumentViewModelResponse>() {
            @Override
            public void onChanged(DocumentViewModelResponse documentViewModelResponse) {
                Log.d(TAG, "onChanged: call");
                if (documentViewModelResponse.getStatusResponse() == StatusResponse.OK){
                    archivoSubidoListener();
                }
                //todo: crear cartel de error
//                errorDialog(documentViewModelResponse);
            }
        });
    }

    public void proressBar(){
        Log.d(TAG, "proressBar: call");
        if (progressBar.getVisibility() == View.INVISIBLE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        } else if (progressBar.getVisibility() == View.VISIBLE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public void cartelFinalizacion(){
        //todo: poner cartel de finalización
        FinalizacionDeTrabajoDialog finalizacionDeTrabajo = FinalizacionDeTrabajoDialog.newInstance();
        finalizacionDeTrabajo.setCancelable(false);
        finalizacionDeTrabajo.show(getSupportFragmentManager(), "finalizacion fialog");
    }

    /** Open Dialog Error Demo */
    private void errorDialog(DemoRepoResponse demoRepoResponse){
        DemoErrorDialog demoErrorDialog = new DemoErrorDialog(demoRepoResponse);
        demoErrorDialog.show(getSupportFragmentManager(), "Error en la demo");
    }

    @Override
    public void realizarOtroTrabajo(boolean siOno) {
        Log.d(TAG, "realizarOtroTrabajo: " + siOno);
        if (siOno){
           goBaseActivity();
        } else {
            closeApp();
        }
    }

    public void closeApp() {
        Intent intent = new Intent(this, IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }

    public void goBaseActivity(){
        Intent intent = new Intent(this, Select_App_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("EXIT", false);
        startActivity(intent);
        finish();
    }

    //todo cambiar a private
    private void changeTitle(){
        Log.d(TAG, "changeTitle call: " + getActivityTitle());
        titulo.setText(getActivityTitle());
    }
}
