package uk.co.bossdogsoftware.nes_collection.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameItemsIndex;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String DB_NAME;
    Context context;

    String wherestatement;
    public static int width, totalGames, viewas;

    public static ArrayList<AllGameItems> gamesList;
    public static ArrayList<GameItemsIndex> indexList;
    public static String sqlstatement;

    //AllGamesViewModel viewM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //viewM = new ViewModelProvider(this).get(AllGamesViewModel.class);
        //gamesList = viewM.GetGamesDetails("all");
        //indexList = viewM.GetIndex("all");

        setContentView(R.layout.activity_main);
        ImageButton AllGames = (ImageButton) findViewById(R.id.btnAllGames);
        ImageButton NeededGames = (ImageButton) findViewById(R.id.btnneeded);
        ImageButton OwnedGames = (ImageButton) findViewById(R.id.btnowned);

        ImageButton FavouriteGames = (ImageButton) findViewById(R.id.btnFavouriteGames);
        ImageButton WishList = (ImageButton) findViewById(R.id.btnWishList);
        ImageButton ShelfOrder = (ImageButton) findViewById(R.id.btnShelfOrder);

        ImageButton Stats = (ImageButton) findViewById(R.id.btnStats);
        ImageButton Finished = (ImageButton) findViewById(R.id.btnFinished);
        ImageButton Settings = (ImageButton) findViewById(R.id.btnSettings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("Nes Collection");



        NeededGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, NeededGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);

            }
        });

        OwnedGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, OwnedGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
            }
       });

        FavouriteGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, FavouriteGames.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
            }
        });

        WishList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, WishList.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
            }
        });

        ShelfOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ShelfOrder.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
            }
        });

        Stats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, Statistics.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
            }
        });

        Finished.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                    Intent intent = new Intent(HomeScreenActivity.this, FinishedGames.class);//opens a new screen when the shopping list is clicked
                    startActivity(intent);
            }
        });

        Settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

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
                Intent intent = new Intent(HomeScreenActivity.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(HomeScreenActivity.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(HomeScreenActivity.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_allgames) {

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
