package uk.co.bossdogsoftware.nes_collection.activities;

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

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.adapters.NesCollectionAdapter;
import uk.co.bossdogsoftware.nes_collection.adapters.NesOwnedAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;

public class SearchResults extends AppCompatActivity {

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, listName,currentDate, pagetitle, search, field, searchname, columnname,sql, currentgroup, thename, theimage;
    String prevgroup = "";
    int readgameid, gameid, totalResults, viewas, titles;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<AllGameItems> nesList;
    ListView gamelistView;
    TextView thesearchresults;
    GridView gamegalleryview;
    SQLiteDatabase db;//sets up the connection to the database
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }






}
