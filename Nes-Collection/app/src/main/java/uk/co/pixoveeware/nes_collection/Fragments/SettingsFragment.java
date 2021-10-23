package uk.co.pixoveeware.nes_collection.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.pixoveeware.nes_collection.activities.About;
import uk.co.pixoveeware.nes_collection.activities.HomeScreenActivity;
import uk.co.pixoveeware.nes_collection.activities.Settings;
import uk.co.pixoveeware.nes_collection.adapters.spinners.PlayedSpinnerAdapter;
import uk.co.pixoveeware.nes_collection.models.GameSettings;
import uk.co.pixoveeware.nes_collection.models.spinners.Data;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    String sql, currentregion, licensed, currentcurrency, shelfS, wherestatement, regionmissing, missingsql, missingchecked, regionmissingcheck;
    int regionselected, shelfsize, showprice, posF, posT, listtype, titles, titlestype, regionSelectedPos;
    ArrayAdapter<CharSequence> adapter, adapter2;
    public Button btnexport, btnimport, ok, cancel;

    private CheckBox unlicensed, showprices;
    private TextView shelfspace;
    private Spinner region, currency;


    public PlayedSpinnerAdapter spinnerAdapter;

    AllGamesViewModel viewM;

    private RadioGroup Titles, List;
    private View v;
    private GameSettings gSettings;

    RadioButton ListView, GalleryView, EuTitles, UsTitles;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        gSettings = viewM.AppSettings();


        if (Build.VERSION.SDK_INT >= 23)
        {
            isStoragePermissionGranted();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getsettings();

        v = inflater.inflate(R.layout.fragment_settings, container, false);
        unlicensed = v.findViewById(R.id.chkUnlicensed);
        shelfspace = v.findViewById(R.id.txtShelfSize);
        showprices = v.findViewById(R.id.chkShowPrices);
        region =  v.findViewById(R.id.dropRegion);
        currency = v.findViewById(R.id.dropCurrency);

        //final CheckBox missingregion = (CheckBox) findViewById(R.id.chkRegionMissing);
        List = v.findViewById(R.id.rgLayout);
        ListView = v.findViewById(R.id.rbListView);
        GalleryView = v.findViewById(R.id.rbGallery);
        Titles = v.findViewById(R.id.rgTitles);
        EuTitles = v.findViewById(R.id.rbTitles);
        UsTitles = v.findViewById(R.id.rbUStitles);
        ok = v.findViewById(R.id.rgnOk);
        cancel = v.findViewById(R.id.rgnCancel);
        btnexport = v.findViewById(R.id.btnExport);
        btnimport = v.findViewById(R.id.btnImport);

        shelfspace.setText(String.valueOf(shelfsize));

        spinnerAdapter = new PlayedSpinnerAdapter(getActivity(), Data.getRegionList());
        region.setAdapter(spinnerAdapter);

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.regionsArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        region.setAdapter(spinnerAdapter);
        int spinnerPosition = adapter.getPosition(currentregion);
        region.setSelection(gSettings.getRegionTitle());

        adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.currencyArray, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        currency.setAdapter(adapter2);
        spinnerPosition = adapter2.getPosition(currentcurrency);
        currency.setSelection(spinnerPosition);

        if (gSettings.getLicensedOrNot().contains(" and (unlicensed = 0 or 1)")) { unlicensed.setChecked(true); } else { unlicensed.setChecked(false);}
        //if (regionmissingcheck.contains("(owned = 0 or 1") ){ missingregion.setChecked(true); } else { missingregion.setChecked(false);}
        if (gSettings.getShowPrice() == 1) { showprices.setChecked(true); } else { showprices.setChecked(false);}
        if (gSettings.getGameView() == 0) {ListView.setChecked(true);} else {GalleryView.setChecked(true);}
        if (gSettings.getUsTitles() == 0) {EuTitles.setChecked(true);} else {UsTitles.setChecked(true);}



        List.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                posF = List.indexOfChild(v.findViewById(checkedId));
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
                posT = Titles.indexOfChild(v.findViewById(checkedId));
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

        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = (int) region.getSelectedItem();
                //regionselected = (String) region.getSelectedItem();
                 regionselected = pos;
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

        // Inflate the layout for this fragment
        return v;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                setSetttings();
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(getActivity(), About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void getsettings(){
        gSettings = viewM.AppSettings();

        currentregion = gSettings.getRegionSql();
        licensed = gSettings.getLicensedOrNot();
        currentcurrency = gSettings.getCurrency();
        shelfsize = gSettings.getShelfSize();
        //wherestatement = gSettings.getWherestatement();
        showprice = gSettings.getShowPrice();
        listtype = gSettings.getGameView();
        titles = gSettings.getUsTitles();
    }

    public void setSetttings(){

        switch(regionselected){
            case 0:
                sql = "(pal_a_release = 1)";
                //regionselected = "Pal A";
                break;
            case 1:
                sql = "(pal_uk_release = 1)";
                //regionselected = "Pal A UK";
                break;
            case 2:
                sql = "(pal_b_release = 1)";
                //regionselected = "Pal B";
                break;
            case 3:
                sql = "(ntsc_release = 1)";
                //regionselected = "US";
                break;
            case 4:
                sql = "(pal_a_release = 1 or pal_b_release = 1)";
                //regionselected = "Europe";
                break;
            case 5:
                sql = " flag_australia = 1";
                //regionselected = "Australia";
                break;
            case 6:
                sql = " flag_austria = 1";
                //regionselected = "Pal A";
                break;
            case 7:
                sql = " flag_australia = 1";
                //regionselected = "Pal A UK";
                break;
            case 8:
                sql = "(pal_b_release = 1)";
                ///regionselected = "Pal B";
                break;
            case 9:
                sql = "(ntsc_release = 1)";
                //regionselected = "US";
                break;
            case 10:
                sql = "(pal_a_release = 1 or pal_b_release = 1)";
                //regionselected = "Europe";
                break;
            case 11:
                sql = " flag_australia = 1";
                //regionselected = "Australia";
                break;
            case 12:
                sql = "(pal_a_release = 1)";
                //regionselected = "Pal A";
                break;
            case 13:
                sql = "(pal_uk_release = 1)";
                //regionselected = "Pal A UK";
                break;
            case 14:
                sql = "(pal_b_release = 1)";
                //regionselected = "Pal B";
                break;
            case 15:
                sql = "(ntsc_release = 1)";
                //regionselected = "US";
                break;
            case 16:
                sql = "(pal_a_release = 1 or pal_b_release = 1)";
                //regionselected = "Europe";
                break;
            case 17:
                sql = " flag_australia = 1";
                //regionselected = "Australia";
                break;
            case 18:
                sql = "(pal_a_release = 1)";
                //regionselected = "Pal A";
                break;
            case 19:
                sql = "(pal_uk_release = 1)";
                //regionselected = "Pal A UK";
                break;
            case 20:
                sql = "(pal_b_release = 1)";
                //regionselected = "Pal B";
                break;
            case 21:
                sql = "(pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1)";
                //regionselected = "All regions";
                break;
            default:
                break;
        }

        /*
        }
        if (regionselected.contains("Austria")) {
            sql = " flag_austria = 1";
            //missingsql = "and (pal_a_cart = 32573 or pal_b_cart = 32573)";
        }
        if (regionselected.contains("Benelux")) {
            sql = " flag_benelux = 1";
            //missingsql = "and (pal_a_cart = 32573 or ntsc_cart = 32573)";
        }
        if (regionselected.contains("Brazil")) {
            sql = " sa_release = 1";
            //missingsql = "and (pal_a_cart = 32573 or ntsc_cart = 32573)";
        }
        if (regionselected.contains("Canada")) {
            sql = " flag_canada = 1";
            //missingsql = "and (pal_b_cart = 32573 or pal_b_cart = 32573)";
        }
        if (regionselected.contains("Denmark")) {
            sql = " flag_denmark = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Finland")) {
            sql = " flag_finland = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("France")) {
            sql = " flag_france = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Germany")) {
            sql = " flag_germany = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Greece")) {
            sql = " flag_greece = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Ireland")) {
            sql = " flag_ireland = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Italy")) {
            sql = " flag_italy = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Norway")) {
            sql = " flag_norway = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Poland")) {
            sql = " flag_poland = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Portugal")) {
            sql = " flag_portugal = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Scandinavia")) {
            sql = " flag_scandinavia = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Spain")) {
            sql = " flag_spain = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Sweden")) {
            sql = " flag_sweden = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains("Switzerland")) {
            sql = " flag_switzerland = 1";
            //missingsql = "and (pal_a_cart = 32573)";
        }
        if (regionselected.contains(getString(R.string.regionname01))) {
            sql = "(pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1)";
            //missingsql = "";
        }*/

        //if (missingregion.isChecked()){ regionmissing = missingsql; missingchecked = "(owned = 0 or 1)";} else {regionmissing = ""; missingchecked = "( owned = 0)";}
        if (unlicensed.isChecked()){ licensed = " and (unlicensed = 0 or 1)";} else  { licensed = " and (unlicensed = 0)"; }
        if (showprices.isChecked()){ showprice = 1;} else  { showprice = 0; }

        shelfS = shelfspace.getText().toString();

        if (shelfS.matches("") || !Character.isDigit(shelfS.charAt(0))) {shelfsize = 9;}
        else {shelfsize = Integer.valueOf(shelfspace.getText().toString());}

        //SQLiteDatabase db;//set up the connection to the database
        //db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        //String str = "UPDATE settings SET region = '" + sql + "', region_title = '" + regionselected + "',region_missing = '" + regionmissing + "',region_missing_check = '" + missingchecked + "',licensed = '" + licensed + "', shelf_size = '" + shelfsize + "',currency = '" + currentcurrency + "',game_view = '" + listtype + "',show_price = '" + showprice + "'"; //update the database basket field with 8783
        String str = "UPDATE settings SET region = '" + sql + "', region_title = '" + regionselected + "',licensed = '" + licensed +
                "', shelf_size = '" + shelfsize + "',currency = '" + currentcurrency + "',game_view = '" + listtype + "',us_titles = '" +
                titlestype +"',show_price = '" + showprice + "'"; //update the database basket field with 8783

        gSettings.setRegionSql(sql);
        gSettings.setRegionTitle(regionselected);
        //gSettings.setNeededGames();
        gSettings.setLicensedOrNot(licensed);
        gSettings.setShelfSize(shelfsize);
        gSettings.setCurrency(currentcurrency);
        gSettings.setGameView(listtype);
        gSettings.setUsTitles(titlestype);
        gSettings.setShowPrice(showprice);

        viewM.updateSettings(gSettings);

        closeEditFragment();
    }


    public void ExportDatabase(){
        //SQLiteDatabase db;//set up the connection to the database
        //db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
       /* Cursor c = null;
        String FOLDER_NAME = "nes_collect";
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME);
        if( !folder.exists() )
            folder.mkdir();
        File sdCardDir = Environment.getExternalStorageDirectory();


        //main code begins here
        try {
            String sql = "select _id, owned, cart, manual, box, pal_a_cart, pal_a_box, pal_a_manual, pal_a_cost, pal_b_cart, pal_b_box, pal_b_manual, pal_b_cost, ntsc_cart, ntsc_box, ntsc_manual,  ntsc_cost, price, favourite, wishlist, onshelf, pal_a_owned, pal_b_owned, ntsc_owned, finished_game from eu where owned = 1 or favourite = 1 or wishlist = 1 or finished_game = 1";
            //c = db.rawQuery(sql, null);
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
                {
                    Toast toast = Toast.makeText(getContext(),
                            "Exported Successfully to " + FOLDER_NAME + ".",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            if (db.isOpen()) {
                db.close();
                {Toast toast = Toast.makeText(getContext(),
                        ex.getMessage().toString(),
                        Toast.LENGTH_SHORT);
                    toast.show();}
            }
        } finally {
        }
        db.close();*/

    }

    public void ImportDatabase(){
        /*SQLiteDatabase db;//set up the connection to the database
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
            {Toast toast = Toast.makeText(getContext(),
                    "Imported Successfully from " + FOLDER_NAME + ".",
                    Toast.LENGTH_SHORT);
                toast.show();}

        } catch (Exception ex){
            if (db.isOpen()) {
                db.close();
                {Toast toast = Toast.makeText(getContext(),
                        ex.getMessage().toString(),
                        Toast.LENGTH_SHORT);
                    toast.show();}
            }
        } finally {

        }

        db.close();*/
    }

    public boolean isStoragePermissionGranted() {
        Boolean pg;
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                pg = true;
            } else {
                pg = false;
            }

        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Request permission","Permission is granted");
            pg = true;
        }
        return pg;
    }

    public void file_checks(){
        String FOLDER_NAME = "nes_collect";
        File sdCardDir = Environment.getExternalStorageDirectory();
        String filename = "GamesBackUp.csv"; // the name of the file to import
        File file = new File(sdCardDir+ File.separator + FOLDER_NAME, filename);
        if(!file.exists()){
            //do something
            btnimport.setEnabled(false);
        }

        if (viewM.OwnedGamesCount() == 0){
            btnexport.setEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        SetTitles();
    }


    private void SetTitles(){
        getActivity().setTitle("Settings");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(null);
    }

    public void closeEditFragment(){
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }

}