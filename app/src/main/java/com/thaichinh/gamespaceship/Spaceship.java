package com.thaichinh.gamespaceship;


import static com.thaichinh.gamespaceship.GameView.screenRatioX;
import static com.thaichinh.gamespaceship.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Spaceship {

    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, shootCounter = 1, hp;
    Bitmap spaceship, dead;
    private GameView gameView;

    Spaceship (GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        spaceship = BitmapFactory.decodeResource(res, R.drawable.spaceship3);
        hp = 100;
        width = spaceship.getWidth();
        height = spaceship.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        spaceship = Bitmap.createScaledBitmap(spaceship, width, height, false);
        spaceship = Bitmap.createScaledBitmap(spaceship, width, height, false);

        y = screenY - height;
        x = 10;

    }

    Bitmap getFlight () {
        return spaceship;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead () {
        return dead;
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
}