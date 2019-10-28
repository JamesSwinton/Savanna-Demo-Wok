package com.zebra.jamesswinton.savannademowok.apis.printers.pojos;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrinterDetails {

    @SerializedName("deviceModel")
    @Expose
    public String deviceModel;
    @SerializedName("linkOSVersion")
    @Expose
    public String linkOSVersion;
    @SerializedName("firmwareVersion")
    @Expose
    public String firmwareVersion;
    @SerializedName("printerName")
    @Expose
    public String printerName;
    @SerializedName("dotsPerMm")
    @Expose
    public double dotsPerMm;
    @SerializedName("ipAddress")
    @Expose
    public String ipAddress;
    @SerializedName("manufactureDate")
    @Expose
    public String manufactureDate;
    @SerializedName("settings")
    @Expose
    public Settings settings;
    @SerializedName("readiness")
    @Expose
    public String readiness;
    @SerializedName("objectsLastUpdated")
    @Expose
    public double objectsLastUpdated;

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getLinkOSVersion() {
        return linkOSVersion;
    }

    public void setLinkOSVersion(String linkOSVersion) {
        this.linkOSVersion = linkOSVersion;
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

    public double getDotsPerMm() {
        return dotsPerMm;
    }

    public void setDotsPerMm(double dotsPerMm) {
        this.dotsPerMm = dotsPerMm;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getManufactureDate() {
        return TextUtils.isEmpty(manufactureDate) ? "Unknown" : manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getReadiness() {
        return readiness;
    }

    public void setReadiness(String readiness) {
        this.readiness = readiness;
    }

    public double getObjectsLastUpdated() {
        return objectsLastUpdated;
    }

    public void setObjectsLastUpdated(double objectsLastUpdated) {
        this.objectsLastUpdated = objectsLastUpdated;
    }

    public class Settings {

        @SerializedName("memory.ram_free_percentage")
        @Expose
        public String memoryRamFreePercentage;
        @SerializedName("device.uptime")
        @Expose
        public String deviceUptime;
        @SerializedName("log.reboot.reason")
        @Expose
        public String logRebootReason;
        @SerializedName("memory.ram_free_mb")
        @Expose
        public String memoryRamFreeMb;
        @SerializedName("power.percent_full")
        @Expose
        public String powerPercentFull;
        @SerializedName("memory.flash_free_percentage")
        @Expose
        public String memoryFlashFreePercentage;
        @SerializedName("memory.flash_free_mb")
        @Expose
        public String memoryFlashFreeMb;
        @SerializedName("odometer.user_label_count2")
        @Expose
        public String odometerUserLabelCount2;
        @SerializedName("odometer.user_label_count1")
        @Expose
        public String odometerUserLabelCount1;
        @SerializedName("odometer.total_label_count")
        @Expose
        public String odometerTotalLabelCount;
        @SerializedName("bluetooth.address")
        @Expose
        public String bluetoothAddress;
        @SerializedName("odometer.user_label_count")
        @Expose
        public String odometerUserLabelCount;

        public String getMemoryRamFreePercentage() {
            return memoryRamFreePercentage + "%";
        }

        public void setMemoryRamFreePercentage(String memoryRamFreePercentage) {
            this.memoryRamFreePercentage = memoryRamFreePercentage;
        }

        public String getDeviceUptime() {
            return deviceUptime;
        }

        public void setDeviceUptime(String deviceUptime) {
            this.deviceUptime = deviceUptime;
        }

        public String getLogRebootReason() {
            return logRebootReason;
        }

        public void setLogRebootReason(String logRebootReason) {
            this.logRebootReason = logRebootReason;
        }

        public String getMemoryRamFreeMb() {
            return memoryRamFreeMb;
        }

        public void setMemoryRamFreeMb(String memoryRamFreeMb) {
            this.memoryRamFreeMb = memoryRamFreeMb;
        }

        public String getPowerPercentFull() {
            return powerPercentFull == null ? "Unavailable" : powerPercentFull + "%";
        }

        public void setPowerPercentFull(String powerPercentFull) {
            this.powerPercentFull = powerPercentFull;
        }

        public String getMemoryFlashFreePercentage() {
            return memoryFlashFreePercentage + "%";
        }

        public void setMemoryFlashFreePercentage(String memoryFlashFreePercentage) {
            this.memoryFlashFreePercentage = memoryFlashFreePercentage;
        }

        public String getMemoryFlashFreeMb() {
            return memoryFlashFreeMb;
        }

        public void setMemoryFlashFreeMb(String memoryFlashFreeMb) {
            this.memoryFlashFreeMb = memoryFlashFreeMb;
        }

        public String getOdometerUserLabelCount2() {
            return odometerUserLabelCount2;
        }

        public void setOdometerUserLabelCount2(String odometerUserLabelCount2) {
            this.odometerUserLabelCount2 = odometerUserLabelCount2;
        }

        public String getOdometerUserLabelCount1() {
            return odometerUserLabelCount1;
        }

        public void setOdometerUserLabelCount1(String odometerUserLabelCount1) {
            this.odometerUserLabelCount1 = odometerUserLabelCount1;
        }

        public String getOdometerTotalLabelCount() {
            return odometerTotalLabelCount;
        }

        public void setOdometerTotalLabelCount(String odometerTotalLabelCount) {
            this.odometerTotalLabelCount = odometerTotalLabelCount;
        }

        public String getBluetoothAddress() {
            return bluetoothAddress;
        }

        public void setBluetoothAddress(String bluetoothAddress) {
            this.bluetoothAddress = bluetoothAddress;
        }

        public String getOdometerUserLabelCount() {
            return odometerUserLabelCount;
        }

        public void setOdometerUserLabelCount(String odometerUserLabelCount) {
            this.odometerUserLabelCount = odometerUserLabelCount;
        }
    }
}
