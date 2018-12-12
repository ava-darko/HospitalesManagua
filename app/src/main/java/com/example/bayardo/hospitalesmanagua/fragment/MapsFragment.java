package com.example.bayardo.hospitalesmanagua.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.bayardo.hospitalesmanagua.R;
import com.example.bayardo.hospitalesmanagua.adapter.HospitalAdapter;
import com.example.bayardo.hospitalesmanagua.api.Api;
import com.example.bayardo.hospitalesmanagua.model.CoordinateModel;
import com.example.bayardo.hospitalesmanagua.model.HospitalModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private String lat;
    private String lon;
    private  RealmResults<HospitalModel> results;
    //Recuperar Datos
    private String hospitalname;
    private Boolean hospitaltype;

    private Button btnhibrido,btnnormal,btnsatelital,btnterreno;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        MapFragment fragment = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        }
        fragment.getMapAsync(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_maps,container,false);
        btnhibrido=(Button) vista.findViewById(R.id.hibrido);
        btnhibrido.setOnClickListener(this);
        btnnormal=(Button) vista.findViewById(R.id.normal);
        btnnormal.setOnClickListener(this);
        btnsatelital=(Button) vista.findViewById(R.id.satelital);
        btnsatelital.setOnClickListener(this);
        btnterreno=(Button) vista.findViewById(R.id.terreno);
        btnterreno.setOnClickListener(this);

        return vista;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng hospital = new LatLng(12.1328200,-86.2504000);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital, 12));
        getcoordinates();

    }

    //Obteniendo Datos de la Api
    public void getcoordinates()
    {
        Call<List<CoordinateModel>> call = Api.instance().getcoordinates();
        call.enqueue(new Callback<List<CoordinateModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CoordinateModel>> call, Response<List<CoordinateModel>> response) {
                if (response.isSuccessful()) {
                   ObtenerDatos(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<CoordinateModel>> call, @NonNull Throwable t) {
            getcoordinates();
            }
        });
    }

public void ObtenerDatos(List<CoordinateModel> coordinate){
getFromDataBase();

    for(int x=0;x<results.size();x++) {
        hospitalname=results.get(x).getName();
        hospitaltype=results.get(x).getType();

        lat=coordinate.get(x).getLatitude();
        lon=coordinate.get(x).getLongitude();

        if(hospitaltype==true) {
            // Add a marker in Sydney and move the camera
            LatLng hospital = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.privado)).anchor(0.0f, 1.0f).position(hospital).title(hospitalname).snippet("Hospital Privado"));
        }else{
            LatLng hospital = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.publico)).anchor(0.0f, 1.0f).position(hospital).title(hospitalname).snippet("Hospital Publico"));
        }
    }
}
    //Obtener los datos la BD LOCAL
    private void getFromDataBase() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<HospitalModel> query = realm.where(HospitalModel.class);
        results = query.findAll();
    }

    //Cambios de mapa
    public void mapHybrid(){
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
    public void mapNormal(){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    public void mapSatellite(){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
    public void mapTerrain(){
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.hibrido:
                    mapHybrid();
                    break;

                    case R.id.normal:
                        mapNormal();
                        break;

                        case R.id.satelital:
                        mapSatellite();
                        break;

                        case R.id.terreno:
                            mapTerrain();
                            break;
                        default:
                            break;
        }
    }
}