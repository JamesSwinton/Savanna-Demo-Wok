package com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zebra.jamesswinton.savannademowok.R;
import com.zebra.jamesswinton.savannademowok.apis.barcodeintelligence.pojos.UPCProduct.UpcItem.UpcItemOffers;
import java.text.DecimalFormat;
import java.util.List;

public class UpcProductOffersAdapter extends RecyclerView.Adapter  {

  // Debugging
  private static final String TAG = "UpcProductImagesAdapter";

  // Constants


  // Static Variables


  // Variables
  private Context mContext;
  private List<UpcItemOffers> mUpcItemOffers;


  public UpcProductOffersAdapter(Context cx, List<UpcItemOffers> upcItemOffers) {
    this.mContext = cx;
    this.mUpcItemOffers = upcItemOffers;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ProductImagesHolder(LayoutInflater.from(
        parent.getContext()).inflate(R.layout.adapter_upc_product_offers, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    // Cast ViewHolder to PopulatedViewHolder
    ProductImagesHolder vh = (ProductImagesHolder) viewHolder;

    // Get Offer for Item
    UpcItemOffers offer = mUpcItemOffers.get(position);

    // Strip URL from Merchant
    String offerMerchant;
    if (offer.getMerchant().contains(".")) {
      String[] offerMerchantSplit = offer.getMerchant().split("\\.");
      offerMerchant = offerMerchantSplit[0];
    } else {
      offerMerchant = offer.getMerchant();
    }

    // Get Currency
    String currency = TextUtils.isEmpty(offer.getCurrency()) ? "$" : offer.getCurrency();
    DecimalFormat mPriceFormat = new DecimalFormat(currency + "#.#");
    mPriceFormat.setMinimumFractionDigits(2);
    String price = mPriceFormat.format(offer.getPrice());

    // Get Current BasketItem
    vh.productTitle.setText(offer.getTitle());
    vh.merchantAndWebsite.setText(offerMerchant + " | " + offer.getDomain() + " | " + price);

    // Parse URL (Add https:// if needed)
    Uri offerUrl;
    if (offer.getLink().contains("http") || offer.getLink().contains("https")) {
      offerUrl = Uri.parse(offer.getLink());
    } else {
      offerUrl = Uri.parse("https://" + offer.getLink());
    }

    // Set Click Listener to open URL
    vh.baseLayout.setOnClickListener(view ->
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, offerUrl))
    );
  }

  @Override
  public int getItemCount() {
    return mUpcItemOffers != null ? mUpcItemOffers.size() : 0;
  }

  private class ProductImagesHolder extends ViewHolder {
    // UI Elements
    CardView baseLayout;
    TextView productTitle;
    TextView merchantAndWebsite;

    private ProductImagesHolder(@NonNull View itemView) {
      super(itemView);
      baseLayout = itemView.findViewById(R.id.base_layout);
      productTitle = itemView.findViewById(R.id.product_title);
      merchantAndWebsite = itemView.findViewById(R.id.merchant_and_website);
    }
  }

}
