package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameDetail extends AppCompatActivity {

    Context context; //sets up a variable as context
    int gameid, editgameid, coverid, owned, carttrue, boxtrue, manualtrue, favourite, wishlist, finished;
    String covername, gamename, headers;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        gameid = getIntent().getIntExtra("gameid", 0); //sets a variable fname with data passed from the main screen
        gamename = getIntent().getStringExtra("name"); //sets a variable fname with data passed from the main screen
        //ImageView cover = (ImageView) findViewById(R.id.imgCover);
        //TextView detail = (TextView) findViewById(R.id.lblDetail);
        //TextView synopsis = (TextView) findViewById(R.id.lblSynopsis);

        setTitle("Game Details");//sets the screen title with the shopping list name
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
                Intent intent3 = new Intent(GameDetail.this, About.class);//opens a new screen when the shopping list is clicked
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
        TextView gamename = (TextView) findViewById(R.id.lblGameName);
        ImageView cover = (ImageView) findViewById(R.id.imgGameCover);
        TextView genre = (TextView) findViewById(R.id.lblGenre);
        TextView subgenre = (TextView) findViewById(R.id.lblSubgenre);
        TextView publisher = (TextView) findViewById(R.id.lblPublisher);
        TextView developer = (TextView) findViewById(R.id.lblDeveloper);
        TextView year = (TextView) findViewById(R.id.lblYear);
        ImageView cart = (ImageView) findViewById(R.id.imgCart);
        ImageView box = (ImageView) findViewById(R.id.imgBox);
        ImageView manual = (ImageView) findViewById(R.id.imgManual);

        TextView synopsis = (TextView) findViewById(R.id.lblSynopsis);

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM eu where _id = '" + gameid + "' ", null);//select everything from the database table
        //Cursor c = db.rawQuery("SELECT * FROM " + fname, null);

        //Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                editgameid = (c.getInt(c.getColumnIndex("_id")));
                covername = (c.getString(c.getColumnIndex("image")));
                owned = (c.getInt(c.getColumnIndex("owned")));
                carttrue = (c.getInt(c.getColumnIndex("cart")));
                boxtrue = (c.getInt(c.getColumnIndex("box")));
                manualtrue = (c.getInt(c.getColumnIndex("manual")));
                favourite = (c.getInt(c.getColumnIndex("favourite")));
                wishlist = (c.getInt(c.getColumnIndex("wishlist")));
                coverid = getResources().getIdentifier(covername, "drawable", getPackageName());
                finished = (c.getInt(c.getColumnIndex("finished_game")));
                gamename.setText((c.getString(c.getColumnIndex("name"))));
                cover.setImageResource(coverid);
                genre.setText((c.getString(c.getColumnIndex("genre"))));
                subgenre.setText((c.getString(c.getColumnIndex("subgenre"))));
                publisher.setText((c.getString(c.getColumnIndex("publisher"))));
                developer.setText((c.getString(c.getColumnIndex("developer"))));
                year.setText((c.getString(c.getColumnIndex("year"))));
                synopsis.setText((c.getString(c.getColumnIndex("synopsis"))));

                if(owned == 1){
                    if(carttrue == 1){
                        cart.setVisibility(View.VISIBLE);
                    }else{}
                    if(boxtrue == 1){
                        box.setVisibility(View.VISIBLE);
                    }else{}
                    if(manualtrue == 1){
                        manual.setVisibility(View.VISIBLE);
                    } else{}
                }
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    public void editgame(){

        Intent intent = new Intent(GameDetail.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
        intent.putExtra("editgameid", editgameid);
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

}
