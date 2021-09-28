package uk.co.pixoveeware.nes_collection;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class Statistics extends AppCompatActivity implements PieChartView.Callback,
        NavigationView.OnNavigationItemSelectedListener  {

    String[] palanames;
    String[] palbnames;
    String[] usnames;
    String[] names;
    String popgenre;
    float[] datapoints;
    int[] piecolours;

    String name, dbfile, sql, licensed, PalA, PalB, US, s, gamecost, wherestatement, palaadd, palbadd, usadd, palanames2, palbnames2, usnames2, currency,poppublisher, perpalacoll, perpalbcoll, peruscoll;
    int totalOwned, totalReleased, totalPalA, totalPalB, totalUS, ownedPalA, ownedPalB, ownedUS, io,cost, totalCost, percentPalANeeded, percentPalBNeeded, percentUSNeeded, i;
    double percentPalAOwned, percentPalBOwned, percentUSOwned, percentagepalacollection, percentagepalbcollection, percentageuscollection;
    float palacost, palbcost, uscost, totalpalacost, totalpalbcost, totaluscost, totalcost;
    int lay = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbfile = (this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite"); //sets up the variable dbfile with the location of the database

        setTitle("Statistics");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        run();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void run(){
        gameregion();
        readDatabase();

        PieChartView pieChartView = (PieChartView) findViewById(R.id.pie_chart);
        pieChartView.setDataPoints(datapoints, names, piecolours);
        // Because this activity is of the type PieChartView.Callback
        pieChartView.setCallback(Statistics.this);
    }

    @Override
    public void onDrawFinised(DataColorSet[] data) {
        // When the chart has finished drawing it will return the colors used
        // and the value along (for our key)

        LinearLayout keyContainer = (LinearLayout) findViewById(R.id.key);
        if (keyContainer.getChildCount() > 0)
            keyContainer.removeAllViews(); // Empty all views if any found

        for (int i = 0; i < data.length; i++) {
            DataColorSet dataColorSet = data[i];

            LinearLayout keyItem = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.key_item, null);
            LinearLayout colorView = (LinearLayout) keyItem.findViewById(R.id.color);
            TextView name = (TextView) keyItem.findViewById(R.id.names);
            TextView valueView = (TextView) keyItem.findViewById(R.id.value);

            colorView.setBackgroundColor(Color.parseColor("#" + dataColorSet.getColor()));
            name.setText("" + dataColorSet.getName());
            valueView.setText("" + dataColorSet.getDataValue());

            // Add the key to the container
            keyContainer.addView(keyItem, i);
        }

    }

    @Override
    public void onSliceClick(DataColorSet data) {
        // When the slice has been clicked. You can decide to call another
        // activity here. We'll just make a toast
        Toast.makeText(this, "Value is: " + data.getName(), Toast.LENGTH_SHORT).show();
        //get datavalue and run search based on
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
                Intent intent = new Intent(Statistics.this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(Statistics.this, Search.class);//opens a new screen when the shopping list is clicked
                startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(Statistics.this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void readDatabase(){

        TextView pala = (TextView) findViewById(R.id.lblPalA);
        TextView palb = (TextView) findViewById(R.id.lblPalB);
        TextView us = (TextView) findViewById(R.id.lblUS);
        TextView cost = (TextView) findViewById(R.id.lblCost);

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database

        sql = "SELECT SUM(pal_a_cost) FROM eu ";
        Cursor c = db.rawQuery(sql, null);//select everything from the database table
        c.moveToFirst();
        palacost = c.getFloat(0);
        c.close();

        sql = "SELECT SUM(pal_b_cost) FROM eu ";
        c = db.rawQuery(sql, null);//select everything from the database table
        c.moveToFirst();
        palbcost = c.getFloat(0);
        c.close();

        sql = "SELECT SUM(ntsc_cost) FROM eu ";
        c = db.rawQuery(sql, null);//select everything from the database table
        c.moveToFirst();
        uscost = c.getFloat(0);
        c.close();

        totalcost = palacost + palbcost + uscost;


        sql = "SELECT * FROM eu where pal_a_release = 1 " + licensed +  "";
        c = db.rawQuery(sql, null);
        totalPalA = c.getCount();

        sql = "SELECT * FROM eu where owned = 1 and (pal_a_cart = 8783 " + licensed +  ")";
        Log.d("Pixo", sql);
        c = db.rawQuery(sql, null);
        ownedPalA = c.getCount();

        sql = "SELECT * FROM eu where pal_b_release = 1 " + licensed +  "";
        c = db.rawQuery(sql, null);
        totalPalB = c.getCount();
        sql = "SELECT * FROM eu where owned = 1 and (pal_b_cart = 8783 " + licensed +  ")";
        Log.d("Pixo", sql);
        c = db.rawQuery(sql, null);
        ownedPalB = c.getCount();

        sql = "SELECT * FROM eu where ntsc_release = 1 " + licensed +  "";
        c = db.rawQuery(sql, null);
        totalUS = c.getCount();

        sql = "SELECT * FROM eu where owned = 1 and (ntsc_cart = 8783 " + licensed +  ")";
        c = db.rawQuery(sql, null);
        Log.d("Pixo", sql);
        ownedUS = c.getCount();

        sql = "SELECT * FROM eu where 1 " + licensed +  "";
        c = db.rawQuery(sql, null);
        totalReleased = c.getCount();

        sql = "SELECT * FROM eu where owned = 1 " + licensed +  "";
        c = db.rawQuery(sql, null);
        totalOwned = c.getCount();

        sql = "select name from eu where owned = 1 and pal_a_cost=(select max(pal_a_cost) from eu)";
        c = db.rawQuery(sql, null);
        i = c.getCount();
        Log.d("pixo", "value:"+i);
        if (i > 1){palaadd = "Your most expensive games are";} else {palaadd = "Your most expensive game is";}
        String[] palanames = new String[i];

        if (c.moveToFirst()) {//move to the first record
            io = 0;
            while ( !c.isAfterLast() ) {//while there are records to read
                palanames[io] = c.getString(c.getColumnIndex("name")) + " ";
                c.moveToNext();
                io ++;
            }
            palanames2 = Arrays.toString(palanames);
            palanames2 = palanames2.substring(1, palanames2.length() -1);
            Log.d("pixo", palanames2);
        }


        sql = "select name from eu where owned = 1 and pal_b_cost=(select max(pal_b_cost) from eu)";
        c = db.rawQuery(sql, null);
        i = c.getCount();
        if (i > 1){palbadd = "Your most expensive games are";} else {palbadd = "Your most expensive game is";}
        String[] palbnames = new String[i];
        if (c.moveToFirst()) {//move to the first record
            io=0;
            while ( !c.isAfterLast() ) {//while there are records to read
                palbnames[io] = c.getString(c.getColumnIndex("name")) + " ";
                c.moveToNext();
                io++;
            }
            palbnames2 = Arrays.toString(palbnames);
            palbnames2 = palbnames2.substring(1, palbnames2.length() -1);
        }

        sql = "select name from eu where owned = 1 and ntsc_cost=(select max(ntsc_cost) from eu)";
        c = db.rawQuery(sql, null);
        i = c.getCount();
        if (i > 1){usadd = "Your most expensive games are";} else {usadd = "Your most expensive game is";}
        String[] usnames = new String[i];
        if (c.moveToFirst()) {//move to the first record
            io = 0;
            while ( !c.isAfterLast() ) {//while there are records to read
                usnames[io] = c.getString(c.getColumnIndex("name")) + " ";
                c.moveToNext();
                io ++;
            }
            usnames2 = Arrays.toString(usnames);
            usnames2 = usnames2.substring(1, usnames2.length() -1);
        }

        sql = "select genre, COUNT(genre) AS MOST_FREQUENT from eu where owned = 1 GROUP BY genre ORDER BY COUNT(genre) DESC limit 1";
        c = db.rawQuery(sql, null);
        c.moveToFirst();
        popgenre = c.getString(c.getColumnIndex("genre"));

        sql = "select publisher, COUNT(publisher) AS MOST_FREQUENT from eu where owned = 1 GROUP BY genre ORDER BY COUNT(publisher) DESC limit 1";
        c = db.rawQuery(sql, null);
        c.moveToFirst();
        poppublisher = c.getString(c.getColumnIndex("publisher"));

        i = 0;

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Action-Adventure'";
        c = db.rawQuery(sql, null);
        int ownedactionadventure = c.getCount();

        if (ownedactionadventure > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Action'";
        c = db.rawQuery(sql, null);
        int ownedaction = c.getCount();

        if (ownedaction > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Adventure'";
        c = db.rawQuery(sql, null);
        int ownedadventure = c.getCount();

        if (ownedadventure > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Arcade'";
        c = db.rawQuery(sql, null);
        int ownedarcade = c.getCount();

        if (ownedarcade > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Beat em Up'";
        c = db.rawQuery(sql, null);
        int ownedbeatemup = c.getCount();

        if (ownedbeatemup > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Board Game'";
        c = db.rawQuery(sql, null);
        int ownedboardgame = c.getCount();

        if (ownedboardgame > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Compilation'";
        c = db.rawQuery(sql, null);
        int ownedcompilation = c.getCount();

        if (ownedcompilation > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Fighting'";
        c = db.rawQuery(sql, null);
        int ownedfighting = c.getCount();

        if (ownedfighting > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Platformer'";
        c = db.rawQuery(sql, null);
        int ownedplatformer = c.getCount();

        if (ownedplatformer > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Puzzle'";
        c = db.rawQuery(sql, null);
        int ownedpuzzle = c.getCount();

        if (ownedpuzzle > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Racing'";
        c = db.rawQuery(sql, null);
        int ownedracing = c.getCount();

        if (ownedracing > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Racing'";
        c = db.rawQuery(sql, null);
        int ownedroleplayinggame = c.getCount();

        if (ownedroleplayinggame > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Shoot em Up'";
        c = db.rawQuery(sql, null);
        int ownedshootemup = c.getCount();

        if (ownedshootemup > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Shooter'";
        c = db.rawQuery(sql, null);
        int ownedshooter = c.getCount();

        if (ownedshooter > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Simulation'";
        c = db.rawQuery(sql, null);
        int ownedsimulation = c.getCount();

        if (ownedsimulation > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Sports'";
        c = db.rawQuery(sql, null);
        int ownedsports = c.getCount();

        if (ownedsports > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Strategy'";
        c = db.rawQuery(sql, null);
        int ownedstrategy = c.getCount();

        if (ownedstrategy > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Traditional'";
        c = db.rawQuery(sql, null);
        int ownedtraditional = c.getCount();
        if (ownedtraditional > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and genre = 'Trivia'";
        c = db.rawQuery(sql, null);
        int ownedtrivia = c.getCount();
        if (ownedtrivia > 0){ i++;}

        sql = "SELECT * FROM eu where owned = 1 and  genre = 'Vehicular Simulation'";
        c = db.rawQuery(sql, null);
        int ownedvehicularsimulation = c.getCount();

        if (ownedvehicularsimulation > 0){ i++;}

        names = new String[i];
        datapoints = new float[i];
        piecolours = new int[i];
        io = 0;
        if (ownedactionadventure > 0){
            names[io] = "Action-Adventure";
            datapoints[io] = ownedactionadventure;
            piecolours[io] = Color.parseColor("#FF2B32");
            io ++;
        }
        if (ownedaction > 0){
            names[io] = "Action";
            datapoints[io] = ownedaction;
            piecolours[io] = Color.parseColor("#FF6A00");
            io ++;
        }
        if (ownedadventure > 0){
            names[io] = "Adventure";
            datapoints[io] = ownedadventure;
            piecolours[io] = Color.parseColor("#FFD800");
            io ++;
        }
        if (ownedarcade > 0){
            names[io] = "Arcade";
            datapoints[io] = ownedarcade;
            piecolours[io] = Color.parseColor("#B6FF00");
            io ++;
        }
        if (ownedbeatemup > 0){
            names[io] = "Beat em Up";
            datapoints[io] = ownedbeatemup;
            piecolours[io] = Color.parseColor("#4CFF00");
            io ++;
        }
        if (ownedboardgame > 0){
            names[io] = "Board Game";
            datapoints[io] = ownedboardgame;
            piecolours[io] = Color.parseColor("#00FF90");
            io ++;
        }
        if (ownedcompilation > 0){
            names[io] = "Compilation";
            datapoints[io] = ownedcompilation;
            piecolours[io] = Color.parseColor("#0094FF");
            io ++;
        }
        if (ownedfighting > 0){
            names[io] = "Fighting";
            datapoints[io] = ownedfighting;
            piecolours[io] = Color.parseColor("#B200FF");
            io ++;
        }
        if (ownedplatformer > 0){
            names[io] = "Platformer";
            datapoints[io] = ownedplatformer;
            piecolours[io] = Color.parseColor("#FF00DC");
            io ++;
        }
        if (ownedpuzzle > 0){
            names[io] = "Puzzle";
            datapoints[io] = ownedpuzzle;
            piecolours[io] = Color.parseColor("#FF006E");
            io ++;
        }
        if (ownedracing > 0){
            names[io] = "Racing";
            datapoints[io] = ownedracing;
            piecolours[io] = Color.parseColor("#FF7F7F");
            io ++;
        }
        if (ownedroleplayinggame > 0){
            names[io] = "Role Playing Game";
            datapoints[io] = ownedroleplayinggame;
            piecolours[io] = Color.parseColor("#C0C0C0");
            io ++;
        }
        if (ownedshootemup > 0){
            names[io] = "Shoot em Up";
            datapoints[io] = ownedshootemup;
            piecolours[io] = Color.parseColor("#613F7C");
            io ++;
        }
        if (ownedshooter > 0){
            names[io] = "Shooter";
            datapoints[io] = ownedshooter;
            piecolours[io] = Color.parseColor("#60C5FF");
            io ++;
        }
        if (ownedsimulation > 0){
            names[io] = "Simulation";
            datapoints[io] = ownedsimulation;
            piecolours[io] = Color.parseColor("#FFAFB5");
            io ++;
        }
        if (ownedsports > 0){
            names[io] = "Sports";
            datapoints[io] = ownedsports;
            piecolours[io] = Color.parseColor("#D5FFBF");
            io ++;
        }
        if (ownedstrategy > 0){
            names[io] = "Strategy";
            datapoints[io] = ownedstrategy;
            piecolours[io] = Color.parseColor("#4FFFBE");
            io ++;
        }
        if (ownedtraditional > 0){
            names[io] = "Traditional";
            datapoints[io] = ownedtraditional;
            piecolours[io] = Color.parseColor("#A856FF");
            io ++;
        }
        if (ownedtrivia > 0){
            names[io] = "Trivia";
            datapoints[io] = ownedtrivia;
            piecolours[io] = Color.parseColor("#CBFF72");
            io ++;
        }
        if (ownedvehicularsimulation > 0){
            names[io] = "Vehicular Simulation";
            datapoints[io] = ownedvehicularsimulation;
            piecolours[io] = Color.parseColor("#613F7C");
            io ++;
        }

        c.close();
        db.close();//close the database
        totalOwned = ownedPalA + ownedPalB + ownedUS;
        gamecost = "You have spent " + currency + totalcost + " on games for your collection\n" +
                "You own a total of " + totalOwned + " games\n" +
                "The top publisher for your games is " + poppublisher + "\n" +
                "The most popular genre you have is " + popgenre;
        cost.setText(gamecost);

        percentPalAOwned = ((double)ownedPalA / totalPalA) * 100;
        percentagepalacollection = ((double)ownedPalA / totalOwned) * 100;
        s = String.format("%.2f", percentPalAOwned);
        perpalacoll = String.format("%.2f", percentagepalacollection);
        PalA = "You own " + ownedPalA + " of the " + totalPalA + " Pal A games released.\n" +
                "Pal A games make up " + perpalacoll + "% of your collection\n"
                 +palaadd + " " + palanames2;
        pala.setText(PalA);

        percentPalBOwned = ((double)ownedPalB / totalPalB) * 100;
        percentagepalbcollection = ((double)ownedPalB / totalOwned) * 100;
        s = String.format("%.2f", percentPalBOwned);
        perpalbcoll = String.format("%.2f", percentagepalbcollection);
        PalB = "You own " + ownedPalB + " of the " + totalPalB + " Pal B games released.\n" +
                "Pal B games make up " + perpalbcoll + "% of your collection\n"
                +palbadd + " " + palbnames2;
        palb.setText(PalB);

        percentUSOwned = ((double)ownedUS / totalUS) * 100;
        Log.d("Pixo", "PalB% owned:" +percentUSOwned);
        s = String.format("%.2f", percentUSOwned);
        percentageuscollection = ((double)ownedUS / totalOwned) * 100;
        peruscoll = String.format("%.2f", percentageuscollection);
        US = "You own " + ownedUS + " of the " + totalUS + " US games released.\n" +
                "US games make up " + peruscoll + "% of your collection\n"
                +usadd + " " + usnames2;
        us.setText(US);

        Log.d("Pixo", "Owned PalA: " + ownedPalA);
        Log.d("Pixo", "Owned PalB: " + ownedPalB);
        Log.d("Pixo", "Owned US: " + ownedUS);

    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                licensed = (c.getString(c.getColumnIndex("licensed")));
                wherestatement = (c.getString(c.getColumnIndex("region")));
                currency = (c.getString(c.getColumnIndex("currency")));

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        run();//run the list tables function

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_allgames) {
            Intent intent = new Intent(this, AllGames.class);//opens a new screen when the shopping list is clicked
            intent.putExtra("wherestatement", wherestatement);
            startActivity(intent);
        } else if (id == R.id.nav_neededgames) {
            Intent intent = new Intent(this, NeededGames.class);//opens a new screen when the shopping list is clicked
            intent.putExtra("wherestatement", wherestatement);
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_ownedgames) {
            Intent intent = new Intent(this, OwnedGames.class);//opens a new screen when the shopping list is clicked
            intent.putExtra("wherestatement", wherestatement);
            startActivity(intent);
        } else if (id == R.id.nav_favouritegames) {
            Intent intent = new Intent(this, FavouriteGames.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_wishlist) {
            Intent intent = new Intent(this, WishList.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_shelforder) {
            Intent intent = new Intent(this, ShelfOrder.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(this, Statistics.class);//opens a new screen when the shopping list is clicked
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, Settings.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
