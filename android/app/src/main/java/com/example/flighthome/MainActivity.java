package com.example.flighthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static String fromCity;
    public static String toCity;
    public static String date;
    public static String airport_dep;
    public static String airport_arr;
    public static String time_dep;
    public static String time_arr;
    public static String airport_dep_name;
    public static String airport_arr_name;
    public static String flight_iata;
    public static boolean isArrival = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent settingsIntent = new Intent(this, HomeActivity.class);
        startActivity(settingsIntent);
    }
}