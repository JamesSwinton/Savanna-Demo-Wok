package com.zebra.jamesswinton.savannademowok.apis.printers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zebra.jamesswinton.savannademowok.App;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.home.MainActivity;
import com.zebra.jamesswinton.savannademowok.apis.printers.pojos.AllPrinters.Printer;
import java.util.List;

public class GetAllPrintersAdapter extends RecyclerView.Adapter  {

  // Debugging
  private static final String TAG = "GetAllPrintersAdapter";

  // Constants
  private static final String ONLINE = "ONLINE";
  private static final String OFFLINE = "OFFLINE";

  // Static Variables


  // Variables
  private Context mContext;
  private List<Printer> mPrinters;

  public GetAllPrintersAdapter(Context context, List<Printer> printers) {
    this.mContext = context;
    this.mPrinters = printers;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new PrinterDetailsHolder(LayoutInflater.from(
        parent.getContext()).inflate(R.layout.adapter_printer_details, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    // Cast ViewHolder to PopulatedViewHolder
    PrinterDetailsHolder vh = (PrinterDetailsHolder) viewHolder;

    // Get Printer
    Printer printer = mPrinters.get(position);

    // Get Printer Color based on Status
    int color;
    String printerStatus = printer.getReadiness();
    if (printerStatus.equalsIgnoreCase(ONLINE)) {
      color = ContextCompat.getColor(mContext, R.color.success);
    } else if (printerStatus.equalsIgnoreCase(OFFLINE)) {
      color = ContextCompat.getColor(mContext, R.color.error);
    } else {
      color = ContextCompat.getColor(mContext, R.color.grey);
    }

    // Populate Details
    vh.mPrinterTitleAndModel.setText(printer.getPrinterName() + " | " + printer.getPrinterModel());
    vh.mPrinterSerialAndFirmware.setText(printer.getSerialNumber() + " | " + printer.getFirmwareVersion());
    vh.mPrinterStatus.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);

    // Init Overflow Menu
    if (printer.getReadiness().equalsIgnoreCase(ONLINE)) {
      vh.mPrinterOptionsOverflow.setEnabled(true);
      vh.mPrinterOptionsOverflow.setOnClickListener(view -> {
        // Build Menu & Inflater
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();

        // Inflate Overflow Menu
        inflater.inflate(R.menu.printer_overflow_menu, popup.getMenu());

        // Init Click Listener
        popup.setOnMenuItemClickListener(menuItem -> {
          // Handle Menu Options
          switch(menuItem.getItemId()) {
            case R.id.send_print_job:
              // Create Bundle
              Bundle printerDetails = new Bundle();
              printerDetails.putString(App.ARG_PRINTER_SERIAL, printer.getSerialNumber());

              // Create Fragment with Bundle
              Fragment sendPrintJob = new SendPrintJobFragment();
              sendPrintJob.setArguments(printerDetails);

              // Add Fragment to Back Stack
              ((MainActivity) mContext).addFragmentToBackStack(sendPrintJob, "SEND-PRINT-JOB");
              break;
            case R.id.get_printer_details:

              break;
            case R.id.get_printer_odometer:

              break;
          } return false;
        });

        // Show Menu
        popup.show();
      });
    } else {
      vh.mPrinterOptionsOverflow.setEnabled(false);
      vh.mPrinterOptionsOverflow.setColorFilter(R.color.disabled_grey, android.graphics.PorterDuff.Mode.SRC_IN);
    }
  }

  @Override
  public int getItemCount() {
    return mPrinters != null ? mPrinters.size() : 0;
  }

  private class PrinterDetailsHolder extends ViewHolder {

    // UI Elements
    CardView mBaseLayout;
    TextView mPrinterTitleAndModel;
    TextView mPrinterSerialAndFirmware;
    ImageView mPrinterStatus;
    ImageView mPrinterOptionsOverflow;

    private PrinterDetailsHolder(@NonNull View itemView) {
      super(itemView);
      mBaseLayout = itemView.findViewById(R.id.base_layout);
      mPrinterTitleAndModel = itemView.findViewById(R.id.printer_name_and_model);
      mPrinterSerialAndFirmware = itemView.findViewById(R.id.printer_firmware_and_serial);
      mPrinterStatus = itemView.findViewById(R.id.printer_status);
      mPrinterOptionsOverflow = itemView.findViewById(R.id.printer_options_overflow);
    }
  }

}
