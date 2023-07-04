package com.example.deviceinfo.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deviceinfo.R;
import com.example.deviceinfo.databinding.FragmentMagnetometerBinding;
import com.example.deviceinfo.databinding.FragmentProximityBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class ProximityFragment extends Fragment implements SensorEventListener
{
    private SensorManager sensorManager;
    private Sensor proximitySensor;

    private FragmentProximityBinding binding;

    public ProximityFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sensorManager= (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_proximity, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            float distance = event.values[0];
            setTextViews();

            if (distance< proximitySensor.getMaximumRange())
            {
                turnOffScreen();
            }
            else
            {
                turnOnScreen();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    private void turnOffScreen()
    {
        PowerManager powerManager = (PowerManager) requireContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyTag:ProximityScreenOff");
        wakeLock.acquire();
    }

    private void turnOnScreen()
    {
        PowerManager powerManager = (PowerManager) requireContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyTag:ProximityScreenOn");
        wakeLock.acquire();
    }

    private void setTextViews()
    {
        if(proximitySensor!=null)
        {
            binding.proxStatus.setText("Available");
            binding.proxVendor.setText(proximitySensor.getVendor());
            binding.proxName.setText(proximitySensor.getName());
            binding.proxVersion.setText(String.valueOf(proximitySensor.getVersion()));
            binding.proxPower.setText(proximitySensor.getPower() + " mA");
            binding.proxResolution.setText(proximitySensor.getResolution()+" m/s2");
            binding.proxRange.setText(proximitySensor.getMaximumRange()+" m/s2");

        }
    }
}