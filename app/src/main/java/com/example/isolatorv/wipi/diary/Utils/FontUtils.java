package com.example.isolatorv.wipi.diary.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

public class FontUtils {

    private static Typeface mTypeface;

    public static void setTypefaceDefault(TextView view) {
        view.setTypeface(Typeface.DEFAULT);
    }

    public static void setTypeface(Context context, AssetManager assetManager, TextView view) {
        view.setTypeface(getTypeface(context, assetManager));
    }

    public static Typeface getTypeface(Context context, AssetManager assetManager) {
        if (mTypeface == null) {
            mTypeface = setCurrentTypeface(context, assetManager);
        }
        return  mTypeface;
    }

    public static Typeface setCurrentTypeface(Context context, AssetManager assetManager) {
        String currentFont = CommonUtils.loadStringPreference(context, "font_setting", "NanumPen.ttf");
        if (StringUtils.equals(currentFont, "Default")) {
            mTypeface = Typeface.DEFAULT;
        } else {
            mTypeface = Typeface.createFromAsset(assetManager, "fonts/" + currentFont);
        }
        return mTypeface;
    }

    public static void setToolbarTypeface(Context context, Toolbar toolbar, AssetManager assetManager) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof  TextView) {
                setTypeface(context, assetManager, (TextView)view);
//                ((TextView) view).setTypeface(Typeface.DEFAULT);
            }
        }
    }

    public static void setToolbarTypeface(Toolbar toolbar, Typeface typeface) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof  TextView) {
                ((TextView) view).setTypeface(typeface);
            }
        }
    }
}
