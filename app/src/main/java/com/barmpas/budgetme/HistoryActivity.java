package com.barmpas.budgetme;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.barmpas.budgetme.TransactionsData.TransactionsContract;

import static com.barmpas.budgetme.MainActivity.currency;

/*
 * The HistoryActivity. Reads from the SQLite Database for the transactions and displays the results using an Adapter.
 * The reading is performed using a cursor.
 */
public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Loader number
     */
    private static final int URL_LOADER = 0;
    /**
     * Simple cursor adapter
     */
    private SimpleCursorAdapter mAdapter;
    /**
     * Shared preferences
     */
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        //Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //The displayed views
        String[] columns = {
                TransactionsContract.UserEntry.COLUMN_AMOUNT,
                TransactionsContract.UserEntry.COLUMN_DATE,
                TransactionsContract.UserEntry.COLUMN_CATEGORY
        };

        int[] views = {
                R.id.amount,
                R.id.date,
                R.id.category
        };

        prefs = this.getSharedPreferences(
                "com.barmpas.budgetme", MODE_PRIVATE);


        //Setting the adapter
        mAdapter = new SimpleCursorAdapter(this, R.layout.history_card, null, columns, views, 0) {

            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                super.bindView(view, context, cursor);

                //Setting each individual view of the adapter.
                TextView amount_txt = (TextView) view.findViewById(R.id.amount);
                TextView date_txt = (TextView) view.findViewById(R.id.date);
                TextView category_txt = (TextView) view.findViewById(R.id.category);
                final String date = cursor.getString(cursor.getColumnIndex("date"));
                final String category = cursor.getString(cursor.getColumnIndex("category"));
                final double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                final int sign_index = cursor.getInt(cursor.getColumnIndex("sign"));

                //Choose color and sign based on the sign
                if (sign_index==1) {
                    date_txt.setText(date);
                    category_txt.setText(category);
                    amount_txt.setText(" + " + amount+" "+currency);
                    amount_txt.setTextColor(Color.parseColor("#66BB6A"));
                }else{
                    date_txt.setText(date);
                    category_txt.setText(category);
                    amount_txt.setText(" - " + amount+" "+currency);
                    amount_txt.setTextColor(Color.parseColor("#B71C1C"));
                }

                //On click pass data to the Graph activity
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };

        //Activating the cursor and the adapter
        ListView favListView = (ListView) findViewById(R.id.saved_recycler_view);
        favListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }


    //Menus to allow delete the SQlite database.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.delete_all_transactions))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getContentResolver().delete(TransactionsContract.UserEntry.CONTENT_URI, null, null);
                                prefs.edit().putFloat("Budget", (float) (0)).apply();
                                prefs.edit().putFloat("Spent", (float) (0)).apply();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //Creating the Cursor
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {

        switch (loaderID) {
            case URL_LOADER:
                String[] projection = {
                        TransactionsContract.UserEntry._ID,
                        TransactionsContract.UserEntry.COLUMN_AMOUNT,
                        TransactionsContract.UserEntry.COLUMN_CATEGORY,
                        TransactionsContract.UserEntry.COLUMN_DATE,
                        TransactionsContract.UserEntry.COLUMN_SIGN
                };
                return new CursorLoader(
                        this,
                        TransactionsContract.UserEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
