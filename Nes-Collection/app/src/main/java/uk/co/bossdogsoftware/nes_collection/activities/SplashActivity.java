package uk.co.bossdogsoftware.nes_collection.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Wildheart on 24/06/2016.
 */
public class SplashActivity extends AppCompatActivity {

    File dbfile;
    SharedPreferences prefs = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            prefs = getSharedPreferences("uk.co.pixoveeware.nes_collection", MODE_PRIVATE);

            checkDataBase();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    public void checkDataBase(){

        dbfile = getApplicationContext().getDatabasePath("nes.sqlite");

        if(!dbfile.exists())
        {

            try {
                SQLiteDatabase checkDB = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);
                //Toast toast = Toast.makeText(getApplicationContext(),
                //        "Creating blank database",
                //        Toast.LENGTH_SHORT);
                //toast.show();
                if(checkDB != null){
                    //Toast toast2 = Toast.makeText(getApplicationContext(),
                    //        "Checking blank database",
                    //        Toast.LENGTH_SHORT);
                    //toast2.show();
                    checkDB.close();
                    copyDataBase();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
           //UpdateDatabase();
        }
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
        //Toast toast = Toast.makeText(getApplicationContext(),
        //        "Creating nes database",
        //        Toast.LENGTH_SHORT);
        //toast.show();
    }

    public void ExportDatabase(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        Cursor c = null;
        String FOLDER_NAME = "nes_collect";
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME);
        if( !folder.exists() )
            folder.mkdir();
        File sdCardDir = Environment.getExternalStorageDirectory();


        //main code begins here
        try {
            String sql = "select _id, owned, cart, manual, box, pal_a_cart, pal_a_box, pal_a_manual, pal_a_cost, pal_b_cart, pal_b_box, pal_b_manual, pal_b_cost, ntsc_cart, ntsc_box, ntsc_manual,  ntsc_cost, price, favourite, wishlist, onshelf, pal_a_owned, pal_b_owned, ntsc_owned, finished_game from eu where owned = 1 or favourite = 1 or wishlist = 1 or finished_game = 1";
            c = db.rawQuery(sql, null);
            int rowcount = 0;
            int colcount = 0;


            String filename = "GamesBackUp.csv"; // the name of the file to export with

            File saveFile = new File(sdCardDir+ File.separator + FOLDER_NAME, filename);
            FileWriter fw = new FileWriter(saveFile);
            BufferedWriter bw = new BufferedWriter(fw);

            rowcount = c.getCount();
            colcount = c.getColumnCount();

            if (rowcount > 0) {
                c.moveToFirst();
                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        bw.write(c.getColumnName(i) + ",");
                    } else {
                        bw.write(c.getColumnName(i));
                    }
                }
                bw.newLine();
                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {

                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ",");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
                {Toast toast = Toast.makeText(getApplicationContext(),
                        "Exported Successfully to " + FOLDER_NAME + ".",
                        Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            if (db.isOpen()) {
                db.close();
                /*{Toast toast = Toast.makeText(getApplicationContext(),
                        ex.getMessage().toString(),
                        Toast.LENGTH_SHORT);
                    toast.show();}*/
            }
        } finally {
        }
        db.close();

    }

    public void ImportDatabase(){
        SQLiteDatabase db;//set up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String FOLDER_NAME = "nes_collect";
        //Cursor c = null;
        String gameid, own, cartridge, man, boxed, palAcart, palAbox, palAmanual, palAcost, palBcart, palBbox, palBmanual, palBcost, UScart, USbox, USmanual,  UScost, gameprice, fave, wish, ontheshelf, palAowned, palBowned, USowned, finished;
        try {
            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "GamesBackUp.csv"; // the name of the file to import

            File openFile = new File(sdCardDir+ File.separator + FOLDER_NAME, filename);
            FileReader fr = new FileReader(openFile);
            BufferedReader br = new BufferedReader(fr);

            br.readLine();
            String line = null;
            while ((line = br.readLine()) != null) {
                String row[] = line.split(",");
                gameid = row[0];
                own = row[1];
                cartridge = row[2];
                man = row[3];
                boxed = row[4];
                palAcart = row[5];
                palAbox = row[6];
                palAmanual = row[7];
                palAcost = row[8];
                palBcart = row[9];
                palBbox = row[10];
                palBmanual = row[11];
                palBcost = row[12];
                UScart = row[13];
                USbox = row[14];
                USmanual = row[15];
                UScost = row[16];
                gameprice = row[17];
                fave = row[18];
                wish = row[19];
                ontheshelf = row[20];
                palAowned = row[21];
                palBowned = row[22];
                USowned = row[23];
                finished = row[24];

                String str = "UPDATE eu SET owned = " + own + ", cart = " + cartridge + ", box = " + boxed + ", manual = " + man + ", pal_a_cart = " + palAcart + ", pal_a_box = " + palAbox + ", pal_a_manual = " + palAmanual + ", pal_b_cart = " + palBcart + ", pal_b_box = " + palBbox + ", pal_b_manual = " + palBmanual + ", ntsc_cart = " + UScart + ", ntsc_box = " + USbox + ",  ntsc_manual = " + USmanual + ",  pal_a_cost = " + palAcost + ",  pal_b_cost = " + palBcost + ",  ntsc_cost = " + UScost + ",  favourite = " + fave + ",price = " + gameprice + ",  pal_a_owned = " + palAowned +  ",  pal_b_owned = " + palBowned +  ",  ntsc_owned = " + USowned +  ",  wishlist = " + wish + ", onshelf = " + ontheshelf + ", finished_game = " + finished + " where _id = " + gameid + " "; //update the database basket field with 8783
                Log.d("pixovee", str);
                db.execSQL(str);//run the sql command
            }
            /*{Toast toast = Toast.makeText(getApplicationContext(),
                    "Imported Successfully from " + FOLDER_NAME + ".",
                    Toast.LENGTH_SHORT);
                toast.show();}*/

        } catch (Exception ex){
            if (db.isOpen()) {
                db.close();
                {Toast toast = Toast.makeText(getApplicationContext(),
                        ex.getMessage().toString(),
                        Toast.LENGTH_SHORT);
                    toast.show();}
            }
        } finally {

        }

        db.close();
    }

    public void UpdateDatabase() {
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs

        if (dbfile.exists()) {
            ExportDatabase();
            dbfile.delete();
            try {
                SQLiteDatabase checkDB = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);
                //Toast toast = Toast.makeText(getApplicationContext(),
                //        "Creating blank database",
                //        Toast.LENGTH_SHORT);
                //toast.show();
                if(checkDB != null){
                    //Toast toast2 = Toast.makeText(getApplicationContext(),
                    //        "Checking blank database",
                    //        Toast.LENGTH_SHORT);
                    //toast2.show();
                    checkDB.close();
                    copyDataBase();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            ImportDatabase();
        }
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

}