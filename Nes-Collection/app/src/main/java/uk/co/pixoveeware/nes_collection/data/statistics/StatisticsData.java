package uk.co.pixoveeware.nes_collection.data.statistics;

import android.graphics.Color;

public class StatisticsData {

    public static int LicensedGamesCollection(int regionCode){
        int collection;
        switch(regionCode) {
            case 0:
                collection = 256;
                break;
            case 1:
                collection = 234;
                break;
            case 2:
                collection = 351;
                break;
            case 3:
                collection = 679;
                break;
            case 4:
                collection = 355;
                break;
            case 5:
                collection = 109;
                break;
            case 6:
                collection = 313;
                break;
            case 7:
                collection = 309;
                break;
            case 8:
                collection = 316;
                break;
            case 9:
                collection = 314;
                break;
            case 10:
                collection = 211;
                break;
            case 11:
                collection = 243;
                break;
            case 12:
                collection = 310;
                break;
            case 13:
                collection = 242;
                break;
            case 14:
                collection = 152;
                break;
            case 15:
                collection = 317;
                break;
            case 16:
                collection = 310;
                break;
            case 17:
                collection = 310;
                break;
            case 18:
                collection = 206;
                break;
            case 19:
                collection = 317;
            case 20:
                collection = 314;
                break;
            case 21:
                collection = 709;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + regionCode);
        }
        return collection;
    }


}
