package com.rodrigo.kitdemoapp.Dialogs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rodrigo.kitdemoapp.Activities.DocumentPreviewActivity;
import com.rodrigo.kitdemoapp.Adapter.DocumentRecyclerAdapter;
import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.rodrigo.kitdemoapp.Activities.DocumentPreviewActivity.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DocuFiliaFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocuFiliaFragment2 extends Fragment implements DocumentRecyclerAdapter.OnItemClickListener {
    private static final String TAG = "DocuFiliaFragment2";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DOCUMENTS = "documents";
    private static final String TIPO = "tipo";

    // TODO: Rename and change types of parameters
    private ArrayList<Document> documentsList;
    private String tipo;

    private DocuFiliaFragmentListener mListener;

    public DocuFiliaFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param documents Lista de documentos from Adapter.
     * @return A new instance of fragment DocuFiliaFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static DocuFiliaFragment2 newInstance(ArrayList<Document> documents, String tipo) {
        DocuFiliaFragment2 fragment = new DocuFiliaFragment2();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DOCUMENTS, documents);
        args.putString(TIPO, tipo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tipo = getArguments().getString(TIPO);
            documentsList = getArguments().getParcelableArrayList(DOCUMENTS);
            documentsList = separarCategorias(documentsList, tipo);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.filiatorio_document_fragment, container, false);
        initRecycler(v,documentsList);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DocuFiliaFragmentListener){
            mListener = (DocuFiliaFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implements DocuFilaListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: call");
        mListener.onDocuClick(documentsList.get(position));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface DocuFiliaFragmentListener{
        void onDocuClick(Document documentSeleccionado);
    }

    private void initRecycler(View v, ArrayList<Document> documentArrayList){
        DocumentRecyclerAdapter adapter = new DocumentRecyclerAdapter(documentArrayList);
        RecyclerView recyclerView = v.findViewById(R.id.docufiliatoriorecyclerfragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private ArrayList<Document> separarCategorias(List<Document> documentsList, String tipo){
        ArrayList<Document> documentsSeparados = new ArrayList<>();
        if (tipo.equalsIgnoreCase(FILIATORIO)){
            for (Document d : documentsList){
                if (d.getSerieName().toLowerCase().equalsIgnoreCase("filiation")){
                    documentsSeparados.add(d);
                }
            }
        } else if (tipo.equalsIgnoreCase(PRODUCTOS)){
            for (Document d : documentsList){
                if (d.getSerieName().toLowerCase().equalsIgnoreCase("high")
                        || d.getSerieName().toLowerCase().equalsIgnoreCase("low")
                        || d.getSerieName().toLowerCase().equalsIgnoreCase("modification")){
                    documentsSeparados.add(d);
                }
            }

        } else if (tipo.equalsIgnoreCase(QR)){
            for (Document d : documentsList){
                if (d.getSerieName().toLowerCase().equalsIgnoreCase("qr") || d.getSerieName().toLowerCase().equalsIgnoreCase("barcode")){
                    documentsSeparados.add(d);
                }
            }
        } else if (tipo.equalsIgnoreCase(RECORTEFIRMA)){
            for (Document d : documentsList){
                if (d.getSerieName().toLowerCase().equalsIgnoreCase("signature")){
                    documentsSeparados.add(d);
                }
            }
        } else if (tipo.equalsIgnoreCase(EJEMPLOS)){
            documentsSeparados.add(new Document("1","QR", "demo id", null, "Carátula"));
            documentsSeparados.add(new Document("2","Barcode", "demo id", null, "Carátula"));
            documentsSeparados.add(new Document("3","Recorte de Firma", "demo id", null, "Carátula"));
        }

        return documentsSeparados;
    }
}
