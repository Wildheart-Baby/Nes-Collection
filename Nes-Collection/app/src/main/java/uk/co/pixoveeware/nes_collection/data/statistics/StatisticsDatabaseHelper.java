package uk.co.pixoveeware.nes_collection.data.statistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.telephony.IccOpenLogicalChannelResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import uk.co.pixoveeware.nes_collection.data.SqlStatement;
import uk.co.pixoveeware.nes_collection.models.AllGameItems;
import uk.co.pixoveeware.nes_collection.models.GameSettings;
import uk.co.pixoveeware.nes_collection.models.statistics.GenreCountItems;

public class StatisticsDatabaseHelper extends SQLiteOpenHelper {

    String[] palanames;
    String[] palbnames;
    String[] usnames;
    String[] gamenames;
    public String[] GenreNames, names2;
    String popgenre;
    public float[] datapoints, datapoints2;
    public int[] piecolours, piecolours2;

    ArrayList<AllGameItems> gamesList;
    ArrayList<GenreCountItems>genreList;
    String orderby, groupHeader, sql, theimage, thename, currency, thePublisher;
    int titles, ordering, regionCode, license;

    public Double costPalA, costPalB, costUs, costTotal;
    public int finishedGames, ownedGameCount;

    int piechartCount, palACount, palBCount, usCount;
    boolean actionGame, actionAdventureGame, adventureGame, miscellaneousGame, puzzleGame, racingGame, rolePlayingGame, simulationGame, sportsGame, strategyGame;
    int ownedAction, ownedActionAdventure, ownedAdventure, ownedMiscellaneous, ownedPuzzle, ownedRacing, ownedRolePlaying, ownedSimulation, ownedSports, ownedStrategy;
    int actionGenreCount, actionAdventureGenreCount, adventureGenreCount, miscellaneousGenreCount, puzzleGenreCount, racingGenreCount, rolePlayingGenreCount, simulationGenreCount, sportsGenreCount, strategyGenreCount;

    //public String licensed, PalA, PalB, US, s, gamecost, wherestatement, palaadd, palbadd, usadd, palanames2, palbnames2, usnames2, poppublisher, perpalacoll, perpalbcoll, peruscoll, gamename, gamescost, gameorgames, regionselected, loosecarts, looseboxes, looseman, looseitems, collectionpercentagestr, avgCst;
    //int totalOwned, totalReleased, totalPalA, totalPalB, totalUS, ownedPalA, ownedPalB, ownedUS, io,cost, totalCost, percentPalANeeded, percentPalBNeeded, percentUSNeeded, i, showprices, numberowned, palaMaxCost, palbMaxCost, usMaxCost,totalfinished, collectiongames, collectionreleased, loosecart, loosemanual, loosebox;
    //int ownedPalAbox, ownedPalBbox, ownedUSbox, ownedPalAman, ownedPalBman, ownedUSman, boxed, totalOwnedGames;
    //int ownedaction, ownedadventure, ownedbeatemup, ownedactionadventure, ownedarcade, ownedboardgame, ownedcompilation, ownedfighting, ownedother, ownedplatformer, ownedpuzzle, ownedracing, ownedroleplayinggame, ownedshootemup, ownedshooter, ownedsimulation, ownedsports, ownedstrategy, ownedtraditional, ownedtrivia;
    //double percentPalAOwned, percentPalBOwned, percentUSOwned, percentagepalacollection, percentagepalbcollection, percentageuscollection, collectionpercentage, cibpercentage, boxedpercentage;
    //float palacost, palbcost, uscost, totalpalacost, totalpalbcost, totaluscost, totalcost, avgCost, percentageOwned;

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "nes.sqlite";


    public StatisticsDatabaseHelper(Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);

        ClearVariables();
        ReadIntoArray();
        getData();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void ReadIntoArray(){
        gamesList = new ArrayList<>();
        genreList = new ArrayList<>();
        GameSettings gSettings = getSettings();

        regionCode = gSettings.getRegionCode();
        license = gSettings.getLicensedOrNot();
        titles = gSettings.getUsTitles();
        orderby = gSettings.getOrderedBy();
        groupHeader = gSettings.getGroupHeader();
        ordering = gSettings.getGameOrdering();

        if (titles == 0){
            thename = "eu_name";
            theimage = "image";
            thePublisher = "eu_publisher";
        } else if (titles == 1){
            thename = "us_name";
            theimage = "us_image";
            thePublisher = "us_publisher";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        sql = "select * from eu where owned = 1";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                AllGameItems gameListItems = new AllGameItems();//creates a new array

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
                gameListItems.setGenre((c.getString(c.getColumnIndex("genre"))));
                gameListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                gameListItems.setGameCondition(c.getInt(c.getColumnIndex("condition")));
                gameListItems.setGameOwnership(c.getInt(c.getColumnIndex("play_owned")));
                gameListItems.setGameCompletion(c.getInt(c.getColumnIndex("play_completed")));
                gameListItems.setGameScore(c.getInt(c.getColumnIndex("play_score")));
                gameListItems.setGameTime(c.getDouble(c.getColumnIndex("play_hours")));
                gameListItems.setCurrency(currency);
                gamesList.add(gameListItems);//add items to the arraylist
                c.moveToNext();//move to the next record
            }
            c.close();//close the cursor
        }
        db.close();

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

    public void getData(){

        for (int i=0; i < gamesList.size(); i++){
            costPalA += gamesList.get(i).pal_a_cost;
            costPalB += gamesList.get(i).pal_b_cost;
            costUs += gamesList.get(i).ntsc_cost;
            finishedGames += gamesList.get(i).finished;

            if(gamesList.get(i).genre.equals("Action")){
                actionGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedAction ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedAction ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedAction ++;}
                actionGenreCount ++;
            }

            if(gamesList.get(i).genre.equals("Action-Adventure")){
                actionAdventureGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedActionAdventure ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedActionAdventure ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedActionAdventure ++;}
                actionAdventureGenreCount++;
            }

            if(gamesList.get(i).genre.equals("Adventure")){
                adventureGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedAdventure ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++;ownedAdventure ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedAdventure++;}
                adventureGenreCount++;
            }

            if(gamesList.get(i).genre.equals("Miscellaneous")){
                miscellaneousGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedMiscellaneous ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedMiscellaneous ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedMiscellaneous ++;}
                miscellaneousGenreCount++;
            }

            if(gamesList.get(i).genre.equals("Puzzle")){
                puzzleGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedPuzzle ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedPuzzle ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedPuzzle ++;}
                puzzleGenreCount++;
            }

            if(gamesList.get(i).genre.equals("Racing")){
                racingGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedRacing ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedRacing ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedRacing ++;}
                racingGenreCount++;
            }

            if(gamesList.get(i).genre.equals("Role-Playing")){
                rolePlayingGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedRolePlaying ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedRolePlaying ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedRolePlaying ++;}
                rolePlayingGenreCount++;
            }

            if(gamesList.get(i).genre.equals("Simulation")){
                simulationGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedSimulation ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedSimulation ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedSimulation ++;}
                simulationGenreCount++;
            }

            if(gamesList.get(i).genre.equals("Sports")){
                sportsGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedSports ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedSports ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedSports ++;}
                sportsGenreCount++;
            }

            if(gamesList.get(i).genre.equals("Strategy")){
                strategyGame = true;
                if(gamesList.get(i).pal_a_cart == 8783){ palACount ++; ownedStrategy ++;}
                if(gamesList.get(i).pal_b_cart == 8783){ palBCount ++; ownedStrategy ++;}
                if(gamesList.get(i).ntsc_cart == 8783){ usCount ++; ownedStrategy ++;}
                strategyGenreCount++;
            }


        }
        costTotal = costPalA + costPalB + costUs;
        ownedGameCount = palACount + palBCount + usCount;
        piechartCount = pieCount();
        //if(gamesList.get(i).cart == 1){ piechartCount ++;}
        setPieData();
        //sortGenreList();
    }

    public int pieCount(){
        int temp = 0;
        if(actionGame){
            temp ++;
        }
        if(actionAdventureGame){
            temp ++;
        }
        if(adventureGame){
            temp ++;
        }
        if(miscellaneousGame){
            temp ++;
        }
        if(puzzleGame){
            temp ++;
        }
        if(racingGame){
            temp ++;
        }
        if(rolePlayingGame){
            temp ++;
        }
        if(simulationGame){
            temp ++;
        }
        if(sportsGame){
            temp ++;
        }
        if(strategyGame){
            temp ++;
        }
        return temp;
    }

    public int setPieData(){
        int temp = pieCount();
        GenreNames = new String[temp];
        datapoints = new float[temp];
        piecolours = new int[temp];
        int io = 0;
            if(actionGame){
                GenreNames[io] = "Action";
                datapoints[io] = ownedAction;
                piecolours[io] = Color.parseColor("#02A0E9");
                SetGenre("action", actionGenreCount);
                io++;
            }
            if(actionAdventureGame){
                GenreNames[io] = "Action-Adventure";
                datapoints[io] = ownedActionAdventure;
                piecolours[io] = Color.parseColor("#FFD800");
                SetGenre("action-adventure", actionGenreCount);
                io++;
            }
            if(adventureGame){
                GenreNames[io] = "Adventure";
                datapoints[io] = ownedAdventure;
                piecolours[io] = Color.parseColor("#4800FF");
                SetGenre("adventure", adventureGenreCount);
                io++;
            }
            if(miscellaneousGame){
                GenreNames[io] = "Miscellaneous";
                datapoints[io] = ownedMiscellaneous;
                piecolours[io] = Color.parseColor("#FF6A00");
                SetGenre("miscellaneous", miscellaneousGenreCount);
                io++;
            }
            if(puzzleGame){
                GenreNames[io] = "Puzzle";
                datapoints[io] = ownedPuzzle;
                piecolours[io] = Color.parseColor("#A975CE");
                SetGenre("puzzle", puzzleGenreCount);
                io++;
            }
            if(racingGame){
                GenreNames[io] = "Racing";
                datapoints[io] = ownedRacing;
                piecolours[io] = Color.parseColor("#FF0000");
                SetGenre("racing", racingGenreCount);
                io++;
            }
            if(rolePlayingGame){
                GenreNames[io] = "Role-Playing";
                datapoints[io] = ownedRolePlaying;
                piecolours[io] = Color.parseColor("#FFAC78");
                SetGenre("role-playing", rolePlayingGenreCount);
                io++;
            }
            if(simulationGame){
                GenreNames[io] = "Simulation";
                datapoints[io] = ownedSimulation;
                piecolours[io] = Color.parseColor("#7CA60F");
                SetGenre("simulation", simulationGenreCount);
                io++;
            }
            if(sportsGame){
                GenreNames[io] = "Sports";
                datapoints[io] = ownedSports;
                piecolours[io] = Color.parseColor("#FF3F42");
                SetGenre("sports", sportsGenreCount);
                io++;
            }
            if(strategyGame){
                GenreNames[io] = "Strategy";
                datapoints[io] = ownedStrategy;
                piecolours[io] = Color.parseColor("#FFB548");
                SetGenre("strategy", strategyGenreCount);
                io++;
            }
        return temp;
    }

    /*




            sql = "SELECT * FROM eu where pal_a_release = 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalPalA = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (pal_a_cart = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedPalA = c.getCount();

            sql = "SELECT * FROM eu where pal_b_release = 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalPalB = c.getCount();
            sql = "SELECT * FROM eu where owned = 1 and (pal_b_cart = 8783 " + licensed + ")";
            //Log.d("Pixo", sql);
            c = db.rawQuery(sql, null);
            ownedPalB = c.getCount();

            sql = "SELECT * FROM eu where ntsc_release = 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalUS = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 and (ntsc_cart = 8783 " + licensed + ")";
            c = db.rawQuery(sql, null);
            //Log.d("Pixo", sql);
            ownedUS = c.getCount();

            sql = "SELECT * FROM eu where 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalReleased = c.getCount();

            sql = "SELECT * FROM eu where owned = 1 " + licensed + "";
            c = db.rawQuery(sql, null);
            totalOwned = c.getCount();

            sql = "select name from eu where pal_a_owned = 1 and pal_a_cost >0 and pal_a_cost=(select max(pal_a_cost) from eu) limit 1";
            c = db.rawQuery(sql, null);
            i = c.getCount();
            //Log.d("pixo", "value:" + i);
            if (i > 1) {
                palaadd = getString(R.string.statsMostexpensivegames);
            } else {
                palaadd = getString(R.string.statsMostexpensivegame);
            }
            if (ownedPalA == 0) {
                RlPalA.setVisibility(View.GONE);
                Div2.setVisibility(View.GONE);
            }
            palanames = new String[i];
            if (c.moveToFirst()) {//move to the first record
                io = 0;
                while (!c.isAfterLast()) {//while there are records to read
                    palanames[io] = c.getString(c.getColumnIndex("name")) + " ";
                    c.moveToNext();
                    io++;
                }
                palanames2 = Arrays.toString(palanames);
                palanames2 = palanames2.substring(1, palanames2.length() - 1);
                Log.d("Pixo", "Pal A: " + palanames2);
                //i = 0;
            }
            sql = "select name from eu where pal_b_owned = 1 and pal_b_cost >0 and pal_b_cost=(select max(pal_b_cost) from eu) limit 1";
            c = db.rawQuery(sql, null);
            i = c.getCount();

            if (i > 1) {
                palbadd = getString(R.string.statsMostexpensivegames);
            } else {
                palbadd = getString(R.string.statsMostexpensivegame);
            }
            if (ownedPalB == 0) {
                RlPalB.setVisibility(View.GONE);
                //Div3.setVisibility(View.GONE);
            }
            palbnames = new String[i];
            if (c.moveToFirst()) {//move to the first record
                io = 0;
                while (!c.isAfterLast()) {//while there are records to read
                    palbnames[io] = c.getString(c.getColumnIndex("name")) + " ";
                    c.moveToNext();
                    io++;
                }
                palbnames2 = Arrays.toString(palbnames);
                palbnames2 = palbnames2.substring(1, palbnames2.length() - 1);
                Log.d("Pixo", "Pal B: " + palbnames2);
            }

            sql = "select name from eu where ntsc_owned = 1 and ntsc_cost >0 and ntsc_cost=(select max(ntsc_cost) from eu) limit 1";
            c = db.rawQuery(sql, null);
            i = c.getCount();

            if (i > 1) {
                usadd = getString(R.string.statsMostexpensivegames);
            } else {
                usadd = getString(R.string.statsMostexpensivegame);
            }
            if (ownedUS == 0) {
                RlUS.setVisibility(View.GONE);
                //Div3.setVisibility(View.GONE);
            }
            usnames = new String[i];
            if (c.moveToFirst()) {//move to the first record
                io = 0;
                while (!c.isAfterLast()) {//while there are records to read
                    usnames[io] = c.getString(c.getColumnIndex("name")) + " ";
                    c.moveToNext();
                    io++;
                }
                usnames2 = Arrays.toString(usnames);
                usnames2 = usnames2.substring(1, usnames2.length() - 1);
                Log.d("Pixo", "US: " + usnames2);

            }

            sql = "select genre, COUNT(genre) AS MOST_FREQUENT from eu where owned = 1 GROUP BY genre ORDER BY COUNT(genre) DESC limit 1";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            popgenre = c.getString(c.getColumnIndex("genre"));

            sql = "select publisher, COUNT(publisher) AS MOST_FREQUENT from eu where owned = 1 GROUP BY genre ORDER BY COUNT(publisher) DESC limit 1";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            poppublisher = c.getString(c.getColumnIndex("publisher"));

            i = 0;

            sql = "select * from eu where (owned = 1 and cart = 1 and box = 1 and manual = 1)";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            completeinbox = c.getCount();

            sql = "select * from eu where (owned = 1 and cart = 1 and box = 1)";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            boxed = c.getCount();
            boxed = boxed - completeinbox;
            //c.close();
            //db.close();//close the database
            totalOwned = ownedPalA + ownedPalB + ownedUS;
            Log.d("pixo-stats", "Total owned " + totalOwned + " Total counted " + numberowned);
            DecimalFormat decimalFormat = new DecimalFormat(getString(R.string.statsDecimalFormat));


            if (showprices == 1) {
                if (totalcost > 0) {

                    avgCost = Float.valueOf(totalcost / totalOwned);
                    //String avgCst = String.valueOf(avgCost);
                    avgCst = String.format("%.2f", avgCost);
                    //avgCst = avgCst.replace(",",".");
                    Log.d("pixo-stats", "avg cost: " + avgCst);
                    gamescost = getString(R.string.statsGamescost1) + " " + currency + String.format("%.2f", totalcost)  + " " + getString(R.string.statsGamescost2) + " " + currency + avgCst + "\n";
                } else if (totalcost == 0) {
                    gamescost = "";
                }
            } else if (showprices == 0) {
                gamescost = "";
            }

            if (totalOwned > 1) {
                gameorgames = getString(R.string.statsGameorgames1);
            } else {
                gameorgames = getString(R.string.statsGameorgames2);
            }
            //gamecost = "You have spent " + currency + totalcost + " on games for your collection\n" +

            switch (wherestatement) {
                case "(pal_a_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 327;
                    } else {
                        collectionreleased = 361;
                    }
                    sql = "select * from eu where (owned = 1 and pal_a_release = 1)";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = getString(R.string.regionsA);

                    break;
                 case "(pal_uk_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 246;
                    } else {
                        collectionreleased = 255;
                    }
                    sql = "select * from eu where (owned = 1 and pal_uk_release = 1)";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = getString(R.string.regionsA2);
                     break;
                case "(pal_b_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 242;
                    } else {
                        collectionreleased = 268;
                    }
                    sql = "select * from eu where (owned = 1 and pal_b_release = 1)";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = getString(R.string.regionsB);
                    break;
                case "(ntsc_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 673;
                    } else {
                        collectionreleased = 760;
                    }
                    sql = "select * from eu where (owned = 1 and ntsc_release = 1)";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = getString(R.string.regionsUSA);
                break;
                case "(pal_a_release = 1 or pal_b_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 350;
                    } else {
                        collectionreleased = 384;
                    }
                    sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or pal_b_release = 1))";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = "Pal A & Pal B";
                    break;
                case "(pal_a_release = 1 or ntsc_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 717;
                    } else {
                        collectionreleased = 818;
                    }
                    sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or ntsc_release = 1))";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = "Pal A & Ntsc";
                    break;
                case "(pal_b_release = 1 or ntsc_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 723;
                    } else {
                        collectionreleased = 822;
                    }
                    sql = "select * from eu where (owned = 1 and (pal_b_release = 1 or ntsc_release = 1))";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    regionselected = "Pal B & Ntsc";
                    break;
                case "(pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1)":
                    if (licensed.equals(" and (unlicensed = 0)")) {
                        collectionreleased = 725;
                    } else {
                        collectionreleased = 826;
                    }
                    Log.d("pixo-stats", "all regions count hit");
                    sql = "select * from eu where (owned = 1 and (pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1))";
                    c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    collectiongames = c.getCount();
                    Log.d("pixo-stats", "Count % " + collectiongames);
                    regionselected = getString(R.string.regionsAll);
                    break;
                }

     if (ownedactionadventure > 0) {
        names[io] = "Action-Adventure";
        datapoints[io] = ownedactionadventure;
        piecolours[io] = Color.parseColor("#FF2B32");
        io++;
    }
        if (ownedaction > 0) {
        names[io] = "Action";
        datapoints[io] = ownedaction;
        piecolours[io] = Color.parseColor("#FF6A00");
        io++;
    }
        if (ownedadventure > 0) {
        names[io] = "Adventure";
        datapoints[io] = ownedadventure;
        piecolours[io] = Color.parseColor("#FFD800");
        io++;
    }
        if (ownedarcade > 0) {
        names[io] = "Arcade";
        datapoints[io] = ownedarcade;
        piecolours[io] = Color.parseColor("#B6FF00");
        io++;
    }
        if (ownedbeatemup > 0) {
        names[io] = "Beat em Up";
        datapoints[io] = ownedbeatemup;
        piecolours[io] = Color.parseColor("#FF004C");
        io++;
    }
        if (ownedboardgame > 0) {
        names[io] = "Board game";
        datapoints[io] = ownedboardgame;
        piecolours[io] = Color.parseColor("#FF0FFF");
        io++;
    }
        if (ownedcompilation > 0) {
        names[io] = "Compilation";
        datapoints[io] = ownedcompilation;
        piecolours[io] = Color.parseColor("#0095FF");
        io++;
    }
        if (ownedfighting > 0) {
        names[io] = "Fighting";
        datapoints[io] = ownedfighting;
        piecolours[io] = Color.parseColor("#B200FF");
        io++;
    }
        if (ownedother > 0) {
        names[io] = "Other";
        datapoints[io] = ownedother;
        piecolours[io] = Color.parseColor("#FF006E");
        io++;
    }
        if (ownedplatformer > 0) {
        names[io] = "Platformer";
        datapoints[io] = ownedplatformer;
        piecolours[io] = Color.parseColor("#FF00DC");
        io++;
    }
        if (ownedpuzzle > 0) {
        names[io] = "Puzzle";
        datapoints[io] = ownedpuzzle;
        piecolours[io] = Color.parseColor("#B002FF");
        io++;
    }
        if (ownedracing > 0) {
        names[io] = "Racing";
        datapoints[io] = ownedracing;
        piecolours[io] = Color.parseColor("#FF7F7F");
        io++;
    }
        if (ownedroleplayinggame > 0) {
        names[io] = "Role-Playing Game";
        datapoints[io] = ownedroleplayinggame;
        piecolours[io] = Color.parseColor("#C0C0C0");
        io++;
    }
        if (ownedshootemup > 0) {
        names[io] = "Shoot em Up";
        datapoints[io] = ownedshootemup;
        piecolours[io] = Color.parseColor("#613F7C");
        io++;
    }
        if (ownedshooter > 0) {
        names[io] = "Shooter";
        datapoints[io] = ownedshooter;
        piecolours[io] = Color.parseColor("#60C5FF");
        io++;
    }
        if (ownedsimulation > 0) {
        names[io] = "Simulation";
        datapoints[io] = ownedsimulation;
        piecolours[io] = Color.parseColor("#FFAFB5");
        io++;
    }
        if (ownedsports > 0) {
        names[io] = "Sports";
        datapoints[io] = ownedsports;
        piecolours[io] = Color.parseColor("#D5FFBF");
        io++;
    }
        if (ownedstrategy > 0) {
        names[io] = "Strategy";
        datapoints[io] = ownedstrategy;
        piecolours[io] = Color.parseColor("#4FFFBE");
        io++;
    }
        if (ownedtraditional > 0) {
        names[io] = "Traditional";
        datapoints[io] = ownedtraditional;
        piecolours[io] = Color.parseColor("#A856FF");
        io++;
    }
        if (ownedtrivia > 0) {
        names[io] = "Trivia";
        datapoints[io] = ownedtrivia;
        piecolours[io] = Color.parseColor("#CBFF72");
        io++;
    }
     */

    public void ClearVariables(){
        piechartCount = 0; palACount = 0; palBCount = 0; usCount = 0; ownedGameCount = 0; finishedGames = 0;
        actionGame = false; actionAdventureGame = false; adventureGame = false; miscellaneousGame = false; puzzleGame = false; racingGame = false; rolePlayingGame = false; simulationGame = false; sportsGame = false; strategyGame = false;
        ownedAction = 0; ownedActionAdventure = 0; ownedAdventure = 0; ownedMiscellaneous = 0; ownedPuzzle = 0; ownedRacing = 0; ownedRolePlaying = 0; ownedSimulation = 0; ownedSports = 0; ownedStrategy = 0;
        costPalA = 0.0; costPalB = 0.0; costUs =0.0; costTotal = 0.0;
        actionGenreCount=0; actionAdventureGenreCount=0; adventureGenreCount=0; miscellaneousGenreCount=0; puzzleGenreCount=0; racingGenreCount=0; rolePlayingGenreCount=0; simulationGenreCount=0; sportsGenreCount=0; strategyGenreCount=0;

    }

    public String GameOrGames(){
        String temp = "";
        if(ownedGameCount > 1){
            temp = " games ";
        }
        else
            {
            temp = " game ";
        }
        return temp;
    }

    public String CollectionPercentage(){

        double collectionpercentage  = ((double) ownedGameCount / StatisticsData.LicensedGamesCollection(regionCode)) * 100;

        return String.format("%.2f", collectionpercentage);
    }

    public String RegionSelected(){
        return SqlStatement.RegionTitle(regionCode);
    }

    public void SetGenre(String name, int count){
        GenreCountItems item = new GenreCountItems();
        item.setGName(name);
        item.setGCount(count);
        genreList.add(item);
    }

    public String getPopGenre(){
        int pos = 0;
        if(genreList.size() != -1){

            int max = genreList.get(0).getGCount();

            for(int i=1; i<genreList.size(); i++) {
                if (genreList.get(i).getGCount() > max) {
                    pos = i;
                    max = genreList.get(i).getGCount();
                }
            }
        }

        return genreList.get(pos).getGName();
    }

    public void SortGenreLIst(){
        
    }

}
