package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.adapters.NesCollectionAdapter;
import uk.co.pixoveeware.nes_collection.adapters.NesCollectionImageAdapter;
import uk.co.pixoveeware.nes_collection.adapters.NesOwnedAdapter;
import uk.co.pixoveeware.nes_collection.models.GameItems;

public class SearchResults extends AppCompatActivity {

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, listName,currentDate, pagetitle, search, field, searchname, columnname,sql, currentgroup, thename, theimage;
    String prevgroup = "";
    int readgameid, gameid, totalResults, viewas, titles;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<GameItems> nesList;
    ListView gamelistView;
    TextView thesearchresults;
    GridView gamegalleryview;
    SQLiteDatabase db;//sets up the connection to the database
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        thesearchresults = (TextView) findViewById(R.id.lblSearchResults);
        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database
        search = getIntent().getStringExtra("searchname"); //sets a variable fname with data passed from the main screen
        field = getIntent().getStringExtra("columnname"); //sets a variable fname with data passed from the main screen
        sql = getIntent().getStringExtra("sqlstatement");
        Log.d("Pixo search", "value: " + sql);
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

        /*MobileAds.initialize(getApplicationContext(), "ca-app-pub-0537596348696744~2585816192");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        gameregion();
        readList();

        gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list


                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(SearchResults.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
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

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(SearchResults.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
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
                Intent intent = new Intent(SearchResults.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
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

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(SearchResults.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
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
                Intent intent = new Intent(SearchResults.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(SearchResults.this, Search.class);//opens a new screen when the shopping list is clicked
                finish();
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(SearchResults.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void readList(){//the readlist function
        if (MainActivity.nesList == null){ MainActivity.readList(); }
        MainActivity.nesList.clear();//clear the shoppingList array

        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        c = db.rawQuery(sql, null);//select everything from the database table

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
                        MainActivity.nesList.add(nesListItems);//add items to the arraylist
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
                        MainActivity.nesList.add(nesListItems);//add items to the arraylist
                        prevgroup = c.getString(c.getColumnIndex("groupheader"));
                    }
                    c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }

        c = db.rawQuery(sql, null);
        totalResults = c.getCount();
        db.close();//close the database

        if (totalResults == 0){
           gamelistView.setVisibility(View.GONE);
           thesearchresults.setVisibility(View.VISIBLE);
        }else {if(viewas == 0){
            NesCollectionAdapter nes = new NesCollectionAdapter(this, MainActivity.nesList);//set up an new list adapter from the arraylist
            gamegalleryview.setVisibility(View.GONE);
            gamelistView.setAdapter(nes);

        }else if (viewas == 1){
            NesCollectionImageAdapter nes = new NesCollectionImageAdapter(this, MainActivity.nesList);//set up an new list adapter from the arraylist
            gamelistView.setVisibility(View.GONE);
            gamegalleryview.setAdapter(nes);
            }
        }
    }

    public void gameregion(){//selects the region from the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                viewas = (c.getInt(c.getColumnIndex("game_view")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
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

    @Override
    public void onRestart() {
        super.onRestart();
        readList();//run the list tables function
    }

}
