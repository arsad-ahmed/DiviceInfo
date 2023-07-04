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
import com.example.deviceinfo.databinding.FragmentMagnetometerBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


public class MagnetometerFragment extends Fragment implements SensorEventListener
{

    private SensorManager sensorManager;
    private Sensor magnetometer;

    private LineChart chart;

    private LineData lineData;
    private LineDataSet lineDataSetX;
    private LineDataSet lineDataSetY;
    private LineDataSet lineDataSetZ;

    private FragmentMagnetometerBinding binding;

    public MagnetometerFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sensorManager= (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        magnetometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_magnetometer, container, false);

        chart=binding.magChart;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        configureChart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
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
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
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
    }

    private void configureChart()
    {
        chart.setTouchEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        Description description=new Description();
        description.setText("Magnetometer");
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

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
        if(magnetometer!=null)
        {
            binding.magStatus.setText("Available");
            binding.magVendor.setText(magnetometer.getVendor());
            binding.magName.setText(magnetometer.getName());
            binding.magVersion.setText(String.valueOf(magnetometer.getVersion()));
            binding.magPower.setText(magnetometer.getPower() + " mA");
            binding.magResolution.setText(magnetometer.getResolution()+" m/s2");
            binding.magRange.setText(magnetometer.getMaximumRange()+" m/s2");

        }
    }

}