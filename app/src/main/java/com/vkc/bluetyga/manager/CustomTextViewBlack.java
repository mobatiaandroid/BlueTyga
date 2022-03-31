package com.vkc.bluetyga.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vkc.bluetyga.R;

/**
 * Created by user2 on 27/11/17.
 */
public class CustomTextViewBlack extends TextView {

    public CustomTextViewBlack(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewBlack(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        //    setTypeface(font, Typeface.NORMAL);
        setTextColor(getContext().getResources().getColor(R.color.black));
    }
}