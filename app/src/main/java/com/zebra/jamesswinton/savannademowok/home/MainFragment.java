package com.zebra.jamesswinton.savannademowok.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.databinding.FragmentMainBinding;
import im.delight.android.webview.AdvancedWebView;

public class MainFragment extends Fragment implements AdvancedWebView.Listener {

  // Debugging
  private static final String TAG = "MainFragment";

  // Constants
  private static final String DEFAULT_URL = "https://www.zebra.com/gb/en/solutions/savanna.html";
  private static final String DEV_PORTAL_URL = "https://developer.zebra.com/";
  private static final String SAVANNA_GETTING_STARTED_URL = "https://developer.zebra.com/getting-started-0";

  // Static Variables


  // Variables
  private FragmentMainBinding mDataBinding;

  private String mUrl;

  public MainFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Init DataBinding
    mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container,
        false);

    // Get Args (URL)
    if (getArguments() != null) {
      mUrl = getArguments().getString(App.ARG_HOME_PAGE_URL, DEFAULT_URL);
    } else {
      mUrl = DEFAULT_URL;
    }

    // Return Root View
    return mDataBinding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();

    // Set Title
    String title;
    switch (mUrl) {
      case DEV_PORTAL_URL:
        title = "Developer Portal";
        break;
      case SAVANNA_GETTING_STARTED_URL:
        title = "Getting Started with Savanna";
        break;
      case DEFAULT_URL:
      default:
        title = "Home";
        break;
    } getActivity().setTitle(title);

    // Init WebView
    mDataBinding.webView.setListener(getActivity(), this);

    // Validate Network Connection -> Load WebPage or Show Default
    if (hasNetwork()) {
      mDataBinding.webView.setVisibility(View.VISIBLE);
      mDataBinding.noNetworkLayout.setVisibility(View.GONE);
      mDataBinding.webView.loadUrl(mUrl);
    } else {
      mDataBinding.webView.setVisibility(View.GONE);
      mDataBinding.noNetworkLayout.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  @SuppressLint("NewApi")
  @Override
  public void onResume() {
    super.onResume();
    mDataBinding.webView.onResume();
  }

  @SuppressLint("NewApi")
  @Override
  public void onPause() {
    mDataBinding.webView.onPause();
    super.onPause();
  }

  @Override
  public void onDestroy() {
    mDataBinding.webView.onDestroy();
    super.onDestroy();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    mDataBinding.webView.onActivityResult(requestCode, resultCode, intent);
  }

  /**
   * WebView Methods
   */

  @Override
  public void onPageStarted(String url, Bitmap favicon) { }

  @Override
  public void onPageFinished(String url) { }

  @Override
  public void onPageError(int errorCode, String description, String failingUrl) { }

  @Override
  public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

  @Override
  public void onExternalPageRequest(String url) { }

  /**
   * Network Validation
   */

  private boolean hasNetwork() {
    ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }
}
