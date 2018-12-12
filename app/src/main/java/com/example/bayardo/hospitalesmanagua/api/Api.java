package com.example.bayardo.hospitalesmanagua.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private final static String URL = "https://hospitals-api.herokuapp.com/api/";

    public static ApiInterface instance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiInterface.class);
    }
}
