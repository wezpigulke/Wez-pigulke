package com.wezpigulke;

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
    private static final String UZYTKOWNICY_OBRAZEK = "Obrazek";

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
    private static final String PRZYPOMNIENIE_DZWIEK = "Dzwiek";
    private static final String PRZYPOMNIENIE_WIBRACJA = "Wibracja";

    private static final String NOTYFIKACJA = "Notyfikacja";
    private static final String NOTYFIKACJA_ID = "ID";
    private static final String NOTYFIKACJA_PRZYPOMNIENIE = "ID_przypomnienie";
    private static final String NOTYFIKACJA_GODZINA = "Godzina";
    private static final String NOTYFIKACJA_OSTATNIADATA = "Data";
    private static final String NOTYFIKACJA_RANDID = "Rand_ID";

    private static final String HISTORIA = "Historia";
    private static final String HISTORIA_ID = "ID";
    private static final String HISTORIA_PROFIL = "Profil";
    private static final String HISTORIA_GODZINA = "Godzina";
    private static final String HISTORIA_DATA = "Data";
    private static final String HISTORIA_LEK = "Lek";
    private static final String HISTORIA_DAWKA = "Dawka";
    private static final String HISTORIA_GODZINA_AKCEPTACJI = "Godzina_akceptacji";
    private static final String HISTORIA_STATUS = "Status";

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
    private static final String WIZYTY_PROFIL = "Profil";
    private static final String WIZYTY_RAND = "Rand_ID";
    private static final String WIZYTY_DZWIEK = "Dzwiek";
    private static final String WIZYTY_WIBRACJA = "Wibracja";

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
        db.execSQL("CREATE TABLE " + UZYTKOWNICY + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Imie TEXT, " +
                "Obrazek INTEGER)");

        db.execSQL("CREATE TABLE " + PRZYPOMNIENIE + " (" +
                "ID INTEGER PRIMARY KEY, " +
                "Godzina TEXT, " +
                "Data TEXT, " +
                "Lek TEXT, " +
                "Dawka REAL, " +
                "Ilosc_dni INTEGER, " +
                "Profil TEXT, " +
                "Typ INTEGER, " +
                "Wszystkie_godziny TEXT, " +
                "Dzwiek INTEGER, " +
                "Wibracja INTEGER)");

        db.execSQL("CREATE TABLE " + HISTORIA + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Profil TEXT, " +
                "Godzina TEXT, " +
                "Data TEXT, " +
                "Lek TEXT, " +
                "Dawka REAL, " +
                "Godzina_akceptacji TEXT, " +
                "Status TEXT)");

        db.execSQL("CREATE TABLE " + NOTYFIKACJA + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_przypomnienie INTEGER, " +
                "Godzina TEXT, " +
                "Data TEXT, " +
                "Rand_ID INTEGER, " +
                "FOREIGN KEY(ID_przypomnienie) REFERENCES Przypomnienie(ID))");

        db.execSQL("CREATE TABLE " + DOKTORZY + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Imie_Nazwisko TEXT, " +
                "Specjalizacja TEXT, " +
                "Numer INTEGER, " +
                "Adres TEXT)");

        db.execSQL("CREATE TABLE " + WIZYTY + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Godzina TEXT, " +
                "Data TEXT, " +
                "Imie_Nazwisko TEXT, " +
                "Specjalizacja TEXT, " +
                "Profil TEXT, " +
                "Rand_ID INTEGER, " +
                "Dzwiek INTEGER, " +
                "Wibracja INTEGER)");

        db.execSQL("CREATE TABLE " + POMIARY + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Typ TEXT, " +
                "Wynik REAL, " +
                "Profil TEXT, " +
                "Godzina TEXT, " +
                "Data TEXT)");

        db.execSQL("CREATE TABLE " + TYP_POMIAR + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Typ TEXT)");

        db.execSQL("CREATE TABLE " + NOTATKI + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Tytul TEXT, " +
                "Tresc TEXT, " +
                "Profil TEXT, " +
                "Data TEXT)");

        db.execSQL("CREATE TABLE " + STATYSTYKI + " (" +
                "ID INTEGER PRIMARY KEY, " +
                "Wziete INTEGER, " +
                "Niewziete INTEGER)");

        db.execSQL("CREATE TABLE " + LEK + " (" +
                "ID INTEGER PRIMARY KEY, " +
                "Nazwa TEXT, " +
                "Ilosc_tabletek REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UZYTKOWNICY);
        db.execSQL("DROP TABLE IF EXISTS " + PRZYPOMNIENIE);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORIA);
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
     * ============ HISTORIA =============
     **/

    public void insert_HISTORIA(String profil, String godzina, String data, String lek, Double dawka, String godzinaAkceptacji, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HISTORIA_PROFIL, profil);
        contentValues.put(HISTORIA_GODZINA, godzina);
        contentValues.put(HISTORIA_DATA, data);
        contentValues.put(HISTORIA_LEK, lek);
        contentValues.put(HISTORIA_DAWKA, dawka);
        contentValues.put(HISTORIA_GODZINA_AKCEPTACJI, godzinaAkceptacji);
        contentValues.put(HISTORIA_STATUS, status);

        db.insert(HISTORIA, null, contentValues);
    }

    public Cursor getUserData_HISTORIA(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + HISTORIA + " WHERE Profil='" + user + "'", null);
    }

    public Cursor getMAXid_HISTORIA() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT IFNULL(MAX(ID), 0) FROM " + HISTORIA, null);
    }

    public void update_HISTORIA(Integer id, String godzina, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HISTORIA_GODZINA_AKCEPTACJI, godzina);
        contentValues.put(HISTORIA_STATUS, status);

        db.update(HISTORIA, contentValues, HISTORIA_ID + "=" + id, null);

    }

    /**
     * ============ LEK =============
     **/

    public void insert_LEK(Integer id, String nazwa, String ilosc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LEK_ID, id);
        contentValues.put(LEK_NAZWA, nazwa);
        contentValues.put(LEK_ILOSC_TABLETEK, ilosc);

        db.insert(LEK, null, contentValues);
    }

    public Cursor getMAXid_LEK() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT IFNULL(MAX(ID), 0) FROM " + LEK, null);
    }

    public Cursor getData_LEK() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * " + "FROM " + LEK, null);
    }

    public Cursor getDataNameFromId_LEK(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Nazwa " + "FROM " + LEK + " WHERE ID=" + id, null);
    }

    public Cursor getDataName_LEK(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * " + "FROM " + LEK + " WHERE Nazwa='" + name + "'", null);
    }

    public Cursor getID_LEK(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ID " + "FROM " + LEK + " WHERE Nazwa='" + name + "'", null);
    }

    public Cursor getNumber_LEK(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Ilosc_tabletek " + "FROM " + LEK + " WHERE ID=" + id, null);
    }

    public void update_LEK(Integer id, Double ilosc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LEK_ILOSC_TABLETEK, ilosc);

        db.update(LEK, contentValues, "ID=" + id, null);

    }

    public void remove_LEK(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LEK, LEK_ID + "=" + id, null);
    }

    /**
     * ============ STATYSTYKI =============
     **/

    public void insert_STATYSTYKI(Integer id, Integer wziete, Integer niewziete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STATYSTYKI_ID, id);
        contentValues.put(STATYSTYKI_WZIETE, wziete);
        contentValues.put(STATYSTYKI_NIEWZIETE, niewziete);

        db.insert(STATYSTYKI, null, contentValues);
    }

    public Cursor getAllData_STATYSTYKI() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + STATYSTYKI, null);
    }

    public Cursor getWziete_STATYSTYKI(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Wziete " +
                        "FROM " + STATYSTYKI + " WHERE ID = " + id
                , null);
    }

    public void update_STATYSTYKI_WZIETE(Integer id, Integer wziete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STATYSTYKI_WZIETE, wziete);

        db.update(STATYSTYKI, contentValues, "ID=" + id, null);

    }

    public Cursor getNiewziete_STATYSTYKI(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Niewziete " +
                        "FROM " + STATYSTYKI + " WHERE ID = " + id
                , null);
    }

    public void update_STATYSTYKI_NIEWZIETE(Integer id, Integer niewziete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STATYSTYKI_NIEWZIETE, niewziete);

        db.update(STATYSTYKI, contentValues, "ID=" + id, null);

    }

    /**
     * ============ NOTYFIKACJA ============
     **/

    public void insert_NOTYFIKACJA(Integer przypomnienie, String godzina, String data, Integer rand) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTYFIKACJA_PRZYPOMNIENIE, przypomnienie);
        contentValues.put(NOTYFIKACJA_GODZINA, godzina);
        contentValues.put(NOTYFIKACJA_OSTATNIADATA, data);
        contentValues.put(NOTYFIKACJA_RANDID, rand);

        db.insert(NOTYFIKACJA, null, contentValues);
    }

    public Cursor getAllData_NOTYFIKACJA() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT A.ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil, A.ID_przypomnienie, B.Ilosc_dni, B.Typ, A.Rand_ID, B.Dzwiek, B.Wibracja " +
                        "FROM " + NOTYFIKACJA + " A " +
                        "INNER JOIN " + PRZYPOMNIENIE + " B " +
                        "ON " + "B.ID = A.ID_przypomnienie"
                , null);
    }

    public Cursor getUserData_NOTYFIKACJA(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT A.ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil " +
                "FROM " + NOTYFIKACJA + " A " +
                "INNER JOIN " + PRZYPOMNIENIE + " B " +
                "ON " + "B.ID = A.ID_przypomnienie " +
                "WHERE " + " B.Profil" + "='" + user + "'", null);
    }

    public Cursor getCount_NOTYFIKACJA(Integer id, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT COUNT(ID) " +
                "FROM " + NOTYFIKACJA +
                " WHERE " + "ID_przypomnienie" + "=" + "'" + id + "'" +
                " AND " + "Data" + "=" + "'" + data + "'", null);
    }

    public Cursor getCountType_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT COUNT(ID) " +
                "FROM " + NOTYFIKACJA +
                " WHERE " + "ID_przypomnienie" + "=" + "'" + id + "'", null);
    }

    public Cursor getdataID_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT A.Rand_ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil, " +
                "A.ID, A.ID_przypomnienie, B.Typ, B.Ilosc_dni " +
                "FROM " + NOTYFIKACJA + " A " +
                "INNER JOIN " + PRZYPOMNIENIE + " B " +
                "ON " + "B.ID = A.ID_przypomnienie " +
                "WHERE " + " A.ID" + "=" + id, null);
    }

    public Cursor getID_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ID FROM " + NOTYFIKACJA +
                " WHERE " + NOTYFIKACJA_PRZYPOMNIENIE + "=" + id, null);
    }

    public Cursor getRandId_NOTYFIKACJA(Integer rand) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT COUNT(ID) FROM " + NOTYFIKACJA + " WHERE Rand_ID=" + rand, null);
    }

    public Cursor getMAXid_NOTYFIKACJA() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT IFNULL(MAX(ID), 0) FROM " + NOTYFIKACJA, null);
    }

    public Cursor getRand_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Rand_ID FROM " + NOTYFIKACJA + " WHERE ID=" + id, null);
    }

    public void updateDate_NOTYFIKACJA(Integer id, String days) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTYFIKACJA_OSTATNIADATA, days);

        db.update(NOTYFIKACJA, contentValues, "ID=" + id, null);

    }

    public void remove_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTYFIKACJA, NOTYFIKACJA_ID + "=" + id, null);
    }

    public void removeIdPrz_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTYFIKACJA, NOTYFIKACJA_PRZYPOMNIENIE + "=" + id, null);
    }

    /**
     * ============ NOTATKI ============
     **/

    public void insert_NOTATKI(String tytul, String tekst, String profil, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTATKI_TYTUL, tytul);
        contentValues.put(NOTATKI_TRESC, tekst);
        contentValues.put(NOTATKI_PROFIL, profil);
        contentValues.put(NOTATKI_DATA, data);
        db.insert(NOTATKI, null, contentValues);
    }

    public Cursor getAllData_NOTATKI() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + NOTATKI, null);
    }

    public Cursor getNotes_NOTATKI(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Tresc FROM " + NOTATKI + " WHERE ID=" + id, null);
    }

    public Cursor getUserData_NOTATKI(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + NOTATKI + " WHERE Profil='" + user + "'", null);
    }

    public void removeUser_NOTATKI(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTATKI, NOTATKI_PROFIL + "='" + profil + "'", null);
    }

    public void remove_NOTATKI(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTATKI, NOTATKI_ID + "=" + id, null);
    }

    /**
     * ============ POMIARY ============
     **/

    public void insert_POMIARY(String typ, Double wynik, String profil, String godzina, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POMIARY_TYP, typ);
        contentValues.put(POMIARY_WYNIK, wynik);
        contentValues.put(POMIARY_PROFIL, profil);
        contentValues.put(POMIARY_GODZINA, godzina);
        contentValues.put(POMIARY_DATA, data);
        db.insert(POMIARY, null, contentValues);
    }

    public Cursor getAllData_POMIARY() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + POMIARY, null);
    }

    public Cursor getUserData_POMIARY(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Profil='" + user + "'", null);
    }

    public Cursor getUserTypeData_POMIARY(String user, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Profil='" + user + "' AND Typ='" + type + "'", null);
    }

    public Cursor getUserType_POMIARY(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Typ='" + type + "'", null);
    }

    public void removeUser_POMIARY(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POMIARY, POMIARY_PROFIL + "='" + profil + "'", null);
    }

    public void removeType_POMIARY(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POMIARY, POMIARY_TYP + "='" + type + "'", null);
    }

    public void remove_POMIARY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POMIARY, POMIARY_ID + "=" + id, null);
    }

    /**
     * ============ TYP POMIAR ============
     **/

    public void insert_TYP_POMIAR(String typ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYP_POMIAR_NAZWA, typ);
        db.insert(TYP_POMIAR, null, contentValues);
    }

    public Cursor getAllData_TYP_POMIAR() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TYP_POMIAR, null);
    }

    public Cursor getDataID_TYP_POMIAR(String typ) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ID FROM " + TYP_POMIAR + " WHERE Typ='" + typ + "'", null);
    }

    public Cursor getDataType_TYP_POMIAR(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Typ FROM " + TYP_POMIAR + " WHERE ID=" + id, null);
    }

    public Cursor getPomiar_TYP_POMIAR() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + TYP_POMIAR_NAZWA + " FROM " + TYP_POMIAR, null);
    }

    public void remove_TYP_POMIAR(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TYP_POMIAR, TYP_POMIAR_ID + "=" + id, null);
    }

    /**
     * ============ UZYTKOWNICY ============
     **/

    public void insert_UZYTKOWNICY(String name, Integer obrazek) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UZYTKOWNICY_IMIE, name);
        contentValues.put(UZYTKOWNICY_OBRAZEK, obrazek);
        db.insert(UZYTKOWNICY, null, contentValues);
    }

    public Cursor getAllData_UZYTKOWNICY() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + UZYTKOWNICY, null);
    }

    public Cursor getAllName_UZYTKOWNICY() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Imie FROM " + UZYTKOWNICY, null);
    }

    public Cursor getNameFromID_UZYTKOWNICY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Imie FROM " + UZYTKOWNICY + " WHERE ID=" + id, null);
    }

    public Cursor getId_UZYTKOWNICY(String imie) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ID FROM " + UZYTKOWNICY + " WHERE Imie='" + imie + "'", null);
    }

    public void remove_UZYTKOWNICY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UZYTKOWNICY, UZYTKOWNICY_ID + "=" + id, null);
    }

    /**
     * ============ DOKTORZY ============
     **/

    public void insert_DOKTORZY(String name, String specialization, Integer phone_number, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DOKTORZY_IMIE_I_NAZWISKO, name);
        contentValues.put(DOKTORZY_SPECJALIZACJA, specialization);
        contentValues.put(DOKTORZY_NUMER, phone_number);
        contentValues.put(DOKTORZY_ADRES, address);
        db.insert(DOKTORZY, null, contentValues);
    }

    public Cursor getAllData_DOKTORZY() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + DOKTORZY, null);
    }

    public Cursor getNumer_DOKTORZY(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + DOKTORZY_NUMER + " FROM " + DOKTORZY + " WHERE " + DOKTORZY_ID + "=" + id, null);
    }

    public void remove_DOKTORZY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DOKTORZY, DOKTORZY_ID + "=" + id, null);
    }

    /**
     * ============ PRZYPOMNIENIE ============
     **/

    public void insert_PRZYPOMNIENIE(Integer id, String hour, String date, String medicine, Double dawka, Integer days, String profile, Integer type, String alltime, Integer dzwiek, Boolean wibracja) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PRZYPOMNIENIE_ID, id);
        contentValues.put(PRZYPOMNIENIE_GODZINA, hour);
        contentValues.put(PRZYPOMNIENIE_DATA, date);
        contentValues.put(PRZYPOMNIENIE_LEK, medicine);
        contentValues.put(PRZYPOMNIENIE_DAWKA, dawka);
        contentValues.put(PRZYPOMNIENIE_ILOSC_DNI, days);
        contentValues.put(PRZYPOMNIENIE_PROFIL, profile);
        contentValues.put(PRZYPOMNIENIE_TYP, type);
        contentValues.put(PRZYPOMNIENIE_WSZYSTKIEGODZINY, alltime);
        contentValues.put(PRZYPOMNIENIE_DZWIEK, dzwiek);
        contentValues.put(PRZYPOMNIENIE_WIBRACJA, wibracja);

        db.insert(PRZYPOMNIENIE, null, contentValues);
    }

    public Cursor getAllData_PRZYPOMNIENIE() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + PRZYPOMNIENIE, null);
    }

    public Cursor getAllDataMedicine_PRZYPOMNIENIE(String nazwa) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + PRZYPOMNIENIE + " WHERE Lek='" + nazwa + "'", null);
    }

    public Cursor getIDfromMedicine_PRZYPOMNIENIE(String nazwa) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ID FROM " + PRZYPOMNIENIE + " WHERE Lek='" + nazwa + "'", null);
    }

    public Cursor getType_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Typ FROM " + PRZYPOMNIENIE + " WHERE ID=" + id, null);
    }

    public Cursor getIDforUser_PRZYPOMNIENIE(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ID FROM " + PRZYPOMNIENIE + " WHERE Profil='" + profil + "'", null);
    }

    public Cursor getDays_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Ilosc_dni FROM " + PRZYPOMNIENIE + " WHERE " + PRZYPOMNIENIE_ID + "=" + id, null);
    }

    public Cursor getUserData_PRZYPOMNIENIE(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + PRZYPOMNIENIE + " WHERE Profil='" + user + "'", null);
    }

    public Cursor getMAXid_PRZYPOMNIENIE() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT IFNULL(MAX(ID), -1) FROM " + PRZYPOMNIENIE, null);
    }

    public void remove_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRZYPOMNIENIE, PRZYPOMNIENIE_ID + "=" + id, null);
    }

    public void removeUser_PRZYPOMNIENIE(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRZYPOMNIENIE, PRZYPOMNIENIE_PROFIL + "='" + profil + "'", null);
    }

    public void updateDays_PRZYPOMNIENIE(Integer id, Integer days) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PRZYPOMNIENIE_ILOSC_DNI, days);

        db.update(PRZYPOMNIENIE, contentValues, "ID=" + id, null);

    }

    /**
     * ============ WIZYTY ============
     **/


    public void insert_WIZYTY(String godzina, String data, String dane, String specjalizacja, String profile, Integer rand, Integer dzwiek, Boolean wibracja) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WIZYTY_GODZINA, godzina);
        contentValues.put(WIZYTY_DATA, data);
        contentValues.put(WIZYTY_IMIE_I_NAZWISKO, dane);
        contentValues.put(WIZYTY_SPECJALIZACJA, specjalizacja);
        contentValues.put(WIZYTY_PROFIL, profile);
        contentValues.put(WIZYTY_RAND, rand);
        contentValues.put(WIZYTY_DZWIEK, dzwiek);
        contentValues.put(WIZYTY_WIBRACJA, wibracja);

        db.insert(WIZYTY, null, contentValues);
    }

    public Cursor getAllData_WIZYTY() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + WIZYTY, null);
    }

    public Cursor getUserData_WIZYTY(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + WIZYTY + " WHERE Profil='" + name + "'", null);
    }

    public Cursor getIdForUser_WIZYTY(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ID FROM " + WIZYTY + " WHERE Profil='" + profil + "'", null);
    }

    public Cursor getMaxId_WIZYTY() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT MAX(ID) FROM " + WIZYTY, null);
    }

    public Cursor getRand_WIZYTY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Rand_ID FROM " + WIZYTY + " WHERE ID=" + id, null);
    }

    public void removeUser_WIZYTY(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WIZYTY, WIZYTY_PROFIL + "='" + profil + "'", null);
    }

    public void remove_WIZYTY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WIZYTY, WIZYTY_ID + "=" + id, null);
    }


}
