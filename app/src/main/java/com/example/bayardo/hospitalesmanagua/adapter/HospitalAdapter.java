package com.example.bayardo.hospitalesmanagua.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bayardo.hospitalesmanagua.R;
import com.example.bayardo.hospitalesmanagua.fragment.MapsFragment;
import com.example.bayardo.hospitalesmanagua.holder.HospitalViewHolder;
import com.example.bayardo.hospitalesmanagua.model.HospitalModel;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalViewHolder>{
    private List<HospitalModel> hospital;

    public HospitalAdapter(List<HospitalModel>hospital){
        this.hospital = hospital;
    }

    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hospital, parent, false);
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HospitalViewHolder holder, int position) {
    String type;
    HospitalModel hospitalmodel= hospital.get(position);

    boolean Type=hospitalmodel.getType();//Aqui obtengo el booleano que viene de la API

    if(Type==true){
        type="Privado";
    }
    else{
        type="Publico";
    }
        holder.getId().setText(hospitalmodel.getId());
        holder.getName().setText(hospitalmodel.getName());
        holder.getDescription().setText(hospitalmodel.getDescription());
        holder.getPhone().setText(hospitalmodel.getPhone());
        holder.getAdress().setText(hospitalmodel.getAddress());
        holder.getPhoto().setText(hospitalmodel.getPhoto_url());
        holder.getType().setText(type);

    holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
      return hospital.size();
    }
}