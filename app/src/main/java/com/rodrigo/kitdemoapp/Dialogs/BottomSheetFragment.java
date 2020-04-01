package com.rodrigo.kitdemoapp.Dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.Utils.ImagenManipulation;
import com.rodrigo.kitdemoapp.Utils.Tools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private Demo demo;
    private BottomSheetListener listener;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BottomSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomSheetFragment newInstance() {
        return  new BottomSheetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo = Tools.getDemoFromSharePreference(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        ((TextView) view.findViewById(R.id.name)).setText(demo.getClient());
        ((TextView) view.findViewById(R.id.brief)).setText(demo.getToken());
        ImageView logoImageView = view.findViewById(R.id.logoEmpresaBottomSheet);
        Bitmap logo = ImagenManipulation.loadImage(demo.getLogo());
        if (logo != null){
            logoImageView.setImageBitmap(ImagenManipulation.resize(logo, 80, 80));
        }

        Button borrarTokenBtn = view.findViewById(R.id.deleteToken);
        borrarTokenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBorrarTokenButtonPress();
                dismiss();
            }
        });

        return view;
    }

    public interface BottomSheetListener{
        void onBorrarTokenButtonPress();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (BottomSheetListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must Implement BottomSheetListener");
        }

    }
}
