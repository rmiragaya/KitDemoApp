package com.rodrigo.kitdemoapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodrigo.kitdemoapp.Models.MetadataItem;
import com.rodrigo.kitdemoapp.R;

import java.util.List;

public class MetadataClienteAdapter extends RecyclerView.Adapter<MetadataClienteAdapter.MyViewHolder> {
    private static final String TAG = "MetadataClienteAdapter";

    private List<MetadataItem> metadataItemList;

    public MetadataClienteAdapter(List<MetadataItem> metadataItemList) {
        this.metadataItemList = metadataItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.metadata_client_adapter_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String titulo = metadataItemList.get(position).getTitulo();
        Log.d(TAG, "titulo: " + titulo);
        String valor = metadataItemList.get(position).getValor();
        Log.d(TAG, "valor: " + valor);

        holder.titulo.setText(titulo);
        holder.valor.setText(valor);
    }

    @Override
    public int getItemCount() {
        return metadataItemList.size();
    }

     public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView titulo, valor;

         public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tituloMetadataId);
            valor = itemView.findViewById(R.id.valorMetadataId);
        }
    }
}
