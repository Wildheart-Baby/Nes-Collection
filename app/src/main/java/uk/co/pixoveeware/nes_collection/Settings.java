package uk.co.pixoveeware.nes_collection;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mScreenTitles;

    String regionselected, sql, currentregion, licensed, currentcurrency, shelfS, wherestatement;
    int shelfsize;
    ArrayAdapter<CharSequence> adapter, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTitle = mDrawerTitle = "Nes Collect";
        mScreenTitles = getResources().getStringArray(R.array.screenArray);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mScreenTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
        getSupportActionBar().setHomeButtonEnabled(true);

        getsettings();
        setregion();

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle("Nes Collect");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_allgames, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
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


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            selectItem(pos);
            Log.d("Pixo", "value: " + pos);
        }
    }

    private void selectItem(int pos) {
        // update the main content by replacing fragments
        Log.d("Pixo", "value: " + pos);
        switch(pos){
            case '0' :
                Log.d("Pixo", "selectItem running");
                Intent intent = new Intent(this, AllGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
                break;
            case 1 :
                intent = new Intent(this, NeededGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
                break;
            case 2 :
                intent = new Intent(this, OwnedGames.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("wherestatement", wherestatement);
                startActivity(intent);
                break;
            case 3 :
                intent = new Intent(this, FavouriteGames.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                break;
            case 4 :
                intent = new Intent(this, WishList.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                break;
            case 5 :
                intent = new Intent(this, ShelfOrder.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                break;
            case 6 :
                intent = new Intent(this, Statistics.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);
                break;
            case 7 :
                intent = new Intent(this, Settings.class);//opens a new screen when the shopping list is clicked
                startActivity(intent);//start the new screen
                break;
            default:
        }


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(pos, true);
        //setTitle(mScreenTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
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
                Log.d("Pixo", "Values: " + currentregion + " " + licensed + " " + currentcurrency + " " + shelfsize);
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }
}
