package lb.themike10452.game.Rendering.Classes;

import android.content.Context;

import java.lang.ref.WeakReference;

import lb.themike10452.game.R;
import lb.themike10452.game.Rendering.Animation.SpriteSheet;

/**
 * Created by Mike on 2/17/2016.
 */
public class SceneController {
    private Duck mDuck1;
    private Duck mDuck2;

    public SceneController(WeakReference<Context> context) {
        SpriteSheet spriteSheet = new SpriteSheet(context.get().getResources(), R.drawable.spritesheet, context.get().getAssets(), "spritesheet.json");
    }
}
