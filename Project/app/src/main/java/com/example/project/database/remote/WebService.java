package com.example.project.database.remote;

import com.example.project.model.weather.remote.RemoteWeatherReport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebService {

    final static String boxPath = "/box_e997ba1f18a999988402";

    @GET(boxPath+"/reports")
    Call<List<RemoteWeatherReport>> getAllReports();

    @Headers("content-type: application/json")
    @POST(boxPath+"/reports")
    Call<RemoteWeatherReport> postReport(@Body RemoteWeatherReport report);

    @Headers("content-type: application/json")
    @POST(boxPath+"/reports")
    Call<List<RemoteWeatherReport>> postUsers(@Body List<RemoteWeatherReport> reports);
}