package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class GamesDetail extends AppCompatActivity {
    public static int idforgame, favourited, ownedgame, wishlist, finished;
    public static String gamesname;
    public static int listcount = 0, gamedetailcount = 0;

    Context context; //sets up a variable as context
    int gameid, editgameid, coverid, owned, carttrue, boxtrue, manualtrue, favourite, pos;

    String covername, gamename, headers, sql, wherestatement, licensed, sqlstate;
    ViewPager viewPager;
    NesPagerAdapter adapter;
    private Menu menu;
    ArrayList<NesItems> nesList;
    NesItems nesListItems;
    View v;
    TextView thegameid;
    ImageView flagAustralia, flagFrance, flagGermany, flagUK, flagUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        if (width <600){setContentView(R.layout.activity_games_detail_small);} else if (width >599){setContentView(R.layout.activity_games_detail);}


        gameid = getIntent().getIntExtra("gameid", 0); //sets a variable fname with data passed from the main screen
        gamename = getIntent().getStringExtra("name");
        pos = getIntent().getIntExtra("position",0);//sets a variable fname with data passed from the main screen
        sql = getIntent().getStringExtra("sqlstatement");
        //sql = "SELECT * FROM eu where " + sqlstate;
        Log.d("pixowned", "Value game id: " + gameid + " position: " + pos + " name: " + gamename);
        //thegameid = (TextView) findViewById(R.id.lblGameid);
        setTitle("Game Details");//sets the screen title with the shopping list name
        gameregion();
        readGame();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*flagAustralia = (ImageView) findViewById(R.id.imgAustralia);
        flagFrance = (ImageView) findViewById(R.id.imgFrance);
        flagGermany = (ImageView) findViewById(R.id.imgGermany);
        flagUK = (ImageView) findViewById(R.id.imgUK);
        flagUS = (ImageView) findViewById(R.id.imgUS);

        flagAustralia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_australia = 1" + licensed + ")";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(GamesDetail.this, StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Australian games");
                startActivity(intent);//start the new screen

            }
        });*/

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {invalidateOptionsMenu();}
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {invalidateOptionsMenu();}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gamedetails, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem fav = menu.findItem(R.id.action_favourite);
        MenuItem own = menu.findItem(R.id.action_edit);
        if(favourited == 1){ fav.setIcon(R.drawable.ic_heart_white_24dp); }else if(favourited == 0){ fav.setIcon(R.drawable.ic_favorite_border_white_24dp);}
            if(ownedgame == 0){own.setIcon(R.drawable.ic_add_game24dp);} else if(ownedgame == 1){own.setIcon(R.drawable.ic_edit_white_24dp);}
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_edit:
                editgame();
                return true;

            case R.id.action_favourite:
                favouritegame();
                return true;

            case R.id.action_finishedgame:
                finishedgame();
                return true;

            case R.id.action_wishlist:
                wishlist();
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(GamesDetail.this, About.class);//opens a new screen when the shopping list is clicked
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
        pos = viewPager.getCurrentItem();
    }

    public void readGame(){//the readlist function
        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array
        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        //sql = "SELECT * FROM eu where " + wherestatement + licensed +  "";
        //Log.d("Pixo", sqlstate);
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                nesListItems = new NesItems();//creates a new array
                nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                nesListItems.setName(c.getString(c.getColumnIndex("name")));
                nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                nesListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                nesListItems.setBox(c.getInt(c.getColumnIndex("box")));
                nesListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                nesListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                nesListItems.setYear(c.getString(c.getColumnIndex("year")));
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
                nesListItems.setScandinavia(c.getInt(c.getColumnIndex("flag_scandinavia")));
                nesListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                nesListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                nesListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                nesListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                nesListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));
                nesList.add(nesListItems);//add items to the arraylist

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new NesPagerAdapter(this, nesList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);

    }

    public void editgame(){
        Intent intent = new Intent(GamesDetail.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
        intent.putExtra("editgameid", idforgame);
        startActivity(intent);//start the new screen
    }

    public void favouritegame(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        if(favourited == 0) {
            str = "UPDATE eu SET favourite = 1 where _id = " + idforgame + " "; //update the database basket field with 8783
        } else  if(favourited == 1) {
            str = "UPDATE eu SET favourite = 0 where _id = " + idforgame + " "; //update the database basket field with 8783
        }
        Log.d("Pixo", str);
        db.execSQL(str);//run the sql command
        db.close();//close the database
        pos = viewPager.getCurrentItem();
        readGame();
        invalidateOptionsMenu();
    }

    public void wishlist(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        if (ownedgame == 1){Toast toast = Toast.makeText(getApplicationContext(),
                "You already own this game",
                Toast.LENGTH_SHORT);
            toast.show();}
        else if (ownedgame == 0) {

            if (wishlist == 0) {
                str = "UPDATE eu SET wishlist = 1 where _id = " + idforgame + " "; //update the database basket field with 8783
                db.execSQL(str);//run the sql command
                Toast toast = Toast.makeText(getApplicationContext(),
                        gamesname + " added to wishlist",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else if (wishlist == 1) {
                str = "UPDATE eu SET wishlist = 0 where _id = " + idforgame + " "; //update the database basket field with 8783
                db.execSQL(str);//run the sql command
                Toast toast = Toast.makeText(getApplicationContext(),
                        gamesname + " removed from wish list",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        db.close();//close the database
        pos = viewPager.getCurrentItem();
        readGame();
    }

    public void finishedgame(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        if (finished == 0) {
            str = "UPDATE eu SET finished_game = 1 where _id = " + idforgame + " "; //update the database basket field with 8783
            db.execSQL(str);//run the sql command
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You finished " + gamesname,
                    Toast.LENGTH_SHORT);
            toast.show();
        } else if (finished == 1) {
            str = "UPDATE eu SET finished_game = 0 where _id = " + idforgame + " "; //update the database basket field with 8783
            db.execSQL(str);//run the sql command
            Toast toast = Toast.makeText(getApplicationContext(),
                    gamesname + " removed from finished games",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        db.close();//close the database
        pos = viewPager.getCurrentItem();
        readGame();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        readGame();//run the list tables function
        invalidateOptionsMenu();
        viewPager.setCurrentItem(pos);
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                NesPagerAdapter.licensed = (c.getString(c.getColumnIndex("licensed")));

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

}
