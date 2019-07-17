package com.zebra.jamesswinton.savannademowok.barcodeintelligence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.CustomDialog;
import com.zebra.jamesswinton.savannademowok.CustomDialog.DialogType;
import com.zebra.jamesswinton.savannademowok.MainActivity;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.databinding.FragmentCreateBarcodeBinding;
import com.zebra.jamesswinton.savannademowok.network.BarcodeEndPointsApi;
import com.zebra.jamesswinton.savannademowok.network.RetrofitInstance;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBarcodeFragment extends Fragment {

  // Debugging
  private static final String TAG = "CreateBarcodeFragment";

  // Constants


  // Static Variables


  // Variables
  private FragmentCreateBarcodeBinding mDataBinding;
  private BarcodeEndPointsApi mBarcodeEndPointApi;

  private String mSymbology = null;
  private String mRotate = "N"; // Default N = Normal rotation
  private Integer mScale = 1; // Default 1 = Default Scale
  private String mShowExtraBarcodeData = null;

  public CreateBarcodeFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_barcode, container,
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
      getActivity().setTitle("Create Barcode API");
    }

    // Init Spinners (Adapter & Listener)
    initSymbologySpinner();
    initRotateSpinner();
    initScaleSpinner();

    // Init Checkbox
    initShowDataUnderBarcodeCheckBox();

    // Init Button Listener
    mDataBinding.createBarcodeButton.setOnClickListener(view -> {
      // Validate Data
      if (!validateFormData()) {
        return;
      }

      // Data Valid -> Execute HTTP Request
      mBarcodeEndPointApi.createBarcode(App.API_KEY,
          mSymbology, Objects.requireNonNull(mDataBinding.barcodeData.getText()).toString(),
          null, null, mScale, mRotate, mShowExtraBarcodeData)
          .enqueue(createBarcodeCallback);

      // Show Progress Bar On Activity
      ((MainActivity) getActivity()).showProgressBar(true);
    });
  }

  /**
   * Networking Methods
   */

  private Callback<ResponseBody> createBarcodeCallback = new Callback<ResponseBody>() {
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

      // Convert Response to Image
      convertResponseToImage(response.body());
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
      // Hide Progress Bar
      if (getActivity() != null) {
        ((MainActivity) getActivity()).showProgressBar(false);
      }

      // Log Error
      Log.e(TAG, "onResponse: Unsuccessful - " + t.getMessage(), t);
    }
  };

  private void convertResponseToImage(ResponseBody body) {
    try {
      // Convert ResponseBody to byte[]
      byte[] imageBytes = getBytesFromInputStream(body.byteStream());
      // Convert Byte[] to Bitmap
      Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
      // Display Bitmap in ImageView
      mDataBinding.barcodeImageView.setImageBitmap(bmp);
    } catch (IOException e) {
      Log.e(TAG, "IOException: " + e.getMessage());
    }
  }

  private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException, OutOfMemoryError {
    int bytesRead;
    byte[] buffer = new byte[8192];
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      output.write(buffer, 0, bytesRead);
    }
    return output.toByteArray();
  }

  /**
   * UI Initialisation Methods
   */

  private void initSymbologySpinner() {
    // Build Adapter
    ArrayAdapter<String> symbologySpinnerAdapter = new ArrayAdapter<>(
        getContext(),
        R.layout.material_drop_down,
        getResources().getStringArray(R.array.symbologies_array));

    // Set to Spinner (AutoCompleteTextView)
    mDataBinding.symbologySpinner.setAdapter(symbologySpinnerAdapter);

    // Init Selected Listener
    mDataBinding.symbologySpinner.setOnItemClickListener((adapterView, view, i, l) ->  {
      // Update Variable
      mSymbology = symbologySpinnerAdapter.getItem(i);
      // Remove Error
      mDataBinding.symbologySpinner.setError(null);
    });
  }

  private void initRotateSpinner() {
    // Build Adapter
    ArrayAdapter<String> rotateSpinnerAdapter = new ArrayAdapter<>(
        getContext(),
        R.layout.material_drop_down,
        getResources().getStringArray(R.array.rotate_array));

    // Set Default Selection
    mDataBinding.rotateSpinner.setText(rotateSpinnerAdapter.getItem(0));

    // Set to Spinner (AutoCompleteTextView)
    mDataBinding.rotateSpinner.setAdapter(rotateSpinnerAdapter);

    // Init Selected Listener
    mDataBinding.rotateSpinner.setOnItemClickListener((adapterView, view, i, l) -> {
      switch (rotateSpinnerAdapter.getItem(i)) {
        case "0°":
          mRotate = "N";
          break;
        case "90°":
          mRotate = "R";
          break;
        case "180°":
          mRotate = "I";
          break;
        case "270°":
          mRotate = "L";
          break;
      }
    });
  }

  private void initScaleSpinner() {
    // Build Adapter
    ArrayAdapter<String> scaleSpinnerAdapter = new ArrayAdapter<>(
        getContext(),
        R.layout.material_drop_down,
        getResources().getStringArray(R.array.scale_array));

    // Set Default Selection
    mDataBinding.scaleSpinner.setText(scaleSpinnerAdapter.getItem(0));

    // Set to Spinner (AutoCompleteTextView)
    mDataBinding.scaleSpinner.setAdapter(scaleSpinnerAdapter);

    // Init Selected Listener
    mDataBinding.scaleSpinner.setOnItemClickListener((adapterView, view, i, l) -> {
      mScale = Integer.parseInt((String) adapterView.getItemAtPosition(i));
    });
  }

  private void initShowDataUnderBarcodeCheckBox() {
    mDataBinding.showExtraDataCheckbox.setOnCheckedChangeListener(
        (compoundButton, checked) -> mShowExtraBarcodeData = checked ? "showData" : null);
  }

  /**
   * Data Validation Method
   */
  private boolean validateFormData() {
    // Check Symbology
    if (mSymbology == null) {
      mDataBinding.symbologySpinner.setError("Please choose a symbology");
      return false;
    }

    // Check for Barcode Data
    if (TextUtils.isEmpty(mDataBinding.barcodeData.getText())) {
      mDataBinding.barcodeData.setError("Please enter barcode data");
      return false;
    }

    // All other fields are optional -> return true
    return true;
  }
}
