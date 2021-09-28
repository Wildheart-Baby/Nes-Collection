package uk.co.pixoveeware.nes_collection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String DB_NAME;
    Context context;

    String searchterm,fieldname, wherestatement, sql, regionselected;
    int pala, palb, us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //File dbfile = new File(this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite");

        ImageButton AllGames = (ImageButton) findViewById(R.id.btnAllGames);
        ImageButton NeededGames = (ImageButton) findViewById(R.id.btnneeded);
        ImageButton OwnedGames = (ImageButton) findViewById(R.id.btnowned);

        ImageButton FavouriteGames = (ImageButton) findViewById(R.id.btnFavouriteGames);
        ImageButton WishList = (ImageButton) findViewById(R.id.btnWishList);
        ImageButton ShelfOrder = (ImageButton) findViewById(R.id.btnShelfOrder);

        ImageButton Stats = (ImageButton) findViewById(R.id.btnStats);
        ImageButton Settings = (ImageButton) findViewById(R.id.btnSettings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Nes Collect");
        AllGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);

            }
        });

        NeededGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NeededGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);

            }
        });

        OwnedGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OwnedGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
            }
       });

        FavouriteGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavouriteGames.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
            }
        });

        WishList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WishList.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
            }
        });

        ShelfOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShelfOrder.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
            }
        });

        Stats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Statistics.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
            }
        });
        Settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
            }
        });


        //checkDataBase();
        //checksetting();
        //gameregion();
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
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT region FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                if(wherestatement.contains("pal_a_release = 1")){setTitle("Pal A Games");}
                else if(wherestatement.contains("pal_b_release = 1")){setTitle("Pal B Games");}
                else if(wherestatement.contains("ntsc_release = 1")){setTitle("US Games");}

                Log.d("Pixo", wherestatement);
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database

        Toast toast = Toast.makeText(getApplicationContext(),
                wherestatement,
                Toast.LENGTH_SHORT);
        toast.show();
    }


}
