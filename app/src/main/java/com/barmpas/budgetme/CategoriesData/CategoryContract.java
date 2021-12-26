package com.barmpas.budgetme.CategoriesData;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract class for the local SQLite Database class where the user stores the categories.
 * @author Konstantinos Barmpas
 */
public final class CategoryContract {

    /**
     * The app's authority
     */
    public static final String CONTENT_AUTHORITY = "com.barmpas.budgetme.category";
    /**
     * The uri for the provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * The title of the table in the SQL Database
     */
    public static final String PATH_USER = "category";

    /**
     * The constructor of the contract
     */
    private CategoryContract() {}
    /**
     * Connects contract to the SQL Database columns
     */
    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);
        public final static String TABLE_NAME = "categories";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CATEGORY = "category";
    }
}