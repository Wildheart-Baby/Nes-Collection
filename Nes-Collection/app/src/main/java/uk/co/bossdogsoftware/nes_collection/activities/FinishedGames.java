package uk.co.bossdogsoftware.nes_collection.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.adapters.NesCollectionAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;

public class FinishedGames extends AppCompatActivity {

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, sql,listName,searchterm,fieldname, wherestatement, title, currentgroup, licensed;
    String prevgroup = "";
    int readgameid, gameid, index, top, totalResults, viewas;
    ArrayAdapter<CharSequence> adapter;
    //ArrayList<AllGameItems> nesList;
    ListView finishedgamelistView;
    GridView gamegalleryview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }






}
