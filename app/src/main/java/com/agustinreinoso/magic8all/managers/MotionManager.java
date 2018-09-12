package com.agustinreinoso.magic8all.managers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.EventLog;

public class MotionManager {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long lastUpdate = 0;
    private float last_x;
    private float last_y;
    private float last_z;
    private static final int SHAKE_THRESHOLD = 600;


    public MotionManager(Context context, int sensorType) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(sensorType);
    }

    public void registerListener(SensorEventListener sensorlistener, int delay) {
        mSensorManager.registerListener(sensorlistener, mSensor, delay);
    }

    public void removeListener(SensorEventListener sensorlistener) {
        mSensorManager.unregisterListener(sensorlistener);
    }

    public boolean hasShaken(float x, float y, float z) {
        long curTime = System.currentTimeMillis();
        boolean flag = false;

        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
            if (speed > SHAKE_THRESHOLD) {
                flag = true;
            }
        }
        last_x = x;
        last_y = y;
        last_z = z;
        return flag;
    }

}
