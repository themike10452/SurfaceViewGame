package lb.themike10452.game.Rendering.Animation;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import lb.themike10452.game.Utilities.Utils;

/**
 * Created by DELL on 1/31/2016.
 */
public class SpriteSheet {
    public static float DRAW_SCALE = 4f;

    public final Bitmap Bitmap;
    private final HashMap<String, Sprite> mSpriteMap;

    /**
     * @param spriteSheetResId resId of the bitmap containing the sprites
     * @param dataAsset        name of the json data map file located in assets folder
     */
    public SpriteSheet(Resources resources, int spriteSheetResId, AssetManager assets, String dataAsset) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap = BitmapFactory.decodeResource(resources, spriteSheetResId, options);
        mSpriteMap = new HashMap<>();
        try {
            JSONArray array = new JSONArray(Utils.readAsset(assets, dataAsset));
            int length = array.length();
            JSONObject obj;
            String name;
            int x, y, w, h;
            for (int i = 0; i < length; i++) {
                obj = array.getJSONObject(i);
                name = obj.getString("name");
                x = obj.getInt("x");
                y = obj.getInt("y");
                w = obj.getInt("width");
                h = obj.getInt("height");
                mSpriteMap.put(name, new Sprite(x, y, w, h));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //DRAW_SCALE = resources.getDisplayMetrics().scaledDensity;
    }

    public Sprite getSprite(String spriteName) {
        return mSpriteMap.get(spriteName);
    }
}
