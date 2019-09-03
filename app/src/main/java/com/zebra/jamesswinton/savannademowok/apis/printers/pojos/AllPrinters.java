package com.zebra.jamesswinton.savannademowok.apis.printers.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AllPrinters {

  @SerializedName("hasMore")
  @Expose
  public Boolean hasMore;
  @SerializedName("totalCount")
  @Expose
  public Integer totalCount;
  @SerializedName("tags")
  @Expose
  public List<Object> tags = null;
  @SerializedName("devices")
  @Expose
  public List<Printer> devices = null;

  public Boolean getHasMore() {
    return hasMore;
  }

  public void setHasMore(Boolean hasMore) {
    this.hasMore = hasMore;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public List<Object> getTags() {
    return tags;
  }

  public void setTags(List<Object> tags) {
    this.tags = tags;
  }

  public List<Printer> getDevices() {
    return devices;
  }

  public void setDevices(
      List<Printer> devices) {
    this.devices = devices;
  }

  public class Printer {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("serialNumber")
    @Expose
    public String serialNumber;
    @SerializedName("readiness")
    @Expose
    public String readiness;
    @SerializedName("readinessPretty")
    @Expose
    public String readinessPretty;
    @SerializedName("firmwareVersion")
    @Expose
    public String firmwareVersion;
    @SerializedName("printerName")
    @Expose
    public String printerName;
    @SerializedName("printerModel")
    @Expose
    public String printerModel;
    @SerializedName("dotsPerMm")
    @Expose
    public Integer dotsPerMm;
    @SerializedName("ipAddress")
    @Expose
    public String ipAddress;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("manufactureDate")
    @Expose
    public String manufactureDate;
    @SerializedName("activeInterfaceMACAddress")
    @Expose
    public String activeInterfaceMACAddress;
    @SerializedName("linkOSVersion")
    @Expose
    public String linkOSVersion;
    @SerializedName("tags")
    @Expose
    public List<Object> tags = null;
    @SerializedName("deviceType")
    @Expose
    public String deviceType;
    @SerializedName("odometer_user_labels")
    @Expose
    public Integer odometerUserLabels;
    @SerializedName("odometer_total_labels")
    @Expose
    public Integer odometerTotalLabels;
    @SerializedName("odometer_total_length")
    @Expose
    public Integer odometerTotalLength;
    @SerializedName("odometer_headclean")
    @Expose
    public Integer odometerHeadclean;
    @SerializedName("odometer_headnew")
    @Expose
    public Integer odometerHeadnew;
    @SerializedName("battery_voltage")
    @Expose
    public Double batteryVoltage;
    @SerializedName("battery_percent")
    @Expose
    public Integer batteryPercent;
    @SerializedName("battery_status")
    @Expose
    public String batteryStatus;
    @SerializedName("battery_health")
    @Expose
    public String batteryHealth;
    @SerializedName("battery_first_used")
    @Expose
    public String batteryFirstUsed;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getSerialNumber() {
      return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
      this.serialNumber = serialNumber;
    }

    public String getReadiness() {
      return readiness;
    }

    public void setReadiness(String readiness) {
      this.readiness = readiness;
    }

    public String getReadinessPretty() {
      return readinessPretty;
    }

    public void setReadinessPretty(String readinessPretty) {
      this.readinessPretty = readinessPretty;
    }

    public String getFirmwareVersion() {
      return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
      this.firmwareVersion = firmwareVersion;
    }

    public String getPrinterName() {
      return printerName;
    }

    public void setPrinterName(String printerName) {
      this.printerName = printerName;
    }

    public String getPrinterModel() {
      return printerModel;
    }

    public void setPrinterModel(String printerModel) {
      this.printerModel = printerModel;
    }

    public Integer getDotsPerMm() {
      return dotsPerMm;
    }

    public void setDotsPerMm(Integer dotsPerMm) {
      this.dotsPerMm = dotsPerMm;
    }

    public String getIpAddress() {
      return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
    }

    public String getLocation() {
      return location;
    }

    public void setLocation(String location) {
      this.location = location;
    }

    public String getManufactureDate() {
      return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
      this.manufactureDate = manufactureDate;
    }

    public String getActiveInterfaceMACAddress() {
      return activeInterfaceMACAddress;
    }

    public void setActiveInterfaceMACAddress(String activeInterfaceMACAddress) {
      this.activeInterfaceMACAddress = activeInterfaceMACAddress;
    }

    public String getLinkOSVersion() {
      return linkOSVersion;
    }

    public void setLinkOSVersion(String linkOSVersion) {
      this.linkOSVersion = linkOSVersion;
    }

    public List<Object> getTags() {
      return tags;
    }

    public void setTags(List<Object> tags) {
      this.tags = tags;
    }
    public String getDeviceType() {
      return deviceType;
    }

    public void setDeviceType(String deviceType) {
      this.deviceType = deviceType;
    }

    public Integer getOdometerUserLabels() {
      return odometerUserLabels;
    }

    public void setOdometerUserLabels(Integer odometerUserLabels) {
      this.odometerUserLabels = odometerUserLabels;
    }

    public Integer getOdometerTotalLabels() {
      return odometerTotalLabels;
    }

    public void setOdometerTotalLabels(Integer odometerTotalLabels) {
      this.odometerTotalLabels = odometerTotalLabels;
    }

    public Integer getOdometerTotalLength() {
      return odometerTotalLength;
    }

    public void setOdometerTotalLength(Integer odometerTotalLength) {
      this.odometerTotalLength = odometerTotalLength;
    }

    public Integer getOdometerHeadclean() {
      return odometerHeadclean;
    }

    public void setOdometerHeadclean(Integer odometerHeadclean) {
      this.odometerHeadclean = odometerHeadclean;
    }

    public Integer getOdometerHeadnew() {
      return odometerHeadnew;
    }

    public void setOdometerHeadnew(Integer odometerHeadnew) {
      this.odometerHeadnew = odometerHeadnew;
    }

    public Double getBatteryVoltage() {
      return batteryVoltage;
    }

    public void setBatteryVoltage(Double batteryVoltage) {
      this.batteryVoltage = batteryVoltage;
    }

    public Integer getBatteryPercent() {
      return batteryPercent;
    }

    public void setBatteryPercent(Integer batteryPercent) {
      this.batteryPercent = batteryPercent;
    }

    public String getBatteryStatus() {
      return batteryStatus;
    }

    public void setBatteryStatus(String batteryStatus) {
      this.batteryStatus = batteryStatus;
    }

    public String getBatteryHealth() {
      return batteryHealth;
    }

    public void setBatteryHealth(String batteryHealth) {
      this.batteryHealth = batteryHealth;
    }

    public String getBatteryFirstUsed() {
      return batteryFirstUsed;
    }

    public void setBatteryFirstUsed(String batteryFirstUsed) {
      this.batteryFirstUsed = batteryFirstUsed;
    }

  }
}
