package com.hackathon.spectralnischay.next36;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;


public class StartScreenActivity extends Activity {

    private double mOldX;
    private double mY;
    private double mW;
    private double mZ;
    private double mPitch;
    private double mYaw;
    private double mRoll;

    View mStartScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        mStartScreenView = findViewById(R.id.start_screen_layout);

        Hub hub = Hub.getInstance();
        if (!hub.init(this)) {
            Log.e("Sina", "Could not initialize the Hub.");
            finish();
            return;
        }

        hub.pairWithAdjacentMyo();
        hub.addListener(mListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_screen, menu);
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

    DeviceListener mListener = new DeviceListener() {
        @Override
        public void onPair(Myo myo, long l) {

        }

        @Override
        public void onConnect(Myo myo, long l) {

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
            if (pose == Pose.WAVE_IN) {
                mStartScreenView.setBackgroundColor(getResources().getColor(R.color.myosdk__button_red));
            }
            else if (pose == Pose.WAVE_OUT) {
                mStartScreenView.setBackgroundColor(getResources().getColor(R.color.myosdk__thalmic_blue));
            }
        }

        @Override
        public void onOrientationData(Myo myo, long l, Quaternion quaternion) {
            mOldX = quaternion.x();
            mY = quaternion.y();
            mW = quaternion.w();
            mZ = quaternion.z();
            mPitch = Quaternion.pitch(quaternion);
            mRoll = Quaternion.roll(quaternion);
            mYaw = Quaternion.yaw(quaternion);


            if (checkForDoorknob()) {
                mStartScreenView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            }

            if (checkForRocketship()) {
                mStartScreenView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        }

        private boolean checkForDoorknob() {
            if (mOldX > 0.5 ) {
                if (mRoll > 1 ) {
                    Log.d("Nischay", "" + mOldX);
                    return true;
                }
            }
            return false;
        }

        private boolean checkForRocketship() {
            double xAvg = 0.5;
            double yAvg = 0.3;
            double wAvg = -0.2;
            double zAvg = 0.75;

            double range = 0.1;
            boolean xCondition = (xAvg - range) < mOldX && mOldX < (xAvg + range);
            boolean yCondition = (yAvg - range) < mY && mY < (xAvg + range);
            boolean wCondition = (wAvg - range) < mW && mW < (xAvg + range);
            boolean zCondition = (zAvg - range) < mZ && mZ < (xAvg + range);

            Log.d("ADAM", xCondition + " " + yCondition + " " + zCondition + " " + wCondition);
            if (mPitch < -1) {
                return true;
            }
            return false;
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
