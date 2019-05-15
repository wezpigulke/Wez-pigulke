package com.wezpigulke.go_to;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.notification.NotificationReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class GoToSettings extends Fragment {

    DatabaseHelper myDb;

    private Integer i;
    private Integer progress;
    private ProgressBar myprogressbar;
    private int wziete;
    private int niewziete;

    private TextView tProgres;
    private TextView tWziete;
    private TextView tZapomniane;
    private Button bt;
    private Button pdf;
    private PdfPCell cell;
    private PdfPTable table;
    private DialogInterface.OnClickListener dialogClickListener;

    private Cursor cursor;
    private Cursor cursorTemp;

    private PdfPTable table_result;

    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    private Handler mHandler = new Handler();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Ustawienia");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.settings, container, false);
        initializeVariables(v);
        dialogClickListener();
        btClickListener();
        pdfClickListener();
        countProgress();
        setProgress();
        addProgressBar();
        return v;

    }

    private void initializeVariables(View v) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        myDb = new DatabaseHelper(getActivity());
        bt = v.findViewById(R.id.button2);
        pdf = v.findViewById(R.id.button5);

        myprogressbar = v.findViewById(R.id.myprogressbar);
        tProgres = v.findViewById(R.id.textView8);
        tWziete = v.findViewById(R.id.textView13);
        tZapomniane = v.findViewById(R.id.textView14);

        i = 0;
        wziete = 0;
        niewziete = 0;

    }

    private void dialogClickListener() {

        dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    cursor = myDb.getAllData_NOTYFIKACJA();

                    if (cursor.getCount() != 0) {
                        while (cursor.moveToNext()) {

                            cursor = myDb.getRand_NOTYFIKACJA(cursor.getInt(0));
                            cursor.moveToFirst();
                            cancelAlarm(cursor.getInt(0));

                        }
                    }

                    cursor = myDb.getAllData_WIZYTY();

                    if (cursor.getCount() != 0) {
                        while (cursor.moveToNext()) {

                            cursorTemp = myDb.getRand_WIZYTY(cursor.getInt(0));
                            cursorTemp.moveToFirst();
                            cancelAlarm(cursorTemp.getInt(0));
                            cancelAlarm(cursorTemp.getInt(0) - 1);

                        }
                    }

                    myDb.removeData(getActivity());

                    System.exit(0);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

    }

    private void btClickListener() {

        bt.setOnClickListener(view -> new AlertDialog.Builder(getActivity())
                .setMessage("Czy chcesz usunąć wszystkie dane?")
                .setPositiveButton("Tak", dialogClickListener)
                .setNegativeButton("Nie", dialogClickListener)
                .create()
                .show());

    }

    private void pdfClickListener() {
        pdf.setOnClickListener(v1 -> {
            try {
                createPdfWrapper();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
        });
    }

    private void countProgress() {
        cursor = myDb.getWziete_STATYSTYKI(0);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            wziete = Integer.parseInt(cursor.getString(0));
        }

        cursor = myDb.getNiewziete_STATYSTYKI(0);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            niewziete = Integer.parseInt(cursor.getString(0));
        }

        if (niewziete >= 0 && wziete == 0) progress = 0;
        else if (wziete >= 0 && niewziete == 0) progress = 100;
        else progress = ((wziete * 100) / (wziete + niewziete));
    }

    private void setProgress() {
        if (Build.VERSION.SDK_INT >= 24) {
            tWziete.setText(Html.fromHtml("Wzięte: " + "<b>" + wziete + "</b> ", 0));
            tZapomniane.setText(Html.fromHtml("Zapomniane: " + "<b>" + niewziete + "</b> ", 0));
            tProgres.setText(Html.fromHtml("Progres: " + "<b>" + progress + "%" + "</b>", 0));
        } else {
            tWziete.setText(Html.fromHtml("Wzięte: " + "<b>" + wziete + "</b> "));
            tZapomniane.setText(Html.fromHtml("Zapomniane: " + "<b>" + niewziete + "</b> "));
            tProgres.setText(Html.fromHtml("Progres: " + "<b>" + progress + "%" + "</b>"));
        }

        tWziete.setVisibility(View.INVISIBLE);
        tZapomniane.setVisibility(View.INVISIBLE);
        tProgres.setVisibility(View.INVISIBLE);
    }

    private void addProgressBar() {

        myprogressbar.setScaleY(10);

        if (Build.VERSION.SDK_INT >= 21) {
            if (progress < 50) myprogressbar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            else myprogressbar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            myprogressbar.setVisibility(View.INVISIBLE);
        }

        new Thread(() -> {
            while (i <= progress) {
                i++;
                android.os.SystemClock.sleep(5);
                mHandler.post(() -> myprogressbar.setProgress(i));
            }
            mHandler.post(() -> {
                tWziete.setVisibility(View.VISIBLE);
                tZapomniane.setVisibility(View.VISIBLE);
                tProgres.setVisibility(View.VISIBLE);
            });
        }).start();

    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                showMessageOKCancel((dialog, which) -> requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS));
                return;
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        } else createPdf();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException | DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Brak pozwolenia na dostęp do plików", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        String message = "Musisz zezwolić na dostęp do plików";
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @SuppressWarnings("all")
    private void createPdf() throws FileNotFoundException, DocumentException {

        BaseColor greenColor = WebColors.getRGBColor("#00ff00");
        BaseColor redColor = WebColors.getRGBColor("#ff0000");

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) docsFolder.mkdir();

        pdfFile = new File(docsFolder.getAbsolutePath(), "Raport.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        Paragraph p = new Paragraph("LISTA STARYCH PRZYPOMNIEN",
                FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.RED));
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        document.add(Chunk.NEWLINE);

        cursor = myDb.getAllName_UZYTKOWNICY();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String user = cursor.getString(0);

                Paragraph pz = new Paragraph("Profil: " + user,
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.BOLD, BaseColor.BLUE));
                pz.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(pz);
                document.add(Chunk.NEWLINE);

                table = new PdfPTable(6);

                generateNewCell("Godzina");
                generateNewCell("Data");
                generateNewCell("Lek");
                generateNewCell("Dawka");
                generateNewCell("Potw.");
                generateNewCell("Status");
                document.add(table);

                cursor = myDb.getUserData_HISTORIA(user);
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {

                        table_result = new PdfPTable(6);

                        addNewCell(cursor.getString(2));
                        addNewCell(cursor.getString(3).substring(0, 5));
                        addNewCell(cursor.getString(4));
                        addNewCell(cursor.getString(5));
                        addNewCell(cursor.getString(6));

                        String statusHistorii = cursor.getString(7);

                        switch (statusHistorii) {
                            case "WZIETE":
                                cell = new PdfPCell(new Paragraph(""));
                                cell.setBackgroundColor(greenColor);
                                break;
                            case "NIEWZIETE":
                                cell = new PdfPCell(new Paragraph(""));
                                cell.setBackgroundColor(redColor);
                                break;
                            case "BRAK":
                                cell = new PdfPCell(new Paragraph("BRAK"));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                break;
                        }

                        table_result.addCell(cell);
                        document.add(table_result);
                    }
                }
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
            }
        }
        document.close();
        previewPdf();
    }

    private void generateNewCell(String text) {
        cell = new PdfPCell(new Paragraph(text, FontFactory.getFont(
                FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addNewCell(String value) {
        cell = new PdfPCell(new Paragraph(value));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_result.addCell(cell);
    }

    private void previewPdf() {

        PackageManager packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Pobierz program do otwierania plików PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public void onResume() {

        super.onResume();

    }

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
        if (cursorTemp != null) cursorTemp.close();
        super.onDestroy();
    }

    private void cancelAlarm(Integer id) {
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getActivity(), id, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }

}
