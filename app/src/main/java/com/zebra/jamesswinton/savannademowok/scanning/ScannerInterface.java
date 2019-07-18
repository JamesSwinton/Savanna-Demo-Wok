package com.zebra.jamesswinton.savannademowok.scanning;

public interface ScannerInterface {
  void onScanSuccess(String data);
  void onScanFailure(String error);
}
