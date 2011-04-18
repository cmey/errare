/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe Meyer
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/
package graphicsEngine;

import java.util.LinkedList;
import java.util.List;
import logger.Logger;

public class FPSHelper {

    private GraphicsEngine ge;
    private long last_update_time;
    private long last_logged_time;
    private List<Integer> fps10;
    private int fps10iter;
    private final int averageLength = 10;

    public FPSHelper(GraphicsEngine ge) {
        this.ge = ge;
        fps10 = new LinkedList<Integer>();
        fps10iter = 0;
        last_logged_time = 0;
        last_update_time = 0;
    }

    public void tick() {
        long currentTime = System.currentTimeMillis();

        long deltaUpdate = currentTime - last_update_time;
        if (deltaUpdate >= 1000) {
            ge.actual_fps = ge.fpscount;
            ge.fpscount = 0;
            updateAverageFps(ge.actual_fps);
            last_update_time = currentTime;
        }

        long deltaLogged = currentTime - last_logged_time;
        if (deltaLogged >= 10000) {
            Logger.printDEBUG("FPS=" + getAverageFps());
            last_logged_time = currentTime;
        }
    }

    private void updateAverageFps(int actualFps) {
        if (fps10.size() < averageLength) {
            fps10.add(actualFps);
        } else {
            fps10iter %= averageLength;
            fps10.set(fps10iter, actualFps);
            ++fps10iter;
        }
    }

    public int getAverageFps() {
        int size = fps10.size();
        if (0 == size) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += fps10.get(i);
        }
        return sum / size;
    }
}
