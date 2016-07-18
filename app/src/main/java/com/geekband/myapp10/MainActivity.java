package com.geekband.myapp10;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private SensorManager sensorManager;
    private Camera camera;
    private Boolean isOpen = false;
    private long lastClickTime = 0;
    private Camera.Parameters parameters = null;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera = Camera.open();
        parameters = camera.getParameters();

        textView = (TextView) findViewById(R.id.main_text_view);
        textView1 = (TextView) findViewById(R.id.main_text_view1);
        textView2 = (TextView) findViewById(R.id.main_text_view2);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = Math.abs(sensorEvent.values[0]);
            float y = Math.abs(sensorEvent.values[1]);
            float z = Math.abs(sensorEvent.values[2]);
//            Log.i("SOS  ","x = " + x + " y = " + y + " z = " + z);
            textView1.setText("实时：\n x = " + x + " \n y = " + y + "\n z = " + z);
            if(x>16 || y>16 || z>16){
                if(isFastDoubleClick()){
                    Log.i("SOS  ","x = " + x + " y = " + y + " z = " + z);
                    textView2.setText("启用：\n x = " + x + "\n y = " + y + "\n z = " + z);
                    if(!isOpen){
                        openFlashlight();
                        isOpen = true;
                        textView.setText("手电筒打开");
                    }else{
                        closeFlashlight();
                        isOpen = false;
                        textView.setText("手电筒关闭");
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    //打开闪光灯
    public void openFlashlight(){

        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
        camera.setParameters(parameters);
    }

    //关闭闪光灯
    public void closeFlashlight(){
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭
        camera.setParameters(parameters);
    }


    /**
     * 防止1秒内重复触发
     * @return
     */
    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        if (timeD > 1000) {
            return true;
        }else{
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
        if(sensorManager != null){
            sensorManager.unregisterListener(listener);
        }
    }
}
