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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OwnedGames extends AppCompatActivity {

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, sql,listName,searchterm,fieldname, wherestatement;
    int readgameid, gameid, ownedgames, totalgames;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<NesItems> nesList;
    ListView ownedlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_games);
        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database
        wherestatement = getIntent().getStringExtra("wherestatement");
        setTitle("Owned Games");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ownedlistView = (ListView) findViewById(R.id.lvOwnedGames); //sets up a listview with the name shoplistview
        //gameregion();
        readList();

        ownedlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

        NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
        readgameid = gameListItems.getItemId();//get the name of the shopping list table
        readgamename = gameListItems.getName();//get the name of the shopping list table
        Intent intent = new Intent(OwnedGames.this, GameDetail.class);//opens a new screen when the shopping list is clicked
        intent.putExtra("gameid", readgameid);//passes the table name to the new screen
        intent.putExtra("name", readgamename);//passes the table name to the new screen
        startActivity(intent);//start the new screen
        }
    });



    ownedlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

            NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
            final Integer itemId = gameListItems.getItemId();//get the item id

            Intent intent = new Intent(OwnedGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
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
                search();
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
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database

        sql = "SELECT * FROM eu where owned = 1 and " + wherestatement + "";

        Toast toast = Toast.makeText(getApplicationContext(),
                sql,
                Toast.LENGTH_SHORT);
        toast.show();

        Cursor c = db.rawQuery(sql, null);//select everything from the database table
        //Cursor c = db.rawQuery("SELECT * FROM " + fname, null);

        //Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                NesItems nesListItems = new NesItems();//creates a new array
                nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                nesListItems.setImage(c.getString(c.getColumnIndex("image")));
                nesListItems.setName(c.getString(c.getColumnIndex("name")));
                nesListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                nesListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                nesListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                nesListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                nesListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                nesListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                nesListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                nesListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                nesListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                //nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));

                nesList.add(nesListItems);//add items to the arraylist
                c.moveToNext();//move to the next record
            }
            ownedgames = c.getCount();
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);

        c = db.rawQuery("SELECT * FROM eu where " + wherestatement + "", null);
        totalgames = c.getCount();
        c.close();
        db.close();//close the database
        str = "You own " + ownedgames + " of the " + totalgames + " games";
        TextView gamesfooter = (TextView) findViewById(R.id.lblTotal);
        gamesfooter.setText(str);

        NesOwnedAdapter nes = new NesOwnedAdapter(this, nesList);//set up an new list adapter from the arraylist
        ownedlistView.setAdapter(nes);//set the listview with the contents of the arraylist
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT region FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                Log.d("Pixo", wherestatement);

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    public void search(){

        final Dialog dialog = new Dialog(context);//sets up the dialog
        dialog.setContentView(R.layout.searchform_elements);//sets up the layout of the dialog box

        final Spinner field = (Spinner) dialog.findViewById(R.id.field_name);
        final TextView searchTerm = (TextView) dialog.findViewById(R.id.txtSearch);//sets up the dialog title
        final Button ok = (Button) dialog.findViewById(R.id.btnOk);
        final Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

        String[] fieldnames = {"name", "publisher", "genre", "subgenre" , "developer", "synopsis"};
        ArrayAdapter<String> wsaa1 = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, fieldnames);
        wsaa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        field.setAdapter(wsaa1);

        field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fieldname = (String) field.getSelectedItem();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });

        dialog.setTitle("Search games");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//read the quantity from the quantity text box and adds one to the total
                searchterm = String.valueOf(searchTerm.getText().toString());//get the name from the item name text box
                Intent intent = new Intent(OwnedGames.this, SearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("columnname", fieldname);//passes the table name to the new screen
                intent.putExtra("searchname", searchterm);//passes the table name to the new screen
                startActivity(intent);//start the new screen
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//read the quantity from the quantity text box and adds one to the total
                dialog.dismiss();
            }
        });

        dialog.show();//show the dialog

    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        readList();//run the list tables function
    }

}
