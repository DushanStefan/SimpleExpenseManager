package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBhelper extends SQLiteOpenHelper {

    private static final String NAME_OF_DATABASE = "200163E.sqlite";
    private static final int VERSION = 1;


    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_TRANSACTION = "transac";


    public static final String ACCOUNT_NO = "accountNo";


    public static final String NAME_OF_BANK = "bankName";
    public static final String NAME_OF_HOLDER = "accountHolderName";
    public static final String BALANCE = "balance";


    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String TYPE_OF_EXPENSE = "expenseType";
    public static final String AMOUNT = "amount";


    public SQLiteDBhelper(Context context) {
        super(context, NAME_OF_DATABASE, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ACCOUNT + "(" +
                ACCOUNT_NO + " TEXT PRIMARY KEY, " +
                NAME_OF_BANK + " TEXT NOT NULL, " +
                NAME_OF_HOLDER + " TEXT NOT NULL, " +
                BALANCE + " REAL NOT NULL)");



        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TRANSACTION + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " TEXT NOT NULL, " +
                TYPE_OF_EXPENSE + " TEXT NOT NULL, " +
                AMOUNT + " REAL NOT NULL, " +
                ACCOUNT_NO + " TEXT," +
                "FOREIGN KEY (" + ACCOUNT_NO + ") REFERENCES " + TABLE_ACCOUNT + "(" + ACCOUNT_NO + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);


        onCreate(sqLiteDatabase);
    }

}
