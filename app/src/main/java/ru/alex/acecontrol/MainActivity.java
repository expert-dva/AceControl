package ru.alex.acecontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //    Объявление переменных
    Button btnManage, btnGive, btnExit;
    // Новое намерение
    Intent intent, intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        Инициализация
        btnManage = (Button) findViewById(R.id.buttonManage);
        btnGive = (Button) findViewById(R.id.buttonGive);
        btnExit = (Button) findViewById(R.id.buttonExit);

        // Назначение слушателя кнопке Manage. Инициализация нажатия на кнопку
        btnManage.setOnClickListener(v -> {
                // Инициализация намерения для вызова следующей активности
                intent = new Intent(MainActivity.this, ManageActivity.class);
                // Запуск новой активности
                startActivity(intent);
        });

        btnGive.setOnClickListener(view -> {
            intent2 = new Intent(MainActivity.this,GivekeyActivity.class);
            startActivity(intent2);
        });

        // Назначение слушателя кнопке Exit. Инициализация нажатия на кнопку
        btnExit.setOnClickListener(view -> {
            super.finish();
        });
    }
}