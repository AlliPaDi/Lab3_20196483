package com.example.lab3_20196483;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20196483.databinding.ActivityMainBinding;
import com.example.lab3_20196483.databinding.ActivityTimerBinding;
import com.google.android.material.appbar.MaterialToolbar;

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

        // Configurar el toolbar
        MaterialToolbar toolbar = binding.topAppBar;
        setSupportActionBar((toolbar));

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    // Log out
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.power) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Eliminar datos de sesión
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Limpiar todas las preferencias de sesión
        editor.apply();  // Aplicar los cambios

        // Redirigir al usuario a la pantalla de Login
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}