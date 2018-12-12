package com.example.bayardo.hospitalesmanagua.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bayardo.hospitalesmanagua.R;
import com.example.bayardo.hospitalesmanagua.fragment.MapsFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_CALL_PHONE=1;

    private ImageView call;
    private ImageView ubication;
    private TextView name;
    private TextView description;
    private TextView address;
    private TextView numero;
    private ImageView photo;

    //Recuperar Datos
    private String hospitalId;
    private String hospitalName;
    private String hospitalDescription;
    private String hospitalAddress;
    private String hospitalnumero;
    private String hospitalphoto;
    private String hospitalType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_information);

        call= (ImageView) findViewById(R.id.btncall);
        ubication=(ImageView) findViewById(R.id.btnlocation);
        call.setOnClickListener(this);
        ubication.setOnClickListener(this);

        initializeViews();
        getExtras();
        AgregarPhoto();
        mostrarDatos();
    }

    public void AgregarPhoto(){
        String photo=hospitalphoto;
        Uri uri = Uri.parse(photo);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.ivphoto);
        draweeView.setImageURI(uri);
    }

    public void initializeViews (){
        name = findViewById(R.id.tvname);
        description = findViewById(R.id.tvdescription);
        address = findViewById(R.id.tvaddress);
        numero= findViewById(R.id.tvtel);
        photo=findViewById(R.id.ivphoto);
    }

    private void getExtras(){
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            hospitalId = extras.getString("Id");
            hospitalName = extras.getString("Name");
            hospitalDescription = extras.getString("Description");
            hospitalAddress = extras.getString("Address");
            hospitalnumero = extras.getString("Tel");
            hospitalphoto=extras.getString("Photo");
            hospitalType=extras.getString("Type");
        }
    }
    private void mostrarDatos(){
        name.setText(hospitalName);
        description.setText(hospitalDescription);
        address.setText(hospitalAddress);
        numero.setText(hospitalnumero);
    }

    @Override
    public void onClick(View view) {
switch (view.getId()) {
        case R.id.btncall:
            if (ActivityCompat.checkSelfPermission(InformationActivity.this, Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(this, "No se tiene permiso para realizar llamadas telefónicas", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_CALL_PHONE);
                }
            }
            break;
        case R.id.btnlocation:
            Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("Id",hospitalId);
            intent.putExtra("Name",hospitalName);
            intent.putExtra("Type",hospitalType);
            startActivity(intent);
            break;
        }
    }

    public void callPhone(){
        String numero=hospitalnumero;
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+numero));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CALL_PHONE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permiso Concedido",Toast.LENGTH_SHORT).show();
                    callPhone();
                } else {
                    buildDialog(InformationActivity.this).show();

                }
                return;
            }
        }
    }
    public android.support.v7.app.AlertDialog.Builder buildDialog(Context c) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(c);
        builder.setTitle("Advertencia");
        builder.setMessage("Se ha desactivado el permiso de llamadas, si desea activarlo puede hacerlo desde el menu de aplicaciones");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }
}
