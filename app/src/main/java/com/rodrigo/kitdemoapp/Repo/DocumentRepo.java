package com.rodrigo.kitdemoapp.Repo;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.Models.DocumentFileRepoResponse;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;
import com.rodrigo.kitdemoapp.Models.DocumentViewModelResponse;
import com.rodrigo.kitdemoapp.Models.DocumentsRepoResponse;
import com.rodrigo.kitdemoapp.Retrofit.KitDemoApi;
import com.rodrigo.kitdemoapp.Retrofit.RetrofitRequest;
import com.rodrigo.kitdemoapp.StatusResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentRepo {
    private static final String TAG = "DocumentRepo";
    private KitDemoApi apiRequest;
    private DocumentsRepoResponse documentsRepoResponse;
    private DocumentFileRepoResponse documentFileRepoResponse;
    private DocumentViewModelResponse documentViewModelResponse;
    private DocumentViewModelResponse postDocumentViewModelResponse;

    public DocumentRepo() {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(KitDemoApi.class);
    }

    public LiveData<DocumentsRepoResponse> getDocuments(final String token){
        final MutableLiveData<DocumentsRepoResponse> data = new MutableLiveData<>();

        apiRequest.getDocuments(token).enqueue(new Callback<List<Document>>() {
            @Override
            public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {

                if (response.code() >= 400){
                    documentsRepoResponse = new DocumentsRepoResponse(null, StatusResponse.ERROR_CONEXION, "Error Inesperado");
                    Log.d(TAG, "respuesa con error " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.errorBody: " + response.errorBody());
                }

                if (response.body() !=null){
                    ArrayList<Document> documentArrayList = new ArrayList<>(response.body());
                    documentsRepoResponse = new DocumentsRepoResponse(documentArrayList,StatusResponse.OK);
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }

                data.setValue(documentsRepoResponse);

            }

            @Override
            public void onFailure(Call<List<Document>> call, Throwable t) {
                documentsRepoResponse = new DocumentsRepoResponse(null, StatusResponse.ERROR_CLIENTE, t.getMessage() );
                t.fillInStackTrace();
                data.setValue(documentsRepoResponse);
            }
        });
        return data;
    }

    public LiveData<DocumentFileRepoResponse> getDocumentsFile( final String token, final String idFile, final String externalDir){
        final MutableLiveData<DocumentFileRepoResponse> data = new MutableLiveData<>();

        apiRequest.getDocumentFile(idFile ,token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() >= 400){
                    documentFileRepoResponse = new DocumentFileRepoResponse(null, StatusResponse.ERROR_CONEXION, "Error Inesperado");
                    Log.d(TAG, "respuesa con error " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.errorBody: " + response.errorBody());
                    try {
                        Log.d(TAG, "response.errorBody bytes to string: " + new String(response.errorBody().bytes(), "UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (response.body() !=null){
                    File file = createFile(externalDir, response.body());
                    documentFileRepoResponse = new DocumentFileRepoResponse(file,StatusResponse.OK);
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }

                data.setValue(documentFileRepoResponse);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                documentFileRepoResponse = new DocumentFileRepoResponse(null, StatusResponse.ERROR_CLIENTE, t.getMessage() );
                t.fillInStackTrace();
                data.setValue(documentFileRepoResponse);
            }
        });
        return data;
    }

    public LiveData<DocumentViewModelResponse> getDocumentsViewModel (final String token, final String idFile){
        final MutableLiveData<DocumentViewModelResponse> data = new MutableLiveData<>();

        apiRequest.getDocumentViewModel(idFile, token).enqueue(new Callback<DocumentViewModel>() {
            @Override
            public void onResponse(Call<DocumentViewModel> call, Response<DocumentViewModel> response) {

                if (response.code() >= 400){
                    documentViewModelResponse =  new DocumentViewModelResponse(null, StatusResponse.ERROR_CONEXION, "Error Inesperado");
                    Log.d(TAG, "respuesa con error " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.errorBody: " + response.errorBody());
                    try {
                        Log.d(TAG, "response.errorBody bytes to string: " + new String(response.errorBody().bytes(), "UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (response.body() !=null){
                    DocumentViewModel documentViewModel = response.body();
                    documentViewModelResponse = new DocumentViewModelResponse(documentViewModel, StatusResponse.OK);
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }

                data.setValue(documentViewModelResponse);
            }

            @Override
            public void onFailure(Call<DocumentViewModel> call, Throwable t) {
                documentViewModelResponse = new DocumentViewModelResponse(null, StatusResponse.ERROR_CLIENTE, t.getMessage());
                t.fillInStackTrace();
                data.setValue(documentViewModelResponse);
            }
        });
        return data;
    }

    public LiveData<DocumentFileRepoResponse> getExampleFile( final String token, final String idFile, final String externalDir){
        final MutableLiveData<DocumentFileRepoResponse> data = new MutableLiveData<>();

        apiRequest.getDocumentExampleFile(idFile,token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() >= 400){
                    documentFileRepoResponse = new DocumentFileRepoResponse(null, StatusResponse.ERROR_CONEXION, "Error Inesperado");
                    Log.d(TAG, "respuesa con error " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.errorBody: " + response.errorBody());
                    try {
                        Log.d(TAG, "response.errorBody bytes to string: " + new String(response.errorBody().bytes(), "UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (response.body() !=null){
                    File file = createFile(externalDir, response.body());
                    documentFileRepoResponse = new DocumentFileRepoResponse(file,StatusResponse.OK);
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }
                data.setValue(documentFileRepoResponse);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                documentFileRepoResponse = new DocumentFileRepoResponse(null, StatusResponse.ERROR_CLIENTE, t.getMessage() );
                t.fillInStackTrace();
                data.setValue(documentFileRepoResponse);
            }
        });
        return data;
    }

    public MutableLiveData<DocumentViewModelResponse> postDocument (final String token, final DocumentViewModel documentViewModel, final File file){
        final MutableLiveData<DocumentViewModelResponse> data = new MutableLiveData<>();

        Gson g = new Gson();
        String documentToPostString = g.toJson(documentViewModel);


        Log.d(TAG, "documentToPostString: " + documentToPostString);
        RequestBody modelParttoString = RequestBody.create(MultipartBody.FORM, documentToPostString);
        Log.d(TAG, "postDocument: " );
        RequestBody filePart = RequestBody.create(MultipartBody.FORM, file);

        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData("File", documentViewModel.getMetadataClient().getDocumentName() + ".tif", filePart);
        apiRequest.createDocument(token,modelParttoString,filePart2).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: ok");
                if (response.code() >= 400){
                    postDocumentViewModelResponse = new DocumentViewModelResponse(null, StatusResponse.ERROR_CONEXION, "Error Inesperado");
                    Log.d(TAG, "respuesa con error " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.errorBody: " + response.errorBody());
                    try {
                        Log.d(TAG, "response.errorBody bytes to string: " + new String(response.errorBody().bytes(), "UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.body() !=null){
                    postDocumentViewModelResponse = new DocumentViewModelResponse(null,StatusResponse.OK);
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }
                data.setValue(postDocumentViewModelResponse);

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: call");
                data.setValue( new DocumentViewModelResponse(null, StatusResponse.ERROR_CONEXION, "Error de Conexión"));
            }
        });
        return data;
    }



    private File createFile(String idFile, ResponseBody body){
        try {
            // todo change the file location/name according to your needs
//            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ idFile + ".pdf");
            //aca poner todo el filename
            File futureStudioIconFile = new File(idFile);
            Log.d(TAG, "futureStudioIconFile.getAbsolutePath(): " + futureStudioIconFile.getAbsolutePath());
            Log.d(TAG, "futureStudioIconFile.getPath(): " + futureStudioIconFile.getPath());

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return futureStudioIconFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

































}
