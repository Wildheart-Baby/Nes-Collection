package uk.co.pixoveeware.nes_collection.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.data.DatabaseHelper;
import uk.co.pixoveeware.nes_collection.models.AllGameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

public class LightGamesViewModel extends AndroidViewModel {

    DatabaseHelper dbh;
    public ArrayList<GameListItems> gamesList;
    public ArrayList<GameItemsIndex> indexList;
    ArrayList<AllGameItems> specificGames;
    ArrayList<AllGameItems> allGames;

    public String regionTitle, regionFlag, gamesCount;
    public int viewType;
    String times;

    public LightGamesViewModel(Application application, String param) {
        super(application);
        dbh = new DatabaseHelper(application);
        gamesList = dbh.getGameslist(param);
        indexList = dbh.gamesIndex(param);
        regionFlag = dbh.regionFlag();
        regionTitle = dbh.regionTitle();
        gamesCount = dbh.gamesCount(param);
        viewType = dbh.viewType();
    }
}
