package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.StatusResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentErrorDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentErrorDialog extends AppCompatDialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ERRORMENSAJE = "param1";

    private String errorMensaje;
    private ErrorDocumentInterface listener;

    public DocumentErrorDialog() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DocumentErrorDialog.
     */
    public static DocumentErrorDialog newInstance(String mensajeError) {
        DocumentErrorDialog fragment = new DocumentErrorDialog();
        Bundle args = new Bundle();
        args.putString(ERRORMENSAJE, mensajeError);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //todo poner el nuevo layout
        View v = inflater.inflate(R.layout.demo_error_dialog, null);

        builder.setView(v)
                .setTitle(getActivity().getResources().getString(R.string.error_document))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onErrorDocumentOkBtn();
                        dismiss();
                    }
                });

        TextView mensaje = v.findViewById(R.id.mensaje_dialog_id);
        mensaje.setText(getActivity().getResources().getString(R.string.error_document_sub));

        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(false);

        return dialog;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            errorMensaje = getArguments().getString(ERRORMENSAJE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    public interface ErrorDocumentInterface {
        void onErrorDocumentOkBtn();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ErrorDocumentInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ErrorDocumentInterface");
        }
    }
}
