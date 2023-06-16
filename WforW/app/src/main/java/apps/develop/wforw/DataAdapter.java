package apps.develop.wforw;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.develop.wforw.databinding.ItemDataBinding;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;
    public static ArrayList<DataModel> dataList;
    ItemDataBinding binding;

    String cityName;

    public DataAdapter(Context context, ArrayList<DataModel> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemDataBinding.inflate(LayoutInflater.from(context));

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel model = dataList.get(position);
        if (model !=null){

            binding.name.setText(model.getName());
            binding.address.setText(model.getAddress());
            binding.phone.setText(model.getPhone());
            binding.email.setText(model.getEmail());
            binding.priceRange.setText(model.getPrice());


            binding.btnLocate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context,MapsActivity.class);
                    intent.putExtra("position",position);
                    context.startActivity(intent);


                }
            });

            binding.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askPermissions(model);
                }
            });

            try {
                getCityName(model.getLat(),model.getLat());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    private void askPermissions(DataModel model){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},
                    100);
        }
        else
        {
            try {
                String phone = model.getPhone();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
                context.startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        String packageName = "com.google.android.apps.maps";

        customTabsIntent.intent.setPackage(packageName);

        customTabsIntent.launchUrl(activity, uri);
    }
    private void getCityName(double myLat,double myLong) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(myLat, myLong, 1);
        cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);


    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDataBinding binding;
        public ViewHolder(@NonNull ItemDataBinding dataBinding) {
            super(dataBinding.getRoot());
            binding = dataBinding;
        }
    }
}
