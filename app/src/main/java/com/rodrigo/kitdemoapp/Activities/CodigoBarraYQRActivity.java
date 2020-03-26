package com.rodrigo.kitdemoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.Utils.Constant;
import com.rodrigo.kitdemoapp.Utils.ImagenManipulation;
import com.rodrigo.kitdemoapp.ViewModel.CodigoBarrayqrVM;

public class CodigoBarraYQRActivity extends AppCompatActivity {

    private CardView qrCardView, barCodeCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_barra_yqr);
        init();
    }

    private void init() {

        CodigoBarrayqrVM codigoBarrayqrVM = ViewModelProviders.of(this).get(CodigoBarrayqrVM.class);

        TextView nombreEmpresaTextView = findViewById(R.id.nombreMarcaEmpresaPrev);
        nombreEmpresaTextView.setText(codigoBarrayqrVM.getNombreEmpresa());

        ImageView logoImageView = findViewById(R.id.logoHPOEmpresaId);
        Bitmap logo = codigoBarrayqrVM.getLogoEnBitmap();
        if (logo != null){
            logoImageView.setImageBitmap(ImagenManipulation.resize(logo, 70, 70));
        }



        qrCardView = findViewById(R.id.qrCardViewId);
        qrCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQRAndCodBarra(Constant.QR);
            }
        });

        barCodeCardView = findViewById(R.id.cardCodeCardId);
        barCodeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQRAndCodBarra(Constant.CODBARRA);
            }
        });

        onCreateAnimation();
    }

    private void startQRAndCodBarra(String qrOrBarcode) {
        Intent intent = new Intent(this, ScanQrAndBarCodeActivity.class);
        intent.putExtra(Constant.QRORCORBARRA, qrOrBarcode);
        startActivity(intent);
    }

    private void onCreateAnimation(){
        qrCardView.setAlpha(0f);
        qrCardView.setVisibility(View.VISIBLE);

        qrCardView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        barCodeCardView.setAlpha(0f);
                        barCodeCardView.setVisibility(View.VISIBLE);
                        barCodeCardView.animate()
                                .alpha(1f)
                                .setDuration(1000);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Select_App_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}
