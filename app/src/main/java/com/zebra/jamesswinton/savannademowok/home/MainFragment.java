package com.zebra.jamesswinton.savannademowok.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.databinding.FragmentMainBinding;
import im.delight.android.webview.AdvancedWebView;

public class MainFragment extends Fragment implements AdvancedWebView.Listener {

  // Debugging
  private static final String TAG = "MainFragment";

  // Constants


  // Static Variables


  // Variables
  private FragmentMainBinding mDataBinding;

  public MainFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container,
        false);
    return mDataBinding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();

    // Load & Play Splash Video
    // initSplashVideo();

    // Init WebView
    mDataBinding.webView.setListener(getActivity(), this);
    mDataBinding.webView.loadUrl("https://www.zebra.com/gb/en/solutions/savanna.html");
  }

  @Override
  public void onStop() {
    super.onStop();
    // mDataBinding.promoVideoView.stopPlayback();
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
    // ...
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

  private void initSplashVideo() {
//    // Init Media Controller
//    MediaController mediaController = new MediaController(getActivity());
//    mDataBinding.promoVideoView.setMediaController(mediaController);
//
//    // Init Video to View
//    mDataBinding.promoVideoView
//        .setVideoURI(
//            Uri.parse("android.resource://com.zebra.jamesswinton.savannademowok/"
//                + R.raw.savanna_promo_video));
//
//    // Restart
//    mDataBinding.promoVideoView.setOnPreparedListener(mediaPlayer -> {
//      try {
//        // Stop Player if Playing
//        if (mediaPlayer.isPlaying()) {
//          mediaPlayer.stop();
//          mediaPlayer.release();
//          mediaPlayer = new MediaPlayer();
//        }
//
//        // Reset Volume, set loop, start video
//        mediaPlayer.setVolume(0f, 0f);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
//      } catch (Exception e) {
//        Log.e(TAG, "Exception: " + e.getMessage());
//      }
//    });
  }
}
