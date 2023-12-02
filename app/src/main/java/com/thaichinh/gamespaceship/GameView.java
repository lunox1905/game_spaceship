package com.thaichinh.gamespaceship;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.Random;

import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private List<Bullet> bullets;
    private List<Bullet> bulletsEnemy;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private Boss boss;
    private int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    Background background1, background2;
    private Spaceship spaceship;
    private boolean isUp, isLeft, isMove = false;
    private float moveX, moveY, speedSpaceship = 40;
    public float mouseX, mouseY;
    public boolean isShot = false;
    public int time = 0, heightLaser = 0, bulletLevel = 1, score = 0, showShield = 0,
            delay = 20;
    public Random random = new Random();
    public boolean showBoss = false, bossDie = false, shotLaser = false, isGameOver = false;
    public static int countBossEx = 0;
    private Bitmap laser;
    private Bitmap shield;
    private Item item;
    GameActivity gameActivity;
    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1080f / screenX;
        screenRatioY = 1920f / screenY;
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        laser = BitmapFactory.decodeResource(getResources(), R.drawable.laser);
        shield = BitmapFactory.decodeResource(getResources(), R.drawable.shield);
        background2.y = -screenY;
        spaceship = new Spaceship(this, screenY, getResources());
        bullets = new ArrayList<>();
        bulletsEnemy = new ArrayList<>();
        enemies = new ArrayList<>();
        explosions = new ArrayList<>();
        if (context instanceof GameActivity) {
            gameActivity = (GameActivity) context;
        }
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }
    public void update() {
        background1.y += 10;
        background2.y += 10;
        heightLaser += 1;
        time += 1;
        if (background1.y > screenY) {
            background1.y = -screenY;
        }

        if (background2.y > screenY) {
            background2.y = -screenY;
        }
        explosions.clear();
        spaceshipMove();
        isShot = !isShot;
        if(isShot) {
            newBullet();
        }
        if(time % 20 == 0) {
            newEnemy(1);
        }
        if(time % 30 == 0) {
            newEnemy(2);
        }
        if(time == 200) {
            showBoss = true;
            boss = new Boss(getResources());
            boss.x = screenX /2 - boss.width/2;
            boss.y = -(boss.height);
        }

        if(time == 20) {
            showItem(3);
        }

        if(time == 150) {
            showItem(3);
        }

        if(item != null) {
            item.y += 25;
            if(Rect.intersects(item.getCollisionShape(),
                    spaceship.getCollisionShape())) {
                openItem(item.type);
                item = null;
            }
        }

        if(showBoss) {
            if(boss.y < 10) {
                boss.y += 10;
            }
            if(time % 12 == 0) {
                newBulletEnemy(boss.x + boss.width - 270, boss.y + boss.height - 20);
                newBulletEnemy(boss.x + boss.width - 220, boss.y + boss.height - 20);
                newBulletEnemy(boss.x + 160, boss.y + boss.height - 20);
                newBulletEnemy(boss.x + 210, boss.y + boss.height - 20);
            }

        }
        List<Bullet> trash = new ArrayList<>();
        for (Bullet bullet : bullets) {

            bullet.y -= 50 * screenRatioY;
            for(Enemy enemy : enemies) {
                if(Rect.intersects(enemy.getCollisionShape(),
                        bullet.getCollisionShape())) {
                    newExplosion(enemy.x, enemy.y);
                    enemy.y = screenY + 100;
                    bullet.y = -10;
                    score += 1;
                }
                if(time % 15 == 0) {
                    newBulletEnemy(enemy.x + enemy.width/2 - bullet.width/2, enemy.y);
                }
            }
            if(showBoss) {
                if(Rect.intersects(boss.getCollisionShape(),
                        bullet.getCollisionShape())) {
                    bullet.y = -10;
                    boss.hp -= 1 * bulletLevel;
                    if(boss.hp <= 0) {
                        bossDie = true;
                        score += 100;
                    }
                }
                if(boss.hp % 50 == 0) {
                    shotLaser = true;
                    heightLaser = 0;
                }
                if(shotLaser) {
                    heightLaser += 60;

                    int x = (boss.x + boss.width/2 - laser.getWidth()/2 + 10);
                    int y = boss.y + 300;
                    Rect r = new Rect(x, y, x + laser.getWidth(), y + laser.getHeight());
                    if(Rect.intersects(r,
                            spaceship.getCollisionShape())) {
                        spaceship.hp -= 10;
                    }
                }
                if(heightLaser > screenY + 200) {
                    shotLaser = false;
                }
            }
            if(bossDie && countBossEx <= 251) {
                countBossEx += 1;
                if(countBossEx == 10) {
                    newExplosion(boss.x, boss.y);
                }
                if(countBossEx == 50) {
                    newExplosion(boss.x + boss.height - 100, boss.y + boss.height - 50);
                }
                if(countBossEx == 120) {
                    newExplosion(boss.x + 100, boss.y + 100);
                    newExplosion(boss.x + 500, boss.y + 200);
                }
                if(countBossEx == 170) {
                    newExplosion(boss.x + 600, boss.y + 60);
                    newExplosion(boss.x + 330, boss.y + 30);
                    newExplosion(boss.x + 100, boss.y + 20);
                }
                if(countBossEx == 180) {
                    showBoss = false;
                }
                if(countBossEx == 250) {
                    newExplosion(boss.x + 200, boss.y + 10);
                    newExplosion(boss.x + 400, boss.y + 40);
                    newExplosion(boss.x + 500, boss.y + 20);
                    boss = null;
                    isGameOver = true;
                }
            }
            if (bullet.y < 0)
                trash.add(bullet);

        }
        for (Bullet bullet : bulletsEnemy) {
            if(Rect.intersects(spaceship.getCollisionShape(),
                    bullet.getCollisionShape())) {
                spaceship.hp -= 1;
                bullet.y = screenY + 30;
            }
            if (bullet.y > screenY + 20)
                trash.add(bullet);
            bullet.y += 50 * screenRatioY;

        }

        for (Enemy enemy : enemies) {
            if(enemy.getType() == 1) {
                enemy.y += 10;
            }
            if(enemy.getType() == 2) {
                enemy.y += 20;
                enemy.x += enemy.move;
            }
            if(enemy.y < 0) {
                enemies.remove(enemy);
            }
        }
        if(spaceship.hp <= 0) {
            newExplosion(spaceship.x + 10, spaceship.y + 10);
            isGameOver = true;
        }
        if(isGameOver) {
            delay --;
            if(delay <= 0) {
                gameOver();
            }
        }
        for (Bullet bullet : trash) {
            bullets.remove(bullet);
            bulletsEnemy.remove(bullet);
        }

    }

    public void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            if(showShield > 0) {
                canvas.drawBitmap(Bitmap.createScaledBitmap(shield,
                                spaceship.getWidth() + 50,
                                spaceship.getHeight() + 50, false), (spaceship.x - 10)
                        , spaceship.y - 10, paint);
            }
            canvas.drawBitmap(spaceship.getFlight(), spaceship.x, spaceship.y, paint);
            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
            for (Bullet bullet : bulletsEnemy)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
            for (Enemy enemy : enemies)
                canvas.drawBitmap(enemy.enemy, enemy.x, enemy.y, paint);
            for (Explosion explosion : explosions) {
                canvas.drawBitmap(explosion.explosion, explosion.x, explosion.y, paint);
            }
            if(showBoss) {
                canvas.drawBitmap(boss.boss, boss.x, boss.y, paint);
            }
            if(item != null) {
                canvas.drawBitmap(item.item, item.x, item.y, paint);
            }
            if(shotLaser) {
                canvas.drawBitmap(Bitmap.createScaledBitmap(laser, laser.getWidth(),
                        heightLaser, false), (boss.x + boss.width/2 - laser.getWidth()/2 + 10)
                        , boss.y + 300, paint);

            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }
    private void sleep () {
        try {
            Thread.sleep(35);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void showItem(int type) {
        item = new Item(getResources(), type);
        item.x = random.nextInt(screenX - item.width*2 ) + item.width;
        item.y = -10;
    }

    public void openItem (int type) {
        switch (type) {
            case 2:
                spaceship.hp += 30;
                break;
            case 3:
                if (bulletLevel < 3) bulletLevel ++;
                break;
            case 4:
                if (bulletLevel > 1) bulletLevel --;
                break;
        }
    }
    public void gameOver () {
        if (gameActivity != null) {
            Intent intent = new Intent(gameActivity, EndGameActivity.class);
            intent.putExtra("DIEM_SO", score);
            gameActivity.startActivity(intent);
            gameActivity.finish();
        }
    }

    public void spaceshipMove() {
        float distanceX = mouseX - spaceship.x - spaceship.getWidth()/2;
        float distanceY = mouseY - spaceship.y - spaceship.getHeight()/2;
        if(Math.abs(distanceX) < spaceship.getWidth() / 2
                && Math.abs(distanceY) < spaceship.getHeight() / 2) {
            isMove = false;
            return;
        }

        if(distanceY < 0) {
            isUp = true;
        } else {
            isUp = false;
        }
        if(distanceX < 0) {
            isLeft = true;
        } else {
            isLeft = false;
        }
        moveX = speedSpaceship;
        moveY = speedSpaceship;
        float v = Math.abs(distanceX) / Math.abs(distanceY);
        if(Math.abs(distanceX) > Math.abs(distanceY)) {
            if(isLeft) {
                moveX = -speedSpaceship;
            }
            if(isUp) {
                moveY /= -v;
            }
        } else{
            if(isLeft) {
                moveX *= -v;
            }
            if(isUp) {
                moveY = -speedSpaceship;
            }
        }
        if(isMove) {
            spaceship.x += moveX * screenRatioX;
            spaceship.y += moveY * screenRatioY;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = true;
                mouseX = event.getRawX();
                mouseY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mouseX = event.getRawX();
                mouseY = event.getRawY();
                isMove = true;
                break;
            case MotionEvent.ACTION_UP:
                isMove = false;
                break;
        }
        return true;
    }

    public void newBullet() {
        Bullet bullet = new Bullet(getResources(), bulletLevel);
        bullet.x = spaceship.x + (spaceship.width / 2) - bullet.width/2;
        bullet.y = spaceship.y;
        bullets.add(bullet);
    }

    private void newEnemy(int type) {
        Enemy enemy = new Enemy(getResources(), type);
        if(type == 1) {
            enemy.x = random.nextInt(screenX - enemy.width) + enemy.width;
        }
        if(type == 2) {
            int randomInRange1 = random.nextInt(300 ) + enemy.width;
            int randomInRange2 = random.nextInt(300) + (screenX - 400);

            int randomNumber = random.nextBoolean() ? randomInRange1 : randomInRange2;
            if(randomNumber > screenX / 2) {
                enemy.move = -15;
            } else {
                enemy.move = 15;
            }
            enemy.x = randomNumber;
        }

        enemy.y = 10;
        enemies.add(enemy);
    }

    private void newExplosion(int x, int y) {
        Explosion explosion = new Explosion (getResources());
        explosion.x = x;
        explosion.y = y;
        explosions.add(explosion);
    }

    private void newBulletEnemy(int x, int y) {
        Bullet bullet = new Bullet(getResources(), 1);
        bullet.x = x;
        bullet.y = y;
        bulletsEnemy.add(bullet);
    }
}
