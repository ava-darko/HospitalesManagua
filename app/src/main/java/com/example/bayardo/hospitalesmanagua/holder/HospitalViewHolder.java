package com.example.bayardo.hospitalesmanagua.holder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bayardo.hospitalesmanagua.R;
import com.example.bayardo.hospitalesmanagua.activity.InformationActivity;
import com.example.bayardo.hospitalesmanagua.fragment.MapsFragment;

public class HospitalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView id;
    private TextView name;
    private TextView description;
    private TextView address;
    private TextView phone;
    private TextView photo;
    private Button information;
    private TextView type;

    private Context context;
    private Intent intent;


    public HospitalViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();

        setId((TextView) itemView.findViewById(R.id.tvid));
        setName((TextView) itemView.findViewById(R.id.tvname));
        setDescription((TextView) itemView.findViewById(R.id.tvdescription));
        setAdress((TextView) itemView.findViewById(R.id.tvaddress));
        setPhone((TextView) itemView.findViewById(R.id.tvtel));
        setInformation((Button) itemView.findViewById(R.id.btninformation));
        setPhoto((TextView) itemView.findViewById(R.id.tvphoto));
        setType((TextView) itemView.findViewById(R.id.tvtype));
    }

    public void setOnClickListeners(){
        getInformation().setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btninformation:
                intent = new Intent(context, InformationActivity.class);
                intent.putExtra("Id", getId().getText().toString());
                intent.putExtra("Name", getName().getText().toString());
                intent.putExtra("Description", getDescription().getText().toString());
                intent.putExtra("Address", getAdress().getText().toString());
                intent.putExtra("Tel", getPhone().getText().toString());
                intent.putExtra("Photo", getPhoto().getText().toString());
                intent.putExtra("Type",getType().getText().toString());
                context.startActivity(intent);
                break;
        }
    }

    public TextView getId() {
        return id;
    }

    public void setId(TextView id) {
        this.id = id;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public TextView getAdress() {
        return address;
    }

    public void setAdress(TextView adress) {
        this.address = adress;
    }

    public TextView getPhoto() {
        return photo;
    }

    public void setPhoto(TextView photo) {
        this.photo = photo;
    }


    public Button getInformation() {
        return information;
    }

    public void setInformation(Button information) {
        this.information = information;
    }

    public TextView getPhone() {
        return phone;
    }

    public void setPhone(TextView phone) {
        this.phone = phone;
    }

    public TextView getType() {
        return type;
    }

    public void setType(TextView type) {
        this.type = type;
    }
}