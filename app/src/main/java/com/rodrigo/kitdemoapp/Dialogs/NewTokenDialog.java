package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.new_token_dialog, null);

        builder.setView(v)
                .setTitle(getActivity().getResources().getString(R.string.new_token_tittle))
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "cancelar click");
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tokenString = tokenEditText.getText().toString();
                        Log.d(TAG, "Ok: Click con el token: " + tokenString);
                        if (tokenString.isEmpty()){
                            tokenEditText.setError("Campo Obligatorio");
                        } else {
                            listener.newTokenSaved(tokenString);
                        }
                    }
                });

        tokenEditText = v.findViewById(R.id.token_editText_id);

        Dialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(false);

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
