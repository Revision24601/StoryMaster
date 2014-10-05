package com.hackathon.spectralnischay.next36;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;

import java.util.Timer;
import java.util.TimerTask;


public class StartScreenActivity extends Activity {

    private double mOldX;
    private double mY;
    private double mW;
    private double mZ;
    private double mPitch;
    private double mYaw;
    private double mRoll;
    private boolean taskScheduled = false;

    private int circleStage = 0;
    private int circleDataCount = 0;
    private double mLastCirclePitch = 0;
    private double circleErrorCount = 0;
    private double mCircleStartPitch = mPitch;
    private double circleStartYaw = 0;
    private double minYaw;
    private double maxYaw;

    public enum Scene {
        ONE, TWO, THREE, FOUR,
        THREEPAUSE, FIVE
    }

    Scene mScene = Scene.ONE;
    View mStartScreenView;
    ImageView mStoryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        mStartScreenView = findViewById(R.id.start_screen_layout);
        mStoryImage = (ImageView)findViewById(R.id.story_image);

        mStoryImage.setImageResource(R.drawable.rocket1);

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
            if (mScene == Scene.ONE && pose == Pose.WAVE_IN) {
                mStartScreenView.setBackgroundColor(getResources().getColor(R.color.myosdk__button_red));
                mStoryImage.setImageResource(R.drawable.rocket2);
                mScene = Scene.TWO;
            }
//            else if (pose == Pose.WAVE_OUT) {
//                switch (mScene) {
//                    case TWO:
//                        mStartScreenView.setBackgroundColor(getResources().getColor(R.color.myosdk__thalmic_blue));
//                        mStoryImage.setImageResource(R.drawable.rocket1);
//                        mScene = Scene.ONE;
//                        break;
//                    case THREE:
//                        mStartScreenView.setBackgroundColor(getResources().getColor(R.color.myosdk__button_red));
//                        mStoryImage.setImageResource(R.drawable.rocket2);
//                        mScene = Scene.TWO;
//                        break;
//                    case FOUR:
//                        mStartScreenView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
//                        mStoryImage.setImageResource(R.drawable.rocket3);
//                        mScene = Scene.THREE;
//                        break;
//                    case FIVE:
//                        mStartScreenView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
//                        mStoryImage.setImageResource(R.drawable.rocket4);
//                        mScene = Scene.FOUR;
//                        break;
//                }
//            }
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


            if (mScene == Scene.TWO && checkForDoorknob()) {
                mStartScreenView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                mStoryImage.setImageResource(R.drawable.rocket3);
                mScene = Scene.THREE;
            }

            if (mScene == Scene.THREE && checkForRocketship()) {
                mStoryImage.setImageResource(R.drawable.rocket31);
                mScene = Scene.THREEPAUSE;
            }

            if (mScene == Scene.THREEPAUSE && !taskScheduled) {
                taskScheduled = true;
                Handler handler = new Handler();
                handler.postDelayed(runnable, 2000);
            }
            if (checkingCircleProgress()) {
                mStartScreenView.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                mStoryImage.setImageResource(R.drawable.endscreen1);
                mScene = Scene.FIVE;
                Log.d("ADAM", "THIS WORKED");
            }
        }

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startCircle();
                mStartScreenView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mStoryImage.setImageResource(R.drawable.rocket4);
                mScene = Scene.FOUR;
            }
        };

        private boolean checkForDoorknob() {
            if (mRoll > 1 ) {
                Log.d("Nischay", "" + mOldX);
                return true;
            }

//            if (mOldX > 0.5 ) {
//
//            }
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

            if (mPitch < -1) {
                return true;
            }
            return false;
        }

        private boolean checkingCircleProgress() {
            //Log.d("ADAM", circleDataCount + " " + circleStage);
            if (circleStage == 0) {
                if (mPitch > mLastCirclePitch) {
                    circleDataCount += 1;
                } else {
                    if (circleDataCount < 90) {
                        circleErrorCount += 1;
                    } else {
                        circleStage = 1;
                        circleErrorCount = 0;
                    }
                }
                if (circleDataCount % 5 == 0) {
                    mLastCirclePitch = mPitch;
                }
                if (circleErrorCount > 30) {
                    circleDataCount = 0;
                    circleErrorCount = 0;
                }
                if (mYaw < maxYaw) {
                    maxYaw = mYaw;
                }
            }

            if (circleStage == 1) {
                //Log.d("ADAM", "stage is 1");

                if (mPitch < mLastCirclePitch) {
                    circleDataCount -= 1;
                } else {
                    circleErrorCount += 1;
                }
                if (circleDataCount % 5 == 0) {
                    mLastCirclePitch = mPitch;
                }
                if (circleDataCount < 40 && -0.25 < (mCircleStartPitch - mPitch))
                {
                    if (checkIfCirclish()) {
                        return true;
                    }
                }
                if (circleErrorCount > 50) {
                    circleDataCount = 0;
                    circleStage = 0;
                    circleErrorCount = 0;
                }
                if (mYaw > minYaw) {
                    minYaw = mYaw;
                }
            }
            return false;
        }

        private boolean checkIfCirclish() {
            double leftDiff = circleStartYaw - minYaw;
            double rightDiff = maxYaw - circleStartYaw;
//            boolean areSidesEqual = -0.5 < (rightDiff - leftDiff) && (rightDiff - leftDiff) < 0.5;
            boolean isNotAVerticalOval = rightDiff < -0.1;
            return isNotAVerticalOval;
        }

        public void startCircle() {
            circleStage = 0;
            mLastCirclePitch = mPitch;
            mCircleStartPitch = mPitch;
            circleStartYaw = mYaw;
            maxYaw = mYaw;
            minYaw = mYaw;
            Log.d("ADAM", "start circle");

            circleDataCount = 0;
            circleErrorCount = 0;
        }

        @Override
        public void onAccelerometerData(Myo myo, long l, Vector3 vector3) {
//            if (vector3.x() < -0.1 && vector3.y() < -0.1) {
//                TextView circleTextView = (TextView) findViewById(R.id.circleTextView);
//                startCircle();
//            }
        }

        @Override
        public void onGyroscopeData(Myo myo, long l, Vector3 vector3) {

        }

        @Override
        public void onRssi(Myo myo, long l, int i) {

        }
    };
}
