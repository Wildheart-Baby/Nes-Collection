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
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.Random;

public class ShelfOrder extends AppCompatActivity {

    final Context context = this;
    SQLiteDatabase sqlDatabase;

    String name, dbfile, readgamename, str, sql,listName,searchterm,fieldname, wherestatement, sql1, sql2, sql3, licensed, currentgroup;
    String prevgroup = "";
    int readgameid, gameid, ownedgames, totalgames, index, top, region1games, region2games,region3games, count, randomgame, itemId, position, shelf, cartPala, cartpalb, cartus;
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ownedlistView = (ListView) findViewById(R.id.lvOwnedGames); //sets up a listview with the name shoplistview
        gameregion();
        readList();
        gamescount();

        ownedlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                NesItems gameListItems = (NesItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                readgameid = gameListItems.getItemId();//get the name of the shopping list table
                readgamename = gameListItems.getName();//get the name of the shopping list table
                Intent intent = new Intent(ShelfOrder.this, GameDetail.class);//opens a new screen when the shopping list is clicked
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

                Intent intent = new Intent(ShelfOrder.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("editgameid", itemId);
                startActivity(intent);//start the new screen

                return true;//return is equal to true
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ownedgames, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(ShelfOrder.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(ShelfOrder.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;
            case R.id.action_random:
                randomgame();
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(ShelfOrder.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
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

        index = ownedlistView.getFirstVisiblePosition();
        View v = ownedlistView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
    }

    public void readList(){//the readlist function
        ArrayList<NesItems> nesList = new ArrayList<NesItems>();//sets up an array list called shoppingList
        nesList.clear();//clear the shoppingList array

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        sql = "SELECT * from eu where owned = 1";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table

        position = 1;
        shelf = 1;

        if (c.moveToFirst()) {//move to the first record
            while (!c.isAfterLast()) {//while there are records to read

                //  palAcart = (c.getInt(c.getColumnIndex("pal_a_cart")));
                cartPala = (c.getInt(c.getColumnIndex("pal_a_cart")));
                cartpalb = (c.getInt(c.getColumnIndex("pal_b_cart")));
                cartus = (c.getInt(c.getColumnIndex("ntsc_cart")));

                NesItems nesListItems = new NesItems();//creates a new array


                Log.d("Pixo", "carts: " + cartPala + " " + cartpalb + " " + cartus);
                //currentgroup = c.getString(c.getColumnIndex("groupheader"));

                if(position == 1) {
                    nesListItems.setGroup("Shelf " + shelf);
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
                    //prevgroup = c.getString(c.getColumnIndex("groupheader"));
                    nesList.add(nesListItems);//add items to the arraylist
                    shelf ++;
                }
                else {
                    nesListItems.setGroup("no");
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
                    //prevgroup = c.getString(c.getColumnIndex("groupheader"));
                    nesList.add(nesListItems);//add items to the arraylist
                    if (position == 9){ position = 0;}
                }
                if (cartpalb == cartPala){c.moveToPrevious(); Log.d("Pixo", "First if check");}
                else if (cartus == cartPala){c.moveToPrevious(); Log.d("Pixo", "Second if check");}
                else if (cartus == cartpalb){c.moveToPrevious(); Log.d("Pixo", "Third if check");}
                else {c.moveToNext(); Log.d("Pixo", "else if check");}//move to the next record

                position ++;
            }
            //ownedgames = c.getCount();
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);

        sql = "SELECT * FROM eu where " + wherestatement + licensed + "";

        c = db.rawQuery(sql, null);
        region1games = c.getCount();
        c.close();

        db.close();//close the database


        NesOwnedAdapter nes = new NesOwnedAdapter(this, nesList);//set up an new list adapter from the arraylist
        ownedlistView.setAdapter(nes);//set the listview with the contents of the arraylist
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                Log.d("Pixo", wherestatement);

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    public void gamescount(){
        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        Cursor c; //= db.rawQuery(sql, null);//select everything from the database table
        //sql = "SELECT * FROM eu where " + wherestatement + "";
        sql1 = "SELECT * FROM eu where pal_a_cart = 8783";
        sql2 = "SELECT * FROM eu where pal_b_cart = 8783";
        sql3 = "SELECT * FROM eu where ntsc_cart = 8783";

        c = db.rawQuery(sql1, null);
        region1games = c.getCount();
        c.close();
        c = db.rawQuery(sql2, null);
        region2games = c.getCount();
        c.close();
        c = db.rawQuery(sql3, null);
        region3games = c.getCount();
        c.close();
        db.close();//close the database
        ownedgames = region1games + region2games + region3games;
        str = "You own " + ownedgames + " games";
        TextView gamesfooter = (TextView) findViewById(R.id.lblTotal);
        gamesfooter.setText(str);
    }

    public void randomgame(){
        count = ownedlistView.getAdapter().getCount();
        if (count >2) {

            Random rand = new Random();
            randomgame = rand.nextInt(count);

            NesItems gameListItems = (NesItems) ownedlistView.getItemAtPosition(randomgame);//get the position of the item on the list
            itemId = gameListItems.getItemId();//get the item id

            final Dialog randomgame = new Dialog(ShelfOrder.this);//sets up the dialog
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
                    int coverid = ShelfOrder.this.getResources().getIdentifier(gameimage, "drawable", ShelfOrder.this.getPackageName());
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

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        readList();//run the list tables function
        ownedlistView.setSelectionFromTop(index, top);
    }

}
