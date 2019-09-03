package com.zebra.jamesswinton.savannademowok.apis.printers;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.databinding.FragmentGetAllPrintersBinding;
import com.zebra.jamesswinton.savannademowok.home.MainActivity;
import com.zebra.jamesswinton.savannademowok.home.SettingsFragment;
import com.zebra.jamesswinton.savannademowok.network.PrinterEndPointsApi;
import com.zebra.jamesswinton.savannademowok.network.RetrofitInstance;
import com.zebra.jamesswinton.savannademowok.apis.printers.pojos.AllPrinters;
import com.zebra.jamesswinton.savannademowok.apis.printers.pojos.AllPrinters.Printer;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog.DialogType;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllPrintersFragment extends Fragment {

  // Debugging
  private static final String TAG = "GetAllPrintersFragment";
  
  // Constants
  
  
  // Static Variables
  
  
  // Variables
  private FragmentGetAllPrintersBinding mDataBinding;

  private PrinterEndPointsApi mPrinterEndPointsApi;

  public GetAllPrintersFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_all_printers, container,
        false);
    return mDataBinding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();

    // Set Title
    getActivity().setTitle("Get All Printers");

    // Init Retrofit
    mPrinterEndPointsApi = RetrofitInstance.getInstanceSandbox().create(PrinterEndPointsApi.class);

    // Init Swipe to Refresh
    mDataBinding.printerDetailsRefreshView.setOnRefreshListener(() -> {
      // Disallow Refresh (MainActivity progress bar is more inline with design)
      mDataBinding.printerDetailsRefreshView.setRefreshing(false);

      // Execute Call
      mPrinterEndPointsApi.getAllPrinters(App.getApiKey(), null, null, null,
          null, null).enqueue(getAllPrintersCallback);

      // Show Progress Bar On Activity
      ((MainActivity) getActivity()).showProgressBar(true);
    });

    // Init First Call with Refresh Anim
    if (validateApiKey()) {
      // Fetching data from server
      mPrinterEndPointsApi.getAllPrinters(App.getApiKey(), null, null, null,
          null, null).enqueue(getAllPrintersCallback);

      // Show Progress Bar On Activity
      ((MainActivity) getActivity()).showProgressBar(true);
    }
  }

  private Callback<AllPrinters> getAllPrintersCallback = new Callback<AllPrinters>() {
    @Override
    public void onResponse(Call<AllPrinters> call, Response<AllPrinters> response) {
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

      // Show Printers
      if (response.body().totalCount > 0) {

        // Remove Search View
        mDataBinding.emptyLayout.setVisibility(View.GONE);

        // Populate Rrcycler View
        populatePrinterRecyclerView(response.body());
      } else {

        // Show Search View
        mDataBinding.emptyLayout.setVisibility(View.GONE);
      }

    }

    @Override
    public void onFailure(Call<AllPrinters> call, Throwable t) {
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

  private void populatePrinterRecyclerView(AllPrinters allPrinters) {
    // Get Devices List
    List<Printer> printers = allPrinters.getDevices();

    // Init Adapter & Layout Manager
    GetAllPrintersAdapter getAllPrintersAdapter = new GetAllPrintersAdapter(getActivity(), printers);

    // Set Adapter & Layout Manager
    mDataBinding.printerDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mDataBinding.printerDetailsRecyclerView.setAdapter(getAllPrintersAdapter);
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
