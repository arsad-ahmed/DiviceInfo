package com.example.deviceinfo.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.deviceinfo.R;
import com.example.deviceinfo.databinding.FragmentGpsBinding;


public class GpsFragment extends Fragment implements LocationListener
{

    private LocationManager locationManager;
    private FragmentGpsBinding binding;
    private double latitude=0.0;
    private double longitude=0.0;

    private ActivityResultLauncher<String> locationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted -> {
        if (isGranted)
        {
            checkGpsEnabled();
        }
        else
        {
            Toast.makeText(requireContext(), "Location Permission is required for this app. Please enable Location.", Toast.LENGTH_SHORT).show();
        }
    });

    private ActivityResultLauncher<Intent> gpsSettingsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if (result.getResultCode() == RESULT_OK)
        {
            startLocationUpdates();
        }
        else
        {
            checkGpsEnabled();
        }
    });

    public GpsFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        locationManager= (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        checkPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_gps, container, false);
        binding.progressCircular.setVisibility(View.VISIBLE);
        return binding.getRoot();
    }

    private void checkPermissions()
    {
        String permissions= android.Manifest.permission.ACCESS_FINE_LOCATION;
        locationPermissionLauncher.launch(permissions);

    }

    private void startLocationUpdates()
    {
        if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        latitude=location.getLatitude();
        longitude=location.getLongitude();


        binding.progressCircular.setVisibility(View.INVISIBLE);

        binding.gpsLatTv.setText(String.valueOf(latitude));
        binding.gpsLonTv.setText(String.valueOf(longitude));

    }

    private void checkGpsEnabled()
    {
        Boolean isGpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!isGpsEnabled)
        {
            new AlertDialog.Builder(requireContext()).setTitle("Enable GPS")
                    .setMessage("GPS is required for this app. Please enable GPS.")
                    .setPositiveButton("OK", (dialog, which) ->
                    {
                        dialog.dismiss();
                        Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        gpsSettingsLauncher.launch(intent);

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setCancelable(false)
                    .show();
        }
        else
        {
            startLocationUpdates();
        }
    }

}