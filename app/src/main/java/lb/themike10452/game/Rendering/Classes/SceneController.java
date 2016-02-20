package lb.themike10452.game.Rendering.Classes;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

import lb.themike10452.game.R;
import lb.themike10452.game.Rendering.Animation.IDrawable;
import lb.themike10452.game.Rendering.Animation.SpriteBatch;
import lb.themike10452.game.Rendering.Animation.SpriteSheet;
import lb.themike10452.game.Rendering.GameResources;

/**
 * Copyright 2016 Michael Mouawad
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SceneController implements IDrawable, View.OnTouchListener {
    private static final boolean DEBUG = true;
    private static final String TAG = "DEBUG";

    private Duck mDuck1;
    private Duck mDuck2;
    private GameResources mGamesRes;

    public SceneController(WeakReference<Context> context, GameResources resources) {
        mGamesRes = resources;
        SpriteSheet spriteSheet = new SpriteSheet(context.get().getResources(), R.drawable.spritesheet, context.get().getAssets(), "sprites.json");
        mDuck1 = new Duck(resources, spriteSheet);
        mDuck2 = new Duck(resources, spriteSheet);
    }

    public void update() {
        mDuck1.update();
        mDuck2.update();
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        batch.add(mDuck1);
        batch.add(mDuck2);
    }

    private void onShoot(int x, int y) {
        if (mDuck1.hit(x, y)) {
            mDuck1.shoot();
            if (DEBUG) {
                Log.d(TAG, String.format("Duck1 shot %d %d", x, y));
            }
        }

        if (mDuck2.hit(x, y)) {
            mDuck2.shoot();
            if (DEBUG) {
                Log.d(TAG, String.format("Duck2 shot %d %d", x, y));
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            if (pointerIndex == 1) {
                mGamesRes.playSound(R.raw.shoot);
                onShoot((int) event.getRawX(), (int) event.getRawY());
            }
        }
        return false;
    }
}
