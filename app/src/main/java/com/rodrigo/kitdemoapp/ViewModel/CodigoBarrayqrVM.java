package com.rodrigo.kitdemoapp.ViewModel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.rodrigo.kitdemoapp.Interfaces.ICodigoBarrayqrVM;
import com.rodrigo.kitdemoapp.Utils.ImagenManipulation;
import com.rodrigo.kitdemoapp.Utils.Tools;

public class CodigoBarrayqrVM extends AndroidViewModel implements ICodigoBarrayqrVM {

    private String nombreEmpresa;
    private String logoEnBitmap;

    public CodigoBarrayqrVM(@NonNull Application application) {
        super(application);
        nombreEmpresa = Tools.getDemoFromSharePreference(application).getClient();
        logoEnBitmap = Tools.getDemoFromSharePreference(application).getLogo();
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public Bitmap getLogoEnBitmap() {
        return ImagenManipulation.loadImage(logoEnBitmap);
    }

}
