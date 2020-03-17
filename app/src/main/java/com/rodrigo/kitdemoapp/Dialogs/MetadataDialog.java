package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rodrigo.kitdemoapp.Adapter.MetadataClienteAdapter;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.MetadataClient;
import com.rodrigo.kitdemoapp.Models.MetadataItem;
import com.rodrigo.kitdemoapp.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MetadataDialog extends AppCompatDialogFragment {

    private static final String DOCUMENTVIEWMODEL = "documentViewModel";
    private DocumentViewModel documentViewModel;

    public MetadataDialog() {
    }

    public static MetadataDialog newInstance(DocumentViewModel documentViewModel) {
        Bundle args = new Bundle();
        MetadataDialog fragment = new MetadataDialog();
        args.putParcelable(DOCUMENTVIEWMODEL, documentViewModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            documentViewModel = getArguments().getParcelable(DOCUMENTVIEWMODEL);
            Log.d(TAG, "documentViewModel onCreate: " + documentViewModel.toString());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.metadata_dialog, null);
        builder.setView(view);
        initRecyclerView(view);

        TextView clienteName = view.findViewById(R.id.clienteId);
        if (documentViewModel.getClient()!= null){
            Log.d(TAG, "titulo: " + documentViewModel.getClient());
            clienteName.setText(documentViewModel.getClient());
        } else {
            clienteName.setText(R.string.ejemplo);
        }


        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    private void initRecyclerView(View view) {
        ArrayList<MetadataItem> lista = getMetadataItems(documentViewModel);
        MetadataClienteAdapter adapter = new MetadataClienteAdapter(lista);
        RecyclerView recyclerView =  view.findViewById(R.id.recyclerViewMetadataCliente);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<MetadataItem> getMetadataItems(DocumentViewModel dodumentViewModel){
        ArrayList<MetadataItem> lista = new ArrayList<>();
        MetadataClient currentMetadata = dodumentViewModel.getMetadataClient();
        if (currentMetadata != null){

            if (currentMetadata.getBusinessName()!= null){
                lista.add(new MetadataItem("Nombre:", currentMetadata.getBusinessName()));
            }

            if (currentMetadata.getEmail()!= null){
                lista.add(new MetadataItem("Email: ", currentMetadata.getEmail()));
            }

            if (currentMetadata.getSex()!= null){
                lista.add(new MetadataItem("Sexo:", currentMetadata.getSex()));
            }

            if (currentMetadata.getCountry()!= null){
                lista.add(new MetadataItem("País:", currentMetadata.getCountry()));
            }

            if (currentMetadata.getDocumentName()!= null){
                lista.add(new MetadataItem("Documento:", currentMetadata.getDocumentName()));
            }

            if (currentMetadata.getCode()!=null){
                lista.add(new MetadataItem("Código:", currentMetadata.getCode()));
            }

            if (currentMetadata.getReason()!=null){
                lista.add(new MetadataItem("Razón:", currentMetadata.getReason()));
            }

        }
        return lista;
    }





















}
