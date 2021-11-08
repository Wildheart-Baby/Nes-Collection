package uk.co.bossdogsoftware.nes_collection.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.MenuItem;

import uk.co.bossdogsoftware.nes_collection.Fragments.AllGamesFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.FavouriteGamesFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.GamesDetailFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.HomeScreenFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.NeededGamesFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.OwnedGamesFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.SearchBoxFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.SearchResultsFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.SettingsFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.ShelfOrderFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.StatisticsFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.WishlistFragment;
import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModelFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchBoxFragment.SearchListener {

    public AllGamesViewModel viewM;
    public Toolbar toolbar;
    GamesDetailFragment fragment;
    FragmentManager frg;
    SearchResultsFragment searchFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewM = new ViewModelProvider(this, new AllGamesViewModelFactory(this.getApplication(), "all")).get(AllGamesViewModel.class);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        frg = getSupportFragmentManager();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, HomeScreenFragment.newInstance(), "homeScreen")
                    .addToBackStack("homeScreen")
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            /*FragmentManager manager = getSupportFragmentManager();
            //manager.popBackStackImmediate()
            if (manager.getBackStackEntryCount() > 1 ) {
                manager.popBackStackImmediate();
            }*/
            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_allgames, menu);
        return true;
    }*/



    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(MainActivity.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(MainActivity.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_allgames) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .add(R.id.container, AllGamesFragment.newInstance(), "gamesList")
                    .addToBackStack("gamesList")
                    .commit();
        } else if (id == R.id.nav_neededgames) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .add(R.id.container, NeededGamesFragment.newInstance(), "gamesList")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_ownedgames) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .add(R.id.container, OwnedGamesFragment.newInstance(), "gamesList")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_favouritegames) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .add(R.id.container, FavouriteGamesFragment.newInstance("",""), "gamesList")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_wishlist) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .add(R.id.container, WishlistFragment.newInstance("",""), "gamesList")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_shelforder) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .add(R.id.container, ShelfOrderFragment.newInstance("", ""), "gamesList")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_statistics) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .add(R.id.container, StatisticsFragment.newInstance("", ""), "gamesList")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_finished) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .add(R.id.container, FavouriteGamesFragment.newInstance("", ""), "gamesList")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_settings) {
            Fragment f = frg.findFragmentByTag("gamesList");
            frg.beginTransaction()
                    .remove(f)
                    .commit();
            frg.popBackStack();
            frg.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance("", ""), "gamesList")
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onReceiveSearch(String searchType, String searchString) {
        searchFrag = (SearchResultsFragment) getSupportFragmentManager().findFragmentByTag("SearchResults"); //sets up a reference to the search results fragment
        //sets the search term to the searchString variable
        if (searchFrag != null) {                                                                            //if the fragment exists on the screen
            searchFrag.searchString(searchType, searchString);                                                              //pass the search term to the search places function in the search results fragment
        }
    }
}