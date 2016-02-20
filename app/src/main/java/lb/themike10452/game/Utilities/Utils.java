package lb.themike10452.game.Utilities;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Copyright 2016 Michael Mouawad
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
