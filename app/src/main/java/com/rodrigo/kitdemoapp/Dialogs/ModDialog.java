package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.rodrigo.kitdemoapp.R;

public class ModDialog extends AppCompatDialogFragment {
    private static final String TAG = "ModDialog";

    private DigitalizarModificacionDialogListener listener;
    private TextInputEditText nombreDocumento;

    public ModDialog() {
    }

    public static ModDialog newInstance() {
        return new ModDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.modificacion_producto_dialog, null);
        builder.setView(view);

        nombreDocumento = view.findViewById(R.id.modificacionNombredocumentoEdittext);

        Button btn = view.findViewById(R.id.digitalizacionBtnId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()){
                    listener.onDigitalizacionModificacionDialogRespons(nombreDocumento.getText().toString().toLowerCase().trim());
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

    private boolean validarCampos(){
        if (nombreDocumento.getText().toString().trim().length() == 0){
            nombreDocumento.setError("Campo obligatorio");
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DigitalizarModificacionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarModificacionDialogListener");
        }
    }

    public interface DigitalizarModificacionDialogListener {
        void onDigitalizacionModificacionDialogRespons(String documentName);
        void closeAndOpenProductDialog();
    }

}
