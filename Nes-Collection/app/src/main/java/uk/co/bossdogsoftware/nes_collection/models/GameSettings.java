package uk.co.bossdogsoftware.nes_collection.models;

public class GameSettings {

    String region_sql, needed_games;
    String show_all_games, currency,  group_header;

    int shelf_size, show_price, game_view, owned_graphic, game_ordering,
            us_titles, show_condition, region_title, orderedby, licensed_or_not;

    public int getRegionCode() { return region_title; }
    public void setRegionCode(int region_title) { this.region_title = region_title; }

    public int getLicensedOrNot() { return licensed_or_not; }
    public void setLicensedOrNot(int licensed_or_not) { this.licensed_or_not = licensed_or_not; }

    public String getShowAllGames() { return show_all_games; }
    public void setShowAllGames(String show_all_games) { this.show_all_games = show_all_games; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public int getOrderedBy() {return orderedby; }
    public void setOrderedBy(int orderedby) { this.orderedby = orderedby; }

    public String getGroupHeader() { return group_header; }
    public void setGroupHeader(String group_header) { this.group_header = group_header; }

    public int getShelfSize() { return shelf_size; }
    public void setShelfSize(int shelf_size) { this.shelf_size = shelf_size; }

    public int getShowPrice() { return show_price; }
    public void setShowPrice(int show_price) { this.show_price = show_price; }

    public int getGameView() { return game_view; }
    public void setGameView(int game_view) { this.game_view = game_view; }

    public int getOwnedGraphic() { return owned_graphic; }
    public void setOwnedGraphic(int owned_graphic) { this.owned_graphic = owned_graphic; }

    public int getGameOrdering() { return game_ordering; }
    public void setGameOrdering(int game_ordering) { this.game_ordering = game_ordering; }

    public int getUsTitles() { return us_titles; }
    public void setUsTitles(int us_titles) { this.us_titles = us_titles; }

    public int getShowCondition() { return show_condition; }
    public void setShowCondition(int show_condition) { this.show_condition = show_condition; }





}
