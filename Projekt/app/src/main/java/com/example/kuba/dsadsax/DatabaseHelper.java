package com.example.kuba.dsadsax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";

    private static final String UZYTKOWNICY = "Uzytkownicy";
    private static final String UZYTKOWNICY_ID = "ID";
    private static final String UZYTKOWNICY_IMIE = "Imie";

    private static final String PRZYPOMNIENIE = "Przypomnienie";
    private static final String PRZYPOMNIENIE_ID = "ID";
    private static final String PRZYPOMNIENIE_GODZINA = "Godzina";
    private static final String PRZYPOMNIENIE_DATA = "Data";
    private static final String PRZYPOMNIENIE_LEK = "Lek";
    private static final String PRZYPOMNIENIE_DAWKA = "Dawka";
    private static final String PRZYPOMNIENIE_ILOSC_DNI = "Ilosc_dni";
    private static final String PRZYPOMNIENIE_PROFIL = "Profil";
    private static final String PRZYPOMNIENIE_TYP = "Typ";
    private static final String PRZYPOMNIENIE_WSZYSTKIEGODZINY = "Wszystkie_godziny";

    private static final String NOTYFIKACJA = "Notyfikacja";
    private static final String NOTYFIKACJA_ID = "ID";
    private static final String NOTYFIKACJA_ID_NOTYFIKACJA = "ID_notyfikacja";
    private static final String NOTYFIKACJA_PRZYPOMNIENIE = "ID_przypomnienie";
    private static final String NOTYFIKACJA_GODZINA = "Godzina";
    private static final String NOTYFIKACJA_OSTATNIADATA = "Data";

    private static final String DOKTORZY = "Doktorzy";
    private static final String DOKTORZY_ID = "ID";
    private static final String DOKTORZY_IMIE_I_NAZWISKO = "Imie_Nazwisko";
    private static final String DOKTORZY_SPECJALIZACJA = "Specjalizacja";
    private static final String DOKTORZY_NUMER = "Numer";
    private static final String DOKTORZY_ADRES = "Adres";

    private static final String WIZYTY = "Wizyty";
    private static final String WIZYTY_ID = "ID";
    private static final String WIZYTY_GODZINA = "Godzina";
    private static final String WIZYTY_DATA = "Data";
    private static final String WIZYTY_IMIE_I_NAZWISKO = "Imie_Nazwisko";
    private static final String WIZYTY_SPECJALIZACJA = "Specjalizacja";
    private static final String WIZYTY_ADRES = "Adres";
    private static final String WIZYTY_PROFIL = "Profil";

    private static final String POMIARY = "Pomiary";
    private static final String POMIARY_ID = "ID";
    private static final String POMIARY_TYP = "Typ";
    private static final String POMIARY_WYNIK = "Wynik";
    private static final String POMIARY_PROFIL = "Profil";
    private static final String POMIARY_GODZINA = "Godzina";
    private static final String POMIARY_DATA = "Data";

    private static final String TYP_POMIAR = "Typ_pomiar";
    private static final String TYP_POMIAR_ID = "ID";
    private static final String TYP_POMIAR_NAZWA = "Typ";

    private static final String NOTATKI = "Notatki";
    private static final String NOTATKI_ID = "ID";
    private static final String NOTATKI_TYTUL = "Tytul";
    private static final String NOTATKI_TRESC = "Tresc";
    private static final String NOTATKI_PROFIL = "Profil";
    private static final String NOTATKI_DATA = "Data";

    private static final String STATYSTYKI = "Statystyki";
    private static final String STATYSTYKI_ID = "ID";
    private static final String STATYSTYKI_WZIETE = "Wziete";
    private static final String STATYSTYKI_NIEWZIETE = "Niewziete";

    private static final String LEK = "Lek";
    private static final String LEK_ID = "ID";
    private static final String LEK_NAZWA = "Nazwa";
    private static final String LEK_ILOSC_TABLETEK = "Ilosc_tabletek";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void removeData(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UZYTKOWNICY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Imie TEXT)");
        db.execSQL("CREATE TABLE " + PRZYPOMNIENIE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Godzina TEXT, Data TEXT, Lek Integer, Dawka TEXT, Ilosc_dni Integer, Profil TEXT, Typ INTEGER, Wszystkie_godziny TEXT)");
        db.execSQL("CREATE TABLE " + NOTYFIKACJA + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ID_notyfikacja INTEGER, ID_przypomnienie INTEGER, Godzina TEXT, Data TEXT)");
        db.execSQL("CREATE TABLE " + DOKTORZY + " (ID INTEGER PRIMARY KEY, Imie_Nazwisko TEXT, Specjalizacja TEXT, Numer TEXT, Adres TEXT)");
        db.execSQL("CREATE TABLE " + WIZYTY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Godzina TEXT, Data TEXT, Imie_Nazwisko TEXT, Specjalizacja TEXT, Adres TEXT, Profil TEXT)");
        db.execSQL("CREATE TABLE " + POMIARY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Typ TEXT, Wynik TEXT, Profil TEXT, Godzina TEXT, Data TEXT)");
        db.execSQL("CREATE TABLE " + TYP_POMIAR + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Typ TEXT)");
        db.execSQL("CREATE TABLE " + NOTATKI + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Tytul TEXT, Tresc TEXT, Profil TEXT, Data TEXT)");
        db.execSQL("CREATE TABLE " + STATYSTYKI + " (ID INTEGER PRIMARY KEY, Wziete INTEGER, Niewziete INTEGER)");
        db.execSQL("CREATE TABLE " + LEK + " (ID INTEGER PRIMARY KEY, Nazwa TEXT, Ilosc_tabletek INTEGER)");
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
        db.execSQL("DROP TABLE IF EXISTS " + LEK);
        onCreate(db);
    }

    /**
     * ============ STATYSTYKI =============
     **/

    public boolean insert_LEK(Integer id, String nazwa, Integer ilosc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LEK_ID, id);
        contentValues.put(LEK_NAZWA, nazwa);
        contentValues.put(LEK_ILOSC_TABLETEK, ilosc);

        long result = db.insert(LEK, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getMAXid_LEK() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT IFNULL(MAX(ID), 0) FROM " + LEK, null);
        return res;
    }

    public Cursor getData_LEK() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * " +
                        "FROM " + LEK
                , null);
        return res;
    }

    public Cursor getDataName_LEK(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * " +
                        "FROM " + LEK + " WHERE Nazwa='" + name + "'"
                , null);
        return res;
    }
    public boolean update_LEK(Integer id, Integer ilosc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LEK_ILOSC_TABLETEK, ilosc);

        long result = db.update(LEK, contentValues, "ID=" + id, null);

        if (result == -1)
            return false;
        else
            return true;
    }

    public void remove_LEK(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LEK, LEK_ID + "=" + id, null);
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
        contentValues.put(NOTYFIKACJA_OSTATNIADATA, data);

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


    public Cursor getID_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ID_notyfikacja FROM " + NOTYFIKACJA + " WHERE " + NOTYFIKACJA_PRZYPOMNIENIE + "=" + id, null);
        return res;
    }

    public Cursor getMAXid_NOTYFIKACJA() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT IFNULL(MAX(ID_notyfikacja), 0) FROM " + NOTYFIKACJA, null);
        return res;
    }

    public boolean updateDate_NOTYFIKACJA(Integer id, String days) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTYFIKACJA_OSTATNIADATA, days);

        long result = db.update(NOTYFIKACJA, contentValues, "ID_notyfikacja=" + id, null);

        if (result == -1)
            return false;
        else
            return true;
    }

    public void remove_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTYFIKACJA, NOTYFIKACJA_ID_NOTYFIKACJA + "=" + id, null);
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

    public Cursor getNotes_NOTATKI(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Tresc FROM " + NOTATKI + " WHERE ID=" + id, null);
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

    public Cursor getDataID_TYP_POMIAR(String typ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ID FROM " + TYP_POMIAR + " WHERE Typ='" + typ + "'", null);
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

    public Cursor getId_UZYTKOWNICY(String imie) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor nam = db.rawQuery("SELECT ID FROM " + UZYTKOWNICY + " WHERE Imie='" + imie + "'", null);
        return nam;
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

    public boolean insert_DOKTORZY(Integer id, String name, String specialization, String phone_number, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DOKTORZY_ID, id);
        contentValues.put(DOKTORZY_IMIE_I_NAZWISKO, name);
        contentValues.put(DOKTORZY_SPECJALIZACJA, specialization);
        contentValues.put(DOKTORZY_NUMER, phone_number);
        contentValues.put(DOKTORZY_ADRES, address);
        long result = db.insert(DOKTORZY, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getMAXid_DOKTORZY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT IFNULL(MAX(ID), -1) FROM " + DOKTORZY, null);
        return res;
    }

    public Cursor getAllData_DOKTORZY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DOKTORZY, null);
        return res;
    }

    public Cursor getIdData_DOKTORZY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DOKTORZY + " WHERE ID=" + id, null);
        return res;
    }

    public Cursor getNameData_DOKTORZY(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DOKTORZY + " WHERE Imie_Nazwisko='" + name + "'", null);
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

    public void remove_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRZYPOMNIENIE, PRZYPOMNIENIE_ID + "=" + id, null);
    }

    public boolean updateDays_PRZYPOMNIENIE(Integer id, Integer days) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

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


    public boolean insert_WIZYTY(String godzina, String data, String dane, String specjalizacja, String adres, String profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WIZYTY_GODZINA, godzina);
        contentValues.put(WIZYTY_DATA, data);
        contentValues.put(WIZYTY_IMIE_I_NAZWISKO, dane);
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

    public Cursor getMAXid_WIZYTY() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT MAX(ID) FROM " + WIZYTY, null);
        return res;
    }

    public void remove_WIZYTY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WIZYTY, WIZYTY_ID + "=" + id, null);
    }


}
