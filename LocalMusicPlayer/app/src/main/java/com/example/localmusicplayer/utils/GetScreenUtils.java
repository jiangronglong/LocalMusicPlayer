package com.example.localmusicplayer.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by jiang rong long on 2017/1/22.
 */

public class GetScreenUtils {
    public static int[] getScreen(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        return new int[] {(int) (outMetrics.density * outMetrics.widthPixels),
                (int)(outMetrics.density * outMetrics.heightPixels)
        };
    }

}
