package com.rodrigo.kitdemoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.MetadataClient;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.Utils.Tools;

public class SeleccionSerieDocumentalActivity extends AppCompatActivity {
    private static final String TAG = "SeleccionSerieDocumenta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_serie_documental);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        // for testing
//        Demo demoTest = Tools.getDemoFromSharePreference(this);
//        Log.d(TAG, "Demo test: " + demoTest.toString());
//        Log.d(TAG, "*******************************************");
//        MetadataClient metadataClient = Tools.getMetadataClientSharePreference(this);
//        Log.d(TAG, "metadataClient test: " + metadataClient.toString());
        // for testing
        init();
    }

    private void init() {
        CardView docuFiliatorios = findViewById(R.id.docuFiliatoriosId);
        docuFiliatorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SeleccionSerieDocumentalActivity.this, "Documentos Filiatorios", Toast.LENGTH_SHORT).show();
                startDocuOProductActivity(0);
            }
        });

        CardView productos = findViewById(R.id.productosId);
        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SeleccionSerieDocumentalActivity.this, "Producto", Toast.LENGTH_SHORT).show();
                startDocuOProductActivity(1);
            }

        });
    }

    private void startDocuOProductActivity(int i) {
        // todo: abrir proxima activity con docu o product
    }


}
