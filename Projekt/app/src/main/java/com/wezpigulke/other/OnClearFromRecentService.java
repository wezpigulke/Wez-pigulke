package com.wezpigulke.other;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.wezpigulke.DatabaseHelper;

public class OnClearFromRecentService extends Service {

    private DatabaseHelper myDb;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Toast.makeText(getApplicationContext(), "Aplikacja Weź pigułkę została zamknięta. Włącz ją ponownie, jeżeli chcesz otrzymywać powiadomienia", Toast.LENGTH_LONG).show();

        myDb = new DatabaseHelper(getApplicationContext());
        myDb.updateStatus_CZYZAMKNIETA(1);
        stopSelf();
    }
}