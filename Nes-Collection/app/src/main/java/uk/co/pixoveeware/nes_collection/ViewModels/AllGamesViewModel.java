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
        gamesList = dbh.getGames(param);
        indexList = dbh.gamesIndex(param);
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


}
