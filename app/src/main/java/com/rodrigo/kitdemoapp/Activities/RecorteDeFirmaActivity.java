package com.rodrigo.kitdemoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.rodrigo.kitdemoapp.R;

public class RecorteDeFirmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorte_de_firma);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Select_App_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }


}
