package cluo29.hmssensing;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Comet on 01/02/16. v02011816
 */

//This service collects sensor data and sends intent

public class Collection extends Service implements SensorEventListener{

    public static final String ACTION_SENSOR_WATERMARKING = "ACTION_SENSOR_WATERMARKING";

    public static final String EXTRA_DATA = "data";

    private static SensorManager mSensorManager;
    private static Sensor mHeartRate;
    private static int heart_rate;

    //testing conditions: duration, sensing frequency: SENSOR_DELAY_FASTEST
    //testing duration
    //we test for 300 seconds
    private static int duration = 300;

    //how many heart rate rows are got
    private static int heart_rate_count=0;

    //start time of testing
    private static long start_time = 0;

    @Override
    public void onCreate() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mHeartRate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        //testing frequency
        mSensorManager.registerListener(this, mHeartRate, SensorManager.SENSOR_DELAY_FASTEST);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_HEART_RATE)
        {
            if(System.currentTimeMillis() - start_time > duration * 1000)
            {
                Log.d("SENSORS10", "In " + duration +" seconds, sent readings = " + heart_rate_count);
            }

            heart_rate = (int) event.values[0];

            heart_rate_count++;

            Intent intent = new Intent(ACTION_SENSOR_WATERMARKING);
            intent.putExtra(EXTRA_DATA, heart_rate);
            sendBroadcast(intent);

            if(start_time == 0)
            {
                start_time = System.currentTimeMillis();

                Log.d("SENSORS10", "testing starts at = " + start_time);
            }
        }
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {

    }
}
