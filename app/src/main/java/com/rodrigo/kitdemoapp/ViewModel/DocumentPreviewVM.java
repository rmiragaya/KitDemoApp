package com.rodrigo.kitdemoapp.ViewModel;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rodrigo.kitdemoapp.Models.DocumentFileRepoResponse;
import com.rodrigo.kitdemoapp.Models.DocumentViewModelResponse;
import com.rodrigo.kitdemoapp.Repo.DocumentRepo;
import com.rodrigo.kitdemoapp.Utils.ImagenManipulation;
import com.rodrigo.kitdemoapp.Utils.Tools;

import java.io.File;

public class DocumentPreviewVM extends AndroidViewModel{
    private static final String TAG = "DocumentPreviewVM";

    private String nombreEmpresa;
    private String logoEnBitmap;
    private LiveData<DocumentFileRepoResponse> documentFileRepoResponseLiveData;
    private LiveData<DocumentFileRepoResponse> documentExampleFileRepoResponseLiveData;
    private LiveData<DocumentViewModelResponse> documentViewModelResponseLiveData;
    private DocumentRepo documentRepo;
    private String token;
    private String externalFilesDir;

    public DocumentPreviewVM(@NonNull Application application) {
        super(application);
        this.externalFilesDir = application.getExternalFilesDir(null).getAbsolutePath();
        this.documentRepo = new DocumentRepo();
        this.token = Tools.getTokenFromPreference(application);
        nombreEmpresa = Tools.getDemoFromSharePreference(application).getClient();
        logoEnBitmap = Tools.getDemoFromSharePreference(application).getLogo();
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public Bitmap getLogoEnBitmap() {
        return ImagenManipulation.loadImage(logoEnBitmap);
    }

    public void searchDocumentFile(String fileId){
        String externalFileId = externalFilesDir  +"/"+ fileId + ".pdf";
        Log.d(TAG, "externalFileId: " + externalFileId);
        documentFileRepoResponseLiveData =  documentRepo.getDocumentsFile(token, fileId, externalFileId);
    }

    public LiveData<DocumentFileRepoResponse> getFileDocument(){
        return documentFileRepoResponseLiveData;
    }

    public void searchDocumentViewModel(String docuId){
        documentViewModelResponseLiveData = documentRepo.getDocumentsViewModel(token, docuId);
    }

    public LiveData<DocumentViewModelResponse> getDocumentViewModel(){
        return documentViewModelResponseLiveData;
    }

    public void searchDocumentExampleFile(String docuId){
        String externalFileId = externalFilesDir  +"/"+ docuId + "example.pdf";
        documentExampleFileRepoResponseLiveData = documentRepo.getExampleFile(token,docuId, externalFileId);
    }

    public LiveData<DocumentFileRepoResponse> getDocumentExampleFileRepoResponseLiveData(){
        return documentExampleFileRepoResponseLiveData;
    }
}


























