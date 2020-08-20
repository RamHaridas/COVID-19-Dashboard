package com.example.covid_19tracker.ui.Info;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.covid_19tracker.JsonPlaceApiHolder;
import com.example.covid_19tracker.MainObject;
import com.example.covid_19tracker.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InfoFragment extends Fragment {
   View view;
   //TextView txt;
   JsonPlaceApiHolder jsonPlaceApiHolder;
   RecyclerView recyclerView;
   InfoAdapter infoAdapter;
   ProgressBar progressBar;
   Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info, container, false);
        button = view.findViewById(R.id.logout);
        progressBar = view.findViewById(R.id.new_progress);
        progressBar.setVisibility(View.VISIBLE);
        //txt = view.findViewById(R.id.number);
        recyclerView = view.findViewById(R.id.recycler3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newrequest = request.newBuilder()
                                .header("Interceptor","xyz")
                                .build();
                        return chain.proceed(newrequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://covid-19india-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceApiHolder = retrofit.create(JsonPlaceApiHolder.class);
        getInfo();
        return view;
    }

    public void getInfo(){
        Call<ContactDetails> call = jsonPlaceApiHolder.getInfo();
        call.enqueue(new Callback<ContactDetails>() {
            @Override
            public void onResponse(Call<ContactDetails> call, Response<ContactDetails> response) {
                if(response.isSuccessful()){
                    ContactDetails contactDetails = response.body();
                    List<InfoDetails> infoDetails = contactDetails.getInfoDetails();
                    infoAdapter = new InfoAdapter(getContext(),infoDetails);
                    recyclerView.setAdapter(infoAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ContactDetails> call, Throwable t) {

            }
        });
    }
}
