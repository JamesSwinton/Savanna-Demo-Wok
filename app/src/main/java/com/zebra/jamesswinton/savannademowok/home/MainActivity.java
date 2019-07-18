package com.zebra.jamesswinton.savannademowok.home;

import static com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities.DATAWEDGE_SCAN_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.barcodeintelligence.CreateBarcodeFragment;
import com.zebra.jamesswinton.savannademowok.barcodeintelligence.UpcLookupFragment;
import com.zebra.jamesswinton.savannademowok.databinding.ActivityMainBinding;
import com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities;
import com.zebra.jamesswinton.savannademowok.scanning.ScannerInterface;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

  // Debugging
  private static final String TAG = "MainActivity";

  // Constants
  private static final int ALL_PERMISSIONS = 1;

  // Static Variables
  public static boolean mZebraDevice = false;

  // Variables
  private ActivityMainBinding mDataBinding;
  private FragmentManager mFragmentManager;
  private MenuItem mPreviousMenuItem;
  private ScannerInterface mScannerInterface;
  private IntentFilter mIntentFilter = new IntentFilter();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    // Get Zebra Device or Not
    setDeviceAsZebraOrCommercial();

    // Init FragmentManager
    mFragmentManager = getSupportFragmentManager();

    // Show Video Fragment
    mFragmentManager.beginTransaction()
        .replace(R.id.content_frame, new MainFragment())
        .commit();

    // Configure DataWedge
    if (mZebraDevice) {
      configureDataWedgeProfile();
      mIntentFilter.addAction(DATAWEDGE_SCAN_ACTION);
    }

    // Setup Navigation
    configureToolbar();
    configureNavigationDrawer();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (mZebraDevice) {
      unregisterReceiver(dwBroadcastReceiver);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mZebraDevice) {
      registerReceiver(dwBroadcastReceiver, mIntentFilter);
    }
  }

  private void setDeviceAsZebraOrCommercial() {
    mZebraDevice = Build.MANUFACTURER.contains("Zebra Technologies") ||
        Build.MANUFACTURER.contains("Motorola Solutions");
  }

  /**
   * Fragment Utility Methods
   */

  public void showProgressBar(boolean visible) {
    mDataBinding.progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  /**
   * Configuration Methods
   */

  private void configureToolbar() {
    setSupportActionBar(mDataBinding.toolbarLayout.toolbar);
    ActionBar actionbar = getSupportActionBar();
    if (actionbar != null) {
      actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
      actionbar.setDisplayHomeAsUpEnabled(true);
    }
  }

  private void configureNavigationDrawer() {
    mDataBinding.navigationView.setNavigationItemSelectedListener(menuItem -> {
      // Init Blank Fragment
      Fragment fragment = null;

      // Switch Fragment
      switch(menuItem.getItemId()) {
        case R.id.create_barcode:
          fragment = new CreateBarcodeFragment();
          break;
        case R.id.upc_lookup:
          fragment = new UpcLookupFragment();
          break;
      }

      // Transact Fragment
      if (fragment != null) {
        mFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit();
      }

      // Highlight Item
      checkMenuItem(menuItem);

      // Close Drawer
      mDataBinding.drawerLayout.closeDrawers();
      return false;
    });
  }

  private void checkMenuItem(MenuItem menuItem) {
    menuItem.setCheckable(true);
    menuItem.setChecked(true);
    if (mPreviousMenuItem != null) {
      mPreviousMenuItem.setChecked(false);
    } mPreviousMenuItem = menuItem;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    // Handle Navigation Events
    switch(item.getItemId()) {
      case android.R.id.home:
        mDataBinding.drawerLayout.openDrawer(GravityCompat.START);
        return true;
    } return true;
  }

  /**
   * Permissions Methods
   */

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
      @NonNull int[] grantResults) {
    // Forward results to EasyPermissions
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  private void showDialogOK(DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(this)
        .setMessage("All Permissions are required to run this app")
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", okListener)
        .create()
        .show();
  }

  /**
   *  Commercial Scanner Methods
   */

  public void registerScannerInterface(ScannerInterface scannerInterface) {
    mScannerInterface = scannerInterface;
  }

  public void unregisterScannerInterface() {
    mScannerInterface = null;
  }

  public void startScan() {
    // Start Scan Via DW or ZX'ing
    if (!mZebraDevice) {
      new IntentIntegrator(this).initiateScan();
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if(result != null) {
      if(result.getContents() == null) {
        Log.d("MainActivity", "Cancelled scan");
        mScannerInterface.onScanFailure("Cancelled Scan");
      } else {
        Log.d("MainActivity", "Scanned");
        mScannerInterface.onScanSuccess(result.getContents());
      }
    } else {
      // This is important, otherwise the result will not be passed to the fragment
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  /**
   * DataWedge Scanner Methods
   */

  private void configureDataWedgeProfile() {
    //  Create a profile for the data capture application
    DataWedgeUtilities.CreateProfile(getApplicationContext());
    // Set Profile Configuration
    DataWedgeUtilities.SetProfileConfig(this);
  }

  private BroadcastReceiver dwBroadcastReceiver = new BroadcastReceiver() {

    private static final String SCAN_DATA = "com.symbol.datawedge.data_string";

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (action != null && action.equals(DATAWEDGE_SCAN_ACTION)) {
        Log.i(TAG, "DW Scan Received");
        mScannerInterface.onScanSuccess(intent.getStringExtra(SCAN_DATA));
      }
    }
  };
}
