package lb.themike10452.game.Rendering;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.HashMap;

import lb.themike10452.game.R;

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
public class GameResources {
    private DisplayMetrics mDisplayMetrics;
    private GameClock mGameClock;
    private HashMap<Integer, MediaPlayer> mSoundMap;

    public GameClock getGameClock() {
        return mGameClock;
    }

    public DisplayMetrics getDisplayMetrics() {
        return mDisplayMetrics;
    }

    public void playSound(int id) {
        MediaPlayer mp = mSoundMap.get(id);
        if (mp.isPlaying()) {
            mp.seekTo(0);
        } else {
            mp.start();
        }
    }

    public GameResources(Context context) {
        mDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getRealMetrics(mDisplayMetrics);
        mGameClock = new GameClock();
        loadSounds(context);
    }

    private void loadSounds(Context context) {
        mSoundMap = new HashMap<>(2);
        mSoundMap.put(R.raw.flap, MediaPlayer.create(context, R.raw.flap));
        mSoundMap.put(R.raw.quack, MediaPlayer.create(context, R.raw.quack));
        mSoundMap.put(R.raw.shoot, MediaPlayer.create(context, R.raw.shoot));
    }

    public void dispose() {
        for (MediaPlayer mp : mSoundMap.values()) {
            mp.release();
        }
    }
}
