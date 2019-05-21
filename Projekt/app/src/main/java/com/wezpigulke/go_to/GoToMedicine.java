package com.wezpigulke.go_to;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.adapters.MedicineListAdapter;
import com.wezpigulke.add.AddMedicine;
import com.wezpigulke.classes.Medicine;
import com.wezpigulke.other.DecimalDigitsInputFilter;
import com.wezpigulke.other.OpenDialog;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToMedicine extends Fragment {

    DatabaseHelper myDb;
    private MedicineListAdapter adapter;
    private List<Medicine> results;
    private ListView lv;
    private Integer id;
    private String medicineName;
    private Integer medicineCount;
    private View v;
    private Integer id_l;
    private Cursor cursor;
    private FloatingActionButton fab;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Lista leków");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.medicine, container, false);
        initializeVariables();
        fabClickListener();
        listviewClickListener();
        listviewTouchListener();

        return v;

    }

    private void fabClickListener() {
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddMedicine.class);
            startActivity(intent);
        });
    }

    private void initializeVariables() {
        results = new ArrayList<>();
        lv = v.findViewById(R.id.medicineList);
        fab = v.findViewById(R.id.fabMedicine);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();
        aktualizujBaze();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void listviewTouchListener() {

        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                lv,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {

                            id = results.get(position).getId();
                            dialogRemove();

                        }

                    }

                });

        lv.setOnTouchListener(touchListener);

    }

    private void listviewClickListener() {
        lv.setOnItemClickListener((parent, view, position, id) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

            builder.setMessage("Co chcesz zrobić?").setCancelable(true)
                    .setPositiveButton("Aktualizuj ilość", (dialog, which) -> updateQuantity(position))
                    .setNegativeButton("Sprawdź informacje", (dialog, which) -> {
                        if (isOnline()) {
                            medicineName = results.get(position).getName();
                            new getInformationAboutMedicine().execute();
                        } else
                            Toast.makeText(getContext(), "Brak połączenia z internetem", Toast.LENGTH_LONG).show();
                    });

            builder.show();

        });
    }

    public void updateQuantity(Integer position) {

        id_l = results.get(position).getId();

        cursor = myDb.getNumber_LEK(id_l);
        cursor.moveToFirst();
        String ilosc = cursor.getString(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);
        builder.setTitle("Aktualizacja ilości");

        final EditText input = new EditText(getContext());

        input.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        input.setText(ilosc);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        input.setSelection(input.getText().length());

        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            myDb.update_LEK(id_l, Double.valueOf(input.getText().toString()));
            aktualizujBaze();
        });
        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    public boolean isOnline() {
        boolean var = false;
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            var = true;
        }
        return var;
    }

    public void aktualizujBaze() {

        results.clear();
        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());

        cursor = myDb.getData_LEK();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                results.add(new Medicine(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            }
        }

        adapter = new MedicineListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
        super.onDestroy();
    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> usunDane())
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    private void usunDane() {

        cursor = myDb.getDataNameFromId_LEK(id);
        cursor.moveToFirst();
        String nazwa = cursor.getString(0);

        cursor = myDb.getAllDataMedicine_PRZYPOMNIENIE(nazwa);

        if (cursor.getCount() == 0) {
            myDb.remove_LEK(id);
            aktualizujBaze();
        } else openDialog("Nie można usunąć. Posiadasz aktywne przypomnienie z tym lekiem.");

    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        assert getFragmentManager() != null;
        openDialog.show(getFragmentManager(), "GoToMedicine");
    }

    @SuppressLint("StaticFieldLeak")
    public class getInformationAboutMedicine extends AsyncTask<Void, Void, Void> {

        @SuppressLint("ShowToast")
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup.connect("http://bazalekow.leksykon.com.pl/szukaj-leku.html?a=search&o=0&p=50&cmn=" + medicineName).get();
                Elements elements = doc.select("div.results-drug-list-block.block-shadow > div.header-block > span.quantity-block > span.quantity");
                medicineCount = Integer.parseInt(elements.text());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint({"ResourceType", "ShowToast"})
        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            if (medicineCount > 0) {
                Intent intent = new Intent(v.getContext(), GoToMedicineInformation.class);
                intent.putExtra("medicineName", medicineName);
                startActivity(intent);
            } else {
                Handler handler = new Handler(Objects.requireNonNull(getActivity()).getMainLooper());
                handler.post(() -> Toast.makeText(getActivity(), "Nie znaleziono takiego leku w bazie", Toast.LENGTH_LONG).show());
            }

        }

    }

}
