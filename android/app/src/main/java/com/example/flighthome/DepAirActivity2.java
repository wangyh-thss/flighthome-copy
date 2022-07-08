package com.example.flighthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DepAirActivity2 extends AppCompatActivity {
    private List<Airport> airportList = new ArrayList<>();
    private RecyclerView airportRecyclerView;
    private LinearLayoutManager layoutManager;
    private AirportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep_air2);

        MainActivity.isArrival=false;

        getData();
        airportRecyclerView = findViewById(R.id.airport_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new AirportAdapter(airportList);
        airportRecyclerView.setLayoutManager(layoutManager);
        airportRecyclerView.setAdapter(adapter);
    }

    private void getData(){
        getairportIata();
    }

    private void getairportIata(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<JsonArray> call = retrofitAPI.getAirportIata(MainActivity.fromCity);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("Message", response.toString());
                try {
                    if (response.body() != null) {
                        JsonArray json = response.body();
                        for (int i = 0; i < json.size(); i++) {
                            JsonObject object = json.get(i).getAsJsonObject();
                            Airport airport = new Airport("", "");
                            airport.setIata(object.get("airportiata").getAsString());
                            airport.setName(object.get("airportname").getAsString());
                            airportList.add(airport);
                            adapter.notifyItemInserted(airportList.size() - 1);
                            airportRecyclerView.scrollToPosition(airportList.size() - 1);
                        }
                        airportRecyclerView.scrollToPosition(0);
                    }else{
                        Intent settingsIntent = new Intent(DepAirActivity2.this, HomeActivity.class);
                        startActivity(settingsIntent);
                    }
                }catch(Exception e){
                    Log.d("Message", e.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Message",t.toString());
            }
        });
    }
}