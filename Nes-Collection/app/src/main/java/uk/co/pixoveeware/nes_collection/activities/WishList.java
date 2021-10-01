package uk.co.pixoveeware.nes_collection.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.adapters.NesCollectionAdapter;
import uk.co.pixoveeware.nes_collection.adapters.NesCollectionImageAdapter;
import uk.co.pixoveeware.nes_collection.models.GameItems;

public class WishList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView gamelistView;
    TextView thesearchresults;
    GridView gamegalleryview;

    Context context;

    String name, readgamename, searchterm,fieldname, sql, currentgroup, wherestatement, theimage, thename, thisimage;
    String prevgroup = "";
    int readgameid, index, top, count, randomgame, itemId, totalResults, viewas, titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        thesearchresults = (TextView) findViewById(R.id.lblSearchResults);
        gamelistView = (ListView) findViewById(R.id.listView);
        gamegalleryview = (GridView) findViewById(R.id.gvAllGames);
        setTitle("Wish List");

        gameregion();
        readList();

        gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(WishList.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                intent.putExtra("sqlstatement", "SELECT * FROM eu where wishlist = 1");
                intent.putExtra("position", arg2);
                startActivity(intent);//start the new screen
            }
        });

        gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(WishList.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });

        gamegalleryview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(WishList.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                intent.putExtra("sqlstatement", "SELECT * FROM eu where wishlist = 1");
                intent.putExtra("position", arg2);
                startActivity(intent);//start the new screen
            }
        });

        gamegalleryview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(WishList.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });

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
                Intent intent = new Intent(WishList.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(WishList.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(WishList.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            selectItem(pos);
            Log.d("Pixo", "value: " + pos);
        }
    }

    private void selectItem(int pos) {
        // update the main content by replacing fragments
        Log.d("Pixo", "value: " + pos);
        switch(pos){
            case '0' :
                Log.d("Pixo", "selectItem running");
                Intent intent = new Intent(this, AllGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
                break;
            case 1 :
                intent = new Intent(this, NeededGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
                break;
            case 2 :
                intent = new Intent(this, OwnedGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
                break;
            case 3 :
                intent = new Intent(this, FavouriteGames.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                break;
            case 4 :
                intent = new Intent(this, WishList.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                break;
            case 5 :
                intent = new Intent(this, ShelfOrder.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                break;
            case 6 :
                intent = new Intent(this, Statistics.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                break;
            case 7 :
                intent = new Intent(this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                break;
            default:
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        index = gamelistView.getFirstVisiblePosition();
        View v = gamelistView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
    }

    public void readList(){//the readlist function
        //if (HomeScreenActivity.nesList == null){ HomeScreenActivity.readList(); }
        HomeScreenActivity.gamesList.clear();//clear the shoppingList array

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        sql ="SELECT * FROM eu where wishlist = 1";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table
        //Cursor c = db.rawQuery("SELECT * FROM " + fname, null);

        //Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                GameItems nesListItems = new GameItems();//creates a new array
                currentgroup = c.getString(c.getColumnIndex("groupheader"));

                if(!currentgroup.equals(prevgroup)){
                    nesListItems.setGroup(c.getString(c.getColumnIndex("groupheader")));
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    nesListItems.setName(c.getString(c.getColumnIndex(thename)));
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
                    nesListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    nesListItems.setName(c.getString(c.getColumnIndex(thename)));
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
            //c.getcount(totalgames);
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);

        c = db.rawQuery(sql, null);
        totalResults = c.getCount();
        c.close();
        db.close();//close the database

        if (totalResults == 0){
            gamelistView.setVisibility(View.GONE);
            thesearchresults.setVisibility(View.VISIBLE);
            thesearchresults.setText(getString(R.string.wishlisNoResults));
        }else {
            if(viewas == 0){
            NesCollectionAdapter nes = new NesCollectionAdapter(this, HomeScreenActivity.gamesList);//set up an new list adapter from the arraylist
            gamegalleryview.setVisibility(View.GONE);
            gamelistView.setAdapter(nes);

        }else if (viewas == 1){
            NesCollectionImageAdapter nes = new NesCollectionImageAdapter(this, HomeScreenActivity.gamesList);//set up an new list adapter from the arraylist
            gamelistView.setVisibility(View.GONE);
            gamegalleryview.setAdapter(nes);
            }
        }
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                viewas = (c.getInt(c.getColumnIndex("game_view")));
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
            Log.d("pixo-the image", theimage);
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            Log.d("pixo-the image", theimage);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        readList();//run the list tables function
        gamelistView.setSelectionFromTop(index, top);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_mainpage){
            Intent intent = new Intent(this, HomeScreenActivity.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }else if (id == R.id.nav_allgames) {
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
        }  else if (id == R.id.nav_finished) {
            Intent intent = new Intent(this, FinishedGames.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }  else if (id == R.id.nav_shelforder) {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}