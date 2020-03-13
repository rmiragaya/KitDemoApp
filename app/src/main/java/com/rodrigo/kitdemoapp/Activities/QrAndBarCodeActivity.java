package com.rodrigo.kitdemoapp.Activities;

import android.os.Bundle;
import android.util.Log;

import com.rodrigo.kitdemoapp.Dialogs.DigitalizarQroBarcodeDialog;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.DocumentVMandFile;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.MetadataClient;
import com.rodrigo.kitdemoapp.Utils.Constant;
import com.rodrigo.kitdemoapp.Utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QrAndBarCodeActivity extends ScanActivityTemplate implements DigitalizarQroBarcodeDialog.DigitalizarQroBarcodeDialogListener {

    private static final String TAG = "QrAndBarCodeActivity";
    private String filePath, fileName, idCliente;
    private String titulo;
    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tipo = getIntent().getStringExtra(Constant.QRORCORBARRA);
        if (tipo!=null){
            switch (tipo){
                case Constant.QR:
                    titulo = "QR";
                    break;
                case Constant.CODBARRA:
                    titulo = "Codigo de Barras";
                    break;
                    default:
                        break;
            }
        }

        openDialogs();
    }

    private void openDialogs() {
        DigitalizarQroBarcodeDialog dialog = DigitalizarQroBarcodeDialog.newInstance(tipo);
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(), "digitalizar Qr o Barcode");
    }




    @Override
    public Boolean imagesConvertedAndEmptyList(String pathOfFileCreated) {
        filePath = pathOfFileCreated;
        fileName = pathOfFileCreated;
        return true;
    }

    @Override
    public void archivoSubidoListener() {
        proressBar();
        cartelFinalizacion();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public List<DocumentVMandFile> getAllDocumentsAndFiles() {
        return postAllDocuments();
    }



    @Override
    public String getActivityTitle() {
        return titulo;
    }

    @Override
    public void onDigitalizarQroBarcodeDialog(String idCliente) {
        Log.d(TAG, "onDigitalizarQroBarcodeDialog: idCliente " + idCliente);
        this.idCliente = idCliente;
    }


    private List<DocumentVMandFile> postAllDocuments() {
        proressBar();

        Demo demo = Tools.getDemoFromSharePreference(this);
        MetadataClient metadataClient = Tools.getMetadataClientSharePreference(this);
        Log.d(TAG, "demo: " + demo.toString());
        Log.d(TAG, "metadataClient: " + metadataClient.toString());

        DocumentViewModel dvm = new DocumentViewModel(Integer.toString(demo.getId()),tipo, demo.getClientNameNew(),null, demo.getClient() ,metadataClient);
        dvm.setDemoId(Integer.toString(demo.getId()));

        String newDocumentName = fileName.split("-001")[0];
        Log.d(TAG, "newDocumentName: " + newDocumentName);
        dvm.getMetadataClient().setDocumentName(newDocumentName);


        File file = new File(fileName);

        List<DocumentVMandFile> documentVMandFileList = new ArrayList<>();
        documentVMandFileList.add(new DocumentVMandFile(dvm, file));
        return documentVMandFileList;
    }
}
