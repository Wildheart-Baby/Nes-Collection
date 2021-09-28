package uk.co.pixoveeware.nes_collection;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Search extends AppCompatActivity {

    String searchterm, fieldname, sql;
    ArrayAdapter<CharSequence> adapter, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search();

    }


    public void search(){

        final Spinner field = (Spinner) findViewById(R.id.field_name);
        final TextView searchTerm = (TextView) findViewById(R.id.txtSearch);//sets up the dialog title
        final Button ok = (Button) findViewById(R.id.rgnOk);
        final Button cancel = (Button) findViewById(R.id.rgnCancel);

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
                    searchterm = String.valueOf(searchTerm.getText().toString());//get the name from the item name text box
                    sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'";
                }
                Intent intent = new Intent(Search.this, SearchResults.class);//opens a new screen when the shopping list is clicked
                //intent.putExtra("columnname", fieldname);//passes the table name to the new screen
                //intent.putExtra("search", searchterm);//passes the table name to the new screen
                intent.putExtra("sqlstatement", sql);
                startActivity(intent);//start the new screen
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

    //<item>genre</item>
    //<item>subgenre</item>
    //<item>developer</item>
    //<item>synopsis</item>

    public void setfieldselection(){
        final Spinner namesearch = (Spinner) findViewById(R.id.field_selection);


        if (fieldname.contains("name")){
            //searchterm = String.valueOf(searchTerm.getText().toString());//get the name from the item name text box
            //sql = "SELECT * FROM eu WHERE " + fieldname + " like '%" + searchterm + "%'";

        }else if (fieldname.contains("publisher")) {
            namesearch.setVisibility(View.VISIBLE);
            search = ArrayAdapter.createFromResource(getBaseContext(),
                    R.array.publisherArray, android.R.layout.simple_spinner_item);
            search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesearch.setAdapter(search);
        }else if(fieldname.contains("developer")) {
            namesearch.setVisibility(View.VISIBLE);
            search = ArrayAdapter.createFromResource(getBaseContext(),
                    R.array.developerArray, android.R.layout.simple_spinner_item);
            search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesearch.setAdapter(search);
        }else if(fieldname.contains("year")) {
            namesearch.setVisibility(View.VISIBLE);
            search = ArrayAdapter.createFromResource(getBaseContext(),
                    R.array.yearArray, android.R.layout.simple_spinner_item);
            search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesearch.setAdapter(search);
        }


        namesearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchterm = (String) namesearch.getSelectedItem();
                sql = "select * from eu where " + fieldname + " = '" + searchterm + "';";
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });


    }
}
