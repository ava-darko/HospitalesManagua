package com.example.bayardo.hospitalesmanagua.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bayardo.hospitalesmanagua.R;
import com.example.bayardo.hospitalesmanagua.activity.MainActivity;
import com.example.bayardo.hospitalesmanagua.adapter.HospitalAdapter;
import com.example.bayardo.hospitalesmanagua.api.Api;
import com.example.bayardo.hospitalesmanagua.model.HospitalModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout srl;
    private RecyclerView.Adapter mAdapter;


    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_list, container, false);
        //OnRefresh
        srl=vista.findViewById(R.id.actualizar);
        //Configuracion Recicler View
        recyclerView = vista.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //VALIDACION SI ESTA CON CONEXION TRAER LOS DATOS DE LA API , SINO CARGAR LOS DE LA BD
                if(isConnected()==true){
                    getFromDataBase();
                }
                else{
                    gethospital();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                }, 1200);
            }
        });
        if(isConnected()==true){
            getFromDataBase();
        }
        else{
            gethospital();
        }


        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Obteniendo Datos de la Api
    public void gethospital()
    {
        Call<List<HospitalModel>> call = Api.instance().gethospital();
        call.enqueue(new Callback<List<HospitalModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<HospitalModel>> call, Response<List<HospitalModel>> response) {
                if (response.isSuccessful()) {
                    HospitalAdapter hospitalAdapter = new HospitalAdapter(response.body());
                    recyclerView.setAdapter(hospitalAdapter);
                    sync(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HospitalModel>> call, @NonNull Throwable t) {
                buildDialog(getActivity()).show();
            }
        });
    }

    //Codigo para verificar Conexion

    public boolean isConnected() {

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<HospitalModel> query = realm.where(HospitalModel.class);

        RealmResults<HospitalModel> results = query.findAll();

        if(results.size()>0){
            return true;
        }else{
            return false;
        }
    }
    //CODIGO PARA TRABAJAR CON LA BASE DE DATOS LOCAL(REALM)
    //--------------------------------------------------------

    //Metodo obtiene los archibos que vienen de la API
    private void sync(List<HospitalModel> hospitalModels) {
        for(HospitalModel hospitalModel : hospitalModels) {
            store(hospitalModel);
        }
    }

    //se encarga de guardar los datos en la BD Local
    private void store(HospitalModel hospitalModelFromApi){
        //Verificar si el dato que viene de la API esta en la BD
        String a=hospitalModelFromApi.getId();
        if (exist(a)==false) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            //Guardar los Datos
            HospitalModel productModel = realm.createObject(HospitalModel.class); // Create a new object

            productModel.setId(hospitalModelFromApi.getId());
            productModel.setName(hospitalModelFromApi.getName());
            productModel.setAddress(hospitalModelFromApi.getAddress());
            productModel.setDescription(hospitalModelFromApi.getDescription());
            productModel.setPhone(hospitalModelFromApi.getPhone());
            productModel.setPhoto_url(hospitalModelFromApi.getPhoto_url());
            productModel.setType(hospitalModelFromApi.getType());
            realm.commitTransaction();
        }

    }
    //Obtener los datos la BD LOCAL
    private void getFromDataBase() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<HospitalModel> query = realm.where(HospitalModel.class);

        RealmResults<HospitalModel> results = query.findAll();

        mAdapter = new HospitalAdapter(results);
        recyclerView.setAdapter(mAdapter);
    }

    //Verificar que no se guarden datos repetidos en la BD
    private boolean exist(String id){

        Boolean exist=false;
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<HospitalModel> query = realm.where(HospitalModel.class);

        RealmResults<HospitalModel> results = query.findAll();

        for (int i=0; i<results.size(); i++)
        {
            if (id.equals(results.get(i).getId()))
            {
                exist=true;
            }
        }
        return exist;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Acceso a Internet");
        builder.setMessage("Verifique la Conexion a internet");

        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                gethospital();
            }
        });
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        return builder;
    }


}
