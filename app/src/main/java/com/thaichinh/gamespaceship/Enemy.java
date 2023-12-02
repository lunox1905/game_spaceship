package com.thaichinh.gamespaceship;

import static com.thaichinh.gamespaceship.GameView.screenRatioX;
import static com.thaichinh.gamespaceship.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

public class Enemy {

    int x, y, width, height;
    int drawable, type;
    public int move;
    Bitmap enemy;
    Enemy (Resources res, int typeEnemy) {
        if(typeEnemy == 1) {
            drawable = R.drawable.enemy1;
        }
        if(typeEnemy == 2) {
            drawable = R.drawable.enemy2;
        }
        enemy = BitmapFactory.decodeResource(res, drawable);
        width = enemy.getWidth();
        height = enemy.getHeight();
        type = typeEnemy;
        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);
        enemy = Bitmap.createScaledBitmap(enemy, width, height, false);
    }

    public int getType () {
        return type;
    }
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}