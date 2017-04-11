package com.example.android.mypopularmovies.util;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Class that handles movie's width and height according to device screen/resolution
 */

public class ImageUtil {

    /**
     * Method that calculates movie's poster height and width according to device resolution
     *
     * @param activity
     * @return
     */
    public static int[] getPosterHeightAndWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new int[]{height / 2, width / 2};
    }
}
