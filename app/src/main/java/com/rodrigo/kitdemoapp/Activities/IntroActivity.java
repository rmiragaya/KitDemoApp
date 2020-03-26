package com.rodrigo.kitdemoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rodrigo.kitdemoapp.Dialogs.DemoErrorDialog;
import com.rodrigo.kitdemoapp.Dialogs.NewTokenDialog;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.DemoRepoResponse;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.StatusResponse;
import com.rodrigo.kitdemoapp.Utils.Tools;
import com.rodrigo.kitdemoapp.ViewModel.IntroVM;

public class IntroActivity extends AppCompatActivity implements NewTokenDialog.NewTokenDialogListener,
        DemoErrorDialog.DemoErrorListener {
    private static final String TAG = "IntroActivity";
    private static final int METODOS_COMPLETADOS = 2;

    private ImageView logo;
    private TextView poweredBy;
    private int metodos = 0;
    private IntroVM introVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        init();

        splashAnimation();

        getLinkIntent();

    }

    private void getLinkIntent() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
//        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            Log.d(TAG, "el intent llega con el token: " + appLinkData.getLastPathSegment());
            Toast.makeText(this, R.string.un_momento, Toast.LENGTH_SHORT).show();
            introVM.updateSavedToken(appLinkData.getLastPathSegment());
        }
        getToken();
    }

    /**
     * Views & VM
     */
    private void init() {
        //check if TLS is needed
        Tools.checkTls(this);
        //delete all files from files folder
        Tools.deleteAllFiles(this);

        // views (todo: should use dataBinding?)
        logo = findViewById(R.id.logoHp);
        poweredBy = findViewById(R.id.textoPoweredBySoluciones);
        // VM
        introVM = ViewModelProviders.of(this).get(IntroVM.class);
    }

    private void splashAnimation() {
        logo.setAlpha(0f);
        logo.setVisibility(View.VISIBLE);

        logo.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        textAnimation();
                    }
                });
    }

    private void textAnimation() {
        ObjectAnimator animaton = ObjectAnimator.ofFloat(poweredBy, "translationY", 50f);
        animaton.setDuration(1300);
        animaton.setInterpolator(new AccelerateDecelerateInterpolator());
        poweredBy.setAlpha(0f);
        poweredBy.setVisibility(View.VISIBLE);

        animaton.start();
        poweredBy.animate()
                .alpha(1f)
                .setDuration(1300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startNextActivity();
                            }
                        }, 1000);

                    }
                });
    }

    private void startNextActivity() {
        Log.d(TAG, "startNextActivity: call");
        metodos++;
        Log.d(TAG, "startNextActivity: metodos hechos " + metodos);
        if (metodos != METODOS_COMPLETADOS) {
            Log.d(TAG, "startNextActivity: return");
            return;
        }
        Log.d(TAG, "startNextActivity: inicia");

        Intent intent = new Intent(this, Select_App_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void getToken() {
        introVM.getTokenSavedResponseLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged S: " + s);
                if (s.isEmpty()) {
                    Log.d(TAG, "Token empty, open dialog to add token");
                    openDialogNewToken();
                } else {
                    Log.d(TAG, "Token valid, go Get Demo with " + s);
                    getDemo(s);
                }
            }
        });
    }

    private void getDemo(final String demo) {
        introVM.searchDemo(demo);
        introVM.getDemoFromToken().observe(this, new Observer<DemoRepoResponse>() {
            @Override
            public void onChanged(DemoRepoResponse demoRepoResponse) {

                if (demoRepoResponse.getStatusResponse() == StatusResponse.OK) {
                    Log.d(TAG, "OK");
                    //Save Token on SharePreference
                    Tools.saveDemoOnSharePreference(IntroActivity.this, demoRepoResponse.getDemo());
                    startNextActivity();
                    return;
                }

                if (demoRepoResponse.getStatusResponse() == StatusResponse.ERROR_CONEXION) {
                    Log.d(TAG, "CONEXION");
                    errorDialog(demoRepoResponse);
                    return;
                }

                Tools.saveTokenOnSharePreference(IntroActivity.this, "");
                Log.d(TAG, "Error");
                errorDialog(demoRepoResponse);
            }
        });
    }

    /**
     * Open Dialog new Token
     */
    private void openDialogNewToken() {
        NewTokenDialog newTokenDialog = NewTokenDialog.newInstance();
        newTokenDialog.setCancelable(false);
        newTokenDialog.show(getSupportFragmentManager(), "new Token");
    }

    /**
     * Open Dialog Error Demo
     */
    //todo crearlo bien
    private void errorDialog(DemoRepoResponse demoRepoResponse) {
        DemoErrorDialog demoErrorDialog = new DemoErrorDialog(demoRepoResponse);
        demoErrorDialog.show(getSupportFragmentManager(), "Error en la demo");
    }

    /**
     * New Token Dialog callback
     */
    @Override
    public void newTokenSaved(String newToken) {
        Log.d(TAG, "newTokenSaved: vuelve del dialog con el token:" + newToken);
        Log.d(TAG, "newToken.isEmpty(): " + newToken.isEmpty());
        Toast.makeText(this, R.string.un_momento, Toast.LENGTH_SHORT).show();
        introVM.updateSavedToken(newToken);
        getToken();
    }

    @Override
    public void cancel() {
        finish();
    }

    @Override
    public void addNewToken(Boolean addNewToken) {
        if (addNewToken) {
            openDialogNewToken();
        } else {
            finish();
        }
    }
}
