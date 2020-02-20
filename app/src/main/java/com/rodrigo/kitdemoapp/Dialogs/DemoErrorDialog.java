package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.rodrigo.kitdemoapp.Models.DemoRepoResponse;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.StatusResponse;

public class DemoErrorDialog extends AppCompatDialogFragment {
    private static final String TAG = "DemoErrorDialog";
    private DemoErrorListener listener;
    private DemoRepoResponse demoRepoResponse;

    public DemoErrorDialog(DemoRepoResponse demoRepoResponse) {
        this.demoRepoResponse = demoRepoResponse;
    }

    public interface DemoErrorListener{
        void addNewToken(Boolean addNewToken);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //todo poner el nuevo layout
        View v = inflater.inflate(R.layout.demo_error_dialog, null);

        builder.setView(v)
                .setTitle(getActivity().getResources().getString(R.string.error_demo))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (demoRepoResponse.getStatusResponse() == StatusResponse.ERROR_DEMO){
                            listener.addNewToken(true);
                        } else {
                            listener.addNewToken(false);
                        }
                    }
                });

        TextView mensaje = v.findViewById(R.id.mensaje_dialog_id);
        Log.d(TAG, "demoRepoResponse: " + demoRepoResponse.toString());
        mensaje.setText(demoRepoResponse.getMensajeError());

        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(false);

        return dialog;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DemoErrorListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "hay que implementar DemoErrorListener");
        }

    }
}
