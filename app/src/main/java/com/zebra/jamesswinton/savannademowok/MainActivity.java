package com.zebra.jamesswinton.savannademowok;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zebra.jamesswinton.savannademowok.barcodeintelligence.CreateBarcodeFragment;
import com.zebra.jamesswinton.savannademowok.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  // Debugging
  private static final String TAG = "MainActivity";

  // Constants


  // Static Variables


  // Variables
  private ActivityMainBinding mDataBinding;
  private FragmentManager mFragmentManager;
  private MenuItem mPreviousMenuItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    // Init FragmentManager
    mFragmentManager = getSupportFragmentManager();

    // Setup Navigation
    configureToolbar();
    configureNavigationDrawer();
  }

  public void showProgressBar(boolean visibile) {
    mDataBinding.progressBar.setVisibility(visibile ? View.VISIBLE : View.GONE);
  }

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
}
