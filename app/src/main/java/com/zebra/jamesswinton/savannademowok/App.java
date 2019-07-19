package com.zebra.jamesswinton.savannademowok;

import android.app.Application;
import com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities;

public class App extends Application {

  // Debugging
  private static final String TAG = "ApplicationClass";

  // Constants


  // Static Variables


  // Variables


  @Override
  public void onCreate() {
    super.onCreate();

    // Init DataWedge
    initDataWedge();
  }

  private void initDataWedge() {
    //  Create a profile for the data capture application
    DataWedgeUtilities.CreateProfile(getApplicationContext());
    // Set Profile Configuration
    DataWedgeUtilities.SetProfileConfig(this);
  }
}
