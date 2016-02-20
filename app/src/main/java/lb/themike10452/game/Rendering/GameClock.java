package lb.themike10452.game.Rendering;

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
public class GameClock {
    private long _frameCount;
    private long _gameTime;
    private long _frameTime;
    private long _startTime;

    public long getFrameCount() {
        return _frameCount;
    }

    public long getGameTime() {
        return _gameTime;
    }

    public long getFrameTime() {
        return _frameTime;
    }

    public long getStartTime() {
        return _startTime;
    }

    public GameClock() {

    }

    public void start() {
        _startTime = System.currentTimeMillis();
        _gameTime = 0;
    }

    public void update() {
        _frameTime = System.currentTimeMillis() - (_startTime + _gameTime);
        _gameTime = System.currentTimeMillis() - _startTime;
        _frameCount++;
    }
}
