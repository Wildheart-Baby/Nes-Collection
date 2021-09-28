package uk.co.pixoveeware.nes_collection;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    String regionselected, sql, currentregion, licensed, currentcurrency, shelfS;
    int shelfsize;
    ArrayAdapter<CharSequence> adapter, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Settings");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getsettings();
        setregion();


    }

    public void setregion() {
        //region;
        final Spinner region = (Spinner) findViewById(R.id.dropRegion);
        final Spinner currency = (Spinner) findViewById(R.id.dropCurrency);
        final CheckBox unlicensed = (CheckBox) findViewById(R.id.chkUnlicensed);
        final TextView shelfspace = (TextView) findViewById(R.id.txtShelfSize);
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

        shelfspace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                shelfspace.setText("");
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//read the quantity from the quantity text box and adds one to the total


                if (regionselected.contains("Pal A")) {
                    sql = "pal_a_release = 1";
                }
                if (regionselected.contains("Pal A UK")) {
                    sql = "pal_uk_release = 1";
                }
                if (regionselected.contains("Pal B")) {
                    sql = "pal_b_release = 1";
                }
                if (regionselected.contains("Ntsc")) {
                    sql = "ntsc_release = 1";
                }
                if (regionselected.contains("Pal A + Pal B")) {
                    sql = "pal_a_release = 1 or pal_b_release = 1";
                }
                if (regionselected.contains("Pal A + Ntsc")) {
                    sql = "pal_a_release = 1 or ntsc_release = 1";
                }
                if (regionselected.contains("Pal B + Ntsc")) {
                    sql = "pal_b_release = 1 or ntsc_release = 1";
                }
                if (regionselected.contains("All Regions")) {
                    sql = "pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1";
                }

                if (unlicensed.isChecked()){ licensed = " and (unlicensed = 0 or 1)";} else  { licensed = " and (unlicensed = 0)"; }

                shelfS = shelfspace.getText().toString();

                if (shelfS.matches("") || !Character.isDigit(shelfS.charAt(0))) {shelfsize = 9;}
                else {shelfsize = Integer.valueOf(shelfspace.getText().toString());}

                SQLiteDatabase db;//set up the connection to the database
                db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
                String str = "UPDATE settings SET region = '" + sql + "', region_title = '" + regionselected + "',licensed = '" + licensed + "', shelf_size = '" + shelfsize + "' "; //update the database basket field with 8783
                db.execSQL(str);//run the sql command
                db.close();//close the database
                Intent intent = new Intent(Settings.this, MainActivity.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//read the quantity from the quantity text box and adds one to the total
                Intent intent = new Intent(Settings.this, MainActivity.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                finish();
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
                Log.d("Pixo", "Values: " + currentregion + " " + licensed + " " + currentcurrency + " " + shelfsize);
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }
}
