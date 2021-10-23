package uk.co.pixoveeware.nes_collection.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.pixoveeware.nes_collection.models.AllGameItems;
import uk.co.pixoveeware.nes_collection.models.GameItem;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;
import uk.co.pixoveeware.nes_collection.models.GameSettings;

/**
 * Created by Wildheart on 28/08/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    String currentgroup, prevgroup, wherestatement, orderby, groupHeader, theimage, thename, currency, searchQuery, thePublisher, licensed;
    int titles, ordering, conditionstatement, showprice, title, regionCode, license;

    private static final String KEY_ID = "_id";
    private static final String OWNED = "owned";
    private static final String CART = "cart";
    private static final String BOX = "box";
    private static final String MANUAL = "manual";
    private static final String PLANACART = "pal_a_cart";
    private static final String PALABOX = "pal_a_box";
    private static final String PALAMANUAL = "pal_a_manual";
    private static final String PALAOWNED = "pal_a_owned";
    private static final String PALBCART = "pal_b_cart";
    private static final String PALBMANUAL = " pal_b_manual";
    private static final String PALBBOX = "pal_b_box";
    private static final String PALBOWNED = "pal_b_owned";
    private static final String NTSCCART = "ntsc_cart";
    private static final String NTSCBOX = "ntsc_box";
    private static final String NTSCMANUAL = "ntsc_manual";
    private static final String NTSCOWNED = "ntsc_owned";
    private static final String FAVOURITE = "favourite";
    private static final String ONSHELF = "onshelf";

    private static final String PRICE = "price";
    private static final String WISHLIST = "wishlist";
    private static final String GAMECONDITION = "condition";
    private static final String PLAYOWNED = "play_owned";
    private static final String PLAYTIME = "play_hours";
    private static final String PLAYSCORE = "play_score";
    private static final String PLAYCOMPLETION = "play_completed";
    private static final String FINISHED = "finished_game";

    private static final String REGIONCODE = "region_code";
    private static final String LICENSEDORNOT = "licensed_or_not";
    private static final String CURRENCY = "currency";
    private static final String SHELFSIZE = "shelf_size";
    private static final String SHOWPRICE = "show_price";
    private static final String GAMEVIEW = "game_view";
    private static final String OWNEDGRAPHIC = "owned_graphic";
    private static final String GAMEORDERING = "game_ordering";
    private static final String SHOWALLGAMES = "show_all_games";
    private static final String USTITLES = "us_titles";
    private static final String ORDEREDBY = "orderedby";
    private static final String GROUPHEADER ="group_header";
    private static final String SHOWCONDITION = "show_condition";

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
        ArrayList<GameListItems> gamesList = new ArrayList<>();
        gamesList.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        GameSettings gSettings = getSettings();

        regionCode = gSettings.getRegionCode();
        license = gSettings.getLicensedOrNot();
        titles = gSettings.getUsTitles();
        orderby = gSettings.getOrderedBy();
        groupHeader = gSettings.getGroupHeader();
        ordering = gSettings.getGameOrdering();
        currency = gSettings.getCurrency();
        conditionstatement = gSettings.getShowCondition();

        wherestatement = SqlStatement.Region(regionCode);
        licensed = SqlStatement.LicensedGames(license);

        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
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
                break;
            case "statsearch":
                //searchQuery = StatsSearchResults.searchString + " order by " + orderby;

                break;
        }
        //d("pixo-time","Got query time: "+ times);
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
        return gamesList;
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("mm:ss.SSS").format(new Date());
    }

    public ArrayList<AllGameItems> getGames(String games) {
        ArrayList<AllGameItems> gamesList = new ArrayList<>();
        gamesList.clear();

        GameSettings gSettings = getSettings();

        regionCode = gSettings.getRegionCode();
        license = gSettings.getLicensedOrNot();
        titles = gSettings.getUsTitles();
        orderby = gSettings.getOrderedBy();
        groupHeader = gSettings.getGroupHeader();
        ordering = gSettings.getGameOrdering();
        currency = gSettings.getCurrency();
        conditionstatement = gSettings.getShowCondition();

        wherestatement = SqlStatement.Region(regionCode);
        licensed = SqlStatement.LicensedGames(license);

        //SQLiteDatabase db = this.getWritableDatabase();
        //Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        /*if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

        //GameSettings gSettings = getSettings();

        /*wherestatement = gSettings.getRegionSql();
        title = gSettings.getRegionTitle();
        licensed = gSettings.getLicensedOrNot();
        titles = gSettings.getUsTitles();
        orderby = gSettings.getOrderedBy();
        groupHeader = gSettings.getGroupHeader();
        ordering = gSettings.getGameOrdering();
        currency = gSettings.getCurrency();
        conditionstatement = gSettings.getShowCondition();*/

                /*wherestatement = (c.getString(c.getColumnIndex("region_sql")));
                title = (c.getInt(c.getColumnIndex("region_title")));
                licensed = (c.getString(c.getColumnIndex("licensed_or_not")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                groupHeader = (c.getString(c.getColumnIndex("group_header")));
                ordering = (c.getInt(c.getColumnIndex("orderedby")));
                currency = (c.getString(c.getColumnIndex("currency")));
                conditionstatement = (c.getInt(c.getColumnIndex("show_condition")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }*/

        //db.close();//close the database
        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
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
                break;
            case "statsearch":
                //searchQuery = StatsSearchResults.searchString + " order by " + orderby;
                break;
            case "australia":
                searchQuery = "SELECT * FROM eu where flag_australia = 1" + licensed + "";
                break;
            case "austria":
                searchQuery = "SELECT * FROM eu where flag_austria = 1" + licensed + "";
                break;
            case "benelux":
                searchQuery = "SELECT * FROM eu where flag_benelux = 1" + licensed + "";
                break;
            case "denmark":
                searchQuery = "SELECT * FROM eu where flag_denmark = 1" + licensed + "";
                break;
            case "finland":
                searchQuery = "SELECT * FROM eu where flag_finland = 1" + licensed + "";
                break;
            case "france":
                searchQuery = "SELECT * FROM eu where flag_france = 1" + licensed + "";
                break;
            case "germany":
                searchQuery = "SELECT * FROM eu where flag_germany = 1" + licensed + "";
                break;
            case "greece":
                searchQuery = "SELECT * FROM eu where flag_greece = 1" + licensed + "";
                break;
            case "ireland":
                searchQuery = "SELECT * FROM eu where flag_ireland = 1" + licensed + "";
                break;
            case "italy":
                searchQuery = "SELECT * FROM eu where flag_italy = 1" + licensed + "";
                break;
            case "norway":
                searchQuery = "SELECT * FROM eu where flag_norway = 1" + licensed + "";
                break;
            case "poland":
                searchQuery = "SELECT * FROM eu where flag_poland = 1" + licensed + "";
                break;
            case "portugal":
                searchQuery = "SELECT * FROM eu where flag_portugal = 1" + licensed + "";
                break;
            /*scandinavia":
                    searchQuery = "SELECT * FROM eu where flag_scandinavian = 1" + licensed + "";
                    break;*/
            case "spain":
                    searchQuery = "SELECT * FROM eu where flag_spain = 1" + licensed + "";
                    break;
            case "sweden":
                    searchQuery = "SELECT * FROM eu where flag_sweden = 1" + licensed + "";
                    break;
            case "switzerland":
                    searchQuery = "SELECT * FROM eu where flag_switzerland = 1" + licensed + "";
                    break;
            case "us":
                    searchQuery = "SELECT * FROM eu where flag_us = 1" + licensed + "";
                   break;
            case "uk":
                    searchQuery = "SELECT * FROM eu where flag_uk = 1" + licensed + "";
                    break;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(searchQuery, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                AllGameItems gameListItems = new AllGameItems();//creates a new array

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
                regionCode = (c.getInt(c.getColumnIndex("region_code")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database

        return SqlStatement.RegionFlag(regionCode);
    }

    public ArrayList<GameItemsIndex> gamesIndex(String games){
        ArrayList<GameItemsIndex> indexList = new ArrayList<>();
        indexList.clear();
        int indexpos = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                regionCode = (c.getInt(c.getColumnIndex("region_code")));
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
        //String title = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                regionCode = (c.getInt(c.getColumnIndex("region_code")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }

        return SqlStatement.RegionTitle(regionCode);
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

    public ArrayList<AllGameItems> ShelfOrder(){
        int gameid, totalgames, index,palAcart, palBcart, uscart, pos, shelf, id, rec, shelfsize, posInList, shelfPos;
        String currentgroup, prevgroup, wherestatement, orderby, groupHeader, theimage, thename, title, currency, name, check, gamename;

        Boolean newShelf = true;
        rec = 0;
        shelfsize = 0;
        thename = "";
        theimage = "";
        shelfPos = 0;

        GameSettings gSettings = getSettings();

        titles = gSettings.getUsTitles();
        ordering = gSettings.getGameOrdering();
        conditionstatement = gSettings.getShowCondition();

        ArrayList<AllGameItems> gamesList = new ArrayList<>();
        gamesList.clear();


        //db.close();//close the database
        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
        }
        check = "y";
        pos = 1;
        posInList = -1;
        shelf = 1;
        String sql = "SELECT * FROM eu where owned = 1 and cart = 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                AllGameItems gameListItems = new AllGameItems();//creates a new array
                id = c.getInt(c.getColumnIndex("_id"));

                name = c.getString(c.getColumnIndex("name"));

                palAcart = c.getInt(c.getColumnIndex("euro_cart"));
                uscart = c.getInt(c.getColumnIndex("ntsc_cart"));
                palBcart = c.getInt(c.getColumnIndex("sa_cart"));


                if (check.equals("y")){
                    rec = 0;

                    if (palAcart == 8783){rec ++; posInList++; }
                    if (uscart == 8783){rec ++; posInList++; }
                    if (palBcart == 8783){rec ++; posInList++; }

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
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
        }

        String sql = "SELECT * FROM eu where _id = '" + gameid + "' ";
        c = db.rawQuery(sql, null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while (!c.isAfterLast()) {//while there are records to read
                GameItem gameListItems = new GameItem();//creates a new array
                gameListItems.setItemId(c.getInt(c.getColumnIndex("_id")));
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
                gameListItems.setOnShelf(c.getInt(c.getColumnIndex("onshelf")));
                //gameListItems.setEuroOwned(c.getInt(c.getColumnIndex("euro_release")));
                //gameListItems.setUsOwned(c.getInt(c.getColumnIndex("sa_release")));
                //gameListItems.setSaOwned(c.getInt(c.getColumnIndex("ntsc_release")));
                gameListItems.setFavourite(c.getInt(c.getColumnIndex("favourite")));
                //gameListItems.setPalACost(c.getDouble(c.getColumnIndex("euro_cost")));
                //gameListItems.setNtscCost(c.getDouble(c.getColumnIndex("ntsc_cost")));
                gameListItems.setWishlist(c.getInt(c.getColumnIndex("wishlist")));
                gameListItems.setGameCondition(c.getInt(c.getColumnIndex("condition")));
                //gameListItems.setGameOwnership(c.getInt(c.getColumnIndex("play_owned")));
                //gameListItems.setGameCompletion(c.getInt(c.getColumnIndex("play_completed")));
                //gameListItems.setGameScore(c.getInt(c.getColumnIndex("play_score")));
                //gameListItems.setGameTime(c.getDouble(c.getColumnIndex("play_hours")));
                gameListItems.setFinished(c.getInt(c.getColumnIndex("finished_game")));
                gameListItems.setPalARelease(c.getInt(c.getColumnIndex("pal_a_release")));
                gameListItems.setPalBRelease(c.getInt(c.getColumnIndex("pal_b_release")));
                gameListItems.setNtscRelease(c.getInt(c.getColumnIndex("ntsc_release")));
                gameListItems.setConditionStatement(conditionstatement);
                gameListItems.setCurrency(currency);
                gameListItems.setShowPrice(showprice);
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

    public void updateGameRecord(int owned, int cart, int box, int manual, int pal_a_cart, int pal_a_box, int pal_a_manual, int pal_a_owned, int pal_b_cart, int pal_b_manual, int pal_b_box, int pal_b_owned, int ntsc_cart, int ntsc_box, int ntsc_manual, int ntsc_owned, double price, int onshelf, int gameCondition, int gameOwned, int gameid){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(OWNED, owned);
        values.put(CART, cart);
        values.put(BOX, box);
        values.put(MANUAL, manual);
        values.put(PLANACART, pal_a_cart);
        values.put(PALABOX, pal_a_box);
        values.put(PALAMANUAL, pal_a_manual);
        values.put(PALAOWNED, pal_a_owned);
        values.put(PALBCART, pal_b_cart);
        values.put(PALBMANUAL, pal_b_manual);
        values.put(PALBBOX, pal_b_box);
        values.put(PALBOWNED, pal_b_owned);
        values.put(NTSCCART, ntsc_cart);
        values.put(NTSCBOX, ntsc_box);
        values.put(NTSCMANUAL, ntsc_manual);
        values.put(NTSCOWNED, ntsc_owned);
        values.put(PRICE, price);
        values.put(ONSHELF, onshelf);
        values.put(WISHLIST, 0);
        values.put(GAMECONDITION, gameCondition);
        values.put(PLAYOWNED, gameOwned);
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

    public void favouriteGame(String value, int gameId){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAVOURITE, value);
        db.update("eu", values, KEY_ID + "=" + gameId, null);
        db.close();
    }

    public void wishList(String value, int gameId){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(WISHLIST, value);
        db.update("eu", values, KEY_ID + "=" + gameId, null);
        db.close();
    }

    public int fragmentSubtitleCount(String option){
        int gCount = 0;

        //c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        /*if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                regionCode = (c.getInt(c.getColumnIndex("region_code")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }*/

        GameSettings gSettings = getSettings();

        titles = gSettings.getUsTitles();
        ordering = gSettings.getGameOrdering();
        license = gSettings.getLicensedOrNot();

        wherestatement = SqlStatement.Region(regionCode);
        licensed = SqlStatement.LicensedGames(license);

        switch (option){
            case "all":
                searchQuery = "select * from eu where " + wherestatement + licensed + " order by " + orderby +"";
                break;
            case "needed":
                searchQuery = "SELECT * FROM eu where cart = 0 and (" + wherestatement + licensed +  ") order by " + orderby +"";
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

        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(searchQuery, null);//select everything from the database table
        if (c.moveToFirst()) {//move to the first record
            gCount= c.getCount();
            c.close();//close the cursor
        }

        return gCount;
    }

    public String fragmentSubtitle(String option, String region){
        String subtitle;

        switch(option){
            case "all":
                subtitle = fragmentSubtitleCount(region) + " games for this region";
                break;
            case "needed":
                subtitle = fragmentSubtitleCount(region) + " missing games";
                break;
            case "owned":
                subtitle = fragmentSubtitleCount(region) + " owned games";
                break;
            default:
                subtitle = "";
                break;
        }
        return  subtitle;
    }

    public ArrayList<AllGameItems> getSpecificSearch(String InformationType, String Query) {
        ArrayList<AllGameItems> gamesList = new ArrayList<>();
        gamesList.clear();

        String iType = InformationType;

        //Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        GameSettings gSettings = getSettings();

        regionCode = gSettings.getRegionCode();
        license = gSettings.getLicensedOrNot();
        titles = gSettings.getUsTitles();
        orderby = gSettings.getOrderedBy();
        groupHeader = gSettings.getGroupHeader();
        ordering = gSettings.getGameOrdering();
        currency = gSettings.getCurrency();
        conditionstatement = gSettings.getShowCondition();

        /*if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region_sql")));
                title = (c.getString(c.getColumnIndex("region_title")));
                licensed = (c.getString(c.getColumnIndex("licensed_or_not")));
                titles = (c.getInt(c.getColumnIndex("us_titles")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                groupHeader = (c.getString(c.getColumnIndex("group_header")));
                ordering = (c.getInt(c.getColumnIndex("ordered")));
                currency = (c.getString(c.getColumnIndex("currency")));
                conditionstatement = (c.getInt(c.getColumnIndex("show_condition")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }*/
        //db.close();//close the database
        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
            if(iType.equals("publisher")) {
                iType = "eu_publisher";
                }
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
            if(iType.equals("publisher")) {
                 iType = "us_publisher";
             }
        }

        licensed = SqlStatement.LicensedGames(license);

        searchQuery = "select * from eu where " + iType + " = '" + Query + "'" + licensed + " order by " + orderby + "";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(searchQuery, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                AllGameItems gameListItems = new AllGameItems();//creates a new array

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

    public ArrayList<GameItemsIndex> specificGamesIndex(String InformationType, String Query){
        ArrayList<GameItemsIndex> indexList = new ArrayList<>();
        indexList.clear();
        int indexpos = 0;
        String iType = InformationType;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                wherestatement = (c.getString(c.getColumnIndex("region_sql")));
                orderby = (c.getString(c.getColumnIndex("orderedby")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }

        searchQuery = "select * from eu where " + iType + " = '" + Query + "'" + licensed + " order by " + orderby + "";

        c = db.rawQuery(searchQuery, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                GameItemsIndex indexListItems = new GameItemsIndex();
                currentgroup = c.getString(c.getColumnIndex(groupHeader));

                if(!currentgroup.equals(prevgroup)){
                    indexListItems.setItemid(indexpos);
                    indexListItems.setLetter(c.getString(c.getColumnIndex(groupHeader)));
                    indexList.add(indexListItems);
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
        return indexList;
    }

    public int ownedGamesCount(){
        int gCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM eu where owned = 1", null);//select everything from the database table

        if (c.moveToFirst()) {//move to the first record
            gCount= c.getCount();
            c.close();//close the cursor
        }

        return gCount;
    }

    public GameSettings getSettings(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM settings", null);//select everything from the database table
        GameSettings gSettings = new GameSettings();

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read

                gSettings.setRegionCode(c.getInt(c.getColumnIndex("region_code")));
                gSettings.setLicensedOrNot(c.getInt(c.getColumnIndex("licensed_or_not")));
                gSettings.setShowAllGames(c.getString(c.getColumnIndex("show_all_games")));
                gSettings.setGroupHeader(c.getString(c.getColumnIndex("group_header")));
                gSettings.setCurrency(c.getString(c.getColumnIndex("currency")));
                gSettings.setShelfSize(c.getInt(c.getColumnIndex("shelf_size")));
                gSettings.setShowPrice(c.getInt(c.getColumnIndex("show_price")));
                gSettings.setGameView(c.getInt(c.getColumnIndex("game_view")));
                gSettings.setOwnedGraphic(c.getInt(c.getColumnIndex("owned_graphic")));
                gSettings.setGameOrdering(c.getInt(c.getColumnIndex("game_ordering")));
                gSettings.setUsTitles(c.getInt(c.getColumnIndex("us_titles")));
                gSettings.setOrderedBy(c.getString(c.getColumnIndex("orderedby")));
                gSettings.setShowCondition(c.getInt(c.getColumnIndex("show_condition")));
                //regionmissingcheck = (c.getString(c.getColumnIndex("region_missing_check")));
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();//close the database

        return gSettings;
    }

    public void UpdateSettings(GameSettings gSettings){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REGIONCODE, gSettings.getRegionCode());
        values.put(LICENSEDORNOT, gSettings.getLicensedOrNot());
        values.put(SHOWALLGAMES, gSettings.getShowAllGames());
        values.put(CURRENCY, gSettings.getCurrency());
        values.put(ORDEREDBY, gSettings.getOrderedBy());
        values.put(GROUPHEADER, gSettings.getGroupHeader());

        values.put(SHELFSIZE, gSettings.getShelfSize());
        values.put(SHOWPRICE, gSettings.getShowPrice());
        values.put(GAMEVIEW, gSettings.getGameView());
        values.put(OWNEDGRAPHIC, gSettings.getOwnedGraphic());
        values.put(GAMEORDERING, gSettings.getGameOrdering());
        values.put(USTITLES, gSettings.getUsTitles());
        values.put(SHOWCONDITION, gSettings.getShowCondition());



        db.update("settings", values, null, null);
        db.close();
    }

}
