package uk.co.bossdogsoftware.nes_collection.models.statistics;

public class GenreCountItems {

    private String name, colour;
    private int count, box_count, manual_count, owned, pal_a_owned, pal_a_box, pal_a_manual,
            pal_b_owned, pal_b_box, pal_b_manual,
            us_owned, us_box, us_manual;

    private double pal_a_cost, pal_b_cost, us_cost;

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

    public int getGBoxCount() {
        return this.box_count;
    }

    public void setGBoxCount(int box_count) {
        this.box_count = box_count;
    }

    public int getGManualCount() {
        return this.manual_count;
    }

    public void setGManualCount(int manual_count) {
        this.manual_count = manual_count;
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


    public double getGCost() {
        return this.pal_a_cost;
    }

    public void setGCost(double pal_a_cost) {
        this.pal_a_cost = pal_a_cost;
    }


    public int getPalAOwned() {
        return this.pal_a_owned;
    }

    public void setPalAOwned(int pal_a_owned) {
        this.pal_a_owned = pal_a_owned;
    }

    public int getPalABox() {
        return this.pal_a_box;
    }

    public void setPalABox(int pal_a_box) {
        this.pal_a_box = pal_a_box;
    }

    public int getPalAManual() {
        return this.pal_a_manual;
    }

    public void setPalAManual(int pal_a_manual) {
        this.pal_a_manual = pal_a_manual;
    }



    public int getPalBOwned() {
        return this.pal_b_owned;
    }

    public void setPalBOwned(int pal_b_owned) {
        this.pal_b_owned = pal_b_owned;
    }

    public int getPalBBox() {
        return this.pal_b_box;
    }

    public void setPalBBox(int pal_b_box) {
        this.pal_b_box = pal_b_box;
    }

    public int getPalBManual() {
        return this.pal_b_manual;
    }

    public void setPalBManual(int pal_b_manual) {
        this.pal_b_manual = pal_b_manual;
    }


    public int getUSOwned() {
        return this.us_owned;
    }

    public void setUSOwned(int us_owned) {
        this.us_owned = us_owned;
    }

    public int getUSBox() {
        return this.us_box;
    }

    public void setUSBox(int us_box) {
        this.us_box = us_box;
    }

    public int getUSManual() {
        return this.us_manual;
    }

    public void setUSManual(int us_manual) {
        this.us_manual = us_manual;
    }

}



