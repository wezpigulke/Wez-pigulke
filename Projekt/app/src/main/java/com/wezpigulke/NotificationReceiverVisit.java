package com.wezpigulke;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiverVisit extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String powiadomienie = intent.getStringExtra("Value");

        Integer id_v = intent.getIntExtra("id", 0);
        String godzina = intent.getStringExtra("godzina");
        String data = intent.getStringExtra("data");
        String imie = intent.getStringExtra("imie");
        String nazwisko = intent.getStringExtra("nazwisko");
        String specjalizacja = intent.getStringExtra("specjalizacja");
        String adres = intent.getStringExtra("adres");
        String uzytkownik = intent.getStringExtra("uzytkownik");
        Integer wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, RepeatingActivityVisit.class);

        repeating_intent.putExtra("id", id_v);
        repeating_intent.putExtra("godzina", godzina);
        repeating_intent.putExtra("data", data);
        repeating_intent.putExtra("imie", imie);
        repeating_intent.putExtra("nazwisko", nazwisko);
        repeating_intent.putExtra("specjalizacja", specjalizacja);
        repeating_intent.putExtra("adres", adres);
        repeating_intent.putExtra("uzytkownik", uzytkownik);

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id_v, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound;
        alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);

        if (wybranyDzwiek==2) alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm2);
        else if (wybranyDzwiek==3) alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm3);
        else if (wybranyDzwiek==4) alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm4);
        else if (wybranyDzwiek==5) alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm5);
        else if (wybranyDzwiek==6) alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm6);
        else if (wybranyDzwiek==7) alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm7);
        else if (wybranyDzwiek==8) alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm8);
        else if (wybranyDzwiek==9) alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm9);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Weź pigułke")
                .setContentText(powiadomienie)
                .setSound(alarmSound)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);
        assert notificationManager != null;
        notificationManager.notify(id_v, builder.build());

    }

}
