package com.wezpigulke.Settings;

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
import com.wezpigulke.Database.DatabaseHelper;
import com.wezpigulke.NotificationSystem.NotificationReceiver;
import com.wezpigulke.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class GoToSettings extends Fragment {

    DatabaseHelper myDb;

    private Integer i = 0;

    private Integer progress;

    private ProgressBar myprogressbar;

    private TextView tProgres;
    private TextView tWziete;
    private TextView tZapomniane;

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

        myDb = new DatabaseHelper(getActivity());

        View v = inflater.inflate(R.layout.settings, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        final Button bt = v.findViewById(R.id.button2);
        final Button pdf = v.findViewById(R.id.button5);

        myprogressbar = v.findViewById(R.id.myprogressbar);

        tProgres = v.findViewById(R.id.textView8);
        tWziete = v.findViewById(R.id.textView13);
        tZapomniane = v.findViewById(R.id.textView14);

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    Cursor cursorAllDataNotyfikacja = myDb.getAllData_NOTYFIKACJA();

                    if(cursorAllDataNotyfikacja.getCount() != 0) {
                        while(cursorAllDataNotyfikacja.moveToNext()) {
                            Cursor cursorRandNotyfikacja = myDb.getRand_NOTYFIKACJA(cursorAllDataNotyfikacja.getInt(0));
                            cursorRandNotyfikacja.moveToFirst();

                            AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
                            Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    getActivity(), cursorRandNotyfikacja.getInt(0), myIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            assert alarmManager != null;
                            alarmManager.cancel(pendingIntent);
                        }
                    }

                    Cursor cw = myDb.getAllData_WIZYTY();

                    if(cw.getCount() != 0) {
                        while(cw.moveToNext()) {
                            AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

                            Cursor crand = myDb.getRand_WIZYTY(cw.getInt(0));
                            crand.moveToFirst();

                            Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    getActivity(), crand.getInt(0), myIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            assert alarmManager != null;
                            alarmManager.cancel(pendingIntent);

                            Toast.makeText(getContext(), "Anulacja:" + String.valueOf(crand.getInt(0)), Toast.LENGTH_LONG).show();

                            myIntent = new Intent(getActivity(), NotificationReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(
                                    getActivity(), crand.getInt(0)+1, myIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                        }
                    }

                    myDb.removeData(getActivity());

                    System.exit(0);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        bt.setOnClickListener(view -> new AlertDialog.Builder(getActivity())
                .setMessage("Czy chcesz usunąć wszystkie dane?")
                .setPositiveButton("Tak", dialogClickListener)
                .setNegativeButton("Nie", dialogClickListener)
                .create()
                .show());

        pdf.setOnClickListener(v1 -> {
            try {
                createPdfWrapper();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
        });

        Cursor cw = myDb.getWziete_STATYSTYKI(0);
        cw.moveToFirst();
        int wziete = Integer.parseInt(cw.getString(0));

        Cursor cn = myDb.getNiewziete_STATYSTYKI(0);
        cn.moveToFirst();
        int niewziete = Integer.parseInt(cn.getString(0));

        if (niewziete >= 0 && wziete == 0) progress = 0;
        else if (wziete >= 0 && niewziete == 0) progress = 100;
        else progress = ((wziete * 100) / (wziete + niewziete));

        tWziete.setText(Html.fromHtml("Wzięte: " + "<b>" + wziete + "</b> ", 0));
        tZapomniane.setText(Html.fromHtml("Zapomniane: " + "<b>" + niewziete + "</b> ", 0));
        tProgres.setText(Html.fromHtml("Progres: " + "<b>" + progress + "%" + "</b>", 0));

        tWziete.setVisibility(View.INVISIBLE);
        tZapomniane.setVisibility(View.INVISIBLE);
        tProgres.setVisibility(View.INVISIBLE);

        myprogressbar.setScaleY(10);

        if (progress < 50) myprogressbar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        else myprogressbar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

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

        return v;

    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                showMessageOKCancel(
                        (dialog, which) -> requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS));
                return;
            }

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            createPdf();
        }
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
                    Toast.makeText(getActivity(), "Brak pozwolenia na dostęp do plików", Toast.LENGTH_SHORT)
                            .show();
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

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }

        pdfFile = new File(docsFolder.getAbsolutePath(), "Raport.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        Paragraph p = new Paragraph("LISTA STARYCH PRZYPOMNIEN", FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.RED));
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        document.add(Chunk.NEWLINE);

        Cursor cu = myDb.getAllName_UZYTKOWNICY();

        while (cu.moveToNext()) {

            String user = cu.getString(0);

            Paragraph pz = new Paragraph("Profil: " + user, FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.BOLD, BaseColor.BLUE));
            pz.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(pz);
            document.add(Chunk.NEWLINE);

            PdfPCell cell;

            PdfPTable table = new PdfPTable(6);
            cell = new PdfPCell(new Paragraph("Godzina", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Data", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Lek", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Dawka", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Potw.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Status", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            document.add(table);

            String statusHistorii;


            BaseColor greenColor = WebColors.getRGBColor("#00ff00");
            BaseColor redColor = WebColors.getRGBColor("#ff0000");

            Cursor cp = myDb.getUserData_HISTORIA(user);
            if (cp.getCount() != 0) {
                while (cp.moveToNext()) {

                    PdfPTable table_result = new PdfPTable(6);

                    cell = new PdfPCell(new Paragraph(cp.getString(2)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_result.addCell(cell);

                    cell = new PdfPCell(new Paragraph(cp.getString(3).substring(0, 5)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_result.addCell(cell);

                    cell = new PdfPCell(new Paragraph(cp.getString(4)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_result.addCell(cell);

                    cell = new PdfPCell(new Paragraph(cp.getString(5)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_result.addCell(cell);

                    cell = new PdfPCell(new Paragraph(cp.getString(6)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_result.addCell(cell);

                    statusHistorii = cp.getString(7);

                    switch (statusHistorii) {
                        case "WZIETE":
                            cell = new PdfPCell(new Paragraph(""));
                            cell.setBackgroundColor(greenColor);
                            table_result.addCell(cell);
                            break;
                        case "NIEWZIETE":
                            cell = new PdfPCell(new Paragraph(""));
                            cell.setBackgroundColor(redColor);
                            table_result.addCell(cell);
                            break;
                        case "BRAK":
                            cell = new PdfPCell(new Paragraph("BRAK"));
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_result.addCell(cell);
                            break;
                    }

                    document.add(table_result);

                }
            }

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

        }

        document.close();
        previewPdf();

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

}
