package uk.co.pixoveeware.nes_collection.activities;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.models.AllGameItems;

public class EditOwnedGame extends AppCompatActivity {

    int gameid, coverid, palAcart, palAbox, palAmanual, palBcart, palBbox, gamepos,
        palBmanual, uscart, usbox, usmanual, cart, box, manual, owned,
        regionatrue, regionbtrue, regionustrue, favourite, ontheshelf, wishlist, showprice, palaowned, palbowned, usowned;
    String covername, sql, test, currency, PACheck, PBCheck, USCheck;
    Double  PalAcost,PalBcost, UScost, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_owned_game);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        setSupportActionBar(toolbar);
        gamepos = getIntent().getIntExtra("listposition", 0);
        gameid = getIntent().getIntExtra("editgameid", 0);
        final EditText PalACost = (EditText) findViewById(R.id.txtPalAcost);
        final EditText PalBCost = (EditText) findViewById(R.id.txtPalBcost);
        final EditText USCost = (EditText) findViewById(R.id.txtUScost);

        Button ok = (Button) findViewById(R.id.rgnOk);
        Button cancel = (Button) findViewById(R.id.rgnCancel);

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
        PalACost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PalACost.getText().clear();
            }
        });
        PalBCost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PalBCost.getText().clear();
            }
        });
        USCost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                USCost.getText().clear();
            }
        });

        setTitle("Which items do you own");
        readSettings();
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
            case R.id.action_about:
                Intent intent3 = new Intent(EditOwnedGame.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
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
        ArrayList<AllGameItems> nesList = new ArrayList<AllGameItems>();//sets up an array list called shoppingList
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
        CheckBox onshelf = (CheckBox) findViewById(R.id.chkShelf);

        TextView CostHdr = (TextView) findViewById(R.id.lblCost);
        EditText PalACost = (EditText) findViewById(R.id.txtPalAcost);
        EditText PalBCost = (EditText) findViewById(R.id.txtPalBcost);
        EditText USCost = (EditText) findViewById(R.id.txtUScost);
        CheckBox BlankChk = (CheckBox) findViewById(R.id.checkBox);

        TextView PalACurrency = (TextView) findViewById(R.id.lblCurrencyPalA);
        TextView PalBCurrency = (TextView) findViewById(R.id.lblCurrencyPalB);
        TextView USCurrency = (TextView) findViewById(R.id.lblCurrencyUS);

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        sql = "SELECT * FROM eu where _id = '" + gameid + "' ";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table
        Log.d("shelf",sql);
        if (c.moveToFirst()) {//move to the first record
            while (!c.isAfterLast()) {//while there are records to read

                covername = (c.getString(c.getColumnIndex("image")));
                coverid = getResources().getIdentifier(covername, "drawable", getPackageName());
                gamename.setText((c.getString(c.getColumnIndex("name"))));
                System.gc();
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
                PalAcost = (c.getDouble(c.getColumnIndex("pal_a_cost")));
                PalBcost = (c.getDouble(c.getColumnIndex("pal_b_cost")));
                UScost = c.getDouble(c.getColumnIndex("ntsc_cost"));

                ontheshelf = (c.getInt(c.getColumnIndex("onshelf")));
                wishlist = (c.getInt(c.getColumnIndex("wishlist")));
                c.moveToNext();//move to the next record
                            }
                        c.close();//close the cursor
                    }
                db.close();//close the database

                PalACost.setText(String.format("%.2f",PalAcost));
                PalBCost.setText(String.format("%.2f",PalBcost));
                USCost.setText(String.format("%.2f",UScost));

                PalACurrency.setText(currency);
                PalBCurrency.setText(currency);
                USCurrency.setText(currency);

                if (regionatrue == 0) { chkpalacart.setEnabled(false); chkpalabox.setEnabled(false); chkpalamanual.setEnabled(false);  PalACost.setEnabled(false); PalACost.setText("");
                } else if (regionatrue == 1) {
                    if (palAcart == 8783) { chkpalacart.setChecked(true); } else { chkpalacart.setChecked(false); }
                    if (palAbox == 8783) { chkpalabox.setChecked(true); } else { chkpalabox.setChecked(false); }
                    if (palAmanual == 8783) { chkpalamanual.setChecked(true); } else { chkpalamanual.setChecked(false); }
                    if (palAbox == 0){ chkpalabox.setEnabled(false); }

                }

                if (regionbtrue == 0) { chkpalbcart.setEnabled(false); chkpalbbox.setEnabled(false); chkpalbmanual.setEnabled(false);  PalBCost.setEnabled(false); PalBCost.setText("");}
                else if (regionbtrue == 1) {
                    if (palBcart == 8783) { chkpalbcart.setChecked(true);  } else { chkpalbcart.setChecked(false); }
                    if (palBbox == 8783) { chkpalbbox.setChecked(true); } else { chkpalbbox.setChecked(false);  }
                    if (palBmanual == 8783) { chkpalbmanual.setChecked(true); } else { chkpalbmanual.setChecked(false);}
                    if (palBbox == 0 ){ chkpalbbox.setEnabled(false); }
                }
                if (regionustrue == 0) { chkuscart.setEnabled(false); chkusbox.setEnabled(false); chkusmanual.setEnabled(false);  USCost.setEnabled(false); USCost.setText("");}
                else if (regionustrue == 1) {if (uscart == 8783) { chkuscart.setChecked(true); } else { chkuscart.setChecked(false); }
                if (usbox == 8783) { chkusbox.setChecked(true); } else { chkusbox.setChecked(false); }
                if (usmanual == 8783) { chkusmanual.setChecked(true); } else { chkusmanual.setChecked(false);}
                if (usbox == 0){ chkusbox.setEnabled(false); }
                }


        if (ontheshelf == 1){onshelf.setChecked(true);} else { onshelf.setChecked(false);}
        if (showprice == 0){CostHdr.setVisibility(View.GONE); BlankChk.setVisibility(View.GONE);
                            PalACurrency.setVisibility(View.GONE); PalBCurrency.setVisibility(View.GONE); USCurrency.setVisibility(View.GONE);
                            PalACost.setVisibility(View.GONE); PalBCost.setVisibility(View.GONE); USCost.setVisibility(View.GONE);}
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
        CheckBox onshelf = (CheckBox) findViewById(R.id.chkShelf);
        EditText PalACost = (EditText) findViewById(R.id.txtPalAcost);
        EditText PalBCost = (EditText) findViewById(R.id.txtPalBcost);
        EditText USCost = (EditText) findViewById(R.id.txtUScost);

        if (chkpalacart.isChecked()){ palAcart = 8783; cart = 1; palaowned = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setCart(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1); } else  { palAcart = 32573; }
        if (chkpalabox.isChecked()){ palAbox = 8783; box = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setBox(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1); } else { palAbox = 32573; }
        if (chkpalamanual.isChecked()){ palAmanual = 8783; manual = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setManual(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1); } else { palAmanual = 32573; }

        if (chkpalbcart.isChecked()){ palBcart = 8783; cart = 1; palbowned = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setCart(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1); } else { palBcart = 32573; }
        if (chkpalbbox.isChecked()){ palBbox = 8783; box = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setBox(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1); } else { palBbox = 32573; }
        if (chkpalbmanual.isChecked()){ palBmanual = 8783; manual = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setManual(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1);} else { palBmanual = 32573; }

        if (chkuscart.isChecked()){ uscart = 8783; cart = 1; usowned = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setCart(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1);} else { uscart = 32573; }
        if (chkusbox.isChecked()){ usbox = 8783; box = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setBox(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1); } else { usbox = 32573;  }
        if (chkusmanual.isChecked()){ usmanual = 8783; manual = 1; owned = 1; HomeScreenActivity.gamesList.get(gamepos).setManual(1); HomeScreenActivity.gamesList.get(gamepos).setOwned(1);} else { usmanual = 32573; }

        if (onshelf.isChecked()){ontheshelf = 1;} else { ontheshelf = 0;}

        if (palAcart == 32573 && palBcart == 32573 && uscart == 32573) { cart = 0; HomeScreenActivity.gamesList.get(gamepos).setCart(0);  }
        if (palAbox == 32573 && palBbox == 32573 && usbox == 32573) { box = 0; HomeScreenActivity.gamesList.get(gamepos).setBox(0); }
        if (palAmanual == 32573 && palBmanual == 32573 && usmanual == 32573) { manual = 0; HomeScreenActivity.gamesList.get(gamepos).setManual(0); }

        if (cart == 0 && box == 0 && manual == 0) {owned = 0; HomeScreenActivity.gamesList.get(gamepos).setOwned(0);}
        PACheck = PalACost.getText().toString().replaceAll("[,]", ".");
        Log.d("Pixo-cost", PACheck);
        PACheck = PalACost.getText().toString().replaceAll("[^0-9.]", "");
        Log.d("Pixo-cost", PACheck);
        //PACheck = PACheck.replaceAll("[^0-9.]", "");

        PBCheck = PalBCost.getText().toString().replaceAll("[^0-9.]", "");
        //PBCheck = PBCheck.replaceAll("[^0-9.]", "");

        USCheck = USCost.getText().toString().replaceAll("[^0-9.]", "");
        //USCheck = USCheck.replaceAll("[^0-9.]", "");

        Log.d("pixo", " " + PACheck + " " + PBCheck + " " + USCheck  );

        if (PACheck.matches("") || !Character.isDigit(PACheck.charAt(0))) {PalAcost = 0.0;}
        else {PalAcost = Double.parseDouble(PACheck);}

        if (PBCheck.matches("") || !Character.isDigit(PBCheck.charAt(0))) {PalBcost = 0.0;}
        else {PalBcost = Double.parseDouble(PBCheck);}
        //else {PalBcost = Double.valueOf(PalBCost.getText().toString());}

        if (USCheck.matches("") || !Character.isDigit(USCheck.charAt(0))) {UScost = 0.0;}
        else {UScost = Double.parseDouble(USCheck);}
        //else {UScost = Double.valueOf(USCost.getText().toString());}

        if (PalAcost > PalBcost && PalAcost > UScost){price = PalAcost;}
        else if (PalBcost > PalAcost && PalBcost > UScost){price = PalBcost;}
        else if (UScost > PalAcost && UScost > PalBcost){price = UScost;}

        if (PACheck.equals("0.00") && PBCheck.equals("0.00") && USCheck.equals("0.00")){price = 0.00;}

        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str = "UPDATE eu SET owned = " + owned + ", cart = " + cart + ", box = " + box + ", manual = " + manual + ", pal_a_cart = " + palAcart + ", pal_a_box = " + palAbox + ", pal_a_manual = " + palAmanual + ", pal_b_cart = " + palBcart + ", pal_b_box = " + palBbox + ", pal_b_manual = " + palBmanual + ", ntsc_cart = " + uscart + ", ntsc_box = " + usbox + ",  ntsc_manual = " + usmanual + ",  pal_a_cost = " + PalAcost + ",  pal_b_cost = " + PalBcost + ",  ntsc_cost = " + UScost + ",  price = " + price + ",  onshelf = " + ontheshelf +  ",  pal_a_owned = " + palaowned +  ",  pal_b_owned = " + palbowned +  ",  ntsc_owned = " + usowned +  ",  wishlist = 0 where _id = " + gameid + " "; //update the database basket field with 8783
        db.execSQL(str);//run the sql command
                Log.d("Pixo", str);
                //Intent intent = new Intent(EditOwnedGame.this, OwnedGames.class);//opens a new screen when the shopping list is clicked
                //intent.putExtra("gameid", gameid);
                //startActivity(intent);//start the new screen
        db.close();
        finish();
            }

    public void favouritegame(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        if(favourite == 0) {
            str = "UPDATE eu SET favourite = 1 where _id = " + gameid + " ";
            HomeScreenActivity.gamesList.get(gamepos).setFavourite(1);//update the database basket field with 8783
        } else  if(favourite == 1) {
            str = "UPDATE eu SET favourite = 0 where _id = " + gameid + " ";
            HomeScreenActivity.gamesList.get(gamepos).setFavourite(0);//update the database basket field with 8783
        }
        Log.d("Pixo", str);
        db.execSQL(str);//run the sql command
        db.close();
        readGame();
        invalidateOptionsMenu();
    }



    public void readSettings(){
        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        sql = "SELECT * FROM settings";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table
        c.moveToFirst();
        currency = (c.getString(c.getColumnIndex("currency")));
        showprice = (c.getInt(c.getColumnIndex("show_price")));
        c.close();
        db.close();

    }
}

