package edu.cmsc425.doyle.drawingturnbasedgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import edu.cmsc425.doyle.drawingturnbasedgame.model.AttackType;


public class DrawingView extends View {

    protected static final Object lock = new Object();

    private Bitmap bitmap;
    private boolean doneDrawing;
    private TextView countDown;
    private CountDownTimer countDownTimer;

    //Drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = Color.CYAN;
    private int canvasColor = Color.LTGRAY;
    //canvas
    //bitmap
    private Bitmap canvasBitmap;

    private boolean cc = true;
    private boolean first = true;
    //The total number of pixels to trace
    private int totalPixels = 0;
    private int mistakes = 0;
    private double score = 0;
    private int[][] imageData;

    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();
    int paddingRight = getPaddingRight();
    int paddingBottom = getPaddingBottom();

    int width = getWidth() - paddingLeft - paddingRight;
    int height = getHeight() - paddingTop - paddingBottom;

    public DrawingView(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pentagon);
        setupDrawing();
        initTimer();
        imageData = bitmapTo2DArray(bitmap);

    }

    private void initTimer() {
        Log.d("Timer", "Init called");

        countDownTimer = new CountDownTimer(5000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {

                countDown.setText((millisUntilFinished / 1000.0) + "");
            }

            @Override
            public void onFinish() {
                doneDrawing = true;
            }
        };


    }

    private void setupDrawing() {
        //initialize
        drawPath = new Path();
        drawPaint = new Paint();
        //set properties of paint
        paintColor = Color.CYAN;
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    /**
     * Converts a bitmap to an array of pixels. We can use this to determine
     * Which pixels the user is hitting and if they will be counted for or against their score
     */
    private int[][] bitmapTo2DArray(Bitmap bmap) {

        int origPixel = 0;
        int [][]arr = new int[bmap.getHeight()][bmap.getWidth()];
        for(int j = 0; j < bmap.getWidth(); j++){
            for(int i = 0; i < bmap.getHeight(); i++){
                origPixel = bmap.getPixel(j,i);
                if(origPixel != -1) //-16777216 is 255 alpha
                    totalPixels++;
                arr[i][j] = origPixel;
            }
        }

        return arr;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, w, h), Matrix.ScaleToFit.CENTER);
        canvasBitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

        imageData = bitmapTo2DArray(canvasBitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cc) {
            canvas.drawBitmap(canvasBitmap, 0.0f, 0.0f, null);
            canvas.drawPath(drawPath, drawPaint);
            drawPath.reset();
        } else {
            canvas.drawBitmap(canvasBitmap, 0.0f, 0.0f, null);
            canvas.drawPath(drawPath, drawPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        //If it is in range of the image, do the calculation
        if(!(touchX < 0 || touchY < 0) && touchY < imageData.length && touchX < imageData[(int)touchY].length) {

            Log.i("Coords", "x: " + (int)touchX + " y: " + (int)touchY);
            Log.i("Color", "Color: " + imageData[(int) touchY][(int)touchX]);
            Log.i("Canvas", "Color: " + canvasBitmap.getPixel((int) touchX, (int) touchY));
            if (imageData[(int) touchX][(int) touchY] != -1) // 255 alpha
                score += 1;
            else
                mistakes += 1;

        }

        //If the time is up, we no longer accept input
        if(doneDrawing) {
            Log.d("Timer", "Timer Done");
            event.setAction(MotionEvent.ACTION_UP);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initTimer();
                countDownTimer.start();
                cc = false;
                score = 0;
                mistakes = 0;
                first = true;
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                //If the timer is up && not the first action up
                if (first) {
                    countDownTimer.cancel();
                    initTimer();
                    first = false;
                    Log.d("ActionUp", "Called");
                    //drawCanvas.drawPath(drawPath, drawPaint);
                    Log.i("Total", totalPixels + "");
                    Log.i("Pre-Score", score + "");
                    Log.i("Mistakes", mistakes + "");
                    //TODO: determine a scaling factor to determine what 100% is in normal image
                    if (mistakes == 0 && score >= 30) {
                        score = .5 + (score / (totalPixels / 10000));
                        Log.d("Score", "Bonus for no mistakes");
                    } else if (mistakes > 0) {
                        //We make lots of mistakes according to the computer
                        //TODO: we can adjust this or adjust the width of the path
                        Log.d("Score", "We had more mistakes");
                        score -= (.05) * mistakes;
                        score /= (totalPixels / 10000);
                        score *= 2;
                    } else {
                        //Else no modification to score
                        score /= (totalPixels / 10000);
                        score *= 2;
                    }

                    //If the timer hadn't ended, we get a bonus
                    if (!doneDrawing) {
                        score *= 1.5;
                    }

                    doneDrawing = false;

                    //We can't heal the opponent on accident
                    score = Math.max(0.0, score);
                    //Bonus damage maxes out at 1.5x
                    score = Math.min(1.5, score);

                    Log.i("score", score + "");
                    invalidate();
                    //end drawing
                    synchronized (lock) {
                        lock.notifyAll();
                    }

                    cc = true;
                }
                break;
            default:
                return false;
        }
        Log.i("score", score + "");
        invalidate();
        return true;
    }

    public void setBitmap(int bitmapId) {
        bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);

        //Resize the bitmap
        if (canvasBitmap != null) {
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, canvasBitmap.getWidth(), canvasBitmap.getHeight()), Matrix.ScaleToFit.CENTER);
            canvasBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

            imageData = bitmapTo2DArray(canvasBitmap);
        }

        setupDrawing();
    }
    public double getScore() {
        return score;
    }

    public TextView getCountDown() {
        return countDown;
    }

    public void setCountDown(TextView countDown) {
        this.countDown = countDown;
    }

    public boolean isDoneDrawing() {
        return doneDrawing;
    }
}
