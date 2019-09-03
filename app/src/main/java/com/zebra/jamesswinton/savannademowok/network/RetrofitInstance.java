package com.zebra.jamesswinton.savannademowok.network;

import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

  private static Retrofit retrofitInstance = null;
  private static Retrofit retrofitInstanceSandbox = null;
  private static final String BASE_URL = "https://api.zebra.com/v2/";
  private static final String BASE_URL_SANDBOX = "https://sandbox-api.zebra.com/v2/";

  public static Retrofit getInstance() {
    if (retrofitInstance == null) {
      retrofitInstance = new Retrofit.Builder()
          .baseUrl(BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
          .build();
    } return retrofitInstance;
  }

  public static Retrofit getInstanceSandbox() {
    if (retrofitInstanceSandbox == null) {
      retrofitInstanceSandbox = new Retrofit.Builder()
          .baseUrl(BASE_URL_SANDBOX)
          .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
          .build();
    } return retrofitInstanceSandbox;
  }

  private RetrofitInstance() { }
}
