package com.vkc.bluetyga.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user2 on 16/8/17.
 */
public class CustomTextViewBold extends TextView {

    public CustomTextViewBold(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/arialbd.ttf");
        setTypeface(font, Typeface.BOLD);
        // setTextColor(getContext().getResources().getColor(R.color.black));
    }
}