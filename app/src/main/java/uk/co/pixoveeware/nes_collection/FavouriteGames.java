package uk.co.pixoveeware.nes_collection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class FavouriteGames extends AppCompatActivity {

    ListView favouritelistView;
    Context context;

    String name, readgamename, searchterm,fieldname;
    int readgameid, index, top, count, randomgame, itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_games);
        setTitle("Favourite Games");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favouritelistView = (ListView) findViewById(R.id.lvFavouriteGames);

        readList();

        favouritelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(FavouriteGames.this, GameDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                startActivity(intent);//start the new screen
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favouritegames, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_random:
                    randomgame();
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(FavouriteGames.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(FavouriteGames.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
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

        index = favouritelistView.getFirstVisiblePosition();
        View v = favouritelistView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
    }

    public void readList(){//the readlist function
        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM eu where favourite = 1", null);//select everything from the database table
        //Cursor c = db.rawQuery("SELECT * FROM " + fname, null);

        //Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                NesItems nesListItems = new NesItems();//creates a new array
                nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                nesListItems.setGroup(c.getString(c.getColumnIndex("group")));
                nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                nesListItems.setName(c.getString(c.getColumnIndex("name")));
                nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                //nesListItems.setOwned(c.getInt(c.getColumnIndex("favourite")));

                nesList.add(nesListItems);//add items to the arraylist
                c.moveToNext();//move to the next record
            }
            //c.getcount(totalgames);
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);

        db.close();//close the database

        NesCollectionAdapter nes = new NesCollectionAdapter(this, nesList);//set up an new list adapter from the arraylist
        favouritelistView.setAdapter(nes);//set the listview with the contents of the arraylist
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        readList();//run the list tables function
        favouritelistView.setSelectionFromTop(index, top);
    }


    public void randomgame(){
        count = favouritelistView.getAdapter().getCount();
        if (count >2) {

            Random rand = new Random();
            randomgame = rand.nextInt(count);

            NesItems gameListItems = (NesItems) favouritelistView.getItemAtPosition(randomgame);//get the position of the item on the list
            itemId = gameListItems.getItemId();//get the item id

            final Dialog randomgame = new Dialog(FavouriteGames.this);//sets up the dialog
            randomgame.setContentView(R.layout.random_game);//sets up the layout of the dialog box

            Button cancel = (Button) randomgame.findViewById(R.id.btnCancel);
            TextView title = (TextView) randomgame.findViewById(R.id.lblTitle);
            ImageView cover = (ImageView) randomgame.findViewById(R.id.imgGameCover);

            randomgame.setTitle("Random game");
            SQLiteDatabase db;//sets up the connection to the database
            db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
            Cursor c = db.rawQuery("SELECT * FROM eu where _id = " + itemId + "", null);//select everything from the database table

            if (c.moveToFirst()) {//move to the first record
                while (!c.isAfterLast()) {//while there are records to read

                    title.setText(c.getString(c.getColumnIndex("name")));
                    String gameimage = (c.getString(c.getColumnIndex("image")));
                    int coverid = FavouriteGames.this.getResources().getIdentifier(gameimage, "drawable", FavouriteGames.this.getPackageName());
                    cover.setImageResource(coverid);
                    c.moveToNext();//move to the next record
                }
                c.close();//close the cursor
            }
            db.close();//close the database
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    randomgame.dismiss();
                }
            });
            randomgame.show();//show the dialog
        }
        else if(count <2){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Sorry you don't have enough games to make a random selection",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}