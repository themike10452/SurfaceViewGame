package lb.themike10452.game.Utilities;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static Bitmap cropBitmap(Bitmap src, Rect srcRect, int rotationDegree) {
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(rotationDegree);
        return Bitmap.createBitmap(src, srcRect.left, srcRect.top, srcRect.width(), srcRect.height(), rotationMatrix, true);
    }

    public static String readAsset(AssetManager assets, String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(assets.open(filename)));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException ignored) {
            return "";
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
        }
    }
}
