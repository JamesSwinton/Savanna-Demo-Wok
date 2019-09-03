package com.zebra.jamesswinton.savannademowok.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.PreferenceFragmentCompat;
import com.zebra.jamesswinton.savannademowok.R;

public class SettingsFragment extends PreferenceFragmentCompat {

  // Debugging
  private static final String TAG = "SettingsFragment";

  // Constants


  // Static Variables


  // Variables


  public SettingsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);
  }

  @Override
  public void onStart() {
    super.onStart();

    // Set Title
    getActivity().setTitle("Settings");
  }
}
