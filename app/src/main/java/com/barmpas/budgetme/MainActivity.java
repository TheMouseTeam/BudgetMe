package com.barmpas.budgetme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.barmpas.budgetme.PopupWindows.SharePopup;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;


/**
 * MainActivity is the activity that displays the money left with a circular UI.
 * @author Konstantinos Barmpas
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The percentage of the money used
     */
    private static double percentage;
    /**
     * Money total added
     */
    public static float budget;
    /**
     * Money left
     */
    public static float money;
    /**
     * Chosen currency
     */
    public static String currency;
    /**
     * ints for circular UI
     */
    private int  series1Index,backIndex;
    /**
     * DecoView for circular UI
     */
    private DecoView decoView;
    /**
     * TextView for text display of the percentage
     */
    private TextView textPercentage;
    /**
     * SharedPreferences
     */
    private SharedPreferences prefs;
    /**
     * Money left text view
     */
    private TextView moneyTxt;
    /**
     * Money added total text view
     */
    private TextView moneyInTxt;
    /**
     * Buttons for spend / add from / to the budget
     */
    private at.markushi.ui.CircleButton add_btn, spend_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set the UI
        moneyTxt = (TextView) findViewById(R.id.money);
        moneyInTxt = (TextView) findViewById(R.id.money_in);
        textPercentage = (TextView) findViewById(R.id.textPercentage);
        add_btn = (at.markushi.ui.CircleButton) findViewById(R.id.btn_add);
        spend_btn = (at.markushi.ui.CircleButton) findViewById(R.id.btn_minus);


        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), KeyboardActivity.class);
                intent.putExtra("sign",1);
                startActivity(intent);
            }
        });


        spend_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), KeyboardActivity.class);
                intent.putExtra("sign",0);
                startActivity(intent);
            }
        });

        //Reset
        percentage=0;
        textPercentage.setText("0 %");

        //Shared preferences
        prefs = this.getSharedPreferences(
                "com.barmpas.budgetme", MODE_PRIVATE);
        currency = prefs.getString("Currency","€");
        money = (float) prefs.getFloat("Budget", (float) 0.0)- (float) prefs.getFloat("Spent", (float) 0.0);
        budget = (float) prefs.getFloat("Budget", (float) 0.0);
        Circle();
    }


    @Override
    public void onResume (){
        super.onResume();
        //Reset
        textPercentage.setText("0 %");
        //Shared preferences
        prefs = this.getSharedPreferences(
                "com.barmpas.budgetme", MODE_PRIVATE);
        currency = prefs.getString("Currency","€");
        money = (float) prefs.getFloat("Budget", (float) 0.0)- (float) prefs.getFloat("Spent", (float) 0.0);
        budget = (float) prefs.getFloat("Budget", (float) 0.0);
        Circle();
    }


    //Circular UI Animation
    public void Circle(){
        decoView = (DecoView) findViewById(R.id.dynamicArcView);

        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#1B5E20"))
                .setRange(0, 50, 0)
                .build();

        backIndex = decoView.addSeries(seriesItem1);

        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFFFF"))
                .setRange(0, 50, 0)
                .build();

        series1Index = decoView.addSeries(seriesItem);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {

            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
        if (budget!=0) {
            percentage = (money * 50) / budget;
        }else{
            percentage=0;
        }
        if (percentage<0){
            percentage=0;
        }
        moneyTxt.setText(money +currency);
        moneyInTxt.setText(getResources().getString(R.string.total_money_in)+budget+currency);
        decoView.addEvent(new DecoEvent.Builder(50)
                .setIndex(backIndex)
                .build());
        decoView.addEvent(new DecoEvent.Builder((float) percentage)
                .setIndex(series1Index)
                .setDelay(3000)
                .build());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        return true;
    }


    //Set the selected currency
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==R.id.action_goal){
            new SharePopup.Builder(MainActivity.this)
                    .setContentView(R.layout.layout_bottom_popup).bindClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, R.id.cancel_action_goal).bindClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    prefs.edit().putString("Currency", "€").apply();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, R.id.euro).bindClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    prefs.edit().putString("Currency", "$").apply();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, R.id.dollar).bindClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    prefs.edit().putString("Currency", "£").apply();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, R.id.gbp).bindClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    prefs.edit().putString("Currency", "CHF").apply();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, R.id.swiss).bindClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    prefs.edit().putString("Currency", "¥").apply();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, R.id.china).setGravity(Gravity.BOTTOM).build().show();
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_today) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_previous_days) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_info) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themouseteam.com")));

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
