package uk.co.bossdogsoftware.nes_collection.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.adapters.NesCollectionAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;

public class FinishedGames extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, sql,listName,searchterm,fieldname, wherestatement, title, currentgroup, licensed;
    String prevgroup = "";
    int readgameid, gameid, index, top, totalResults, viewas;
    ArrayAdapter<CharSequence> adapter;
    //ArrayList<AllGameItems> nesList;
    ListView finishedgamelistView;
    GridView gamegalleryview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_games);
        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database
        gamegalleryview = (GridView) findViewById(R.id.gvAllGames);
        finishedgamelistView = (ListView) findViewById(R.id.lvFinishedGames); //sets up a listview with the name shoplistview


        gameregion();
        readList();



        setTitle("Finished Games");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    public void readList(){//the readlist function
        //if (HomeScreenActivity.nesList == null){ HomeScreenActivity.readList(); }
        //HomeScreenActivity.nesList.clear();//clear the shoppingList array
        TextView header = (TextView) findViewById(R.id.lblHeader);
        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        sql = "SELECT * FROM eu where finished_game = 1";
        Log.d("Pixo", sql);
        Cursor c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                AllGameItems nesListItems = new AllGameItems();//creates a new array
                currentgroup = c.getString(c.getColumnIndex("groupheader"));

                if(!currentgroup.equals(prevgroup)){
                    nesListItems.setGroup(c.getString(c.getColumnIndex("groupheader")));
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                    nesListItems.setName(c.getString(c.getColumnIndex("name")));
                    nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                    nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    nesListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                    nesListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                    nesListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                    nesListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                    nesListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                    nesListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                    nesListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                    nesListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                    nesListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                    nesListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                    nesListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                    nesListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                    nesListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    nesListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    nesListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    nesListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    nesListItems.setYear(c.getString(c.getColumnIndex("year"))); //re-edit this after testing
                    nesListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                    nesListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                    nesListItems.setDeveloper((c.getString(c.getColumnIndex("developer"))));
                    nesListItems.setSynopsis((c.getString(c.getColumnIndex("synopsis"))));
                    nesListItems.setAustralia(c.getInt(c.getColumnIndex("flag_australia")));
                    nesListItems.setAustria(c.getInt(c.getColumnIndex("flag_austria")));
                    nesListItems.setBenelux(c.getInt(c.getColumnIndex("flag_benelux")));
                    nesListItems.setDenmark(c.getInt(c.getColumnIndex("flag_denmark")));
                    nesListItems.setFinland(c.getInt(c.getColumnIndex("flag_finland")));
                    nesListItems.setFrance(c.getInt(c.getColumnIndex("flag_france")));
                    nesListItems.setGermany((c.getInt(c.getColumnIndex("flag_germany"))));
                    nesListItems.setGreece(c.getInt(c.getColumnIndex("flag_greece")));
                    nesListItems.setIreland(c.getInt(c.getColumnIndex("flag_ireland")));
                    nesListItems.setItaly(c.getInt(c.getColumnIndex("flag_italy")));
                    nesListItems.setNorway(c.getInt(c.getColumnIndex("flag_norway")));
                    nesListItems.setPoland(c.getInt(c.getColumnIndex("flag_poland")));
                    nesListItems.setPortugal(c.getInt(c.getColumnIndex("flag_portugal")));
                    nesListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                    nesListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                    nesListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                    nesListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                    nesListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));

                    HomeScreenActivity.gamesList.add(nesListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex("groupheader"));
                }
                else if(currentgroup.equals(prevgroup)){
                    nesListItems.setGroup("no");
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                    nesListItems.setName(c.getString(c.getColumnIndex("name")));
                    nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                    nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    nesListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                    nesListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                    nesListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                    nesListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                    nesListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                    nesListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                    nesListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                    nesListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                    nesListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                    nesListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                    nesListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                    nesListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                    nesListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    nesListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    nesListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    nesListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    nesListItems.setYear(c.getString(c.getColumnIndex("year"))); //re-edit this after testing
                    nesListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                    nesListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                    nesListItems.setDeveloper((c.getString(c.getColumnIndex("developer"))));
                    nesListItems.setSynopsis((c.getString(c.getColumnIndex("synopsis"))));
                    nesListItems.setAustralia(c.getInt(c.getColumnIndex("flag_australia")));
                    nesListItems.setAustria(c.getInt(c.getColumnIndex("flag_austria")));
                    nesListItems.setBenelux(c.getInt(c.getColumnIndex("flag_benelux")));
                    nesListItems.setDenmark(c.getInt(c.getColumnIndex("flag_denmark")));
                    nesListItems.setFinland(c.getInt(c.getColumnIndex("flag_finland")));
                    nesListItems.setFrance(c.getInt(c.getColumnIndex("flag_france")));
                    nesListItems.setGermany((c.getInt(c.getColumnIndex("flag_germany"))));
                    nesListItems.setGreece(c.getInt(c.getColumnIndex("flag_greece")));
                    nesListItems.setIreland(c.getInt(c.getColumnIndex("flag_ireland")));
                    nesListItems.setItaly(c.getInt(c.getColumnIndex("flag_italy")));
                    nesListItems.setNorway(c.getInt(c.getColumnIndex("flag_norway")));
                    nesListItems.setPoland(c.getInt(c.getColumnIndex("flag_poland")));
                    nesListItems.setPortugal(c.getInt(c.getColumnIndex("flag_portugal")));
                    nesListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                    nesListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                    nesListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                    nesListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                    nesListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));

                    HomeScreenActivity.gamesList.add(nesListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex("groupheader"));
                }
                c.moveToNext();//move to the next record
            }
            totalResults = c.getCount();
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);
        db.close();//close the database
        if (totalResults == 0){
            finishedgamelistView.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            header.setText("You have haven't finished any games, you should get to playing");
        }else {
            if(viewas == 0){
                NesCollectionAdapter nes = new NesCollectionAdapter(this, HomeScreenActivity.gamesList);//set up an new list adapter from the arraylist
                gamegalleryview.setVisibility(View.GONE);
                finishedgamelistView.setAdapter(nes);

            }else if (viewas == 1){

            }
        }
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                viewas = (c.getInt(c.getColumnIndex("game_view")));

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_mainpage) {
            Intent intent = new Intent(this, HomeScreenActivity.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }else if (id == R.id.nav_allgames) {

        } else if (id == R.id.nav_neededgames) {
            Intent intent = new Intent(this, NeededGames.class);//opens a new screen when the shopping list is clicked
            intent.putExtra("wherestatement", wherestatement);
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
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(this, Statistics.class);//opens a new screen when the shopping list is clicked
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
