package com.thaichinh.gamespaceship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        TextView textView = findViewById(R.id.score);
        findViewById(R.id.start_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EndGameActivity.this, GameActivity.class));
            }
        });


        if (getIntent().hasExtra("DIEM_SO")) {
            int receivedDiemSo = getIntent().getIntExtra("DIEM_SO", 0);

            textView.setText("" + receivedDiemSo);
        }
    }
}