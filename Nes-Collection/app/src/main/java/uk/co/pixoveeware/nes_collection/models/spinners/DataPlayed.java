package uk.co.pixoveeware.nes_collection.models.spinners;

import java.util.ArrayList;
import java.util.List;

import uk.co.pixoveeware.nes_collection.R;

public class DataPlayed {

    public static List<PlaySpinner> getPlayedList(){
        List<PlaySpinner> playedList = new ArrayList<>();

        PlaySpinner Played1 = new PlaySpinner();
        Played1.setName("Tried it ");
        Played1.setImage(R.drawable.played_1);
        playedList.add(Played1);

        PlaySpinner Played2 = new PlaySpinner();
        Played2.setName("Played it");
        Played2.setImage(R.drawable.played_2);
        playedList.add(Played2);

        PlaySpinner Played3 = new PlaySpinner();
        Played3.setName("1/4 way");
        Played3.setImage(R.drawable.played_3);
        playedList.add(Played3);

        PlaySpinner Played4 = new PlaySpinner();
        Played4.setName("1/2 way");
        Played4.setImage(R.drawable.played_4);
        playedList.add(Played4);

        PlaySpinner Played5 = new PlaySpinner();
        Played5.setName("3/4 way");
        Played5.setImage(R.drawable.played_5);
        playedList.add(Played5);

        PlaySpinner Played6 = new PlaySpinner();
        Played6.setName("Beat it");
        Played6.setImage(R.drawable.played_6);
        playedList.add(Played6);

        PlaySpinner Played7 = new PlaySpinner();
        Played7.setName("100% done");
        Played7.setImage(R.drawable.played_7);
        playedList.add(Played7);

        return playedList;
    }
}
