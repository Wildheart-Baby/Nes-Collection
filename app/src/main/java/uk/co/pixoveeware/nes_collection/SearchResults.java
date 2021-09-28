package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, listName,currentDate, search, field, searchname, columnname,sql;
    int readgameid, gameid;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<NesItems> nesList;
    ListView gamelistView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_games);
        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database
        search = getIntent().getStringExtra("searchname"); //sets a variable fname with data passed from the main screen
        field = getIntent().getStringExtra("columnname"); //sets a variable fname with data passed from the main screen
        Toolbar allgames_toolbar = (Toolbar) findViewById(R.id.allgames_toolbar);
        setSupportActionBar(allgames_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gamelistView = (ListView) findViewById(R.id.lvAllGames); //sets up a listview with the name shoplistview
        readList();

        gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list


                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(SearchResults.this, GameDetail.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                intent.putExtra("name", readgamename);//passes the table name to the new screen
                startActivity(intent);//start the new screen
            }
        });

        gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                Intent intent = new Intent(SearchResults.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });

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
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_search:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void readList(){//the readlist function
        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        sql = "SELECT * FROM eu WHERE " + field + " like '%" + search + "%'";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record

                while ( !c.isAfterLast() ) {//while there are records to read
                NesItems nesListItems = new NesItems();//creates a new array
                nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                nesListItems.setName(c.getString(c.getColumnIndex("name")));
                nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));

                nesList.add(nesListItems);//add items to the arraylist
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);
        db.close();//close the database

        NesCollectionAdapter nes = new NesCollectionAdapter(this, nesList);//set up an new list adapter from the arraylist
        gamelistView.setAdapter(nes);//set the listview with the contents of the arraylist
    }



}
