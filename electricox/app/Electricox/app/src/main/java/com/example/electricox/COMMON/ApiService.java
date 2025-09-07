package com.example.electricox.COMMON;// or whatever package you're using



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("directions/json")
    Call<RouteResponse> getRoute(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String apiKey
    );
}
