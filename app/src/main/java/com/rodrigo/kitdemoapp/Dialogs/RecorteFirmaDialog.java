package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rodrigo.kitdemoapp.R;

public class RecorteFirmaDialog extends AppCompatDialogFragment {
    private static final String TAG = "RecorteFirmaDialog";

    private RecorteFirmaDialogListener listener;
    private Button btnSiguiente;
    private TextInputEditText editText;
    private TextView subtitulo;


    public RecorteFirmaDialog() {
    }

    public static RecorteFirmaDialog newInstance() {
        return new RecorteFirmaDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.recorte_firma_dialog, null);
        builder.setView(view);

        editText = view.findViewById(R.id.editTextRecorteFirmaDialog);
        subtitulo = view.findViewById(R.id.subtituloRecorteFirma);
        subtitulo.setText(R.string.ingrese_id);

        btnSiguiente = view.findViewById(R.id.btnSiguienteRecorteFirmaDialog);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().length() == 0){
                    editText.setError("Campo requerido");
                    return;
                }
                dismiss();
                listener.onDigitalizarRecorteFirmaDialog(editText.getText().toString());
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
                    listener.close();
                }
            });
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RecorteFirmaDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarQroBarcodeDialogListener");
        }
    }


    public interface RecorteFirmaDialogListener {
        void onDigitalizarRecorteFirmaDialog(String idCliente);
        void close();
    }
}
