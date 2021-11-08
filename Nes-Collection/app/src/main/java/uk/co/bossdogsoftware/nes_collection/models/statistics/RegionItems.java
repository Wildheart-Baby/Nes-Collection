package uk.co.bossdogsoftware.nes_collection.models.statistics;

public class RegionItems {

    private String regionName;
    private int regionTotal;

    public RegionItems(){

    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getRegionTotal() {
        return this.regionTotal;
    }

    public void setRegionTotal(int regionTotal) {
        this.regionTotal = regionTotal;
    }

}
