package uk.co.pixoveeware.nes_collection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ShelfOrder extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mScreenTitles;

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, sql,listName,searchterm,fieldname, wherestatement, licensed, currentgroup, check;
    String prevgroup = "";
    int readgameid, gameid, totalgames, neededgames, index, top, palAcart, palBcart, uscart, pos, shelf, id, rec, shelfsize;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<NesItems> nesList;
    ListView gamelistView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database
        //wherestatement = getIntent().getStringExtra("wherestatement");

        mTitle = mDrawerTitle = "Nes Collect";
        mScreenTitles = getResources().getStringArray(R.array.screenArray);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mScreenTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setTitle("Shelf order");
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

        gamelistView = (ListView) findViewById(R.id.listView); //sets up a listview with the name shoplistview

        gameregion();
        readList();

        gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(ShelfOrder.this, GameDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                startActivity(intent);//start the new screen
            }
        });

        gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(ShelfOrder.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle("Nes Collect");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_allgames, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(ShelfOrder.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(ShelfOrder.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(ShelfOrder.this, About.class);//opens a new screen when the shopping list is clicked
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
            case 0 :
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


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(pos, true);
        //setTitle(mScreenTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onPause(){
        super.onPause();

        index = gamelistView.getFirstVisiblePosition();
        View v = gamelistView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
    }

    public void readList(){//the readlist function
        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array
        check = "y";
        pos = 1;
        shelf = 1;

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        sql = "SELECT * FROM eu where owned = 1 and onshelf = 1";
        //sql = "SELECT * FROM eu where owned = 0";
        Log.d("Pixo", sql);
        Cursor c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                NesItems nesListItems = new NesItems();//creates a new array
                id = c.getInt(c.getColumnIndex("_id"));

                name = c.getString(c.getColumnIndex("name"));

                palAcart = c.getInt(c.getColumnIndex("pal_a_cart"));
                palBcart = c.getInt(c.getColumnIndex("pal_b_cart"));
                uscart = c.getInt(c.getColumnIndex("ntsc_cart"));
                //Log.d("Pixo", "Name: " + name + " Regions " + palAcart + " " + palBcart + " " + uscart);

                if (check.equals("y")){
                    rec = 0;

                    if (palAcart == 8783){rec ++; Log.d("Pixoif", "first if done");} else {}
                    if (palBcart == 8783){rec ++; Log.d("Pixoif", "second if done");} else {}
                    if (uscart == 8783){rec ++; Log.d("Pixoif", "third if done");} else {}
                    check = "n";
                }

                Log.d("Pixoif", "rec: " + rec);

                if(pos == 1){
                    nesListItems.setGroup("Shelf " + shelf);
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                    nesListItems.setName(c.getString(c.getColumnIndex("name")));
                    nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                    //nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    nesList.add(nesListItems);//add items to the arraylist
                    shelf ++;
                    Log.d("pixo", "added shelf record " + name);

                }
                else {
                    nesListItems.setGroup("no");
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                    nesListItems.setName(c.getString(c.getColumnIndex("name")));
                    nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                    //nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    nesList.add(nesListItems);//add items to the arraylist
                    if (pos == shelfsize){pos = 0;}
                    Log.d("pixo", "added other record " + name);
                }

                if (rec == 1){c.moveToNext(); check = "y"; Log.d("pixoif", "rec:" + rec);}
                else if(rec > 1){ rec = rec - 1; Log.d("pixoif", "rec:" + rec);}
                pos ++;
            }

            c.close();//close the cursor
        }

        db.close();//close the database

        NesCollectionAdapter nes = new NesCollectionAdapter(this, nesList);//set up an new list adapter from the arraylist
        gamelistView.setAdapter(nes);//set the listview with the contents of the arraylist
    }


    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                shelfsize = (c.getInt(c.getColumnIndex("shelf_size")));
                Log.d("Pixo", wherestatement);
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        readList();//run the list tables function
        gamelistView.setSelectionFromTop(index, top);
    }

}

