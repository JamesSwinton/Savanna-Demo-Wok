package com.zebra.jamesswinton.savannademowok.apis.printers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.apis.printers.pojos.PrinterDetails;
import com.zebra.jamesswinton.savannademowok.databinding.FragmentGetPrinterDetailsBinding;
import com.zebra.jamesswinton.savannademowok.home.MainActivity;
import com.zebra.jamesswinton.savannademowok.home.SettingsFragment;
import com.zebra.jamesswinton.savannademowok.network.PrinterEndPointsApi;
import com.zebra.jamesswinton.savannademowok.network.RetrofitInstance;
import com.zebra.jamesswinton.savannademowok.scanning.DataWedgeUtilities;
import com.zebra.jamesswinton.savannademowok.scanning.ScannerInterface;
import com.zebra.jamesswinton.savannademowok.utilities.CustomDialog;
import com.zebra.jamesswinton.savannademowok.utilities.PermissionsHelper;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPrinterDetailsFragment extends Fragment {

    // Debugging
    private static final String TAG = "GetPrinterDeetsFragment";

    // Constants


    // Private Variables


    // Public Variables
    private FragmentGetPrinterDetailsBinding mDataBinding;
    private PrinterEndPointsApi mPrinterEndPointsApi;

    public GetPrinterDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_printer_details, container,
                false);

        // Get Arguments
        if (getArguments() != null) {
            mDataBinding.printerSerial.setText(getArguments().getString(App.ARG_PRINTER_SERIAL));
        }

        return mDataBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set Toolbar
        if (getActivity() != null) {
            getActivity().setTitle("Get Printer Details");
        }

        // Init Retrofit
        mPrinterEndPointsApi = RetrofitInstance.getInstanceSandbox().create(PrinterEndPointsApi.class);

        // Change UI depending on device type
        configureUiZebraCommercial();

        // Init Button Listener
        mDataBinding.getPrinterDetailsButton.setOnClickListener(view -> {
            // Validate Data
            if (!validateFormData()) {
                return;
            }

            // Validate Api Key
            if (!validateApiKey()) {
                return;
            }

            // Execute Call
            mPrinterEndPointsApi.getPrinterDetilas(App.getApiKey(),
                    mDataBinding.printerSerial.getText().toString())
                    .enqueue(getPrinterDetailsCallback);

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
     * Retrofit Methods
     */

    private Callback<PrinterDetails> getPrinterDetailsCallback = new Callback<PrinterDetails>() {
        @Override
        public void onResponse(Call<PrinterDetails> call, Response<PrinterDetails> response) {
            // Hide Progress Bar
            if (getActivity() != null) {
                ((MainActivity) getActivity()).showProgressBar(false);
            }

            // Validate HTTP Response (200, 404, etc...)
            if (!response.isSuccessful()) {
                Log.e(TAG, "Unsuccessful Response: " + response.code());
                CustomDialog.showCustomDialog(getActivity(), CustomDialog.DialogType.ERROR,
                        "Unsuccessful Response!",
                        "The server returned an unsuccessful response code: <b>" + response.code()
                                + "</b> due to: <b>" + response.message() + "</b>");
                return;
            }

            // Validate Body
            if (response.body() == null) {
                Log.e(TAG, "Response Body Was Null!");
                CustomDialog.showCustomDialog(getActivity(), CustomDialog.DialogType.ERROR,
                        "Unsuccessful Response!",
                        "The server returned an empty response body");
                return;
            }

            // Populate UI Fields with Data
            populateUiFields(response.body());

            // Init Floating Action Button
            if (response.body().getReadiness().equalsIgnoreCase("ONLINE")) {
                mDataBinding.sendPrintJobFab.setVisibility(View.VISIBLE);
                mDataBinding.sendPrintJobFab.setOnClickListener(view -> {
                    // Build Body
                    RequestBody mZplToPrint = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), buildZplFromDetails(response.body()));

                    // Get All Printers
                    mPrinterEndPointsApi.sendPrintJob(App.getApiKey(), mDataBinding.printerSerial.getText().toString(), mZplToPrint).enqueue(sendPrintJobCallback);

                    // Show Progress Bar On Activity
                    ((MainActivity) getActivity()).showProgressBar(true);
                });
            }
        }

        @Override
        public void onFailure(Call<PrinterDetails> call, Throwable t) {
            // Hide Progress Bar
            if (getActivity() != null) {
                ((MainActivity) getActivity()).showProgressBar(false);
            }

            // Log Error
            Log.e(TAG, "onResponse: Unsuccessful - " + t.getMessage(), t);

            // Show dialog
            CustomDialog.showCustomDialog(getActivity(), CustomDialog.DialogType.ERROR,
                    "HTTP Request Failed!",
                    "The server returned an unsuccessful response: " + t.getMessage());
        }
    };

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
                CustomDialog.showCustomDialog(getActivity(), CustomDialog.DialogType.ERROR,
                        "Unsuccessful Response!",
                        "The server returned an unsuccessful response code: <b>" + response.code()
                                + "</b> due to: <b>" + response.message() + "</b>");
                return;
            }

            // Validate Body
            if (response.body() == null) {
                Log.e(TAG, "Response Body Was Null!");
                CustomDialog.showCustomDialog(getActivity(), CustomDialog.DialogType.ERROR,
                        "Unsuccessful Response!",
                        "The server returned an empty response body");
                return;
            }

            Log.i(TAG, "Success");

            // Show Success Dialog
            CustomDialog.showCustomDialog(getActivity(), CustomDialog.DialogType.SUCCESS, "Success!",
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
            CustomDialog.showCustomDialog(getActivity(), CustomDialog.DialogType.ERROR,
                    "HTTP Request Failed!",
                    "The server returned an unsuccessful response: " + t.getMessage());
        }
    };

    /**
     * Validation Methods
     */

    private boolean validateFormData() {
        // Validate Barcode
        if (TextUtils.isEmpty(mDataBinding.printerSerial.getText())) {
            mDataBinding.printerSerial.setError("Please enter a serial number");
            return false;
        }

        // Clear Errors
        mDataBinding.printerSerial.setError(null);

        // Barcode present -> return true
        return true;
    }

    private boolean validateApiKey() {
        if (TextUtils.isEmpty(App.getApiKey())) {
            CustomDialog.showCustomDialog(getActivity(), CustomDialog.DialogType.ERROR, "Api Key Missing!",
                    "You have not set an API key in the settings page. You cannot test any API's without an API key",
                    "OPEN SETTINGS", (dialogInterface, i) ->
                            ((MainActivity) getActivity()).addFragmentToBackStack(new SettingsFragment(),
                                    "SETTINGS"),
                    "CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());
            return false;
        } return true;
    }

    /**
     * Scanning Methods
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
                if (PermissionsHelper.hasPermission(getActivity(), new String[] {Manifest.permission.CAMERA},
                        "Camera access is required in order to decode barcodes using the Camera")) {
                    ((MainActivity) getActivity()).startScan();
                }
            });
        }
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
            mDataBinding.printerSerial.setText(data);
        }

        @Override
        public void onScanFailure(String error) {
            Log.e(TAG, "Scan Failure");
        }
    };

    /**
     * UI Populate Methods
     */

    @SuppressLint("SetTextI18n")
    private void populateUiFields(PrinterDetails printerDetails) {
        // Make Layouts Visible
        mDataBinding.emptyLayout.setVisibility(View.GONE);
        mDataBinding.detailsLayout.setVisibility(View.VISIBLE);

        // Set Status & Image
        mDataBinding.printerImage.setImageResource(getPrinterImage(
                printerDetails.getDeviceModel().toUpperCase()));
        mDataBinding.printerStatus.setImageResource(printerDetails.getReadiness()
                .equalsIgnoreCase("ONLINE")
                ? R.drawable.ic_online : R.drawable.ic_offline);

        // Populate Printer Details
        mDataBinding.printerModel.setText(printerDetails.getDeviceModel());
        mDataBinding.printerName.setText(printerDetails.getPrinterName());
        mDataBinding.macAddress.setText(printerDetails.getSettings().getBluetoothAddress());
        mDataBinding.ipAddress.setText(printerDetails.getIpAddress());
        mDataBinding.linkOs.setText(printerDetails.getLinkOSVersion());
        mDataBinding.firmwareVersion.setText(printerDetails.getFirmwareVersion());
        mDataBinding.dotsPerMin.setText(String.valueOf(printerDetails.getDotsPerMm()));
        mDataBinding.manufactureDate.setText(printerDetails.getManufactureDate());

        // Populate Printer Settings
        PrinterDetails.Settings printerSettings = printerDetails.getSettings();
        mDataBinding.printerAvailableRam.setText(printerSettings.getMemoryRamFreeMb() +
                "MB (" + printerSettings.getMemoryRamFreePercentage()  + ")");
        mDataBinding.printerUptime.setText(printerSettings.deviceUptime);
        mDataBinding.printerRebootReason.setText(printerSettings.getLogRebootReason());
        mDataBinding.printerBatteryLevel.setText(printerSettings.getPowerPercentFull());
        mDataBinding.printerAvailableFlash.setText(printerSettings.getMemoryFlashFreeMb() +
                "MB (" + printerSettings.getMemoryFlashFreePercentage() + ")");

        // Populate Odometer Settings
        mDataBinding.printerTotalLabelCount.setText(printerSettings.getOdometerTotalLabelCount());
        mDataBinding.printerTotalCountType.setText("Integer");
        mDataBinding.printerCurrentUserLabelCount.setText(printerSettings.getOdometerUserLabelCount());
        mDataBinding.printerCurrentUserCountType.setText("Integer");
    }

    private int getPrinterImage(String printerModel) {
        // ZT Models
        if (printerModel.startsWith("ZT2")) {
            return R.drawable.printer_zt200;
        }

        if (printerModel.startsWith("ZT4")) {
            return R.drawable.printer_zt400;
        }

        if (printerModel.startsWith("ZT5")) {
            return R.drawable.printer_zt500;
        }

        if (printerModel.startsWith("ZT6")) {
            return R.drawable.printer_zt600;
        }

        // ZD Models
        if (printerModel.startsWith("ZD4")) {
            return R.drawable.printer_zd400;
        }

        if (printerModel.startsWith("ZD5")) {
            return R.drawable.printer_zd500;
        }

        if (printerModel.startsWith("ZD6")) {
            return R.drawable.printer_zd600;
        }

        // QLN - iMZ
        if (printerModel.startsWith("QLN")) {
            return R.drawable.printer_qln;
        }

        if (printerModel.startsWith("IMZ")) {
            return R.drawable.printer_qln;
        }

        // ZQ Models
        if (printerModel.startsWith("ZQ3")) {
            return R.drawable.printer_zq300;
        }

        if (printerModel.startsWith("ZQ5")) {
            return R.drawable.printer_zq500;
        }

        if (printerModel.startsWith("ZQ6")) {
            return R.drawable.printer_zq600;
        }

        return R.drawable.ic_printer;
    }

    private String buildZplFromDetails(PrinterDetails printerDetails) {
        return  "CT~~CD,~CC^~CT~\n" +
                "^XA~TA000~JSN^LT0^MNN^MTD^PON^PMN^LH0,0^JMA^PR4,4~SD10^JUS^LRN^CI0^XZ\n" +
                "^XA\n" +
                "^MMT\n" +
                "^PW561\n" +
                "^LL1519\n" +
                "^LS0\n" +
                "^FO0,0^GFA,15232,15232,00068,:Z64:\n" +
                "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:B54F\n" +
                "^FO0,256^GFA,01024,01024,00016,:Z64:\n" +
                "eJxjYBj2gPEP+wcgZQNGxPD/8aPy3/Fh8Pkb+G3YHyD49g/s//B/QPDrP9T/kf8B5T+X+fDjR0WNjQ2C/6HiR02FDJz/8YHNB5sKOSj/sQSYX/8Pxt9BFL8C5r6HGyDmw/kfIPZLIPgg99nD9D94AHa//B8EH+w/qH9GwTAGAKVDYxE=:ECDC\n" +
                "^FO0,352^GFA,01024,01024,00016,:Z64:\n" +
                "eJzt0DsOwjAMgOFUGTpmZbIvUoWLIRQpQ8bcgLP0JFERA1vpyBDZNDREfQwssPXfvsGWbCH+XtXB0sPKzy+OG9cDnLSI2QQYgbii7B6QkNly9gMajeidM5PvlwOAcl4WCw2q9rKdfAuSkjn7GlRYmADm7qh5u9y79lmn/c4WH9N+V+Y19qMtm4+VH21o88q9vd/2AiWNRBI=:12B0\n" +
                "^FO0,416^GFA,01792,01792,00028,:Z64:\n" +
                "eJzt0S1uAzEQBeCxDMxqWlDZF3F3r7UgVbwKyC16hsJCRwWFvcKsFoRVEy0JWHk62Tg/akJbtA9Z+sjzG4A5c+b8cdRoyCbIAD6CA3tjqDmCT/fNcLpn2ZJHywj1rX0/kCO/QnjG++YUiZlX/WWbGpaHasUqqiDBC/ps2dKy5Q+pViw0QV4N1dmLiWyl2sG2T2cLlfO27frNegUnW0y2p0VVeatTn0w8WxMORk3jghUbyODR+sdfxrusx2LvxGOApHhfjLjli+VKkeLxZHVaxou5K0tg0B7/0L3tyPk1agylJyjUxWiHVrbWyU3/U5g23erK5EZi0y6KB/4smyEOKAWiSW7aE7g77ykmO2Qw0U53gDqe7jDnn/ID5PnBNg==:BB04\n" +
                "^FO0,512^GFA,01536,01536,00024,:Z64:\n" +
                "eJzt0DFOxDAQBdCxUricG8RX2HILi1wFbuECyZG2cJkrDco90FDRDqIxksXgSKwTIEj05MvVK+zvD3DkyJ9inpTB1jN29M0FrOx5/sUL4J7zPWDYc1/dPI+v8yOSo2Fc3QWrF52L4yi1Xrun96ip+iD6Vuu1d6tjSjOHIHdMa88b73BKM8lZDG/+5fveTl11X0xuTs21xFFbT4D4fvWBI21cr47i+KcLmNzJ6hk/+wAE2LjYaelP4XQKt199+S/Foi/rDmDEpmUfGvJ2HzDcPSx7EnLb88g/zAdpTKta:F1B8\n" +
                "^FO0,608^GFA,02048,02048,00032,:Z64:\n" +
                "eJzt0rFOwzAQBuCzTsIb9wJV+hoMEXklxkMEGpQH6Cs58tDXcOShY111wEPVw05LS0qZGcg/WFE+OTnfGWDKP49yEAAYMELVKIcmvzyuI6cDSHrUzS8+FyXhtkP2StqLj/8/+BtZF4HpVn07xzWjNZxcReIFmke1Nmf/CBwj+vQlJtzTVqyR9407u0QOQfk9QFnoA+3Ed9Klar48xmcXVC8mORG1XWt9v7144BcTlEs7yqLQhAZt17uRN8mrCLNZoZcYcNVsR77I+1OLi3IuS3W47VAfHaT94S7iuinh/iF7Za/91dV6ZUogLoBA+2vnkPtbn1z5b/VLHDzPJ4J+ojuCzqfzj53zfJMHkiWKTf0be5nvRwB0WkgN/b/yfL9McrUnGOZ38ilTpvxRPgEz9/nt:2DDE\n" +
                "^FO0,704^GFA,02304,02304,00036,:Z64:\n" +
                "eJzt0TFqwzAUBuBnDNX4rx0a6xodlPhKGTUE4uDBS6lukF6jS6igg8dcwblAoi0ajF4VN+CkNi109r8IPT6J9ySiKX8kYe/uCqJAt+IXk9rsn4ZGzW0/zaCgRow7b1eLDOWnh84vRjTzQwvy4q03nAWOZh/g1kU0cHnNSIP4KHojmV9RGoZjS05Ln+/ZCBZx992zc15KY2S9ldgc4hGt1AIPgKnq3rgMVTQvQGovRj8pECCq8s6IalaTjMbRcqkzBYssrX+a3SmJfbIj7dZBCX9r4uVXI069ScOIKXdHoDPPTTTEY+b9mF16Jpo3mhRJM2J8uDPC3Mx1NRvfSomksTSz6lGl0ZiBcSvJiFOStHlQYMR3HhqcQdwQCtkq0f3X0IjaUF5EI5xKun+/milTpkyZEvMFyy/Zcw==:0956\n" +
                "^FO0,768^GFA,01536,01536,00024,:Z64:\n" +
                "eJzt0DsOwjAMgGFXkfAEXhmguUYH1FyJjZFIDBk5AkehW68RJsZ27BA1OIW+yw36D5H1KVIiA6ytrS0UectHGDKyyujeKwBccgdAPAj918trxxDZtPPzZehxVKqX+7oxj84lOvX2/HbjT906Ua0Kr3/us86lJNqE+8nbmLz3mIig9VvvJ7xTBrslxyr4Pp+6qIMfRy7Zwc89Di71Yeop/wcw+NYM3Dv+v2icxk41FR5mXqFD3k8ChDh0G5WC9znxNe4DyD13QA==:E2A0\n" +
                "^FO0,864^GFA,02304,02304,00036,:Z64:\n" +
                "eJzt0rFqwzAQBmC5HrSU3JqhSC8i4lfpI2T0EGqFDF4KfoP2FTp2yBDokDFvUBQyZEvPdIgHI/UcWbHTtHMp+B+EOD7E6STGhgwZ8seJao5+Bytabtqyo2J0bWRjRsHUjPFrkzRGBGPp4LOx0BrFfjWH0U8G5TeT6E+TKpdjjNmG50upGUJcJabujDNHJPNc87oxFXWGUNhka3sGXdUYC/ZkMsOQFy7Zudbs78otVmSUmggpgOcrRDJPkrY6GKRiSmYsxG1j5mZK5hEA4s5E3rCJYt6kZLQks/JmN0bpTWxb4yoycwfggnnFB2/gvW/yjwuD3gjRM1BA35TGGxWMoTmDIBPmvH3pTLhX8xYXBsuSzGymJqf5FLnGe3pTSfNZdMaRyWqV0JzlxuWa+iHjYB/6MQbdFNNkqeSB17A+5jpD+mNw5Dunz2aWYirp7kVU8bd9rulHOuTrhbFsyJD/ky92+fTH:473A\n" +
                "^FO0,192^GFA,01792,01792,00028,:Z64:\n" +
                "eJzt0jFqxDAQBVAtU6jUEXyRkHMZskQyKlT6CLmKzBQq9wbBsBewcZGFOJqMLCX2ZtOFwBb+jWSe0czIFmLPnj13mgP1QvFq0oO+LDtAX2xIBtnislvtkkxmo2W32pyWKxNQrD+uVitzbQ9PJ/VYDc/wKmpp5mqIfGYsplulq4FgZutiNZFFJJPPZKOXkSwbILEhBvK5l6NSreN3e7bgOGy2zFArJZ1ruPEaTuAssJkye7FztmAkW5/NZ6NsFIxGxDn3KX5Y847Y0a1JNu4KvTZbM5CsFTK17JW/MfVlsDXbWc93La10gevZrU2pHhuFt+VeNvXsSDy7jkB2+sDv+1ysGTSX4H8iHsbxXL7Df0TS7+n/YHeTT5QNHLY=:A790\n" +
                "^FO0,992^GFA,02048,02048,00032,:Z64:\n" +
                "eJzt0TFuwyAUBuBnIZWRG4SLRPVVeoxItQKRh4y+QXMVIh8gN2iNPHTFysJg+fVBTRInTTu0Uhf/CzKfxeM9AObMmfOLZNiAoFWHD+5oA0ozcRecRZc9/cOu3Afn0fMBQPAr7+PJ0VV0PanfFGcv5Be+fD6IR+nW7BVWIvM5HuuDXrpcJ1eVUNIh66EA1gd/M4OjvsbzyXHXYdmD0nwIjhY76mu8XyFEtS3ruoHcC0H3r1tru8ak/la0t91uagNyWPDg+5b8NJ/RWwMcF7yKbjqf3Hw6koOSOLrD1B9cuNidXJlbd/zsspm6ZgY8fwExOnO3Xojk1sHUyz09m0LxEN227mnqx1A/R4HV+5Gc5jepX3YY58ORt+TNxfyjb5yi+txnPbPBi/R+9+K+1Uz/4DY5x3vxf+Jz/jEfcKw3CQ==:F67A\n" +
                "^FO0,1056^GFA,01792,01792,00028,:Z64:\n" +
                "eJzt0T1OxDAQBWBHKdLhliKKr5FiSK7CAShSzioBB23BlbagcMVewycAb2eJiPAmYc2PBD1SnjRRpE8ZjydK/Z+YUZ7MKEV4a99Uq2w2LeZ+2Kza8Vcbk7WLqS/m7Gj3i13570aF612/f1yNyThm43aeCq8o/7RbmA3MNthIOoi9no0DLMKinchE6Xnyyeo6wEIcCJE5T353lKNVDMTXzKihIS5h98nQkhW+Q1XEFSzzIVnZaZgWs1jYBOMnsWyOZdeiJ2oxavI58PNqU9XxmtVMPsdkF+iBOcWwHdLJck9nkznFHrClm8VcpX3ELDHe4XqXuPtxJB4+rHBowrPsBT+W8mSFmD1gZ4c+kplgL7B6sVEXDgdxQ/IfsOtsVtVqW7Zs2fJn3gEQuMsk:023A\n" +
                "^FO0,1120^GFA,01024,01024,00016,:Z64:\n" +
                "eJzt0DEOwjAMBdAgBo+5gXOUXIsB0UgMGXsEjtIwcQ1HGbp27IBqktYIJRMDEhLiD5be8C3LSv3z49kR5gGuMjTWrZt+bTza0V+0s2KzZA9D6JyYs5mJg1hr8LcUIz3dgz/HFMQIxdcUptr8lqdXf71nPm37xYrvptfFkYKY9Vi89lU34wLFxOUeZQgP+9VWnoLNSz/t7+UB5h1piA==:6BF2\n" +
                "^FO0,1216^GFA,02048,02048,00032,:Z64:\n" +
                "eJzt0TFuwzAMQFEaGrwE4ZqJvIgg91gejCweMvoGPYuBDh17g0JF9yIjB0OqRDey4F6gKEwv+XhGIFMAxxzzp6eJdwBafz9p7F0e3swae18ebmaNvYfawy/3rvIceydzpx4EBhSNNj3G01C8FQ4mNFE9xcILCoXy/7hwbKOJk6wRupDeieX8RIw43V5fZg3r7MU55uL2zDi1t1Hdni9kwRJh2Y9Fap/NW/M5a4DroMfNfRc3T2EC+8o/7pXnwHeUyr1UrkFmqb23sHmfzwbX2gcL5fw5klN1fu/oxNPP9+dw7kTObU4c1/2NotGFLm14c8ZAuv9GNPhL918cR1nvD0QjnSXfH+5v+phj/tl8AxgBsz8=:34ED\n" +
                "^FO0,1312^GFA,01792,01792,00028,:Z64:\n" +
                "eJzt0D1OxDAQBeAxQmzHXADZV6BfK74KR2C7oI0UIgrKPQIXoUi0xZZcgGIiLuCIxoW1QxwS7w9KRIWElNdY9leM3wDM+cMIJoDLi/5CJ6dgC3BzPWIOQMsR8+NG2YRpJObK4bpM2zdVkDUHM4556/GzM1ORzYvBUuOZd3tsyLaW12S5iv9MMy0RsejMWqrrOva71VrKBYpggu6bjzKagyzYBhoKlrbWxH4Lr6Xi3tittjwYJer121blD5Nan9jDsaXhL4CwDkawfCpj92Sw5eO58T4L/a46Y2fvTEGxu8+9eXlr56lg7JSI89iZd7XZMYIKl9wq4MNelMTnyvemSgWm3/VR5PnDLy0ZJ+EnzE2YnZg3Z87/yRcpcsab:493E\n" +
                "^FO0,1376^GFA,02048,02048,00032,:Z64:\n" +
                "eJzt0bFOwzAQBmCjDlkQXjNU9mt0uNqvwiN0PERp+ga8Tsc8CfITII83WAn/mSQtSEwMDOSPLoryKTn7bMyaNWvW/F38We/MKEN4iqPTF7R4/zuP1c2Pvk9fncx3Z/I9s++fEjVJvclNJpn8Bd5l5i53Qjar++LLvkzOGS5w6Qp5qT5EXIvvdhme5UiIiYOjQG0Ik0smfmRGHQPxVr11ZMhNjt+zwfcoR+zUTYi3vj1YuFXvBvXN4NPsd6NsDxH/R1XH+uyblasXd+DPzO7cptz4Q8L2sU3tX+cHO82+STS7rn92t3jvbBKsT+SE7bfqIdzfetOP8FHnF2t/TG/p36h3F8z38izkS53vu7/62TY9GnMgPb96PvYV5zfP5z/nA1Dzq2g=:2816\n" +
                "^FT29,330^AAN,27,15^FH\\^FD" + printerDetails.getDeviceModel() + "^FS\n" +
                "^FT29,418^AAN,27,15^FH\\^FD" + printerDetails.getPrinterName() + "^FS\n" +
                "^FT29,507^AAN,27,15^FH\\^FD" + printerDetails.getSettings().getBluetoothAddress() + "^FS\n" +
                "^FT29,595^AAN,27,15^FH\\^FD" + printerDetails.getIpAddress() + "^FS\n" +
                "^FT29,683^AAN,27,15^FH\\^FD" + printerDetails.getLinkOSVersion() + "^FS\n" +
                "^FT29,772^AAN,27,15^FH\\^FD" + printerDetails.getFirmwareVersion() + "^FS\n" +
                "^FT29,860^AAN,27,15^FH\\^FD" + printerDetails.getDotsPerMm() + "^FS\n" +
                "^FT29,949^AAN,27,15^FH\\^FD" + printerDetails.getManufactureDate() + "^FS\n" +
                "^FT29,1121^AAN,27,15^FH\\^FD" + printerDetails.getSettings().getMemoryRamFreeMb() + "MB (" + printerDetails.getSettings().getMemoryRamFreePercentage() + ")" + "^FS\n" +
                "^FT29,1209^AAN,27,15^FH\\^FD" + printerDetails.getSettings().getDeviceUptime() + "^FS\n" +
                "^FT29,1296^AAN,27,15^FH\\^FD" + printerDetails.getSettings().getLogRebootReason() + "^FS\n" +
                "^FT29,1383^AAN,27,15^FH\\^FD" + printerDetails.getSettings().getPowerPercentFull() + "^FS\n" +
                "^FT29,1471^AAN,27,15^FH\\^FD" + printerDetails.getSettings().getMemoryFlashFreeMb() + "MB (" + printerDetails.getSettings().getMemoryFlashFreePercentage() + ")" +  "^FS\n" +
                "^FO19,981^GB505,0,8^FS\n" +
                "^FO19,195^GB505,0,8^FS\n" +
                "^PQ1,0,1,Y^XZ";
    }

}
