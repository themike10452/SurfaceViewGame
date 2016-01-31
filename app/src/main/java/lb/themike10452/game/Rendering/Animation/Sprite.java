package lb.themike10452.game.Rendering.Animation;

import android.graphics.Rect;

/**
 * Created by DELL on 1/31/2016.
 */
public class Sprite {
    public Rect Rect;

    public int Rotation;
    public int[] Flip;

    public Sprite(int x, int y, int w, int h) {
        this(x, y, w, h, 0, null);
    }

    public Sprite(int x, int y, int w, int h, int rotation) {
        this(x, y, w, h, rotation, null);
    }

    public Sprite(int x, int y, int w, int h, int rotation, int[] flip) {
        this(new Rect(x, y, x + w, y + h), rotation, flip);
    }

    public Sprite(Rect rect, int rotation, int[] flip) {
        this.Rect = rect;
        this.Rotation = rotation;
        this.Flip = flip;
    }
}
