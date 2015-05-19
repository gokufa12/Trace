package edu.cmsc425.doyle.drawingturnbasedgame;

import edu.cmsc425.doyle.drawingturnbasedgame.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;


/**
 */
public class TraceActivity extends Activity {


    private DrawingView mDrawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trace);
        mDrawingView = (DrawingView) findViewById(R.id.drawing_view);
    }


    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("score", mDrawingView.getScore());
        setResult(RESULT_OK, data);
        super.finish();
    }
}
