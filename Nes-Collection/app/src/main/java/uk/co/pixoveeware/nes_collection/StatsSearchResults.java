package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class StatsSearchResults extends AppCompatActivity {

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, listName,currentDate, pagetitle, search, field, searchname, columnname,sql, currentgroup;
    String prevgroup = "";
    int readgameid, gameid, totalResults, viewas;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<NesItems> nesList;
    ListView gamelistView;
    TextView thesearchresults;
    GridView gamegalleryview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        thesearchresults = (TextView) findViewById(R.id.lblSearchResults);
        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database
        search = getIntent().getStringExtra("searchname"); //sets a variable fname with data passed from the main screen
        field = getIntent().getStringExtra("columnname"); //sets a variable fname with data passed from the main screen
        sql = getIntent().getStringExtra("sqlstatement");
        searchname = getIntent().getStringExtra("searchterm");
        pagetitle = getIntent().getStringExtra("pagetitle");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(pagetitle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        NesOwnedAdapter.screenwidth = metrics.widthPixels;

        gamelistView = (ListView) findViewById(R.id.listView); //sets up a listview with the name shoplistview
        gamegalleryview = (GridView) findViewById(R.id.gvAllGames);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-0537596348696744~2585816192");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        gameregion();
        readList();

        gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list


                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(StatsSearchResults.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("position", arg2);
                startActivity(intent);//start the new screen
            }
        });

        gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(StatsSearchResults.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
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
                Intent intent = new Intent(StatsSearchResults.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("position", arg2);
                startActivity(intent);//start the new screen
            }
        });

        gamegalleryview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(StatsSearchResults.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });

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
                Intent intent = new Intent(StatsSearchResults.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(StatsSearchResults.this, Search.class);//opens a new screen when the shopping list is clicked
                finish();
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(StatsSearchResults.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void readList(){//the readlist function
        Log.d("Pixo", sql);

        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        //sql = "SELECT * FROM eu WHERE " + field + " like '%" + search + "%'";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                NesItems nesListItems = new NesItems();//creates a new array
                currentgroup = c.getString(c.getColumnIndex("groupheader"));

                if(!currentgroup.equals(prevgroup)){
                    nesListItems.setGroup(c.getString(c.getColumnIndex("groupheader")));
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                    nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    nesListItems.setName(c.getString(c.getColumnIndex("name")));
                    nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                    nesList.add(nesListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex("groupheader"));
                }
                else if(currentgroup.equals(prevgroup)){
                    nesListItems.setGroup("no");
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                    nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    nesListItems.setName(c.getString(c.getColumnIndex("name")));
                    nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                    nesList.add(nesListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex("groupheader"));
                }
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);
        c = db.rawQuery(sql, null);
        totalResults = c.getCount();
        db.close();//close the database

        if (totalResults == 0){
            gamelistView.setVisibility(View.GONE);
            thesearchresults.setVisibility(View.VISIBLE);
        }else {if(viewas == 0){
            StatsCollectionAdapter nes = new StatsCollectionAdapter(this, nesList);//set up an new list adapter from the arraylist
            gamegalleryview.setVisibility(View.GONE);
            gamelistView.setAdapter(nes);

        }else if (viewas == 1){
            StatsCollectionImageAdapter nes = new StatsCollectionImageAdapter(this, nesList);//set up an new list adapter from the arraylist
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

                viewas = (c.getInt(c.getColumnIndex("game_view")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    @Override
    public void onRestart() {
        super.onRestart();
        readList();//run the list tables function
    }

}
