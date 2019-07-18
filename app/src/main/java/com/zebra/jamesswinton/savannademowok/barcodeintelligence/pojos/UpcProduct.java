package com.zebra.jamesswinton.savannademowok.barcodeintelligence.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UpcProduct {

  @SerializedName("code")
  @Expose
  private String code;
  @SerializedName("total")
  @Expose
  private int total;
  @SerializedName("offset")
  @Expose
  private int offset;
  @SerializedName("items")
  @Expose
  private List<UpcItem> items = null;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public List<UpcItem> getItems() {
    return items;
  }

  public void setItems(List<UpcItem> items) {
    this.items = items;
  }

  public class UpcItem {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lowest_recorded_price")
    @Expose
    private Double lowestRecordedPrice;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    @SerializedName("offers")
    @Expose
    private List<UpcItemOffers> offers = null;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Double getLowestRecordedPrice() {
      return lowestRecordedPrice;
    }

    public void setLowestRecordedPrice(Double lowestRecordedPrice) {
      this.lowestRecordedPrice = lowestRecordedPrice;
    }

    public List<String> getImages() {
      return images;
    }

    public void setImages(List<String> images) {
      this.images = images;
    }

    public List<UpcItemOffers> getOffers() {
      return offers;
    }

    public void setOffers(List<UpcItemOffers> offers) {
      this.offers = offers;
    }

    public class UpcItemOffers {

      @SerializedName("merchant")
      @Expose
      private String merchant;
      @SerializedName("domain")
      @Expose
      private String domain;
      @SerializedName("title")
      @Expose
      private String title;
      @SerializedName("currency")
      @Expose
      private String currency;
      @SerializedName("list_price")
      @Expose
      private String listPrice;
      @SerializedName("price")
      @Expose
      private double price;
      @SerializedName("shipping")
      @Expose
      private String shipping;
      @SerializedName("condition")
      @Expose
      private String condition;
      @SerializedName("availability")
      @Expose
      private String availability;
      @SerializedName("link")
      @Expose
      private String link;
      @SerializedName("updated_t")
      @Expose
      private int updatedT;

      public String getMerchant() {
        return merchant;
      }

      public void setMerchant(String merchant) {
        this.merchant = merchant;
      }

      public String getDomain() {
        return domain;
      }

      public void setDomain(String domain) {
        this.domain = domain;
      }

      public String getTitle() {
        return title;
      }

      public void setTitle(String title) {
        this.title = title;
      }

      public String getCurrency() {
        return currency;
      }

      public void setCurrency(String currency) {
        this.currency = currency;
      }

      public String getListPrice() {
        return listPrice;
      }

      public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
      }

      public double getPrice() {
        return price;
      }

      public void setPrice(double price) {
        this.price = price;
      }

      public String getShipping() {
        return shipping;
      }

      public void setShipping(String shipping) {
        this.shipping = shipping;
      }

      public String getCondition() {
        return condition;
      }

      public void setCondition(String condition) {
        this.condition = condition;
      }

      public String getAvailability() {
        return availability;
      }

      public void setAvailability(String availability) {
        this.availability = availability;
      }

      public String getLink() {
        return link;
      }

      public void setLink(String link) {
        this.link = link;
      }

      public int getUpdatedT() {
        return updatedT;
      }

      public void setUpdatedT(int updatedT) {
        this.updatedT = updatedT;
      }

    }

  }

}
