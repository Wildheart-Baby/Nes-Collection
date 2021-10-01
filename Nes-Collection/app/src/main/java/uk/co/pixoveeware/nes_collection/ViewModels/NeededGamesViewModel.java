package uk.co.pixoveeware.nes_collection.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.data.DatabaseHelper;
import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

public class NeededGamesViewModel extends AndroidViewModel {

    DatabaseHelper dbh;
    public ArrayList<GameListItems> gamesList;
    public ArrayList<GameItemsIndex> indexList;
    ArrayList<GameItems> specificGames;
    ArrayList<GameItems> allGames;

    public String regionTitle, regionFlag, gamesCount;
    public int viewType;
    String times;

    public NeededGamesViewModel(@NonNull Application application) {
        super(application);
        dbh = new DatabaseHelper(application);
        gamesList = dbh.getGameslist("needed");
        indexList = dbh.gamesIndex("needed");
        regionFlag = dbh.regionFlag();
        regionTitle = dbh.regionTitle();
        gamesCount = dbh.gamesCount("needed");
        viewType = dbh.viewType();
    }
}
