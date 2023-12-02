package com.thaichinh.gamespaceship;

import static com.thaichinh.gamespaceship.GameView.screenRatioX;
import static com.thaichinh.gamespaceship.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bullet {

    int x, y, width, height, moveX, moveY, level = 1;

    Bitmap bullet;

    Bullet (Resources res, int bulletLevel) {
        level = bulletLevel;
        if(level == 1) {
            bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);
        }
        if(level == 2) {
            bullet = BitmapFactory.decodeResource(res, R.drawable.bulletlevel2);
        }
        if(level == 3) {
            bullet = BitmapFactory.decodeResource(res, R.drawable.bulletlevel3);
        }
        width = bullet.getWidth();
        height = bullet.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}