package com.hackathon.spectralnischay.next36;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Hub hub = Hub.getInstance();
        if (!hub.init(this)) {
            Log.e("Sina", "Could not initialize the Hub.");
            finish();
            return;
        }

        hub.pairWithAdjacentMyo();
        hub.addListener(mListener);


//        Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToStartScreen(View view) {
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

    private DeviceListener mListener = new AbstractDeviceListener() {
        @Override
        public void onPair(Myo myo, long l) {
            Toast.makeText(getApplicationContext(), "paired", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "MAC" + myo.getMacAddress(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onConnect(Myo myo, long l) {
            Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDisconnect(Myo myo, long l) {
        }

        @Override
        public void onArmRecognized(Myo myo, long l, Arm arm, XDirection xDirection) {
        }

        @Override
        public void onArmLost(Myo myo, long l) {
        }

        @Override
        public void onPose(Myo myo, long l, Pose pose) {
            Toast.makeText(getApplicationContext(), "Pose: " + pose, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onOrientationData(Myo myo, long l, Quaternion quaternion) {
        }

        @Override
        public void onAccelerometerData(Myo myo, long l, Vector3 vector3) {
        }

        @Override
        public void onGyroscopeData(Myo myo, long l, Vector3 vector3) {
        }

        @Override
        public void onRssi(Myo myo, long l, int i) {
        }
    };

}
