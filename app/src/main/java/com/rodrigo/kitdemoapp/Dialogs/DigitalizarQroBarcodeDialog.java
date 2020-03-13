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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.Utils.Constant;

public class DigitalizarQroBarcodeDialog extends AppCompatDialogFragment {
    private static final String TAG = "DigitalizarQroBarcodeDi";


    private DigitalizarQroBarcodeDialogListener listener;
    private String qrOrBarcode;
    private TextInputEditText editText;
    private TextInputLayout editTextLayout;
    private TextView subtitulo;
    private String editTextGuardado;
    private Button btnSiguiente;
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet;

    public DigitalizarQroBarcodeDialog() {
    }

    public static DigitalizarQroBarcodeDialog newInstance(String qrOrBarcode) {
        DigitalizarQroBarcodeDialog fragment = new DigitalizarQroBarcodeDialog();
        Bundle args = new Bundle();
        args.putString(Constant.QRORCORBARRA, qrOrBarcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qrOrBarcode = getArguments().getString(Constant.QRORCORBARRA);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.qr_or_barcode_dialog, null);
        builder.setView(view);

        constraintLayout = view.findViewById(R.id.dialogQrBarcodeConstraintId);
        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        subtitulo = view.findViewById(R.id.subtituloqrBarcode);
        subtitulo.setText("Ingrese ID del cliente");
        editTextLayout =view.findViewById(R.id.textimputlayoutQrBarcode);
        editText = view.findViewById(R.id.textEditDocuIdQrBarcode);

        btnSiguiente = view.findViewById(R.id.digitalizacionBtnIdQrBarcode);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() == 0){
                    editText.setError("Campo requerido");
                    return;
                }
                listener.onDigitalizarQroBarcodeDialog(editText.getText().toString());
                dismiss();
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
            listener = (DigitalizarQroBarcodeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarQroBarcodeDialogListener");
        }
    }

    public interface DigitalizarQroBarcodeDialogListener {
        void onDigitalizarQroBarcodeDialog(String idCliente);
    }
}
