package uk.co.bossdogsoftware.nes_collection.models.statistics;

import java.util.Comparator;

public class GenreCountItems implements Comparable<GenreCountItems>{

    private String name, colour;
    private int _id, count, box_count, manual_count, owned, pal_a_owned, pal_a_box, pal_a_manual,
            pal_b_owned, pal_b_box, pal_b_manual,
            us_owned, us_box, us_manual,
            pal_a_boxed, pal_b_boxed, us_boxed,
            pal_a_complete, pal_b_complete, us_complete;

    private double pal_a_cost, pal_b_cost, us_cost;

    public GenreCountItems() {

    }

    public int getId() {
        return this._id;
    }

    public void setId(int _id) {
        this._id = _id;
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


    public int getPalABoxed() {
        return this.pal_a_boxed;
    }

    public void setPalABoxed(int pal_a_boxed) {
        this.pal_a_boxed = pal_a_boxed;
    }

    public int getPalBBoxed() {
        return this.pal_b_boxed;
    }

    public void setPalBBoxed(int pal_b_boxed) {
        this.pal_b_boxed = pal_b_boxed;
    }

    public int getUsBoxed() {
        return this.us_boxed;
    }

    public void setUsBoxed(int us_boxed) {
        this.us_boxed = us_boxed;
    }


    public int getPalAComplete() {
        return this.pal_a_complete;
    }

    public void setPalAComplete(int pal_a_complete) {
        this.pal_a_complete = pal_a_complete;
    }

    public int getPalBComplete() {
        return this.pal_b_boxed;
    }

    public void setPalBComplete(int pal_b_boxed) {
        this.pal_b_boxed = pal_b_boxed;
    }

    public int getUsComplete() {
        return this.us_boxed;
    }

    public void setUsComplete(int us_boxed) {
        this.us_boxed = us_boxed;
    }

    @Override
    public int compareTo(GenreCountItems genreCountItems) {
        return this._id > genreCountItems._id ? 1 : (this._id < genreCountItems._id ? - 1 : 0);
    }


    public static class ByLargest implements Comparator<GenreCountItems> {

        @Override
        public int compare(GenreCountItems item1, GenreCountItems item2) {
            return item1.count < item2.count ? 1 : (item1.count > item2.count ? - 1 : 0);
        }
    }

}



