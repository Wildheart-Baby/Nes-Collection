package uk.co.bossdogsoftware.nes_collection.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.adapters.NesIndexAdapter;
import uk.co.bossdogsoftware.nes_collection.adapters.NesOwnedAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameItemsIndex;

public class OwnedGames extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Context context = this;

    AllGameItems gameListItems;

    String readgamename, str, sql, listName,searchterm,fieldname, wherestatement, sql1, sql2, sql3, licensed, currentgroup, order, currency, thename, theimage;
    String prevgroup = "";

    int readgameid, gameid, ownedgames, totalgames,  readindexid, index, top, region1games, region2games,region3games, count, randomgame, itemId, viewas, showprice, ordering, titles;
    ArrayAdapter<CharSequence> adapter;
    //ArrayList<AllGameItems> nesList;
    ListView ownedlistView, alphaIndex;
    GridView gamegalleryview;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_games);

        wherestatement = getIntent().getStringExtra("wherestatement");
        setTitle("Owned Games");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gamegalleryview = findViewById(R.id.gvAllGames);
        alphaIndex = findViewById(R.id.lvAlphaIndex);
        ownedlistView = findViewById(R.id.lvOwnedGames); //sets up a listview with the name shoplistview

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        NesOwnedAdapter.screenwidth = metrics.widthPixels;


        gameregion();
        readList();
        gamescount();
        //toolbar.setSubtitle(str);

        alphaIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GameItemsIndex indexListItems = (GameItemsIndex) arg0.getItemAtPosition(arg2);
                readindexid = indexListItems.getItemid();
                //readindexid = readindexid - 1;
                ownedlistView.setSelection(readindexid);
                //gamegalleryview.setSelection(readindexid);

            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-0537596348696744~2585816192");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ownedgames, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(OwnedGames.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(OwnedGames.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;
            case R.id.action_random:
                randomgame();
                return true;
            case R.id.action_order_alphabetic:
                order_alphabetic();
                return true;
            case R.id.action_order_price:
                order_price();
                return true;
            case R.id.action_about:
                Intent intent3 = new Intent(OwnedGames.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if (showprice == 0){
            menu.removeItem(R.id.action_order_alphabetic);
            menu.removeItem(R.id.action_order_price);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPause(){
        super.onPause();

        index = ownedlistView.getFirstVisiblePosition();
        View v = ownedlistView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
    }

    public void readList() {//the readlist function

        sql = "SELECT * FROM eu where " + wherestatement + licensed + "";


        if (viewas == 0) {
            if (ordering == 0) {
                NesOwnedAdapter nes = new NesOwnedAdapter(this, HomeScreenActivity.gamesList, 1);//set up an new list adapter from the arraylist
                gamegalleryview.setVisibility(View.GONE);
                alphaIndex.setVisibility(View.VISIBLE);
                ownedlistView.setAdapter(nes);
                NesIndexAdapter nii = new NesIndexAdapter(this, HomeScreenActivity.indexList);
                alphaIndex.setAdapter(nii);
            } else if (ordering == 1){

            }

        } else if (viewas == 1) {

        }
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                viewas = (c.getInt(c.getColumnIndex("game_view")));
                showprice = (c.getInt(c.getColumnIndex("show_price")));
                NesOwnedAdapter.showprice = (c.getInt(c.getColumnIndex("show_price")));
                ordering = (c.getInt(c.getColumnIndex("ordered")));
                currency = (c.getString(c.getColumnIndex("currency")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                Log.d("Pixo", wherestatement);

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
        if (titles == 0){
            thename = "name";
            theimage = "image";
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
        }
    }

    public void gamescount(){
        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        Cursor c; //= db.rawQuery(sql, null);//select everything from the database table
        //sql = "SELECT * FROM eu where " + wherestatement + "";
        sql1 = "SELECT * FROM eu where pal_a_cart = 8783 " + licensed +  "";
        sql2 = "SELECT * FROM eu where pal_b_cart = 8783" + licensed +  "";
        sql3 = "SELECT * FROM eu where ntsc_cart = 8783" + licensed +  "";

        c = db.rawQuery(sql1, null);
        region1games = c.getCount();
        c.close();
        c = db.rawQuery(sql2, null);
        region2games = c.getCount();
        c.close();
        c = db.rawQuery(sql3, null);
        region3games = c.getCount();
        c.close();
        db.close();//close the database
        ownedgames = region1games + region2games + region3games;
        str = "You own " + ownedgames + " games";
        toolbar.setSubtitle(str);
    }

    public void randomgame(){
        if(viewas == 0) {
            count = ownedlistView.getCount();
        }else if (viewas == 1){
            count = gamegalleryview.getCount();
        }
        if (count >1) {

            Random rand = new Random();
            randomgame = rand.nextInt(count);

            if(viewas == 0) {
                gameListItems = (AllGameItems) ownedlistView.getItemAtPosition(randomgame);//get the position of the item on the list
                itemId = gameListItems.getItemId();//get the item id
            }else if (viewas == 1){
                gameListItems = (AllGameItems) gamegalleryview.getItemAtPosition(randomgame);//get the position of the item on the list
                itemId = gameListItems.getItemId();//get the item id
            }

            final Dialog arandomgame = new Dialog(OwnedGames.this);//sets up the dialog
            arandomgame.requestWindowFeature(Window.FEATURE_NO_TITLE);
            arandomgame.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//sets the dialog background as transparent
            arandomgame.setContentView(R.layout.random_game);//sets up the layout of the dialog box


            Button cancel = arandomgame.findViewById(R.id.btnCancel);
            TextView title = arandomgame.findViewById(R.id.lblTitle);
            ImageView cover = arandomgame.findViewById(R.id.imgGameCover);

            //randomgame.setTitle("Random game");


            title.setText(gameListItems.getName());
            String gameimage = (gameListItems.getImage());
                    int coverid = OwnedGames.this.getResources().getIdentifier(gameimage, "drawable", OwnedGames.this.getPackageName());
                    cover.setImageResource(coverid);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arandomgame.dismiss();
                }
            });
            arandomgame.show();//show the dialog
        }
        else if(count <2){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Sorry you don't have enough games to make a random selection",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void order_price(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database

        //SELECT * FROM (SELECT name, pal_a_cost FROM eu UNION SELECT name, pal_b_cost FROM eu union select name, ntsc_cost from eu)t ORDER BY t.pal_a_cost DESC
        String str = "UPDATE settings SET ordered = 1"; //update the database basket field with 8783
        //Log.d("Pixo", str);
        db.execSQL(str);//run the sql command
        db.close();

        gameregion();
        readList();
    }

    public void order_alphabetic(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database

        String str = "UPDATE settings SET ordered = 0"; //update the database basket field with 8783
        Log.d("Pixo", str);
        db.execSQL(str);//run the sql command
        db.close();

        gameregion();
        readList();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        ownedgames = 0;
        readList();//run the list tables function
        ownedlistView.setSelectionFromTop(index, top);
        gamescount();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_mainpage){
            Intent intent = new Intent(this, HomeScreenActivity.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }else if (id == R.id.nav_allgames) {

        } else if (id == R.id.nav_neededgames) {
            Intent intent = new Intent(this, NeededGames.class);//opens a new screen when the shopping list is clicked
            intent.putExtra("wherestatement", wherestatement);
            finish();
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
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(this, Statistics.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_finished) {
            Intent intent = new Intent(this, FinishedGames.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, Settings.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
