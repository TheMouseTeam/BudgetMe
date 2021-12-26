package com.barmpas.budgetme.TransactionsData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The helper function class for the local SQLite Database class where the user stores the transaction.
 * Here we define the variables and the acceptable form of the entries in the database.
 * @author Konstantinos Barmpas.
 */
public class TransactionsDbHelper extends SQLiteOpenHelper {

    /**
     * The database's name
     */
    private static final String DATABASE_NAME = "budgetme.db";
    /**
     * The version of the SQL Database
     */
    private static final int DATABASE_VERSION = 1;


    /**
     * The Helper of the SQL Database
     */
    public TransactionsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate method of the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TransactionsContract.UserEntry.TABLE_NAME + " ("
                + TransactionsContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TransactionsContract.UserEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL , "
                + TransactionsContract.UserEntry.COLUMN_DATE + " TEXT NOT NULL , "
                + TransactionsContract.UserEntry.COLUMN_CATEGORY + " TEXT NOT NULL , "
                + TransactionsContract.UserEntry.COLUMN_SIGN + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Upgrade versions of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Still at version 1, no upgrade required
    }
}
