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

import android.widget.Toast;

import java.util.ArrayList;

public class GamesDetail extends AppCompatActivity {
    public static int idforgame, favourited, ownedgame, wishlist, finished;
    public static String gamesname;
    public static int listcount = 0, gamedetailcount = 0;
    int gamepos;

    Context context; //sets up a variable as context
    int gameid, editgameid, coverid, owned, pos, position, titles;

    String gamename, sql, wherestatement, licensed, previd, thename, theimage;
    ViewPager viewPager;
    NesPagerAdapter adapter;
    private Menu menu;
    ArrayList<GameItems> nesList;
    GameItems nesListItems;
    View v;
    Cursor c;
    SQLiteDatabase db;//sets up the connection to the database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        previd = String.valueOf(pos);
        //Log.d("pixo height: ", " " + height);
        //Log.d("pixo ", "width: " + width);
        //Log.d("pixo ", "density: " + densityDpi);

        if (width <600){setContentView(R.layout.activity_games_detail_small);} else if (width >599){setContentView(R.layout.activity_games_detail);}

        gamepos = getIntent().getIntExtra("listposition", 0);
        gameid = getIntent().getIntExtra("gameid", 0); //sets a variable fname with data passed from the main screen
        gamename = getIntent().getStringExtra("name");
        pos = getIntent().getIntExtra("position",0);//sets a variable fname with data passed from the main screen
        sql = getIntent().getStringExtra("sqlstatement");
        //sql = "SELECT * FROM eu where " + sqlstate;
        Log.d("Pixo", "list position: " + pos + " game id: " + gameid +  " name: " + gamename);
        //setTitle("@string/gameDetailPageTitle");//sets the screen title with the shopping list name
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

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("param", MainActivity.sqlstatement);
        outState.putInt("position", viewPager.getCurrentItem());
        Log.d("pixo-saved-instance", "SQL: " + MainActivity.sqlstatement + " Pos: " + viewPager.getCurrentItem() );
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        MainActivity.sqlstatement = savedInstanceState.getString("param");
        pos = savedInstanceState.getInt("position");
        Log.d("pixo-saved-instance", "SQL: " + MainActivity.sqlstatement + " Pos: " + pos);
        super.onRestoreInstanceState(savedInstanceState);
        new FillGamesAdapter(this);
        readGame();
    }

    public void readGame(){//the readlist function
        if (MainActivity.nesList == null){ MainActivity.readList(); readGames(); }
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new NesPagerAdapter(this, MainActivity.nesList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
    }

    public void editgame(){
        Intent intent = new Intent(GamesDetail.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
        intent.putExtra("editgameid", idforgame);
        intent.putExtra("listposition", gamepos);
        startActivity(intent);//start the new screen
    }

    public void favouritegame(){
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        //if(favourited == 0) {
        if(MainActivity.nesList.get(viewPager.getCurrentItem()).getFavourite() == 0) {
            str = "UPDATE eu SET favourite = 1 where _id = " + idforgame + " ";
            MainActivity.nesList.get(viewPager.getCurrentItem()).setFavourite(1);


        } else  if(MainActivity.nesList.get(viewPager.getCurrentItem()).getFavourite()  == 1) {
            str = "UPDATE eu SET favourite = 0 where _id = " + idforgame + " ";
            MainActivity.nesList.get(viewPager.getCurrentItem()).setFavourite(0);

        }
        //Toast toast = Toast.makeText(getApplicationContext(), "Favourite no:" + MainActivity.nesList.get(viewPager.getCurrentItem()).getFavourite() + " Name: " + MainActivity.nesList.get(viewPager.getCurrentItem()).getName() , Toast.LENGTH_LONG);
        //toast.show();
        //invalidateOptionsMenu();
        Log.d("Pixo", str);
        db.execSQL(str);//run the sql command
        db.close();//close the database

        pos = viewPager.getCurrentItem();
        readGames();
        invalidateOptionsMenu();

    }

    public void wishlist(){
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
        readGames();
    }

    public void finishedgame(){
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
        readGames();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (MainActivity.nesList == null){ MainActivity.readList(); readGames(); }
        readGames();//run the list tables function
        invalidateOptionsMenu();
        //viewPager.setCurrentItem(pos);
    }

    public void gameregion(){//selects the region from the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                NesPagerAdapter.licensed = (c.getString(c.getColumnIndex("licensed")));

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

    public void readGames() {
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        /*//sql = "SELECT * FROM eu where " + wherestatement + licensed +  "";
        //Log.d("Pixo", sqlstate);
        c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                nesListItems = new GameItems();//creates a new array
                nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                nesListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                nesListItems.setName(c.getString(c.getColumnIndex(thename)));
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
                MainActivity.nesList.add(nesListItems);//add items to the arraylist

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database*/
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new NesPagerAdapter(this, MainActivity.nesList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
    }

}
