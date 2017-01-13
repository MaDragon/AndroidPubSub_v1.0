package com.amazonaws.demo.androidpubsub;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by zhizhong on 2017-01-13.
 */

public class DeviceSensor implements SensorEventListener {

    private final String TAG = DeviceSensor.class.getName();
    private static DeviceSensor instance;
    private  Sensor accelerometer;
    private  Sensor linearAccelerometer;
    private  Sensor magnetometer;
    private SensorManager sensorManager = null;
    private SensorEventListener sensorListener = null;
    // Values used for accelerometer, magnetometer, orientation sensor data
    private float[] G = new float[3]; // gravity x,y,z
    private float[] M = new float[3]; // geomagnetic field x,y,z
    private float[] L = new float[3];
    private float step;
    private final float[] R = new float[9]; // rotation matrix
    private final float[] I = new float[9]; // inclination matrix
    private float[] O = new float[3]; // orientation azimuth, pitch, roll
    private float yaw;
    private  Context context;
    private final Sensor stepCounter;
    private float lastStep=0.0f;
    public String getStateDeviceMovement() {
        return StateDeviceMovement;
    }

    public void setStateDeviceMovement(String stateDeviceMovement) {
        StateDeviceMovement = stateDeviceMovement;
    }

    private String StateDeviceMovement;

    public String getStateUserMovement() {
        return StateUserMovement;
    }

    public String namei1;
    public void setStateUserMovement(String stateUserMovement) {
        StateUserMovement = stateUserMovement;
    }

    private String StateUserMovement;


    public DeviceSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        stepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);



    }

    public void startDeviceSensor() {
        sensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_FASTEST);




    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.v(TAG, "onSensorChanged() entered");
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.v(TAG, "Accelerometer -- x: " + sensorEvent.values[0] + " y: "
                    + sensorEvent.values[1] + " z: " + sensorEvent.values[2]);
            G = sensorEvent.values;

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.v(TAG, "Magnetometer -- x: " + sensorEvent.values[0] + " y: "
                    + sensorEvent.values[1] + " z: " + sensorEvent.values[2]);
            M = sensorEvent.values;
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            Log.v(TAG, "StepCounter -- x: " + sensorEvent.values[0]);

            step += sensorEvent.values[0];




        }else if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Log.v(TAG, "Linear -- x: " + sensorEvent.values[0]);
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            L = sensorEvent.values;

            float diff = (float) Math.sqrt(x * x + y * y + z * z);

            if (diff > 0.5) // 0.5 is a threshold, you can test it and change it
            {
                setStateDeviceMovement("MOVING");
            }
            else{
                setStateDeviceMovement("STILL");
            }
            Log.d("statemoving","Device motion detected!!!!"+getStateDeviceMovement());
        }
        if(getStateDeviceMovement()!=null){
            Log.d("ssd","device;+"+getStateDeviceMovement());
            float currentStep=step;
            if(getStateDeviceMovement()=="MOVING"&&lastStep!=currentStep){
                setStateUserMovement("UserMoving");
                Log.d("ssd","device;111+"+getStateDeviceMovement()+"!!!"+lastStep+"222"+currentStep+" 33 "+getStateUserMovement());
                lastStep=step;

            }else{
                setStateUserMovement("UserStill");
                Log.d("ssd","device;222+"+getStateDeviceMovement()+"!!!"+lastStep+"222"+currentStep);

            }
            Log.d("ssd","device;333+"+getStateDeviceMovement()+"!!!"+lastStep+"222"+currentStep);

        }
        if (G != null && M != null) {
            if (SensorManager.getRotationMatrix(R, I, G, M)) {
                float[] previousO = O.clone();
                O = SensorManager.getOrientation(R, O);
                yaw = O[0] - previousO[0];
                Log.v(TAG, "Orientation: azimuth: " + O[0] + " pitch: " + O[1] + " roll: " + O[2] + " yaw: " + yaw);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}



