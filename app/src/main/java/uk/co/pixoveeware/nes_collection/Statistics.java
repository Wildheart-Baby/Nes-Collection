package uk.co.pixoveeware.nes_collection;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

public class Statistics extends AppCompatActivity {

    String[] palanames;
    String[] palbnames;
    String[] usnames;
    String[] pienames;
    int[] pienumbers;

    String name, dbfile, sql, licensed, PalA, PalB, US, s, gamecost, wherestatement, palaadd, palbadd, usadd, palanames2, pienames2, pienumbers2;
    int totalOwned, totalReleased, totalPalA, totalPalB, totalUS, ownedPalA, ownedPalB, ownedUS, io,cost, totalCost, percentPalANeeded, percentPalBNeeded, percentUSNeeded, i;
    double percentPalAOwned, percentPalBOwned, percentUSOwned;
    float palacost, palbcost, uscost, totalpalacost, totalpalbcost, totaluscost, totalcost;

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

        gameregion();
        readDatabase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_allgames, menu);
        return true;
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

        gamecost = "You spent " + palacost + " on pal a, " + palbcost + " on pal b and " + uscost + " on US games";
        cost.setText(gamecost);

        sql = "SELECT * FROM eu where pal_a_release = 1 " + licensed +  "";
        c = db.rawQuery(sql, null);
        totalPalA = c.getCount();

        sql = "SELECT * FROM eu where owned = 1 and (pal_a_cart = 8783 " + licensed +  ")";
        Log.d("Pixo", sql);
        c = db.rawQuery(sql, null);
        ownedPalA = c.getCount();

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

        pienames = new String[i];
        pienumbers = new int[i];
        io = 0;
        if (ownedactionadventure > 0){
            pienames[io] = "Action-Adventure";
            pienumbers[io] = ownedactionadventure;
            io ++;
        }
        if (ownedaction > 0){
            pienames[io] = "Action";
            pienumbers[io] = ownedaction;
            io ++;
        }
        if (ownedadventure > 0){
            pienames[io] = "Adventure";
            pienumbers[io] = ownedadventure;
            io ++;
        }
        if (ownedarcade > 0){
            pienames[io] = "Arcade";
            pienumbers[io] = ownedarcade;
            io ++;
        }
        if (ownedbeatemup > 0){
            pienames[io] = "Beat em Up";
            pienumbers[io] = ownedbeatemup;
            io ++;
        }
        if (ownedboardgame > 0){
            pienames[io] = "Board Game";
            pienumbers[io] = ownedboardgame;
            io ++;
        }
        if (ownedcompilation > 0){
            pienames[io] = "Compilation";
            pienumbers[io] = ownedcompilation;
            io ++;
        }
        if (ownedfighting > 0){
            pienames[io] = "Fighting";
            pienumbers[io] = ownedfighting;
            io ++;
        }
        if (ownedplatformer > 0){
            pienames[io] = "Platformer";
            pienumbers[io] = ownedplatformer;
            io ++;
        }
        if (ownedpuzzle > 0){
            pienames[io] = "Puzzle";
            pienumbers[io] = ownedpuzzle;
            io ++;
        }
        if (ownedracing > 0){
            pienames[io] = "Racing";
            pienumbers[io] = ownedracing;
            io ++;
        }
        if (ownedroleplayinggame > 0){
            pienames[io] = "Role Playing Game";
            pienumbers[io] = ownedroleplayinggame;
            io ++;
        }
        if (ownedshootemup > 0){
            pienames[io] = "Shoot em Up";
            pienumbers[io] = ownedshootemup;
            io ++;
        }
        if (ownedshooter > 0){
            pienames[io] = "Shooter";
            pienumbers[io] = ownedshooter;
            io ++;
        }
        if (ownedsimulation > 0){
            pienames[io] = "Simulation";
            pienumbers[io] = ownedsimulation;
            io ++;
        }
        if (ownedsports > 0){
            pienames[io] = "Sports";
            pienumbers[io] = ownedsports;
            io ++;
        }
        if (ownedstrategy > 0){
            pienames[io] = "Strategy";
            pienumbers[io] = ownedstrategy;
            io ++;
        }
        if (ownedtraditional > 0){
            pienames[io] = "Traditional";
            pienumbers[io] = ownedtraditional;
            io ++;
        }
        if (ownedtrivia > 0){
            pienames[io] = "Trivia";
            pienumbers[io] = ownedtrivia;
            io ++;
        }
        if (ownedvehicularsimulation > 0){
            pienames[io] = "Vehicular Simulation";
            pienumbers[io] = ownedvehicularsimulation;
            io ++;
        }

        pienames2 = Arrays.toString(pienames);
        pienumbers2 = Arrays.toString(pienumbers);
        pienames2 = pienames2.substring(1, pienames2.length() -1);
        pienumbers2 = pienumbers2.substring(1, pienumbers2.length() -1);

        Log.d("Pixo", "Pie names: " + pienames2 + "\nPie Numbers: " + pienumbers2);

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
        if (i > 1){palaadd = "Your most expensive games are";} else {palaadd = " Your most expensive game is ";}
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
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                //palbnames[i] = c.getString(c.getColumnIndex("name")) + " ";
                c.moveToNext();
             }
        }

        sql = "select name from eu where owned = 1 and ntsc_cost=(select max(ntsc_cost) from eu)";
        c = db.rawQuery(sql, null);
        i = c.getCount();
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                //usnames[i] = c.getString(c.getColumnIndex("name")) + " ";
                c.moveToNext();
            }
        }

        c.close();
        db.close();//close the database

        percentPalAOwned = ((double)ownedPalA / totalPalA) * 100;
        s = String.format("%.2f", percentPalAOwned);
        PalA = "You own " + ownedPalA + " of the " + totalPalA + " Pal A games released.\n" +
                palaadd + " " + palanames2;
        pala.setText(PalA);

        percentPalBOwned = ((double)ownedPalB / totalPalB) * 100;
        s = String.format("%.2f", percentPalBOwned);
        PalB = "You own " + ownedPalB + " of the " + totalPalB + " Pal B games released.";
        palb.setText(PalB);

        percentUSOwned = ((double)ownedUS / totalUS) * 100;
        s = String.format("%.2f", percentPalBOwned);
        US = "You own " + ownedUS + "  of the " + totalUS + " US games released.";
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

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

}
