package com.rodrigo.kitdemoapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rodrigo.kitdemoapp.R;
import com.rodrigo.kitdemoapp.Utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AltaBajaModificacionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AltaBajaModificacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AltaBajaModificacionFragment extends AppCompatDialogFragment {

    private OnFragmentInteractionListener mListener;

    public AltaBajaModificacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AltaBajaModificacionFragment.
     */
    public static AltaBajaModificacionFragment newInstance() {
        return new AltaBajaModificacionFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alta_baja_modificacion, container, false);
        Button alta = v.findViewById(R.id.altaBtn);
        Button baja = v.findViewById(R.id.bajaBtn);
        Button mod = v.findViewById(R.id.modificacionBtn);

        alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Alta", Toast.LENGTH_SHORT).show();
                onButtonPressed(Constant.ALTA);
            }
        });

        baja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Baja", Toast.LENGTH_SHORT).show();
                onButtonPressed(Constant.BAJA);
            }
        });

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Modificación", Toast.LENGTH_SHORT).show();
                onButtonPressed(Constant.MODIFICACION);
            }
        });

        return v;
    }

    private void onButtonPressed(String altaBajaoMod) {
        if (mListener != null) {
            mListener.onAltaBajaOModSelect(altaBajaoMod);
            dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAltaBajaOModSelect(String string);
    }
}
