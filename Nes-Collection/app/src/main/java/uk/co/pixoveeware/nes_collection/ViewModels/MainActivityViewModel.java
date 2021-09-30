package uk.co.pixoveeware.nes_collection.ViewModels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import uk.co.pixoveeware.nes_collection.activities.MainActivity;
import uk.co.pixoveeware.nes_collection.data.DatabaseHelper;
import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

public class MainActivityViewModel extends AndroidViewModel {

    DatabaseHelper dbh;
    ArrayList<GameListItems> gamesList;
    ArrayList<GameItemsIndex> indexList;
    ArrayList<GameItems> specificGames;
    ArrayList<GameItems> allGames;

    public String regionTitle, regionFlag, gamesCount;
    public int viewType;
    String times;

    public MainActivityViewModel(Application application){
        super(application);
        times = getCurrentTimeStamp();
        Log.d("pixo-time2","Start activity: "+ times);
        dbh = new DatabaseHelper(application);
        //ConvertToLightList();
        regionFlag = dbh.regionFlag();
        regionTitle = dbh.regionTitle();
        viewType = dbh.viewType();
        times = getCurrentTimeStamp();
        Log.d("pixo-time2","finish vm load: "+ times);
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("mm:ss.SSS").format(new Date());
    }

    public ArrayList<GameListItems> ConvertToLightList(){
        times = getCurrentTimeStamp();
        Log.d("pixo-time2","start conversion: "+ times);
        allGames = dbh.getGames("all");
        times = getCurrentTimeStamp();
        Log.d("pixo-time2","start iteration: "+ times);
        Iterator<GameItems> itr = allGames.iterator();
        while (itr.hasNext()) {
            times = getCurrentTimeStamp();
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
    }

    public ArrayList<GameListItems> LightList(){
        return gamesList;
    }

    public ArrayList<GameListItems> GetGames(String games){
        gamesList = dbh.getGameslist(games);
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

    public ArrayList<GameItems> GetGamesDetails(String games){
        gamesList = dbh.getGameslist(games);
        return specificGames;
    }

}
