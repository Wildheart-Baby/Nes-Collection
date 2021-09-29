package uk.co.pixoveeware.nes_collection;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Statistics extends AppCompatActivity implements PieChartView.Callback, SecondPieChartView.Callback,
        NavigationView.OnNavigationItemSelectedListener  {

    String[] palanames;
    String[] palbnames;
    String[] usnames;
    String[] gamenames;
    String[] names, names2;
    String popgenre;
    float[] datapoints, datapoints2;
    int[] piecolours, piecolours2;
    int completeinbox;
    SQLiteDatabase db;//sets up the connection to the database
    RelativeLayout RlPie2;

    String name, dbfile, sql, licensed, PalA, PalB, US, s, gamecost, wherestatement, palaadd, palbadd, usadd, palanames2, palbnames2, usnames2, currency,poppublisher, perpalacoll, perpalbcoll, peruscoll, gamename, gamescost, gameorgames, regionselected, loosecarts, looseboxes, looseman, looseitems, collectionpercentagestr, avgCst;
    int totalOwned, totalReleased, totalPalA, totalPalB, totalUS, ownedPalA, ownedPalB, ownedUS, io,cost, totalCost, percentPalANeeded, percentPalBNeeded, percentUSNeeded, i, showprices, numberowned, palaMaxCost, palbMaxCost, usMaxCost,totalfinished, collectiongames, collectionreleased, loosecart, loosemanual, loosebox;
    int ownedPalAbox, ownedPalBbox, ownedUSbox, ownedPalAman, ownedPalBman, ownedUSman, boxed, totalOwnedGames;
    //int ownedaction, ownedadventure, ownedbeatemup, ownedactionadventure, ownedarcade, ownedboardgame, ownedcompilation, ownedfighting, ownedother, ownedplatformer, ownedpuzzle, ownedracing, ownedroleplayinggame, ownedshootemup, ownedshooter, ownedsimulation, ownedsports, ownedstrategy, ownedtraditional, ownedtrivia;
    double percentPalAOwned, percentPalBOwned, percentUSOwned, percentagepalacollection, percentagepalbcollection, percentageuscollection, collectionpercentage, cibpercentage, boxedpercentage;
    float palacost, palbcost, uscost, totalpalacost, totalpalbcost, totaluscost, totalcost, avgCost, percentageOwned;
    int lay = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database

        //("Statistics");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RlPie2 = (RelativeLayout) findViewById(R.id.rlPieChart2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        run();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView PalALabel = (TextView) findViewById(R.id.lblPalATitle);
        TextView PalBLabel = (TextView) findViewById(R.id.lblPalBTitle);
        TextView USLabel = (TextView) findViewById(R.id.lblUSTitle);
        TextView CompleteLabel = (TextView) findViewById(R.id.lblCompleteTitle);

        PalALabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql = "SELECT * FROM eu where owned = 1 and (pal_a_cart = 8783 " + licensed + ")";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(Statistics.this, StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Pal A games");
                startActivity(intent);//start the new screen
            }
        });

        PalBLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql = "SELECT * FROM eu where owned = 1 and (pal_b_cart = 8783 " + licensed + ")";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(Statistics.this, StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Pal B games");
                startActivity(intent);//start the new screen
            }
        });

        USLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql = "SELECT * FROM eu where owned = 1 and (ntsc_cart = 8783 " + licensed + ")";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(Statistics.this, StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "US games");
                startActivity(intent);//start the new screen
            }
        });

       /* CompleteLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*sql = "SELECT * FROM eu where owned = 1 and (cart = 1 and manual = 1 and box = 1 " + licensed + ")";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(Statistics.this, StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Complete in box games");*/
               /* getIntent().putExtra("games", completeinbox);
                Log.d("pixo", "games " + completeinbox);
                Intent intent = new Intent(Statistics.this, TrophyRoom.class);
                startActivity(intent);//start the new screen
            }
        });*/

        /*MobileAds.initialize(getApplicationContext(), "ca-app-pub-0537596348696744~2585816192");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void run(){
        gameregion();
        readDatabase();
        piechart();
        piechart2();

    }

    @Override
    public void onDrawFinised(DataColorSet[] data) {
        // When the chart has finished drawing it will return the colors used
        // and the value along (for our key)

        LinearLayout keyContainer = (LinearLayout) findViewById(R.id.key);
        if (keyContainer.getChildCount() > 0)
            keyContainer.removeAllViews(); // Empty all views if any found

        for (int i = 0; i < data.length; i++) {
            DataColorSet dataColorSet = data[i];

            LinearLayout keyItem = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.key_item, null);
            LinearLayout colorView = (LinearLayout) keyItem.findViewById(R.id.color);
            TextView name = (TextView) keyItem.findViewById(R.id.names);
            TextView valueView = (TextView) keyItem.findViewById(R.id.value);

            colorView.setBackgroundColor(Color.parseColor("#" + dataColorSet.getColor()));
            name.setText("" + dataColorSet.getName() + "  ");
            String pievalue = String.format("%.0f",dataColorSet.getDataValue());
            valueView.setText(pievalue);

            // Add the key to the container
            keyContainer.addView(keyItem, i);
        }

    }

    @Override
    public void onSliceClick(DataColorSet data) {
        gamename = data.getName();
        sql = "select * from eu where owned = 1 and genre = '" + gamename + "';";
        //Log.d("Pixo", sql);
        Intent intent = new Intent(this, StatsSearchResults.class);//opens a new screen when the shopping list is clicked
        //intent.putExtra("columnname", fieldname);//passes the table name to the new screen
        //intent.putExtra("search", searchterm);//passes the table name to the new screen
        intent.putExtra("sqlstatement", sql);
        intent.putExtra("pagetitle", "" + gamename + " games");
        startActivity(intent);//start the new screen
    }

    @Override
    public void onSliceClick2(DataColorSet data) {
        gamename = data.getName();
        Log.d("pixo-stats", "Region: " + gamename);;
        switch(gamename){
            case "Pal A games":
                sql = "select * from eu where owned = 1 and pal_a_cart = 8783";
                break;
            case "Pal B games":
                sql = "select * from eu where owned = 1 and pal_b_cart = 8783";
                break;
            case "US games":
                sql = "select * from eu where owned = 1 and ntsc_cart = 8783";
                break;
        }

        Intent intent = new Intent(this, StatsSearchResults.class);//opens a new screen when the shopping list is clicked
        //intent.putExtra("columnname", fieldname);//passes the table name to the new screen
        //intent.putExtra("search", searchterm);//passes the table name to the new screen
        intent.putExtra("sqlstatement", sql);
        intent.putExtra("pagetitle", "" + gamename);
        startActivity(intent);//start the new screen
    }

    @Override
    public void onDrawFinised2(DataColorSet[] data) {
        // When the chart has finished drawing it will return the colors used
        // and the value along (for our key)

        LinearLayout keyContainer = (LinearLayout) findViewById(R.id.key2);
        if (keyContainer.getChildCount() > 0)
            keyContainer.removeAllViews(); // Empty all views if any found



        for (int i = 0; i < data.length; i++) {
            DataColorSet dataColorSet = data[i];

            LinearLayout keyItem = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.key_item, null);
            LinearLayout colorView = (LinearLayout) keyItem.findViewById(R.id.color);
            TextView name = (TextView) keyItem.findViewById(R.id.names);
            TextView valueView = (TextView) keyItem.findViewById(R.id.value);



            colorView.setBackgroundColor(Color.parseColor("#" + dataColorSet.getColor()));
            name.setText("" + dataColorSet.getName() + "  ");
            percentageOwned = dataColorSet.getDataValue() / totalOwnedGames * 100;
            //String pievalue = String.format("%.0f",dataColorSet.getDataValue());
            String pievalue = String.format("%.0f",percentageOwned);
            valueView.setText(pievalue+"%");

            // Add the key to the container
            keyContainer.addView(keyItem, i);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_allgames, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(Statistics.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(Statistics.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(Statistics.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void readDatabase() {
        TextView pala = (TextView) findViewById(R.id.lblPalA);
        TextView palb = (TextView) findViewById(R.id.lblPalB);
        TextView us = (TextView) findViewById(R.id.lblUS);
        TextView cost = (TextView) findViewById(R.id.lblCost);
        TextView cib = (TextView) findViewById(R.id.lblCompleteInfo);
        TextView boxedgames = (TextView) findViewById(R.id.lblBoxedInfo);
        TextView header = (TextView) findViewById(R.id.lblHeader);
        TextView loose = (TextView) findViewById(R.id.lblLooseInfo);
        RelativeLayout RlPie = (RelativeLayout) findViewById(R.id.rlPieChart);
        RelativeLayout RlAll = (RelativeLayout) findViewById(R.id.rlAll);
        RelativeLayout RlPalA = (RelativeLayout) findViewById(R.id.rlPalA);
        RelativeLayout RlPalB = (RelativeLayout) findViewById(R.id.rlPalB);
        RelativeLayout RlUS = (RelativeLayout) findViewById(R.id.rlUS);
        RelativeLayout RlCib = (RelativeLayout) findViewById(R.id.rlComplete);
        TextView Div1 = (TextView) findViewById(R.id.lblDivider);
        TextView Div2 = (TextView) findViewById(R.id.lblDivider2);
        TextView Div3 = (TextView) findViewById(R.id.lblDivider3);
        TextView Div4 = (TextView) findViewById(R.id.lblDivider4);


        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database

        sql = "select * from eu where owned = 1";
        Cursor c = db.rawQuery(sql, null);
        numberowned = c.getCount();

        if (numberowned == 0) {
            header.setText(getString(R.string.statsNogames));
            RlPie.setVisibility(View.INVISIBLE);
            RlAll.setVisibility(View.INVISIBLE);
            RlAll.setVisibility(View.INVISIBLE);
            RlPalA.setVisibility(View.INVISIBLE);
            RlPalB.setVisibility(View.INVISIBLE);
            RlUS.setVisibility(View.INVISIBLE);
            RlCib.setVisibility(View.INVISIBLE);
            Div1.setVisibility(View.INVISIBLE);
            Div2.setVisibility(View.INVISIBLE);
            Div3.setVisibility(View.INVISIBLE);
            Div4.setVisibility(View.INVISIBLE);
        } else if (numberowned >0) {

            sql = "SELECT SUM(pal_a_cost) FROM eu ";
            c = db.rawQuery(sql, null);//select everything from the database table
            c.moveToFirst();
            palacost = c.getFloat(0);
            c.close();

            sql = "SELECT SUM(pal_b_cost) FROM eu ";
            c = db.rawQuery(sql, null);//select everything from the database table
            c.moveToFirst();
            palbcost = c.getFloat(0);
            c.close();

            sql = "SELECT SUM(ntsc_cost) FROM eu ";
            c = db.rawQuery(sql, null);//select everything from the database table
            c.moveToFirst();
            uscost = c.getFloat(0);
            c.close();

            totalcost = palacost + palbcost + uscost;

            sql = "Select finished_game from eu where finished_game = 1";
            c = db.rawQuery(sql, null);
            totalfinished = c.getCount();

            sql = "SELECT * FROM eu where pal_a_release = 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalPalA = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (pal_a_cart = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedPalA = c.getCount();

            sql = "SELECT * FROM eu where pal_b_release = 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalPalB = c.getCount();
            sql = "SELECT * FROM eu where owned = 1 and (pal_b_cart = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedPalB = c.getCount();

            sql = "SELECT * FROM eu where ntsc_release = 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalUS = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (ntsc_cart = 8783 " + licensed + ")";
            c = db.rawQuery(sql, null);
            //Log.d("Pixo", sql);
            ownedUS = c.getCount();

            sql = "SELECT * FROM eu where 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalReleased = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalOwned = c.getCount();

            sql = "select name from eu where pal_a_owned = 1 and pal_a_cost >0 and pal_a_cost=(select max(pal_a_cost) from eu) limit 1";
            c = db.rawQuery(sql, null);
            i = c.getCount();
            //Log.d("pixo", "value:" + i);
            if (i > 1) {
                palaadd = getString(R.string.statsMostexpensivegames);
            } else {
                palaadd = getString(R.string.statsMostexpensivegame);
            }
            if (ownedPalA == 0) {
                RlPalA.setVisibility(View.GONE);
                Div2.setVisibility(View.GONE);
            }
            palanames = new String[i];
            if (c.moveToFirst()) {//move to the first record
                io = 0;
                while (!c.isAfterLast()) {//while there are records to read
                    palanames[io] = c.getString(c.getColumnIndex("name")) + " ";
                    c.moveToNext();
                    io++;
                }
                palanames2 = Arrays.toString(palanames);
                palanames2 = palanames2.substring(1, palanames2.length() - 1);
                Log.d("Pixo", "Pal A: " + palanames2);
                //i = 0;
            }
            sql = "select name from eu where pal_b_owned = 1 and pal_b_cost >0 and pal_b_cost=(select max(pal_b_cost) from eu) limit 1";
            c = db.rawQuery(sql, null);
            i = c.getCount();

            if (i > 1) {
                palbadd = getString(R.string.statsMostexpensivegames);
            } else {
                palbadd = getString(R.string.statsMostexpensivegame);
            }
            if (ownedPalB == 0) {
                RlPalB.setVisibility(View.GONE);
                //Div3.setVisibility(View.GONE);
            }
            palbnames = new String[i];
            if (c.moveToFirst()) {//move to the first record
                io = 0;
                while (!c.isAfterLast()) {//while there are records to read
                    palbnames[io] = c.getString(c.getColumnIndex("name")) + " ";
                    c.moveToNext();
                    io++;
                }
                palbnames2 = Arrays.toString(palbnames);
                palbnames2 = palbnames2.substring(1, palbnames2.length() - 1);
                Log.d("Pixo", "Pal B: " + palbnames2);
            }

            sql = "select name from eu where ntsc_owned = 1 and ntsc_cost >0 and ntsc_cost=(select max(ntsc_cost) from eu) limit 1";
            c = db.rawQuery(sql, null);
            i = c.getCount();

            if (i > 1) {
                usadd = getString(R.string.statsMostexpensivegames);
            } else {
                usadd = getString(R.string.statsMostexpensivegame);
            }
            if (ownedUS == 0) {
                RlUS.setVisibility(View.GONE);
                //Div3.setVisibility(View.GONE);
            }
            usnames = new String[i];
            if (c.moveToFirst()) {//move to the first record
                io = 0;
                while (!c.isAfterLast()) {//while there are records to read
                    usnames[io] = c.getString(c.getColumnIndex("name")) + " ";
                    c.moveToNext();
                    io++;
                }
                usnames2 = Arrays.toString(usnames);
                usnames2 = usnames2.substring(1, usnames2.length() - 1);
                Log.d("Pixo", "US: " + usnames2);

            }

            sql = "select genre, COUNT(genre) AS MOST_FREQUENT from eu where owned = 1 GROUP BY genre ORDER BY COUNT(genre) DESC limit 1";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            popgenre = c.getString(c.getColumnIndex("genre"));

            sql = "select publisher, COUNT(publisher) AS MOST_FREQUENT from eu where owned = 1 GROUP BY genre ORDER BY COUNT(publisher) DESC limit 1";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            poppublisher = c.getString(c.getColumnIndex("publisher"));

            i = 0;

            sql = "select * from eu where (owned = 1 and cart = 1 and box = 1 and manual = 1)";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            completeinbox = c.getCount();

            sql = "select * from eu where (owned = 1 and cart = 1 and box = 1)";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            boxed = c.getCount();
            boxed = boxed - completeinbox;
            //c.close();
            //db.close();//close the database
            totalOwned = ownedPalA + ownedPalB + ownedUS;
            Log.d("pixo-stats", "Total owned " + totalOwned + " Total counted " + numberowned);
            DecimalFormat decimalFormat = new DecimalFormat(getString(R.string.statsDecimalFormat));


            if (showprices == 1) {
                if (totalcost > 0) {

                    avgCost = Float.valueOf(totalcost / totalOwned);
                    //String avgCst = String.valueOf(avgCost);
                    avgCst = String.format("%.2f", avgCost);
                    //avgCst = avgCst.replace(",",".");
                    Log.d("pixo-stats", "avg cost: " + avgCst);
                    gamescost = getString(R.string.statsGamescost1) + " " + currency + String.format("%.2f", totalcost)  + " " + getString(R.string.statsGamescost2) + " " + currency + avgCst + "\n";
                } else if (totalcost == 0) {
                    gamescost = "";
                }
            } else if (showprices == 0) {
                gamescost = "";
            }

            if (totalOwned > 1) {
                gameorgames = getString(R.string.statsGameorgames1);
            } else {
                gameorgames = getString(R.string.statsGameorgames2);
            }
            //gamecost = "You have spent " + currency + totalcost + " on games for your collection\n" +

            switch (wherestatement) {
                case "(pal_a_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 327;
                    } else {
                        collectionreleased = 361;
                    }
                    sql = "select * from eu where (owned = 1 and pal_a_release = 1)";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = getString(R.string.regionsA);

                    break;
                 case "(pal_uk_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 246;
                    } else {
                        collectionreleased = 255;
                    }
                    sql = "select * from eu where (owned = 1 and pal_uk_release = 1)";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = getString(R.string.regionsA2);
                     break;
                case "(pal_b_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 242;
                    } else {
                        collectionreleased = 268;
                    }
                    sql = "select * from eu where (owned = 1 and pal_b_release = 1)";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = getString(R.string.regionsB);
                    break;
                case "(ntsc_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 673;
                    } else {
                        collectionreleased = 760;
                    }
                    sql = "select * from eu where (owned = 1 and ntsc_release = 1)";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = getString(R.string.regionsUS);
                break;
                case "(pal_a_release = 1 or pal_b_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 350;
                    } else {
                        collectionreleased = 384;
                    }
                    sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or pal_b_release = 1))";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = "Pal A & Pal B";
                    break;
                case "(pal_a_release = 1 or ntsc_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 717;
                    } else {
                        collectionreleased = 818;
                    }
                    sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or ntsc_release = 1))";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = "Pal A & Ntsc";
                    break;
                case "(pal_b_release = 1 or ntsc_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 723;
                    } else {
                        collectionreleased = 822;
                    }
                    sql = "select * from eu where (owned = 1 and (pal_b_release = 1 or ntsc_release = 1))";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = "Pal B & Ntsc";
                    break;
                case "(pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 725;
                    } else {
                        collectionreleased = 826;
                    }
                    Log.d("pixo-stats", "all regions count hit");
                    sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1))";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    Log.d("pixo-stats", "Count % " + collectiongames);
                    regionselected = getString(R.string.regionsAll);
                    break;
                }


            /*if (wherestatement.equals("(pal_a_release = 1)")) {
                if (licensed.equals(" and (unlicensed = 0)")) {
                    collectionreleased = 327;
                } else {
                    collectionreleased = 361;
                }
                sql = "select * from eu where (owned = 1 and pal_a_release = 1)";
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                collectiongames = c.getCount();

                regionselected = getString(R.string.regionsA);
            } else if (wherestatement.equals("(pal_uk_release = 1)")) {
                if (licensed.equals(" and (unlicensed = 0)")) {
                    collectionreleased = 246;
                } else {
                    collectionreleased = 255;
                }
                sql = "select * from eu where (owned = 1 and pal_uk_release = 1)";
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                collectiongames = c.getCount();
                regionselected = getString(R.string.regionsA2);
            }else if (wherestatement.equals("(pal_b_release = 1)")) {
                if (licensed.equals(" and (unlicensed = 0)")) {
                    collectionreleased = 242;
                } else {
                    collectionreleased = 268;
                }
                sql = "select * from eu where (owned = 1 and pal_b_release = 1)";
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                collectiongames = c.getCount();
                regionselected = getString(R.string.regionsB);
            }else if (wherestatement.equals("(ntsc_release = 1)")) {
                if (licensed.equals(" and (unlicensed = 0)")) {
                    collectionreleased = 673;
                } else {
                    collectionreleased = 760;
                }
                sql = "select * from eu where (owned = 1 and ntsc_release = 1)";
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                collectiongames = c.getCount();
                regionselected = getString(R.string.regionsUS);
            }else if (wherestatement.equals("(pal_a_release = 1 or pal_b_release = 1)")) {
                if (licensed.equals(" and (unlicensed = 0)")) {
                    collectionreleased = 350;
                } else {
                    collectionreleased = 384;
                }
                sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or pal_b_release = 1))";
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                collectiongames = c.getCount();
                regionselected = "Pal A & Pal B";
            }
            else if (wherestatement.equals("(pal_a_release = 1 or ntsc_release = 1)")) {
                if (licensed.equals(" and (unlicensed = 0)")) {
                    collectionreleased = 717;
                } else {
                    collectionreleased = 818;
                }
                sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or ntsc_release = 1))";
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                collectiongames = c.getCount();
                regionselected = "Pal A & Ntsc";
            }else if (wherestatement.equals("(pal_b_release = 1 or ntsc_release = 1)")) {
                if (licensed.equals(" and (unlicensed = 0)")) {
                    collectionreleased = 723;
                } else {
                    collectionreleased = 822;
                }
                sql = "select * from eu where (owned = 1 and (pal_b_release = 1 or ntsc_release = 1))";
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                collectiongames = c.getCount();
                regionselected = "Pal B & Ntsc";
            }else if (wherestatement.equals("(pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1)")) {
                if (licensed.equals(" and (unlicensed = 0)")) {
                    collectionreleased = 725;
                } else {
                    collectionreleased = 826;
                }
                Log.d("pixo-stats", "all regions count hit");
                sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1))";
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                collectiongames = c.getCount();
                Log.d("pixo-stats", "Count % " + collectiongames);
                regionselected = getString(R.string.regionsAll);
            }*/

            //avgCost = Float.valueOf(decimalFormat.format(totalcost / totalOwned));
            Log.d("pixo-stats", "collection games: " + collectiongames + " collection released: " + collectionreleased);
            collectionpercentage  = ((double) collectiongames / collectionreleased) * 100;

            //Float.format("%.2f", totalcost);

            collectionpercentagestr = String.format("%.2f", collectionpercentage);
            Log.d("pixo-stats", "Games " + collectiongames + " released " + collectionreleased);
            Log.d("pixo-stats", "Collection % " + collectionpercentage);
            Log.d("pixo-stats", "Region: " + wherestatement);
            gamecost = gamescost +

                    getString(R.string.statsGamescost3) + " " + totalOwned + " " + gameorgames +
                    getString(R.string.statsGamescost4) + " " + collectionpercentagestr +
                    getString(R.string.statsGamescost5) + " "+ regionselected +
                    getString(R.string.statsGamescost6) + " " + poppublisher +
                    getString(R.string.statsGamescost7) + " " + popgenre +
                    getString(R.string.statsGamescost8) + " " + totalfinished + " " + getString(R.string.statsGamescost9);

            cost.setText(gamecost);

            if (showprices == 1) {
                gamescost = "\n" + palaadd + " " + palanames2;
            } else {
                gamescost = "";
            }
            if (palacost == 0) {
                gamescost = "";
            }
            percentPalAOwned = ((double) ownedPalA / totalPalA) * 100;
            percentagepalacollection = ((double) ownedPalA / totalOwned) * 100;
            s = String.format("%.2f", percentPalAOwned);
            perpalacoll = String.format("%.2f", percentagepalacollection);
            PalA = getString(R.string.statsPala1) + " " + ownedPalA + " " + getString(R.string.statsPala2) + " " + totalPalA + " " + getString(R.string.statsPala3) + " " + perpalacoll + getString(R.string.statsPala4)
                    + gamescost;
            pala.setText(PalA);
            //Log.d("Pixo",PalA);

            if (showprices == 1) {
                gamescost = "\n" + palbadd + " " + palbnames2;
            } else {
                gamescost = "";
            }
            if (palbcost == 0) {
                gamescost = "";
            }
            percentPalBOwned = ((double) ownedPalB / totalPalB) * 100;
            percentagepalbcollection = ((double) ownedPalB / totalOwned) * 100;
            s = String.format("%.2f", percentPalBOwned);
            perpalbcoll = String.format("%.2f", percentagepalbcollection);
            PalB = getString(R.string.statsPala1) + " " + ownedPalB + " " + getString(R.string.statsPala2) + " " + totalPalB + " " + getString(R.string.statsPalb3) + " " + perpalbcoll + getString(R.string.statsPala4)
            + gamescost;
            palb.setText(PalB);
            //Log.d("Pixo",PalB);

            if (showprices == 1) {
                gamescost = "\n" + usadd + " " + usnames2;
            } else {
                gamescost = "";
            }
            if (uscost == 0) {
                gamescost = "";
            }
            percentUSOwned = ((double) ownedUS / totalUS) * 100;
            //Log.d("Pixo", "PalB% owned:" + percentUSOwned);
            s = String.format("%.2f", percentUSOwned);
            percentageuscollection = ((double) ownedUS / totalOwned) * 100;
            peruscoll = String.format("%.2f", percentageuscollection);
            US = getString(R.string.statsPala1) + " " + ownedUS + " " + getString(R.string.statsPala2) + " " + totalUS + " " + getString(R.string.statsUS3) + " " + peruscoll + getString(R.string.statsPala4)
            + gamescost;
            us.setText(US);
            cibpercentage = ((double)completeinbox / totalOwned * 100);
            String CIB = "";
            if (completeinbox == 1) {
                CIB = getString(R.string.statsCib1) + " " + completeinbox + " " +
                        getString(R.string.statsCib2a) +  getString(R.string.statsCib3a) + " " + String.format("%.2f", cibpercentage) + getString(R.string.statsCib4);;
            } else if (completeinbox > 1) {
                CIB = getString(R.string.statsCib1) + " " + completeinbox + " " +
                        getString(R.string.statsCib2) +  getString(R.string.statsCib3) + " " + String.format("%.2f", cibpercentage) + getString(R.string.statsCib4);
            } else {
                CIB = getString(R.string.statsCib1) + " " + completeinbox + " " +
                        getString(R.string.statsCib2) +  getString(R.string.statsCib3) + " " + String.format("%.2f", cibpercentage) + getString(R.string.statsCib4);
            }
            cib.setText(CIB);

            String Boxed = "";
            boxedpercentage = ((double)boxed / totalOwned * 100);
            Log.d("pixo-stats", "boxed games: " + boxed + " boxed percentage: " + boxedpercentage);
            if (boxed == 1){
                Boxed = getString(R.string.statsBoxed1) + " "  + boxed + " " + getString(R.string.statsBoxed2a) +
                        getString(R.string.statsBoxed3a) + " " + String.format("%.2f", boxedpercentage) + getString(R.string.statsBoxed4);
            } else if (boxed > 1) {
                Boxed = getString(R.string.statsBoxed1) + " "  + boxed + " " + getString(R.string.statsBoxed2) +
                        getString(R.string.statsBoxed3) + " " + String.format("%.2f", boxedpercentage) + getString(R.string.statsBoxed4);
            }  else {
                Boxed = getString(R.string.statsBoxed1) + " "  + boxed + " " + getString(R.string.statsBoxed2) +
                        getString(R.string.statsBoxed3) + " " + String.format("%.2f", boxedpercentage) + getString(R.string.statsBoxed4);
            }
            boxedgames.setText(Boxed);

            //Log.d("Pixo",US);

            //.d("Pixo", "Owned PalA: " + ownedPalA);
            //Log.d("Pixo", "Owned PalB: " + ownedPalB);
            //Log.d("Pixo", "Owned US: " + ownedUS);

            sql = "SELECT * FROM eu where owned = 1 and (pal_a_box = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedPalAbox = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (pal_b_box = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedPalBbox = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (ntsc_box = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedUSbox = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (pal_a_manual = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedPalAman = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (pal_b_manual = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedPalBman = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (ntsc_manual = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedUSman = c.getCount();

            loosecart = ownedPalA + ownedPalB + ownedUS;
            loosecart = (loosecart - completeinbox) - boxed;
            Log.d("pixo-stats", "Carts: " + loosecart);

            loosebox = ownedPalAbox + ownedPalBbox + ownedUSbox;
            loosebox = (loosebox - completeinbox) - boxed;
            Log.d("pixo-stats", "Boxes: " + loosebox);


            loosemanual = ownedPalAman + ownedPalBman + ownedUSman;
            loosemanual = loosemanual - completeinbox;

            Log.d("pixo-stats", "Manuals: " + loosemanual);
            if (loosecart == 1)
                {loosecarts = getString(R.string.statsLC1) + " "  + loosecart +  " " + getString(R.string.statsLC2a);}
            else if (loosecart > 1)
                {loosecarts = getString(R.string.statsLC1) + " "  + loosecart + " " + getString(R.string.statsLC2);}
            else {loosecarts = "";}

            if (loosebox == 1)
                {looseboxes = getString(R.string.statsLB1) + " "  + loosebox + " " + getString(R.string.statsLB2a);}
            else if (loosebox > 1)
                {looseboxes = getString(R.string.statsLB1) + " "  + loosebox + " " + getString(R.string.statsLB2);}
            else {looseboxes = "";}

            if (loosemanual == 1)
                {looseman = getString(R.string.statsLM1) + " "  + loosemanual + " " + getString(R.string.statsLM2a);}
            else if (loosemanual > 1)
                {looseman = getString(R.string.statsLM1) + " "  + loosemanual + " " + getString(R.string.statsLM2);}
            else {looseman = "";}

            looseitems = (loosecarts
            + looseboxes
            + looseman);
            loose.setText(looseitems);
            c.close();
            db.close();//close the database
            }
        }


    public void gameregion(){//selects the region from the database
        Log.d("Pixo","Running game region");

        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                licensed = (c.getString(c.getColumnIndex("licensed")));
                wherestatement = (c.getString(c.getColumnIndex("region")));
                currency = (c.getString(c.getColumnIndex("currency")));
                showprices = (c.getInt(c.getColumnIndex("show_price")));

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }


    public void piechart(){
        i = 0;
        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        //Cursor c;
        sql = "SELECT * FROM eu where owned = 1 and cart = 1 and genre = 'Action-Adventure'";
        Cursor c = db.rawQuery(sql, null);
        int ownedactionadventure = c.getCount();

        if (ownedactionadventure > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Action'";
        c = db.rawQuery(sql, null);
        int ownedaction = c.getCount();

        if (ownedaction > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Adventure'";
        c = db.rawQuery(sql, null);
        int ownedadventure = c.getCount();

        if (ownedadventure > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Arcade'";
        c = db.rawQuery(sql, null);
        int ownedarcade = c.getCount();

        if (ownedarcade > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Beat em Up'";
        c = db.rawQuery(sql, null);
        int ownedbeatemup = c.getCount();

        if (ownedbeatemup > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Board game'";
        c = db.rawQuery(sql, null);
        int ownedboardgame = c.getCount();

        if (ownedboardgame > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Compilation'";
        c = db.rawQuery(sql, null);
        int ownedcompilation = c.getCount();
        Log.d("compilation set up","value: " + ownedcompilation);
        if (ownedcompilation > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Fighting'";
        c = db.rawQuery(sql, null);
        int ownedfighting = c.getCount();

        if (ownedfighting > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Other'";
        c = db.rawQuery(sql, null);
        int ownedother = c.getCount();

        if (ownedother > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Platformer'";
        c = db.rawQuery(sql, null);
        int ownedplatformer = c.getCount();

        if (ownedplatformer > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Puzzle'";
        c = db.rawQuery(sql, null);
        int ownedpuzzle = c.getCount();

        if (ownedpuzzle > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Racing'";
        c = db.rawQuery(sql, null);
        int ownedracing = c.getCount();

        if (ownedracing > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Role-Playing Game'";
        c = db.rawQuery(sql, null);
        int ownedroleplayinggame = c.getCount();

        if (ownedroleplayinggame > 0) {
            i++;
        }


        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Shoot em Up'";
        c = db.rawQuery(sql, null);
        int ownedshootemup = c.getCount();

        if (ownedshootemup > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Shooter'";
        c = db.rawQuery(sql, null);
        int ownedshooter = c.getCount();

        if (ownedshooter > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Simulation'";
        c = db.rawQuery(sql, null);
        int ownedsimulation = c.getCount();

        if (ownedsimulation > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Sports'";
        c = db.rawQuery(sql, null);
        int ownedsports = c.getCount();

        if (ownedsports > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Strategy'";
        c = db.rawQuery(sql, null);
        int ownedstrategy = c.getCount();

        if (ownedstrategy > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Traditional'";
        c = db.rawQuery(sql, null);
        int ownedtraditional = c.getCount();
        if (ownedtraditional > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where owned = 1 and cart = 1  and genre = 'Trivia'";
        c = db.rawQuery(sql, null);
        int ownedtrivia = c.getCount();
        if (ownedtrivia > 0) {
            i++;
        }

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Action-Adventure'";
        c = db.rawQuery(sql, null);
        int ownedAactionadventure = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Action-Adventure'";
        c = db.rawQuery(sql, null);
        int ownedBactionadventure = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Action-Adventure'";
        c = db.rawQuery(sql, null);
        int ownedUSactionadventure = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Action'";
        c = db.rawQuery(sql, null);
        int ownedAaction = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Action'";
        c = db.rawQuery(sql, null);
        int ownedBaction = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Action'";
        c = db.rawQuery(sql, null);
        int ownedUSaction = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Adventure'";
        c = db.rawQuery(sql, null);
        int ownedAadventure = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Adventure'";
        c = db.rawQuery(sql, null);
        int ownedBadventure = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Adventure'";
        c = db.rawQuery(sql, null);
        int ownedUSadventure = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Arcade'";
        c = db.rawQuery(sql, null);
        int ownedAarcade = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Arcade'";
        c = db.rawQuery(sql, null);
        int ownedBarcade = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Arcade'";
        c = db.rawQuery(sql, null);
        int ownedUSarcade = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Beat em Up'";
        c = db.rawQuery(sql, null);
        int ownedAbeatemup = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Beat em Up'";
        c = db.rawQuery(sql, null);
        int ownedBbeatemup = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Beat em Up'";
        c = db.rawQuery(sql, null);
        int ownedUSbeatemup = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Board game'";
        c = db.rawQuery(sql, null);
        int ownedAboardgame = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Board game'";
        c = db.rawQuery(sql, null);
        int ownedBboardgame = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Board game'";
        c = db.rawQuery(sql, null);
        int ownedUSboardgame = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Compilation'";
        c = db.rawQuery(sql, null);
        int ownedAcompilation = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Compilation'";
        c = db.rawQuery(sql, null);
        int ownedBcompilation = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Compilation'";
        c = db.rawQuery(sql, null);
        int ownedUScompilation = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Fighting'";
        c = db.rawQuery(sql, null);
        int ownedAfighting = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Fighting'";
        c = db.rawQuery(sql, null);
        int ownedBfighting = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Fighting'";
        c = db.rawQuery(sql, null);
        int ownedUSfighting = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Other'";
        c = db.rawQuery(sql, null);
        int ownedAother = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Other'";
        c = db.rawQuery(sql, null);
        int ownedBother = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Other'";
        c = db.rawQuery(sql, null);
        int ownedUSother = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Platformer'";
        c = db.rawQuery(sql, null);
        int ownedAplatformer = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Platformer'";
        c = db.rawQuery(sql, null);
        int ownedBplatformer = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Platformer'";
        c = db.rawQuery(sql, null);
        int ownedUSplatformer = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Puzzle'";
        c = db.rawQuery(sql, null);
        int ownedApuzzle = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Puzzle'";
        c = db.rawQuery(sql, null);
        int ownedBpuzzle = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Puzzle'";
        c = db.rawQuery(sql, null);
        int ownedUSpuzzle = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Racing'";
        c = db.rawQuery(sql, null);
        int ownedAracing = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Racing'";
        c = db.rawQuery(sql, null);
        int ownedBracing = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Racing'";
        c = db.rawQuery(sql, null);
        int ownedUSracing = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Role-Playing Game'";
        c = db.rawQuery(sql, null);
        int ownedAroleplayinggame = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Role-Playing Game'";
        c = db.rawQuery(sql, null);
        int ownedBroleplayinggame = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Role-Playing Game'";
        c = db.rawQuery(sql, null);
        int ownedUSroleplayinggame = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Shoot em Up'";
        c = db.rawQuery(sql, null);
        int ownedAshootemup = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Shoot em Up'";
        c = db.rawQuery(sql, null);
        int ownedBshootemup = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Shoot em Up'";
        c = db.rawQuery(sql, null);
        int ownedUSshootemup = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Shooter'";
        c = db.rawQuery(sql, null);
        int ownedAshooter = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Shooter'";
        c = db.rawQuery(sql, null);
        int ownedBshooter = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Shooter'";
        c = db.rawQuery(sql, null);
        int ownedUSshooter = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Simulation'";
        c = db.rawQuery(sql, null);
        int ownedAsimulation = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Simulation'";
        c = db.rawQuery(sql, null);
        int ownedBsimulation = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Simulation'";
        c = db.rawQuery(sql, null);
        int ownedUSsimulation = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Sports'";
        c = db.rawQuery(sql, null);
        int ownedAsports = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Sports'";
        c = db.rawQuery(sql, null);
        int ownedBsports = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Sports'";
        c = db.rawQuery(sql, null);
        int ownedUSsports = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Strategy'";
        c = db.rawQuery(sql, null);
        int ownedAstrategy = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Strategy'";
        c = db.rawQuery(sql, null);
        int ownedBstrategy = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Strategy'";
        c = db.rawQuery(sql, null);
        int ownedUSstrategy = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Traditional'";
        c = db.rawQuery(sql, null);
        int ownedAtraditional = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Traditional'";
        c = db.rawQuery(sql, null);
        int ownedBtraditional = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Traditional'";
        c = db.rawQuery(sql, null);
        int ownedUStraditional = c.getCount();

        sql = "SELECT * FROM eu where pal_a_cart = 8783 and genre = 'Trivia'";
        c = db.rawQuery(sql, null);
        int ownedAtrivia = c.getCount();

        sql = "SELECT * FROM eu where pal_b_cart = 8783 and genre = 'Trivia'";
        c = db.rawQuery(sql, null);
        int ownedBtrivia = c.getCount();

        sql = "SELECT * FROM eu where ntsc_cart = 8783 and genre = 'Trivia'";
        c = db.rawQuery(sql, null);
        int ownedUStrivia = c.getCount();

        ownedaction = ownedAaction + ownedBaction + ownedUSaction;
        ownedactionadventure = ownedAactionadventure + ownedBactionadventure + ownedUSactionadventure;
        ownedadventure = ownedAadventure + ownedBadventure + ownedUSadventure;
        ownedarcade = ownedAarcade + ownedBarcade + ownedUSarcade;
        ownedbeatemup = ownedAbeatemup + ownedBbeatemup + ownedUSbeatemup;
        ownedboardgame = ownedAboardgame + ownedBboardgame + ownedUSboardgame;
        ownedcompilation = ownedAcompilation + ownedBcompilation + ownedUScompilation;
        ownedfighting = ownedAfighting + ownedBfighting + ownedUSfighting;
        ownedother = ownedAother + ownedBother + ownedUSother;
        ownedplatformer = ownedAplatformer + ownedBplatformer + ownedUSplatformer;
        ownedpuzzle = ownedApuzzle + ownedBpuzzle + ownedUSpuzzle;
        ownedracing = ownedAracing + ownedBracing + ownedUSracing;
        ownedroleplayinggame = ownedAroleplayinggame + ownedBroleplayinggame + ownedUSroleplayinggame;
        ownedshootemup = ownedAshootemup + ownedBshootemup + ownedUSshootemup;
        ownedshooter = ownedAshooter + ownedBshooter + ownedUSshooter;
        ownedsimulation = ownedAsimulation + ownedBsimulation + ownedUSsimulation;
        ownedsports = ownedAsports + ownedBsports + ownedUSsports;
        ownedstrategy = ownedAstrategy + ownedBstrategy + ownedUSstrategy;
        ownedtraditional = ownedAtraditional + ownedBtraditional + ownedUStraditional;
        ownedtrivia = ownedAtrivia + ownedBtrivia + ownedUStrivia;

        names = new String[i];
        datapoints = new float[i];
        piecolours = new int[i];
        io = 0;
        if (ownedactionadventure > 0) {
            names[io] = "Action-Adventure";
            datapoints[io] = ownedactionadventure;
            piecolours[io] = Color.parseColor("#FF2B32");
            io++;
        }
        if (ownedaction > 0) {
            names[io] = "Action";
            datapoints[io] = ownedaction;
            piecolours[io] = Color.parseColor("#FF6A00");
            io++;
        }
        if (ownedadventure > 0) {
            names[io] = "Adventure";
            datapoints[io] = ownedadventure;
            piecolours[io] = Color.parseColor("#FFD800");
            io++;
        }
        if (ownedarcade > 0) {
            names[io] = "Arcade";
            datapoints[io] = ownedarcade;
            piecolours[io] = Color.parseColor("#B6FF00");
            io++;
        }
        if (ownedbeatemup > 0) {
            names[io] = "Beat em Up";
            datapoints[io] = ownedbeatemup;
            piecolours[io] = Color.parseColor("#FF004C");
            io++;
        }
        if (ownedboardgame > 0) {
            names[io] = "Board game";
            datapoints[io] = ownedboardgame;
            piecolours[io] = Color.parseColor("#FF0FFF");
                    io++;
        }
        if (ownedcompilation > 0) {
            names[io] = "Compilation";
            datapoints[io] = ownedcompilation;
            piecolours[io] = Color.parseColor("#0095FF");
            io++;
        }
        if (ownedfighting > 0) {
            names[io] = "Fighting";
            datapoints[io] = ownedfighting;
            piecolours[io] = Color.parseColor("#B200FF");
            io++;
        }
        if (ownedother > 0) {
            names[io] = "Other";
            datapoints[io] = ownedother;
            piecolours[io] = Color.parseColor("#FF006E");
            io++;
        }
        if (ownedplatformer > 0) {
            names[io] = "Platformer";
            datapoints[io] = ownedplatformer;
            piecolours[io] = Color.parseColor("#FF00DC");
            io++;
        }
        if (ownedpuzzle > 0) {
            names[io] = "Puzzle";
            datapoints[io] = ownedpuzzle;
            piecolours[io] = Color.parseColor("#B002FF");
            io++;
        }
        if (ownedracing > 0) {
            names[io] = "Racing";
            datapoints[io] = ownedracing;
            piecolours[io] = Color.parseColor("#FF7F7F");
            io++;
        }
        if (ownedroleplayinggame > 0) {
            names[io] = "Role-Playing Game";
            datapoints[io] = ownedroleplayinggame;
            piecolours[io] = Color.parseColor("#C0C0C0");
            io++;
        }
        if (ownedshootemup > 0) {
            names[io] = "Shoot em Up";
            datapoints[io] = ownedshootemup;
            piecolours[io] = Color.parseColor("#613F7C");
            io++;
        }
        if (ownedshooter > 0) {
            names[io] = "Shooter";
            datapoints[io] = ownedshooter;
            piecolours[io] = Color.parseColor("#60C5FF");
            io++;
        }
        if (ownedsimulation > 0) {
            names[io] = "Simulation";
            datapoints[io] = ownedsimulation;
            piecolours[io] = Color.parseColor("#FFAFB5");
            io++;
        }
        if (ownedsports > 0) {
            names[io] = "Sports";
            datapoints[io] = ownedsports;
            piecolours[io] = Color.parseColor("#D5FFBF");
            io++;
        }
        if (ownedstrategy > 0) {
            names[io] = "Strategy";
            datapoints[io] = ownedstrategy;
            piecolours[io] = Color.parseColor("#4FFFBE");
            io++;
        }
        if (ownedtraditional > 0) {
            names[io] = "Traditional";
            datapoints[io] = ownedtraditional;
            piecolours[io] = Color.parseColor("#A856FF");
            io++;
        }
        if (ownedtrivia > 0) {
            names[io] = "Trivia";
            datapoints[io] = ownedtrivia;
            piecolours[io] = Color.parseColor("#CBFF72");
            io++;
        }

        c.close();
        db.close();

        if (i > 0){PieChartView pieChartView = (PieChartView) findViewById(R.id.pie_chart);
            pieChartView.setDataPoints(datapoints, names, piecolours);
            // Because this activity is of the type PieChartView.Callback
            pieChartView.setCallback(Statistics.this);}

    }

    public void piechart2(){
        i = 0;

        if (ownedPalA > 0){
            i++;
        }
        if (ownedPalB > 0){
            i++;
        }
        if (ownedUS > 0){
            i++;
        }

        Log.i("Pixo-numbers", "Number: " + i + " Owned Euro: " + ownedPalA + " Owned US: " + ownedUS);
        totalOwnedGames = ownedPalA + ownedPalB + ownedUS;
        names2 = new String[i];
        datapoints2 = new float[i];
        piecolours2 = new int[i];
        io = 0;
        if (ownedPalA > 0) {
            names2[io] = "Pal A games";
            datapoints2[io] = ownedPalA;
            piecolours2[io] = Color.parseColor("#F73427");
            io++;
        }

        if (ownedPalB > 0) {
            names2[io] = "Pal B games";
            datapoints2[io] = ownedPalB;
            piecolours2[io] = Color.parseColor("#FE8618");
            io++;
        }

        if (ownedUS > 0) {
            names2[io] = "US games";
            datapoints2[io] = ownedUS;
            piecolours2[io] = Color.parseColor("#21A8D8");
            io++;
        }

        if (i > 0){ SecondPieChartView secondPieChartView = (SecondPieChartView) findViewById(R.id.pie_chart2);
            secondPieChartView.setDataPoints(datapoints2, names2, piecolours2);
            // Because this activity is of the type PieChartView.Callback
            secondPieChartView.setCallback(Statistics.this);
        } else if (i == 1){
            RlPie2.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        Log.d("Pixo", "Running run function");
        run();//run the list tables function

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_mainpage){
            Intent intent = new Intent(this, MainActivity.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_allgames) {
            Intent intent = new Intent(this, AllGames.class);//opens a new screen when the shopping list is clicked
            intent.putExtra("wherestatement", wherestatement);
            startActivity(intent);
        } else if (id == R.id.nav_neededgames) {
            Intent intent = new Intent(this, NeededGames.class);//opens a new screen when the shopping list is clicked
            intent.putExtra("wherestatement", wherestatement);
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_ownedgames) {
            Intent intent = new Intent(this, OwnedGames.class);//opens a new screen when the shopping list is clicked
            intent.putExtra("wherestatement", wherestatement);
            startActivity(intent);
        } else if (id == R.id.nav_favouritegames) {
            Intent intent = new Intent(this, FavouriteGames.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_wishlist) {
            Intent intent = new Intent(this, WishList.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_shelforder) {
            Intent intent = new Intent(this, ShelfOrder.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_finished) {
            Intent intent = new Intent(this, FinishedGames.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, Settings.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
