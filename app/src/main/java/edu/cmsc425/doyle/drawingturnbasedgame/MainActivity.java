package edu.cmsc425.doyle.drawingturnbasedgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static edu.cmsc425.doyle.drawingturnbasedgame.R.*;


public class MainActivity extends Activity {

    boolean continueMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_main);

        init();
    }

    private void init() {
        continueMusic = true;
    }

    private void onClick(View v) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        continueMusic = false;
        MusicManager.start(this, MusicManager.MUSIC_MENU);
    }

    public void on1PClick(View v) {
        Intent i = new Intent(this, FightScreen.class);
        startActivity(i);
    }

    public void onBgClick(View v) {
        findViewById(id.main_button_layout).bringToFront();
    }
}
