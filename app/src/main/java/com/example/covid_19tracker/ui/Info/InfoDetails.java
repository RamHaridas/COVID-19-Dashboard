package com.example.covid_19tracker.ui.Info;

import com.google.gson.annotations.SerializedName;

public class InfoDetails {

    @SerializedName("helpline_number")
    String number;
    @SerializedName("state_or_UT")
    String state;

    public String getNumber() {
        return number;
    }

    public String getState() {
        return state;
    }
}
