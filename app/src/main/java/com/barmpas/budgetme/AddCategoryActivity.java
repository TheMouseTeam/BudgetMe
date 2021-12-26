package com.barmpas.budgetme;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.barmpas.budgetme.CategoriesData.CategoryContract;

/**
 * AddCategoryActivity is the activity where the user can add a category to the database.
 * @author Konstantinos Barmpas
 */
public class AddCategoryActivity extends AppCompatActivity {

    /**
     * Button to add the category
     */
    private at.markushi.ui.CircleButton add_btn;
    /**
     * EditText for the category
     */
    private EditText category_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set UI
        add_btn = (at.markushi.ui.CircleButton) findViewById(R.id.btn_add);
        category_txt = (EditText) findViewById(R.id.category_type);

        //Add to database
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (category_txt.getText().toString()!=null && !category_txt.getText().toString().equals("")){
                    //Prepare values for SQL Database
                    ContentValues values = new ContentValues();
                    values.put(CategoryContract.CategoryEntry.COLUMN_CATEGORY, category_txt.getText().toString());
                    //Add to SQL Database
                    Uri newUri = getContentResolver().insert(CategoryContract.CategoryEntry.CONTENT_URI, values);
                    finish();
                }else{
                    Toast.makeText(AddCategoryActivity.this,getResources().getString(R.string.type_category),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Enable go back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
