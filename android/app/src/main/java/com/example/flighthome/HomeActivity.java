package com.example.flighthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    private Button insertCity;
    private EditText from_place;
    private EditText to_place;
    private EditText date_place;
    private JsonArray data;
    private int result = 0;
    //String iata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        from_place = findViewById(R.id.from_text);
        to_place = findViewById(R.id.to_text);
        date_place = findViewById(R.id.date_text);

        //set city and airport database(no need for user)
        insertCity = findViewById(R.id.button);
        insertCity.setVisibility(View.INVISIBLE);
        insertCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCity();
                Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(data!=null){
                            JsonArray b = new JsonArray();
                            int k = 0;
                            //spilt into smaller packages
                            for(int i = 0;i<data.size();i++){
                                JsonObject a = data.get(i).getAsJsonObject();
                                b.add(a);
                                if(k==299) {
                                    postCity(b);
                                    b = new JsonArray();
                                    k = -1;
                                }
                                k++;
                                if(i == data.size()-1){
                                    postCity(b);
                                    break;
                                }
                            }
                        }
                    }
                },2000);
            }
        });

        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = 0;
                String fromPlace = from_place.getEditableText().toString().trim();
                String toPlace = to_place.getEditableText().toString().trim();
                String date_set = date_place.getEditableText().toString().trim();
                try{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date stringDate= format.parse(date_set);
                    MainActivity.date = date_set;
                    //make sure get all necessary information
                    if(!fromPlace.equals("") && !toPlace.equals("") && !date_set.equals("")){
                        getIata(fromPlace,true);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getIata(toPlace,false);
                            }},1000);
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //jump to next page only when get iata data for both cities
                                if(result == 2) {
                                    Intent settingsIntent = new Intent(HomeActivity.this, DepAirActivity2.class);
                                    startActivity(settingsIntent);
                                }else{
                                    Toast.makeText(HomeActivity.this,"Get Wrong City!",Toast.LENGTH_LONG).show();
                                }
                            }},2000);
                    }else{
                        Toast.makeText(HomeActivity.this,"Some necessary information missing!",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Log.d("Date",e.toString());
                    Toast.makeText(HomeActivity.this,"Check Format of Date!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //get iataCode from the name of city
    private void getIata(String city,boolean isFrom){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://137.184.238.43:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<JsonObject> call = retrofitAPI.getCityIata(city);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Message", response.toString());
                if(response.body()!= null) {
                    if(response.body().get("iata")!=null) {
                        if (isFrom) {
                            //departure city iata
                            MainActivity.fromCity = response.body().get("iata").getAsString();
                            Log.d("Message", MainActivity.fromCity);
                            result ++;
                        } else {
                            //arrival city iata
                            MainActivity.toCity = response.body().get("iata").getAsString();
                            Log.d("Message", MainActivity.toCity);
                            result ++;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Message",t.toString());
            }
        });
    }

    //get all the city (airport) database from api
    private void getCity(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://aviation-edge.com/v2/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<JsonArray> call = retrofitAPI.createCities("d5ee99-63f7dd");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                data = response.body();
                Log.d("Plane",data.toString());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Plane",t.toString());

            }
        });
    }
    //send the database to the server to save in the mongoDB
    private void postCity(JsonArray res){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://137.184.238.43:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<String> call = retrofitAPI.postCities(res);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Message",response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Message",t.toString());
            }
        });
    }
}