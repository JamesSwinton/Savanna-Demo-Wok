package com.zebra.jamesswinton.savannademowok.apis.fdarecall.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FDARecall {

  @SerializedName("meta")
  @Expose
  public Meta meta;
  @SerializedName("results")
  @Expose
  public List<RecallData> recallData = null;

  public Meta getMeta() {
    return meta;
  }

  public void setMeta(Meta meta) {
    this.meta = meta;
  }

  public List<RecallData> getRecallData() {
    return recallData;
  }

  public void setRecallData(
      List<RecallData> recallData) {
    this.recallData = recallData;
  }

  public class Meta {

    @SerializedName("disclaimer")
    @Expose
    public String disclaimer;
    @SerializedName("terms")
    @Expose
    public String terms;
    @SerializedName("license")
    @Expose
    public String license;
    @SerializedName("last_updated")
    @Expose
    public String lastUpdated;
    @SerializedName("results")
    @Expose
    public RecallMeta recallMeta;

    public String getDisclaimer() {
      return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
      this.disclaimer = disclaimer;
    }

    public String getTerms() {
      return terms;
    }

    public void setTerms(String terms) {
      this.terms = terms;
    }

    public String getLicense() {
      return license;
    }

    public void setLicense(String license) {
      this.license = license;
    }

    public String getLastUpdated() {
      return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
      this.lastUpdated = lastUpdated;
    }

    public RecallMeta getRecallMeta() {
      return recallMeta;
    }

    public void setRecallMeta(
        RecallMeta recallMeta) {
      this.recallMeta = recallMeta;
    }

    public class RecallMeta {

      @SerializedName("skip")
      @Expose
      public Integer skip;
      @SerializedName("limit")
      @Expose
      public Integer limit;
      @SerializedName("total")
      @Expose
      public Integer total;

      public Integer getSkip() {
        return skip;
      }

      public void setSkip(Integer skip) {
        this.skip = skip;
      }

      public Integer getLimit() {
        return limit;
      }

      public void setLimit(Integer limit) {
        this.limit = limit;
      }

      public Integer getTotal() {
        return total;
      }

      public void setTotal(Integer total) {
        this.total = total;
      }
    }

  }

  public class RecallData {

    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("reason_for_recall")
    @Expose
    public String reasonForRecall;
    @SerializedName("address_1")
    @Expose
    public String address1;
    @SerializedName("address_2")
    @Expose
    public String address2;
    @SerializedName("code_info")
    @Expose
    public String codeInfo;
    @SerializedName("product_quantity")
    @Expose
    public String productQuantity;
    @SerializedName("center_classification_date")
    @Expose
    public String centerClassificationDate;
    @SerializedName("distribution_pattern")
    @Expose
    public String distributionPattern;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("product_description")
    @Expose
    public String productDescription;
    @SerializedName("report_date")
    @Expose
    public String reportDate;
    @SerializedName("classification")
    @Expose
    public String classification;
    @SerializedName("recall_number")
    @Expose
    public String recallNumber;
    @SerializedName("recalling_firm")
    @Expose
    public String recallingFirm;
    @SerializedName("initial_firm_notification")
    @Expose
    public String initialFirmNotification;
    @SerializedName("event_id")
    @Expose
    public String eventId;
    @SerializedName("product_type")
    @Expose
    public String productType;
    @SerializedName("termination_date")
    @Expose
    public String terminationDate;
    @SerializedName("recall_initiation_date")
    @Expose
    public String recallInitiationDate;
    @SerializedName("postal_code")
    @Expose
    public String postalCode;
    @SerializedName("voluntary_mandated")
    @Expose
    public String voluntaryMandated;
    @SerializedName("status")
    @Expose
    public String status;

    public String getCountry() {
      return country;
    }

    public void setCountry(String country) {
      this.country = country;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getReasonForRecall() {
      return reasonForRecall;
    }

    public void setReasonForRecall(String reasonForRecall) {
      this.reasonForRecall = reasonForRecall;
    }

    public String getAddress1() {
      return address1;
    }

    public void setAddress1(String address1) {
      this.address1 = address1;
    }

    public String getAddress2() {
      return address2;
    }

    public void setAddress2(String address2) {
      this.address2 = address2;
    }

    public String getCodeInfo() {
      return codeInfo;
    }

    public void setCodeInfo(String codeInfo) {
      this.codeInfo = codeInfo;
    }

    public String getProductQuantity() {
      return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
      this.productQuantity = productQuantity;
    }

    public String getCenterClassificationDate() {
      return centerClassificationDate;
    }

    public void setCenterClassificationDate(String centerClassificationDate) {
      this.centerClassificationDate = centerClassificationDate;
    }

    public String getDistributionPattern() {
      return distributionPattern;
    }

    public void setDistributionPattern(String distributionPattern) {
      this.distributionPattern = distributionPattern;
    }

    public String getState() {
      return state;
    }

    public void setState(String state) {
      this.state = state;
    }

    public String getProductDescription() {
      return productDescription;
    }

    public void setProductDescription(String productDescription) {
      this.productDescription = productDescription;
    }

    public String getReportDate() {
      return reportDate;
    }

    public void setReportDate(String reportDate) {
      this.reportDate = reportDate;
    }

    public String getClassification() {
      return classification;
    }

    public void setClassification(String classification) {
      this.classification = classification;
    }

    public String getRecallNumber() {
      return recallNumber;
    }

    public void setRecallNumber(String recallNumber) {
      this.recallNumber = recallNumber;
    }

    public String getRecallingFirm() {
      return recallingFirm;
    }

    public void setRecallingFirm(String recallingFirm) {
      this.recallingFirm = recallingFirm;
    }

    public String getInitialFirmNotification() {
      return initialFirmNotification;
    }

    public void setInitialFirmNotification(String initialFirmNotification) {
      this.initialFirmNotification = initialFirmNotification;
    }

    public String getEventId() {
      return eventId;
    }

    public void setEventId(String eventId) {
      this.eventId = eventId;
    }

    public String getProductType() {
      return productType;
    }

    public void setProductType(String productType) {
      this.productType = productType;
    }

    public String getTerminationDate() {
      return terminationDate;
    }

    public void setTerminationDate(String terminationDate) {
      this.terminationDate = terminationDate;
    }

    public String getRecallInitiationDate() {
      return recallInitiationDate;
    }

    public void setRecallInitiationDate(String recallInitiationDate) {
      this.recallInitiationDate = recallInitiationDate;
    }

    public String getPostalCode() {
      return postalCode;
    }

    public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
    }

    public String getVoluntaryMandated() {
      return voluntaryMandated;
    }

    public void setVoluntaryMandated(String voluntaryMandated) {
      this.voluntaryMandated = voluntaryMandated;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }

}
