package uk.co.pixoveeware.nes_collection.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.activities.MainActivity;
import uk.co.pixoveeware.nes_collection.data.DatabaseHelper;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

public class MainActivityViewModel extends AndroidViewModel {

    DatabaseHelper dbh;
    ArrayList<GameListItems> gamesList;
    ArrayList<GameItemsIndex> indexList;

    public String regionTitle, regionFlag, gamesCount;
    //int ;



    public MainActivityViewModel(Application application){
        super(application);
        dbh = new DatabaseHelper(application);
        regionFlag = dbh.regionFlag();
        gamesCount = dbh.gamesCount("all");
        regionTitle = dbh.regionTitle();
        gamesList = dbh.getGameslist("all");
    }

    public ArrayList<GameListItems> GetGames(String games){
        gamesList = dbh.getGameslist(games);
        return gamesList;
    }



}
