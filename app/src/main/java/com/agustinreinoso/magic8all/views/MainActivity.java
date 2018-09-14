package com.agustinreinoso.magic8all.views;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Property;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agustinreinoso.magic8all.R;
import com.agustinreinoso.magic8all.helpers.AudioRecorder;
import com.agustinreinoso.magic8all.managers.MotionManager;
import com.agustinreinoso.magic8all.managers.QuestionManager;
import com.agustinreinoso.magic8all.managers.RecorderManager;
import com.agustinreinoso.magic8all.models.FutureGuess;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private TextView mTextMessage;
    private boolean isMoving = false;
    private TextView mtextTitle;
    private RecorderManager recorderManager;
    private MotionManager sensorManager;
    private boolean isRecording = false;
    private Menu menuOption;
    private QuestionManager questionManager;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        menuOption = navigation.getMenu();

        navigation.setOnNavigationItemSelectedListener(this);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    2912);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2912);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2912);
        }
        img = findViewById(R.id.imgball);
        mtextTitle = findViewById(R.id.title);
        sensorManager = new MotionManager(getApplicationContext(), Sensor.TYPE_ACCELEROMETER);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.btn_ask:
                startAudioOperation();
                isRecording = !isRecording;
                ProgressBar progressBar = findViewById(R.id.loading);
                recorderManager.startOperation();
                if (isRecording) {
                    progressBar.setVisibility(View.VISIBLE);
                    menuOption.findItem(R.id.btn_see_future).setEnabled(false);
                    menuOption.findItem(R.id.btn_listen).setEnabled(false);
                    img.setVisibility(View.GONE);

                    mtextTitle.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    menuOption.findItem(R.id.btn_see_future).setEnabled(true);
                    menuOption.findItem(R.id.btn_listen).setEnabled(true);
                }
                return true;


            case R.id.btn_listen:
                startAudioOperation();
                recorderManager.controlPlaying();
                return true;

            case R.id.btn_see_future:

                if (recorderManager != null) {
                    recorderManager.cleanReources();
                }
                img.setVisibility(View.VISIBLE);
                mtextTitle.setVisibility(View.VISIBLE);
                mtextTitle.setTextSize(28);
                mtextTitle.setText("Shake Ball to See the Future");
                mtextTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                SpringAnimation animation = new SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, 0);
                animation.setStartValue(800);
                animation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
                animation.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
                animation.start();

                sensorManager.registerListener(this, SensorManager.SENSOR_DELAY_NORMAL);

                return true;
        }
        return false;
    }

    public void startAudioOperation() {
        if (recorderManager == null) {
            recorderManager = new RecorderManager(new AudioRecorder());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if (sensorManager.hasShaken(x, y, z)) {

                if (isMoving == false) {

                    double scale = Math.pow(10, 4);
                    if ((Math.round(x * scale) / scale) > 8.0000) {
                        isMoving = true;
                        ObjectAnimator animator = ObjectAnimator.ofFloat(img, "X", 0);
                        animator.setDuration(300);
                        animator.addListener(animatorListener);
                        animator.start();

                    } else if ((Math.round(x * scale) / scale) < -8.0000) {

                        ObjectAnimator animator = ObjectAnimator.ofFloat(img, "X", 1000);
                        animator.setDuration(300);
                        animator.addListener(animatorListener);
                        animator.start();
                    }

                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.removeListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            ObjectAnimator animator = ObjectAnimator.ofFloat(img, "X", 500);
            animator.setDuration(500);
            animator.start();
            isMoving = false;
            SpringAnimation springAnimationn = new SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, 0);
            springAnimationn.setStartValue(800);
            springAnimationn.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
            springAnimationn.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
            springAnimationn.start();

            if (questionManager == null) {
                questionManager = new QuestionManager();
            }
            FutureGuess futureGuess = questionManager.guessFuture();
            mtextTitle.setText(futureGuess.getGuess());
            mtextTitle.setTextColor(futureGuess.getDangerLevel());
            mtextTitle.setTextSize(40);

        }
    };
}
