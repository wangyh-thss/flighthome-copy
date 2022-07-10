package com.example.flighthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private List<Flight> flightList = new ArrayList<>();
    private RecyclerView flightRecyclerView;
    private LinearLayoutManager layoutManager;
    private FlightAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getData();
        flightRecyclerView = findViewById(R.id.flightView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new FlightAdapter(flightList);
        flightRecyclerView.setLayoutManager(layoutManager);
        flightRecyclerView.setAdapter(adapter);
    }

    private void getData(){
        getflight(flightList);
    }

    private void getflight(List<Flight> list){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://aviation-edge.com/v2/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<JsonArray> call = retrofitAPI.getFlight("d5ee99-63f7dd","departure",MainActivity.airport_dep,MainActivity.airport_arr,MainActivity.date);
        Log.d("Plane",MainActivity.date);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("Plane",response.toString());
                try {
                    JsonArray data = response.body();
                    for(int i = 0; i<data.size();i++){
                        JsonObject json = data.get(i).getAsJsonObject();
                        Log.d("Plane", json.toString());
                        Flight flight = new Flight("-","-","-","-","-","-");
                        String leave = json.get("departure").getAsJsonObject().get("scheduledTime").getAsString();
                        flight.setLeaveTime(leave);
                        String arrive = json.get("arrival").getAsJsonObject().get("scheduledTime").getAsString();
                        flight.setArriveTime(arrive);
                        flight.setAirline(json.get("airline").getAsJsonObject().get("name").getAsString());
                        flight.setFlightNum(json.get("flight").getAsJsonObject().get("iataNumber").getAsString());
                        flight.setFromIata(json.get("departure").getAsJsonObject().get("iataCode").getAsString());
                        flight.setToIata(json.get("arrival").getAsJsonObject().get("iataCode").getAsString());
                        list.add(flight);
                        adapter.notifyItemInserted(list.size()-1);
                        flightRecyclerView.scrollToPosition(list.size()-1);
                    }
                    flightRecyclerView.scrollToPosition(0);
                }catch(Exception e){
                    Log.d("Plane", e.toString());
                    Toast.makeText(SearchActivity.this,"No Flight This Day, Check the date",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Plane",t.toString());

            }
        });
    }
}