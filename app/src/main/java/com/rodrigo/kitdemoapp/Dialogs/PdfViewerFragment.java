package com.rodrigo.kitdemoapp.Dialogs;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.FileObserver;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.rodrigo.kitdemoapp.Activities.DocumentPreviewActivity;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PdfViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PdfViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PdfViewerFragment extends Fragment {
    private static final String TAG = "PdfViewerFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FILE = "filePassed";
    private static final String DOCUMENTVIEWMODEL = "documentViewModel";
    private long mLastClickTime = 0;

    private String fileDir;
    private DocumentViewModel documentViewModel;

    private OnFragmentInteractionListener mListener;

    public PdfViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fileDir Parameter 1.
     * @return A new instance of fragment PdfViewerFragment.
     */

    public static PdfViewerFragment newInstance(String fileDir, DocumentViewModel documentViewModel) {
        PdfViewerFragment fragment = new PdfViewerFragment();
        Bundle args = new Bundle();
        args.putString(FILE, fileDir);
        args.putParcelable(DOCUMENTVIEWMODEL, documentViewModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileDir = getArguments().getString(FILE);
            documentViewModel = getArguments().getParcelable(DOCUMENTVIEWMODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pdf_viewer, container, false);
        // find views
//        Log.d(TAG, "file.isEmpty(): " + file.isEmpty());
//        Log.d(TAG, "documentViewModel.toString(): " + documentViewModel.toString());
        PDFView pdfView = v.findViewById(R.id.pdfPreview);
        final File file = new File(fileDir);
        pdfView.fromFile(file)
                .swipeHorizontal(true)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(getActivity()))
                .spacing(10)
                .enableAntialiasing(true)
                .load();

        Button buttonDialog = v.findViewById(R.id.showDialogButton);
        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                // mis-clicking prevention, using threshold of 1000 ms
                showDialogWithInfo();
            }
        });

        ImageView shareButton = v.findViewById(R.id.shareBtnId);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile(file);
            }
        });

        return v;
    }

    private void shareFile(File file) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        if(file.exists()) {
            intentShareFile.setType("application/pdf");

            Uri uri = FileProvider.getUriForFile(getContext(),"com.rodrigo.kitdemoapp" , file);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(intentShareFile, "Compartir archivo"));
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
        void fragmentFinish();
    }

    private void showDialogWithInfo(){
        MetadataDialog metadataDialog = MetadataDialog.newInstance(documentViewModel);
        metadataDialog.show(getChildFragmentManager(), "Metadata Dialog");
    }


    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim;
        if (enter) {
            anim =  AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        } else {
            anim =  AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
        }


        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "Animation started.");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "Animation repeating.");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "Animation ended.");
                mListener.fragmentFinish();
            }
        });

        return anim;
    }
}
