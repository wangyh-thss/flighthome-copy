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
    @GET("airportDatabase")
    Call<JsonArray> createCities(@Query ("key") String key);


    @POST("/airport")
    Call<String> postCities(@Body JsonArray city);

    @GET("city/{cityID}")
    Call<JsonObject> getCityIata(@Path("cityID") String name);

    @GET("flightsFuture")
    Call<JsonArray> getFlight(@Query("key")String key,
                               @Query("type") String type,
                               @Query("iataCode") String dep,
                               @Query("arr_iataCode") String arr,
                               @Query("date")String data);

    @GET("airport/{cityiata}")
    Call<JsonArray> getAirportIata(@Path("cityiata")String name);

    @GET("routes")
    Call<JsonArray> getRoute(@Query("key")String key,
                             @Query("departureIata") String dep,
                             @Query("airlineIata") String iata,
                             @Query("flightNumber") String num);

    @GET("timetable")
    Call<JsonArray> getRecent(@Query("key")String key,
                              @Query("type") String type,
                              @Query("iataCode") String dep,
                              @Query("flight_iata")String air_iata);

}

