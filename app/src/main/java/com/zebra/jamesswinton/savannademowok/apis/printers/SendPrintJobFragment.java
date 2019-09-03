package com.zebra.jamesswinton.savannademowok.apis.printers;

import android.Manifest.permission;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.databinding.FragmentSendPrintJobBinding;
import com.zebra.jamesswinton.savannademowok.home.MainActivity;
import com.zebra.jamesswinton.savannademowok.home.SettingsFragment;
import com.zebra.jamesswinton.savannademowok.network.PrinterEndPointsApi;
import com.zebra.jamesswinton.savannademowok.network.RetrofitInstance;
import com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities;
import com.zebra.jamesswinton.savannademowok.scanning.ScannerInterface;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog.DialogType;
import com.zebra.jamesswinton.savannademowok.utilities.PermissionsHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendPrintJobFragment extends Fragment {

  // Debugging
  private static final String TAG = "SendPrintJobFragment";

  // Constants


  // Static Variables


  // Variables
  private FragmentSendPrintJobBinding mDataBinding;
  private PrinterEndPointsApi mPrinterEndPointsApi;

  private String mPrinterSerialNumber;

  public SendPrintJobFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Init DataBinding
    mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_print_job, container,
        false);

    // Get Arguments
    if (getArguments() != null) {
      mPrinterSerialNumber = getArguments().getString(App.ARG_PRINTER_SERIAL);
      mDataBinding.printerSerial.setText(mPrinterSerialNumber);
    }

    return mDataBinding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();

    // Set Title
    getActivity().setTitle("Send Print Job");

    // Init Retrofit
    mPrinterEndPointsApi = RetrofitInstance.getInstanceSandbox().create(PrinterEndPointsApi.class);

    // Change UI depending on device type
    configureUiZebraCommercial();

    // Init Print Click Listener
    mDataBinding.sendPrintJobButton.setOnClickListener(view -> {
      // Validate Data
      if (!validateFormData()) {
        return;
      }

      // Validate Api Key Exists
      if (!validateApiKey()) {
        return;
      }

      // Init Body
      RequestBody mZplToPrint = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"),
          mDataBinding.printData.getText().toString());

      // Get All Printers
      mPrinterEndPointsApi.sendPrintJob(App.getApiKey(), mDataBinding.printerSerial.getText().toString(),
          mZplToPrint).enqueue(sendPrintJobCallback);

      // Show Progress Bar On Activity
      ((MainActivity) getActivity()).showProgressBar(true);
    });
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Init Scanning
    initScanFunctionality();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    // De-init Scanning
    deInitScanFunctionality();
  }

  /**
   * View Population Methods
   */

  private void configureUiZebraCommercial() {
    // IF Zebra Device -> Remove Camera Scanner Button ELSE -> set Click Listener
    if (MainActivity.mZebraDevice) {
      // Remove Scan Button
      mDataBinding.scanWithCameraButton.setVisibility(View.GONE);

      // Re-create layout
      LinearLayout.LayoutParams zebraLayoutParams = new LinearLayout.LayoutParams(0,
          LinearLayout.LayoutParams.WRAP_CONTENT);
      zebraLayoutParams.weight = 9;

      // Apply Layout
      mDataBinding.serialNumberLayout.setLayoutParams(zebraLayoutParams);

    } else {

      // Not Zebra Device -> Init Scan with Camera -> Check Permission
      mDataBinding.scanWithCameraButton.setOnClickListener(view -> {
        if (PermissionsHelper.hasPermission(getActivity(), new String[] {permission.CAMERA},
            "Camera access is required in order to decode barcodes using the Camera")) {
          ((MainActivity) getActivity()).startScan();
        }
      });
    }
  }

  /**
   * Network Methods
   */

  private Callback<Object> sendPrintJobCallback = new Callback<Object>() {
    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
      // Hide Progress Bar
      if (getActivity() != null) {
        ((MainActivity) getActivity()).showProgressBar(false);
      }

      // Validate HTTP Response (200, 404, etc...)
      if (!response.isSuccessful()) {
        Log.e(TAG, "Unsuccessful Response: " + response.code());
        CustomDialog.showCustomDialog(getActivity(), DialogType.ERROR,
            "Unsuccessful Response!",
            "The server returned an unsuccessful response code: <b>" + response.code()
                + "</b> due to: <b>" + response.message() + "</b>");
        return;
      }

      // Validate Body
      if (response.body() == null) {
        Log.e(TAG, "Response Body Was Null!");
        CustomDialog.showCustomDialog(getActivity(), DialogType.ERROR,
            "Unsuccessful Response!",
            "The server returned an empty response body");
        return;
      }

      Log.i(TAG, "Success");

      // Show Success Dialog
      CustomDialog.showCustomDialog(getActivity(), DialogType.SUCCESS, "Success!",
          "Data printed successfully.");
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {
      // Hide Progress Bar
      if (getActivity() != null) {
        ((MainActivity) getActivity()).showProgressBar(false);
      }

      // Log Error
      Log.e(TAG, "onResponse: Unsuccessful - " + t.getMessage(), t);

      // Show dialog
      CustomDialog.showCustomDialog(getActivity(), DialogType.ERROR,
          "HTTP Request Failed!",
          "The server returned an unsuccessful response: " + t.getMessage());
    }
  };

  /**
   * Scan Methods
   */

  private void initScanFunctionality() {
    // Register Scanner Interface
    ((MainActivity) getActivity()).registerScannerInterface(scannerInterface);

    // Enable Profile
    DataWedgeUtilities.enableProfile(getActivity());
  }

  private void deInitScanFunctionality() {
    // Register Scanner Interface
    ((MainActivity) getActivity()).unregisterScannerInterface();

    // Disable DataWedge
    DataWedgeUtilities.disableProfile(getActivity());
  }

  private ScannerInterface scannerInterface = new ScannerInterface() {
    @Override
    public void onScanSuccess(String data) {
      Log.i(TAG, "Scan Success: " + data);

      // Pass Scan Data to TextView
      mDataBinding.printerSerial.setText(data);
    }

    @Override
    public void onScanFailure(String error) {
      Log.e(TAG, "Scan Failure");
    }
  };

  /**
   * Data Validation Method
   */
  private boolean validateFormData() {
    // Check for Serial
    if (TextUtils.isEmpty(mDataBinding.printerSerial.getText())) {
      mDataBinding.printerSerial.setError("Please enter a serial number");
      return false;
    }

    // Check for Print Data
    if (TextUtils.isEmpty(mDataBinding.printData.getText())) {
      mDataBinding.printData.setError("Please enter data to print");
      return false;
    }

    // All other fields are optional -> return true
    return true;
  }

  private boolean validateApiKey() {
    if (TextUtils.isEmpty(App.getApiKey())) {
      CustomDialog.showCustomDialog(getActivity(), DialogType.ERROR, "Api Key Missing!",
          "You have not set an API key in the settings page. You cannot test any API's without an API key",
          "OPEN SETTINGS", (dialogInterface, i) ->
              ((MainActivity) getActivity()).addFragmentToBackStack(new SettingsFragment(),
                  "SETTINGS"),
          "CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());
      return false;
    } return true;
  }
}
