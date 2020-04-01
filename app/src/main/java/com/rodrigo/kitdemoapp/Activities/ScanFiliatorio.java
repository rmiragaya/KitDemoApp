package com.rodrigo.kitdemoapp.Activities;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.rodrigo.kitdemoapp.Dialogs.DocuFiliaDialog;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.DocumentVMandFile;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.MetadataClient;
import com.rodrigo.kitdemoapp.Utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Every Activity that have "scan" properties must extend from "ScanActivityTemplate"
 */

public class ScanFiliatorio extends ScanActivityTemplate implements DocuFiliaDialog.DocuFiliaDialogInterface{

    private static final String NUMDIALOGS = "numdialogs";
    private static final String NUMARCHIVOSSUBIDOS = "numarchivossubidos";
    private static final String TITULO = "titulo";
    private static final String TAG = "ScanFiliatorio";
    private int dialogNumero = 0;

    private List<String> listFilesPath = new ArrayList<>();
    private List<String> listFilesNames = new ArrayList<>();

    private int archivosSubidos;

    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            dialogNumero = savedInstanceState.getInt(NUMDIALOGS);
            archivosSubidos = savedInstanceState.getInt(NUMARCHIVOSSUBIDOS);
            title = savedInstanceState.getString(TITULO);
            Log.d(TAG, "restoreSearch: " + dialogNumero);
        }
        if (dialogNumero == 0){
            openDialogDigitalizacion(dialogNumero);
            title = "Dni";
        }
    }

    /**
     * When file converted
     * @param pathOfFileCreated gives file converted Path
     * return Boolean when needs to start the convert and uploads
     */
    @Override
    public Boolean imagesConvertedAndEmptyList(String pathOfFileCreated) {
        listFilesPath.add(pathOfFileCreated);
        Log.d(TAG, "Volvió con: " + pathOfFileCreated);
        dialogNumero ++;
        if (dialogNumero <=2){
            openDialogDigitalizacion(dialogNumero);
            if (dialogNumero==1){
                title ="Constancia";
            }
            if (dialogNumero==2){
                title ="Otra Documentación";
            }
            return false;
        } else {
            Log.d(TAG, "onImageConvert: enviar los archivos");
//            postAllDocuments();
            return true;
        }
    }

    /**
     * when file was upload
     */
    @Override
    public void archivoSubidoListener() {
        Log.d(TAG, "archivos Subidos: " +archivosSubidos);
        archivosSubidos++;
        if (archivosSubidos== 2){
            Log.d(TAG, "archivoSubidoListener: se subieron 3 archivos");
            proressBar();
            cartelFinalizacion();
        }
    }

    private void openDialogDigitalizacion(int numeroDeDialog){
        Log.d(TAG, "openDialogDigitalizacion: call con " + numeroDeDialog);
        DocuFiliaDialog dialog = DocuFiliaDialog.newInstance(numeroDeDialog);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "fragment " + numeroDeDialog);
    }

    @Override
    public void backFromDialog() {
        Log.d(TAG, "back from DocuFiliaDialog");
        //todo: esto debiera ser privado, no debiera llamarlo desde aca. "A Enzo no le gusta esto"
        changeTitle();
    }


    //todo: move this to ViewModel
    private List<DocumentVMandFile> postAllDocuments() {
        proressBar();
        listFilesNames.add("Dni");
        listFilesNames.add("Constancia");
        listFilesNames.add("Otra");

        Demo demo = Tools.getDemoFromSharePreference(this);
        MetadataClient metadataClient = Tools.getMetadataClientSharePreference(this);
        Log.d(TAG, "demo: " + demo.toString());
        Log.d(TAG, "metadataClient: " + metadataClient.toString());
        DocumentViewModel dvm = new DocumentViewModel(Integer.toString(demo.getId()),"Filiation", demo.getClientNameNew(),null, demo.getClient() ,metadataClient);
        dvm.setDemoId(Integer.toString(demo.getId()));

        //esto va?

        if (demo.getClientNameNew()!=null){
            dvm.setClient(demo.getClientNameNew());
        }

        Gson g = new Gson();
        List<DocumentVMandFile> documentVMandFileList = new ArrayList<>();
        for (int i = 0 ; i<listFilesPath.size() ; i++){
            dvm.getMetadataClient().setDocumentName(listFilesNames.get(i));
            File file = new File(listFilesPath.get(i));
            Log.d(TAG, "DocumentViewModel: " + g.toJson(dvm));
            Log.d(TAG, "mandando el " + i);
            documentVMandFileList.add(new DocumentVMandFile(dvm,file));
//            documentViewModels.add(dvm);
//            files.add(file);
        }
        return documentVMandFileList;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NUMDIALOGS, dialogNumero);
        outState.putInt(NUMARCHIVOSSUBIDOS,archivosSubidos);
        outState.putString(TITULO, title);
    }

    /**
     * return the name depending if it is Dni, Constancia or Otra
     * @param dialog: number dialogs shown
     */
    public String getDniConstanciaOtraName(int dialog){
        String fileName;
        switch (dialog){
            case 0:
                fileName = "dni";
                break;
            case 1:
                fileName = "constancia";
                break;
            case 2:
                fileName = "otra";
                break;
            default:
                fileName = "default";
                break;
        }
        return fileName;
    }

    /**
     *
     * @return the name File
     */
    @Override
    public String getFileName() {
        return getDniConstanciaOtraName(dialogNumero);
    }

    @Override
    public  List<DocumentVMandFile> getAllDocumentsAndFiles() {
        return postAllDocuments();
    }

    @Override
    public String getActivityTitle() {
        return title;
    }
}
