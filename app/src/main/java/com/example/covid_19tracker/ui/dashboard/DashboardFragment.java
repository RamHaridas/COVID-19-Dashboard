package com.example.covid_19tracker.ui.dashboard;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.Districts;
import com.example.covid_19tracker.JsonPlaceApiHolder;
import com.example.covid_19tracker.MainObject;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.STATES;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
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

public class DashboardFragment extends Fragment {
    JsonPlaceApiHolder jsonPlaceApiHolder;
    View view;
    RecyclerView recyclerView;
    public static DistrictAdapter districtAdapter;
    List<STATES> statesList;
    List<Districts> districtsList;
    List<Districts> finalList;
    ProgressBar progressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        progressBar = view.findViewById(R.id.progress1);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.recycler2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        finalList = new ArrayList<Districts>();
        HttpLoggingInterceptor loggingInterceptor1 = new HttpLoggingInterceptor();
        loggingInterceptor1.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient1 = new OkHttpClient.Builder()
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
                .addInterceptor(loggingInterceptor1)
                .build();

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("https://api.covidindiatracker.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient1)
                .build();
        jsonPlaceApiHolder = retrofit1.create(JsonPlaceApiHolder.class);

        getData();
        return view;
    }

    private void getData() {
        Call<List<STATES>> call = jsonPlaceApiHolder.getDistrictWiseData();
        call.enqueue(new Callback<List<STATES>>() {
            @Override
            public void onResponse(Call<List<STATES>> call, Response<List<STATES>> response) {
                if(response.isSuccessful()){
                    List<STATES> list = response.body();
                    statesList = response.body();
                    //districtsList = statesList.get(0).getDistrictData();
                    for(STATES states : statesList){
                        districtsList = states.getDistrictData();
                        finalList.addAll(districtsList);
                        /*for(Districts d : districtsList){
                            finalList.add(d);

                        }*/
                    }
                    Log.i("hello",String.valueOf(finalList.get(10).getDead()));
                    districtAdapter = new DistrictAdapter(getContext(),statesList,finalList);
                    recyclerView.setAdapter(districtAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<STATES>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater inflater1 = getActivity().getMenuInflater();
        inflater1.inflate(R.menu.simple_menu,menu);

        MenuItem search = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                districtAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
