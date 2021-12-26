package com.barmpas.budgetme;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.barmpas.budgetme.TransactionsData.TransactionsContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import static com.barmpas.budgetme.MainActivity.currency;

/**
 * KeyboardActivity is the activity that the users add the amount of the transaction.
 * @author Konstantinos Barmpas
 */
public class KeyboardActivity extends AppCompatActivity {

    /**
     * TextView for the money
     */
    private TextView moneyTxt;
    /**
     * Custom keyboard buttons
     */
    private TextView txt_1, txt_2,txt_3,txt_4,txt_5,txt_6,txt_7,txt_8,txt_9,txt_0,txt_del,txt_comma;
    /**
     * Boolean if comma used
     */
    private Boolean commaUsed=false;
    /**
     * Money string
     */
    private String money="0";
    /**
     * TextView to add the category
     */
    private TextView categories_txt;
    /**
     * The amount entered
     */
    private Double amount;
    /**
     * Button to add the transaction
     */
    private at.markushi.ui.CircleButton btn;
    /**
     * String for the category
     */
    public static String amount_category="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set the UI
        moneyTxt =(TextView) findViewById(R.id.money);
        moneyTxt.setText(money+currency);
        categories_txt=(TextView)findViewById(R.id.categories);
        btn = (at.markushi.ui.CircleButton) findViewById(R.id.btn_done);

        //Button to Add Category
        categories_txt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeyboardActivity.this,CategoriesActivity.class);
                startActivity(intent);
            }
        });

        //Shared Preferences
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.barmpas.budgetme", MODE_PRIVATE);


        //Get sign for spend or budger
        Intent intent=getIntent();
        final int sign = intent.getIntExtra("sign",1);

        //Set button to log transaction
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (Double.valueOf(money)!=0) {
                    amount = Double.valueOf(money);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());
                    ContentValues values = new ContentValues();
                    values.put(TransactionsContract.UserEntry.COLUMN_AMOUNT, amount);
                    values.put(TransactionsContract.UserEntry.COLUMN_CATEGORY, amount_category);
                    values.put(TransactionsContract.UserEntry.COLUMN_DATE, formattedDate);
                    values.put(TransactionsContract.UserEntry.COLUMN_SIGN, sign);
                    //Add to SQL Database
                    Uri newUri = getContentResolver().insert(TransactionsContract.UserEntry.CONTENT_URI, values);
                    if (sign == 1) {
                        prefs.edit().putFloat("Budget", (float) (prefs.getFloat("Budget", (float) 0.0) + amount)).apply();
                    } else {
                        prefs.edit().putFloat("Spent", (float) (prefs.getFloat("Spent", (float) 0.0) + amount)).apply();
                    }
                }
                finish();
            }
        });

        //Custom keyboard
        txt_1 = (TextView) findViewById(R.id.one1);
        txt_2 = (TextView) findViewById(R.id.two);
        txt_3 = (TextView) findViewById(R.id.three);
        txt_4 = (TextView) findViewById(R.id.four);
        txt_5 = (TextView) findViewById(R.id.five);
        txt_6 = (TextView) findViewById(R.id.six);
        txt_7 = (TextView) findViewById(R.id.seven);
        txt_8 = (TextView) findViewById(R.id.eight);
        txt_9 = (TextView) findViewById(R.id.nine);
        txt_0 = (TextView) findViewById(R.id.zero);
        txt_del = (TextView) findViewById(R.id.del);
        txt_comma = (TextView) findViewById(R.id.comma);

        txt_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="1";
                }else{
                    money=money+"1";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="2";
                }else{
                    money=money+"2";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="3";
                }else{
                    money=money+"3";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="4";
                }else{
                    money=money+"4";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="5";
                }else{
                    money=money+"5";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="6";
                }else{
                    money=money+"6";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="7";
                }else{
                    money=money+"7";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="8";
                }else{
                    money=money+"8";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="9";
                }else{
                    money=money+"9";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_0.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (money.equals("0")){
                    money="0";
                }else{
                    money=money+"0";
                }
                moneyTxt.setText(money+currency);
            }
        });

        txt_del.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                money=remove(money);
                moneyTxt.setText(money + currency);
            }
        });

        txt_comma.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!commaUsed) {
                    money = money + ".";
                    moneyTxt.setText(money + currency);
                    commaUsed=true;
                }
            }
        });

    }

    /**
     * Delete function
     */
    public String remove (String str) {
        if (str != null && str.length() > 1 ) {
            str = str.substring(0, str.length() - 1);
        }else if (str.length()==1){
            str="0";
        }
        if (str.contains(".")){
            commaUsed=true;
        }else{
            commaUsed=false;
        }
        return str;
    }

    /**
     * Delete selected category
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        amount_category = "";
    }

    /**
     * Set selected category
     */
    @Override
    public void onResume(){
        super.onResume();
        if (!amount_category.equals("")){
            categories_txt.setText(getResources().getString(R.string.category)+amount_category);
        }
    }

    /**
     * Enable button back
     */
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
