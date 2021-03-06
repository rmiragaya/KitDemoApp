package com.rodrigo.kitdemoapp.Retrofit;

import com.rodrigo.kitdemoapp.Models.Cliente;
import com.rodrigo.kitdemoapp.Models.Demo;
import com.rodrigo.kitdemoapp.Models.Document;
import com.rodrigo.kitdemoapp.Models.DocumentViewModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface KitDemoApi {

    //https://apikitdemohp.soluciones.com.ar/api/


    //funciona
    @GET("Documents/GetDocuments/{demoToken}")
    Call<List<Document>> getDocuments(@Path("demoToken") String demoToken);

    @GET("Documents/GetDocument/{id}")
    Call<DocumentViewModel> getDocumentViewModel(@Path("id") String documentId,
                                                 @Header("Token") String header);

    @Multipart
    @Headers({"Source: AndroidApp"})
    @POST("Documents/Create")
    Call<ResponseBody> createDocument(
            @Header("Token") String header,
            @Part ("Model")RequestBody document ,
            @Part MultipartBody.Part documentFile
    );

    @GET("Demos/GetDemo/{token}")
    Call<Demo> getDemo(@Path("token") String token);

    @GET("Demos/GetDemoClientMetadata/{token}&{clientId}")
    Call<Cliente> getClient(@Path("token") String token, @Path("clientId") String clientId);

//    @GET("Documents/GetDocuments/{token}")
//    Call<List<Document>> getDocuments(@Path("token") String token);

    @GET("Documents/GetDocumentFile/{id}")
    Call<ResponseBody> getDocumentFile(@Path("id") String documentId,
                                       @Header("Token") String header);

    @GET("Documents/GetCoverFile/{id}")
    Call<ResponseBody> getDocumentExampleFile(@Path("id") String documentId,
                                       @Header("Token") String header);


}
