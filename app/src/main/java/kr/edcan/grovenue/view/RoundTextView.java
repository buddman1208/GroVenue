package kr.edcan.grovenue.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import kr.edcan.grovenue.R;

/**
 * Created by 최예찬 on 2016-10-10.
 */
public class RoundTextView extends TextView {

    private GradientDrawable background;
    private boolean isFillEnabled = false;
    private int color;

    private float strokeWidth;
    private float roundRadius;

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView, defStyleAttr, 0);

        color = typedArray.getColor(R.styleable.RoundTextView_color, ContextCompat.getColor(context, R.color.colorPrimary));
        isFillEnabled = typedArray.getBoolean(R.styleable.RoundTextView_isfillEnabled, false);

        strokeWidth = getResources().getDimension(R.dimen.roundTextview_strokeWidth);
        roundRadius = getResources().getDimension(R.dimen.roundTextview_roundRadius);
        background = new GradientDrawable();
        background.setCornerRadius(roundRadius);
        background.setStroke((int)strokeWidth, color);
        setBackground(background);

        initStatus();
        typedArray.recycle();
    }

    public void initStatus(){
        if(isFillEnabled) {
            background.setColor(color);
            setTextColor(0xffffffff);
        }
        else {
            background.setColor(Color.TRANSPARENT);
            setTextColor(color);
        }
    }

    public boolean isFillEnabled() {
        return isFillEnabled;
    }

    public void setFillEnabled(boolean fillEnabled) {
        if(this.isFillEnabled != fillEnabled) {
            this.isFillEnabled = fillEnabled;
            initStatus();
        }
    }

    public void toggleFillEnabled(){
        setFillEnabled(!isFillEnabled);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
