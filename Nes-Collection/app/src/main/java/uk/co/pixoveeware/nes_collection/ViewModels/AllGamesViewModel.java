package uk.co.pixoveeware.nes_collection.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import uk.co.pixoveeware.nes_collection.data.DatabaseHelper;
import uk.co.pixoveeware.nes_collection.models.GameItem;
import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

public class AllGamesViewModel extends AndroidViewModel {

    DatabaseHelper dbh;
    public ArrayList<GameItems> gamesList;
    public ArrayList<GameItemsIndex> indexList;

    public String regionTitle, regionFlag, gamesCount;
    public int viewType;

    public AllGamesViewModel(Application application, String param){
        super(application);
        dbh = new DatabaseHelper(application);
        //gamesList = dbh.getGames(param);
        //indexList = dbh.gamesIndex(param);
        regionFlag = dbh.regionFlag();
        regionTitle = dbh.regionTitle();
        viewType = dbh.viewType();
    }

    /*public ArrayList<GameListItems> ConvertToLightList(){

        allGames = dbh.getGames("all");
        Iterator<GameItems> itr = allGames.iterator();
        while (itr.hasNext()) {
            Log.d("pixo-time2","items for list: "+ times);
            GameListItems gameListItems = new GameListItems();
            gameListItems.setGroup(itr.next().group);
            gameListItems.setItemId(itr.next()._id);//set the array with the data from the database
            gameListItems.setImage(itr.next().image);
            gameListItems.setName(itr.next().name);
            gameListItems.setPublisher(itr.next().publisher);
            gamesList.add(gameListItems);//add items to the arraylist
        }
        return gamesList;
    }*/

    public ArrayList<GameItems> GetGames(String games){
        gamesList = dbh.getGames(games);
        return gamesList;
    }

    public ArrayList<GameItemsIndex> GetIndex(String games){
        indexList = dbh.gamesIndex(games);
        return indexList;
    }

    public String GamesCount(String type){
        gamesCount = dbh.gamesCount(type);
        return gamesCount;
    }

    public String RegionTitle(String type){
        regionTitle = dbh.regionTitle();
        return regionTitle;
    }

    public GameItem GetGameDetails(int games){
        return dbh.getGame(games);
    }

    public String FragmentSubTitleText(String page){
        String title;

        switch(page){
            case "all":
                title = " has " + dbh.gamesCount("all") + "games";
                break;
            case "needed":
                title = "You need " + dbh.gamesCount("needed") + " games";
                break;
            default:
                title = "";
        }

        return title;
    }

    public void WriteGame(int listPos, GameItem theGame){
        GameItem game = theGame;
        dbh.updateGameRecord(game.owned, game.cart, game.box, game.manual, game.pal_a_cart, game.pal_a_box, game.pal_a_manual, game.pal_a_owned, game.pal_b_cart, game.pal_b_manual, game.pal_b_box, game.pal_b_owned, game.ntsc_cart, game.ntsc_box, game.ntsc_manual, game.ntsc_owned, game.gamePrice, game.gameCondition, game.pal_a_owned, game._id);
        gamesList.get(listPos).setOwned(game.owned);
        gamesList.get(listPos).setCart(game.cart);
        gamesList.get(listPos).setBox(game.box);
        gamesList.get(listPos).setManual(game.manual);
        gamesList.get(listPos).setCartPalA(game.pal_a_cart);
        gamesList.get(listPos).setBoxPalA(game.pal_a_box);
        gamesList.get(listPos).setManualPalA(game.pal_a_manual);
        gamesList.get(listPos).setPalAOwned(game.pal_a_owned);
        gamesList.get(listPos).setCartPalB(game.pal_b_cart);
        gamesList.get(listPos).setManualPalB(game.pal_b_manual);
        gamesList.get(listPos).setBoxPalB(game.pal_b_box);
        gamesList.get(listPos).setPalBOwned(game.pal_b_owned);
        gamesList.get(listPos).setCartNtsc(game.ntsc_cart);
        gamesList.get(listPos).setBoxNtsc(game.ntsc_box);
        gamesList.get(listPos).setManualNtsc(game.ntsc_manual);
        gamesList.get(listPos).setNtscOwned(game.ntsc_owned);
        gamesList.get(listPos).setGamePrice(game.gamePrice);
        gamesList.get(listPos).setGameCondition(game.gameCondition);
    }

}
