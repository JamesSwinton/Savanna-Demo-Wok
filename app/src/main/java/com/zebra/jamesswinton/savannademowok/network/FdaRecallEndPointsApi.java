package com.zebra.jamesswinton.savannademowok.network;

import com.zebra.jamesswinton.savannademowok.apis.fdarecall.pojos.FDARecall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface FdaRecallEndPointsApi {

  // Create Barcode
  @GET("tools/recalls/food/upc")
  Call<FDARecall> getFoodRecallUpc(
      @Header("apikey") String apiKey,
      @Query("val") String UPC,
      @Query("limit") int limit
  );

}
