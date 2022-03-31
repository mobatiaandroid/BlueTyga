package com.vkc.bluetyga.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.vkc.bluetyga.R;

public class CustomEditTextWhite extends EditText {

    public CustomEditTextWhite(Context context) {
        super(context);
        setFont();
    }

    public CustomEditTextWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomEditTextWhite(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        setTypeface(font, Typeface.NORMAL);
        setTextColor(getContext().getResources().getColor(R.color.white));
    }


}
