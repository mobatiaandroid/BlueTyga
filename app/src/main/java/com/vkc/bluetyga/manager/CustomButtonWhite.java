package com.vkc.bluetyga.manager;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.vkc.bluetyga.R;


public class CustomButtonWhite extends Button {

    public CustomButtonWhite(Context context) {
        super(context);
        setFont();
    }

    public CustomButtonWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomButtonWhite(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        setTypeface(font, Typeface.NORMAL);
        setTextColor(getContext().getResources().getColor(R.color.white));
    }
}
