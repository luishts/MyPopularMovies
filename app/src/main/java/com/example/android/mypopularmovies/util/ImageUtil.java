package com.example.android.mypopularmovies.util;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by Luis on 10/04/2017.
 */

public class ImageUtil {

    public static int[] getPosterHeightAndWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new int[]{height / 2, width / 2};
    }
}
