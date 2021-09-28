package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    String DB_NAME;
    Context context;

    String searchterm,fieldname, wherestatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //File dbfile = new File(this.getApplicationContext().getFilesDir().getPath()+ "nes.sqlite");

        ImageButton AllGames = (ImageButton) findViewById(R.id.btnAllGames);
        ImageButton OwnedGames = (ImageButton) findViewById(R.id.btnowned);
        ImageButton NeededGames = (ImageButton) findViewById(R.id.btnneeded);
        ImageButton FavouriteGames = (ImageButton) findViewById(R.id.btnFavouriteGames);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AllGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
                //finish();
            }
        });
        OwnedGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OwnedGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
               // finish();
            }
       });

        NeededGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NeededGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
               // finish();
            }
        });

        FavouriteGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavouriteGames.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                // finish();
            }
        });

        checkDataBase();
        gameregion();
    }


    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = this.getAssets().open("nes.sqlite");
        // Path to the just created empty db

        String outFileName = getApplicationInfo().dataDir + "/databases/" + "nes.sqlite";
        //String outFileName =  "/data/data/" +getApplicationContext().getPackageName() + "/databases/" + "nes.sqlite";
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);

        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Creating nes database",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void checkDataBase(){
        File dbfile = getApplicationContext().getDatabasePath("nes.sqlite");

        if(!dbfile.exists())
        {

            try {
                SQLiteDatabase checkDB = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Creating blank database",
                        Toast.LENGTH_SHORT);
                toast.show();
                if(checkDB != null){
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Checking blank database",
                            Toast.LENGTH_SHORT);
                    toast2.show();
                    checkDB.close();
                    copyDataBase();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT region FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                if(wherestatement.contains("pal_a_release = 1")){setTitle("Pal A Games");}
                else if(wherestatement.contains("pal_b_release = 1")){setTitle("Pal B Games");}
                else if(wherestatement.contains("ntsc_release = 1")){setTitle("US Games");}

                Log.d("Pixo", wherestatement);
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

}
