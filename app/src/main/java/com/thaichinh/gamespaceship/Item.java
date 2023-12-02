package com.thaichinh.gamespaceship;

import static com.thaichinh.gamespaceship.GameView.screenRatioX;
import static com.thaichinh.gamespaceship.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Item {

    int x, y, width, height, moveX, moveY, type = 1;

    Bitmap item;

    Item (Resources res, int itemType) {
        type = itemType;
        if(type == 1) {
            item = BitmapFactory.decodeResource(res, R.drawable.item1);
        }
        if(type == 2) {
            item = BitmapFactory.decodeResource(res, R.drawable.item2);
        }
        if(type == 3) {
            item = BitmapFactory.decodeResource(res, R.drawable.item3);
        }
        if(type == 4) {
            item = BitmapFactory.decodeResource(res, R.drawable.item3);
        }
        width = item.getWidth();
        height = item.getHeight();

        width /= 2;
        height /= 2;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        item = Bitmap.createScaledBitmap(item, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}