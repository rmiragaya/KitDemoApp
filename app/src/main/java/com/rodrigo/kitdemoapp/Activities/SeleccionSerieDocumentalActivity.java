package com.rodrigo.kitdemoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.Utils.Constant;

public class SeleccionSerieDocumentalActivity extends AppCompatActivity {
    private static final String TAG = "SeleccionSerieDocumenta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_serie_documental);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
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
        Intent intent;
        if (i == 0){
            intent = new Intent(this, ScanFiliatorio.class);
        } else {
            intent = new Intent(this, ScanProducto.class);
        }

        startActivity(intent);
    }


}
