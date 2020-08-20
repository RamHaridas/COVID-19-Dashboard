package com.example.covid_19tracker;

import com.example.covid_19tracker.ui.Info.ContactDetails;
import com.example.covid_19tracker.ui.Info.MainInfoObjects;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceApiHolder {


    @GET("data.json")
    Call<Head> getHead();

    @GET("state_data.json")
    Call<List<STATES>> getDistrictWiseData();

    @GET("helpline_numbers")
    Call<ContactDetails> getInfo();
}
