package uk.co.pixoveeware.nes_collection.models;

public class GameSettings {

    String region_sql, needed_games, licensed_or_not;
    String show_all_games, currency, orderedby, group_header;

    int shelf_size, show_price, game_view, owned_graphic, game_ordering, us_titles, show_condition, region_title;


    public String getRegionSql() { return region_sql; }
    public void setRegionSql(String region_sql) { this.region_sql = region_sql; }

    public int getRegionTitle() { return region_title; }
    public void setRegionTitle(int region_title) { this.region_title = region_title; }

    public String getNeededGames() { return needed_games; }
    public void setNeededGames(String needed_games) { this.needed_games = needed_games; }

    public String getLicensedOrNot() { return licensed_or_not; }
    public void setLicensedOrNot(String licensed_or_not) { this.licensed_or_not = licensed_or_not; }

    public String getShowAllGames() { return show_all_games; }
    public void setShowAllGames(String show_all_games) { this.show_all_games = show_all_games; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getOrderedBy() {return orderedby; }
    public void setOrderedBy(String orderedby) { this.orderedby = orderedby; }

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
