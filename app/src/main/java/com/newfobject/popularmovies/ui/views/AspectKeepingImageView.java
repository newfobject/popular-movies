package com.newfobject.popularmovies.ui.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class AspectKeepingImageView extends ImageView {
    private float aspectRatio;

    public AspectKeepingImageView(Context context) {
        this(context, null);
    }

    public AspectKeepingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        aspectRatio = 1.5f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newWidth;
        int newHeight;

        newWidth = getMeasuredWidth();
        newHeight = (int) (newWidth * aspectRatio);


        setMeasuredDimension(newWidth, newHeight);
    }
}