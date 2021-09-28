package uk.co.pixoveeware.nes_collection;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavouriteGames extends AppCompatActivity {

    ListView favouritelistView;

    String name, readgamename;
    int readgameid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                nesListItems.setName(c.getString(c.getColumnIndex("name")));
                nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                nesListItems.setOwned(c.getInt(c.getColumnIndex("favourite")));

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
    }

}
