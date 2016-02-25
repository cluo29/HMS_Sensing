package cluo29.hmssensing;

/**
 * Created by Comet on 25/02/16.
 */
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ThreeDoubleGenerator extends Service {

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

                    Log.d("SENSORS11", "sensing starts at = " + start_time);
                }

                if (System.currentTimeMillis() - start_time > duration * 1000) {
                    Log.d("SENSORS11", "In " + duration + " seconds, sent readings = " + count);

                    break;
                }

                //Log.d("SENSORS11", "sensing ");


                ContentValues data = new ContentValues();
                data.put("double_x", 65.05826712973);  //latitude
                data.put("double_y", 25.468404709953);  //longitude
                data.put("double_z", 90.473168152242);  //altitude

                Intent intent = new Intent(ACTION_SENSOR_WATERMARKING);
                intent.putExtra(EXTRA_DATA, data);
                sendBroadcast(intent);


                count++;
                try {
                    Thread.sleep(100);
                    //[0] detect once every 0.1 secs
                    //[1] detect once every 1 secs
                    //[2] detect once every 5 secs
                    //[3] detect once every 10 secs
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    public void onCreate() {

        Log.d("SENSORS11", "sensing start");
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
