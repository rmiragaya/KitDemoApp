package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DocuFiliaDialog extends AppCompatDialogFragment {
    private static final String TAG = "DocuFiliaDialog";
    private static final String NUMDIALOG = "numeroDialog";
    private int numDialog;
    private DocuFiliaDialogInterface listener;

    public DocuFiliaDialog() {
    }

    public static DocuFiliaDialog newInstance(int numDialog){
        Bundle args = new Bundle();
        DocuFiliaDialog fragment = new DocuFiliaDialog();
        args.putInt(NUMDIALOG, numDialog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numDialog = getArguments().getInt(NUMDIALOG);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getTitulo())
                .setMessage(getMessage())
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: CALL, numDialog: "+ numDialog);
                                listener.backFromDialog();
                            }
                        }).create();
    }

    private String getTitulo(){
        String message = "";
        switch (numDialog) {
            case 0:
                message = "DNI";
            break;
            case 1:
                message = "Constancia";
            break;
            case 2:
                message = "Otra Documentación";
            break;
        }
        return message;
    }

    private String getMessage(){
        String message = "";
        switch (numDialog) {
            case 0:
                message = "Ingrese DNI";
                break;
            case 1:
                message = "Ingrese Constancia";
                break;
            case 2:
                message = "Ingrese Otra Documentación";
                break;
        }
        return message;
    }

    public interface DocuFiliaDialogInterface{
        void backFromDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DocuFiliaDialogInterface) context;
        } catch (Exception e){
            throw  new ClassCastException(context.toString()+ " must implements DocuFiliaDialogInterface") ;
        }

    }
}
