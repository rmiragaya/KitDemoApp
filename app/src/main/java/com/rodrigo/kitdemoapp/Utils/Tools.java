package com.rodrigo.kitdemoapp.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.DemoRepoResponse;
import com.rodrigo.kitdemoapp.Models.MetadataClient;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Tools {

    private static final String METADATA_CLIENT = "metadataClient";
    private static final String TAG = "Tools";
    private static final String TOKEN = "demoToken";
    private static final String DEMO = "demodemo";

    /**
     * Carga el Token en SharePreference
     */
    public static String getTokenFromPreference(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String tokenGuardado = sharedPreferences.getString(Tools.TOKEN, "");
        Log.d(TAG, "token guardada: " + tokenGuardado);
        return tokenGuardado;
    }

    /**
     * Guarda el Token en SharePreference
     */
    @SuppressLint("ApplySharedPref")
    public static void saveTokenOnSharePreference(Context context, String token) {
        Log.d(TAG, "token a guardar: " + token);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Tools.TOKEN, token);
        editor.commit();
    }

    /**
     * Guarda la demo en SharePreference
     */
    @SuppressLint("ApplySharedPref")
    public static void saveDemoOnSharePreference(Context ctx, Demo demoToSave){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(demoToSave);
        editor.putString(Tools.DEMO, json);
        editor.commit();
    }

    /**
     * Carga el Token en SharePreference
     */
    public static Demo getDemoFromSharePreference(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String demoJson = sharedPreferences.getString(Tools.DEMO, "");
        Log.d(TAG, "demoJson guardado: " + demoJson);
        Gson gson = new Gson();
        Demo demoGuardado = gson.fromJson(demoJson, Demo.class);
        Log.d(TAG, "demoGuardado: " + demoGuardado.toString());
        return demoGuardado;
    }

    /**
     * Guarda la metadata del cliente en SharePreference
     */
    @SuppressLint("ApplySharedPref")
    public static void saveMetadataClientSharePreference(Context context, MetadataClient metadataClient){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(metadataClient);
        editor.putString(Tools.METADATA_CLIENT, json);
        editor.commit();
    }

    /**
     * Carga la metadata del cliente en SharePreference
     */
    public static MetadataClient getMetadataClientSharePreference(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String metadataJson = sharedPreferences.getString(Tools.METADATA_CLIENT, "");
        Log.d(TAG, "json guardado etadataClient: " + metadataJson);
        Gson gson = new Gson();
        MetadataClient metadataClient = gson.fromJson(metadataJson, MetadataClient.class);
        Log.d(TAG, "metadataClient: " + metadataClient.toString());
        return metadataClient;
    }

    /**
     * Como existe una diferencia entre el formato Date de android con el de la api
     * necesitamos de este metodo para convertirlo al pasar entre uno y otro
     */
    public static String convertDateTimeInDateString(String dateTimeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return convetDateFormat.format(date);
    }

    /**
     * Como existe una diferencia entre el formato Date de android con el de la api
     * necesitamos de este metodo para convertirlo al pasar entre uno y otro
     */
    public static String convertDateStringInDateTime(long milliseconds){
        Log.d(TAG, "milliseconds " + milliseconds);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        Log.d(TAG, "sdf.format(calendar.getTime(): " + sdf.format(calendar.getTime()));
        return sdf.format(calendar.getTime());
    }

    /**
     * Compruba si la demo es valida o no
     *
     */
    //todo sacar los textos de strings value
    public static DemoRepoResponse validarDemo(DemoRepoResponse demoResponseLData){
        Log.d(TAG, "validarDemo: call");
        if (demoResponseLData.getDemo().isExpired()){
            demoResponseLData.setStatusResponse(StatusResponse.ERROR_DEMO);
            demoResponseLData.setMensajeError("Token Expirado");
        }
        if (!demoResponseLData.getDemo().isActive()){
            demoResponseLData.setStatusResponse(StatusResponse.ERROR_DEMO);
            demoResponseLData.setMensajeError("Token no activado");
        }
        return demoResponseLData;
    }

    /**
     * Delete all pdf and tiff files
     */
    public static void deleteAllFiles(Context ctx){
        File folder = new File(ctx.getExternalFilesDir(null).getAbsolutePath());
        File[] children = folder.listFiles();
        Log.d(TAG, "children: " + children.length);
        Log.d(TAG, "folder.getAbsolutePath(): " + folder.getAbsolutePath());
        deleteRecursive(children);
    }

    private static void deleteRecursive(File[] filesToDelete){
        for (File file : filesToDelete){
            Log.d(TAG, "file: " + file.getName());
            file.delete();
        }
    }

    /**
     * For API version with no TLS (api 19)
     * */
    public static void checkTls(Context ctx){
        try {
            ProviderInstaller.installIfNeeded(ctx);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }



}
