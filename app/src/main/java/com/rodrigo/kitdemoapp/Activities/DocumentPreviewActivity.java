package com.rodrigo.kitdemoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentPreviewActivity extends AppCompatActivity {
    private static final String TAG = "DocumentPreviewActivity";

    private List<Document> documentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_preview);

        Bundle bundle = getIntent().getExtras();
        documentsList = bundle.getParcelableArrayList("listaDocumentos");
        if (documentsList!= null){
            Collections.reverse(documentsList);
        }

        for (Document d :documentsList){
            Log.d(TAG, "" + d.toString());
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Select_App_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
