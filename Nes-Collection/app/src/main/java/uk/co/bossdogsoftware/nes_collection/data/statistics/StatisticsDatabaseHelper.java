package uk.co.bossdogsoftware.nes_collection.data.statistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.co.bossdogsoftware.nes_collection.data.SqlStatement;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameSettings;
import uk.co.bossdogsoftware.nes_collection.models.statistics.GenreCountItems;

public class StatisticsDatabaseHelper extends SQLiteOpenHelper {

    String[] palanames;
    String[] palbnames;
    String[] usnames;
    String[] gamenames;
    ArrayList<GenreCountItems> GenreItems;
    List<String>genreNames;
    public String[] GenreNames, names2;
    String popgenre;
    public float[] datapoints, datapoints2;
    public int[] piecolours, piecolours2;

    String[] PieColours = new String[]{"#02A0E9","#FFD800","#4800FF","#FF6A00","#A975CE","#FF0000","#FFAC78","#7CA60F","#FF3F42","#FFB548","#5a45e1"};

    ArrayList<AllGameItems> gamesList;
    ArrayList<GenreCountItems>genreList;
    String orderby, groupHeader, sql, theimage, thename, currency, thePublisher;
    int titles, ordering, regionCode, license;

    public Double costPalA, costPalB, costUs, costTotal;
    public int finishedGames, ownedGameCount;

    int piechartCount, palACount, palBCount, usCount;
    boolean actionGame, actionAdventureGame, adventureGame, miscellaneousGame, puzzleGame, racingGame, rolePlayingGame, simulationGame, sportsGame;
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
        //genreList = new ArrayList<>();
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

        String genreName = "";
        GenreItems = new ArrayList<GenreCountItems>();
        genreNames = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        sql = "select * from eu where owned = 1";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                AllGameItems gameListItems = new AllGameItems();//creates a new array
                GenreCountItems gCount = new GenreCountItems();
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
                genreName = c.getString(c.getColumnIndex("genre"));
                gameListItems.setGenre(genreName);
                gameListItems.setSubgenre((c.getString(c.getColumnIndex("subgenre"))));
                gameListItems.setGameCondition(c.getInt(c.getColumnIndex("condition")));
                gameListItems.setGameOwnership(c.getInt(c.getColumnIndex("play_owned")));
                gameListItems.setGameCompletion(c.getInt(c.getColumnIndex("play_completed")));
                gameListItems.setGameScore(c.getInt(c.getColumnIndex("play_score")));
                gameListItems.setGameTime(c.getDouble(c.getColumnIndex("play_hours")));
                gameListItems.setCurrency(currency);
                gamesList.add(gameListItems);//add items to the arraylist



                if(!genreNames.contains(genreName))
                    genreNames.add(genreName);
                    //gCount.setGName(genreName);
                    //GenreItems.add(gCount);
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

//That's all you need
        //list = (ArrayList) list.stream().distinct().collect(Collectors.toList());
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //    list = list.stream().distinct().collect(Collectors.toList());
        //}

        int theCount;

        if(genreNames.size() > 0){
        for (int i=0; i < genreNames.size(); i++) {
            finishedGames = 0;
            GenreCountItems newItem = new GenreCountItems();
            newItem.setGName(genreNames.get(i));
            newItem.setPalAOwned(0);
            newItem.setPalBOwned(0);
            newItem.setUSOwned(0);
            GenreItems.add(newItem);

            for (int g=0; g < gamesList.size(); g++) {
                costPalA += gamesList.get(g).pal_a_cost;
                costPalB += gamesList.get(g).pal_b_cost;
                costUs += gamesList.get(g).ntsc_cost;
                finishedGames += gamesList.get(g).finished;

                if (gamesList.get(g).genre.equals(GenreItems.get(i).getGName())){
                    if (gamesList.get(g).pal_a_cart == 8783) {
                        theCount = GenreItems.get(i).getPalAOwned();
                        theCount ++;
                        GenreItems.get(i).setPalAOwned(theCount);

                    }
                    if (gamesList.get(g).pal_b_cart == 8783) {
                        theCount = GenreItems.get(i).getPalBOwned();
                        theCount ++;
                        GenreItems.get(i).setPalBOwned(theCount);
                    }
                    if (gamesList.get(g).ntsc_cart == 8783) {
                        theCount = GenreItems.get(i).getUSOwned();
                        theCount ++;
                        GenreItems.get(i).setUSOwned(theCount);
                    }

                    if (gamesList.get(g).pal_a_box == 8783) {
                        theCount = GenreItems.get(i).getPalABox();
                        theCount ++;
                        GenreItems.get(i).setPalABox(theCount);
                    }
                    if (gamesList.get(g).pal_b_box == 8783) {
                        theCount = GenreItems.get(i).getPalBBox();
                        theCount ++;
                        GenreItems.get(i).setPalBBox(theCount);
                    }
                    if (gamesList.get(g).ntsc_box == 8783) {
                        theCount = GenreItems.get(i).getUSBox();
                        theCount ++;
                        GenreItems.get(i).setUSBox(theCount);
                    }

                    if (gamesList.get(g).pal_a_manual == 8783) {
                        theCount = GenreItems.get(i).getPalAManual();
                        theCount ++;
                        GenreItems.get(i).setPalAManual(theCount);
                    }
                    if (gamesList.get(g).pal_b_manual == 8783) {
                        theCount = GenreItems.get(i).getPalBManual();
                        theCount ++;
                        GenreItems.get(i).setPalBManual(theCount);
                    }
                    if (gamesList.get(g).ntsc_manual == 8783) {
                        theCount = GenreItems.get(i).getUSManual();
                        theCount ++;
                        GenreItems.get(i).setUSManual(theCount);
                    }




                    Log.d("pixo", "matching genre names: " + genreNames);
                }

            }
            int t = GenreItems.get(i).getPalAOwned() + GenreItems.get(i).getPalBOwned() + GenreItems.get(i).getUSOwned();
            GenreItems.get(i).setGCount(t);

            t = GenreItems.get(i).getPalABox() + GenreItems.get(i).getPalBBox() + GenreItems.get(i).getUSBox();
            GenreItems.get(i).setGBoxCount(t);

            t = GenreItems.get(i).getPalAManual() + GenreItems.get(i).getPalBManual() + GenreItems.get(i).getUSManual();
            GenreItems.get(i).setGManualCount(t);

            double c = costPalA + costPalB + costUs;
            GenreItems.get(i).setGCost(c);
            costPalA = 0.0;
            costPalB = 0.0;
            costUs = 0.0;

        }
        setPieData();
        }
    }

    public void setPieData(){
        //int temp = pieCount();
        GenreNames = new String[GenreItems.size()];
        datapoints = new float[GenreItems.size()];
        piecolours = new int[GenreItems.size()];
        int io = 0;

        for(int i=0; i < GenreItems.size(); i++){
            GenreNames[io] = GenreItems.get(i).getGName();
            datapoints[io] = GenreItems.get(i).getGCount();
            piecolours[io] = Color.parseColor(PieColours[i]);
            io ++;
        }


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
        actionGame = false; actionAdventureGame = false; adventureGame = false; miscellaneousGame = false; puzzleGame = false; racingGame = false; rolePlayingGame = false; simulationGame = false; sportsGame = false;
        ownedAction = 0; ownedActionAdventure = 0; ownedAdventure = 0; ownedMiscellaneous = 0; ownedPuzzle = 0; ownedRacing = 0; ownedRolePlaying = 0; ownedSimulation = 0; ownedSports = 0; ownedStrategy = 0;
        costPalA = 0.0; costPalB = 0.0; costUs =0.0; costTotal = 0.0;
        //actionGenreCount=0; actionAdventureGenreCount=0; adventureGenreCount=0; miscellaneousGenreCount=0; puzzleGenreCount=0; racingGenreCount=0; rolePlayingGenreCount=0; simulationGenreCount=0; sportsGenreCount=0; strategyGenreCount=0;

    }

    public String GameOrGames(){
        String temp;
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

        double collectionpercentage  = ((double) OwnedGameCount() / StatisticsData.LicensedGamesCollection(regionCode)) * 100;

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
        if(GenreItems.size() != -1){

            int max = GenreItems.get(0).getGCount();

            for(int i=1; i<GenreItems.size(); i++) {
                if (GenreItems.get(i).getGCount() > max) {
                    pos = i;
                    max = GenreItems.get(i).getGCount();
                }
            }
        }

        return GenreItems.get(pos).getGName();
    }

    public void SortGenreLIst(){

    }

    public Boolean ShowPieChart()  { return GenreItems.size() > 0;}

    public int OwnedGameCount(){
        int temp = 0;

        for(int i=0; i < GenreItems.size(); i++){
            temp += GenreItems.get(i).getGCount();
        }

        return temp;
    }

    public int ownedBoxes(){
        int temp = 0;

        for(int i=0; i < GenreItems.size(); i++){
            temp += GenreItems.get(i).getGBoxCount();
        }

        return temp;
    }

    public int ownedManuals(){
        int temp = 0;

        for(int i=0; i < GenreItems.size(); i++){
            temp += GenreItems.get(i).getGManualCount();
        }

        return temp;
    }

    public float totalGamesCost(){
        float temp = 0.0f;

        for(int i=0; i < GenreItems.size(); i++){
            temp += GenreItems.get(i).getGCost();
        }

        return temp;
    }

}
