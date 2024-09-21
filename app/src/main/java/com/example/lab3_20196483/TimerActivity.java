package com.example.lab3_20196483;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20196483.databinding.ActivityMainBinding;
import com.example.lab3_20196483.databinding.ActivityTimerBinding;

public class TimerActivity extends AppCompatActivity {

    ActivityTimerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtenemos los datos
        Intent intent = getIntent();
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String email = intent.getStringExtra("email");
        String gender = intent.getStringExtra("gender");

        // Mostrar datos
        binding.tvNombre.setText(firstName+" "+lastName);
        binding.tvCorreo.setText(email);
        if(gender.equalsIgnoreCase("female")){
            binding.ivGenero.setImageResource(R.drawable.ic_woman);
        } else if (gender.equalsIgnoreCase("male")) {
            binding.ivGenero.setImageResource(R.drawable.ic_man);
        }

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}