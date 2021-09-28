package uk.co.pixoveeware.nes_collection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class FavouriteGames extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView favouritelistView;
    Context context;
    TextView thesearchresults;
    GridView gamegalleryview;

    String name, readgamename, searchterm,fieldname, wherestatement, title, sql, currentgroup;
    int readgameid, index, top, count, randomgame, itemId, totalResults, viewas;
    String prevgroup = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_games);

        setTitle("Favourite Games");
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

        favouritelistView = (ListView) findViewById(R.id.lvFavouriteGames);
        thesearchresults = (TextView) findViewById(R.id.lblSearchResults);
        gamegalleryview = (GridView) findViewById(R.id.gvAllGames);

        gameregion();
        readList();

        favouritelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(FavouriteGames.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                intent.putExtra("position", arg2);
                intent.putExtra("sqlstatement", sql);
                startActivity(intent);//start the new screen
            }
        });

        favouritelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(FavouriteGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });

        gamegalleryview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(FavouriteGames.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                intent.putExtra("position", arg2);
                intent.putExtra("sqlstatement", sql);
                startActivity(intent);//start the new screen
            }
        });

        gamegalleryview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(FavouriteGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favouritegames, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_random:
                    randomgame();
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(FavouriteGames.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(FavouriteGames.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(FavouriteGames.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



    @Override
    public void onPause(){
        super.onPause();

        index = favouritelistView.getFirstVisiblePosition();
        View v = favouritelistView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
    }

    public void readList() {//the readlist function
        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array


        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        sql = "SELECT * FROM eu where favourite = 1";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table
        //Cursor c = db.rawQuery("SELECT * FROM " + fname, null);

        //Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {//move to the first record
            while (!c.isAfterLast()) {//while there are records to read
            NesItems nesListItems = new NesItems();//creates a new array
            currentgroup = c.getString(c.getColumnIndex("groupheader"));

            if(!currentgroup.equals(prevgroup)){
                nesListItems.setGroup(c.getString(c.getColumnIndex("groupheader")));
                nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                nesListItems.setGroup(c.getString(c.getColumnIndex("groupheader")));
                nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                nesListItems.setName(c.getString(c.getColumnIndex("name")));
                nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                //nesListItems.setOwned(c.getInt(c.getColumnIndex("favourite")));
                nesList.add(nesListItems);//add items to the arraylist
                prevgroup = c.getString(c.getColumnIndex("groupheader"));
            }
            else if(currentgroup.equals(prevgroup)){
                nesListItems.setGroup("no");
                nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                nesListItems.setName(c.getString(c.getColumnIndex("name")));
                nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                nesList.add(nesListItems);//add items to the arraylist
                prevgroup = c.getString(c.getColumnIndex("groupheader"));
            }
                c.moveToNext();//move to the next record
            }
            //c.getcount(totalgames);
            c = db.rawQuery(sql, null);
            totalResults = c.getCount();
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);

        db.close();//close the database
        if (totalResults == 0){
            favouritelistView.setVisibility(View.GONE);
            thesearchresults.setVisibility(View.VISIBLE);
            thesearchresults.setText("You have no favourite games, oh that's sad, you should add some");
        }else {
            if(viewas == 0){
                NesCollectionAdapter nes = new NesCollectionAdapter(this, nesList);//set up an new list adapter from the arraylist
                gamegalleryview.setVisibility(View.GONE);
                favouritelistView.setAdapter(nes);

            }else if (viewas == 1){
                NesCollectionImageAdapter nes = new NesCollectionImageAdapter(this, nesList);//set up an new list adapter from the arraylist
                favouritelistView.setVisibility(View.GONE);
                gamegalleryview.setAdapter(nes);
            }
    }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        readList();//run the list tables function
        favouritelistView.setSelectionFromTop(index, top);
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                title = (c.getString(c.getColumnIndex("region_title")));
                viewas = (c.getInt(c.getColumnIndex("game_view")));

                Log.d("Pixo", wherestatement);
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    public void randomgame(){
        if(viewas == 0) {
            count = favouritelistView.getCount();
        }else if (viewas == 1){
            count = gamegalleryview.getCount();
        }
        if (count >1) {

            Random rand = new Random();
            randomgame = rand.nextInt(count);

            if(viewas == 0) {
                NesItems gameListItems = (NesItems) favouritelistView.getItemAtPosition(randomgame);//get the position of the item on the list
                itemId = gameListItems.getItemId();//get the item id
            }else if (viewas == 1){
                NesItems gameListItems = (NesItems) gamegalleryview.getItemAtPosition(randomgame);//get the position of the item on the list
                itemId = gameListItems.getItemId();//get the item id
            }



            final Dialog randomgame = new Dialog(FavouriteGames.this);//sets up the dialog
            randomgame.requestWindowFeature(Window.FEATURE_NO_TITLE);
            randomgame.setContentView(R.layout.random_game);//sets up the layout of the dialog box

            Button cancel = (Button) randomgame.findViewById(R.id.btnCancel);
            TextView title = (TextView) randomgame.findViewById(R.id.lblTitle);
            ImageView cover = (ImageView) randomgame.findViewById(R.id.imgGameCover);

            //randomgame.setTitle("Random game");
            SQLiteDatabase db;//sets up the connection to the database
            db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
            Cursor c = db.rawQuery("SELECT * FROM eu where _id = " + itemId + "", null);//select everything from the database table

            if (c.moveToFirst()) {//move to the first record
                while (!c.isAfterLast()) {//while there are records to read

                    title.setText(c.getString(c.getColumnIndex("name")));
                    String gameimage = (c.getString(c.getColumnIndex("image")));
                    int coverid = FavouriteGames.this.getResources().getIdentifier(gameimage, "drawable", FavouriteGames.this.getPackageName());
                    cover.setImageResource(coverid);
                    c.moveToNext();//move to the next record
                }
                c.close();//close the cursor
            }
            db.close();//close the database
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    randomgame.dismiss();
                }
            });
            randomgame.show();//show the dialog
        }
        else if(count <2){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Sorry you don't have enough games to make a random selection",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}