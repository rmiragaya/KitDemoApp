package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

        //todo bind views

        editText = view.findViewById(R.id.editTextRecorteFirmaDialog);
        subtitulo = view.findViewById(R.id.subtituloRecorteFirma);
        subtitulo.setText(R.string.ingrese_id);

        //todo set Buttons y OnclickListener + listener

        btnSiguiente = view.findViewById(R.id.btnSiguienteRecorteFirmaDialog);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
}
