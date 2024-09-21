package com.example.lab3_20196483;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20196483.databinding.ActivityMainBinding;
import com.example.lab3_20196483.dto.Usuario;
import com.example.lab3_20196483.services.DummyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    // Inicializando ViewBinding
    ActivityMainBinding binding;
    private DummyService dummyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializando ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Usando ViewBinding para el botón btnLogin
        binding.btnLogin.setOnClickListener(view -> {
            inicioSesion();
        });

        // Configurar el Retrofit
        dummyService = new Retrofit.Builder()
                .baseUrl("https://dummyjson.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DummyService.class);


        EdgeToEdge.enable(this);
    }


    // Inicio de sesion
    private void inicioSesion() {
        // Tomando los valores de entrada
        String username = binding.etUsuario.getText().toString();
        String password = binding.etContra.getText().toString();

        // Verificar que los campos no estén vacíos
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

       // Realizar la solicitud de inicio de sesion
       dummyService.existeUsuario(username, password).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();

                    // Mostrar mensaje de éxito
                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    // Ir a Pomodoro
                    Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                    // Pasar datos del usuario
                    intent.putExtra("username", usuario.getUsername());
                    startActivity(intent);

                } else {
                    // Mostrar mensaje de error en inicio de sesion
                    Toast.makeText(MainActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                // No hay red
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

}