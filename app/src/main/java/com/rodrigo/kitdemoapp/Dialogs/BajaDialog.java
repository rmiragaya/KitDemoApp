package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rodrigo.kitdemoapp.R;

public class BajaDialog extends AppCompatDialogFragment {
    private static final String TAG = "BajaDialog";

    private DigitalizarBajaDialogListener listener;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextInputLayout editTextLayout;
    private TextInputEditText editText, nombreDocumento;

    public BajaDialog() {
    }

    public static BajaDialog newInstance() {
        return new BajaDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.baja_producto_baja_dialog, null);
        builder.setView(view);

        editText = view.findViewById(R.id.editTextBajaOtro);
        nombreDocumento = view.findViewById(R.id.bajaDocumentNameEditText);
        editTextLayout = view.findViewById(R.id.textInputBajaLayout);
        radioGroup = view.findViewById(R.id.radioGroupRazonBaja);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = view.findViewById(checkedId);
                if (checkedId== R.id.opcion3){
                    editTextLayout.setVisibility(View.VISIBLE);

                } else {
                    editTextLayout.setVisibility(View.GONE);
                }
            }
        });

        radioGroup.check(R.id.opcion1);
        radioButton = view.findViewById(R.id.opcion1);

        Button btn = view.findViewById(R.id.digitalizacionBtnId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()){
                    if (editTextLayout.getVisibility() == View.VISIBLE){
                        Log.d(TAG, "Layout Visible, envia el edittext: " + editText.getText().toString());
                        listener.onDigitalizacionBajaDialogRespons (nombreDocumento.getText().toString().toLowerCase().trim(),radioButton.getText().toString().trim() ,editText.getText().toString());
                    } else {
                        listener.onDigitalizacionBajaDialogRespons (nombreDocumento.getText().toString().toLowerCase().trim(),radioButton.getText().toString().trim() ,"");
                    }
                    dismiss();
                }
            }
        });


        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.d(TAG, "onCancel: call");
                    listener.closeAndOpenProductDialog();
                }
            });
        }
    }



    public interface DigitalizarBajaDialogListener {
        void onDigitalizacionBajaDialogRespons(String documentNAme, String razonBaja, String otros);
        void closeAndOpenProductDialog();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DigitalizarBajaDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarBajaProductoDialog");
        }
    }



    private boolean validarCampos(){
        if (nombreDocumento.getText().toString().trim().length() == 0){
            nombreDocumento.setError("Campo obligatorio");
            return false;
        }
        return true;
    }
}
