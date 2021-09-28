package uk.co.pixoveeware.nes_collection;

import android.util.Log;

/**
 * Created by Wildheart on 22/07/2016.
 */
public class DataColorSet {
    private String color;
    private float dataValue;

    private String name;

    public DataColorSet() {
    }

    public DataColorSet(String color, float dataValue, String name) {
        this.dataValue = dataValue;
        this.color = color;
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
        Log.d("Pixo", "Setting colour");
    }

    public void setDataValue(float dataValue) {
        this.dataValue = dataValue;
        Log.d("Pixo", "Setting data value");
    }

    public String getColor() {
        return this.color;
    }

    public float getDataValue() {
        return this.dataValue;

    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }
}