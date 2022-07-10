package com.example.flighthome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlightInfoActivity extends AppCompatActivity {
    private TextView flight;
    private TextView leave_time;
    private TextView arr_time;
    private TextView dep_name;
    private TextView arr_name;
    private TextView dep_ter;
    private TextView arr_ter;
    private TextView leave_info;
    private TextView arr_info;

    //recent flights in this route
    private TextView sch_dep;
    private TextView sch_arr;
    private TextView status;
    private TextView est_dep;
    private TextView act_dep;
    private TextView est_arr;
    private TextView act_arr;
    private TextView dep_ter_1;
    private TextView arr_ter_1;

    private LinearLayout recent_view;
    private LinearLayout no_recent_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_info);

        flight = findViewById(R.id.textView6);
        leave_time = findViewById(R.id.leave_time_2);
        arr_time = findViewById(R.id.arrive_time_2);
        dep_name = findViewById(R.id.dep_air);
        arr_name = findViewById(R.id.arr_air);
        dep_ter = findViewById(R.id.dep_ter);
        arr_ter = findViewById(R.id.arr_ter);
        sch_dep = findViewById(R.id.leave_time_3);
        sch_arr = findViewById(R.id.arrive_time_3);
        status = findViewById(R.id.status);
        est_dep = findViewById(R.id.est_dep);
        act_dep = findViewById(R.id.act_dep);
        est_arr = findViewById(R.id.est_arr);
        act_arr = findViewById(R.id.act_arr);
        dep_ter_1 = findViewById(R.id.dep_ter_2);
        arr_ter_1 = findViewById(R.id.arr_ter_2);
        leave_info= findViewById(R.id.leave_info);
        arr_info = findViewById(R.id.arrive_info);

        recent_view = findViewById(R.id.recent_item);
        no_recent_view = findViewById(R.id.no_recent_view);

        dep_name.setText(MainActivity.airport_dep_name);
        arr_name.setText(MainActivity.airport_arr_name);
        flight.setText(MainActivity.flight_iata.toUpperCase(Locale.ROOT));
        leave_info.setText(MainActivity.airport_dep);
        arr_info.setText(MainActivity.airport_arr);

        getRoute();
        getRecent();

    }
    //get the specific route information, if not get use old future schedule data
    private void getRoute(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://aviation-edge.com/v2/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        String iata = MainActivity.flight_iata.substring(0,2);
        String num = MainActivity.flight_iata.substring(2);
        Log.d("Plane",num);

        Call<JsonArray> call = retrofitAPI.getRoute("d5ee99-63f7dd",MainActivity.airport_dep,iata,num);
        Log.d("Plane",MainActivity.date);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("Plane",response.toString());
                try {
                    if(response.body().size()==1) {
                        JsonObject data = response.body().get(0).getAsJsonObject();
                        String leave = data.get("departureTime").getAsString();
                        leave_time.setText(leave.substring(0,5));
                        String arr = data.get("arrivalTime").getAsString();
                        arr_time.setText(arr.substring(0,5));
                        dep_ter.setText(data.get("departureTerminal").getAsString());
                        arr_ter.setText(data.get("arrivalTerminal").getAsString());
                    }
                }catch(Exception e){
                    //if not get use old future schedule data and set the notification
                    Log.d("Plane", e.toString());
                    leave_time.setText(MainActivity.time_dep);
                    arr_time.setText(MainActivity.time_arr);
                    Toast.makeText(FlightInfoActivity.this,"No Route Information",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Plane",t.toString());
                leave_time.setText(MainActivity.time_dep);
                arr_time.setText(MainActivity.time_arr);
                Toast.makeText(FlightInfoActivity.this,"No Route Information",Toast.LENGTH_LONG).show();
            }
        });
    }
    //get current flight information with same flight number
    private void getRecent(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://aviation-edge.com/v2/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<JsonArray> call = retrofitAPI.getRecent("d5ee99-63f7dd","departure",MainActivity.airport_dep,MainActivity.flight_iata);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("Plane",response.toString());
                try {
                    JsonArray data = response.body();
                    JsonObject flight = data.get(data.size()-1).getAsJsonObject();
                    JsonObject dep = flight.get("departure").getAsJsonObject();
                    JsonObject arr = flight.get("arrival").getAsJsonObject();
                    if(dep.get("scheduledTime")!=null){
                        String schDep = !dep.get("scheduledTime").isJsonNull() ? dep.get("scheduledTime").getAsString() : "";
                        if(!schDep.equals("")) {
                            sch_dep.setText(schDep.substring(11, 16));
                        }
                    }
                    if(arr.get("scheduledTime")!=null){
                        String schArr = !arr.get("scheduledTime").isJsonNull() ? arr.get("scheduledTime").getAsString() : "";
                        if(!schArr.equals("")) {
                            sch_arr.setText(schArr.substring(11, 16));
                        }
                    }
                    if(dep.get("estimatedTime")!=null){
                        String estDep = !dep.get("estimatedTime").isJsonNull() ? dep.get("estimatedTime").getAsString() : "";
                        if(!estDep.equals("")) {
                            est_dep.setText(estDep.substring(11, 16));
                        }
                    }
                    if(arr.get("estimatedTime")!=null){
                        String estArr = !arr.get("estimatedTime").isJsonNull() ? arr.get("estimatedTime").getAsString() : "";
                        if(!estArr.equals("")) {
                            est_arr.setText(estArr.substring(11, 16));
                        }
                    }
                    if(dep.get("actualTime")!=null){
                        String actDep = !dep.get("actualTime").isJsonNull() ? dep.get("actualTime").getAsString() : "";
                        if(!actDep.equals("")) {
                            act_dep.setText(actDep.substring(11, 16));
                        }
                    }
                    if(arr.get("actualTime")!=null){
                        String actArr = !arr.get("actualTime").isJsonNull() ? arr.get("actualTime").getAsString() : "";
                        if(!actArr.equals("")) {
                            act_arr.setText(actArr.substring(11, 16));
                        }
                    }
                    if(flight.get("status")!=null){
                        status.setText(flight.get("status").getAsString());
                    }
                    if(dep.get("terminal")!=null){
                        String terDep = !dep.get("terminal").isJsonNull() ? dep.get("terminal").getAsString() : "";
                        if(!terDep.equals("")) {
                            dep_ter_1.setText(terDep);
                        }
                    }
                    if(arr.get("terminal")!=null){
                        String terArr = !arr.get("terminal").isJsonNull() ? arr.get("terminal").getAsString() : "";
                        if(!terArr.equals("")) {
                            arr_ter_1.setText(terArr);
                        }
                    }
                    //if can get current flight,hide the layout for no flight
                    no_recent_view.setVisibility(View.GONE);
               }catch(Exception e){
                    //if unable to find one, hide the layout for current flight information
                    Log.d("Plane", e.toString());
                    recent_view.setVisibility(View.GONE);
                    Toast.makeText(FlightInfoActivity.this,"No Flight This Day, Check the date",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Plane",t.toString());
                recent_view.setVisibility(View.GONE);
            }
        });
    }
}