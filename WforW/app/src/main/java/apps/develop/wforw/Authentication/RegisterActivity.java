package apps.develop.wforw.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import apps.develop.wforw.GpsTracker;
import apps.develop.wforw.PreferenceManager;
import apps.develop.wforw.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 9999;

    ActivityRegisterBinding binding;

    FirebaseAuth auth;
    DatabaseReference reference;

    PreferenceManager preferenceManager;
    double latitude,longitude;
    GpsTracker gpsTracker;
    LocationManager locationManager;

    Double defLat = 16.4860505;
    Double defLong = 80.6935739;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        gpsTracker = new GpsTracker(this);
        preferenceManager = new PreferenceManager(this);

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);


        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();



        binding.btnSignUp.setOnClickListener(view -> {
            String name = binding.inputUsername.getText().toString();
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show();
            }else {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Creating New Account");
                progressDialog.setCancelable(false);
                progressDialog.show();


                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();

                        FirebaseUser user = auth.getCurrentUser();
                        assert user !=null;

                        HashMap<String,Object> map = new HashMap<>();
                        map.put("username",name);
                        map.put("email",email);
                        map.put("uid",user.getUid());
                        map.put("latitude",latitude);
                        map.put("longitude",longitude);

                        reference.child(user.getUid()).setValue(map).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                progressDialog.dismiss();
                                startActivity(new Intent(this, LoginActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                preferenceManager.putBoolean("signed",false);
                                finish();

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(this, task1.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });













            }


        });

        binding.txtSignIn.setOnClickListener(view -> onBackPressed());


        checkGps();



        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void checkGps(){

        if (gpsTracker.getIsGPSTrackingEnabled()){
            if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(RegisterActivity.this,

                            Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
            else
            {
                Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                if (LocationGps !=null)
                {
                    double lat=LocationGps.getLatitude();
                    double longi=LocationGps.getLongitude();

                    latitude=lat;
                    longitude=longi;

//                    Toast.makeText(this, "Lat: "+latitude, Toast.LENGTH_SHORT).show();





                }
//                else if (LocationNetwork !=null)
//                {
//                    double lat=LocationNetwork.getLatitude();
//                    double longi=LocationNetwork.getLongitude();
//
//                    latitude=lat;
//                    longitude=longi;
//
//                    Toast.makeText(this, "Lat: "+latitude, Toast.LENGTH_SHORT).show();
//
//                }
//                else if (LocationPassive !=null)
//                {
//                    double lat=LocationPassive.getLatitude();
//                    double longi=LocationPassive.getLongitude();
//
//                    latitude=lat;
//                    longitude=longi;
//
//
//                }
                else
                {
                    Toast.makeText(this, "Can't get your location", Toast.LENGTH_SHORT).show();
//                    Toasty.error(getApplicationContext(),"Can't get your location",Toasty.LENGTH_SHORT)
//                            .show();
                }

            }


        }else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                checkGps();
            }
            else {

                latitude = defLat;
                longitude = defLong;
                Toast.makeText(RegisterActivity.this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}