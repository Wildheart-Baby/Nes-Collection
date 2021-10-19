package uk.co.pixoveeware.nes_collection.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.activities.HomeScreenActivity;
import uk.co.pixoveeware.nes_collection.models.AllGameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;

/**
 * Created by Wildheart on 10/10/2018.
 */

public class FillGamesAdapter {

    String currentgroup, prevgroup, wherestatement, orderby, groupHeader, theimage, thename, licensed, title, currency;
    SQLiteDatabase db;
    int indexpos, titles;
    Cursor c;

    public FillGamesAdapter(Context context){

        gameregion(context);


        db = context.openOrCreateDatabase("nes.sqlite", context.MODE_PRIVATE, null);//open or create the database


        //if (HomeScreenActivity.nesList == null){ HomeScreenActivity.readList(); }
        HomeScreenActivity.gamesList.clear();//clear the shoppingList array
        HomeScreenActivity.indexList = new ArrayList<GameItemsIndex>();
        HomeScreenActivity.indexList.clear();
        indexpos = 0;
        //SQLiteDatabase db;//sets up the connection to the database
        //db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        //sql = "SELECT * FROM eu where " + wherestatement + licensed +  "";
        //Log.d("Pixo", sql);
        c = db.rawQuery(HomeScreenActivity.sqlstatement, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                AllGameItems nesListItems = new AllGameItems();//creates a new array
                GameItemsIndex indexListItems = new GameItemsIndex();
                currentgroup = c.getString(c.getColumnIndex("groupheader"));

                if(!currentgroup.equals(prevgroup)){
                    nesListItems.setGroup(c.getString(c.getColumnIndex("groupheader")));
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    nesListItems.setName(c.getString(c.getColumnIndex(thename)));
                    nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                    nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    nesListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                    nesListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                    nesListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                    nesListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                    nesListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                    nesListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                    nesListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                    nesListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                    nesListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                    nesListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                    nesListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                    nesListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                    nesListItems.setGamePrice(c.getDouble(c.getColumnIndex("price")));
                    nesListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    nesListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    nesListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    nesListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    nesListItems.setYear(c.getString(c.getColumnIndex("year"))); //re-edit this after testing
                    nesListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                    nesListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                    nesListItems.setDeveloper((c.getString(c.getColumnIndex("developer"))));
                    nesListItems.setSynopsis((c.getString(c.getColumnIndex("synopsis"))));
                    nesListItems.setAustralia(c.getInt(c.getColumnIndex("flag_australia")));
                    nesListItems.setAustria(c.getInt(c.getColumnIndex("flag_austria")));
                    nesListItems.setBenelux(c.getInt(c.getColumnIndex("flag_benelux")));
                    nesListItems.setDenmark(c.getInt(c.getColumnIndex("flag_denmark")));
                    nesListItems.setFinland(c.getInt(c.getColumnIndex("flag_finland")));
                    nesListItems.setFrance(c.getInt(c.getColumnIndex("flag_france")));
                    nesListItems.setGermany((c.getInt(c.getColumnIndex("flag_germany"))));
                    nesListItems.setGreece(c.getInt(c.getColumnIndex("flag_greece")));
                    nesListItems.setIreland(c.getInt(c.getColumnIndex("flag_ireland")));
                    nesListItems.setItaly(c.getInt(c.getColumnIndex("flag_italy")));
                    nesListItems.setNorway(c.getInt(c.getColumnIndex("flag_norway")));
                    nesListItems.setPoland(c.getInt(c.getColumnIndex("flag_poland")));
                    nesListItems.setPortugal(c.getInt(c.getColumnIndex("flag_portugal")));
                    nesListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                    nesListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                    nesListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                    nesListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                    nesListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));
                    nesListItems.setCurrency(currency);
                    HomeScreenActivity.gamesList.add(nesListItems);//add items to the arraylist
                    indexListItems.setItemid(indexpos);
                    indexListItems.setLetter(c.getString(c.getColumnIndex("groupheader")));
                    HomeScreenActivity.indexList.add(indexListItems);
                    Log.i("index pos", " " + indexpos);
                    indexpos = indexpos +1;
                    prevgroup = c.getString(c.getColumnIndex("groupheader"));
                }
                else if(currentgroup.equals(prevgroup)){
                    nesListItems.setGroup("no");
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    nesListItems.setName(c.getString(c.getColumnIndex(thename)));
                    nesListItems.setPublisher(c.getString(c.getColumnIndex("publisher")));
                    nesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    nesListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                    nesListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                    nesListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                    nesListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                    nesListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                    nesListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                    nesListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                    nesListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                    nesListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                    nesListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                    nesListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                    nesListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                    nesListItems.setGamePrice(c.getDouble(c.getColumnIndex("price")));
                    nesListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    nesListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    nesListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    nesListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    nesListItems.setYear(c.getString(c.getColumnIndex("year"))); //re-edit this after testing
                    nesListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                    nesListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                    nesListItems.setDeveloper((c.getString(c.getColumnIndex("developer"))));
                    nesListItems.setSynopsis((c.getString(c.getColumnIndex("synopsis"))));
                    nesListItems.setAustralia(c.getInt(c.getColumnIndex("flag_australia")));
                    nesListItems.setAustria(c.getInt(c.getColumnIndex("flag_austria")));
                    nesListItems.setBenelux(c.getInt(c.getColumnIndex("flag_benelux")));
                    nesListItems.setDenmark(c.getInt(c.getColumnIndex("flag_denmark")));
                    nesListItems.setFinland(c.getInt(c.getColumnIndex("flag_finland")));
                    nesListItems.setFrance(c.getInt(c.getColumnIndex("flag_france")));
                    nesListItems.setGermany((c.getInt(c.getColumnIndex("flag_germany"))));
                    nesListItems.setGreece(c.getInt(c.getColumnIndex("flag_greece")));
                    nesListItems.setIreland(c.getInt(c.getColumnIndex("flag_ireland")));
                    nesListItems.setItaly(c.getInt(c.getColumnIndex("flag_italy")));
                    nesListItems.setNorway(c.getInt(c.getColumnIndex("flag_norway")));
                    nesListItems.setPoland(c.getInt(c.getColumnIndex("flag_poland")));
                    nesListItems.setPortugal(c.getInt(c.getColumnIndex("flag_portugal")));
                    nesListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                    nesListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                    nesListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                    nesListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                    nesListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));
                    nesListItems.setCurrency(currency);
                    HomeScreenActivity.gamesList.add(nesListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex("groupheader"));
                    indexpos = indexpos +1;
                }
                c.moveToNext();//move to the next record
            }
            //c = db.rawQuery(sql, null);
            HomeScreenActivity.totalGames = c.getCount();
            c.close();//close the cursor
        }
        //Cursor c = db.rawQuery("SELECT ID, ITEM, QUANTITY, DEPARTMENT, BASKET FROM " + fname, null);
        db.close();//close the database

    }

    public void gameregion(Context context){//selects the region from the database
        db = context.openOrCreateDatabase("nes.sqlite", context.MODE_PRIVATE, null);//open or create the database
        c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                title = (c.getString(c.getColumnIndex("region_title")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                HomeScreenActivity.viewas = (c.getInt(c.getColumnIndex("game_view")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                currency = (c.getString(c.getColumnIndex("currency")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();

        if (titles == 0){
            thename = "name";
            theimage = "image";
            Log.d("pixo-the image", theimage);
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            Log.d("pixo-the image", theimage);
        }
    }

}
