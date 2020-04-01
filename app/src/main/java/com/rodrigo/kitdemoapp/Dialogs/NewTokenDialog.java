package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.rodrigo.kitdemoapp.R;

public class NewTokenDialog extends AppCompatDialogFragment {
    private static final String TAG = "NewTokenDialog";
    private EditText tokenEditText;
    private NewTokenDialogListener listener;

    public interface NewTokenDialogListener{
        void newTokenSaved(String newToken);
        void cancel();
    }

    public NewTokenDialog() {
    }

    public static NewTokenDialog newInstance() {
        return new NewTokenDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.new_token_dialog, null);

        builder.setView(v)
                .setTitle(getActivity().getResources().getString(R.string.new_token_tittle))
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Ok", null);

        tokenEditText = v.findViewById(R.id.token_editText_id);

        final Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tokenString = tokenEditText.getText().toString();
                        Log.d(TAG, "Ok: Click con el token: " + tokenString);
                        //TODO ME DEJA NO PONER NADA
                        if (tokenString.isEmpty() || tokenString.equalsIgnoreCase(" ")){
                            tokenEditText.setError("Campo Obligatorio");
                        } else {
                            listener.newTokenSaved(tokenString);
                            dismiss();
                        }
                    }
                });
                Button negative = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "cancelar click");
                        listener.cancel();
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (NewTokenDialogListener) context;
        } catch (ClassCastException e) {
           throw new ClassCastException(context.toString() +
                   "hay que implementar NewTokenDialogInterface");
        }
    }
}
