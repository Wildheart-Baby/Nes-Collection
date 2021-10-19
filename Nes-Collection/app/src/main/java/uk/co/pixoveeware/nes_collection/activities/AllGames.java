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

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.ViewModels.LightGamesViewModel;
import uk.co.pixoveeware.nes_collection.ViewModels.LightGamesViewModelFactory;
import uk.co.pixoveeware.nes_collection.adapters.LightImageCollectionAdapter;
import uk.co.pixoveeware.nes_collection.adapters.NesIndexAdapter;
import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.adapters.LightCollectionAdapter;
import uk.co.pixoveeware.nes_collection.models.AllGameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

import static uk.co.pixoveeware.nes_collection.R.*;

public class AllGames extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Context context = this;

    String readgamename, sql, wherestatement, title, licensed, thename, theimage;
    int readgameid, index, top, viewas, readindexid, titles;

    ListView gamelistView, alphaIndex;
    GridView gamegalleryview;

    ArrayList<GameListItems> gamesList;
    ArrayList<AllGameItems> gameList;
    ArrayList<GameItemsIndex> indexList;
    LightGamesViewModel viewM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //viewM = new ViewModelProvider(this).get(AllGamesViewModel.class);
        //viewM = new LightGamesViewModelFactory(this, "all").getClass(AllGamesViewModel.class);
        viewM = new ViewModelProvider(this, new LightGamesViewModelFactory(this.getApplication(), "all")).get(LightGamesViewModel.class);
        gamesList = viewM.gamesList;
        indexList = viewM.indexList;
        viewas = viewM.viewType;
        setContentView(layout.activity_all_games);

        gamelistView = findViewById(id.lvAllGames); //sets up a listview with the name shoplistview
        gamegalleryview = findViewById(id.gvAllGames);
        alphaIndex = findViewById(id.lvAlphaIndex);

        loadList();

        Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(" " + viewM.regionFlag);
        toolbar.setSubtitle(viewM.gamesCount);
        toolbar.setLogo(context.getResources().getIdentifier(viewM.regionFlag, "drawable", context.getPackageName()));

        DrawerLayout drawer = findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(id.drawer_layout);
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
            case id.action_settings:
                Intent intent = new Intent(AllGames.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case id.action_search:
                Intent intent2 = new Intent(AllGames.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case id.action_about:
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

        index = gamegalleryview.getFirstVisiblePosition();
        }
    }


    public void loadList(){//the readlist function
        if(gameList == null){gamesList = viewM.gamesList;}
        if(indexList == null){indexList = viewM.indexList;}
        if(viewas == 0){
            LightCollectionAdapter nes = new LightCollectionAdapter(this, gamesList);//set up an new list adapter from the arraylist
            gamegalleryview.setVisibility(View.GONE);
            gamelistView.setAdapter(nes);
            NesIndexAdapter nii = new NesIndexAdapter(this, indexList);
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
            });

            gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                    GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                    readgameid = gameListItems.getItemId();//get the name of the shopping list table
                    readgamename = gameListItems.getName();//get the name of the shopping list table
                    //sql = wherestatement + licensed;
                    sql = "all";
                    Intent intent = new Intent(AllGames.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                    intent.putExtra("name", readgamename);//passes the table name to the new screen
                    intent.putExtra("position", arg2);
                    intent.putExtra("listposition", arg2);
                    intent.putExtra("sqlstatement", sql);
                    intent.putExtra("gamename", thename);
                    intent.putExtra("gameimage", theimage);
                    intent.putExtra("listType", "all");

                    startActivity(intent);//start the new screen
                }
            });

            gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                    GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                    final Integer itemId = gameListItems.getItemId();//get the item id

                    Intent intent = new Intent(AllGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("editgameid", itemId);
                    intent.putExtra("listposition", arg2);
                    startActivity(intent);//start the new screen

                    return true;//return is equal to true
                }

            });

        }else if (viewas == 1){
            LightImageCollectionAdapter nes = new LightImageCollectionAdapter(this, gamesList);//set up an new list adapter from the arraylist
            gamelistView.setVisibility(View.GONE);
            gamegalleryview.setAdapter(nes);

            gamegalleryview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                    GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
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

                    GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                    final Integer itemId = gameListItems.getItemId();//get the item id

                    Intent intent = new Intent(AllGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("editgameid", itemId);
                    intent.putExtra("listposition", arg2);
                    startActivity(intent);//start the new screen

                    return true;//return is equal to true
                }

            });
            }
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
        loadList();//run the list tables function
        gamelistView.setSelectionFromTop(index, top);
        //gamegalleryview.setSelection(gridviewVerticalPositionWhenThumbnailTapped);
        gamegalleryview.setSelection(index);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mainpage) {
            Intent intent = new Intent(this, HomeScreenActivity.class);//opens a new screen when the shopping list is clicked
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
