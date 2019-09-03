package com.zebra.jamesswinton.savannademowok.scanning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class DataWedgeUtilities {

  //  DataWedge API
  public static final String PROFILE_NAME = "SavannaDemoWok";
  public static final String ACTION_DATAWEDGE_FROM_6_2 = "com.symbol.datawedge.api.ACTION";
  private static final String EXTRA_SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";
  private static final String EXTRA_CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE";
  public static final String DATAWEDGE_SCAN_ACTION = "com.darryncampbell.datacapture.ACTION";

  private static void sendDataWedgeIntentWithExtra(String action, String extraKey, String extraValue, Context context) {
    Intent dwIntent = new Intent();
    dwIntent.setAction(action);
    dwIntent.putExtra(extraKey, extraValue);
    context.sendBroadcast(dwIntent);
  }

  private static void sendDataWedgeIntentWithExtra(String action, String extraKey, Bundle extras, Context context) {
    Intent dwIntent = new Intent();
    dwIntent.setAction(action);
    dwIntent.putExtra(extraKey, extras);
    context.sendBroadcast(dwIntent);
  }

  public static void CreateProfile(Context context) {
    sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE_FROM_6_2, EXTRA_CREATE_PROFILE, PROFILE_NAME, context);
  }

  public static void enableProfile(Context cx) {
    Bundle profileConfig = new Bundle();
    profileConfig.putString("PROFILE_NAME", PROFILE_NAME);
    profileConfig.putString("PROFILE_ENABLED", "true");
    profileConfig.putString("CONFIG_MODE", "UPDATE");
    sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE_FROM_6_2, EXTRA_SET_CONFIG, profileConfig, cx);
  }

  public static void disableProfile(Context cx) {
    Bundle profileConfig = new Bundle();
    profileConfig.putString("PROFILE_NAME", PROFILE_NAME);
    profileConfig.putString("PROFILE_ENABLED", "false");
    profileConfig.putString("CONFIG_MODE", "UPDATE");
    sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE_FROM_6_2, EXTRA_SET_CONFIG, profileConfig, cx);
  }

  public static void SetProfileConfig(Context context) {
    // Create Base Profile Config Bundle
    Bundle profileConfig = new Bundle();
    profileConfig.putString("PROFILE_NAME", PROFILE_NAME);
    profileConfig.putString("PROFILE_ENABLED", "false");
    profileConfig.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");

    // Create Associated App Bundle
    Bundle appConfig = new Bundle();
    appConfig.putString("PACKAGE_NAME", "com.zebra.jamesswinton.savannademowok");
    appConfig.putStringArray("ACTIVITY_LIST", new String[]{"com.zebra.jamesswinton.savannademowok" + ".MainActivity"});

    // Store AppConfig in ProfileConfig
    profileConfig.putParcelableArray("APP_LIST", new Bundle[]{appConfig});

    // Create Barcode Config Bundle
    Bundle barcodeConfig = new Bundle();
    barcodeConfig.putString("PLUGIN_NAME", "BARCODE");
    barcodeConfig.putString("RESET_CONFIG", "true");

    // Create BarcodeProperties Bundle
    Bundle barcodeProps = new Bundle();
    barcodeProps.putString("scanner_selection", "auto");
    barcodeProps.putString("scanner_input_enabled", "true");
    barcodeProps.putString("decoder_code128", "true");
    barcodeProps.putString("decoder_code39", "true");
    barcodeProps.putString("decoder_ean8", "true");
    barcodeProps.putString("decoder_ean13", "true");
    barcodeProps.putString("decoder_qrcode", "true");

    // Store BarcodeProperties in BarcodeConfig
    barcodeConfig.putBundle("PARAM_LIST", barcodeProps);

    // Store BarcodeConfig in Profile Config
    profileConfig.putBundle("PLUGIN_CONFIG", barcodeConfig);

    // Send Intent
    sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE_FROM_6_2, EXTRA_SET_CONFIG, profileConfig, context);
  }

}