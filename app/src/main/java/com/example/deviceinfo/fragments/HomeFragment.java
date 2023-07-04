package com.example.deviceinfo.fragments;



import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deviceinfo.R;
import com.example.deviceinfo.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment
{
    private FragmentHomeBinding binding;
    private NavController navController;

    public HomeFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        currentDeviceDetails();
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
    }

    private void currentDeviceDetails()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        binding.mainTv.setText(manufacturer.toUpperCase()+" " +model);
    }

    public void moveToAccelerometerFragment()
    {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToAccelerometerFragment();
        navController.navigate(action);
    }

    public void moveToGyroscopeFragment()
    {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToGyroscopeFragment();
        navController.navigate(action);
    }

    public void moveToMagnetometerFragment()
    {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToMagnetometerFragment();
        navController.navigate(action);
    }

    public void moveToGpsFragment()
    {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToGpsFragment();
        navController.navigate(action);
    }

    public void moveToCompassFragment()
    {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToCompassFragment();
        navController.navigate(action);
    }

    public void moveToProximityFragment()
    {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToProximityFragment();
        navController.navigate(action);
    }

}