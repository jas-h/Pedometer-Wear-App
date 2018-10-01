package comp5216.sydney.edu.au.pedometerwearapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private TextView stepsValue;
    Button resetButton;
    SensorManager sMgr;
    Sensor step;
    boolean running = false;
    float counter = 0;
    float totalSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepsValue = (TextView) findViewById(R.id.stepsValue);
        resetButton = (Button) findViewById(R.id.resetButton);

        // Enables Always-on
        setAmbientEnabled();

        // Instantiate an object of the SensorManager class
        sMgr = (SensorManager)this.getSystemService(SENSOR_SERVICE);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsValue.setText(0);

                // Update the counter when reset button clicked
                counter = totalSteps;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        running = true;

        // Instantiate the object of Sensor class
        step = sMgr.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (step == null) {
            Toast.makeText(this, "No Step Counter Sensor!", Toast.LENGTH_SHORT).show();
        } else {

            // Register our MainActivity to receive the updates of user's steps
            sMgr.registerListener((SensorEventListener) this, step, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        sMgr.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // This method will be called when the Step Counter sensor detects some steps
    public void onSensorChanged(SensorEvent event) {
        if(running) {
            // The step counter is only reset at reboot so we have to subtract it for each reset
            stepsValue.setText("" + (event.values[0] - counter));
            totalSteps = event.values[0];
        }

    }
}
