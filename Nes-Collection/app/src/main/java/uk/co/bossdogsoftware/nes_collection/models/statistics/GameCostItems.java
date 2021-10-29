package uk.co.bossdogsoftware.nes_collection.models.statistics;

import java.util.Comparator;

public class GameCostItems implements Comparable<GameCostItems>{

    private int id;
    private double price;
    private String name;

    public GameCostItems(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCost() {
        return this.price;
    }

    public void setCost(double price) {
        this.price = price;
    }


    @Override
    public int compareTo(GameCostItems gameCostItems) {
        return this.id > gameCostItems.id ? 1 : (this.id < gameCostItems.id ? - 1 : 0);
    }

    public static class ByPrice implements Comparator<GameCostItems>{

        @Override
        public int compare(GameCostItems item1, GameCostItems item2) {
            return item1.price > item2.price ? 1 : (item1.price < item2.price ? - 1 : 0);
        }
    }
}
