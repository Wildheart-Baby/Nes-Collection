package uk.co.bossdogsoftware.nes_collection.data;

public class SqlStatement {

    public static String Region(int regionCode){
        String sql;
        switch(regionCode) {
            case 0:
                sql = "(pal_a_release = 1)";
                break;
            case 1:
                sql = "(pal_uk_release = 1)";
                break;
            case 2:
                sql = "(pal_b_release = 1)";
                break;
            case 3:
                sql = "(ntsc_release = 1)";
                break;
            case 4:
                sql = "(pal_a_release = 1 or pal_b_release = 1)";
                break;
            case 5:
                sql = " flag_australia = 1";
                break;
            case 6:
                sql = " flag_austria = 1";
                break;
            case 7:
                sql = " flag_benelux = 1";
                break;
            case 8:
                sql = " flag_denmark = 1";
                break;
            case 9:
                sql = " flag_finland = 1";
                break;
            case 10:
                sql = " flag_france = 1";
                break;
            case 11:
                sql = " flag_germany = 1";
                break;
            case 12:
                sql = " flag_greece = 1";
                break;
            case 13:
                sql = " flag_ireland = 1";
                break;
            case 14:
                sql = " flag_italy = 1";
                break;
            case 15:
                sql = " flag_norway = 1";
                break;
            case 16:
                sql = " flag_poland = 1";
                break;
            case 17:
                sql = " flag_portugal = 1";
                break;
            case 18:
                sql = " flag_spain = 1";
                break;
            case 19:
                sql = " flag_sweden = 1";
            case 20:
                sql = " flag_switzerland = 1";
                break;
            case 21:
                sql = "(pal_a_release = 1 or pal_b_release = 1 or ntsc_release = 1)";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + regionCode);
        }
        return sql;
    }

    public static String RegionFlag(int regionCode){
        String flagid="";

        switch(regionCode){
            case 0:
                flagid = "pal_a";
                break;
            case 1:
                flagid = "uk";
                break;
            case 2:
                flagid = "euro";
                break;
            case 3:
                flagid = "us";
                break;
            case 4:
                flagid = "euro";
                break;
            case 5:
                flagid = "australia";
                break;
            case 6:
                flagid = "austria";
                break;
            case 7:
                flagid = "benelux";
                break;
            case 8:
                flagid = "denmark";
                break;
            case 9:
                flagid = "finland";
                break;
            case 10:
                flagid = "france";
                break;
            case 11:
                flagid = "germany";
                break;
            case 12:
                flagid = "greece";
                break;
            case 13:
                flagid = "ireland";
                break;
            case 14:
                flagid = "italy";
                break;
            case 15:
                flagid = "norway";
                break;
            case 16:
                flagid = "poland";
                break;
            case 17:
                flagid = "portugal";
                break;
            case 18:
                flagid = "spain";
                break;
            case 19:
                flagid = "sweden";
                break;
            case 20:
                flagid = "switzerland";
                break;
            case 21:
                flagid = "allregions";
                break;
        }
        return flagid;
    }

    public static String RegionTitle(int regionCode){
        String flagid="";

        switch(regionCode){
            case 0:
                flagid = "Pal A";
                break;
            case 1:
                flagid = "UK";
                break;
            case 2:
                flagid = "Pal B";
                break;
            case 3:
                flagid = "USA";
                break;
            case 4:
                flagid = "Pal A & B";
                break;
            case 5:
                flagid = "Australia";
                break;
            case 6:
                flagid = "Austria";
                break;
            case 7:
                flagid = "Benelux";
                break;
            case 8:
                flagid = "Denmark";
                break;
            case 9:
                flagid = "Finland";
                break;
            case 10:
                flagid = "France";
                break;
            case 11:
                flagid = "Germany";
                break;
            case 12:
                flagid = "Greece";
                break;
            case 13:
                flagid = "Ireland";
                break;
            case 14:
                flagid = "Italy";
                break;
            case 15:
                flagid = "Norway";
                break;
            case 16:
                flagid = "Poland";
                break;
            case 17:
                flagid = "Portugal";
                break;
            case 18:
                flagid = "Spain";
                break;
            case 19:
                flagid = "Sweden";
                break;
            case 20:
                flagid = "Switzerland";
                break;
            case 21:
                flagid = "All Regions";
                break;
        }
        return flagid;
    }

    public static String LicensedGames(int gameCode){
        String license = "";
        switch(gameCode){
            case 0:
                license = " and (unlicensed = 0)";
                break;
            case 1:
                license = " and (unlicensed = 0 or 1)";
                break;
        }

        return license;
    }

}
