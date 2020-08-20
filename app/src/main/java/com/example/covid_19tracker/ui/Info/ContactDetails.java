package com.example.covid_19tracker.ui.Info;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactDetails {
    @SerializedName("contact_details")
    List<InfoDetails> infoDetails;

    public List<InfoDetails> getInfoDetails() {
        return infoDetails;
    }
}
