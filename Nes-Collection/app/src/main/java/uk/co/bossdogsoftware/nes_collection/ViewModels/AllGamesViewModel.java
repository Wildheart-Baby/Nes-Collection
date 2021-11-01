package uk.co.bossdogsoftware.nes_collection.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import uk.co.bossdogsoftware.nes_collection.data.DatabaseHelper;
import uk.co.bossdogsoftware.nes_collection.models.GameItem;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameItemsIndex;
import uk.co.bossdogsoftware.nes_collection.models.GameSettings;

public class AllGamesViewModel extends AndroidViewModel {

    DatabaseHelper dbh;
    public ArrayList<AllGameItems> gamesList;
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
        Iterator<AllGameItems> itr = allGames.iterator();
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

    public ArrayList<AllGameItems> GetGames(String games){
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

    public int OwnedGamesCount(){
        int gCount = dbh.ownedGamesCount();
        return gCount;
    }

    public String RegionTitle(String type){
        regionTitle = dbh.regionTitle();
        return regionTitle;
    }

    public GameItem GetGameDetails(int games){
        return dbh.getGame(games);
    }

    public String FragmentSubTitleText(String page, String region){
        String title;
        title = dbh.fragmentSubtitle(page, region);
        return title;
    }

    public void WriteGame(int listPos, GameItem theGame){
        GameItem game = theGame;
        dbh.updateGameRecord(game.owned, game.cart, game.box, game.manual, game.pal_a_cart, game.pal_a_box, game.pal_a_manual, game.pal_a_owned, game.pal_a_cost, game.pal_b_cart, game.pal_b_manual, game.pal_b_box, game.pal_b_owned, game.pal_b_cost, game.ntsc_cart, game.ntsc_box, game.ntsc_manual, game.ntsc_owned, game.ntsc_cost, game.gamePrice, game.onShelf, game.gameCondition, game.pal_a_owned, game._id);
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
        gamesList.get(listPos).setWishlist(game.wishlist);
        gamesList.get(listPos).setGameCondition(game.gameCondition);
    }

    public void favouriteGame(int listPos, int gameId){
        if(gamesList.get(listPos).getFavourite() == 0 ){
            gamesList.get(listPos).setFavourite(1);
            dbh.favouriteGame("1", gameId);
        } else {
            gamesList.get(listPos).setFavourite(0);
            dbh.favouriteGame("0", gameId);
        }
    }

    public void wishList(int listPos, int gameId){
        if(gamesList.get(listPos).getWishlist() == 0 ){
            gamesList.get(listPos).setWishlist(1);
            dbh.wishList("1", gameId);
        } else {
            gamesList.get(listPos).setWishlist(0);
            dbh.wishList("0", gameId);
        }
    }

    public String SpecificTitle(String title){
        try { // We can face index out of bound exception if the string is null
            if(title.equals("uk") || title.equals("us")){
                title = title.toUpperCase();
            } else {
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
            }
        }catch (Exception e){}

        return title;
    }

    public ArrayList<AllGameItems> GetSpecificGames(String InformationType, String Query){
        gamesList = dbh.getSpecificSearch(InformationType, Query);
        return gamesList;
    }

    public ArrayList<GameItemsIndex> GetSpecificGamesIndex(String InformationType, String Query){
        indexList = dbh.specificGamesIndex(InformationType, Query);
        return indexList;
    }

    public ArrayList<AllGameItems> GetShelfOrder(){
        gamesList = dbh.ShelfOrder();
        return gamesList;
    }

    public ArrayList<GameItemsIndex> GetShelfOrderIndex(){
        indexList = dbh.GetShelfOrderIndex();
        return indexList;
    }


    public String convertLogoTitle(String title){
        title = title.toLowerCase();
        title = title.replaceAll("-","_");
        title = title.replaceAll(" ", "_");
        return title;
    }

    public GameSettings AppSettings(){
        GameSettings gSettings = dbh.getSettings();
        return gSettings;
    }

    public void updateSettings(GameSettings settings){
        dbh.UpdateSettings(settings);
    }

    public int ShowPrices(){
        GameSettings gSettings = dbh.getSettings();
        return gSettings.getShowPrice();
    }

    public String GetCurrency(){
        GameSettings gSettings = dbh.getSettings();
        return gSettings.getCurrency();
    }

    public int GetTitles(){
        return dbh.getSettings().getUsTitles();
    }

}
