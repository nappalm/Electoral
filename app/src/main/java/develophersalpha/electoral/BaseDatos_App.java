package develophersalpha.electoral;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos_App extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "prep2018.db";

    //Datos de entradas ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static final String TABLE_PREP = "prep";
    public static final String COLUMN_IDPRO = "_id";
    public static final String COLUMN_PAN = "pan";
    public static final String COLUMN_PRI = "pri";
    public static final String COLUMN_PRD = "prd";
    public static final String COLUMN_PT = "pt";
    public static final String COLUMN_VERDE = "verde";
    public static final String COLUMN_MC = "mc";
    public static final String COLUMN_NA = "na";
    public static final String COLUMN_MOR = "mor";

    public BaseDatos_App(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PREP + " (" +COLUMN_IDPRO + " INTEGER PRIMARY KEY, " +
                COLUMN_PAN + " TEXT, "+
                COLUMN_PRI + " TEXT, "+
                COLUMN_PRD + " TEXT, "+
                COLUMN_PT + " TEXT, "+
                COLUMN_VERDE + " TEXT, "+
                COLUMN_MC + " TEXT, "+
                COLUMN_NA + " TEXT, "+
                COLUMN_MOR + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i > i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PREP);
        }
    }
}
