package com.example.deviceinfo.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deviceinfo.R;
import com.example.deviceinfo.databinding.FragmentGyroscopeBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


public class GyroscopeFragment extends Fragment implements SensorEventListener
{

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;

    private LineChart chart;

    private LineData lineData;
    private LineDataSet lineDataSetX;
    private LineDataSet lineDataSetY;
    private LineDataSet lineDataSetZ;

    private FragmentGyroscopeBinding binding;

    public GyroscopeFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sensorManager= (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_gyroscope, container, false);
        chart=binding.gyroChart;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setupLineChart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            setTextViews();

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            lineData.addEntry(new Entry((float) lineDataSetX.getEntryCount(), x), 0);
            lineData.addEntry(new Entry((float) lineDataSetY.getEntryCount(), y), 1);
            lineData.addEntry(new Entry((float) lineDataSetZ.getEntryCount(), z), 2);

            lineData.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        }

        else
        {
            setTextViews();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    private void setupLineChart()
    {
        chart.setTouchEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        Description description=new Description();
        description.setText("Gyroscope");
        chart.setDescription(description);


        lineData = new LineData();
        lineDataSetX = createLineDataSet("X", requireContext().getColor(R.color.red));
        lineDataSetY = createLineDataSet("Y", requireContext().getColor(R.color.green));
        lineDataSetZ = createLineDataSet("Z", requireContext().getColor(R.color.blue));

        lineData.addDataSet(lineDataSetX);
        lineData.addDataSet(lineDataSetY);
        lineData.addDataSet(lineDataSetZ);

        chart.setData(lineData);
        chart.invalidate();

    }

    private LineDataSet createLineDataSet(String label,int color)
    {
        LineDataSet lineDataSet=new LineDataSet(null, label);
        lineDataSet.setColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawValues(false);
        return lineDataSet;
    }

    private void setTextViews()
    {
        if(gyroscopeSensor!=null)
        {
            binding.gyroStatus.setText("Available");
            binding.gyroVendor.setText(gyroscopeSensor.getVendor());
            binding.gyroName.setText(gyroscopeSensor.getName());
            binding.gyroVersion.setText(String.valueOf(gyroscopeSensor.getVersion()));
            binding.gyroPower.setText(gyroscopeSensor.getPower() + " mA");
            binding.gyroResolution.setText(gyroscopeSensor.getResolution()+" m/s2");
            binding.gyroRange.setText(gyroscopeSensor.getMaximumRange()+" m/s2");

        }

        else
        {
            binding.gyroStatus.setText("Not Available");
            binding.gyroVendor.setText("Not Available");
            binding.gyroName.setText("Not Available");
            binding.gyroVersion.setText("Not Available");
            binding.gyroPower.setText("Not Available");
            binding.gyroResolution.setText("Not Available");
            binding.gyroRange.setText("Not Available");
        }
    }
}