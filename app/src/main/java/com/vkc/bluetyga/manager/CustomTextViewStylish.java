package com.vkc.bluetyga.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user2 on 24/8/17.
 */
public class CustomTextViewStylish extends TextView {

    public CustomTextViewStylish(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewStylish(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewStylish(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/HighlandGothic.ttf");
        setTypeface(font, Typeface.NORMAL);
        // setTextColor(getContext().getResources().getColor(R.color.black));
    }
}