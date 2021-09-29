package uk.co.pixoveeware.nes_collection;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Search extends AppCompatActivity {

    String searchterm, fieldname, sql, searchname, wherestatement;
    int titles;
    ArrayAdapter<CharSequence> adapter, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView searchTerm = (TextView) findViewById(R.id.txtSearch);
        setSupportActionBar(toolbar);
        gameregion();
        search();
        setTitle("Game search");

        searchTerm.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    CheckBox all = (CheckBox) findViewById(R.id.chkAllRegions);
                    if (fieldname.contains("name")){
                        searchterm = String.valueOf(searchTerm.getText().toString());//get the name from the item name text box
                        if (all.isChecked()) {
                            sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'" + wherestatement + "";
                        }else{ sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'"; }
                    }else if (fieldname.contains("synopsis")){
                        searchterm = String.valueOf(searchTerm.getText().toString());//get the name from the item name text box
                        if (all.isChecked()) {
                            sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'" + wherestatement + "";
                        }else{ sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'"; }
                    }


                    Intent intent = new Intent(Search.this, SearchResults.class);//opens a new screen when the shopping list is clicked

                    intent.putExtra("sqlstatement", sql);
                    intent.putExtra("searchterm", fieldname);
                    intent.putExtra("pagetitle", "Search results");
                    startActivity(intent);//start the new screen
                    finish();

                    sql = "";

                    return true;
                }
                return false;
            }
        });

    }


    public void search(){

        final Spinner field = (Spinner) findViewById(R.id.field_name);
        final TextView searchTerm = (TextView) findViewById(R.id.txtSearch);//sets up the dialog title
        final Button ok = (Button) findViewById(R.id.rgnOk);
        final Button cancel = (Button) findViewById(R.id.rgnCancel);
        final CheckBox all = (CheckBox) findViewById(R.id.chkAllRegions);

        //String[] fieldnames = {"name", "publisher", "genre", "subgenre" , "developer", "synopsis"};
        //ArrayAdapter<String> wsaa1 = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, fieldnames);

        //wsaa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //field.setAdapter(wsaa1);

        adapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.searchArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        field.setAdapter(adapter);

        field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fieldname = (String) field.getSelectedItem();
                setfieldselection();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//read the quantity from the quantity text box and adds one to the total

                if (fieldname.contains("name")){
                        if (titles == 0){ fieldname = "name";} else if (titles == 1 ){ fieldname = "us_name"; }
                        searchterm = String.valueOf(searchTerm.getText().toString());//get the name from the item name text box
                        if (all.isChecked()) {
                            sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'" + wherestatement + "";
                        }else{ sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'"; }
                }else if (fieldname.contains("synopsis")){
                    searchterm = String.valueOf(searchTerm.getText().toString());//get the name from the item name text box
                    if (all.isChecked()) {
                            sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'" + wherestatement + "";
                        }else{ sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'"; }
                }


                Intent intent = new Intent(Search.this, SearchResults.class);//opens a new screen when the shopping list is clicked
                //intent.putExtra("columnname", fieldname);//passes the table name to the new screen
                //intent.putExtra("search", searchterm);//passes the table name to the new screen
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("searchterm", fieldname);
                intent.putExtra("pagetitle", "Search results");
                Log.d("search", "titles: " + titles + " sql is: " + sql);
                startActivity(intent);//start the new screen
                finish();

                sql = "";
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setfieldselection() {
        final Spinner namesearch = (Spinner) findViewById(R.id.field_selection);
        TextView seconddd = (TextView) findViewById(R.id.lblSubGenre);

        if (fieldname.contains("name")) {
            namesearch.setVisibility(View.INVISIBLE);
            seconddd.setVisibility(View.INVISIBLE);
        } else if (fieldname.contains("publisher")) {
            namesearch.setVisibility(View.VISIBLE);
            seconddd.setVisibility(View.VISIBLE);
            seconddd.setText("Publisher");
            search = ArrayAdapter.createFromResource(getBaseContext(),
                    R.array.publisherArray, android.R.layout.simple_spinner_item);
            search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesearch.setAdapter(search);
        } else if (fieldname.contains("developer")) {
            namesearch.setVisibility(View.VISIBLE);
            seconddd.setVisibility(View.VISIBLE);
            seconddd.setText("Developer");
            search = ArrayAdapter.createFromResource(getBaseContext(),
                    R.array.developerArray, android.R.layout.simple_spinner_item);
            search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesearch.setAdapter(search);
        } else if (fieldname.contains("year")) {
            namesearch.setVisibility(View.VISIBLE);
            seconddd.setVisibility(View.VISIBLE);
            seconddd.setText("Year");
            search = ArrayAdapter.createFromResource(getBaseContext(),
                    R.array.yearArray, android.R.layout.simple_spinner_item);
            search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesearch.setAdapter(search);
        } else if (fieldname.contains("subgenre")) {
            namesearch.setVisibility(View.VISIBLE);
            seconddd.setVisibility(View.VISIBLE);
            seconddd.setText("Sub genre");
            search = ArrayAdapter.createFromResource(getBaseContext(),
                    R.array.subgenreArray, android.R.layout.simple_spinner_item);
            search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesearch.setAdapter(search);
        } else if (fieldname.contains("genre")) {
            namesearch.setVisibility(View.VISIBLE);
            seconddd.setVisibility(View.VISIBLE);
            seconddd.setText("Genre");
            search = ArrayAdapter.createFromResource(getBaseContext(),
                    R.array.genreArray, android.R.layout.simple_spinner_item);
            search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesearch.setAdapter(search);
        } else if (fieldname.contains("synopsis")) {
            namesearch.setVisibility(View.INVISIBLE);
            seconddd.setVisibility(View.INVISIBLE);
        }


        namesearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchterm = (String) namesearch.getSelectedItem();
                sql = "select * from eu where " + fieldname + " = '" + searchterm + "';";
                Log.d("'pixo", sql);
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });
    }

    public void gameregion(){//selects the region from the database

        SQLiteDatabase db;//sets up the connection to the database
        db = openOrCreateDatabase("nes.sqlite",MODE_PRIVATE,null);//open or create the database
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = "and ( " + (c.getString(c.getColumnIndex("region"))) + ")";
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                //title = (c.getString(c.getColumnIndex("region_title")));

                Log.d("Pixo", wherestatement);
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
    }

    }

