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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.lab3_20196483.databinding.ActivityTimerBinding;
import com.example.lab3_20196483.workers.ContadorWorker;
import com.google.android.material.appbar.MaterialToolbar;


public class TimerActivity extends AppCompatActivity {

    ActivityTimerBinding binding;
    private OneTimeWorkRequest workRequest;
    private WorkManager workManager;
    private LiveData<WorkInfo> currentWorkInfo;
    private boolean isTimerRunning = false;
    private static final String TIMER_WORK_ID = "TIMER_WORK_ID";


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

        // Inicializar WorkManager
        workManager = WorkManager.getInstance(this);

        // Verificar si hay un Worker en progreso
        if (savedInstanceState != null) {
            String workId = savedInstanceState.getString(TIMER_WORK_ID);
            if (workId != null) {
                observeWorkInfo(workId);
            }
        }

        // Listener para iniciar o reiniciar el temporizador
        binding.ibStart.setOnClickListener(view -> {
            if (isTimerRunning) {
                resetTimer();
            } else {
                startContadorWorker();
            }
        });

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
        // Redirigir al usuario a la pantalla de Login
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Método para iniciar el Worker del temporizador
    private void startContadorWorker() {
        if (workRequest != null && isTimerRunning) {
            workManager.cancelWorkById(workRequest.getId());
            if (currentWorkInfo != null) {
                currentWorkInfo.removeObservers(this);
            }
        }

        // Crear un nuevo OneTimeWorkRequest para el temporizador
        workRequest = new OneTimeWorkRequest.Builder(ContadorWorker.class).build();

        // Encolar el Worker
        workManager.enqueue(workRequest);

        // Observar el progreso del Worker
        observeWorkInfo(workRequest.getId().toString());


        isTimerRunning = true;
        updateBtnIcon(isTimerRunning);
    }

    // Método para observar el progreso del Worker
    private void observeWorkInfo(String workRequestId) {
        currentWorkInfo = workManager.getWorkInfoByIdLiveData(workRequest.getId());
        currentWorkInfo.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING) {
                    // Obtener el progreso del Worker
                    Data progress = workInfo.getProgress();
                    int minutesLeft = progress.getInt("minutesLeft", 25);
                    int secondsLeft = progress.getInt("secondsLeft", 0);

                    // Formatear el tiempo restante
                    String timeFormatted = String.format("%02d:%02d", minutesLeft, secondsLeft);

                    // Actualizar el TextView con el tiempo restante
                    binding.tvTimer.setText(timeFormatted);
                }

                if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    // El temporizador ha terminado
                    isTimerRunning = false;
                    updateBtnIcon(isTimerRunning);
                }
            }
        });
    }

    // Método para reiniciar el temporizador
    private void resetTimer() {
        if (currentWorkInfo != null) {
            currentWorkInfo.removeObservers(this);
        }

        // Cancelar el trabajo actual y reiniciar
        if (workRequest != null) {
            workManager.cancelWorkById(workRequest.getId());
        }

        startContadorWorker();  // Iniciar uno nuevo
    }

    // Guardar el estado del Worker al cambiar de orientación
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (workRequest != null) {
            outState.putString(TIMER_WORK_ID, workRequest.getId().toString());
        }
    }

    private void updateBtnIcon(boolean isTimerRunning) {
        if (isTimerRunning) {
            binding.ibStart.setImageResource(R.drawable.ic_refresh);
        } else {
            binding.ibStart.setImageResource(R.drawable.ic_play);
        }
    }



    // no se cancelaban los conteos :c
}