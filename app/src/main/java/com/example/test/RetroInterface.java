package com.example.test;
import retrofit2.Call;
import retrofit2.http.GET;

public class RetroInterface {
    static String JSONURL="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @GET("baking.json")
    Call<String> getString() {
        return null;
    }
}
