package com.rodrigo.kitdemoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.rodrigo.kitdemoapp.Dialogs.RecorteFirmaDialog;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.DocumentVMandFile;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.MetadataClient;
import com.rodrigo.kitdemoapp.Utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanRecorteFirma extends ScanActivityTemplate implements RecorteFirmaDialog.RecorteFirmaDialogListener {
    private static final String TAG = "ScanRecorteFirma";

    private String title = "Recorte De Firma";
    private String idCliente;
    private List<String> listFilesPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openRecorteFirmaDialog();
    }

    private void openRecorteFirmaDialog() {
        RecorteFirmaDialog dialog = RecorteFirmaDialog.newInstance();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Recorte Firma Dialog");
    }

    @Override
    public void archivoSubidoListener() {
        proressBar();
        cartelFinalizacion();
    }

    @Override
    public Boolean imagesConvertedAndEmptyList(String pathOfFileCreated) {
        listFilesPath.add(pathOfFileCreated);
        Log.d(TAG, "Volvió del dialog con: " + pathOfFileCreated);
        return true;
    }

    @Override
    public String getFileName() {
        return idCliente;
    }

    @Override
    public List<DocumentVMandFile> getAllDocumentsAndFiles() {
        return postAllDocuments();
    }

    private List<DocumentVMandFile> postAllDocuments() {
        proressBar();

        Demo demo = Tools.getDemoFromSharePreference(this);
        MetadataClient metadataClient = new MetadataClient();
        Log.d(TAG, "demo: " + demo.toString());
        Log.d(TAG, "metadataClient: " + metadataClient.toString());
        String serieName = "Signature";

        DocumentViewModel dvm = new DocumentViewModel(Integer.toString(demo.getId()),serieName, demo.getClientNameNew(),null, idCliente ,metadataClient);
        dvm.setDemoId(Integer.toString(demo.getId()));
        dvm.getMetadataClient().setDocumentName("firma");

        File file = new File(listFilesPath.get(0));

        List<DocumentVMandFile> documentVMandFileList = new ArrayList<>();
        documentVMandFileList.add(new DocumentVMandFile(dvm, file));
        return documentVMandFileList;
    }

    @Override
    public String getActivityTitle() {
        return title;
    }

    @Override
    public void onDigitalizarRecorteFirmaDialog(String idClienteIngresado) {
        idCliente = idClienteIngresado;
    }


}
