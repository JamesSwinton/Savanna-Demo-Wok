package com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.squareup.picasso.Picasso;
import com.zebra.jamesswinton.savannademowok.R;
import java.util.List;

public class UpcProductImagesAdapter extends RecyclerView.Adapter  {

  // Debugging
  private static final String TAG = "UpcProductImagesAdapter";

  // Constants


  // Static Variables


  // Variables
  private Context mContext;
  private List<String> mUpcProductImages;


  public UpcProductImagesAdapter(Context cx, List<String> upcProductImages) {
    this.mContext = cx;
    this.mUpcProductImages = upcProductImages;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ProductImagesHolder(LayoutInflater.from(
        parent.getContext()).inflate(R.layout.adapter_upc_product_image, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    // Cast ViewHolder to PopulatedViewHolder
    ProductImagesHolder vh = (ProductImagesHolder) viewHolder;

    // Get Current BasketItem
    Picasso.get()
        .load(mUpcProductImages.get(position))
        .placeholder(R.drawable.ic_loading_image_placeholder)
        .error(R.drawable.ic_missing_image_placeholder)
        .into(vh.productImage);
  }

  @Override
  public int getItemCount() {
    return mUpcProductImages != null ? mUpcProductImages.size() : 0;
  }

  private class ProductImagesHolder extends ViewHolder {
    // UI Elements
    ImageView productImage;

    private ProductImagesHolder(@NonNull View itemView) {
      super(itemView);
      productImage = itemView.findViewById(R.id.product_image);
    }
  }

}
