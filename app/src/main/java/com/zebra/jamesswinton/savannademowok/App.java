package com.zebra.jamesswinton.savannademowok;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities;

public class App extends Application {

  // Debugging
  private static final String TAG = "ApplicationClass";

  // Constants
  private static final String API_KEY_PREF = "api_key";


  // Static Variables

  // Variables
  private static SharedPreferences mSharedPreferences;

  @Override
  public void onCreate() {
    super.onCreate();

    // Init DataWedge
    initDataWedge();

    // Init Preferences
    initPreferences();
  }

  private void initDataWedge() {
    //  Create a profile for the data capture application
    DataWedgeUtilities.CreateProfile(getApplicationContext());
    // Set Profile Configuration
    DataWedgeUtilities.SetProfileConfig(this);
  }

  /**
   * Preference Helper Methods
   */

  private void initPreferences() {
    // Init Shared Prefs
    if (mSharedPreferences == null) {
      mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }
  }

  public static String getApiKey() {
    return mSharedPreferences.getString(API_KEY_PREF, "");
  }
}
