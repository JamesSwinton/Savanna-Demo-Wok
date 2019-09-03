package com.zebra.jamesswinton.savannademowok.network;

import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.pojos.UPCProduct;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface BarcodeEndPointsApi {

  // Create Barcode
  @GET("tools/barcode/generate/")
  Call<ResponseBody> createBarcode(
      @Header("apikey") String apiKey,
      @Query("symbology") String symbology,
      @Query("text") String text,
      @Query("scaleX") Integer scaleX,
      @Query("scaleY") Integer scaleY,
      @Query("scaleX") Integer scale,
      @Query("rotate") String rotate,
      @Query("includeText") String includeText
  );

  // Barcode Lookup
  @GET("tools/barcode/lookup/")
  Call<UPCProduct> upcLookup(
      @Header("apikey") String apiKey,
      @Query("upc") String barcode
  );

}
