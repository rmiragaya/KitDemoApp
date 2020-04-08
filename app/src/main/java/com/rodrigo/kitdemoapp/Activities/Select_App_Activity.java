package com.rodrigo.kitdemoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.rodrigo.kitdemoapp.Dialogs.BottomSheetFragment;
import com.rodrigo.kitdemoapp.Dialogs.DeleteTokenDialog;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.Models.DocumentsRepoResponse;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.Utils.StatusResponse;
import com.rodrigo.kitdemoapp.Utils.Tools;
import com.rodrigo.kitdemoapp.ViewModel.SelectAppVM;

import java.util.ArrayList;

public class Select_App_Activity extends AppCompatActivity implements BottomSheetFragment.BottomSheetListener, DeleteTokenDialog.DeleteTokenListener {
    private static final String TAG = "Select_App_Activity";

    private static final int MAX_STEP = 4;
    private ViewPager viewPager;
    private View cargandoProgresBar;
    private SelectAppVM selectAppVM;
    private long mLastClickTime = 0;
    private FloatingActionButton fab_add;


    private String[] about_title_array = {
            "Apertura de Cuenta",
            "Clasificación de Documentos",
            "Captura de Firma",
            "Visualización de Documentos"
    };

    private String[] about_description_array = {
            "Cargue la documentación de sus clientes de forma rápida y sencilla.",
            "Direccione sus documentos de forma dinámica mediante la lectura de códigos de Barra/QR.",
            "Recorte la firma de sus clientes plasmadas en formularios de manera dinámica.",
            "Accede a la cuenta, visualiza y comparte la documentación previamente ingresada"
    };

    private String[] about_images_array = {
            "abrir_cuenta.json",
            "qr.json",
            "firma.json",
            "print.json"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_app_layout);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            Log.d(TAG, "onCreate: exit es True");
            finish();
        }

        /* For Testing */
        Demo demo =  Tools.getDemoFromSharePreference(this);
        Log.d(TAG, "demo en la nueva Activity: " + demo.toString());
        /* For Testing */

        fab_add = findViewById(R.id.fabModalSheet);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });

        cargandoProgresBar = findViewById(R.id.selectAppProgressDialog);
        viewPager = findViewById(R.id.view_pager);

        // adding bottom dots
        bottomProgressDots(0);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
//        viewPager.setCurrentItem(Tools.getViewPagerPosition(this));

        selectAppVM = ViewModelProviders.of(this).get(SelectAppVM.class);


    }



    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @Override
    public void onBorrarTokenButtonPress() {
        Log.d(TAG, "onBorrarTokenButtonPress: call, now open a dialog to delete Token and Reset App");
        DeleteTokenDialog deleteTokenDialog = DeleteTokenDialog.newInstance();
        deleteTokenDialog.show(getSupportFragmentManager(), "DeleteToken Dialog");
    }

    @Override
    public void deleteToken() {
        Log.d(TAG, "deleteToken: BORRAR EL TOKEN Y REINICIAR");
        Tools.saveTokenOnSharePreference(this, "");
        ProcessPhoenix.triggerRebirth(this);
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {

        private MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_card_wizard_light, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(about_title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(about_description_array[position]);
            ((LottieAnimationView) view.findViewById(R.id.image)).setAnimation(about_images_array[position]);

            final Button btnNext = view.findViewById(R.id.btn_next);

            btnNext.setText(getResources().getString(R.string.abrir));

            //cada slide abre cada app!!!
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "1er click call");

                    //previene DClicks
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                        Log.d(TAG, "2do click");
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    cargandoDialog();

                    int current = viewPager.getCurrentItem();

                    Intent intent;
                    switch (current) {
                        case 0:
                            intent = new Intent(v.getContext(), AperturaCuentaMainActivity.class);
                            startActivity(intent);
//                            finish();
                            break;
                        case 1:
                            intent = new Intent(v.getContext(), CodigoBarraYQRActivity.class);
                            startActivity(intent);
//                            finish();
                            break;
                        case 2:
                            intent = new Intent(v.getContext(), ScanRecorteFirma.class);
                            startActivity(intent);
//                            finish();
                            break;
                        case 3:
                            //carga documentos para luego mandarlos a Preview Activity
                            getListaDocumentos();
                            break;
                    }
                    cargandoDialog();
                }

            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return about_title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void getListaDocumentos() {
        selectAppVM.init();
        selectAppVM.getClienteResponseLiveData().observe(this, new Observer<DocumentsRepoResponse>() {
            @Override
            public void onChanged(DocumentsRepoResponse documentsRepoResponse) {

                if (documentsRepoResponse.getStatusResponse() == StatusResponse.OK){
                    Log.d(TAG, "OK");
                    startNextActivity(documentsRepoResponse);
                    return;
                }

                Log.d(TAG, "ERROR");
                Toast.makeText(Select_App_Activity.this, getBaseContext().getResources().getString(R.string.error_conexion), Toast.LENGTH_SHORT).show();
                cargandoDialog();
            }
        });
    }

    private void startNextActivity(DocumentsRepoResponse documentsRepoResponse) {
        //todo llamar proxima activity con el document response en bundle
        Log.d(TAG, "startNextActivity: ok");
        for (Document d : documentsRepoResponse.getDocumentList()){
            Log.d(TAG, "" + d.toString());
        }


        Intent intent = new Intent(this, DocumentPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listaDocumentos", (ArrayList<? extends Parcelable>) documentsRepoResponse.getDocumentList());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    private void cargandoDialog() {
        Log.d(TAG, "cargandoDialog: call");
        if (cargandoProgresBar.getVisibility() == View.GONE) {
            cargandoProgresBar.setVisibility(View.VISIBLE);
        } else {
            cargandoProgresBar.setVisibility(View.GONE);
        }
    }


    private void openBottomSheet() {
        Log.d(TAG, "openBottomSheet: call");
        BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance();
        bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheet");
    }
}
