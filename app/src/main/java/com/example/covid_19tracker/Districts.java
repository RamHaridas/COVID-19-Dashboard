package com.example.covid_19tracker;

import com.google.gson.annotations.SerializedName;

public class Districts {

    @SerializedName("name")
    String name;
    @SerializedName("confirmed")
    int confirm;
    @SerializedName("recovered")
    int recovered;
    @SerializedName("deaths")
    int dead;
    @SerializedName("zone")
    String zone;

    public String getName() {
        return name;
    }

    public int getConfirm() {
        return confirm;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getDead() {
        return dead;
    }

    public String getZone() {
        return zone;
    }
}
