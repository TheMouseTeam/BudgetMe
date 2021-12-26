package com.barmpas.budgetme.TransactionsData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * The provider for the local SQLite Database class where the user stores transaction.
 * This class allows the interaction between the main activities and the SQLite Database.
 * @author Konstantinos Barmpas
 */
public class TransactionsProvider extends ContentProvider {

    /**
     * The database's helper
     */
    private TransactionsDbHelper mDbHelper;
    /**
     * The database's instance
     */
    static SQLiteDatabase generalDB;
    /**
     * Code for general enquiry
     */
    public static final int USER = 100;
    /**
     * Code for specific enquiry
     */
    public static final int USER_ID = 101;
    /**
     * The Uru matcher of the Database
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    /**
     * The static add methods declarations
     */
    static {
        sUriMatcher.addURI(TransactionsContract.CONTENT_AUTHORITY, TransactionsContract.PATH_USER, USER);
        sUriMatcher.addURI(TransactionsContract.CONTENT_AUTHORITY, TransactionsContract.PATH_USER + "/#", USER_ID);
    }

    /**
     * onCreate method of the provider
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new TransactionsDbHelper(getContext());
        generalDB = mDbHelper.getReadableDatabase();
        return true;
    }

    /**
     * Using cursor traverse the sqlite database
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case USER:
                cursor = database.query(TransactionsContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER_ID:
                selection = TransactionsContract.UserEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TransactionsContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * The Get type from URI
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Using cursor insert a new element to the SQLite Database
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USER:
                Uri newUri = insertNew(uri, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                return newUri;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert new face to the database
     */
    private Uri insertNew (Uri uri, ContentValues values) {
        String  ido = values.getAsString(TransactionsContract.UserEntry.COLUMN_AMOUNT);

        if (ido == null) {
            throw new IllegalArgumentException("invalid");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(TransactionsContract.UserEntry.TABLE_NAME, null, values);
        if (id == -1) {
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Delete the whole SQLite Database
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case USER:
                rowsUpdated = database.delete(TransactionsContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_ID:
                selection = TransactionsContract.UserEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.delete(TransactionsContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not successful for " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /**
     * Update the whole SQLite Database
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.update(TransactionsContract.UserEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        return 0;
    }

    /**
     * Get the date
     */
    public static String getDate(String fieldValue) {
        String date="";
        String Query = "Select date from " + TransactionsContract.UserEntry.TABLE_NAME + " where " + "date" + " = '" + fieldValue + "'";
        Cursor cursor = generalDB.rawQuery(Query, null);
        if(cursor != null && cursor.moveToFirst()) {
            date = cursor.getString(cursor.getColumnIndex("date"));
            cursor.close();
        }
        return date;
    }

}