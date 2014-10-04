package com.hackathon.spectralnischay.next36;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.thalmic.myo.scanner.ScanActivity;

/**
 * Created by sina on 2014-10-04.
 */
public class LauncherActivity extends Activity {

    Button mDebugButton;
    Button mStartStoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getActionBar().hide();


        mDebugButton = (Button)findViewById(R.id.debug);
        mStartStoryButton = (Button)findViewById(R.id.start_story);

        mDebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent debugIntent = new Intent(getApplicationContext(), MainActivity.class);
                debugIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(debugIntent);

            }
        });

        mStartStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startStoryIntent = new Intent(getApplicationContext(), StartScreenActivity.class);
                startStoryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startStoryIntent);
            }
        });
    }
}
