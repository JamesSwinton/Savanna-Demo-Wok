package com.zebra.jamesswinton.savannademowok.network;

import com.zebra.jamesswinton.savannademowok.apis.printers.pojos.AllPrinters;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PrinterEndPointsApi {

  // Get All Printers
  @GET("printers-basic")
  Call<AllPrinters> getAllPrinters(
      @Header("apikey") String apiKey,
      @Query("index") Integer index,
      @Query("size") Integer size,
      @Query("sortField") String sortField,
      @Query("sortOrder") String sortOrder,
      @Query("filter") String filter
  );

  // Send Print Job
  @POST("printers-basic/{printerSN}/sendRawData")
  Call<Object> sendPrintJob(
      @Header("apikey") String apiKey,
      @Path("printerSN") String serialNumber,
      @Body RequestBody zpl
  );

}
