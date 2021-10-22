package uk.co.pixoveeware.nes_collection.models.spinners;

import android.graphics.Region;

import java.util.ArrayList;
import java.util.List;

import uk.co.pixoveeware.nes_collection.R;

public class Data {

    public static List<Spinner> getPlayedList(){
        List<Spinner> playedList = new ArrayList<>();

        Spinner Played1 = new Spinner();
        Played1.setName("Tried it ");
        Played1.setImage(R.drawable.played_1);
        playedList.add(Played1);

        Spinner Played2 = new Spinner();
        Played2.setName("Played it");
        Played2.setImage(R.drawable.played_2);
        playedList.add(Played2);

        Spinner Played3 = new Spinner();
        Played3.setName("1/4 way");
        Played3.setImage(R.drawable.played_3);
        playedList.add(Played3);

        Spinner Played4 = new Spinner();
        Played4.setName("1/2 way");
        Played4.setImage(R.drawable.played_4);
        playedList.add(Played4);

        Spinner Played5 = new Spinner();
        Played5.setName("3/4 way");
        Played5.setImage(R.drawable.played_5);
        playedList.add(Played5);

        Spinner Played6 = new Spinner();
        Played6.setName("Beat it");
        Played6.setImage(R.drawable.played_6);
        playedList.add(Played6);

        Spinner Played7 = new Spinner();
        Played7.setName("100% done");
        Played7.setImage(R.drawable.played_7);
        playedList.add(Played7);

        return playedList;
    }

    public static List<Spinner> getConditionList(){
        List<Spinner> conditionList = new ArrayList<>();

        Spinner Condition1 = new Spinner();
        Condition1.setName("Poor");
        Condition1.setImage(R.drawable.condition_1);
        conditionList.add(Condition1);

        Spinner Condition2 = new Spinner();
        Condition2.setName("Average");
        Condition2.setImage(R.drawable.condition_2);
        conditionList.add(Condition2);

        Spinner Condition3 = new Spinner();
        Condition3.setName("Near Mint");
        Condition3.setImage(R.drawable.condition_3);
        conditionList.add(Condition3);

        Spinner Condition4 = new Spinner();
        Condition4.setName("Immaculate");
        Condition4.setImage(R.drawable.condition_4);
        conditionList.add(Condition4);

        return conditionList;
    }

    public static List<Spinner> getRegionList(){
        List<Spinner> regionList = new ArrayList<>();

        Spinner Region1 = new Spinner();
        Region1.setName("Pal A");
        Region1.setImage(R.drawable.pal_a);
        regionList.add(Region1);

        Spinner Region2 = new Spinner();
        Region2.setName("Pal A UK");
        Region2.setImage(R.drawable.uk);
        regionList.add(Region2);

        Spinner Region3 = new Spinner();
        Region3.setName("Pal B");
        Region3.setImage(R.drawable.euro);
        regionList.add(Region3);

        Spinner Region4 = new Spinner();
        Region4.setName("US");
        Region4.setImage(R.drawable.us);
        regionList.add(Region4);

        Spinner Region5 = new Spinner();
        Region5.setName("Australia");
        Region5.setImage(R.drawable.australia);
        regionList.add(Region5);

        Spinner Region6 = new Spinner();
        Region6.setName("Austria");
        Region6.setImage(R.drawable.austria);
        regionList.add(Region6);

        Spinner Region7 = new Spinner();
        Region7.setName("Benelux");
        Region7.setImage(R.drawable.benelux);
        regionList.add(Region7);

        Spinner Region8 = new Spinner();
        Region8.setName("Canada");
        Region8.setImage(R.drawable.denmark);
        regionList.add(Region8);

        //,Denmark,Finland,France,Germany
        //Greece,Ireland,Italy,Norway,Poland,Portugal
        //Spain,Sweden,Switzerland

        //getString(R.string.regionname01)
        return regionList;
    }
}
