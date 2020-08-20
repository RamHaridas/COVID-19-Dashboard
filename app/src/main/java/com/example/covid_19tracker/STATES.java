package com.example.covid_19tracker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class STATES {
    @SerializedName("id")
    String id;

    @SerializedName("districtData")
    List<Districts> districtData;

    @SerializedName("state")
    String state;

    public String getState() {
        return state;
    }

    public List<Districts> getDistrictData() {
        return districtData;
    }
}
