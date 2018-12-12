package com.example.bayardo.hospitalesmanagua.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.bayardo.hospitalesmanagua.R;
import com.example.bayardo.hospitalesmanagua.api.Api;
import com.example.bayardo.hospitalesmanagua.model.CoordinateModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String HospitalId;
    private String HospitalName;
    private String HospitalType;
    private String lat;
    private  String lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getExtras();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        getcoordinates();
    }
    private void getExtras(){

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            HospitalId=extras.getString("Id");
            HospitalName = extras.getString("Name");
            HospitalType= extras.getString("Type");

        }
    }
    //Obteniendo Datos de la Api
    public void getcoordinates()
    {
        Call<List<CoordinateModel>> call = Api.instance().getcoordinates();
        call.enqueue(new Callback<List<CoordinateModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CoordinateModel>> call, Response<List<CoordinateModel>> response) {
                if (response.isSuccessful()) {
                   obtenerDatos(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<CoordinateModel>> call, @NonNull Throwable t) {
            }
        });
    }
   public void obtenerDatos(List<CoordinateModel> coordinate){

        for (int i=0;i<coordinate.size();i++){
            if (coordinate.get(i).getId().equals(HospitalId)){
                lat=coordinate.get(i).getLatitude();
                lon=coordinate.get(i).getLongitude();
            }
        }
       LatLng hospital = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
       mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).anchor(0.0f, 1.0f).position(hospital).title(HospitalName).snippet("Tipo Hospital: "+HospitalType));
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital, 15));

    }
}
