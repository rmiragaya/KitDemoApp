package com.rodrigo.kitdemoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.rodrigo.kitdemoapp.Dialogs.AltaBajaModificacionFragment;
import com.rodrigo.kitdemoapp.Dialogs.AltaDialog;
import com.rodrigo.kitdemoapp.Dialogs.BajaDialog;
import com.rodrigo.kitdemoapp.Dialogs.ModDialog;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.DocumentVMandFile;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.MetadataClient;
import com.rodrigo.kitdemoapp.Utils.Constant;
import com.rodrigo.kitdemoapp.Utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanProducto extends ScanActivityTemplate implements AltaBajaModificacionFragment.OnFragmentInteractionListener,
        BajaDialog.DigitalizarBajaDialogListener,
AltaDialog.DigitalizarAltaProductoDialogListener,
ModDialog.DigitalizarModificacionDialogListener{
    private static final String TAG = "ScanProducto";
    private ConstraintLayout constraintOfFragment;
    //todo que se guarden al girar
    private String serieName, reasonLow, codeHigh, documentName;
    private String title = "Producto";
    private List<String> listFilesPath = new ArrayList<>();
    private AltaBajaModificacionFragment fragmentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openProductFragment();
        title = "Producto";
    }

    /**
     * Open AltaBajaModificacionFragment fragment
     */
    private void openProductFragment() {
        Log.d(TAG, "openProductFragment: call");
        fragmentDialog = AltaBajaModificacionFragment.newInstance();
        fragmentDialog.setCancelable(true);
        fragmentDialog.show(getSupportFragmentManager(), "Alta baja o modificacion");

    }

    @Override
    public void onAltaBajaOModSelect(String fragmentToOpen) {
        //Todo: crear Fragment para llenar datos de AltaBajaoMod y reemplazar el anterior por este nuevo
        Log.d(TAG, "onAltaBajaOModSelect: abrir " + fragmentToOpen + " DialogFragment");
        switch (fragmentToOpen){
            case Constant.ALTA:
                title = "Alta";
                openAltaDialog();
                break;
            case Constant.BAJA:
                title = "Baja";
                openBajaDialog();
                break;
            case Constant.MODIFICACION:
                title = "Modificación";
                openModDialog();
                break;
                default:
                    break;
        }
    }

    @Override
    public void close() {
        Log.d(TAG, "close: call");
        //todo si cambio el backpress poner finish?
        finish();
    }

    private void openAltaDialog() {
        AltaDialog bajaDialog = AltaDialog.newInstance();
//        bajaDialog.setCancelable(false);
        bajaDialog.show(getSupportFragmentManager(), "Alta Dialog");
    }

    private void openBajaDialog() {

        BajaDialog altaDialog = BajaDialog.newInstance();
//        altaDialog.setCancelable(false);
        altaDialog.show(getSupportFragmentManager(), "Baja Dialog");
    }

    private void openModDialog() {
        ModDialog modDialog = ModDialog.newInstance();
//        modDialog.setCancelable(false);
        modDialog.show(getSupportFragmentManager(), "Mod Dialog");
    }

    /**
     * When file converted
     * @param pathOfFileCreated gives file converted Path
     */
    @Override
    public Boolean imagesConvertedAndEmptyList(String pathOfFileCreated) {
        listFilesPath.add(pathOfFileCreated);
        Log.d(TAG, "Volvió con: " + pathOfFileCreated);
        return true;
    }

    @Override
    public String getFileName() {
        return this.documentName;
    }

    @Override
    public List<DocumentVMandFile> getAllDocumentsAndFiles() {
        return postAllDocuments();
    }

    @Override
    public String getActivityTitle() {
        return title;
    }


    /**
     * when file was upload
     */
    @Override
    public void archivoSubidoListener() {
        proressBar();
        cartelFinalizacion();
    }

    @Override
    public void onDigitalizacionBajaDialogRespons(String documentName, String razonBaja, String otros) {
        Log.d(TAG, "documentNAme: " + documentName);
        Log.d(TAG, "razonBaja: " + razonBaja);
        Log.d(TAG, "otros: " + otros);

        this.serieName = "Low";
        reasonLow = razonBaja;
        if (!otros.isEmpty()){
            reasonLow += " " + otros;
        }
        this.documentName = documentName;
    }

    @Override
    public void closeAndOpenProductDialog() {
        Log.d(TAG, "closeAndOpenProductDialog: call");
        openProductFragment();
    }

    private List<DocumentVMandFile> postAllDocuments() {
        proressBar();

        Demo demo = Tools.getDemoFromSharePreference(this);
        MetadataClient metadataClient = Tools.getMetadataClientSharePreference(this);
        Log.d(TAG, "demo: " + demo.toString());
        Log.d(TAG, "metadataClient: " + metadataClient.toString());

        DocumentViewModel dvm = new DocumentViewModel(Integer.toString(demo.getId()),serieName, demo.getClientNameNew(),null, demo.getClient() ,metadataClient);
        dvm.setDemoId(Integer.toString(demo.getId()));
        dvm.getMetadataClient().setDocumentName(documentName);

        switch (serieName) {
            case "Low":
                dvm.getMetadataClient().setReason(reasonLow);
                break;
            case "High":
                dvm.getMetadataClient().setCode(codeHigh);
                break;
        }

        if (demo.getClientNameNew()!=null){
            dvm.setClient(demo.getClientNameNew());
        }

        File file = new File(listFilesPath.get(0));
        Log.d(TAG, "filepath: " + listFilesPath.get(0));

        List<DocumentVMandFile> documentVMandFileList = new ArrayList<>();
        documentVMandFileList.add(new DocumentVMandFile(dvm, file));
        return documentVMandFileList;
    }

    @Override
    public void onDigitalizacionAltaDialogRespons(String codigoAlta, String documentName) {
        Log.d(TAG, "codigoAlta: " + codigoAlta);
        Log.d(TAG, "documentName: " + documentName);
        this.serieName = "High";
        codeHigh = codigoAlta;
        this.documentName = documentName;
    }

    @Override
    public void onDigitalizacionModificacionDialogRespons(String documentName) {
        this.serieName = "Modification";
        this.documentName = documentName;
    }



}
