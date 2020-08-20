package com.example.covid_19tracker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainObject {

    @SerializedName("0")
    STATES states;

    public STATES getStates() {
        return states;
    }
}
