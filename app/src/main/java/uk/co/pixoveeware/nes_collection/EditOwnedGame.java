package uk.co.pixoveeware.nes_collection;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditOwnedGame extends AppCompatActivity {

    int gameid, coverid, palAcart, palAbox, palAmanual, palBcart, palBbox, palBmanual, uscart, usbox, usmanual, cart, box, manual, owned, regionatrue, regionbtrue, regionustrue, favourite;
    String covername, sql, test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_owned_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gameid = getIntent().getIntExtra("editgameid", 0);

        Button ok = (Button) findViewById(R.id.btnOk);
        Button cancel = (Button) findViewById(R.id.btnCancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writegame();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setTitle("What do you own");
        readGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editgame, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem fav = menu.findItem(R.id.action_favourite);

        if(favourite == 1){ fav.setIcon(R.drawable.ic_heart_red_24dp); }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favourite:
                favouritegame();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



    public void readGame() {//the readlist function
        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array
        TextView gamename = (TextView) findViewById(R.id.lblGame);
        ImageView cover = (ImageView) findViewById(R.id.imgCover);


        CheckBox chkpalacart = (CheckBox) findViewById(R.id.chkPalACart);
        CheckBox chkpalabox = (CheckBox) findViewById(R.id.chkPalABox);
        CheckBox chkpalamanual = (CheckBox) findViewById(R.id.chkPalAmanual);
        CheckBox chkpalbcart = (CheckBox) findViewById(R.id.chkPalBCart);
        CheckBox chkpalbbox = (CheckBox) findViewById(R.id.chkPalBBox);
        CheckBox chkpalbmanual = (CheckBox) findViewById(R.id.chkPalBmanual);
        CheckBox chkuscart = (CheckBox) findViewById(R.id.chkUSCart);
        CheckBox chkusbox = (CheckBox) findViewById(R.id.chkUSBox);
        CheckBox chkusmanual = (CheckBox) findViewById(R.id.chkUSmanual);

        //TextView synopsis = (TextView) findViewById(R.id.lblSynopsis);

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        sql = "SELECT * FROM eu where _id = '" + gameid + "' ";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while (!c.isAfterLast()) {//while there are records to read

                covername = (c.getString(c.getColumnIndex("image")));
                coverid = getResources().getIdentifier(covername, "drawable", getPackageName());
                gamename.setText((c.getString(c.getColumnIndex("name"))));
                cover.setImageResource(coverid);

                palAcart = (c.getInt(c.getColumnIndex("pal_a_cart")));
                palAbox = (c.getInt(c.getColumnIndex("pal_a_box")));
                palAmanual = (c.getInt(c.getColumnIndex("pal_a_manual")));

                palBcart = (c.getInt(c.getColumnIndex("pal_b_cart")));
                palBbox = (c.getInt(c.getColumnIndex("pal_b_box")));
                palBmanual = (c.getInt(c.getColumnIndex("pal_b_manual")));

                uscart = (c.getInt(c.getColumnIndex("ntsc_cart")));
                usbox = (c.getInt(c.getColumnIndex("ntsc_box")));
                usmanual = (c.getInt(c.getColumnIndex("ntsc_manual")));

                regionatrue = (c.getInt(c.getColumnIndex("pal_a_release")));
                regionbtrue = (c.getInt(c.getColumnIndex("pal_b_release")));
                regionustrue = (c.getInt(c.getColumnIndex("ntsc_release")));

                favourite = (c.getInt(c.getColumnIndex("favourite")));

                test = palAcart + " " + palAbox + " " + palAmanual + " " + palBcart + " " + palBbox + " " + palBmanual + " " + uscart + " " + usbox + " " + usmanual;
                Log.d("pixo", test);

                if (regionatrue == 0) { chkpalacart.setEnabled(false); chkpalabox.setEnabled(false); chkpalamanual.setEnabled(false);
                } else if (regionatrue == 1) { if (palAcart == 8783) { chkpalacart.setChecked(true); } else { chkpalacart.setChecked(false); }
                    if (palAbox == 8783) { chkpalabox.setChecked(true); } else { chkpalabox.setChecked(false); }
                    if (palAmanual == 8783) { chkpalamanual.setChecked(true); } else { chkpalamanual.setChecked(false); }
                }

                if (regionbtrue == 0) { chkpalbcart.setEnabled(false); chkpalbbox.setEnabled(false); chkpalbmanual.setEnabled(false); }
                else if (regionustrue == 1) { if (palBcart == 8783) { chkpalbcart.setChecked(true);  } else { chkpalbcart.setChecked(false); }
                    if (palBbox == 8783) { chkpalbbox.setChecked(true); } else { chkpalbbox.setChecked(false);  }
                    if (palBmanual == 8783) { chkpalbmanual.setChecked(true); } else { chkpalbmanual.setChecked(false);}
                }
                if (regionustrue == 0) { chkuscart.setEnabled(false); chkusbox.setEnabled(false); chkusmanual.setEnabled(false); }
                else if (regionustrue == 1) {if (uscart == 8783) { chkuscart.setChecked(true); } else { chkuscart.setChecked(false); }
                if (usbox == 8783) { chkusbox.setChecked(true); } else { chkusbox.setChecked(false); }
                if (usmanual == 8783) { chkusmanual.setChecked(true); } else { chkusmanual.setChecked(false);}
                }
                    c.moveToNext();//move to the next record
                }
                c.close();//close the cursor
            }

            db.close();//close the database
        }


    public void writegame(){

        CheckBox chkpalacart = (CheckBox) findViewById(R.id.chkPalACart);
        CheckBox chkpalabox = (CheckBox) findViewById(R.id.chkPalABox);
        CheckBox chkpalamanual = (CheckBox) findViewById(R.id.chkPalAmanual);
        CheckBox chkpalbcart = (CheckBox) findViewById(R.id.chkPalBCart);
        CheckBox chkpalbbox = (CheckBox) findViewById(R.id.chkPalBBox);
        CheckBox chkpalbmanual = (CheckBox) findViewById(R.id.chkPalBmanual);
        CheckBox chkuscart = (CheckBox) findViewById(R.id.chkUSCart);
        CheckBox chkusbox = (CheckBox) findViewById(R.id.chkUSBox);
        CheckBox chkusmanual = (CheckBox) findViewById(R.id.chkUSmanual);

        if (chkpalacart.isChecked()){ palAcart = 8783; cart = 1; owned = 1;} else  { palAcart = 32573; }
        if (chkpalabox.isChecked()){ palAbox = 8783; box = 1; owned = 1;} else { palAbox = 32573; }
        if (chkpalamanual.isChecked()){ palAmanual = 8783; manual = 1; owned = 1;} else { palAmanual = 32573; }

        if (chkpalbcart.isChecked()){ palBcart = 8783; cart = 1; owned = 1;} else { palBcart = 32573; }
        if (chkpalbbox.isChecked()){ palBbox = 8783; box = 1; owned = 1;} else { palBbox = 32573; }
        if (chkpalbmanual.isChecked()){ palBmanual = 8783; manual = 1; owned = 1;} else { palBmanual = 32573; }

        if (chkuscart.isChecked()){ uscart = 8783; cart = 1; owned = 1;} else { uscart = 32573; }
        if (chkusbox.isChecked()){ usbox = 8783; box = 1; owned = 1;} else { usbox = 32573;  }
        if (chkusmanual.isChecked()){ usmanual = 8783; manual = 1; owned = 1;} else { usmanual = 32573; }

        //if (chkpalacart.setChecked(false)){ if (chkpalbcart.setChecked(false) { if chkuscart.setChecked(false){cart = 0;}}}
        //  if (chkpalacart.isChecked(false))  (chkpalabox.setChecked(false)) && (chkpalamanual.setChecked(false)) &&  && (chkpalbbox.setChecked(false) && chkpalbmanual.setChecked(false) &&  && chkusbox.setChecked(false) && chkusmanual.setChecked(false){} ;
        if (palAcart == 32573 && palBcart == 32573 && uscart == 32573) { cart = 0; }
        if (palAbox == 32573 && palBbox == 32573 && usbox == 32573) { box = 0; }
        if (palAmanual == 32573 && palBmanual == 32573 && usmanual == 32573) { manual = 0; }

        if (cart == 0 && box == 0 && manual == 0) {owned = 0;}

                SQLiteDatabase db;//set up the connection to the database
                db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
                String str = "UPDATE eu SET owned = " + owned + ", cart = " + cart + ", box = " + box + ", manual = " + manual + ", pal_a_cart = " + palAcart + ", pal_a_box = " + palAbox + ", pal_a_manual = " + palAmanual + ", pal_b_cart = " + palBcart + ", pal_b_box = " + palBbox + ", pal_b_manual = " + palBmanual + ", ntsc_cart = " + uscart + ", ntsc_box = " + usbox + ",  ntsc_manual = " + usmanual + " where _id = " + gameid + " "; //update the database basket field with 8783
        db.execSQL(str);//run the sql command
                Log.d("Pixo", str);
                //Intent intent = new Intent(EditOwnedGame.this, OwnedGames.class);//opens a new screen when the shopping list is clicked
                //intent.putExtra("gameid", gameid);
                //startActivity(intent);//start the new screen
                finish();
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

}

