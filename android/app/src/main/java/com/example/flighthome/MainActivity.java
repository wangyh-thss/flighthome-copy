package com.example.flighthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static String fromCity;//iata code for departure city
    public static String toCity;  //iata code for arrival city
    public static String date;    //date entered in the search
    public static String airport_dep;//iata code for departure airport
    public static String airport_arr;//iata code for arrival airport
    public static String time_dep; //departure time for specific flight
    public static String time_arr; //arrival time for specific flight
    public static String airport_dep_name;//name of departure airport
    public static String airport_arr_name;//name of arrival airport
    public static String flight_iata;//iata code for flight
    public static boolean isArrival = false;//check call a request for departure or arrival
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent settingsIntent = new Intent(this, HomeActivity.class);
        startActivity(settingsIntent);
    }
}