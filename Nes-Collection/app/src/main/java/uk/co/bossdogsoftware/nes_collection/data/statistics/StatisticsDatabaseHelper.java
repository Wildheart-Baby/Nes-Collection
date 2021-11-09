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
import uk.co.bossdogsoftware.nes_collection.data.SqlStatement;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameSettings;
import uk.co.bossdogsoftware.nes_collection.models.statistics.GameCostItems;
import uk.co.bossdogsoftware.nes_collection.models.statistics.GenreCountItems;
import uk.co.bossdogsoftware.nes_collection.models.statistics.RegionItems;

public class StatisticsDatabaseHelper extends SQLiteOpenHelper {

    String[] palanames;
    String[] palbnames;
    String[] usnames;
    String[] gamenames;
    ArrayList<GenreCountItems> GenreItems;
    List<String>genreNames, topGames;
    public String[] GenreNames, RegionNames;
    String popgenre;
    public float[] datapoints, RegionDataPoints;
    public int[] piecolours, BarChartColours;

    String[] PieColours = new String[]{"#02A0E9","#FFD800","#4800FF","#FF6A00","#A975CE","#FF0000","#FFAC78","#7CA60F","#FF3F42","#FFB548","#5a45e1"};
    String[] BarColours = new String[]{"#FFD800","#A975CE","#7CA60F"};
    String[] Regions = new String[]{"Pal A", "Pal B", "US"};

    ArrayList<AllGameItems> gamesList;
    ArrayList<GenreCountItems>genreList;

    ArrayList<RegionItems> regionList;

    ArrayList<GameCostItems> palAPrices;
    ArrayList<GameCostItems> palBPrices;
    ArrayList<GameCostItems> usPrices;
    GameSettings gSettings;


    String  groupHeader, sql, theimage, thename, currency, thePublisher, expensivePalAGame, expensivePalBGame, expensiveUSGame;
    int orderby, titles, ordering, regionCode, license,_id;

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
        gSettings = getSettings();

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
        regionList = new ArrayList<>();
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
                gSettings.setOrderedBy(c.getInt(c.getColumnIndex("orderedby")));
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

                finishedGames += gamesList.get(g).getFinished();

                if (gamesList.get(g).getGenre().equals(GenreItems.get(i).getGName())){
                    //Log.d("pixo", "game list genre: " + gamesList.get(g).getGenre() + " genre list genre: " + GenreItems.get(i).getGName());
                    if (gamesList.get(g).getCartPalA() == 8783) {
                        theCount = GenreItems.get(i).getPalAOwned();
                        theCount ++;
                        GenreItems.get(i).setPalAOwned(theCount);
                        costPalA += gamesList.get(g).getPalACost();
                    }
                    if (gamesList.get(g).getCartPalB() == 8783) {
                        theCount = GenreItems.get(i).getPalBOwned();
                        theCount ++;
                        GenreItems.get(i).setPalBOwned(theCount);
                        costPalB += gamesList.get(g).getPalBCost();
                    }
                    if (gamesList.get(g).getCartNtsc() == 8783) {
                        theCount = GenreItems.get(i).getUSOwned();
                        theCount ++;
                        GenreItems.get(i).setUSOwned(theCount);
                        costUs += gamesList.get(g).getNtscCost();
                    }
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
        setBarData();
    }

    public void setBarData(){
        int palACount = 0;
        int palBCount = 0;
        int usCount = 0;

        for (int g=0; g < GenreItems.size(); g++) {
            palACount += GenreItems.get(g).getPalAOwned();
            palBCount += GenreItems.get(g).getPalBOwned();
            usCount += GenreItems.get(g).getUSOwned();
        }

        if(palACount > 0){
            RegionItems item = new RegionItems();

            item.setRegionName("Pal A");
            item.setRegionTotal(palACount);
            regionList.add(item);
        }

        if(palBCount > 0){
            RegionItems item = new RegionItems();

            item.setRegionName("Pal B");
            item.setRegionTotal(palBCount);
            regionList.add(item);
        }

        if(usCount > 0){
            RegionItems item = new RegionItems();

            item.setRegionName("US");
            item.setRegionTotal(usCount);
            regionList.add(item);
        }


        RegionNames = new String[regionList.size()];
        RegionDataPoints = new float[regionList.size()];
        BarChartColours = new int[regionList.size()];
        int io = 0;

        for(int i=0; i < regionList.size(); i++){
            RegionNames[io] = regionList.get(i).getRegionName();
            RegionDataPoints[io] = regionList.get(i).getRegionTotal();
            BarChartColours[io] = Color.parseColor(BarColours[i]);
            io ++;
        }
    }

    public ArrayList<GameCostItems> ExpensiveGames(String region){
        ArrayList<GameCostItems> temp = new ArrayList<>();
        double price = 0.0;
        int c = 0;
        switch(region){
            case "all":
                Collections.sort(gamesList, new AllGameItems.ByPrice());
                if(gamesList.size() >=10 ){ c = 10; } else { c = gamesList.size(); }
                break;
            case"pala":
                Collections.sort(gamesList, new AllGameItems.ByPalAPrice());
                if(gamesList.size() >=3 ){ c = 3; } else { c = gamesList.size(); }
                break;
            case "palb":
                Collections.sort(gamesList, new AllGameItems.ByPalBPrice());
                if(gamesList.size() >=3 ){ c = 3; } else { c = gamesList.size(); }
                break;
            case "us":
                Collections.sort(gamesList, new AllGameItems.ByUSPrice());
                if(gamesList.size() >=3 ){ c = 3; } else { c = gamesList.size(); }
                break;
        }


        for(int i=0; i < c; i++){
            switch(region){
                case "all":
                    price = gamesList.get(i).getGamePrice();
                    break;
                case"pala":
                    price = gamesList.get(i).getPalACost();
                    break;
                case "palb":
                    price = gamesList.get(i).getPalBCost();
                    break;
                case "us":
                    price = gamesList.get(i).getNtscCost();
                    break;
            }


            if(price > 0.0){
                GameCostItems item = new GameCostItems();
                item.setId(gamesList.get(i).getItemId());
                item.setName(gamesList.get(i).getName());
                item.setCost(price);
                temp.add(item);
            }
        }
        return temp;
    }


    public String ReturnAllPrices(String type){
        String t = "";
        ArrayList<GameCostItems> temp = new ArrayList<>();
        temp = ExpensiveGames(type);
        int g = 1;
        if(temp.size() == 1){t = "Your most expensive game is:\n";}
        if(temp.size() >= 2 ){ t = "Your most expensive games are: \n";}
        for(int i=0; i < temp.size(); i++){
            t += g +" " + temp.get(i).getName() + "\t"+ getSettings().getCurrency() + String.format("%.2f", temp.get(i).getCost()) + "\n";
            g++;
        }

        return t;
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
        Collections.sort(GenreItems, new GenreCountItems.ByLargest());
        String temp ="";
        int listSize = GenreItems.size();
        if(listSize > 3){temp = GenreItems.get(0).getGName()+ ", "+ GenreItems.get(1).getGName() + " and " + GenreItems.get(2).getGName();}
        if(listSize == 2){temp = GenreItems.get(0).getGName() + " and " + GenreItems.get(1).getGName();}
        if(listSize == 1){temp = GenreItems.get(0).getGName();}
        return temp;
    }

    public Boolean ShowPieChart()  { return GenreItems.size() > 0;}

    public Boolean ShowBarChart() { return true;}

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
                mContext.getResources().getString(R.string.statsPala3) + " " + perpalacoll + mContext.getResources().getString(R.string.statsPala4)+ "\n"+"\n"+
                ReturnAllPrices("pala");

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
        double percentPalBOwned = ((double)  ownedGames / StatisticsData.LicensedGamesCollection(0)) * 100;
        double percentagepalacollection = ((double) ownedGames / OwnedGameCount()) * 100;
        String s = String.format("%.2f", percentPalBOwned);
        String perpalbcoll = String.format("%.2f", percentagepalacollection);

        temp = mContext.getResources().getString(R.string.statsPala1) + " " + ownedGames + " " + mContext.getResources().getString(R.string.statsPala2) + " " + releasedGames + " " +
                mContext.getResources().getString(R.string.statsPalb3) + " " + perpalbcoll + mContext.getResources().getString(R.string.statsPala4)+"\n"+"\n"+
                ReturnAllPrices("palb");

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
        double percentUSOwned = ((double)  ownedGames / StatisticsData.LicensedGamesCollection(3)) * 100;
        double percentageuscollection = ((double) ownedGames / OwnedGameCount()) * 100;
        String s = String.format("%.2f", percentUSOwned);
        String peruscoll = String.format("%.2f", percentageuscollection);

        temp = mContext.getResources().getString(R.string.statsPala1) + " " + ownedGames + " " + mContext.getResources().getString(R.string.statsPala2) + " " + releasedGames + " " +
                mContext.getResources().getString(R.string.statsUS3) + " " + peruscoll + mContext.getResources().getString(R.string.statsPala4) +"\n"+"\n"+
                ReturnAllPrices("us");

        return temp;
    }

    public boolean USData(){
        boolean temp = false;
        if(USGames() > 0){
            temp = true;
        }
        return temp;
    }

    public String CompleteInBox(){
        int temp = 0;

        for (int g=0; g < gamesList.size(); g++) {
                if (gamesList.get(g).getCartPalA() == 8783 && gamesList.get(g).getBoxPalA() == 8783 && gamesList.get(g).getManualPalA() == 8783) { temp++; }
                if (gamesList.get(g).getCartPalB() == 8783 && gamesList.get(g).getBoxPalB() == 8783 && gamesList.get(g).getManualPalB() == 8783) { temp++; }
                if (gamesList.get(g).getCartNtsc() == 8783 && gamesList.get(g).getBoxNtsc() == 8783 && gamesList.get(g).getManualNtsc() == 8783) { temp++; }
        }

        return "You have " + temp + " games that are complete in box";
    }

    public String BoxedGames(){
        int temp = 0;

        for (int g=0; g < gamesList.size(); g++) {
            if (gamesList.get(g).getCartPalA() == 8783 && gamesList.get(g).getBoxPalA() == 8783 && gamesList.get(g).getManualPalA() == 32573) { temp++; }
            if (gamesList.get(g).getCartPalB() == 8783 && gamesList.get(g).getBoxPalB() == 8783 && gamesList.get(g).getManualPalB() == 32573) { temp++; }
            if (gamesList.get(g).getCartNtsc() == 8783 && gamesList.get(g).getBoxNtsc() == 8783 && gamesList.get(g).getManualNtsc() == 32573) { temp++; }
        }

        return "You have " + temp +" that are boxed";
    }

    public String LooseCarts(){
        int temp = 0;

        for (int g=0; g < gamesList.size(); g++) {
            if (gamesList.get(g).getCartPalA() == 8783 && gamesList.get(g).getBoxPalA() == 32573 && gamesList.get(g).getManualPalA() == 32573) { temp++; }
            if (gamesList.get(g).getCartPalB() == 8783 && gamesList.get(g).getBoxPalB() == 32573 && gamesList.get(g).getManualPalB() == 32573) { temp++; }
            if (gamesList.get(g).getCartNtsc() == 8783 && gamesList.get(g).getBoxNtsc() == 32573 && gamesList.get(g).getManualNtsc() == 32573) { temp++; }
        }

        return "You have " + temp + " loose carts";
    }



    //
    //
    //                    if (gamesList.get(g).getManualPalA() == 8783) {
    //                        theCount = GenreItems.get(i).getPalAManual();
    //                        theCount ++;
    //                        GenreItems.get(i).setPalAManual(theCount);
    //                    }
    //                    if (gamesList.get(g).getManualPalB() == 8783) {
    //                        theCount = GenreItems.get(i).getPalBManual();
    //                        theCount ++;
    //                        GenreItems.get(i).setPalBManual(theCount);
    //                    }
    //                    if (gamesList.get(g).getManualNtsc() == 8783) {
    //                        theCount = GenreItems.get(i).getUSManual();
    //                        theCount ++;
    //                        GenreItems.get(i).setUSManual(theCount);
    //                    }   //Log.d("pixo", "matching genre names: " + genreNames);



    public int OwnedGameCount(){
        int temp = 0;

        for(int i=0; i < GenreItems.size(); i++){
            temp += GenreItems.get(i).getGCount();
        }

        return temp;
    }

    public int ownedBoxes(){
        String t = "";
        int temp = 0;

        for(int i=0; i < gamesList.size(); i++){
            if (gamesList.get(i).getBoxPalA() == 8783) { temp++;}
            if (gamesList.get(i).getBoxPalB() == 8783) { temp++; }
            if (gamesList.get(i).getBoxNtsc() == 8783) { temp++; }
        }
        return temp;
    }

    public int ownedManuals(){
        int temp = 0;

        for(int i=0; i < gamesList.size(); i++){
            if (gamesList.get(i).getManualPalA() == 8783) { temp++;}
            if (gamesList.get(i).getManualPalB() == 8783) { temp++; }
            if (gamesList.get(i).getManualNtsc() == 8783) { temp++; }
        }

        return temp;
    }

    public int top100(){
        int temp = 0;
        for(int i=0; i < gamesList.size(); i++){
            if (gamesList.get(i).getRanking() != 0) { temp++;}
        }
        return temp;
    }

}
