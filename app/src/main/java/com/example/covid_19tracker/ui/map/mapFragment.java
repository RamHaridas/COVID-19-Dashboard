package com.example.covid_19tracker.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.covid_19tracker.Districts;
import com.example.covid_19tracker.JsonPlaceApiHolder;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.STATES;
import com.example.covid_19tracker.ui.dashboard.DistrictAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class mapFragment extends Fragment implements OnMapReadyCallback{
    ProgressBar progressBar;
    MapView mapView;
    GoogleMap googleMap;
    View view;
    JsonPlaceApiHolder jsonPlaceApiHolder;
    List<STATES> statesList;
    List<Districts> districtsList;
    AutoCompleteTextView autoCompleteTextView;
    public static List<Districts> finalList;
    List<String> arrayList;
    Map<String,String> hashmap;
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        autoCompleteTextView = view.findViewById(R.id.search_loc);
        autoCompleteTextView.setImeActionLabel("ENTER", EditorInfo.IME_ACTION_DONE);
        progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        finalList = new ArrayList<Districts>();
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(mapFragment.this);
        arrayList = new ArrayList<String>();
        hashmap = new HashMap<String, String>();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
        loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
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
                .addInterceptor(loggingInterceptor2)
                .build();

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://api.covidindiatracker.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient2)
                .build();
        jsonPlaceApiHolder = retrofit2.create(JsonPlaceApiHolder.class);

        getData();

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                geoLocate(view,o.toString(),hashmap.get(o.toString()));
            }
        });
        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getContext(),R.raw.map_style);
        googleMap.setMapStyle(mapStyleOptions);
        LatLng latLng = new LatLng(21.097487, 79.185818);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,4));
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void getData() {
        Call<List<STATES>> call = jsonPlaceApiHolder.getDistrictWiseData();
        call.enqueue(new Callback<List<STATES>>() {
            @Override
            public void onResponse(Call<List<STATES>> call, Response<List<STATES>> response) {
                if(response.isSuccessful()){
                    List<STATES> list = response.body();
                    statesList = response.body();
                    districtsList = statesList.get(0).getDistrictData();
                    for(STATES states : statesList){
                        districtsList = states.getDistrictData();
                        for(Districts d : districtsList){
                            finalList.add(d);
                            arrayList.add(d.getName());
                            hashmap.put(d.getName(),d.getZone());
                        }
                    }
                }
                adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<STATES>> call, Throwable t) {

            }
        });
    }

    public void geoLocate(View view,String search,String zone){

        Log.d("Geo","Geo Loacating");
        //source = "friends colony, nagpur";
        //String des = dest.getText().toString().trim();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(search,1);
        }catch(IOException e){
            Toast.makeText(view.getContext(),"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        if(list.size() > 0){
            Address address = list.get(0);
            double l = address.getLatitude();
            double l2 = address.getLongitude();
            addMarkerOnSearch(l,l2,zone);
            Log.d("Location found",address.toString());
            //Toast.makeText(view.getContext(),"Address:"+address,Toast.LENGTH_SHORT).show();
        }
    }
    public void addMarkerOnSearch(double lat, double lng,String zone){
        LatLng src = new LatLng(lat,lng);
        MarkerOptions sourceMarker;
        if(zone.equals("GREEN")){
            sourceMarker = new MarkerOptions().position(src).title(zone).icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_marker_green));
            googleMap.addMarker(sourceMarker);
        }else if(zone.equals("ORANGE")){
            sourceMarker = new MarkerOptions().position(src).title(zone).icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_error_black_24dp));
            googleMap.addMarker(sourceMarker);
        }else if(zone.equals("RED")){
            sourceMarker = new MarkerOptions().position(src).title(zone).icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_marker_red));
            googleMap.addMarker(sourceMarker);
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(src));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(src,10));

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId){
        Drawable drawable = ContextCompat.getDrawable(context,vectorId);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
