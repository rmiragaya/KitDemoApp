package com.rodrigo.kitdemoapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.R;

import java.util.ArrayList;
import java.util.List;

public class DocumentRecyclerAdapter extends RecyclerView.Adapter<DocumentRecyclerAdapter.MyViewHolder> {
    private static final String TAG = "DocumentRecyclerAdapter";

    private List<Document> documentosList;
    private OnItemClickListener mListener;

    /* interface for onclick on Documents */
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public DocumentRecyclerAdapter( ArrayList<Document> documentosList) {
        this.documentosList = documentosList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.documents_recycler_adapter, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String id = documentosList.get(position).getId();
        String serieName = documentosList.get(position).getSerieName();
        String cliente = documentosList.get(position).getClient();
        serieName = traducirSerieName(serieName);

        holder.docuid.setText("Id: " + id);
        holder.demoId.setText("Cliente: " + cliente);
        holder.path.setText(serieName);

    }

    @Override
    public int getItemCount() {
        return documentosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView docuid, seriename,  demoId, path;

        public MyViewHolder( View itemView) {
            super(itemView);
            docuid = itemView.findViewById(R.id.documentidid);
            demoId = itemView.findViewById(R.id.documentDemoId);
            path = itemView.findViewById(R.id.documentFilePath);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                            Log.d(TAG, "onClick: position " + position);
                        }
                    }
                }
            });
        }
    }

    private String traducirSerieName(String serieName){

        switch (serieName.toLowerCase()){
            case "filiation":
                serieName = "Filiatorio";
                break;
            case "high":
                serieName = "Alta Producto";
                break;
            case "low":
                serieName = "Baja Producto";
                break;
            case "modification":
                serieName = "Modificación Producto";
                break;
            case "signature":
                serieName = "Recorte de Firma";
                break;
            case "qr":
                serieName = "QR";
                break;
            case "barcode":
                serieName = "Código de Barra";
                break;
        }

        return serieName;
    }
}