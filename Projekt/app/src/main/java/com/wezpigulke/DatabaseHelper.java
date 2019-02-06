package com.wezpigulke;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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

        String sql = "INSERT INTO " + HISTORIA + "(Profil, Godzina, Data, Lek, Dawka, Godzina_akceptacji, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, profil);
        statement.bindString(2, godzina);
        statement.bindString(3, data);
        statement.bindString(4, lek);
        statement.bindDouble(5, dawka);
        statement.bindString(6, godzinaAkceptacji);
        statement.bindString(7, status);
        statement.executeInsert();

    }

    public Cursor getUserData_HISTORIA(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + HISTORIA + " WHERE Profil= ?",  new String[]{user});

    }

    public Cursor getMAXid_HISTORIA() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT IFNULL(MAX(ID), 0) FROM " + HISTORIA, null);
    }

    public void update_HISTORIA(Integer id, String godzina, String status) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE " + HISTORIA + " SET Godzina_akceptacji=?, Status=? WHERE ID=?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, godzina);
        statement.bindString(2, status);
        statement.bindLong(3, id);

        statement.executeUpdateDelete();

    }

    /**
     * ============ LEK =============
     **/

    public void insert_LEK(Integer id, String nazwa, String ilosc) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + LEK + " VALUES (?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, id);
        statement.bindString(2, nazwa);
        statement.bindString(3, ilosc);
        statement.executeInsert();

    }

    public Cursor getMAXid_LEK() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT IFNULL(MAX(ID), 0) FROM " + LEK, null);
    }

    public Cursor getData_LEK() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * " + "FROM " + LEK, null);
    }

    public Cursor getDataNameFromId_LEK(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Nazwa " + "FROM " + LEK + " WHERE ID=?", new String[]{id.toString()});
    }

    public Cursor getDataName_LEK(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * " + "FROM " + LEK + " WHERE Nazwa=?", new String[]{name});
    }

    public Cursor getID_LEK(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT ID " + "FROM " + LEK + " WHERE Nazwa=?", new String[]{name});
    }

    public Cursor getNumber_LEK(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Ilosc_tabletek " + "FROM " + LEK + " WHERE ID=?", new String[]{id.toString()});
    }

    public void update_LEK(Integer id, Double ilosc) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE " + LEK + " SET Ilosc_tabletek=? WHERE ID=?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindDouble(1, ilosc);
        statement.bindLong(2, id);

        statement.executeUpdateDelete();

    }

    public void remove_LEK(Integer id) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + LEK +
                    " WHERE " + LEK_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }

    }

    /**
     * ============ STATYSTYKI =============
     **/

    public void insert_STATYSTYKI(Integer id, Integer wziete, Integer niewziete) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + STATYSTYKI + " VALUES (?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, id);
        statement.bindLong(2, wziete);
        statement.bindLong(3, niewziete);
        statement.executeInsert();
    }

    public Cursor getAllData_STATYSTYKI() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + STATYSTYKI, null);
    }

    public Cursor getWziete_STATYSTYKI(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Wziete " +
                        "FROM " + STATYSTYKI + " WHERE ID = ?", new String[]{id.toString()});
    }

    public void update_STATYSTYKI_WZIETE(Integer id, Integer wziete) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE " + STATYSTYKI + " SET Wziete=? WHERE ID=?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, wziete);
        statement.bindLong(2, id);

        statement.executeUpdateDelete();

    }

    public Cursor getNiewziete_STATYSTYKI(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Niewziete " +
                        "FROM " + STATYSTYKI + " WHERE ID =?"
                , new String[]{id.toString()});
    }

    public void update_STATYSTYKI_NIEWZIETE(Integer id, Integer niewziete) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE " + STATYSTYKI + " SET Niewziete=? WHERE ID=?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, niewziete);
        statement.bindLong(2, id);

        statement.executeUpdateDelete();

    }

    /**
     * ============ NOTYFIKACJA ============
     **/

    public void insert_NOTYFIKACJA(Integer przypomnienie, String godzina, String data, Integer rand) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + NOTYFIKACJA + "(ID_przypomnienie, Godzina, Data, Rand_ID) VALUES (?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, przypomnienie);
        statement.bindString(2, godzina);
        statement.bindString(3, data);
        statement.bindLong(4, rand);
        statement.executeInsert();
    }

    public Cursor getAllData_NOTYFIKACJA() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT A.ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil, A.ID_przypomnienie, B.Ilosc_dni, B.Typ, A.Rand_ID, B.Dzwiek, B.Wibracja " +
                        "FROM " + NOTYFIKACJA + " A " +
                        "INNER JOIN " + PRZYPOMNIENIE + " B " +
                        "ON " + "B.ID = A.ID_przypomnienie"
                , null);
    }

    public Cursor getUserData_NOTYFIKACJA(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT A.ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil " +
                "FROM " + NOTYFIKACJA + " A " +
                "INNER JOIN " + PRZYPOMNIENIE + " B " +
                "ON " + "B.ID = A.ID_przypomnienie " +
                "WHERE " + " B.Profil" + "=?", new String[]{user});
    }

    public Cursor getCount_NOTYFIKACJA(Integer id, String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT COUNT(ID) " +
                "FROM " + NOTYFIKACJA +
                " WHERE ID_przypomnienie=?" +
                " AND Data=?", new String[]{id.toString(), data});
    }

    public Cursor getCountType_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT COUNT(ID) " +
                "FROM " + NOTYFIKACJA +
                " WHERE ID_przypomnienie=?", new String[]{id.toString()});
    }

    public Cursor getdataID_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT A.Rand_ID, B.Lek, B.Dawka, A.Godzina, A.Data, B.Profil, " +
                "A.ID, A.ID_przypomnienie, B.Typ, B.Ilosc_dni " +
                "FROM " + NOTYFIKACJA + " A " +
                "INNER JOIN " + PRZYPOMNIENIE + " B " +
                "ON " + "B.ID = A.ID_przypomnienie " +
                "WHERE A.ID=?", new String[]{id.toString()});
    }

    public Cursor getID_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT ID FROM " + NOTYFIKACJA +
                " WHERE " + NOTYFIKACJA_PRZYPOMNIENIE + "=?", new String[]{id.toString()});
    }

    public Cursor getRandId_NOTYFIKACJA(Integer rand) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT COUNT(ID) FROM " + NOTYFIKACJA + " WHERE Rand_ID=?",new String[]{rand.toString()});
    }

    public Cursor getMAXid_NOTYFIKACJA() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT IFNULL(MAX(ID), 0) FROM " + NOTYFIKACJA, null);
    }

    public Cursor getRand_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Rand_ID FROM " + NOTYFIKACJA + " WHERE ID=?", new String[]{id.toString()});
    }

    public void updateDate_NOTYFIKACJA(Integer id, String days) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE " + NOTYFIKACJA + " SET Data=? WHERE ID=?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, days);
        statement.bindLong(2, id);

        statement.executeUpdateDelete();

    }

    public void remove_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + NOTYFIKACJA +
                    " WHERE " + NOTYFIKACJA_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    public void removeIdPrz_NOTYFIKACJA(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + NOTYFIKACJA +
                    " WHERE " + NOTYFIKACJA_PRZYPOMNIENIE + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * ============ NOTATKI ============
     **/

    public void insert_NOTATKI(String tytul, String tekst, String profil, String data) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + NOTATKI + "(Tytul, Tresc, Profil, Data) VALUES (?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, tytul);
        statement.bindString(2, tekst);
        statement.bindString(3, profil);
        statement.bindString(4, data);
        statement.executeInsert();
    }

    public Cursor getAllData_NOTATKI() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + NOTATKI, null);
    }

    public Cursor getNotes_NOTATKI(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Tresc FROM " + NOTATKI + " WHERE ID=?", new String[]{id.toString()});
    }

    public Cursor getUserData_NOTATKI(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + NOTATKI + " WHERE Profil=?", new String[]{user});
    }

    public void removeUser_NOTATKI(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + NOTATKI +
                    " WHERE " + NOTATKI_PROFIL + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, profil);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    public void remove_NOTATKI(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + NOTATKI +
                    " WHERE " + NOTATKI_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * ============ POMIARY ============
     **/

    public void insert_POMIARY(String typ, Double wynik, String profil, String godzina, String data) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + POMIARY + "(Typ, Wynik, Profil, Godzina, Data) VALUES (?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, typ);
        statement.bindDouble(2, wynik);
        statement.bindString(3, profil);
        statement.bindString(4, godzina);
        statement.bindString(5, data);
        statement.executeInsert();

    }

    public Cursor getAllData_POMIARY() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + POMIARY, null);
    }

    public Cursor getUserData_POMIARY(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Profil=?", new String[]{user});
    }

    public Cursor getUserTypeData_POMIARY(String user, String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Profil=? AND Typ=?", new String[]{user, type});
    }

    public Cursor getUserType_POMIARY(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + POMIARY + " WHERE Typ=?", new String[]{type});
    }

    public void removeUser_POMIARY(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + POMIARY +
                    " WHERE " + POMIARY_PROFIL + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, profil);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    public void removeType_POMIARY(String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + POMIARY +
                    " WHERE " + POMIARY_TYP + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, type);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    public void remove_POMIARY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + POMIARY +
                    " WHERE " + POMIARY_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * ============ TYP POMIAR ============
     **/

    public void insert_TYP_POMIAR(String typ) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + TYP_POMIAR + "(Typ) VALUES (?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, typ);
        statement.executeInsert();
    }

    public Cursor getAllData_TYP_POMIAR() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TYP_POMIAR, null);
    }

    public Cursor getDataID_TYP_POMIAR(String typ) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT ID FROM " + TYP_POMIAR + " WHERE Typ=?", new String[]{typ});
    }

    public Cursor getDataType_TYP_POMIAR(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Typ FROM " + TYP_POMIAR + " WHERE ID=?", new String[]{id.toString()});
    }

    public Cursor getPomiar_TYP_POMIAR() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + TYP_POMIAR_NAZWA + " FROM " + TYP_POMIAR, null);
    }

    public void remove_TYP_POMIAR(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + TYP_POMIAR +
                    " WHERE " + TYP_POMIAR_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * ============ UZYTKOWNICY ============
     **/

    public void insert_UZYTKOWNICY(String name, Integer obrazek) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + UZYTKOWNICY + "(Imie, Obrazek) VALUES (?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindLong(2, obrazek);
        statement.executeInsert();
    }

    public Cursor getAllData_UZYTKOWNICY() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + UZYTKOWNICY, null);
    }

    public Cursor getAllName_UZYTKOWNICY() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Imie FROM " + UZYTKOWNICY, null);
    }

    public Cursor getNameFromID_UZYTKOWNICY(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Imie FROM " + UZYTKOWNICY + " WHERE ID=?", new String[]{id.toString()});
    }

    public Cursor getId_UZYTKOWNICY(String imie) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT ID FROM " + UZYTKOWNICY + " WHERE Imie=?", new String[]{imie});
    }

    public void remove_UZYTKOWNICY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + UZYTKOWNICY +
                    " WHERE " + UZYTKOWNICY_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * ============ DOKTORZY ============
     **/

    public void insert_DOKTORZY(String name, String specialization, Integer phone_number, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + DOKTORZY + "(Imie_Nazwisko, Specjalizacja, Numer, Adres) VALUES (?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, specialization);
        statement.bindLong(3, phone_number);
        statement.bindString(4, address);
        statement.executeInsert();
    }

    public Cursor getAllData_DOKTORZY() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DOKTORZY, null);
    }

    public Cursor getIdData_DOKTORZY(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DOKTORZY + " WHERE " + DOKTORZY_ID + "=?", new String[]{id.toString()});
    }

    public void remove_DOKTORZY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + DOKTORZY +
                    " WHERE " + DOKTORZY_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * ============ PRZYPOMNIENIE ============
     **/

    public void insert_PRZYPOMNIENIE(Integer id, String hour, String date, String medicine, Double dawka, Integer days, String profile, Integer type, String alltime, Integer dzwiek, Integer wibracja) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + PRZYPOMNIENIE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, id);
        statement.bindString(2, hour);
        statement.bindString(3, date);
        statement.bindString(4, medicine);
        statement.bindDouble(5, dawka);
        statement.bindLong(6, days);
        statement.bindString(7, profile);
        statement.bindLong(8, type);
        statement.bindString(9, alltime);
        statement.bindLong(10, dzwiek);
        statement.bindLong(11, wibracja);

        Log.d("OCOCHO", statement.toString());

        statement.executeInsert();
    }

    public Cursor getAllData_PRZYPOMNIENIE() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PRZYPOMNIENIE, null);
    }

    public Cursor getAllDataMedicine_PRZYPOMNIENIE(String nazwa) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PRZYPOMNIENIE + " WHERE Lek=?", new String[]{nazwa});
    }

    public Cursor getIDfromMedicine_PRZYPOMNIENIE(String nazwa) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT ID FROM " + PRZYPOMNIENIE + " WHERE Lek=?", new String[]{nazwa});
    }

    public Cursor getType_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Typ FROM " + PRZYPOMNIENIE + " WHERE ID=?", new String[]{id.toString()});
    }

    public Cursor getIDforUser_PRZYPOMNIENIE(String profil) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT ID FROM " + PRZYPOMNIENIE + " WHERE Profil=?", new String[]{profil});
    }

    public Cursor getDays_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Ilosc_dni FROM " + PRZYPOMNIENIE + " WHERE " + PRZYPOMNIENIE_ID + "=?", new String[]{id.toString()});
    }

    public Cursor getUserData_PRZYPOMNIENIE(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PRZYPOMNIENIE + " WHERE Profil=?", new String[]{user});
    }

    public Cursor getMAXid_PRZYPOMNIENIE() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT IFNULL(MAX(ID), -1) FROM " + PRZYPOMNIENIE, null);
    }

    public void remove_PRZYPOMNIENIE(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + PRZYPOMNIENIE +
                    " WHERE " + PRZYPOMNIENIE_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    public void removeUser_PRZYPOMNIENIE(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + PRZYPOMNIENIE +
                    " WHERE " + PRZYPOMNIENIE_PROFIL + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, profil);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    public void updateDays_PRZYPOMNIENIE(Integer id, Integer days) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE " + PRZYPOMNIENIE + " SET Ilosc_dni=? WHERE ID=?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, days);
        statement.bindLong(2, id);

        statement.executeUpdateDelete();

    }

    /**
     * ============ WIZYTY ============
     **/


    public void insert_WIZYTY(String godzina, String data, String dane, String specjalizacja, String profile, Integer rand, Integer dzwiek, Integer wibracja) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + WIZYTY + "(Godzina, Data, Imie_Nazwisko, Specjalizacja, Profil, Rand_ID, Dzwiek, Wibracja) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, godzina);
        statement.bindString(2, data);
        statement.bindString(3, dane);
        statement.bindString(4, specjalizacja);
        statement.bindString(5, profile);
        statement.bindLong(6, rand);
        statement.bindLong(7, dzwiek);
        statement.bindLong(8, wibracja);
        statement.executeInsert();

    }

    public Cursor getAllData_WIZYTY() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + WIZYTY, null);
    }

    public Cursor getUserData_WIZYTY(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + WIZYTY + " WHERE Profil=?", new String[]{name});
    }

    public Cursor getIdForUser_WIZYTY(String profil) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT ID FROM " + WIZYTY + " WHERE Profil=?", new String[]{profil});
    }

    public Cursor getMaxId_WIZYTY() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT MAX(ID) FROM " + WIZYTY, null);
    }

    public Cursor getRand_WIZYTY(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Rand_ID FROM " + WIZYTY + " WHERE ID=?", new String[]{id.toString()});
    }

    public void removeUser_WIZYTY(String profil) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + WIZYTY +
                    " WHERE " + WIZYTY_PROFIL + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, profil);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

    public void remove_WIZYTY(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + WIZYTY +
                    " WHERE " + WIZYTY_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }

}
