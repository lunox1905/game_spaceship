package com.thaichinh.gamespaceship;

import static com.thaichinh.gamespaceship.GameView.screenRatioX;
import static com.thaichinh.gamespaceship.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Explosion {

    int x, y, width, height;

    Bitmap explosion;

    Explosion (Resources res) {

        explosion = BitmapFactory.decodeResource(res, R.drawable.explosion);

        width = explosion.getWidth();
        height = explosion.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        explosion = Bitmap.createScaledBitmap(explosion, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}