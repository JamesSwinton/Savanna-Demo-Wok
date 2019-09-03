package com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence;

import android.Manifest.permission;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.squareup.picasso.Picasso;
import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.pojos.UPCProduct.UpcItem.UpcItemOffers;
import com.zebra.jamesswinton.savannademowok.home.SettingsFragment;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog.DialogType;
import com.zebra.jamesswinton.savannademowok.home.MainActivity;
import com.zebra.jamesswinton.savannademowok.utilities.PermissionsHelper;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities;
import com.zebra.jamesswinton.savannademowok.scanning.ScannerInterface;
import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.pojos.UPCProduct;
import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.pojos.UPCProduct.UpcItem;
import com.zebra.jamesswinton.savannademowok.databinding.FragmentUpcLookupBinding;
import com.zebra.jamesswinton.savannademowok.network.BarcodeEndPointsApi;
import com.zebra.jamesswinton.savannademowok.network.RetrofitInstance;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcLookupFragment extends Fragment {

  // Debugging
  private static final String TAG = "UpcLookupFragment";

  // Constants


  // Static Variables


  // Variables
  private FragmentUpcLookupBinding mDataBinding;

  private BarcodeEndPointsApi mBarcodeEndPointApi;

  public UpcLookupFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_upc_lookup, container,
        false);
    return mDataBinding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();

    // Init Retrofit
    mBarcodeEndPointApi = RetrofitInstance.getInstance().create(BarcodeEndPointsApi.class);

    // Set Toolbar
    if (getActivity() != null) {
      getActivity().setTitle("UPC Lookup API");
    }

    // Change UI depending on device type
    configureUiZebraCommercial();

    // Init Scrolling
    mDataBinding.productDescription.setMovementMethod(new ScrollingMovementMethod());

    // Init Button Listener
    mDataBinding.createBarcodeButton.setOnClickListener(view -> {
      // Validate Data
      if (!validateFormData()) {
        return;
      }

      // Validate API Key
      if (!validateApiKey()) {
        return;
      }

      // Data Valid -> Execute HTTP Request
      mBarcodeEndPointApi.upcLookup(App.API_KEY, mDataBinding.barcode.getText().toString())
      .enqueue(upcLookupCallback);
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

  private Callback<UpcProduct> upcLookupCallback = new Callback<UpcProduct>() {
    @Override
    public void onResponse(Call<UpcProduct> call, Response<UpcProduct> response) {
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
      if (response.body().getTotal() > 0) {
        // Get Item
        // TODO: Handle Multiple Items (We only handle the first here.
        //  Should there really be more than one anyway?)
        UpcItem upcItem = response.body().getItems().get(0);

        // Remove Search View
        mDataBinding.emptyLayout.setVisibility(View.GONE);

        // Display Response
        populateProductHeader(upcItem);
        populateProductImages(upcItem);
        populateProductOffers(upcItem);
      } else {
        // Show Search View
        mDataBinding.emptyLayout.setVisibility(View.VISIBLE);

        // Show Warning Dialog -> No Product Found
        CustomDialog.showCustomDialog(getContext(), DialogType.WARN, "No Product Found!",
            "Unfortunately we couldn't find any products with that barcode in our database."
                + " Please check the barcode you have entered, or try another");
      }
    }

    @Override
    public void onFailure(Call<UPCProduct> call, Throwable t) {
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

  private void populateProductHeader(UpcItem item) {
    // Get Currency
    String currency;
    if (item.getOffers().isEmpty() || TextUtils.isEmpty(item.getOffers().get(0).getCurrency())) {
      currency = "$";
    } else {
      currency = item.getOffers().get(0).getCurrency();
    }

    // Init Price Format
    DecimalFormat mPriceFormat = new DecimalFormat(currency + "#.#");
    mPriceFormat.setMinimumFractionDigits(2);

    // Populate UI Elements
    mDataBinding.productTitle.setText(item.getTitle());
    mDataBinding.productDescription.setText(item.getDescription());
    mDataBinding.productLowestPrice.setText(mPriceFormat.format(item.getLowestRecordedPrice()));
    if (item.getImages().isEmpty()) {
      Picasso.get()
          .load(R.drawable.ic_missing_image_placeholder)
          .placeholder(R.drawable.ic_loading_image_placeholder)
          .error(R.drawable.ic_missing_image_placeholder)
          .into(mDataBinding.productMainImage);
    } else {
      Picasso.get()
          .load(item.getImages().get(0))
          .placeholder(R.drawable.ic_loading_image_placeholder)
          .error(R.drawable.ic_missing_image_placeholder)
          .into(mDataBinding.productMainImage);
    }
  }

  private void populateProductImages(UpcItem item) {
    // Init Adapter & Layout Manager
    UpcProductImagesAdapter upcProductImagesAdapter =
        new UpcProductImagesAdapter(getContext(), item.getImages());
    LinearLayoutManager horizontalLayoutManager
        = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

    // Set Adapter & Layout Manager
    mDataBinding.imagesRecyclerView.setLayoutManager(horizontalLayoutManager);
    mDataBinding.imagesRecyclerView.setNestedScrollingEnabled(false);
    mDataBinding.imagesRecyclerView.setAdapter(upcProductImagesAdapter);
  }

  private void populateProductOffers(UpcItem item) {
    // Get Offers from Item
    if (item.getOffers() == null && item.getOffers().size() == 0) {
      Log.e(TAG, "No Offers Found...");
      return;
    }

    // Init Adapter & Layout Manager
    UpcProductOffersAdapter upcProductOffersAdapter =
        new UpcProductOffersAdapter(getContext(), item.getOffers());

    // Set Adapter & Layout Manager
    mDataBinding.offersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mDataBinding.offersRecyclerView.setAdapter(upcProductOffersAdapter);
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
