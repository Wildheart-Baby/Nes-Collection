package uk.co.pixoveeware.nes_collection;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Settings extends AppCompatActivity {

    String regionselected, sql, currentregion, licensed, currentcurrency, shelfS, wherestatement, regionmissing, missingsql, missingchecked, regionmissingcheck;
    int shelfsize, showprice, posF, posT, listtype, titles, total_games, titlestype;
    ArrayAdapter<CharSequence> adapter, adapter2;
    Button btnexport, btnimport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (Build.VERSION.SDK_INT >= 23)
        {
           isStoragePermissionGranted();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Settings");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnexport = (Button) findViewById(R.id.btnExport);
        btnimport = (Button) findViewById(R.id.btnImport);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-0537596348696744~2585816192");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getsettings();
        setregion();

        btnexport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ExportDatabase();
            }
        });
        btnimport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImportDatabase();
            }
        });

        file_checks();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                setSetttings();
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(this, About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void setregion() {
        //region;
        final Spinner region = (Spinner) findViewById(R.id.dropRegion);
        final Spinner currency = (Spinner) findViewById(R.id.dropCurrency);
        final CheckBox unlicensed = (CheckBox) findViewById(R.id.chkUnlicensed);
        final TextView shelfspace = (TextView) findViewById(R.id.txtShelfSize);
        final CheckBox showprices = (CheckBox) findViewById(R.id.chkShowPrices);
        //final CheckBox missingregion = (CheckBox) findViewById(R.id.chkRegionMissing);
        final RadioGroup List = (RadioGroup) findViewById(R.id.rgLayout);
        RadioButton ListView = (RadioButton) findViewById(R.id.rbListView);
        RadioButton GalleryView = (RadioButton) findViewById(R.id.rbGallery);
        final RadioGroup Titles = (RadioGroup) findViewById(R.id.rgTitles);
        RadioButton EuTitles = (RadioButton) findViewById(R.id.rbTitles);
        RadioButton UsTitles = (RadioButton) findViewById(R.id.rbUStitles);
        Button ok = (Button) findViewById(R.id.rgnOk);
        Button cancel = (Button) findViewById(R.id.rgnCancel);

        shelfspace.setText(String.valueOf(shelfsize));


        adapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.regionsArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        region.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(currentregion);
        region.setSelection(spinnerPosition);

        adapter2 = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.currencyArray, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        currency.setAdapter(adapter2);
        spinnerPosition = adapter2.getPosition(currentcurrency);
        currency.setSelection(spinnerPosition);

        if (licensed.contains(" and (unlicensed = 0 or 1)")) { unlicensed.setChecked(true); } else { unlicensed.setChecked(false);}
        //if (regionmissingcheck.contains("(owned = 0 or 1") ){ missingregion.setChecked(true); } else { missingregion.setChecked(false);}
        if (showprice == 1) { showprices.setChecked(true); } else { showprices.setChecked(false);}
        if (listtype == 0) {ListView.setChecked(true);} else {GalleryView.setChecked(true);}
        if (titles == 0) {EuTitles.setChecked(true);} else {UsTitles.setChecked(true);}

        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                regionselected = (String) region.getSelectedItem();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });

        currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentcurrency = (String) currency.getSelectedItem();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });

        List.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                posF = List.indexOfChild(findViewById(checkedId));
                switch (posF) {
                    case 0:
                        listtype = 0;
                        break;
                    case 1:
                        listtype = 1;
                        break;
                }
            }
        });

        Titles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                posT = Titles.indexOfChild(findViewById(checkedId));
                switch (posT) {
                    case 0:
                        titlestype = 0;
                        break;
                    case 1:
                        titlestype = 1;
                        break;
                }
            }
        });

        shelfspace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                shelfspace.setText("");
            }
        });
    }

    public void getsettings(){
        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                currentregion = (c.getString(c.getColumnIndex("region_title")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                currentcurrency = (c.getString(c.getColumnIndex("currency")));
                shelfsize = (c.getInt(c.getColumnIndex("shelf_size")));
                wherestatement = (c.getString(c.getColumnIndex("region")));
                showprice = (c.getInt(c.getColumnIndex("show_price")));
                listtype = (c.getInt(c.getColumnIndex("game_view")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                //regionmissingcheck = (c.getString(c.getColumnIndex("region_missing_check")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    public void setSetttings(){
        final CheckBox unlicensed = (CheckBox) findViewById(R.id.chkUnlicensed);
        final TextView shelfspace = (TextView) findViewById(R.id.txtShelfSize);
        final CheckBox showprices = (CheckBox) findViewById(R.id.chkShowPrices);
        final CheckBox missingregion = (CheckBox) findViewById(R.id.chkRegionMissing);

            if (regionselected.contains("Pal A")) {
                sql = "(pal_a_release = 1)";
                //missingsql = "and (pal_a_cart = 32573)";
            }
            if (regionselected.contains("Pal A UK")) {
                sql = "(pal_uk_release = 1)";
                //missingsql = "and (pal_a_cart = 32573)";
            }
            if (regionselected.contains("Pal B")) {
                sql = "(pal_b_release = 1)";
                //missingsql = "and (pal_b_cart = 32573)";
            }
            if (regionselected.contains("Ntsc")) {
                sql = "(ntsc_release = 1)";
                //missingsql = "and (ntsc_cart = 32573)";
            }
            if (regionselected.contains("Pal A & B")) {
                sql = "(pal_a_release = 1 or pal_b_release = 1)";
                //missingsql = "and (pal_a_cart = 32573 or pal_b_cart = 32573)";
            }
            if (regionselected.contains("Pal A & Ntsc")) {
                sql = "(pal_a_release = 1 or ntsc_release = 1)";
                //missingsql = "and (pal_a_cart = 32573 or ntsc_cart = 32573)";
            }
            if (regionselected.contains("Pal B & Ntsc")) {
                sql = "(pal_b_release = 1 or ntsc_release = 1)";
                //missingsql = "and (pal_b_cart = 32573 or pal_b_cart = 32573)";
            }
            if (regionselected.contains(getString(R.string.regionname01))) {
                sql = "(pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1)";
                //missingsql = "";
            }

            //if (missingregion.isChecked()){ regionmissing = missingsql; missingchecked = "(owned = 0 or 1)";} else {regionmissing = ""; missingchecked = "( owned = 0)";}
            if (unlicensed.isChecked()){ licensed = " and (unlicensed = 0 or 1)";} else  { licensed = " and (unlicensed = 0)"; }
            if (showprices.isChecked()){ showprice = 1;} else  { showprice = 0; }

            shelfS = shelfspace.getText().toString();

            if (shelfS.matches("") || !Character.isDigit(shelfS.charAt(0))) {shelfsize = 9;}
            else {shelfsize = Integer.valueOf(shelfspace.getText().toString());}

            SQLiteDatabase db;//set up the connection to the database
            db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
            //String str = "UPDATE settings SET region = '" + sql + "', region_title = '" + regionselected + "',region_missing = '" + regionmissing + "',region_missing_check = '" + missingchecked + "',licensed = '" + licensed + "', shelf_size = '" + shelfsize + "',currency = '" + currentcurrency + "',game_view = '" + listtype + "',show_price = '" + showprice + "'"; //update the database basket field with 8783
            String str = "UPDATE settings SET region = '" + sql + "', region_title = '" + regionselected + "',licensed = '" + licensed + "', shelf_size = '" + shelfsize + "',currency = '" + currentcurrency + "',game_view = '" + listtype + "',us_titles = '" + titlestype +"',show_price = '" + showprice + "'"; //update the database basket field with 8783
        Log.d("settings", str);
        db.execSQL(str);//run the sql command
            db.close();//close the database
            Intent intent = new Intent(Settings.this, MainActivity.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
            finish();

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
                        btnimport.setEnabled(true);
                        {Toast toast = Toast.makeText(getApplicationContext(),
                                "Exported Successfully to " + FOLDER_NAME + ".",
                                Toast.LENGTH_SHORT);
                            toast.show();
                            }
                    }
                } catch (Exception ex) {
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
            btnexport.setEnabled(true);
            {Toast toast = Toast.makeText(getApplicationContext(),
                    "Imported Successfully from " + FOLDER_NAME + ".",
                    Toast.LENGTH_SHORT);
                toast.show();}

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

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Request permission","Permission is granted");
                return true;
            } else {

                Log.v("Request permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Request permission","Permission is granted");
            return true;
        }
    }

    public void file_checks(){
        String FOLDER_NAME = "nes_collect";
        File sdCardDir = Environment.getExternalStorageDirectory();
        String filename = "GamesBackUp.csv"; // the name of the file to import
        File file = new File(sdCardDir+ File.separator + FOLDER_NAME, filename);
        if(!file.exists()){
        //do something
            btnimport.setEnabled(false);
        } else {}

        sql = "SELECT * FROM eu where owned = 1";
        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        Cursor c = db.rawQuery(sql, null);
        total_games = c.getCount();
        c.close();
        db.close();

        if (total_games == 0){
            btnexport.setEnabled(false);
        } else {}
    }

}
