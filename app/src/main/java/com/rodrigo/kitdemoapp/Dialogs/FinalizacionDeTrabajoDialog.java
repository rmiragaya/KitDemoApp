package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.R;

public class FinalizacionDeTrabajoDialog extends AppCompatDialogFragment {
    private static final String TAG = "FinalizacionDeTrabajoDi";
    private FinalizacionDeTrabajoListener listener;

    public FinalizacionDeTrabajoDialog() {
    }

    public static FinalizacionDeTrabajoDialog newInstance() {
        FinalizacionDeTrabajoDialog fragment = new FinalizacionDeTrabajoDialog();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //todo poner el dialog correcto
        View view = inflater.inflate(R.layout.finalizacion_trabajo_layout, null);
        builder.setView(view);

        TextView trabajo = view.findViewById(R.id.texto1Id);
        trabajo.setText("Los archivos fueron subidos correctamente");
        Button siBtn = view.findViewById(R.id.btnPositivo);
        Button noBtn = view.findViewById(R.id.btnNegativo);

        siBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: SIIIIIIIIIIIIII");
                listener.realizarOtroTrabajo(true);
                dismiss();

            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: NOOOOOOOOO");
                listener.realizarOtroTrabajo(false);
                dismiss();
            }
        });
        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (FinalizacionDeTrabajoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement FinalizacionDeTrabajoListener");
        }
    }

    public interface FinalizacionDeTrabajoListener {
        void realizarOtroTrabajo(boolean siOno);
    }
}
