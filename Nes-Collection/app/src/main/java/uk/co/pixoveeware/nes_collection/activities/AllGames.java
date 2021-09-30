package uk.co.pixoveeware.nes_collection.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


import uk.co.pixoveeware.nes_collection.adapters.NesCollectionImageAdapter;
import uk.co.pixoveeware.nes_collection.data.DatabaseHelper;
import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.adapters.LightCollectionAdapter;
import uk.co.pixoveeware.nes_collection.adapters.NesOwnedAdapter;
import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

public class AllGames extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Context context = this;
    SQLiteDatabase sqlDatabase;
    Cursor c;
    SQLiteDatabase db;

    private  int gridviewVerticalPositionWhenThumbnailTapped;

    String name, dbfile, readgamename, str, sql,listName,searchterm,fieldname, wherestatement, title, currentgroup, licensed, titlestr, titlept1, titlept2, thename, theimage;
    String prevgroup = "";
    int readgameid, gameid, index, top, viewas, ListSize, i, readindexid, AddItems, totalgames, titles;
    ArrayAdapter<CharSequence> adapter;
    //ArrayList<GameItems> nesList;
    ListView gamelistView, alphaIndex;
    GridView gamegalleryview;
    DatabaseHelper dbh;
    ArrayList<GameListItems> gamesList;
    ArrayList<GameItemsIndex> indexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_games);
        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database
        gamelistView = (ListView) findViewById(R.id.lvAllGames); //sets up a listview with the name shoplistview
        gamegalleryview = (GridView) findViewById(R.id.gvAllGames);
        alphaIndex = (ListView) findViewById(R.id.lvAlphaIndex);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        NesOwnedAdapter.screenwidth = metrics.widthPixels;
        dbh = new DatabaseHelper(this);

        //gameregion();
        readList();

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

        setTitle(" " + dbh.regionTitle());
        toolbar.setSubtitle(dbh.gamesCount("all"));
        toolbar.setLogo(context.getResources().getIdentifier(dbh.regionFlag(), "drawable", context.getPackageName()));

        /*alphaIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GameItemsIndex indexListItems = (GameItemsIndex) arg0.getItemAtPosition(arg2);
                readindexid = indexListItems.getItemid();
                //readindexid = readindexid - 1;
                gamelistView.setSelection(readindexid);
                //gamegalleryview.setSelection(readindexid);
            }
        });

        gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                //sql = wherestatement + licensed;
                sql = "SELECT * FROM eu where " + wherestatement + licensed +  "";
                Intent intent = new Intent(AllGames.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                intent.putExtra("position", arg2);
                intent.putExtra("listposition", arg2);
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("gamename", thename);
                intent.putExtra("gameimage", theimage);

                startActivity(intent);//start the new screen
            }
        });

        gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(AllGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                intent.putExtra("listposition", arg2);
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
                //sql = wherestatement + licensed;
                sql = "SELECT * FROM eu where " + wherestatement + licensed +  "";
                Intent intent = new Intent(AllGames.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                intent.putExtra("position", arg2);
                intent.putExtra("listposition", arg2);
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("currentgamename", thename);
                intent.putExtra("currentgameimage", theimage);
                startActivity(intent);//start the new screen
            }
        });

        gamegalleryview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(AllGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                intent.putExtra("listposition", arg2);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });*/

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
                Intent intent = new Intent(AllGames.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(AllGames.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(AllGames.this, About.class);//opens a new screen when the shopping list is clicked
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
        if(viewas == 0){
        index = gamelistView.getFirstVisiblePosition();
        View v = gamelistView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
        } else if(viewas == 1){

        //gridviewVerticalPositionWhenThumbnailTapped = gamegalleryview.getFirstVisiblePosition();
        index = gamegalleryview.getFirstVisiblePosition();
        //View v = gamegalleryview.getChildAt(0);
        //top = (v == null) ? 0 : v.getTop();*/
        }
    }

    private class GetGamesList extends AsyncTask<ArrayList<GameListItems>, ArrayList<GameListItems>, ArrayList<GameListItems>> {

        @Override
        protected ArrayList<GameListItems> doInBackground(ArrayList<GameListItems>... arrayLists) {
            return dbh.getGameslist("all");
        }

        @Override
        protected void onPostExecute(ArrayList<GameListItems> result) {
            super.onPostExecute(result);
            gamesList = result;
        }

        //gamesList = dbh.getGameslist("all");
        //indexList = dbh.gamesIndex("all");
        //viewas = dbh.viewType();
    }


    public void readList(){//the readlist function
        new GetGamesList().execute();
        //gamesList = dbh.getGameslist("all");
        //indexList = dbh.gamesIndex("all");
        //viewas = dbh.viewType();
        viewas = 0;

        if(viewas == 0){
            LightCollectionAdapter nes = new LightCollectionAdapter(this, gamesList);//set up an new list adapter from the arraylist
            gamegalleryview.setVisibility(View.GONE);
            gamelistView.setAdapter(nes);
            /*NesIndexAdapter nii = new NesIndexAdapter(this, indexList);
            alphaIndex.setAdapter(nii);

            alphaIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    GameItemsIndex indexListItems = (GameItemsIndex) arg0.getItemAtPosition(arg2);
                    readindexid = indexListItems.getItemid();
                    //readindexid = readindexid - 1;
                    gamelistView.setSelection(readindexid);
                    //gamegalleryview.setSelection(readindexid);
                }
            });*/

            gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                    GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                    readgameid = gameListItems.getItemId();//get the name of the shopping list table
                    readgamename = gameListItems.getName();//get the name of the shopping list table
                    //sql = wherestatement + licensed;
                    sql = "SELECT * FROM eu where " + wherestatement + licensed +  "";
                    Intent intent = new Intent(AllGames.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                    intent.putExtra("name", readgamename);//passes the table name to the new screen
                    intent.putExtra("position", arg2);
                    intent.putExtra("listposition", arg2);
                    intent.putExtra("sqlstatement", sql);
                    intent.putExtra("gamename", thename);
                    intent.putExtra("gameimage", theimage);

                    startActivity(intent);//start the new screen
                }
            });

            gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                    GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                    final Integer itemId = gameListItems.getItemId();//get the item id

                    Intent intent = new Intent(AllGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("editgameid", itemId);
                    intent.putExtra("listposition", arg2);
                    startActivity(intent);//start the new screen

                    return true;//return is equal to true
                }

            });

        }/*else if (viewas == 1){
            NesCollectionImageAdapter nes = new NesCollectionImageAdapter(this, gamesList);//set up an new list adapter from the arraylist
            gamelistView.setVisibility(View.GONE);
            gamegalleryview.setAdapter(nes);

            gamegalleryview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                    GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                    readgameid = gameListItems.getItemId();//get the name of the shopping list table
                    readgamename = gameListItems.getName();//get the name of the shopping list table
                    //sql = wherestatement + licensed;
                    sql = "SELECT * FROM eu where " + wherestatement + licensed +  "";
                    Intent intent = new Intent(AllGames.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                    intent.putExtra("name", readgamename);//passes the table name to the new screen
                    intent.putExtra("position", arg2);
                    intent.putExtra("listposition", arg2);
                    intent.putExtra("sqlstatement", sql);
                    intent.putExtra("currentgamename", thename);
                    intent.putExtra("currentgameimage", theimage);
                    startActivity(intent);//start the new screen
                }
            });

            gamegalleryview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                    GameItems gameListItems = (GameItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                    final Integer itemId = gameListItems.getItemId();//get the item id

                    Intent intent = new Intent(AllGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("editgameid", itemId);
                    intent.putExtra("listposition", arg2);
                    startActivity(intent);//start the new screen

                    return true;//return is equal to true
                }

            });
            }*/
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                title = (c.getString(c.getColumnIndex("region_title")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
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
        //gamegalleryview.setSelection(gridviewVerticalPositionWhenThumbnailTapped);
        gamegalleryview.setSelection(index);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mainpage) {
            Intent intent = new Intent(this, MainActivity.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }else if (id == R.id.nav_neededgames) {
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
