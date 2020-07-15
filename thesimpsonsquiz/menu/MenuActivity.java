package com.darylhowedevs.thesimpsonsquiz.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.darylhowedevs.thesimpsonsquiz.QuizActivity;
import com.darylhowedevs.thesimpsonsquiz.R;

public class MenuActivity extends AppCompatActivity {

    private MediaPlayer music;
    private Button takeTheQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        takeTheQuizButton = (Button) findViewById(R.id.takeTheQuizButton);
        takeTheQuizButton.setAlpha(0);
        takeTheQuizButton.setEnabled(false);
        takeTheQuizButton.animate().alpha(1).setStartDelay(5000).setDuration(3000);

        music = MediaPlayer.create(this, R.raw.everybodyhatesnedflanderssong);
        music.setLooping(true); // Set looping
        music.setVolume(100, 100);
        music.start();

        checkWifiConnection();
        activateTakeTheQuizButton();
    }

    /**
     * A method to check if the user is connected to wifi.
     * @return  boolean where returns true if user is connected to WIFI else false.
     */
    private boolean checkWifiConnection(){

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo WIFI = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (WIFI.isConnected()) {
            return true;
        }else{
            Toast.makeText(getApplicationContext(), "Please ensure you are connected to WIFI or you have your mobile data switched on.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * A method to activate the 'Take The Quiz' button after a set amount of time. .
     */
    private void activateTakeTheQuizButton(){

        CountDownTimer timer = new CountDownTimer(8000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                takeTheQuizButton.setEnabled(true);
            }
        };

        timer.start();
    }

    /**
     * A method which loads the main quiz game when called if the user is connected to WIFi.
     * @param view
     */
    public void playNowButtonPressed(View view){

        if (checkWifiConnection()) {
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Please ensure you are connected to WIFI or you have your mobile data switched on.", Toast.LENGTH_LONG).show();
        }
    }
}
