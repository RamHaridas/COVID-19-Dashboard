package com.example.covid_19tracker.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.Head;
import com.example.covid_19tracker.JsonPlaceApiHolder;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.StateWise;

import org.jetbrains.annotations.NotNull;

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

public class HomeFragment extends Fragment {

    //https://api.covid19india.org/zones.json
    JsonPlaceApiHolder jsonPlaceApiHolder;
    View root;
    RecyclerView recyclerView;
    TextView conf,active,rec,dead;
    public static List<StateWise> statelist;
    public static StateAdapter stateAdapter;
    Response<OkHttpClient> response;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        conf = root.findViewById(R.id.confirmed);
        active = root.findViewById(R.id.active);
        rec = root.findViewById(R.id.recovered);
        dead = root.findViewById(R.id.dead);
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        //progressBar = root.findViewById(R.id.progress);
        //progressBar.setVisibility(View.VISIBLE);
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
                .baseUrl("https://api.covid19india.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceApiHolder = retrofit.create(JsonPlaceApiHolder.class);
        getHeads();
        getState();
        return root;
    }

    private void getState() {
        
    }

    //Get State wise data
    private void getHeads() {
        Call<Head> call = jsonPlaceApiHolder.getHead();
        call.enqueue(new Callback<Head>() {
            @Override
            public void onResponse(Call<Head> call, Response<Head> response) {
                if(response.isSuccessful()){
                    Head h = response.body();
                    //Log.i("786",h.getList().get(1).getDeaths());
                    statelist = h.getList();
                    conf.setText(statelist.get(0).getConfirmed());
                    rec.setText(statelist.get(0).getRecovered());
                    active.setText(statelist.get(0).getActive());
                    dead.setText(statelist.get(0).getDeaths());
                    stateAdapter = new StateAdapter(root.getContext(),statelist);
                    recyclerView.setAdapter(stateAdapter);
                    //progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Head> call, Throwable t) {

            }
        });
    }



    public void Print(){
        for(StateWise stateWise : statelist){
            String content = "";
            content += stateWise.getState() + "\t";
            content += stateWise.getConfirmed() + "\t";
            content += stateWise.getActive() + "\t";
            content += stateWise.getRecovered() + "\t";
            content += stateWise.getDeaths() + "\n";
            //textView.append(content);
        }
    }
}
