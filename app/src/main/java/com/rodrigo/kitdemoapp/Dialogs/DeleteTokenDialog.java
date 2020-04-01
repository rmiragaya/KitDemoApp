package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteTokenDialog  extends AppCompatDialogFragment {

    private DeleteTokenListener listener;

    public DeleteTokenDialog() {
    }

    public static DeleteTokenDialog newInstance() {

        Bundle args = new Bundle();
        DeleteTokenDialog fragment = new DeleteTokenDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Desea borrar el Token?");
        builder.setMessage("Si presiona OK, se borrará el Token guardado y la aplicación se reiniciará.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.deleteToken();
                dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();


    }

    public interface DeleteTokenListener{
        void deleteToken();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteTokenListener) context;
        } catch (ClassCastException e ){
            throw new ClassCastException(context.toString() +
                    "hay que implementar DeleteTokenListener");
        }

    }
}
