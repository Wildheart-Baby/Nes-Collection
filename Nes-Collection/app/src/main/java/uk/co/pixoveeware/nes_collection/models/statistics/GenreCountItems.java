package uk.co.pixoveeware.nes_collection.models.statistics;

public class GenreCountItems {

    private String name, colour;
    private int count, owned, pal_a_owned, pal_b_owned, us_owned;

    public GenreCountItems() {

    }

    public String getGName() {
        return name;
    }

    public void setGName(String name) {
        this.name = name;
    }

    public int getGCount() {
        return this.count;
    }

    public void setGCount(int count) {
        this.count = count;
    }

    public String getGColour() {
        return colour;
    }

    public void setGColour(String colour) {
        this.colour = colour;
    }

    public int getGOwned() {
        return this.owned;
    }

    public void setGOwned(int count) {
        this.owned = owned;
    }


    public int getPalAOwned() {
        return this.pal_a_owned;
    }

    public void setPalAOwned(int pal_a_owned) {
        this.pal_a_owned = pal_a_owned;
    }


    public int getPalBOwned() {
        return this.pal_b_owned;
    }

    public void setPalBOwned(int pal_b_owned) {
        this.pal_b_owned = pal_b_owned;
    }


    public int getUSOwned() {
        return this.us_owned;
    }

    public void setUSOwned(int us_owned) {
        this.us_owned = us_owned;
    }



}



