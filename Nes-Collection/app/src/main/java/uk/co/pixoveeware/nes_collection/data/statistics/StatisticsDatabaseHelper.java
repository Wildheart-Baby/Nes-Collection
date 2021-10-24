package uk.co.pixoveeware.nes_collection.data.statistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.models.AllGameItems;
import uk.co.pixoveeware.nes_collection.models.GameSettings;

public class StatisticsDatabaseHelper extends SQLiteOpenHelper {

    ArrayList<AllGameItems> gamesList;
    String orderby, groupHeader, sql, theimage, thename, currency, thePublisher;
    int titles, ordering, regionCode, license;

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "nes.sqlite";


    public StatisticsDatabaseHelper(Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);

        ReadIntoArray();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void ReadIntoArray(){
        gamesList = new ArrayList<>();
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

    /*

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
}
