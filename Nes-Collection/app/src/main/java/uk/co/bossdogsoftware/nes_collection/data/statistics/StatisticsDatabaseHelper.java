package uk.co.bossdogsoftware.nes_collection.data.statistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.activities.Statistics;
import uk.co.bossdogsoftware.nes_collection.data.SqlStatement;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameSettings;
import uk.co.bossdogsoftware.nes_collection.models.statistics.GameCostItems;
import uk.co.bossdogsoftware.nes_collection.models.statistics.GenreCountItems;

public class StatisticsDatabaseHelper extends SQLiteOpenHelper {

    String[] palanames;
    String[] palbnames;
    String[] usnames;
    String[] gamenames;
    ArrayList<GenreCountItems> GenreItems;
    List<String>genreNames, topGames;
    public String[] GenreNames, names2;
    String popgenre;
    public float[] datapoints, datapoints2;
    public int[] piecolours, piecolours2;

    String[] PieColours = new String[]{"#02A0E9","#FFD800","#4800FF","#FF6A00","#A975CE","#FF0000","#FFAC78","#7CA60F","#FF3F42","#FFB548","#5a45e1"};

    ArrayList<AllGameItems> gamesList;
    ArrayList<GenreCountItems>genreList;

    ArrayList<GameCostItems> palAPrices;
    ArrayList<GameCostItems> palBPrices;
    ArrayList<GameCostItems> usPrices;

    String orderby, groupHeader, sql, theimage, thename, currency, thePublisher, expensivePalAGame, expensivePalBGame, expensiveUSGame;
    int titles, ordering, regionCode, license,_id;

    public Double costPalA, costPalB, costUs, costTotal, a_cost, b_cost, us_cost;
    public int finishedGames, ownedGameCount;
    Context mContext;

    int piechartCount, palACount, palBCount, usCount;
    boolean actionGame, actionAdventureGame, adventureGame, miscellaneousGame, puzzleGame, racingGame, rolePlayingGame, simulationGame, sportsGame;

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "nes.sqlite";


    public StatisticsDatabaseHelper(Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
        ClearVariables();
        ReadIntoArray();
        getData();
        mContext = context;
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

        String gName;
        a_cost = 0.0;
        b_cost = 0.0;
        us_cost = 0.0;

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
        GenreItems = new ArrayList<>();
        genreNames = new ArrayList<>();
        topGames = new ArrayList<>();
        palAPrices = new ArrayList<>();
        palBPrices = new ArrayList<>();
        usPrices = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        sql = "select * from eu where owned = 1";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {//move to the first record
            while ( !c.isAfterLast() ) {//while there are records to read
                AllGameItems gameListItems = new AllGameItems();//creates a new array
                GenreCountItems gCount = new GenreCountItems();
                gameListItems.setGroup(c.getString(c.getColumnIndex(groupHeader)));
                _id = c.getInt(c.getColumnIndex("_id"));
                gameListItems.setItemId(_id);//set the array with the data from the database
                gName = c.getString(c.getColumnIndex(theimage));
                gameListItems.setImage(gName);
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
                gameListItems.setRanking(c.getInt(c.getColumnIndex("Ranking")));
                if(gameListItems.getRanking() > 0){ topGames.add(gName); }
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
            newItem.setId(i);
            GenreItems.add(newItem);

            costPalA = 0.0;
            costPalB = 0.0;
            costUs = 0.0;

            for (int g=0; g < gamesList.size(); g++) {

                finishedGames += gamesList.get(g).finished;

                if (gamesList.get(g).genre.equals(GenreItems.get(i).getGName())){
                    if (gamesList.get(g).pal_a_cart == 8783) {
                        theCount = GenreItems.get(i).getPalAOwned();
                        theCount ++;
                        GenreItems.get(i).setPalAOwned(theCount);
                        costPalA += gamesList.get(g).getPalACost();
                    }
                    if (gamesList.get(g).pal_b_cart == 8783) {
                        theCount = GenreItems.get(i).getPalBOwned();
                        theCount ++;
                        GenreItems.get(i).setPalBOwned(theCount);
                        costPalB += gamesList.get(g).getPalBCost();
                    }
                    if (gamesList.get(g).ntsc_cart == 8783) {
                        theCount = GenreItems.get(i).getUSOwned();
                        theCount ++;
                        GenreItems.get(i).setUSOwned(theCount);
                        costUs += gamesList.get(g).getNtscCost();
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

    public ArrayList<GameCostItems> ExpensiveGames(String region){
        ArrayList<GameCostItems> temp = new ArrayList<>();
        int c = 0;
        switch(region){
            case "all":
                Collections.sort(gamesList, new AllGameItems.ByPrice());
                c = 10;
                break;
            case"pala":
                Collections.sort(gamesList, new AllGameItems.ByPalAPrice());
                c = 3;
                break;
            case "palb":
                Collections.sort(gamesList, new AllGameItems.ByPalBPrice());
                c = 3;
                break;
            case "us":
                Collections.sort(gamesList, new AllGameItems.ByUSPrice());
                c = 3;
                break;
            }


        for(int i=0; i < c; i++){
            GameCostItems item = new GameCostItems();
            item.setId(gamesList.get(i).getItemId());
            item.setName(gamesList.get(i).getName());
            item.setCost(gamesList.get(i).getGamePrice());
            temp.add(item);
        }
        return temp;
    }


    public String ReturnAllPrices(String type){
        String t = "";
        ArrayList<GameCostItems> temp = new ArrayList<>();
        temp = ExpensiveGames(type);
        int g = 1;
        for(int i=0; i < temp.size(); i++){
            t += g +" " + temp.get(i).getName() + "\t"+ getSettings().getCurrency() + String.format("%.2f", temp.get(i).getCost()) + "\n";
            g++;
        }

        return t;
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




     */

    public void ClearVariables(){
        //piechartCount = 0; palACount = 0; palBCount = 0; usCount = 0; ownedGameCount = 0; finishedGames = 0;

    }

    public String GameOrGames(){
        String temp;
        if(OwnedGameCount() > 1){
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
        SortGenreList();
        String temp ="";
        if(GenreItems.size() != -1){

            temp = GenreItems.get(0).getGName()+ ", "+
            GenreItems.get(1).getGName() + " and " + GenreItems.get(2).getGName();

        }
        return temp;
    }

    public void SortGenreList(){
        Collections.sort(GenreItems, new GenreCountItems.ByLargest());
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

    public int PalAGames(){
        int temp = 0;

        for(int i=0; i < GenreItems.size(); i++){
            temp += GenreItems.get(i).getPalAOwned();
        }

        return temp;
    }

    public int PalBGames(){
        int temp = 0;

        for(int i=0; i < GenreItems.size(); i++){
            temp += GenreItems.get(i).getPalBOwned();
        }

        return temp;
    }

    public int USGames(){
        int temp = 0;

        for(int i=0; i < GenreItems.size(); i++){
            temp += GenreItems.get(i).getUSOwned();
        }
        return temp;
    }

    public String GetPalAData(){
        String temp;
        int ownedGames = PalAGames();
        int releasedGames = StatisticsData.LicensedGamesCollection(0);
        double percentPalAOwned = ((double)  ownedGames / StatisticsData.LicensedGamesCollection(0)) * 100;
        double percentagepalacollection = ((double) ownedGames / OwnedGameCount()) * 100;
        String s = String.format("%.2f", percentPalAOwned);
        String perpalacoll = String.format("%.2f", percentagepalacollection);

        temp = mContext.getResources().getString(R.string.statsPala1) + " " + ownedGames + " " + mContext.getResources().getString(R.string.statsPala2) + " " + releasedGames + " " +
                mContext.getResources().getString(R.string.statsPala3) + " " + perpalacoll + mContext.getResources().getString(R.string.statsPala4)+ "\n"+
                mContext.getResources().getString(R.string.statsMostexpensivegame) + "\n" + ReturnAllPrices("pala");

        return temp;
    }

    public boolean palAData(){
        boolean temp = false;
        if(PalAGames() > 0){
            temp = true;
        }
        return temp;
    }

    public String GetPalBData(){
        String temp;
        int ownedGames = PalBGames();
        int releasedGames = StatisticsData.LicensedGamesCollection(0);
        double percentPalAOwned = ((double)  ownedGames / StatisticsData.LicensedGamesCollection(0)) * 100;
        double percentagepalacollection = ((double) ownedGames / OwnedGameCount()) * 100;
        String s = String.format("%.2f", percentPalAOwned);
        String perpalacoll = String.format("%.2f", percentagepalacollection);

        temp = mContext.getResources().getString(R.string.statsPala1) + " " + ownedGames + " " + mContext.getResources().getString(R.string.statsPala2) + " " + releasedGames + " " +
                mContext.getResources().getString(R.string.statsPala3) + " " + perpalacoll + mContext.getResources().getString(R.string.statsPala4)+"\n"+
                mContext.getResources().getString(R.string.statsMostexpensivegame) + "\n" + ReturnAllPrices("palb");

        return temp;
    }

    public boolean palBData(){
        boolean temp = false;
        if(PalBGames() > 0){
            temp = true;
        }
        return temp;
    }

    public String GetUSData(){
        String temp;
        int ownedGames = USGames();
        int releasedGames = StatisticsData.LicensedGamesCollection(3);
        double percentPalAOwned = ((double)  ownedGames / StatisticsData.LicensedGamesCollection(3)) * 100;
        double percentagepalacollection = ((double) ownedGames / OwnedGameCount()) * 100;
        String s = String.format("%.2f", percentPalAOwned);
        String perpalacoll = String.format("%.2f", percentagepalacollection);

        temp = mContext.getResources().getString(R.string.statsPala1) + " " + ownedGames + " " + mContext.getResources().getString(R.string.statsPala2) + " " + releasedGames + " " +
                mContext.getResources().getString(R.string.statsUS3) + " " + perpalacoll + mContext.getResources().getString(R.string.statsPala4) +"\n"+
                mContext.getResources().getString(R.string.statsMostexpensivegame) + "\n" + ReturnAllPrices("us");

        return temp;
    }

    public boolean USData(){
        boolean temp = false;
        if(USGames() > 0){
            temp = true;
        }
        return temp;
    }



}
