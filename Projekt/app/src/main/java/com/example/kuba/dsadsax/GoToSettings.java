package com.example.kuba.dsadsax;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
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
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import me.itangqi.waveloadingview.WaveLoadingView;

public class GoToSettings extends Fragment {

    DatabaseHelper myDb;

    WaveLoadingView waveLoadingView;

    Integer progress;
    TextView tProgres;
    TextView tWziete;
    TextView tZapomniane;
    private int i = 0;

    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    private Handler mHandler = new Handler();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Ustawienia");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myDb = new DatabaseHelper(getActivity());

        View v = inflater.inflate(R.layout.settings, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        final Button bt = v.findViewById(R.id.button2);
        final Button pdf = v.findViewById(R.id.button5);

        bt.setOnClickListener(view -> {
            myDb.removeData(getActivity());
            System.exit(0);
        });

        pdf.setOnClickListener(v1 -> {
            try {
                createPdfWrapper();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });

        tProgres = v.findViewById(R.id.textView8);
        tWziete = v.findViewById(R.id.textView13);
        tZapomniane = v.findViewById(R.id.textView14);

        Cursor cw = myDb.get_STATYSTYKI_WZIETE(0);
        cw.moveToFirst();
        int wziete = Integer.parseInt(cw.getString(0));

        Cursor cn = myDb.get_STATYSTYKI_NIEWZIETE(0);
        cn.moveToFirst();
        int niewziete = Integer.parseInt(cn.getString(0));

        if (niewziete >= 0 && wziete == 0) progress = 0;
        else if (wziete >= 0 && niewziete == 0) progress = 100;
        else progress = ((wziete * 100) / (wziete + niewziete));

        tWziete.setText(Html.fromHtml("</b> " + "Wzięte: " + "<b>" + wziete));
        tZapomniane.setText(Html.fromHtml("</b> " + "Zapomniane: " + "<b>" + niewziete));
        tProgres.setText(Html.fromHtml("<b>" + progress + "%" + "</b>"));

        tWziete.setVisibility(View.INVISIBLE);
        tZapomniane.setVisibility(View.INVISIBLE);
        tProgres.setVisibility(View.INVISIBLE);

        ProgressBar myprogressbar = v.findViewById(R.id.myprogressbar);
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

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                showMessageOKCancel("Musisz zezwolić na dostęp do plików",
                        (dialog, which) -> {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        });
                return;
            }

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        } else {
            createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Brak pozwolenia na dostęp do plików", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

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

            PdfPTable table = new PdfPTable(6);
            table.addCell(new PdfPCell(new Paragraph("Godzina")));
            table.addCell(new PdfPCell(new Paragraph("Data")));
            table.addCell(new PdfPCell(new Paragraph("Lek")));
            table.addCell(new PdfPCell(new Paragraph("Dawka")));
            table.addCell(new PdfPCell(new Paragraph("Potw.")));
            table.addCell(new PdfPCell(new Paragraph("Status")));
            document.add(table);

            String statusHistorii;

            PdfPCell cell;

            BaseColor greenColor = WebColors.getRGBColor("#00ff00");
            BaseColor redColor = WebColors.getRGBColor("#ff0000");

            Cursor cp = myDb.getUserData_HISTORIA(user);
            if(cp.getCount()!=0) {
                while (cp.moveToNext()) {

                    PdfPTable table_result = new PdfPTable(6);
                    table_result.addCell(new PdfPCell(new Paragraph(cp.getString(2))));
                    table_result.addCell(new PdfPCell(new Paragraph(cp.getString(3))));
                    table_result.addCell(new PdfPCell(new Paragraph(cp.getString(4))));
                    table_result.addCell(new PdfPCell(new Paragraph(cp.getString(5))));
                    table_result.addCell(new PdfPCell(new Paragraph(cp.getString(6))));

                    statusHistorii = cp.getString(7);

                    if (statusHistorii.equals("WZIETE")){
                        cell = new PdfPCell(new Paragraph(""));
                        cell.setBackgroundColor(greenColor);
                        table_result.addCell(cell);
                    }

                    else if (statusHistorii.equals("NIEWZIETE")){
                        cell = new PdfPCell(new Paragraph(""));
                        cell.setBackgroundColor(redColor);
                        table_result.addCell(cell);
                    }

                    else if (statusHistorii.equals("BRAK INFORMACJI")) {
                        cell = new PdfPCell(new Paragraph(""));
                        table_result.addCell(cell);
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

        PackageManager packageManager = getActivity().getPackageManager();
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
