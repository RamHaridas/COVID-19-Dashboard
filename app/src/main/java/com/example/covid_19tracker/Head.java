package com.example.covid_19tracker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Head {
    @SerializedName("statewise")
    List<StateWise> list;

    public List<StateWise> getList() {
        return list;
    }
}
