package cluo29.hmssensing;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Comet on 04/02/16.
 */
public class Magnetometer extends Service implements SensorEventListener {

    public static final String ACTION_SENSOR_WATERMARKING = "ACTION_SENSOR_WATERMARKING";

    public static final String EXTRA_DATA = "data";


    private static SensorManager mSensorManager;
    private static Sensor mMagentometer;

    //how many rows are collected
    private static int count=0;

    private static long last_time=0;

    private static long intervalSum=0;

    //start time of testing
    private static long start_time = 0;

    //testing conditions: sensor type: mMagentometer
    // duration: 300
    // sensing frequency: SENSOR_DELAY_FASTEST
    //testing duration
    //we test for 300 seconds

    private static int duration = 300;

    private static boolean testing = true;

    @Override
    public void onCreate() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //sensor type
        mMagentometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //testing frequency
        mSensorManager.registerListener(this, mMagentometer, SensorManager.SENSOR_DELAY_FASTEST);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            if(start_time == 0)
            {
                start_time = System.currentTimeMillis();

                last_time = start_time;

                Log.d("SENSORS10", "sensing starts at = " + start_time);
            } else
            {
                intervalSum = intervalSum - last_time + System.currentTimeMillis();

                last_time = System.currentTimeMillis();
            }



            if(System.currentTimeMillis() - start_time > duration * 1000 &&testing)
            {
                testing = false;

                Log.d("SENSORS10", "In " + duration +" seconds, avg interval = " + intervalSum*1.0/count);

                Log.d("SENSORS10", "In " + duration +" seconds, sent readings = " + count);
            }

            count++;

            /*
            ContentValues data = new ContentValues();
            data.put("float_x", event.values[0]);
            data.put("float_y", event.values[1]);
            data.put("float_z", event.values[2]);

            Intent intent = new Intent(ACTION_SENSOR_WATERMARKING);
            intent.putExtra(EXTRA_DATA, data);
            sendBroadcast(intent);
            */

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

/*
In this example, the default data delay (SENSOR_DELAY_NORMAL) is specified when the registerListener() method is invoked.
 The data delay (or sampling rate) controls the interval at which sensor events are sent to your application
 via the onSensorChanged() callback method. The default data delay is suitable for monitoring typical screen orientation
 changes and uses a delay of 200,000 microseconds. You can specify other data delays,
  such as SENSOR_DELAY_GAME (20,000 microsecond delay), SENSOR_DELAY_UI (60,000 microsecond delay),
   or SENSOR_DELAY_FASTEST (0 microsecond delay). As of Android 3.0 (API Level 11) you can also
    specify the delay as an absolute value (in microseconds).

The delay that you specify is only a suggested delay. The Android system and other applications
 can alter this delay. As a best practice, you should specify the largest delay that you can
  because the system typically uses a smaller delay than the one you specify (that is, you should
  choose the slowest sampling rate that still meets the needs of your application). Using a larger
   delay imposes a lower load on the processor and therefore uses less power.

 */