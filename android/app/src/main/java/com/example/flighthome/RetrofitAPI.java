package com.example.flighthome;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.POST;

public interface RetrofitAPI {

    //get cities and airports from api
    @GET("airportDatabase")
    Call<JsonArray> createCities(@Query ("key") String key);

    //send cities or airports to server
    @POST("/airport")
    Call<String> postCities(@Body JsonArray city);

    //get iata code for city
    @GET("city/{cityID}")
    Call<JsonObject> getCityIata(@Path("cityID") String name);

    //get future flights by api
    @GET("flightsFuture")
    Call<JsonArray> getFlight(@Query("key")String key,
                               @Query("type") String type,
                               @Query("iataCode") String dep,
                               @Query("arr_iataCode") String arr,
                               @Query("date")String data);
    //get iata code for airports through city they are in
    @GET("airport/{cityiata}")
    Call<JsonArray> getAirportIata(@Path("cityiata")String name);

    //get the specific route information
    @GET("routes")
    Call<JsonArray> getRoute(@Query("key")String key,
                             @Query("departureIata") String dep,
                             @Query("airlineIata") String iata,
                             @Query("flightNumber") String num);
    //get the recent flight information
    @GET("timetable")
    Call<JsonArray> getRecent(@Query("key")String key,
                              @Query("type") String type,
                              @Query("iataCode") String dep,
                              @Query("flight_iata")String air_iata);

}

