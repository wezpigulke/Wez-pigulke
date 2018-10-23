package com.example.kuba.dsadsax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database.db";

    public static final String UZYTKOWNICY = "Uzytkownicy";
    public static final String UZYTKOWNICY_ID = "ID";
    public static final String UZYTKOWNICY_IMIE = "Imie";

    public static final String PRZYPOMNIENIE = "Przypomnienie";
    public static final String PRZYPOMNIENIE_ID = "ID";
    public static final String PRZYPOMNIENIE_GODZINA = "Godzina";
    public static final String PRZYPOMNIENIE_DATA = "Data";
    public static final String PRZYPOMNIENIE_LEK = "Lek";
    public static final String PRZYPOMNIENIE_DAWKA = "Dawka";
    public static final String PRZYPOMNIENIE_ILOSC_DNI = "Ilosc_dni";
    public static final String PRZYPOMNIENIE_PROFIL = "Profil";
    public static final String PRZYPOMNIENIE_TYP = "Typ";
    public static final String PRZYPOMNIENIE_WSZYSTKIEGODZINY = "Wszystkie_godziny";

    public static final String NOTYFIKACJA = "Notyfikacja";
    public static final String NOTYFIKACJA_ID = "ID";
    public static final String NOTYFIKACJA_ID_NOTYFIKACJA = "ID_notyfikacja";
    public static final String NOTYFIKACJA_PRZYPOMNIENIE = "ID_przypomnienie";
    public static final String NOTYFIKACJA_GODZINA = "Godzina";
    public static final String NOTYFIKACJA_DATA = "Data";

    public static final String DOKTORZY = "Doktorzy";
    public static final String DOKTORZY_ID = "ID";
    public static final String DOKTORZY_IMIE = "Imie";
    public static final String DOKTORZY_NAZWISKO = "Nazwisko";
    public static final String DOKTORZY_SPECJALIZACJA = "Specjalizacja";
    public static final String DOKTORZY_NUMER = "Numer";

    public static final String WIZYTY = "Wizyty";
    public static final String WIZYTY_ID = "ID";
    public static final String WIZYTY_GODZINA = "Godzina";
    public static final String WIZYTY_DATA = "Data";
    public static final String WIZYTY_IMIE = "Imie";
    public static final String WIZYTY_NAZWISKO = "Nazwisko";
    public static final String WIZYTY_SPECJALIZACJA = "Specjalizacja";
    public static final String WIZYTY_ADRES = "Adres";
    public static final String WIZYTY_PROFIL = "Profil";

    public static final String POMIARY = "Pomiary";
    public static final String POMIARY_ID = "ID";
    public static final String POMIARY_TYP = "Typ";
    public static final String POMIARY_WYNIK = "Wynik";
    public static final String POMIARY_PROFIL = "Profil";
    public static final String POMIARY_GODZINA = "Godzina";
    public static final String POMIARY_DATA = "Data";

    public static final String TYP_POMIAR = "Typ_pomiar";
    public static final String TYP_POMIAR_ID = "ID";
    public static final String TYP_POMIAR_NAZWA = "Typ";

    public static final String NOTATKI = "Notatki";
    public static final String NOTATKI_ID = "ID";
    public static final String NOTATKI_TYTUL = "Tytul";
    public static final String NOTATKI_TRESC = "Tresc";
    public static final String NOTATKI_PROFIL = "Profil";
    public static final String NOTATKI_DATA = "Data";

    public static final String STATYSTYKI = "Statystyki";
    public static final String STATYSTYKI_ID = "ID";
    public static final String STATYSTYKI_WZIETE = "Wziete";
    public static final String STATYSTYKI_NIEWZIETE = "Niewziete";

    public static final String DODANE_PRZ = "Dodane_przypomnienia";
    public static final String DODANE_PRZ_ID = "ID";

    public static final String USUNIETE_PRZ = "Usuniete_przypomnienia";
    public static final String USUNIETE_PRZ_ID = "ID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void removeData(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UZYTKOWNICY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Imie TEXT)");
        db.execSQL("CREATE TABLE " + PRZYPOMNIENIE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Godzina TEXT, Data TEXT, Lek TEXT, Dawka TEXT, Ilosc_dni Integer, Profil TEXT, Typ INTEGER, Wszystkie_godziny TEXT)");
        db.execSQL("CREATE TABLE " + NOTYFIKACJA + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ID_notyfikacja INTEGER, ID_przypomnienie INTEGER, Godzina TEXT, Data TEXT)");
        db.execSQL("CREATE TABLE " + DOKTORZY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Imie TEXT, Nazwisko TEXT, Specjalizacja TEXT, Numer Number(9))");
        db.execSQL("CREATE TABLE " + WIZYTY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Godzina TEXT, Data TEXT, Imie TEXT, Nazwisko TEXT, Specjalizacja TEXT, Adres TEXT, Profil TEXT)");
        db.execSQL("CREATE TABLE " + POMIARY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Typ TEXT, Wynik TEXT, Profil TEXT, Godzina TEXT, Data TEXT)");
        db.execSQL("CREATE TABLE " + TYP_POMIAR + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Typ TEXT)");
        db.execSQL("CREATE TABLE " + NOTATKI + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Tytul TEXT, Tresc TEXT, Profil TEXT, Data TEXT)");
        db.execSQL("CREATE TABLE " + STATYSTYKI + " (ID INTEGER PRIMARY KEY, Wziete INTEGER, Niewziete INTEGER)");
        db.execSQL("CREATE TABLE " + DODANE_PRZ + " (ID INTEGER PRIMARY KEY)");
        db.execSQL("CREATE TABLE " + USUNIETE_PRZ + " (ID INTEGER PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UZYTKOWNICY);
        db.execSQL("DROP TABLE IF EXISTS " + PRZYPOMNIENIE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTYFIKACJA);
        db.execSQL("DROP TABLE IF EXISTS " + DOKTORZY);
        db.execSQL("DROP TABLE IF EXISTS " + WIZYTY);
        db.execSQL("DROP TABLE IF EXISTS " + POMIARY);
        db.execSQL("DROP TABLE IF EXISTS " + TYP_POMIAR);
        db.execSQL("DROP TABLE IF EXISTS " + NOTATKI);
        db.execSQL("DROP TABLE IF EXISTS " + STATYSTYKI);
        db.execSQL("DROP TABLE IF EXISTS " + DODANE_PRZ);
        db.execSQL("DROP TABLE IF EXISTS " + USUNIETE_PRZ);
        onCreate(db);
    }

    /**
     * ============ DODANE PRZYPOMNIENIA =============
     **/

    public boolean insert_DODANE_PRZ(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DODANE_PRZ_ID, id);

        long result = db.insert(DODANE_PRZ, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public void remove_DODANE_PRZ(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DODANE_PRZ, DODANE_PRZ_ID + "=" + id, null);
    }

    /**
     * ============ USUNIETE PRZYPOMNIENIA =============
     **/

    public boolean insert_USUNIETE_PRZ(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USUNIETE_PRZ_ID, id);

        long result = db.insert(USUNIETE_PRZ, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public void remove_USUNIETE_PRZ(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USUNIETE_PRZ, USUNIETE_PRZ_ID + "=" + id, null);
    }

    /**
     * ============ STATYSTYKI =============
     **/

    public boolean insert_STATYSTYKI(Integer id, Integer wziete, Integer niewziete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STATYSTYKI_ID, id);
        contentValues.put(STATYSTYKI_WZIETE, wziete);
        contentValues.put(STATYSTYKI_NIEWZIETE, niewziete);

        long result = db.insert(STATYSTYKI, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor get_STATYSTYKI_WZIETE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Wziete " +
                        "FROM " + STATYSTYKI + " WHERE ID = " + id
                , null);
        return res;
    }

    public boolean update_STATYSTYKI_WZIETE(Integer id, Integer wziete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STATYSTYKI_WZIETE, wziete);

        long result = db.update(STATYSTYKI, contentValues, "ID=" + id, null);

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor get_STATYSTYKI_NIEWZIETE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Niewziete " +
                        "FROM " + STATYSTYKI + " WHERE ID = " + id
                , null);
        return res;
    }

    public boolean update_STATYSTYKI_NIEWZIETE(Integer id, Integer niewziete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STATYSTYKI_NIEWZIETE, niewziete);

        long result = db.update(STATYSTYKI, contentValues, "ID=" + id, null);

        if (result == -1)
            return false;
        else
            return true;
    }

    public void remove_STATYSTYKI(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(STATYSTYKI, STATYSTYKI_ID + "=" + id, null);
    }

    /**
     * ============ NOTYFIKACJA ============
     **/

    public boolean insert_NOTYFIKACJA(Integer notyfikacja, Integer przypomnienie, String godzina, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTYFIKACJA_ID_NOTYFIKACJA, notyfikacja);
        contentValues.put(NOTYFIKACJA_PRZYPOMNIENIE, przypomnienie);
        contentValues.put(NOTYFIKACJA_GODZINA, godzina);
        contentValues.put(NOTYFIKACJA_DATA, data);

        long result = db.insert(NOTYFIKACJA, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData_NOTYFIKACJA() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT A.ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil " +
                "FROM " + NOTYFIKACJA + " A " +
                "INNER JOIN " + PRZYPOMNIENIE + " B " +
                "ON " + "B.ID = A.ID_przypomnienie"
                , null);
        return res;
    }

    public Cursor getUserData_NOTYFIKACJA(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT A.ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil " +
                "FROM " + NOTYFIKACJA + " A " +
                "INNER JOIN " + PRZYPOMNIENIE + " B " +
                "ON " + "B.ID = A.ID_przypomnienie " +
                "WHERE " + " B.Profil" + "='" + user + "'", null);
        return res;
    }

    public Cursor getCount_NOTYFIKACJA(Integer id, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(ID_notyfikacja) " +
                "FROM " + NOTYFIKACJA +
                " WHERE " + "ID_przypomnienie" + "=" + "'" + id + "'" +
                " AND " + "Data" + "=" + "'" + data + "'", null);
        return res;
    }

    public Cursor getCountAll_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(ID_notyfikacja) " +
                "FROM " + NOTYFIKACJA +
                " WHERE " + "ID_przypomnienie" + "=" + "'" + id + "'", null);
        return res;
    }

    public Cursor getdataID_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT A.ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil, " +
                "A.ID_notyfikacja, A.ID_przypomnienie, B.Typ, B.Ilosc_dni " +
                "FROM " + NOTYFIKACJA + " A " +
                "INNER JOIN " + PRZYPOMNIENIE + " B " +
                "ON " + "B.ID = A.ID_przypomnienie " +
                "WHERE " + " A.ID" + "=" + id, null);
        return res;
    }

    public Cursor getDates_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT A.Data " +
                "FROM " + NOTYFIKACJA + " A " +
                "INNER JOIN " + PRZYPOMNIENIE + " B " +
                "ON " + "B.ID = A.ID_przypomnienie " +
                "WHERE " + " B.ID" + "=" + id, null);
        return res;
    }

    public Cursor getID_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ID_notyfikacja FROM " + NOTYFIKACJA + " WHERE " + NOTYFIKACJA_PRZYPOMNIENIE + "=" + id, null);
        return res;
    }

    public Cursor getID_NOTYFIKACJAFROMID(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ID_notyfikacja FROM " + NOTYFIKACJA + " WHERE " + NOTYFIKACJA_ID + "=" + id, null);
        return res;
    }

    public Cursor getID_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ID_przypomnienie FROM " + NOTYFIKACJA + " WHERE " + NOTYFIKACJA_ID_NOTYFIKACJA + "=" + id, null);
        return res;
    }

    public Cursor getMAXid_NOTYFIKACJA() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT IFNULL(MAX(ID_notyfikacja), 0) FROM " + NOTYFIKACJA, null);
        return res;
    }

    public void remove_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTYFIKACJA, NOTYFIKACJA_ID_NOTYFIKACJA + "=" + id, null);
    }

    public void remove_ALL_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTYFIKACJA, NOTYFIKACJA_PRZYPOMNIENIE + "=" + id, null);
    }


    /**
     * ============ NOTATKI ============
     **/

    public boolean insert_NOTATKI(String tytul, String tekst, String profil, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTATKI_TYTUL, tytul);
        contentValues.put(NOTATKI_TRESC, tekst);
        contentValues.put(NOTATKI_PROFIL, profil);
        contentValues.put(NOTATKI_DATA, data);
        long result = db.insert(NOTATKI, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData_NOTATKI() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + NOTATKI, null);
        return res;
    }

    public Cursor getUserData_NOTATKI(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + NOTATKI + " WHERE Profil='" + user + "'", null);
        return res;
    }

    public void remove_NOTATKI(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTATKI, NOTATKI_ID + "=" + id, null);
    }

    /**
     * ============ POMIARY ============
     **/

    public boolean insert_POMIARY(String typ, String wynik, String profil, String godzina, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POMIARY_TYP, typ);
        contentValues.put(POMIARY_WYNIK, wynik);
        contentValues.put(POMIARY_PROFIL, profil);
        contentValues.put(POMIARY_GODZINA, godzina);
        contentValues.put(POMIARY_DATA, data);
        long result = db.insert(POMIARY, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData_POMIARY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + POMIARY, null);
        return res;
    }

    public Cursor getUserData_POMIARY(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Profil='" + user + "'", null);
        return res;
    }

    public Cursor getUserTypeData_POMIARY(String user, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Profil='" + user + "' AND Typ='" + type + "'" , null);
        return res;
    }

    public Cursor getUserType_POMIARY(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Typ='" + type + "'" , null);
        return res;
    }

    public void remove_POMIARY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POMIARY, POMIARY_ID + "=" + id, null);
    }

    /**
     * ============ TYP POMIAR ============
     **/

    public boolean insert_TYP_POMIAR(String typ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYP_POMIAR_NAZWA, typ);
        long result = db.insert(TYP_POMIAR, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData_TYP_POMIAR() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TYP_POMIAR, null);
        return res;
    }

    public Cursor getCount_TYP_POMIAR() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(" + TYP_POMIAR_ID + ") FROM " + TYP_POMIAR, null);
        return res;
    }

    public Cursor getPomiar_TYP_POMIAR() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + TYP_POMIAR_NAZWA + " FROM " + TYP_POMIAR, null);
        return res;
    }

    /**
     * ============ UZYTKOWNICY ============
     **/

    public boolean insert_UZYTKOWNICY(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UZYTKOWNICY_IMIE, name);
        long result = db.insert(UZYTKOWNICY, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData_UZYTKOWNICY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + UZYTKOWNICY, null);
        return res;
    }

    public Cursor getAllName_UZYTKOWNICY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Imie FROM " + UZYTKOWNICY, null);
        return res;
    }

    public Cursor getName_UZYTKOWNICY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor nam = db.rawQuery("SELECT Imie FROM " + UZYTKOWNICY + " LIMIT 1", null);
        return nam;
    }

    public void remove_UZYTKOWNICY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UZYTKOWNICY, UZYTKOWNICY_ID + "=" + id, null);
    }

    /**
     * ============ DOKTORZY ============
     **/

    public boolean insert_DOKTORZY(String name, String surname, String specialization, String phone_number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DOKTORZY_IMIE, name);
        contentValues.put(DOKTORZY_NAZWISKO, surname);
        contentValues.put(DOKTORZY_SPECJALIZACJA, specialization);
        contentValues.put(DOKTORZY_NUMER, phone_number);
        long result = db.insert(DOKTORZY, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData_DOKTORZY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DOKTORZY, null);
        return res;
    }

    public Cursor getNumer_DOKTORZY(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + DOKTORZY_NUMER + " FROM " + DOKTORZY + " WHERE " + DOKTORZY_ID + "=" + id, null);
        return res;
    }

    public void remove_DOKTORZY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DOKTORZY, DOKTORZY_ID + "=" + id, null);
    }

    /**
     * ============ PRZYPOMNIENIE ============
     **/

    public boolean insert_PRZYPOMNIENIE(String hour, String date, String medicine, String dawka, Integer days, String profile, Integer type, String alltime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PRZYPOMNIENIE_GODZINA, hour);
        contentValues.put(PRZYPOMNIENIE_DATA, date);
        contentValues.put(PRZYPOMNIENIE_LEK, medicine);
        contentValues.put(PRZYPOMNIENIE_DAWKA, dawka);
        contentValues.put(PRZYPOMNIENIE_ILOSC_DNI, days);
        contentValues.put(PRZYPOMNIENIE_PROFIL, profile);
        contentValues.put(PRZYPOMNIENIE_TYP, type);
        contentValues.put(PRZYPOMNIENIE_WSZYSTKIEGODZINY, alltime);

        long result = db.insert(PRZYPOMNIENIE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData_PRZYPOMNIENIE() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + PRZYPOMNIENIE, null);
        return res;
    }

    public Cursor getDays_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Ilosc_dni FROM " + PRZYPOMNIENIE + " WHERE " + PRZYPOMNIENIE_ID + "=" + id, null);
        return res;
    }

    public Cursor getUserData_PRZYPOMNIENIE(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + PRZYPOMNIENIE + " WHERE Profil='" + user + "'", null);
        return res;
    }

    public Cursor getMAXid_PRZYPOMNIENIE() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT IFNULL(MAX(ID), 1) FROM " + PRZYPOMNIENIE, null);
        return res;
    }

    public Cursor getMedicine_PRZYPOMNIENIE() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + PRZYPOMNIENIE_LEK + " FROM " + PRZYPOMNIENIE, null);
        return res;
    }

    public Cursor getUserMedicine_PRZYPOMNIENIE(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + PRZYPOMNIENIE_LEK + " FROM " + PRZYPOMNIENIE + " WHERE " + PRZYPOMNIENIE_PROFIL + "='" + user + "'", null);
        return res;
    }

    public void remove_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRZYPOMNIENIE, PRZYPOMNIENIE_ID + "=" + id, null);
    }

    public boolean updateDays_PRZYPOMNIENIE(Integer id, String date, Integer days) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PRZYPOMNIENIE_DATA, date);
        contentValues.put(PRZYPOMNIENIE_ILOSC_DNI, days);

        long result = db.update(PRZYPOMNIENIE, contentValues, "ID=" + id, null);

        if (result == -1)
            return false;
        else
            return true;
    }

    /**
     * ============ WIZYTY ============
     **/


    public boolean insert_WIZYTY(String godzina, String data, String imie, String nazwisko, String specjalizacja, String adres, String profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WIZYTY_GODZINA, godzina);
        contentValues.put(WIZYTY_DATA, data);
        contentValues.put(WIZYTY_IMIE, imie);
        contentValues.put(WIZYTY_NAZWISKO, nazwisko);
        contentValues.put(WIZYTY_SPECJALIZACJA, specjalizacja);
        contentValues.put(WIZYTY_ADRES, adres);
        contentValues.put(WIZYTY_PROFIL, profile);
        long result = db.insert(WIZYTY, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData_WIZYTY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + WIZYTY, null);
        return res;
    }

    public Cursor getUserData_WIZYTY(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + WIZYTY + " WHERE Profil='" + name + "'", null);
        return res;
    }

    public Cursor getSelectedData_WIZYTY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + WIZYTY + " WHERE id=" + id, null);
        return res;
    }

    public Cursor getID_WIZYTY(String godzina, String data, String imie, String nazwisko) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT id FROM " + WIZYTY + " WHERE godzina='" + godzina + "' AND data='" + data + "' AND imie='" + imie + "' AND nazwisko='" + nazwisko + "'", null);
        return res;
    }

    public Cursor getMAXid_WIZYTY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT MAX(ID) FROM " + WIZYTY, null);
        return res;
    }

    public void remove_WIZYTY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WIZYTY, WIZYTY_ID + "=" + id, null);
    }

    public void removeUser_WIZYTY(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WIZYTY, WIZYTY_PROFIL + "='" + name + "'", null);
    }

}
