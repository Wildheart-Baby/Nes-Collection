package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class GamesDetail extends AppCompatActivity {
    public static int idforgame;
    Context context; //sets up a variable as context
    int gameid, editgameid, coverid, owned, carttrue, boxtrue, manualtrue, favourite, wishlist, finished, pos;

    String covername, gamename, headers, sql, wherestatement, licensed;
    ViewPager viewPager;
    NesPagerAdapter adapter;
    private Menu menu;
    ArrayList<NesItems> nesList;
    AdapterView arg0;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_detail);
        gameid = getIntent().getIntExtra("gameid", 0); //sets a variable fname with data passed from the main screen
        gamename = getIntent().getStringExtra("name");
        pos = getIntent().getIntExtra("position",0);//sets a variable fname with data passed from the main screen

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
        if(favourite == 1){ fav.setIcon(R.drawable.ic_heart_red_24dp); }
        if(owned == 0){own.setIcon(R.drawable.ic_add_game24dp);}
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

    public void readGame(){//the readlist function
        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        sql = "SELECT * FROM eu where " + wherestatement + licensed +  "";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                NesItems nesListItems = new NesItems();//creates a new array
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
                //nesListItems.setWi(c.getInt(c.getColumnIndex("wishlist")));
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
        //viewPager.getChildAt(1);
        //pos = viewPager.getCurrentItem();
        //Log.d("Pixo", "position: " + viewPager.getCurrentItem());
        //NesItems nesListItems = nesList.get(pos);
        //adapter.yourListItens.get(myViewPager.getCurrentItem());
        //NesItems nesListItems = nesList.getItemAtPosition(pos);//read the item at the list position that has been clicked
        //NesItems nesListItems = (NesItems) arg0.getItemAtPosition(viewPager.getCurrentItem());
        //idforgame = nesListItems.getItemId();
        Log.d("Pixo", "Value: " + idforgame);
        Intent intent = new Intent(GamesDetail.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
        intent.putExtra("editgameid", idforgame);
        startActivity(intent);//start the new screen
    }

    public void favouritegame(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        if(favourite == 0) {
            str = "UPDATE eu SET favourite = 1 where _id = " + gameid + " "; //update the database basket field with 8783
        } else  if(favourite == 1) {
            str = "UPDATE eu SET favourite = 0 where _id = " + gameid + " "; //update the database basket field with 8783
        }
        Log.d("Pixo", str);
        db.execSQL(str);//run the sql command
        readGame();
        invalidateOptionsMenu();
    }

    public void wishlist(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        if (owned == 1){Toast toast = Toast.makeText(getApplicationContext(),
                "You already own this game",
                Toast.LENGTH_SHORT);
            toast.show();}
        else if (owned == 0) {

            if (wishlist == 0) {
                str = "UPDATE eu SET wishlist = 1 where _id = " + gameid + " "; //update the database basket field with 8783
                db.execSQL(str);//run the sql command
                Toast toast = Toast.makeText(getApplicationContext(),
                        gamename + " added to wishlist",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else if (wishlist == 1) {
                str = "UPDATE eu SET wishlist = 0 where _id = " + gameid + " "; //update the database basket field with 8783
                db.execSQL(str);//run the sql command
                Toast toast = Toast.makeText(getApplicationContext(),
                        gamename + " removed from wish list",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        //Log.d("Pixo", str);
        readGame();
        //invalidateOptionsMenu();
    }

    public void finishedgame(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        if (finished == 0) {
            str = "UPDATE eu SET finished_game = 1 where _id = " + gameid + " "; //update the database basket field with 8783
            db.execSQL(str);//run the sql command
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You finished " + gamename,
                    Toast.LENGTH_SHORT);
            toast.show();
        } else if (finished == 1) {
            str = "UPDATE eu SET finished_game = 0 where _id = " + gameid + " "; //update the database basket field with 8783
            db.execSQL(str);//run the sql command
            Toast toast = Toast.makeText(getApplicationContext(),
                    gamename + " removed from finished games",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        readGame();
    }


    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here

        readGame();//run the list tables function
        invalidateOptionsMenu();
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                licensed = (c.getString(c.getColumnIndex("licensed")));

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }
}
