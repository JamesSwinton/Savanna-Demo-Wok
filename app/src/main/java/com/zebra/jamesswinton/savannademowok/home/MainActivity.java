package com.zebra.jamesswinton.savannademowok.home;

import static com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities.DATAWEDGE_SCAN_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.CreateBarcodeFragment;
import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.UpcLookupFragment;
import com.zebra.jamesswinton.savannademowok.apis.fdarecall.GetFoodRecallByUpcFragment;
import com.zebra.jamesswinton.savannademowok.databinding.ActivityMainBinding;
import com.zebra.jamesswinton.savannademowok.apis.printers.GetAllPrintersFragment;
import com.zebra.jamesswinton.savannademowok.apis.printers.SendPrintJobFragment;
import com.zebra.jamesswinton.savannademowok.scanning.ScannerInterface;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

  // Debugging
  private static final String TAG = "MainActivity";

  // Constants
  private static final String DEV_PORTAL_URL = "https://developer.zebra.com/";
  private static final String SAVANNA_GETTING_STARTED_URL = "https://developer.zebra.com/getting-started-0";

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
    MainFragment mainFragment = new MainFragment();
    mFragmentManager.beginTransaction()
        .replace(R.id.content_frame, mainFragment)
        .commit();

    // Configure DataWedge Receiver
    if (mZebraDevice) {
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

  @Override
  public void onBackPressed(){
    if (mFragmentManager.getBackStackEntryCount() > 0) {
      mFragmentManager.popBackStack();
    } else {
      new AlertDialog.Builder(this)
        .setTitle("Confirm Exit")
        .setMessage("Are you sure you want to exit the application?")
        .setPositiveButton("EXIT", (dialogInterface, i) -> {
          System.exit(1);
        }).setNegativeButton("CANCEL", (dialogInterface, i) ->
          dialogInterface.dismiss()
        ).create().show();
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

  public void addFragmentToBackStack(Fragment fragment, String tag) {
    mFragmentManager.beginTransaction()
        .replace(R.id.content_frame, fragment)
        .addToBackStack(tag)
        .commit();
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
        // Barcode Intelligence
        case R.id.create_barcode:
          fragment = new CreateBarcodeFragment();
          break;
        case R.id.upc_lookup:
          fragment = new UpcLookupFragment();
          break;
        // FDA Recall
        case R.id.get_food_recall_upc:

          break;
        case R.id.get_drug_recall_upc:

          break;
        case R.id.get_drug_recall_description:

          break;
        case R.id.get_device_recall_description:

          break;
        // Printer
        case R.id.get_all_printers:
          fragment = new GetAllPrintersFragment();
          break;
        case R.id.get_printer_details:

          break;
        case R.id.get_printer_odometer:

          break;
        case R.id.send_print_job:

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
        break;
      case R.id.settings:
        mFragmentManager.beginTransaction()
            .replace(R.id.content_frame, new SettingsFragment())
            .addToBackStack("SETTINGS")
            .commit();
        break;
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
