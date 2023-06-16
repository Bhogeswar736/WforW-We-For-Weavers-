package apps.develop.wforw;

import static apps.develop.wforw.DataAdapter.dataList;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import android.view.View;

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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import apps.develop.wforw.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    int position;
    DataModel model;


    String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        position = getIntent().getIntExtra("position",0);
        model = dataList.get(position);


        binding.btnShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://www.google.co.in/maps/dir/"+cityName+"/"+model.getAddress();

                CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();

                customIntent.setToolbarColor(ContextCompat.getColor(MapsActivity.this, R.color.colorPrimary));


                openCustomTab(MapsActivity.this, customIntent.build(), Uri.parse(url));

            }
        });


        try {
            getCityName(model.getLat(),model.getLon());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        String packageName = "com.google.android.apps.maps";

        customTabsIntent.intent.setPackage(packageName);

        customTabsIntent.launchUrl(activity, uri);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng value = new LatLng(model.getLat(),model.getLon());
        Marker myMarker= googleMap.addMarker(new MarkerOptions()
                .position(value)
                .title(model.getName())
                .icon(bitmapDescriptor(getApplicationContext(),R.drawable.location)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(value,15);
        mMap.animateCamera(cameraUpdate);


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