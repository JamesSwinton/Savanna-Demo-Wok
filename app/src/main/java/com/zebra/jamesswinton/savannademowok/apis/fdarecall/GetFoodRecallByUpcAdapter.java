package com.zebra.jamesswinton.savannademowok.apis.fdarecall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.apis.fdarecall.pojos.FDARecall.RecallData;
import java.util.List;

public class GetFoodRecallByUpcAdapter extends RecyclerView.Adapter  {

  // Debugging
  private static final String TAG = "FoodRecallByUpcAdapter";

  // Constants

  // Static Variables


  // Variables
  private Context mContext;
  private List<RecallData> mRecallData;

  public GetFoodRecallByUpcAdapter(Context context, List<RecallData> recallData) {
    this.mContext = context;
    this.mRecallData = recallData;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new RecallDataHolder(LayoutInflater.from(
        parent.getContext()).inflate(R.layout.adapter_food_recall_by_upc, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    // Cast ViewHolder to PopulatedViewHolder
    RecallDataHolder vh = (RecallDataHolder) viewHolder;

    // Get Recall
    RecallData recallData = mRecallData.get(position);

    // Populate View
    vh.mMetaData.setText(recallData.eventId + " | " + recallData.productType + " | "
        + recallData.recallNumber + " | " + recallData.recallingFirm);
    vh.mStartEndDate.setText("Start Date: " + recallData.getRecallInitiationDate()
        + " | End Date: " + recallData.getTerminationDate());
    vh.mProductDescription.setText(recallData.getProductDescription());
    vh.mRecallReason.setText(recallData.getReasonForRecall());
  }

  @Override
  public int getItemCount() {
    return mRecallData != null ? mRecallData.size() : 0;
  }

  private class RecallDataHolder extends ViewHolder {

    // UI Elements
    TextView mMetaData;
    TextView mStartEndDate;
    TextView mProductDescription;
    TextView mRecallReason;

    private RecallDataHolder(@NonNull View itemView) {
      super(itemView);
      mMetaData = itemView.findViewById(R.id.event_meta_data);
      mStartEndDate = itemView.findViewById(R.id.start_end);
      mProductDescription = itemView.findViewById(R.id.product_description);
      mRecallReason = itemView.findViewById(R.id.reason_for_recall);

    }
  }

}