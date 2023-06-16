package apps.develop.wforw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import apps.develop.wforw.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityMainBinding binding;

    FirebaseUser user;
    private GoogleMap mMap;
    DatabaseReference reference;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.btnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DataActivity.class));
            }
        });

        binding.inputCity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                String destination = binding.inputCity.getText().toString();
                if (destination.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter destination", Toast.LENGTH_SHORT).show();
                }else {
                    String url = "https://www.google.co.in/maps/dir/"+cityName+"/"+destination;

                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();

                    customIntent.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));


                    openCustomTab(MainActivity.this, customIntent.build(), Uri.parse(url));


                }

            }
            return false;
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                    100);
        }

    }
    public void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        String packageName = "com.google.android.apps.maps";

        customTabsIntent.intent.setPackage(packageName);

        customTabsIntent.launchUrl(activity, uri);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    UserModel model = snapshot.getValue(UserModel.class);
                    LatLng value = new LatLng(model.getLatitude(),model.getLongitude());

                    Marker myMarker= googleMap.addMarker(new MarkerOptions()
                            .position(value)
                            .title(model.getUsername())
                            .icon(bitmapDescriptor(getApplicationContext(),R.drawable.location)));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(value,15);
                    mMap.animateCamera(cameraUpdate);

                    try {
                        getCityName(model.getLatitude(),model.getLongitude());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BitmapDescriptor bitmapDescriptor(Context context, int vector){
        Drawable drawable = ContextCompat.getDrawable(context,vector);
        drawable.setBounds(0,0,100,100);

        Bitmap bitmap = Bitmap.createBitmap(100,100,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getCityName(double myLat,double myLong) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(myLat, myLong, 1);
        cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);


    }
}