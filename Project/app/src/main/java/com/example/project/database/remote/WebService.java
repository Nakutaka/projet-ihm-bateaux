package com.example.lastretrofittest.network;

import com.example.lastretrofittest.model.Report;
import com.example.lastretrofittest.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface WebService {

    final static String boxPath = "/box_e997ba1f18a999988402";

    @GET(boxPath+"/tests")
    Call<List<User>> getAllUsers();

    @Headers("content-type: application/json")
    @POST(boxPath+"/tests")
    Call<User> postUser(@Body User user);

    @Headers("content-type: application/json")
    @POST(boxPath+"/tests")
    Call<List<User>> postUsers(@Body List<User> users);

    @GET("/photos")
    Call<List<Report>> getAllPhotos();

    @POST(boxPath)
    Call<Report> postReport(Report report);

    @PUT(boxPath)
    Call<Report> putReport(String jsonId, Report report);

    @DELETE(boxPath)
    Call<Report> deleteReport(String jsonId);
}