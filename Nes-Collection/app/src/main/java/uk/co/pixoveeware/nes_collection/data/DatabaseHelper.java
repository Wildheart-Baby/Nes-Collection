package uk.co.pixoveeware.nes_collection.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.pixoveeware.nes_collection.models.GameItem;
import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

/**
 * Created by Wildheart on 28/08/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    String currentgroup, prevgroup, wherestatement, orderby, groupHeader, theimage, thename, title, currency, searchQuery, thePublisher, licensed;
    int titles, ordering, conditionstatement, showprice;

    private static final String KEY_ID = "_id";
    private static final String OWNED = "owned";
    private static final String CART = "cart";
    private static final String BOX = "box";
    private static final String MANUAL = "manual";
    private static final String EUROCART = "euro_cart";
    private static final String USCART = "ntsc_cart";
    private static final String SACART = "sa_cart";
    private static final String EUROBOX = "euro_box";
    private static final String USBOX = "ntsc_box";
    private static final String SABOX = "sa_box";
    private static final String EUROMAN = "euro_manual";
    private static final String USMAN = "ntsc_manual";
    private static final String SAMAN = "sa_manual";
    private static final String EUROCOST = "euro_cost";
    private static final String USCOST = "ntsc_cost";
    private static final String SACOST = "sa_cost";
    private static final String PRICE = "price";
    private static final String EUROOWNED = "euro_owned";
    private static final String USOWNED = "ntsc_owned";
    private static final String SAOWNED = "sa_owned";
    private static final String WISHLIST = "wishlist";
    private static final String CONDITION = "condition";
    private static final String PLAYOWNED = "play_owned";
    private static final String PLAYTIME = "play_hours";
    private static final String PLAYSCORE = "play_score";
    private static final String PLAYCOMPLETION = "play_completed";
    private static final String FINISHED = "finished_game";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "nes.sqlite";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<GameListItems> getGameslist(String games){
        String times = getCurrentTimeStamp();
        Log.d("pixo-time","Start time: "+ times);
        ArrayList<GameListItems> gamesList = new ArrayList<>();
        gamesList.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                title = (c.getString(c.getColumnIndex("region_title")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                groupHeader = (c.getString(c.getColumnIndex("group_header")));
                ordering = (c.getInt(c.getColumnIndex("ordered")));
                currency = (c.getString(c.getColumnIndex("currency")));
                conditionstatement = (c.getInt(c.getColumnIndex("show_condition")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        times = getCurrentTimeStamp();
        Log.d("pixo-time","Read settings: "+ times);
        //db.close();//close the database
        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
            Log.d("pixo-the image", theimage);
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
            Log.d("pixo-the image", theimage);
        }
        times = getCurrentTimeStamp();
        Log.d("pixo-time","read region "+ times);
        switch (games){
            case "all":
                searchQuery = "select * from eu where " + wherestatement + licensed + " order by " + orderby +"";
                //String t = "select * from eu where " + wherestatement + licensed + "";
                break;
            case "owned":
                if (ordering == 0) {
                    //"SELECT * FROM eu where " + wherestatement + licensed +  "";
                    //sql = "select * from eu where owned = 1 " + " order by " + orderby +"";
                    searchQuery = "select * from eu where owned = 1 " + " order by " + orderby +"";}
                else if(ordering == 1){
                    //sql = "select * from eu where owned = 1 order by price desc";
                    searchQuery = "select * from eu where owned = 1 order by price desc";}
                break;
            case "needed":
                searchQuery = "SELECT * FROM eu where cart = 0 and (" + wherestatement + licensed +  ") order by " + orderby +"";
                break;
            case "favourites":
                searchQuery = "SELECT * FROM eu where favourite = 1 " + wherestatement + " order by " + orderby +"";
                break;
            case "wishlist":
                searchQuery = "SELECT * FROM eu where wishlist = 1 " + wherestatement + "order by " + orderby +"";
                break;
            case "finished":
                searchQuery ="SELECT * FROM eu where finished_game = 1 " + wherestatement + "order by " + orderby +"";
                break;
            case "search":
                //searchQuery = SearchResults.searchString + " order by " + orderby;
                //Log.i("dbhSearch", "query: " + searchQuery);
                break;
            case "statsearch":
                //searchQuery = StatsSearchResults.searchString + " order by " + orderby;
                //Log.i("dbhSearch", "query: " + searchQuery);
                break;
        }
        times = getCurrentTimeStamp();
        Log.d("pixo-time","Got query time: "+ times);
        c = db.rawQuery(searchQuery, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                GameListItems gameListItems = new GameListItems();//creates a new array

                String currentgroup = c.getString(c.getColumnIndex(groupHeader));

                if(!currentgroup.equals(prevgroup)){
                    gameListItems.setGroup(c.getString(c.getColumnIndex(groupHeader)));
                    gameListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    gameListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    gameListItems.setName(c.getString(c.getColumnIndex(thename)));
                    gameListItems.setPublisher(c.getString(c.getColumnIndex(thePublisher)));
                    gamesList.add(gameListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex(groupHeader));
                }
                else if(currentgroup.equals(prevgroup)){
                    gameListItems.setGroup("no");
                    gameListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    gameListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    gameListItems.setName(c.getString(c.getColumnIndex(thename)));
                    gameListItems.setPublisher(c.getString(c.getColumnIndex(thePublisher)));
                    gamesList.add(gameListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex(groupHeader));

                }
                c.moveToNext();//move to the next record
            }
            //c = db.rawQuery(HomeScreenActivity.sqlstatement, null);
            c.close();//close the cursor
        }
        times = getCurrentTimeStamp();
        Log.d("pixo-time","Finish time: "+ times);
        return gamesList;
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("mm:ss.SSS").format(new Date());
    }

    public ArrayList<GameItems> getGames(String games) {
        ArrayList<GameItems> gamesList = new ArrayList<>();
        gamesList.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                title = (c.getString(c.getColumnIndex("region_title")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                groupHeader = (c.getString(c.getColumnIndex("group_header")));
                ordering = (c.getInt(c.getColumnIndex("ordered")));
                currency = (c.getString(c.getColumnIndex("currency")));
                conditionstatement = (c.getInt(c.getColumnIndex("show_condition")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        //db.close();//close the database
        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
            Log.d("pixo-the image", theimage);
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
            Log.d("pixo-the image", theimage);
        }

        switch (games){
            case "all":
                searchQuery = "select * from eu where " + wherestatement + licensed + " order by " + orderby +"";
                //String t = "select * from eu where " + wherestatement + licensed + "";
                break;
            case "owned":
                if (ordering == 0) {
                    //"SELECT * FROM eu where " + wherestatement + licensed +  "";
                    //sql = "select * from eu where owned = 1 " + " order by " + orderby +"";
                    searchQuery = "select * from eu where owned = 1 " + " order by " + orderby +"";}
                else if(ordering == 1){
                    //sql = "select * from eu where owned = 1 order by price desc";
                    searchQuery = "select * from eu where owned = 1 order by price desc";}
                break;
            case "needed":
                searchQuery = "SELECT * FROM eu where cart = 0 and (" + wherestatement + licensed +  ") order by " + orderby +"";
                break;
            case "favourites":
                searchQuery = "SELECT * FROM eu where favourite = 1 " + wherestatement + " order by " + orderby +"";
                break;
            case "wishlist":
                searchQuery = "SELECT * FROM eu where wishlist = 1 " + wherestatement + "order by " + orderby +"";
                break;
            case "finished":
                searchQuery ="SELECT * FROM eu where finished_game = 1 " + wherestatement + "order by " + orderby +"";
                break;
            case "search":
                //searchQuery = SearchResults.searchString + " order by " + orderby;
                //Log.i("dbhSearch", "query: " + searchQuery);
                break;
            case "statsearch":
                //searchQuery = StatsSearchResults.searchString + " order by " + orderby;
                //Log.i("dbhSearch", "query: " + searchQuery);
                break;
        }

        c = db.rawQuery(searchQuery, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                GameItems gameListItems = new GameItems();//creates a new array

                String currentgroup = c.getString(c.getColumnIndex(groupHeader));

                if(!currentgroup.equals(prevgroup)){
                    gameListItems.setGroup(c.getString(c.getColumnIndex(groupHeader)));
                    gameListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    gameListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    gameListItems.setName(c.getString(c.getColumnIndex(thename)));
                    gameListItems.setPublisher(c.getString(c.getColumnIndex(thePublisher)));
                    gameListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    gameListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                    gameListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                    gameListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                    gameListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                    gameListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                    gameListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                    gameListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                    gameListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                    gameListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                    gameListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                    gameListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                    gameListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                    gameListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    gameListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    gameListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    gameListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    gameListItems.setFinished(c.getInt(c.getColumnIndex("finished_game")));
                    gameListItems.setGamePrice(c.getDouble(c.getColumnIndex("price")));
                    gameListItems.setWishlist(c.getInt(c.getColumnIndex("wishlist")));
                    gameListItems.setYear(c.getString(c.getColumnIndex("eu_year"))); //re-edit this after testing
                    gameListItems.setUSYear(c.getString(c.getColumnIndex("us_year")));
                    gameListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                    gameListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                    gameListItems.setDeveloper((c.getString(c.getColumnIndex("developer"))));
                    gameListItems.setSynopsis((c.getString(c.getColumnIndex("synopsis"))));
                    gameListItems.setAustralia(c.getInt(c.getColumnIndex("flag_australia")));
                    gameListItems.setAustria(c.getInt(c.getColumnIndex("flag_austria")));
                    gameListItems.setBenelux(c.getInt(c.getColumnIndex("flag_benelux")));
                    gameListItems.setDenmark(c.getInt(c.getColumnIndex("flag_denmark")));
                    gameListItems.setFinland(c.getInt(c.getColumnIndex("flag_finland")));
                    gameListItems.setFrance(c.getInt(c.getColumnIndex("flag_france")));
                    gameListItems.setGermany((c.getInt(c.getColumnIndex("flag_germany"))));
                    gameListItems.setGreece(c.getInt(c.getColumnIndex("flag_greece")));
                    gameListItems.setIreland(c.getInt(c.getColumnIndex("flag_ireland")));
                    gameListItems.setItaly(c.getInt(c.getColumnIndex("flag_italy")));
                    gameListItems.setNorway(c.getInt(c.getColumnIndex("flag_norway")));
                    gameListItems.setPoland(c.getInt(c.getColumnIndex("flag_poland")));
                    gameListItems.setPortugal(c.getInt(c.getColumnIndex("flag_portugal")));
                    gameListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                    gameListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                    gameListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                    gameListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                    gameListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));
                    gameListItems.setGameCondition(c.getInt(c.getColumnIndex("condition")));
                    gameListItems.setGameOwnership(c.getInt(c.getColumnIndex("play_owned")));
                    gameListItems.setGameCompletion(c.getInt(c.getColumnIndex("play_completed")));
                    gameListItems.setGameScore(c.getInt(c.getColumnIndex("play_score")));
                    gameListItems.setGameTime(c.getDouble(c.getColumnIndex("play_hours")));
                    gameListItems.setConditionStatement(conditionstatement);
                    gameListItems.setCurrency(currency);
                    gamesList.add(gameListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex(groupHeader));
                }
                else if(currentgroup.equals(prevgroup)){
                    gameListItems.setGroup("no");
                    gameListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    gameListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    gameListItems.setName(c.getString(c.getColumnIndex(thename)));
                    gameListItems.setPublisher(c.getString(c.getColumnIndex(thePublisher)));
                    gameListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    gameListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                    gameListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                    gameListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                    gameListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                    gameListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                    gameListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                    gameListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                    gameListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                    gameListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                    gameListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                    gameListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                    gameListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                    gameListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    gameListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    gameListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    gameListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    gameListItems.setFinished(c.getInt(c.getColumnIndex("finished_game")));
                    gameListItems.setGamePrice(c.getDouble(c.getColumnIndex("price")));
                    gameListItems.setWishlist(c.getInt(c.getColumnIndex("wishlist")));
                    gameListItems.setYear(c.getString(c.getColumnIndex("eu_year"))); //re-edit this after testing
                    gameListItems.setUSYear(c.getString(c.getColumnIndex("us_year")));
                    gameListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                    gameListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                    gameListItems.setDeveloper((c.getString(c.getColumnIndex("developer"))));
                    gameListItems.setSynopsis((c.getString(c.getColumnIndex("synopsis"))));
                    gameListItems.setAustralia(c.getInt(c.getColumnIndex("flag_australia")));
                    gameListItems.setAustria(c.getInt(c.getColumnIndex("flag_austria")));
                    gameListItems.setBenelux(c.getInt(c.getColumnIndex("flag_benelux")));
                    gameListItems.setDenmark(c.getInt(c.getColumnIndex("flag_denmark")));
                    gameListItems.setFinland(c.getInt(c.getColumnIndex("flag_finland")));
                    gameListItems.setFrance(c.getInt(c.getColumnIndex("flag_france")));
                    gameListItems.setGermany((c.getInt(c.getColumnIndex("flag_germany"))));
                    gameListItems.setGreece(c.getInt(c.getColumnIndex("flag_greece")));
                    gameListItems.setIreland(c.getInt(c.getColumnIndex("flag_ireland")));
                    gameListItems.setItaly(c.getInt(c.getColumnIndex("flag_italy")));
                    gameListItems.setNorway(c.getInt(c.getColumnIndex("flag_norway")));
                    gameListItems.setPoland(c.getInt(c.getColumnIndex("flag_poland")));
                    gameListItems.setPortugal(c.getInt(c.getColumnIndex("flag_portugal")));
                    gameListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                    gameListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                    gameListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                    gameListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                    gameListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));
                    gameListItems.setGameCondition(c.getInt(c.getColumnIndex("condition")));
                    gameListItems.setGameOwnership(c.getInt(c.getColumnIndex("play_owned")));
                    gameListItems.setGameCompletion(c.getInt(c.getColumnIndex("play_completed")));
                    gameListItems.setGameScore(c.getInt(c.getColumnIndex("play_score")));
                    gameListItems.setGameTime(c.getDouble(c.getColumnIndex("play_hours")));
                    gameListItems.setConditionStatement(conditionstatement);
                    gameListItems.setCurrency(currency);
                    gamesList.add(gameListItems);//add items to the arraylist
                    prevgroup = c.getString(c.getColumnIndex(groupHeader));

                }
                c.moveToNext();//move to the next record
            }
            //c = db.rawQuery(HomeScreenActivity.sqlstatement, null);
            c.close();//close the cursor
        }
        return gamesList;
    }

    public String regionFlag(){//selects the region from the database
        String flagid ="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                title = (c.getString(c.getColumnIndex("region_title")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database
        switch(title){
            case "UK":
                flagid = "uk";
                break;
            case "US":
                flagid = "us";
                break;
            case "Pal A":
                flagid = "pal_a";
                break;
            case "Pal A UK":
                flagid = "uk";
                break;
            case "Pal B":
                flagid = "euro";
                break;
            case "Australia":
                flagid = "australia";
                break;
            case "Austria":
                flagid = "austria";
                break;
            case "Benelux":
                flagid = "benelux";
                break;
            case "Brazil":
                flagid = "brazil";
                break;
            case "Canada":
                flagid = "canada";
                break;
            case "Denmark":
                flagid = "denmark";
                break;
            case "Finland":
                flagid = "finland";
                break;
            case "France":
                flagid = "france";
                break;
            case "Germany":
                flagid = "germany";
                break;
            case "Greece":
                flagid = "greece";
                break;
            case "Ireland":
                flagid = "ireland";
                break;
            case "Italy":
                flagid = "italy";
                break;
            case "Norway":
                flagid = "norway";
                break;
            case "Poland":
                flagid = "poland";
                break;
            case "Portugal":
                flagid = "portugal";
                break;
            case "Scandinavia":
                flagid = "scandinavia";
                break;
            case "Spain":
                flagid = "spain";
                break;
            case "Sweden":
                flagid = "sweden";
                break;
            case "Switzerland":
                flagid = "switzerland";
                break;
            case "Europe":
                flagid = "europe";
                break;
            case "All Regions":
                flagid = "allregions";
                break;
        }
        return flagid;
    }

    public ArrayList<GameItemsIndex> gamesIndex(String games){
        String times = getCurrentTimeStamp();
        Log.d("pixo-time","Index start time: "+ times);
        ArrayList<GameItemsIndex> indexList = new ArrayList<>();
        indexList.clear();
        int indexpos = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }

        switch (games){
            case "all":
                //searchQuery = "select * from eu where" + wherestatement + " order by " + orderby +"";
                searchQuery = "select * from eu where " + wherestatement + licensed + " order by " + orderby +"";
                break;
            case "owned":
                if (ordering == 0) {
                    //sql = "select * from eu where owned = 1 " + " order by " + orderby +"";
                    searchQuery = "select * from eu where owned = 1 " + " order by " + orderby +"";}
                else if(ordering == 1){
                    //sql = "select * from eu where owned = 1 order by price desc";
                    searchQuery = "select * from eu where owned = 1 order by price desc";}
                break;
            case "needed":
                searchQuery = "SELECT * FROM eu where cart = 0 and (" + wherestatement +  ") order by " + orderby +"";
                break;
        }

        c = db.rawQuery(searchQuery, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                GameItemsIndex indexListItems = new GameItemsIndex();
                currentgroup = c.getString(c.getColumnIndex(groupHeader));

                if(!currentgroup.equals(prevgroup)){
                    indexListItems.setItemid(indexpos);
                    indexListItems.setLetter(c.getString(c.getColumnIndex(groupHeader)));
                    indexList.add(indexListItems);
                    Log.i("index pos", " " + indexpos);
                    indexpos = indexpos +1;
                    prevgroup = c.getString(c.getColumnIndex(groupHeader));
                }
                else if(currentgroup.equals(prevgroup)){
                    prevgroup = c.getString(c.getColumnIndex(groupHeader));
                    indexpos = indexpos +1;
                }
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        times = getCurrentTimeStamp();
        Log.d("pixo-time","Index finish time: "+ times);
        return indexList;
    }

    public int viewType(){
        int viewas = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                viewas = (c.getInt(c.getColumnIndex("game_view")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        return viewas;
    }

    public String regionTitle(){
        String title = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                title = (c.getString(c.getColumnIndex("region_title")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }

        return title;
    }

    public String gamesCount(String games){
        String count = "";
        int gCount = 0;


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                licensed = (c.getString(c.getColumnIndex("licensed")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }

        searchQuery = "select * from eu where" + wherestatement + licensed + " order by " + orderby +"";

        c = db.rawQuery(searchQuery, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            gCount = c.getCount();
            c.close();//close the cursor
        }

        switch (games){
            case "all":
                count = " has " + gCount + " games";
                break;
            case "needed":
                count = " " + gCount + " " + "total games";
                break;
        }
        //title =gmaes " " + title + " - " + HomeScreenActivity.totalGames + " " + titlept2;

        //count = " has " + gCount;

        return count;
    }

    public String needGamesCount(){
        String count;
        int gCount = 0;
        //int nCount = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }

        searchQuery = "SELECT * FROM eu where cart = 0 and (" + wherestatement +  ") order by " + orderby +"";

        c = db.rawQuery(searchQuery, null);//select everything from the database table


        if (c.moveToFirst()) {//move to the first record
            gCount = c.getCount();
            c.close();//close the cursor
        }

        count = " You need " + gCount + " games";

        return count;
    }

    public String ownedGamescount(){
        String gCount = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c; //= db.rawQuery(sql, null);//select everything from the database table
        //sql = "SELECT * FROM eu where " + wherestatement + "";
        String sql1 = "SELECT * FROM eu where euro_cart = 8783 " +  "";
        String sql2 = "SELECT * FROM eu where ntsc_cart = 8783" +   "";
        String sql3 = "SELECT * FROM eu where sa_cart = 8783" +  "";

        c = db.rawQuery(sql1, null);
        int region1games = c.getCount();
        c.close();
        c = db.rawQuery(sql2, null);
        int region2games = c.getCount();
        c.close();
        c = db.rawQuery(sql3, null);
        int region3games = c.getCount();
        c.close();
        db.close();//close the database
        int ownedgames = region1games + region2games + region3games;
        gCount = "You own " + ownedgames + " games";
        return gCount;
    }

    public void finishedGames(String action, int gameId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql;

        if (action.equals("adding")){
            sql = "UPDATE eu SET finished_game = 1 where _id = " + gameId + " ";
            db.execSQL(sql);
        } else if (action.equals("removing")){
            sql = "UPDATE eu SET finished_game = 0 where _id = " + gameId + " ";
            db.execSQL(sql);
        }
    }

    public void favouriteGames(String action, int gameId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "";

        if (action.equals("adding")){
            sql = "UPDATE eu SET favourite = 1 where _id = " + gameId + " ";
            db.execSQL(sql);
        } else if (action.equals("removing")){
            sql = "UPDATE eu SET favourite = 0 where _id = " + gameId + " ";
            db.execSQL(sql);
        }
    }

    public void wishList(String action, int gameId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "";

        if (action.equals("adding")){
            sql = "UPDATE eu SET wishlist = 1 where _id = " + gameId + " ";
            db.execSQL(sql);
        } else if (action.equals("removing")){
            sql = "UPDATE eu SET wishlist = 0 where _id = " + gameId + " ";
            db.execSQL(sql);
        }
    }

    public ArrayList<GameItems> ShelfOrder(){
        int gameid, totalgames, index,palAcart, palBcart, uscart, pos, shelf, id, rec, shelfsize, posInList, shelfPos;
        String currentgroup, prevgroup, wherestatement, orderby, groupHeader, theimage, thename, title, currency, name, check, gamename;

        Boolean newShelf = true;
        rec = 0;
        shelfsize = 0;
        thename = "";
        theimage = "";
        shelfPos = 0;

        ArrayList<GameItems> gamesList = new ArrayList<>();
        gamesList.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region")));
                title = (c.getString(c.getColumnIndex("region_title")));
                //HomeScreenActivity.viewas = (c.getInt(c.getColumnIndex("game_view")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                groupHeader = (c.getString(c.getColumnIndex("group_header")));
                ordering = (c.getInt(c.getColumnIndex("ordered")));
                currency = (c.getString(c.getColumnIndex("currency")));
                shelfsize = (c.getInt(c.getColumnIndex("shelf_size")));
                conditionstatement = (c.getInt(c.getColumnIndex("show_condition")));

                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        //db.close();//close the database
        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
            Log.d("pixo-the image", theimage);
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
            Log.d("pixo-the image", theimage);
        }
        check = "y";
        pos = 1;
        posInList = -1;
        shelf = 1;
        String sql = "SELECT * FROM eu where owned = 1 and cart = 1";
        c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                GameItems gameListItems = new GameItems();//creates a new array
                id = c.getInt(c.getColumnIndex("_id"));

                name = c.getString(c.getColumnIndex("name"));

                palAcart = c.getInt(c.getColumnIndex("euro_cart"));
                uscart = c.getInt(c.getColumnIndex("ntsc_cart"));
                palBcart = c.getInt(c.getColumnIndex("sa_cart"));
                //Log.d("Pixo", "Name: " + name + " Regions " + palAcart + " " + palBcart + " " + uscart);

                if (check.equals("y")){
                    rec = 0;

                    if (palAcart == 8783){rec ++; posInList++; Log.i("Pixoif", "first if done");} else {}
                    if (uscart == 8783){rec ++; posInList++; Log.i("Pixoif", "second if done");} else {}
                    if (palBcart == 8783){rec ++; posInList++; Log.i("Pixoif", "third if done");} else {}

                    check = "n";
                }

                if(newShelf){
                    gameListItems.setGroup("Shelf " + shelf);
                    gameListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    gameListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    gameListItems.setName(c.getString(c.getColumnIndex(thename)));
                    gamename = (c.getString(c.getColumnIndex(thename)));
                    gameListItems.setPublisher(c.getString(c.getColumnIndex(thePublisher)));
                    gameListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    gameListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                    gameListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                    gameListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                    gameListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                    gameListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                    gameListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                    gameListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                    gameListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                    gameListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                    gameListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                    gameListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                    gameListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                    gameListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    gameListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    gameListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    gameListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    gameListItems.setFinished(c.getInt(c.getColumnIndex("finished_game")));
                    gameListItems.setGamePrice(c.getDouble(c.getColumnIndex("price")));
                    gameListItems.setWishlist(c.getInt(c.getColumnIndex("wishlist")));
                    gameListItems.setYear(c.getString(c.getColumnIndex("eu_year"))); //re-edit this after testing
                    gameListItems.setUSYear(c.getString(c.getColumnIndex("us_year")));
                    gameListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                    gameListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                    gameListItems.setDeveloper((c.getString(c.getColumnIndex("developer"))));
                    gameListItems.setSynopsis((c.getString(c.getColumnIndex("synopsis"))));
                    gameListItems.setAustralia(c.getInt(c.getColumnIndex("flag_australia")));
                    gameListItems.setAustria(c.getInt(c.getColumnIndex("flag_austria")));
                    gameListItems.setBenelux(c.getInt(c.getColumnIndex("flag_benelux")));
                    gameListItems.setDenmark(c.getInt(c.getColumnIndex("flag_denmark")));
                    gameListItems.setFinland(c.getInt(c.getColumnIndex("flag_finland")));
                    gameListItems.setFrance(c.getInt(c.getColumnIndex("flag_france")));
                    gameListItems.setGermany((c.getInt(c.getColumnIndex("flag_germany"))));
                    gameListItems.setGreece(c.getInt(c.getColumnIndex("flag_greece")));
                    gameListItems.setIreland(c.getInt(c.getColumnIndex("flag_ireland")));
                    gameListItems.setItaly(c.getInt(c.getColumnIndex("flag_italy")));
                    gameListItems.setNorway(c.getInt(c.getColumnIndex("flag_norway")));
                    gameListItems.setPoland(c.getInt(c.getColumnIndex("flag_poland")));
                    gameListItems.setPortugal(c.getInt(c.getColumnIndex("flag_portugal")));
                    gameListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                    gameListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                    gameListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                    gameListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                    gameListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));
                    gameListItems.setGameCondition(c.getInt(c.getColumnIndex("condition")));
                    gameListItems.setGameOwnership(c.getInt(c.getColumnIndex("play_owned")));
                    gameListItems.setGameCompletion(c.getInt(c.getColumnIndex("play_completed")));
                    gameListItems.setGameScore(c.getInt(c.getColumnIndex("play_score")));
                    gameListItems.setGameTime(c.getDouble(c.getColumnIndex("play_hours")));
                    gameListItems.setConditionStatement(conditionstatement);
                    if (rec > 1){posInList =  posInList - 1;}
                    gameListItems.setListPos(posInList);
                    shelfPos ++;
                    gamesList.add(gameListItems);//add items to the arraylist
                    shelf ++;
                    newShelf = false;
                    //Log.d("pixo", "added shelf record " + name);

                }
                else {
                    gameListItems.setGroup("no");
                    gameListItems.setItemId(c.getInt(c.getColumnIndex("_id")));//set the array with the data from the database
                    gameListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                    gameListItems.setName(c.getString(c.getColumnIndex(thename)));
                    gamename = (c.getString(c.getColumnIndex(thename)));
                    gameListItems.setPublisher(c.getString(c.getColumnIndex(thePublisher)));
                    gameListItems.setOwned(c.getInt(c.getColumnIndex("owned")));
                    gameListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                    gameListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                    gameListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                    gameListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                    gameListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                    gameListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                    gameListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                    gameListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                    gameListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                    gameListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                    gameListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                    gameListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                    gameListItems.setCart(c.getInt(c.getColumnIndex("cart")));
                    gameListItems.setBox(c.getInt(c.getColumnIndex("box")));
                    gameListItems.setManual(c.getInt(c.getColumnIndex("manual")));
                    gameListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                    gameListItems.setFinished(c.getInt(c.getColumnIndex("finished_game")));
                    gameListItems.setGamePrice(c.getDouble(c.getColumnIndex("price")));
                    gameListItems.setWishlist(c.getInt(c.getColumnIndex("wishlist")));
                    gameListItems.setYear(c.getString(c.getColumnIndex("eu_year"))); //re-edit this after testing
                    gameListItems.setUSYear(c.getString(c.getColumnIndex("us_year")));
                    gameListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                    gameListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                    gameListItems.setDeveloper((c.getString(c.getColumnIndex("developer"))));
                    gameListItems.setSynopsis((c.getString(c.getColumnIndex("synopsis"))));
                    gameListItems.setAustralia(c.getInt(c.getColumnIndex("flag_australia")));
                    gameListItems.setAustria(c.getInt(c.getColumnIndex("flag_austria")));
                    gameListItems.setBenelux(c.getInt(c.getColumnIndex("flag_benelux")));
                    gameListItems.setDenmark(c.getInt(c.getColumnIndex("flag_denmark")));
                    gameListItems.setFinland(c.getInt(c.getColumnIndex("flag_finland")));
                    gameListItems.setFrance(c.getInt(c.getColumnIndex("flag_france")));
                    gameListItems.setGermany((c.getInt(c.getColumnIndex("flag_germany"))));
                    gameListItems.setGreece(c.getInt(c.getColumnIndex("flag_greece")));
                    gameListItems.setIreland(c.getInt(c.getColumnIndex("flag_ireland")));
                    gameListItems.setItaly(c.getInt(c.getColumnIndex("flag_italy")));
                    gameListItems.setNorway(c.getInt(c.getColumnIndex("flag_norway")));
                    gameListItems.setPoland(c.getInt(c.getColumnIndex("flag_poland")));
                    gameListItems.setPortugal(c.getInt(c.getColumnIndex("flag_portugal")));
                    gameListItems.setSpain(c.getInt(c.getColumnIndex("flag_spain")));
                    gameListItems.setSweden(c.getInt(c.getColumnIndex("flag_sweden")));
                    gameListItems.setSwitzerland(c.getInt(c.getColumnIndex("flag_switzerland")));
                    gameListItems.setUS((c.getInt(c.getColumnIndex("flag_us"))));
                    gameListItems.setUK((c.getInt(c.getColumnIndex("flag_uk"))));
                    gameListItems.setGameCondition(c.getInt(c.getColumnIndex("condition")));
                    gameListItems.setGameOwnership(c.getInt(c.getColumnIndex("play_owned")));
                    gameListItems.setGameCompletion(c.getInt(c.getColumnIndex("play_completed")));
                    gameListItems.setGameScore(c.getInt(c.getColumnIndex("play_score")));
                    gameListItems.setGameTime(c.getDouble(c.getColumnIndex("play_hours")));
                    gameListItems.setConditionStatement(conditionstatement);
                    if (rec > 1){posInList =  posInList - 1;}
                    gameListItems.setListPos(posInList);
                    shelfPos ++;
                    gamesList.add(gameListItems);//add items to the arraylist
                    if (shelfPos == shelfsize){pos = 0;}

                }

                if (rec == 1){c.moveToNext(); check = "y"; }
                else if(rec > 1){ rec = rec - 1; }
                pos ++;
                if(shelfPos == shelfsize ){shelfPos = 0; newShelf = true;}
            }

            c.close();//close the cursor
        }

        return gamesList;
    }

    public GameItem getGame(int gameid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        GameItem gameListItem = new GameItem();
        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read;
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                conditionstatement = (c.getInt(c.getColumnIndex("show_condition")));
                showprice = (c.getInt(c.getColumnIndex("show_price"))) ;
                currency = (c.getString(c.getColumnIndex("currency")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        //db.close();//close the database
        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
            Log.d("pixo-the image", theimage);
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
            Log.d("pixo-the image", theimage);
        }

        String sql = "SELECT * FROM eu where _id = '" + gameid + "' ";
        c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while (!c.isAfterLast()) {//while there are records to read
                GameItem gameListItems = new GameItem();//creates a new array
                gameListItems.setImage(c.getString(c.getColumnIndex(theimage)));
                gameListItems.setName(c.getString(c.getColumnIndex(thename)));
                gameListItems.setCartPalA(c.getInt(c.getColumnIndex("pal_a_cart")));
                gameListItems.setCartPalB(c.getInt(c.getColumnIndex("pal_b_cart")));
                gameListItems.setCartNtsc(c.getInt(c.getColumnIndex("ntsc_cart")));
                gameListItems.setBoxPalA(c.getInt(c.getColumnIndex("pal_a_box")));
                gameListItems.setBoxPalB(c.getInt(c.getColumnIndex("pal_b_box")));
                gameListItems.setBoxNtsc(c.getInt(c.getColumnIndex("ntsc_box")));
                gameListItems.setManualPalA(c.getInt(c.getColumnIndex("pal_a_manual")));
                gameListItems.setManualPalB(c.getInt(c.getColumnIndex("pal_b_manual")));
                gameListItems.setManualNtsc(c.getInt(c.getColumnIndex("ntsc_manual")));
                gameListItems.setPalACost(c.getDouble(c.getColumnIndex("pal_a_cost")));
                gameListItems.setPalBCost(c.getDouble(c.getColumnIndex("pal_b_cost")));
                gameListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                //gameListItems.setEuroOwned(c.getInt(c.getColumnIndex("euro_release")));
                //gameListItems.setUsOwned(c.getInt(c.getColumnIndex("sa_release")));
                //gameListItems.setSaOwned(c.getInt(c.getColumnIndex("ntsc_release")));
                gameListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                //gameListItems.setPalACost(c.getDouble(c.getColumnIndex("euro_cost")));
                //gameListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                gameListItems.setWishlist(c.getInt(c.getColumnIndex("wishlist")));
                //gameListItems.setGameCondition(c.getInt(c.getColumnIndex("condition")));
                //gameListItems.setGameOwnership(c.getInt(c.getColumnIndex("play_owned")));
                //gameListItems.setGameCompletion(c.getInt(c.getColumnIndex("play_completed")));
                //gameListItems.setGameScore(c.getInt(c.getColumnIndex("play_score")));
                //gameListItems.setGameTime(c.getDouble(c.getColumnIndex("play_hours")));
                gameListItems.setFinished(c.getInt(c.getColumnIndex("finished_game")));
                gameListItems.setPalARelease(c.getInt(c.getColumnIndex("pal_a_release")));
                gameListItems.setPalBRelease(c.getInt(c.getColumnIndex("pal_b_release")));
                gameListItems.setNtscRelease(c.getInt(c.getColumnIndex("ntsc_release")));
                gameListItems.setConditionStatement(conditionstatement);
                gameListItem.setCurrency(currency);
                gameListItem.setShowPrice(showprice);
                gameListItem = gameListItems;
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        return gameListItem;
    }

    public void playStats(int gameOwned, int gameCompletion, int gameScore, Double gameTime, int finished, int gameid){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYOWNED, gameOwned);
        values.put(PLAYCOMPLETION, gameCompletion);
        values.put(PLAYSCORE, gameScore);
        values.put(PLAYTIME, gameTime);
        values.put(FINISHED, finished);
        db.update("eu", values, KEY_ID + "=" + gameid, null);
        db.close();
    }

    public void updateGameRecord(int owned, int cart, int box, int manual, int euro_cart, int euro_box, int euro_manual, int us_cart, int us_box, int us_manual, int sa_cart, int sa_box, int sa_manual, double euro_cost, double us_cost, double sa_cost, double price, int euro_owned, int us_owned, int sa_owned, int gameCondition, int gameOwned, int gameid){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(OWNED, owned);
        values.put(CART, cart);
        values.put(BOX, box);
        values.put(MANUAL, manual);
        values.put(EUROCART, euro_cart);
        values.put(EUROBOX, euro_box);
        values.put(EUROMAN, euro_manual);
        values.put(USCART, us_cart);
        values.put(USBOX, us_box);
        values.put(USMAN, us_manual);
        values.put(SACART, sa_cart);
        values.put(SABOX, sa_box);
        values.put(SAMAN, sa_manual);
        values.put(EUROCOST, euro_cost);
        values.put(USCOST, us_cost);
        values.put(SACOST, sa_cost);
        values.put(PRICE, price);
        values.put(EUROOWNED, euro_owned);
        values.put(USOWNED, us_owned);
        values.put(SAOWNED, sa_owned);
        values.put(WISHLIST, 0);
        values.put(CONDITION, gameCondition);
        values.put(PLAYOWNED, gameOwned);
        Log.i("editGame", "price:" + price);
        db.update("eu", values, KEY_ID + "=" + gameid, null);
        db.close();
    }

    public int searchGamesCount(String search){
        int count = 0;
        int gCount = 0;
        //int nCount = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(search, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            gCount = c.getCount();
            c.close();//close the cursor
        }

        count = gCount;

        return count;
    }

    /*public void favouritegame(int gameId){
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        //if(favourited == 0) {
        if(HomeScreenActivity.nesList.get(viewPager.getCurrentItem()).getFavourite() == 0) {
            str = "UPDATE eu SET favourite = 1 where _id = " + idforgame + " ";
            HomeScreenActivity.nesList.get(viewPager.getCurrentItem()).setFavourite(1);


        } else  if(HomeScreenActivity.nesList.get(viewPager.getCurrentItem()).getFavourite()  == 1) {
            str = "UPDATE eu SET favourite = 0 where _id = " + idforgame + " ";
            HomeScreenActivity.nesList.get(viewPager.getCurrentItem()).setFavourite(0);

        }
        //Toast toast = Toast.makeText(getApplicationContext(), "Favourite no:" + HomeScreenActivity.nesList.get(viewPager.getCurrentItem()).getFavourite() + " Name: " + HomeScreenActivity.nesList.get(viewPager.getCurrentItem()).getName() , Toast.LENGTH_LONG);
        //toast.show();
        //invalidateOptionsMenu();
        Log.d("Pixo", str);
        db.execSQL(str);//run the sql command
        db.close();//close the database

        pos = viewPager.getCurrentItem();
        readGames();
        invalidateOptionsMenu();

    }

    public void wishlist(int gameId){
        db = openOrCreateDatabase("nes.sqlite", MODE_PRIVATE, null);//open or create the database
        String str ="";
        if (ownedgame == 1){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You already own this game",
                    Toast.LENGTH_SHORT);
            toast.show();}
        else if (ownedgame == 0) {

            if (wishlist == 0) {
                str = "UPDATE eu SET wishlist = 1 where _id = " + idforgame + " "; //update the database basket field with 8783
                db.execSQL(str);//run the sql command
                Toast toast = Toast.makeText(getApplicationContext(),
                        gamesname + " added to wishlist",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else if (wishlist == 1) {
                str = "UPDATE eu SET wishlist = 0 where _id = " + idforgame + " "; //update the database basket field with 8783
                db.execSQL(str);//run the sql command
                Toast toast = Toast.makeText(getApplicationContext(),
                        gamesname + " removed from wish list",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        db.close();//close the database
        pos = viewPager.getCurrentItem();
        readGames();
    }*/

}
