package edu.cmsc425.doyle.drawingturnbasedgame;

import edu.cmsc425.doyle.drawingturnbasedgame.model.Actor;
import edu.cmsc425.doyle.drawingturnbasedgame.model.AttackType;
import edu.cmsc425.doyle.drawingturnbasedgame.model.Computer;
import edu.cmsc425.doyle.drawingturnbasedgame.model.Player;
import edu.cmsc425.doyle.drawingturnbasedgame.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 */
public class FightScreen extends Activity {

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    boolean playerTurn;
    boolean continueMusic;
    Actor a1, a2;
    private ImageButton trace;
    private ImageButton endButton;
    private DrawingView drawingView;
    private TextView countDown;
    private AttackType currType;
    private ProgressBar spinner, enemyHp, playerHp;
    private LinearLayout playerLayout, attackButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fight_screen);

        setup();
    }

    private void setup() {
        continueMusic = false;
        a1 = new Player();
        a2 = new Computer();
        currType = AttackType.BASIC;
        playerLayout = (LinearLayout)findViewById(R.id.player_display);
        attackButtons = (LinearLayout)findViewById(R.id.attack_buttons);
        trace = (ImageButton)findViewById(R.id.traceButton);
        countDown = (TextView)findViewById(R.id.countDown);
        drawingView = (DrawingView)findViewById(R.id.drawing_view);
        drawingView.setCountDown(countDown);
        drawingView.setBitmap(a1.getTraceImages().get(currType));

        endButton = (ImageButton)findViewById(R.id.endButton);
        endButton.setClickable(false);

        playerTurn = true;

        //Initialize health bars
        enemyHp = (ProgressBar)findViewById(R.id.enemy_health);
        enemyHp.setMax(a2.getHealth());
        enemyHp.setProgress(a2.getHealth());

        playerHp = (ProgressBar)findViewById(R.id.player_health);
        playerHp.setMax(a1.getHealth());
        playerHp.setProgress(a1.getHealth());

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        spinner.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        takeTurn();
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
        MusicManager.start(this, MusicManager.MUSIC_GAME, false);
    }

    /**
     * Sets it up so the player can press the button
     */
    public void takeTurn() {
        if(a1.isDead()) {
            //game over, need to display end text
            Log.i("GameOver", "Loser!");
            endButton.setImageDrawable(getDrawable(R.drawable.lose_image));
            endButton.setVisibility(View.VISIBLE);
            endButton.setClickable(true);
        } else if (a2.isDead()) {
            //Game over, need to display win text
            Log.i("GameOver", "Winner!");
            endButton.setImageDrawable(getDrawable(R.drawable.win_image));
            endButton.setVisibility(View.VISIBLE);
            endButton.setClickable(true);
        }

        if (playerTurn) {
            //Enable the button and then handle the turn in onActivityResult
            trace.setEnabled(true);
            trace.setVisibility(View.VISIBLE);
        } else {
            trace.setEnabled(false);
            trace.setVisibility(View.INVISIBLE);


//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    spinner.setVisibility(View.VISIBLE);
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    spinner.setVisibility(View.INVISIBLE);
//                }
//            });


            a1.takeDamage(a2.attack(AttackType.BASIC));
            playerHp.setProgress(a1.getHealth());
            playerTurn = !playerTurn;
            takeTurn();
        }
    }

    public void onTraceClick(View v) {
        v.setVisibility(View.INVISIBLE);
        drawingView.setBitmap(a1.attackImage(currType));
        drawingView.setVisibility(View.VISIBLE);
        countDown.setText("5.000");
        playerLayout.setVisibility(View.INVISIBLE);
        attackButtons.setVisibility(View.INVISIBLE);
        countDown.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            public void run() {

                synchronized (drawingView.lock) {
                    try {
                        DrawingView.lock.wait();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawingView.setVisibility(View.INVISIBLE);
                                playerLayout.setVisibility(View.VISIBLE);
                                countDown.setVisibility(View.INVISIBLE);
                                attackButtons.setVisibility(View.VISIBLE);
                                double percentage = drawingView.getScore();
                                a2.takeDamage((int)(a1.attack(currType) * percentage));
                                enemyHp.setProgress(a2.getHealth());
                                //Players turn is now over
                                playerTurn = false;
                                takeTurn();
                            }
                        });
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }).start();
    }

    /**
     * onClick method for the attack selection buttons
     * @param v
     */
    public void onButtonClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sword_button:
                currType = AttackType.BASIC;
                break;
            case R.id.arrow_button:
                currType = AttackType.RANGE;
                break;
            case R.id.magic_button:
                currType = AttackType.MAGIC;
                break;
        }

        trace.setImageBitmap(BitmapFactory.decodeResource(getResources(), a1.attackImage(currType)));
    }


    /**
     * Resets the activity for a new round with the same characters
     *
     * TODO: Add handling for yes/no and going to different activity
     * @param v
     */
    public void onEndClick(View v) {
        endButton.setVisibility(View.INVISIBLE);
        setup();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Intent.FILL_IN_DATA) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //We finished the request and can now calculate damage
                double percentage = data.getDoubleExtra("score", 0);
                a2.takeDamage((int)(a1.attack(AttackType.BASIC) * percentage));
                enemyHp.setProgress(a2.getHealth());
                //Players turn is now over
                playerTurn = false;
            }
        }
    }
}
