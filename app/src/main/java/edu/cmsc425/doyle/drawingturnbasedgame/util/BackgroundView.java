package edu.cmsc425.doyle.drawingturnbasedgame.util;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import edu.cmsc425.doyle.drawingturnbasedgame.DrawingView;
import edu.cmsc425.doyle.drawingturnbasedgame.R;

public class BackgroundView extends ImageView {

    private Drawable bg, cloud1, cloud2, cloud3;

    public BackgroundView(Context context) {
        super(context);
        init(null, 0);
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        bg = getResources().getDrawable(R.drawable.sky);
        cloud1 = getResources().getDrawable(R.drawable.clouds_1);
        cloud2 = getResources().getDrawable(R.drawable.clouds_2);
        cloud3 = getResources().getDrawable(R.drawable.clouds_3);

        Drawable[] layers = new Drawable[2];

        layers[0] = bg;
        layers[1] = cloud1;
        //layers[2] = cloud2;
//        layers[3] = cloud3;

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(layers[1], PropertyValuesHolder.ofInt("alpha", 255));
        animator.setTarget(layers[1]);
        animator.setDuration(1000);
        animator.setStartDelay(1000);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();

//        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(layers[0], PropertyValuesHolder.ofInt("alpha", 255));
//        animator2.setTarget(layers[2]);
//        animator2.setDuration(4000);
//        animator2.setRepeatMode(ValueAnimator.REVERSE);
//        animator2.start();

//        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(layers[0], PropertyValuesHolder.ofInt("alpha", 255));
//        animator3.setTarget(layers[3]);
//        animator3.setDuration(3000);
//        animator3.setStartDelay(1500);
//        animator3.setRepeatMode(ValueAnimator.REVERSE);
//        animator3.start();

        LayerDrawable layerDrawable = new LayerDrawable(layers);
        setImageDrawable(layerDrawable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


    }

}
