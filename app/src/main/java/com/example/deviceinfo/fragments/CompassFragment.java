package com.example.deviceinfo.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.example.deviceinfo.R;
import com.example.deviceinfo.databinding.FragmentCompassBinding;
import com.example.deviceinfo.databinding.FragmentGpsBinding;

import java.lang.reflect.Array;

public class CompassFragment extends Fragment implements SensorEventListener
{
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private FragmentCompassBinding binding;
    private AppCompatImageView compassImage;

    float currentDegree=0f;

    public CompassFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_compass, container, false);
        compassImage=binding.compassIv;

        initSensor();
        return binding.getRoot();
    }

    private void initSensor()
    {
        sensorManager= (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (accelerometer != null && magnetometer != null)
        {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else
        {
            Toast.makeText(requireContext(), "Sensors not available", Toast.LENGTH_SHORT).show();
            binding.compassInfo.setText("Required sensors are not available in your device");
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
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
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            float degree = event.values[0];
            String direction = getDirection(degree);

            RotateAnimation rotateAnimation = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);

            compassImage.startAnimation(rotateAnimation);
            currentDegree = -degree;

            binding.compassTv.setText((int) degree+"° "+direction);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    private String getDirection(float degree)
    {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

        int index = (int) ((degree + 22.5) / 45) & 7;
        return directions[index];
    }
}