package com.zebra.jamesswinton.savannademowok.apis.fdarecall;

import android.Manifest.permission;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.pojos.UPCProduct;
import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.pojos.UPCProduct.UpcItem;
import com.zebra.jamesswinton.savannademowok.apis.fdarecall.pojos.FDARecall;
import com.zebra.jamesswinton.savannademowok.apis.fdarecall.pojos.FDARecall.Meta;
import com.zebra.jamesswinton.savannademowok.apis.fdarecall.pojos.FDARecall.RecallData;
import com.zebra.jamesswinton.savannademowok.databinding.FragmentGetFoodRecallByUpcBinding;
import com.zebra.jamesswinton.savannademowok.home.MainActivity;
import com.zebra.jamesswinton.savannademowok.home.SettingsFragment;
import com.zebra.jamesswinton.savannademowok.network.BarcodeEndPointsApi;
import com.zebra.jamesswinton.savannademowok.network.FdaRecallEndPointsApi;
import com.zebra.jamesswinton.savannademowok.network.RetrofitInstance;
import com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities;
import com.zebra.jamesswinton.savannademowok.scanning.ScannerInterface;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog.DialogType;
import com.zebra.jamesswinton.savannademowok.utilities.PermissionsHelper;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetFoodRecallByUpcFragment extends Fragment {

  // Debugging
  private static final String TAG = "RecallByUpcFragment";

  // Constants


  // Static Variables
  private static boolean mDisclaimerDismissed = false;

  // Variables
  private FragmentGetFoodRecallByUpcBinding mDataBinding;
  private FdaRecallEndPointsApi mFdaRecallEndPointApi;

  public GetFoodRecallByUpcFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_food_recall_by_upc,
        container, false);
    return mDataBinding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();

    // Init Retrofit
    mFdaRecallEndPointApi = RetrofitInstance.getInstance().create(FdaRecallEndPointsApi.class);

    // Set Toolbar
    if (getActivity() != null) {
      getActivity().setTitle("FDA Recall by UPC");
    }

    // Change UI depending on device type
    configureUiZebraCommercial();

    // Init Button Listener
    mDataBinding.searchRecallButton.setOnClickListener(view -> {
      // Validate Data
      if (!validateFormData()) {
        return;
      }

      // Validate Api Key
      if (!validateApiKey()) {
        return;
      }

      // Data Valid -> Execute HTTP Request
      mFdaRecallEndPointApi.getFoodRecallUpc(App.getApiKey(), mDataBinding.barcode.getText().toString(),
          25).enqueue(fdaRecallCallback);
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
   * Networking Methods
   */

  private Callback<FDARecall> fdaRecallCallback = new Callback<FDARecall>() {
    @Override
    public void onResponse(Call<FDARecall> call, Response<FDARecall> response) {
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

      // Check No. of Items
      if (response.body().getMeta() != null && response.body().getMeta().getRecallMeta().getTotal() > 0) {
        // Remove Search View
        mDataBinding.emptyLayout.setVisibility(View.GONE);

        // Show Meta Data
        populateMetaData(response.body().getMeta());

        // Populate Recycler View
        populateRecallData(response.body().getRecallData());
      } else {
        // Show Search View
        mDataBinding.emptyLayout.setVisibility(View.VISIBLE);

        // Get Barcode
        String upc = mDataBinding.barcode.getText().toString();

        // Hide Meta
        mDataBinding.disclaimerLayout.metaBaseLayout.setVisibility(View.GONE);

        // Log Failure
        Log.i(TAG, "No recall data for: " + upc);
        CustomDialog.showCustomDialog(getActivity(), DialogType.WARN,
            "No Recall Found!",
            "There are no outstanding FDA recalls for UPC: " + upc);
      }

    }

    @Override
    public void onFailure(Call<FDARecall> call, Throwable t) {
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
      mDataBinding.barcodeLayout.setLayoutParams(zebraLayoutParams);

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

  private void populateMetaData(Meta meta) {
    // Show Disclaimer View if not dismissed previously
    if (!mDisclaimerDismissed) {
      mDataBinding.disclaimerLayout.metaBaseLayout.setVisibility(View.VISIBLE);
    } else {
      mDataBinding.disclaimerLayout.metaBaseLayout.setVisibility(View.GONE);
    }

    // Populate Description / Last Updated
    mDataBinding.disclaimerLayout.disclaimer.setText(Html.fromHtml("<b>Disclaimer: </b>"
        + meta.getDisclaimer()));
    mDataBinding.disclaimerLayout.lastUpdated.setText("Last Updated: " + meta.getLastUpdated());

    // Dismiss Click Listener
    mDataBinding.disclaimerLayout.dismissMeta.setOnClickListener(view -> {
      mDataBinding.disclaimerLayout.metaBaseLayout.setVisibility(View.GONE);
      mDisclaimerDismissed = true;
    });
  }

  private void populateRecallData(List<RecallData> recallData) {
    // Init Adapter
    GetFoodRecallByUpcAdapter foodRecallUpcAdapter =
        new GetFoodRecallByUpcAdapter(getActivity(), recallData);

    // Set Adapter & Layout Manager
    mDataBinding.recallDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mDataBinding.recallDataRecyclerView.setAdapter(foodRecallUpcAdapter);
  }

  /**
   * Data Validation Method
   */

  private boolean validateFormData() {
    // Validate Barcode
    if (TextUtils.isEmpty(mDataBinding.barcode.getText())) {
      mDataBinding.barcode.setError("Please enter a barcode");
      return false;
    }

    // Barcode present -> return true
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
      mDataBinding.barcode.setText(data);
    }

    @Override
    public void onScanFailure(String error) {
      Log.e(TAG, "Scan Failure");
    }
  };

}
