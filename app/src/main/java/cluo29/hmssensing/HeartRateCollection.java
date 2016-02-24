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

public class HeartRateCollection extends Service implements SensorEventListener {

    public static final String ACTION_SENSOR_WATERMARKING = "ACTION_SENSOR_WATERMARKING";

    public static final String EXTRA_DATA = "data";

    private static SensorManager mSensorManager;
    private static Sensor mHeartRate;
    private static int heart_rate;


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
        mHeartRate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        /** get sensor data as fast as possible */
        //public static final int SENSOR_DELAY_FASTEST = 0;
        /** rate suitable for games */
        //public static final int SENSOR_DELAY_GAME = 1;
        /** rate suitable for the user interface  */
        //public static final int SENSOR_DELAY_UI = 2;
        /** rate (default) suitable for screen orientation changes */
        //public static final int SENSOR_DELAY_NORMAL = 3;

        //testing frequency
        mSensorManager.registerListener(this, mHeartRate, 0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_HEART_RATE) {

            if(start_time == 0)
            {
                start_time = System.currentTimeMillis();

                last_time = start_time;

                Log.d("SENSORS11", "sensing starts at = " + start_time);
            } else
            {
                intervalSum = intervalSum - last_time + System.currentTimeMillis();

                last_time = System.currentTimeMillis();
            }

//normal test
            if(System.currentTimeMillis() - start_time > duration * 1000 &&testing)
            {
                testing = false;

                Log.d("SENSORS11", "In " + duration +" seconds, avg interval = " + intervalSum*1.0/count);

                Log.d("SENSORS11", "In " + duration + " seconds,(RECORD THIS ) sent readings = " + count);

                mSensorManager.unregisterListener( this ,mHeartRate);
                //stopSelf();
            }

            //Log.d("SENSORS11", "In sensing");

            count++;


            heart_rate = (int) event.values[0];

            Intent intent = new Intent(ACTION_SENSOR_WATERMARKING);
            intent.putExtra(EXTRA_DATA, heart_rate);
            sendBroadcast(intent);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_HEART_RATE)
        {
            if(start_time == 0)
            {
                start_time = System.currentTimeMillis();

                Log.d("SENSORS10", "sensing starts at = " + start_time);
            }

            if(System.currentTimeMillis() - start_time > duration * 1000)
            {
                Log.d("SENSORS10", "In " + duration +" seconds, sent readings = " + heart_rate_count);
            }

            heart_rate = (int) event.values[0];

            heart_rate_count++;

            Intent intent = new Intent(ACTION_SENSOR_WATERMARKING);
            intent.putExtra(EXTRA_DATA, heart_rate);
            sendBroadcast(intent);


        }
    }

}
*/