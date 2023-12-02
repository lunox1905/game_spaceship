package com.thaichinh.gamespaceship;

import static com.thaichinh.gamespaceship.GameView.screenRatioX;
import static com.thaichinh.gamespaceship.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

public class Boss {

    int x, y, width, height, hp;
    Bitmap boss;
    Boss (Resources res) {
        boss = BitmapFactory.decodeResource(res, R.drawable.boss);
        width = boss.getWidth();
        height = boss.getHeight();
        hp = 190;
        width = width / 2;
        height = height / 3;
        boss = Bitmap.createScaledBitmap(boss, width, height, false);
    }

    Rect getCollisionShape () {
        return new Rect(x + 100, y, x + width - 100, y + 200);
    }

}