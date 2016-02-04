package cluo29.hmssensing;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Comet on 03/02/16.
 */
public class FloatGenerator extends Service {

    public static final String ACTION_SENSOR_WATERMARKING = "ACTION_SENSOR_WATERMARKING";

    public static final String EXTRA_DATA = "data";

    //how many  rows are collected
    private static int count=0;

    //start time of testing
    private static long start_time = 0;

    private static int duration = 300;


    public Thread solar_thread = new Thread() {
        public void run() {
            while(true) {
                if (start_time == 0) {

                    start_time = System.currentTimeMillis();

                    Log.d("SENSORS10", "sensing starts at = " + start_time);
                }

                if (System.currentTimeMillis() - start_time > duration * 1000) {
                    Log.d("SENSORS10", "In " + duration + " seconds, sent readings = " + count);

                    break;
                }

                float data = 255.5f;

                count++;

                Intent intent = new Intent(ACTION_SENSOR_WATERMARKING);
                intent.putExtra(EXTRA_DATA, data);
                sendBroadcast(intent);

                try {
                    Thread.sleep(500);
                    //detect once every 0.1 secs
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    public void onCreate() {

        Log.d("SENSORS10", "sensing start");
        solar_thread.start();

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
