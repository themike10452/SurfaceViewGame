package lb.themike10452.game.Utilities;

import android.graphics.Bitmap;

/**
 * Created by DELL on 1/30/2016.
 */
public class Utils {

    public static class Constants {
        public static final int AUTO = 0;
    }

    public static Bitmap resizeBitmap(Bitmap src, int destWidth, int destHeight) {
        if (destHeight == Constants.AUTO && destWidth == Constants.AUTO) {
            return src;
        } else if (destHeight == Constants.AUTO) {
            float ratio = (float) destWidth / src.getWidth();
            return Bitmap.createScaledBitmap(src, destWidth, (int) (src.getHeight() * ratio), false);
        } else if (destWidth == Constants.AUTO) {
            float ratio = (float) destHeight / src.getHeight();
            return Bitmap.createScaledBitmap(src, (int) (src.getWidth() * ratio), destHeight, false);
        } else {
            return Bitmap.createScaledBitmap(src, destWidth, destHeight, false);
        }
    }
}
