package com.example.bayardo.hospitalesmanagua.api;

import com.example.bayardo.hospitalesmanagua.model.CoordinateModel;
import com.example.bayardo.hospitalesmanagua.model.HospitalModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("hospitals")
    Call<List<HospitalModel>> gethospital();

    @GET("hospitals/coordinates")
    Call<List<CoordinateModel>> getcoordinates();
}
