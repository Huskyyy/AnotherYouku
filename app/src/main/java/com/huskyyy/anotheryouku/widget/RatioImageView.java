package com.huskyyy.anotheryouku.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RatioImageView extends ImageView {

    public RatioImageView(Context context) {
        super(context);
    }


    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > 0) {
            height = (int) ((float) width / 1.618f);
        }

        setMeasuredDimension(width, height);

    }
}
