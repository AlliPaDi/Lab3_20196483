package com.example.lab3_20196483.workers;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ContadorWorker extends Worker {

    public static final String TAG = "ContadorWorker";

    public ContadorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        long workDuration = 25 * 60 * 1000;  // 25 minutos en milisegundos
        long interval = 1000;  // Actualizar cada segundo

        long endTime = System.currentTimeMillis() + workDuration;
        long timeLeft;

        while (System.currentTimeMillis() < endTime) {
            timeLeft = endTime - System.currentTimeMillis();

            // Calcular minutos y segundos restantes
            int minutesLeft = (int) (timeLeft / 1000) / 60;
            int secondsLeft = (int) (timeLeft / 1000) % 60;

            // Enviar el progreso actualizado
            Data progressData = new Data.Builder()
                    .putInt("minutesLeft", minutesLeft)
                    .putInt("secondsLeft", secondsLeft)
                    .build();
            setProgressAsync(progressData);

            Log.d(TAG, "Tiempo restante: " + minutesLeft + ":" + secondsLeft);
            SystemClock.sleep(interval);
        }


        return Result.success();
    }
}
