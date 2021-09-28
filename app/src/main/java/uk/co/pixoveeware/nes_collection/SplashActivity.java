package uk.co.pixoveeware.nes_collection;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Wildheart on 24/06/2016.
 */
public class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            checkDataBase();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    public void checkDataBase(){
        File dbfile = getApplicationContext().getDatabasePath("nes.sqlite");

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

}