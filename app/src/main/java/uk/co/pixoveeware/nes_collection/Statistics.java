package uk.co.pixoveeware.nes_collection;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {

    String name, dbfile, sql, licensed, PalA, PalB, US, s, gamecost;
    int totalOwned, totalReleased, totalPalA, totalPalB, totalUS, ownedPalA, ownedPalB, ownedUS, cost, totalCost, percentPalANeeded, percentPalBNeeded, percentUSNeeded;
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


        gameregion();
        readDatabase();
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

        c.close();
        db.close();//close the database

        percentPalAOwned = ((double)ownedPalA / totalPalA) * 100;
        s = String.format("%.2f", percentPalAOwned);
        PalA = "You own " + ownedPalA + " of the " + totalPalA + " Pal A games released.";
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

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

}
