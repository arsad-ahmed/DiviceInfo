package com.example.deviceinfo.fragments;

import static android.content.Context.SENSOR_SERVICE;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deviceinfo.R;
import com.example.deviceinfo.databinding.FragmentAccelerometerBinding;


public class AccelerometerFragment extends Fragment implements SensorEventListener
{

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private FragmentAccelerometerBinding binding;

    public AccelerometerFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sensorManager= (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_accelerometer, container, false);

        return binding.getRoot();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {

            setTextViews();

            float x=event.values[0];
            float y=event.values[1];
            float z=event.values[2];


            double acceleration=sqrt(x * x + y * y + z * z);

            float THRESHOLD = 2.0f;
            if(acceleration> THRESHOLD)
            {
                if(abs(x)>abs(y))
                {
                    if(x>0)
                    {
                        // Device is in landscape mode with the screen facing right
                        binding.accIv.setRotation(90f);
                    }
                    else
                    {
                        // Device is in landscape mode with the screen facing left
                        binding.accIv.setRotation(270f);
                    }
                }
                else
                {
                    if(y>0)
                    {
                        // Device is in portrait mode with the screen facing up
                        binding.accIv.setRotation(0f);
                    }
                    else
                    {
                        // Device is in portrait mode with the screen facing down
                        binding.accIv.setRotation(180f);
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    private void setTextViews()
    {
        if(accelerometer!=null)
        {
            binding.accStatus.setText("Available");
            binding.accVendor.setText(accelerometer.getVendor());
            binding.accName.setText(accelerometer.getName());
            binding.accVersion.setText(String.valueOf(accelerometer.getVersion()));
            binding.accPower.setText(accelerometer.getPower() + " mA");
            binding.accResolution.setText(accelerometer.getResolution()+" m/s2");
            binding.accRange.setText(accelerometer.getMaximumRange()+" m/s2");

        }
    }
}