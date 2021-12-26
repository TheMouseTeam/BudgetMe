package com.barmpas.budgetme;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.barmpas.budgetme.CategoriesData.CategoryContract;

import static com.barmpas.budgetme.KeyboardActivity.amount_category;

/**
 * CategoriesActivity is the activity that displays all the stores categories.
 * @author Konstantinos Barmpas
 */
public class CategoriesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Loader number
     */
    private static final int URL_LOADER = 1;
    /**
     * The Cursor adapter
     */
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);

        //Back enable button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //The displayed views
        String[] columns = {
                CategoryContract.CategoryEntry.COLUMN_CATEGORY
        };

        int[] views = {
                R.id.action_saved
        };

        //Setting the adapter
        mAdapter = new SimpleCursorAdapter(this, R.layout.category_card, null, columns, views, 0) {

            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                super.bindView(view, context, cursor);

                //Setting each individual view of the adapter.
                TextView category_txt = (TextView) view.findViewById(R.id.action_saved);
                final String category = cursor.getString(cursor.getColumnIndex("category"));
                category_txt.setText(category);

                //On click select category
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        amount_category = category;
                        finish();
                    }
                });
            }
        };

        //Activating the cursor and the adapter
        ListView favListView = (ListView) findViewById(R.id.saved_recycler_view);
        favListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }



    //Menus to allow delete and add to the SQLite database.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_category:
                Intent intent = new Intent(CategoriesActivity.this,AddCategoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.delete_all:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.delete_all_categories))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getContentResolver().delete(CategoryContract.CategoryEntry.CONTENT_URI, null, null);
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
                        CategoryContract.CategoryEntry._ID,
                        CategoryContract.CategoryEntry.COLUMN_CATEGORY
                };
                return new CursorLoader(
                        this,
                        CategoryContract.CategoryEntry.CONTENT_URI,
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
