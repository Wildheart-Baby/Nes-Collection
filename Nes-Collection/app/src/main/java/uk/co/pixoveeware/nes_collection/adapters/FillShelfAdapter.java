package uk.co.pixoveeware.nes_collection.adapters;

/**
 * Created by Wildheart on 10/10/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import uk.co.pixoveeware.nes_collection.activities.MainActivity;
import uk.co.pixoveeware.nes_collection.models.GameItems;

/**
 * Created by Wildheart on 10/09/2018.
 */

public class FillShelfAdapter {



    int gameid, totalgames, index,palAcart, palBcart, uscart, pos, shelf, id, rec, shelfsize, posInList;

    String currentgroup, prevgroup, wherestatement, orderby, groupHeader, theimage, thename, title, currency, name, check, gamename;
    SQLiteDatabase db;
    int titles;
    Cursor c;


    public FillShelfAdapter(Context context){


        db = context.openOrCreateDatabase("nes.sqlite", context.MODE_PRIVATE, null);//open or create the database
        gameregion();

        if (MainActivity.nesList == null){ MainActivity.readList(); }
        MainActivity.nesList.clear();
        check = "y";
        pos = 1;
        posInList = -1;
        shelf = 1;
        Cursor c = db.rawQuery(MainActivity.sqlstatement, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                GameItems nesListItems = new GameItems();//creates a new array
                id = c.getInt(c.getColumnIndex("_id"));

                name = c.getString(c.getColumnIndex("name"));

                palAcart = c.getInt(c.getColumnIndex("pal_a_cart"));
                palBcart = c.getInt(c.getColumnIndex("pal_b_cart"));
                uscart = c.getInt(c.getColumnIndex("ntsc_cart"));
                //Log.d("Pixo", "Name: " + name + " Regions " + palAcart + " " + palBcart + " " + uscart);

                if (check.equals("y")){
                    rec = 0;

                    if (palAcart == 8783){rec ++; posInList++; Log.d("Pixoif", "first if done");}
                    if (palBcart == 8783){rec ++; posInList++; Log.d("Pixoif", "second if done");}
                    if (uscart == 8783){rec ++; posInList++; Log.d("Pixoif", "third if done");}

                    check = "n";
                }

                Log.d("Pixoif", "rec: " + rec);

                if(pos == 1){
                    nesListItems.setGroup("Shelf " + shelf);
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    nesListItems.setName(c.getString(c.getColumnIndex(thename)));
                    gamename = (c.getString(c.getColumnIndex(thename)));
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
                    nesListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    nesListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    nesListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    nesListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    nesListItems.setFinished(c.getInt(c.getColumnIndex("finished_game")));
                    nesListItems.setWishlist(c.getInt(c.getColumnIndex("wishlist")));
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
                    if (rec > 1){posInList =  posInList - 1;}
                    nesListItems.setListPos(posInList);
                    Log.d("shelf", "adding list position: " + posInList);
                    //snesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    MainActivity.nesList.add(nesListItems);//add items to the arraylist
                    shelf ++;
                    //Log.d("pixo", "added shelf record " + name);

                }
                else {
                    nesListItems.setGroup("no");
                    nesListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    nesListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    nesListItems.setName(c.getString(c.getColumnIndex(thename)));
                    gamename = (c.getString(c.getColumnIndex(thename)));
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
                    nesListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    nesListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    nesListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    nesListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    nesListItems.setFinished(c.getInt(c.getColumnIndex("finished_game")));
                    nesListItems.setWishlist(c.getInt(c.getColumnIndex("wishlist")));
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
                    if (rec > 1){posInList =  posInList - 1;}
                    nesListItems.setListPos(posInList);
                    Log.d("shelf", "adding list position: " + posInList);
                    //snesListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    MainActivity.nesList.add(nesListItems);//add items to the arraylist
                    if (pos == shelfsize){pos = 0;}
                    //Log.d("pixo", "added other record " + name);
                }

                if (rec == 1){c.moveToNext(); check = "y"; Log.d("pixoif", "position in the list: " + posInList + " game name: " + gamename);}
                else if(rec > 1){ rec = rec - 1;  Log.d("pixoif", "position in the list: " + posInList + " game name: " + gamename);}
                pos ++;
            }

            c.close();//close the cursor
        }

        db.close();//close the database
    }

    public void gameregion(){//selects the region from the database


        //db = context.openOrCreateDatabase("snes.sqlite",context.MODE_PRIVATE,null);//open or create the database
        c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                title = (c.getString(c.getColumnIndex("region_title")));
                MainActivity.viewas = (c.getInt(c.getColumnIndex("game_view")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                currency = (c.getString(c.getColumnIndex("currency")));

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        //db.close();//close the database
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

