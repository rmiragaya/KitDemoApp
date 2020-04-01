package com.rodrigo.kitdemoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.rodrigo.kitdemoapp.Adapter.ViewPagerAdapter;
import com.rodrigo.kitdemoapp.Dialogs.DocuFiliaFragment2;
import com.rodrigo.kitdemoapp.Dialogs.PdfViewerFragment;
import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.Models.DocumentFileRepoResponse;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.DocumentViewModelResponse;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.StatusResponse;
import com.rodrigo.kitdemoapp.Utils.ImagenManipulation;
import com.rodrigo.kitdemoapp.ViewModel.DocumentPreviewVM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentPreviewActivity extends AppCompatActivity implements DocuFiliaFragment2.DocuFiliaFragmentListener,
                                                                            PdfViewerFragment.OnFragmentInteractionListener{
    private static final String TAG = "DocumentPreviewActivity";
    public static final String FILIATORIO = "Filiatorio";
    public static final String PRODUCTOS = "Productos";
    public static final String QR = "QR Y Barcode";
    public static final String RECORTEFIRMA = "Recorte de Firma";
    public static final String EJEMPLOS = "Ejemplos";
    private DocumentPreviewVM documentPreviewVM;
    private View coverView;
    private PdfViewerFragment fragment;

    private int tabSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_preview);

        documentPreviewVM = ViewModelProviders.of(this).get(DocumentPreviewVM.class);

        Bundle bundle = getIntent().getExtras();
        List<Document> documentsList = bundle.getParcelableArrayList("listaDocumentos");

        for (Document d :documentsList){
            Log.d(TAG, "" + d.toString());
        }

        TabLayout tabLayout = findViewById(R.id.tabDocumentsId);
        ViewPager viewPager = findViewById(R.id.document_view_pager);
        coverView = findViewById(R.id.coverView);

        TextView nombreEmpresaTextView = findViewById(R.id.nombreMarcaEmpresaPrev);
        nombreEmpresaTextView.setText(documentPreviewVM.getNombreEmpresa());

        ImageView logoImageView = findViewById(R.id.logoHPOEmpresaId);
        Bitmap logo = documentPreviewVM.getLogoEnBitmap();
        if (logo != null){
            logoImageView.setImageBitmap(ImagenManipulation.resize(logo, 70, 70));
        }

        ViewPagerAdapter viewPagerAdapter = new  ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // adding lists to tabs
        viewPagerAdapter.addFragment(DocuFiliaFragment2.newInstance(new ArrayList<>(documentsList), FILIATORIO), FILIATORIO);
        viewPagerAdapter.addFragment(DocuFiliaFragment2.newInstance(new ArrayList<>(documentsList), PRODUCTOS), PRODUCTOS);
        viewPagerAdapter.addFragment(DocuFiliaFragment2.newInstance(new ArrayList<>(documentsList), QR), QR);
        viewPagerAdapter.addFragment(DocuFiliaFragment2.newInstance(new ArrayList<>(documentsList),RECORTEFIRMA),  RECORTEFIRMA);
        viewPagerAdapter.addFragment(DocuFiliaFragment2.newInstance(new ArrayList<>(documentsList),EJEMPLOS),  EJEMPLOS);

        //adapter Setup
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: Tab " + tab.getText());
                Log.d(TAG, "onTabSelected: Position " +  tab.getPosition());
                tabSelected = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDocuClick(final Document documentSeleccionado) {
        coverView.setVisibility(View.VISIBLE);
        Log.d(TAG, "descargar y mostrar el : " + documentSeleccionado.getId());

        deleteAllFiles();
        //si el docu es ejemplo
        if (documentSeleccionado.getClient().equalsIgnoreCase("Carátula")){
            Log.d(TAG, "onDocuClick: descargar Ejemplo");
            getExamplesDocumentFiles(documentSeleccionado.getId());
            return;
        }

        documentPreviewVM.searchDocumentFile(documentSeleccionado.getId());
        documentPreviewVM.getFileDocument().observe(this, new Observer<DocumentFileRepoResponse>() {
            @Override
            public void onChanged(DocumentFileRepoResponse documentFileRepoResponse) {
                //todo: cerrar dialog
                Log.d(TAG, "onChanged: call");
                Log.d(TAG, "documentFileRepoResponse.getStatusResponse(): " + documentFileRepoResponse.getStatusResponse());
                Log.d(TAG, "documentFileRepoResponse.getDocumentFile().getAbsolutePath(): " + documentFileRepoResponse.getDocumentFile().getAbsolutePath());

                if (documentFileRepoResponse.getStatusResponse() == StatusResponse.OK){
                    Log.d(TAG, "Ok");
                    getDocumentViewModel(documentSeleccionado.getId(),documentFileRepoResponse.getDocumentFile().getAbsolutePath());




                    return;
                }
                //todo open error dialog
                //errorDialog();
            }
        });
    }

    private void getExamplesDocumentFiles(String id) {
        documentPreviewVM.searchDocumentExampleFile(id);
        documentPreviewVM.getDocumentExampleFileRepoResponseLiveData().observe(this, new Observer<DocumentFileRepoResponse>() {
            @Override
            public void onChanged(DocumentFileRepoResponse documentFileRepoResponse) {
                //todo: cerrar dialog
                Log.d(TAG, "onChanged: call");
                Log.d(TAG, "documentFileRepoResponse.getStatusResponse(): " + documentFileRepoResponse.getStatusResponse());
                Log.d(TAG, "documentFileRepoResponse.getDocumentFile().getAbsolutePath(): " + documentFileRepoResponse.getDocumentFile().getAbsolutePath());

                if (documentFileRepoResponse.getStatusResponse() == StatusResponse.OK){
                    Log.d(TAG, "Ok");
                    DocumentViewModel documentViewModel = new DocumentViewModel(null, null, null, null, null, null);
                    openPdfVewFragment(documentFileRepoResponse.getDocumentFile().getAbsolutePath(), documentViewModel);
                }
                //todo open error dialog
//                errorDialog();
            }
        });
    }

    private void getDocumentViewModel(String docuId, final String pdfFileDir){
        documentPreviewVM.searchDocumentViewModel(docuId);
        documentPreviewVM.getDocumentViewModel().observe(this, new Observer<DocumentViewModelResponse>() {
            @Override
            public void onChanged(DocumentViewModelResponse documentViewModelResponse) {

                if (documentViewModelResponse.getStatusResponse() == StatusResponse.OK){
                    Log.d(TAG, "Ok");
                    openPdfVewFragment(pdfFileDir, documentViewModelResponse.getDocumentViewModel());


                }
            }
        });
    }

    private void openPdfVewFragment(String pdfFile, DocumentViewModel documentViewModel){
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_pdfv_container);

        fragment = PdfViewerFragment.newInstance(pdfFile, documentViewModel);

        if (currentFragment!= null){
            if (currentFragment.getClass().equals(fragment.getClass())) {
                Log.d(TAG, "dobleClick Prevented");
                return;
            }
        }


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_pdfv_container, fragment, "PDF_FRAGMENT").commit();


    }

    @Override
    public void fragmentFinish() {
        coverView.setVisibility(View.INVISIBLE);
    }


    /**
     * if fragment is visible, close it
     * if fragment not visible, go to prev activity (and delete all files)
     */
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0){
            deleteAllFiles();
            Intent intent = new Intent(this, Select_App_Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    /**
     * Borra todos los archivos de la carpeta Files
     */
    private void deleteAllFiles(){
        File folder = new File(this.getExternalFilesDir(null).getAbsolutePath());
        File[] children = folder.listFiles();
        Log.d(TAG, "children: " + children.length);
        Log.d(TAG, "folder.getAbsolutePath(): " + folder.getAbsolutePath());
        deleteRecursive(children);
    }

    private void deleteRecursive(File[] filesToDelete){
        for (File file : filesToDelete){
            Log.d(TAG, "file: " + file.getName());
            file.delete();
        }
    }
}
