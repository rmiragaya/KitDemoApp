package com.rodrigo.kitdemoapp.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rodrigo.kitdemoapp.Adapter.PaisAdapter;
import com.rodrigo.kitdemoapp.Dialogs.BuscarIdDialog;
import com.rodrigo.kitdemoapp.Dialogs.DatePickerFragment;
import com.rodrigo.kitdemoapp.Models.ClientRepoResponse;
import com.rodrigo.kitdemoapp.Models.Cliente;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.MetadataClient;
import com.rodrigo.kitdemoapp.Models.PaisItem;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.StatusResponse;
import com.rodrigo.kitdemoapp.Utils.ImagenManipulation;
import com.rodrigo.kitdemoapp.Utils.Tools;
import com.rodrigo.kitdemoapp.Utils.ViewAnimation;
import com.rodrigo.kitdemoapp.ViewModel.AperturaCuentaVM;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AperturaCuentaMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
                                                                                BuscarIdDialog.BuscarIdDialogListener,
                                                                                    DatePickerDialog.OnDateSetListener {

    private static final String TAG = "AperturaCuentaMainActiv";

    private ArrayList<PaisItem> paisItemArrayList;
    private EditText razonSocial, mail;
    private RadioButton femenino, masculino, noEspecifica;
    private Spinner spinner;
    private boolean rotate = false;
    private View lyt_mic, lyt_call, back_drop;
    private FloatingActionButton fab_add;
    private View cargandoProgresBar;
    private Button calendar;
    private Date dateGuardada;
    private AperturaCuentaVM aperturaCuentaVM;
    private ConstraintLayout maxLayout;
    private String idClienteBuscado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apertura_cuenta_layout);

        //carga imagen y nombre de empresa
        Demo demo = Tools.getDemoFromSharePreference(this);
        String nombreEmpresa = demo.getClient();
        String logoEnString = demo.getLogo();


        if (nombreEmpresa!=null && !nombreEmpresa.isEmpty()){
            TextView nombreEmpresaTextView = findViewById(R.id.nombreEmpresa);
            nombreEmpresaTextView.setText(nombreEmpresa);
        }
        if (logoEnString!= null && !logoEnString.isEmpty()){
            Log.d(TAG, "logoenstring "+ logoEnString);
            ImageView logoImageView = findViewById(R.id.logo_cliente);
            Bitmap logo = ImagenManipulation.loadImage(logoEnString);
            if (logo != null){
                logoImageView.setImageBitmap(ImagenManipulation.resize(logo, 70, 70));
            }
        }

//        initToolbar();
        razonSocial = findViewById(R.id.editTextRazonSocialId);
        mail = findViewById(R.id.edittextMailId);
        femenino = findViewById(R.id.femeninoRadioBtn);
        masculino = findViewById(R.id.masculinoRadioBtn);
        noEspecifica = findViewById(R.id.noEspecificaRadioBtn);
        noEspecifica.setChecked(true);
        back_drop = findViewById(R.id.back_drop);
        cargandoProgresBar = findViewById(R.id.llProgressBar);
        calendar = findViewById(R.id.calendarBtn);
        maxLayout = findViewById(R.id.maxLayout);

        spinner = findViewById(R.id.spinnerPaisesId);
        fab_add = findViewById(R.id.fab_add);
        lyt_mic = findViewById(R.id.lyt_mic);
        lyt_call = findViewById(R.id.lyt_call);

        //vm
        aperturaCuentaVM = ViewModelProviders.of(this).get(AperturaCuentaVM.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initList();
        selectActualDate();

        PaisAdapter paisAdapter = new PaisAdapter(this, paisItemArrayList);
        spinner.setAdapter(paisAdapter);
        spinner.setOnItemSelectedListener(this);

        ViewAnimation.initShowOut(lyt_mic);
        ViewAnimation.initShowOut(lyt_call);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
            }
        });

        lyt_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click en el layout");
//                Toast.makeText(getApplicationContext(), "Buscar", Toast.LENGTH_SHORT).show();
                toggleFabMode(fab_add);
                openSearchIdDialog();
            }
        });

        lyt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click en el layout");
                toggleFabMode(fab_add);

                // comprueba si el los campos estan llenos y luego si el mail es valido
                if (camposLLenos()){
                    if (esMailValido(mail.getText().toString())){
                        guardarInputs();
                        startSerieDocuActivity();
                    }else {
                        Toast.makeText(AperturaCuentaMainActivity.this, "El mail no es válido", Toast.LENGTH_SHORT).show();
                        mail.setError("no válido");
                    }
                } else {
                    razonSocial.setError("Campo Obligatorio");
                    mail.setError("Campo Obligatorio");
                    Toast.makeText(AperturaCuentaMainActivity.this, "Llene todos los campos para continuar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragt = new DatePickerFragment();
                datePickerFragt.show(getSupportFragmentManager(), "date Picker");
            }
        });
    }

    private void toggleFabMode(View v) {
        removeSetErrors();
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_mic);
            ViewAnimation.showIn(lyt_call);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_mic);
            ViewAnimation.showOut(lyt_call);
            back_drop.setVisibility(View.GONE);
        }
    }

    private void removeSetErrors() {
        razonSocial.setError(null);
        mail.setError(null);
    }

    private void initList() {
        paisItemArrayList = new ArrayList<>();
        paisItemArrayList.add(new PaisItem("Argentina", R.drawable.bandera_argentina));
        paisItemArrayList.add(new PaisItem("Brasil", R.drawable.bandera_brasil));
        paisItemArrayList.add(new PaisItem("Chile", R.drawable.bandera_chile));
        paisItemArrayList.add(new PaisItem("Colombia", R.drawable.bandera_colombia));
        paisItemArrayList.add(new PaisItem("México", R.drawable.bandera_mexico));
        paisItemArrayList.add(new PaisItem("Perú", R.drawable.bandera_peru));
        paisItemArrayList.add(new PaisItem("Uruguay", R.drawable.bandera_uruguay));
        paisItemArrayList.add(new PaisItem("Venezuela", R.drawable.bandera_venezuela));
    }

    private void selectActualDate() {
        Calendar c = Calendar.getInstance();
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dateGuardada = c.getTime();
        calendar.setText(fecha);
    }

    private void openSearchIdDialog() {
        Log.d(TAG, "dialog CALL");
        BuscarIdDialog buscarIdDialog = new BuscarIdDialog();
        buscarIdDialog.show(getSupportFragmentManager(), "buscarId Dialog");
    }

    private boolean camposLLenos(){
        String mailParaVerificar =  mail.getText().toString();
        return (!razonSocial.getText().toString().isEmpty() || !mailParaVerificar.isEmpty());
    }

    private boolean esMailValido(CharSequence target){
        if (target == null) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void guardarInputs() {
        String razonSocialIngresad = razonSocial.getText().toString();
        String mailIngresado = mail.getText().toString();
        String sexoIngresado = getRadioBtnSelected();
        PaisItem paisSeleccionadoObj = (PaisItem) spinner.getSelectedItem();
        String paisSeleccionado = paisSeleccionadoObj.getmPaisNombre();
        Log.d(TAG, "dateGuardada.toString(): " + dateGuardada.toString());
        String fecha = Tools.convertDateStringInDateTime(dateGuardada.getTime());
        Log.d(TAG, "fecha: " + fecha);


        MetadataClient metadataCliente = new MetadataClient(razonSocialIngresad,mailIngresado,sexoIngresado, paisSeleccionado,null, null, null, fecha);

        // se guarda la Metadata
        aperturaCuentaVM.saveMetadata(metadataCliente);
        Demo demo = aperturaCuentaVM.getDemo();

        // si se buscó un id y fue encontrado, se lo pone como ClientName, sino se pone el nombre como Client Name
        if (!idClienteBuscado.isEmpty()){
            demo.setClientNameNew(idClienteBuscado);
        } else {
            demo.setClientNameNew(razonSocialIngresad);
        }

        // se guarda la Demo
        aperturaCuentaVM.saveDemo(demo);

    }

    private String getRadioBtnSelected() {
        if (femenino.isChecked()) {
            Log.d(TAG, "femenino");
            return femenino.getText().toString();
        } else if (masculino.isChecked()) {
            Log.d(TAG, "masculino");
            return masculino.getText().toString();
        } else return noEspecifica.getText().toString();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void startSerieDocuActivity(){
        Intent intent = new Intent(this, SeleccionSerieDocumentalActivity.class);
        startActivity(intent);
    }

    @Override
    public void buscarId(final String idCliente) {
        cargandoDialog();
        closeKeyboard();
        aperturaCuentaVM.searchCliente(idCliente);
        aperturaCuentaVM.getCliente().observe(this, new Observer<ClientRepoResponse>() {
            @Override
            public void onChanged(ClientRepoResponse clientRepoResponse) {
                if (clientRepoResponse.getStatusResponse() == StatusResponse.OK){
                    Log.d(TAG, "ok: call");
                    idClienteBuscado = idCliente;

                    Snackbar snackbar = Snackbar.make(maxLayout,"Ususario " + clientRepoResponse.getCliente().getBusinessName() + " encontrado", Snackbar.LENGTH_SHORT);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(AperturaCuentaMainActivity.this, R.color.colorPrimary));
                    FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                    params.gravity = Gravity.BOTTOM;
                    snackbar.show();
                    llenarCampos(clientRepoResponse.getCliente());
                } else {
                    setAllInputsEmpty();
                    idClienteBuscado = "";
                    Snackbar snackbar = Snackbar.make(maxLayout,"Ususario no encontrado", Snackbar.LENGTH_SHORT);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(AperturaCuentaMainActivity.this, R.color.colorPrimary));
                    FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                    params.gravity = Gravity.BOTTOM;
                    snackbar.show();
                    Log.d(TAG, "error call");
                }
                closeKeyboard();
                cargandoDialog();
            }
        });
    }

    public void setAllInputsEmpty(){
        razonSocial.setText("");
        mail.setText("");
        noEspecifica.setChecked(true);
        spinner.setSelection(0);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm !=null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void cargandoDialog() {
        if (cargandoProgresBar.getVisibility() == View.GONE) {
            cargandoProgresBar.setVisibility(View.VISIBLE);
        } else {
            cargandoProgresBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dateGuardada = c.getTime();
        calendar.setText(fecha);
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, Select_App_Activity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        finish();
//    }


    private void llenarCampos(Cliente demoCliente) {
        if (demoCliente.getBusinessName() != null) {
            razonSocial.setText(demoCliente.getBusinessName());
        }
        if (demoCliente.getEmail() != null) {
            mail.setText(demoCliente.getEmail());
        }
    }
}
