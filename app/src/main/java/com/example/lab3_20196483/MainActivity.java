package com.example.lab3_20196483;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20196483.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Inicializando ViewBinding
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializando ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Usando ViewBinding para el botón btnLogin
        binding.btnLogin.setOnClickListener(view -> {
            ingresarPomodoro(view);
        });

        EdgeToEdge.enable(this);
    }

    // Ingresar a Pomodoro
    public void ingresarPomodoro(View view){
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
    }

}