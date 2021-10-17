package uk.co.pixoveeware.nes_collection.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.Fragments.EditGameFragment;
import uk.co.pixoveeware.nes_collection.Fragments.GamesDetailFragment;
import uk.co.pixoveeware.nes_collection.Fragments.SpecificCountryFragment;
import uk.co.pixoveeware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.pixoveeware.nes_collection.activities.HomeScreenActivity;
import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.activities.GamesDetail;
import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.activities.StatsSearchResults;

/**
 * Created by Wildheart on 31/07/2016.
 */
public class NesPagerAdapter extends FragmentStatePagerAdapter {

    public static String licensed;

    Context context; //sets up a variable as context
    ArrayList<GameItems> gamesList; //sets up  an array called shoppingList
    String gameimage, synop, gen, subgen, pub, dev, gname, img, theyear, sql;
    int owned, carttrue, boxtrue, manualtrue, gameid, editgameid, pos, idforgame, favourite, coverid;
    int flagAustralia, flagAustria, flagBenelux, flagDenmark, flagFinland, flagFrance, flagGermany, flagGreece, flagIreland, flagItaly, flagNorway, flagPoland, flagPortugal, flagScandinavia, flagSpain, flagSweden, flagSwitzerland, flagUK, flagUS ;

    AllGamesViewModel viewM;
    Menu menu;
    FragmentManager frg;

    GameItems nesListItems;

    /*public NesPagerAdapter(Context context, ArrayList<GameItems> list) {
        //super();
        this.context = context;//sets up the context for the class
        gamesList = list; //sets up a variable as a list
    }*/

    public NesPagerAdapter(FragmentManager fm, Context context, ArrayList<GameItems> list) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;//sets up the context for the class
        gamesList = list; //sets up a variable as a list
        frg = fm;
    }

    @Override
    public int getCount() {
        return gamesList.size();
    } //returns the number of items in the array

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ScrollView) object);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        nesListItems = gamesList.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater
        View convertView = inflater.inflate(R.layout.content_games_detail, null); //use the layout to diaplay the array data

        //MenuItem fav = menu.findItem(R.id.action_favourite);
        //MenuItem own = menu.findItem(R.id.action_edit);
        FrameLayout Owned = convertView.findViewById(R.id.frmOwned);
        TextView gamename = convertView.findViewById(R.id.lblGameName);
        ImageView cover = convertView.findViewById(R.id.imgGameCover);
        TextView genre = convertView.findViewById(R.id.lblGenre);
        TextView subgenre = convertView.findViewById(R.id.lblSubgenre);
        TextView publisher = convertView.findViewById(R.id.lblPublisher);
        TextView developer = convertView.findViewById(R.id.lblDeveloper);
        TextView year = convertView.findViewById(R.id.lblYear);
        ImageView cart = convertView.findViewById(R.id.imgCart);
        ImageView box = convertView.findViewById(R.id.imgBox);
        ImageView manual = convertView.findViewById(R.id.imgManual);
        TextView synopsis = convertView.findViewById(R.id.lblSynopsis);

        ImageView australia = convertView.findViewById(R.id.imgAustralia);
        ImageView austria = convertView.findViewById(R.id.imgAustria);
        ImageView benelux = convertView.findViewById(R.id.imgBenelux);
        ImageView denmark = convertView.findViewById(R.id.imgDenmark);
        ImageView france = convertView.findViewById(R.id.imgFrance);
        ImageView finland = convertView.findViewById(R.id.imgFinland);
        ImageView germany = convertView.findViewById(R.id.imgGermany);
        ImageView greece = convertView.findViewById(R.id.imgGreece);
        ImageView ireland = convertView.findViewById(R.id.imgIreland);
        ImageView italy = convertView.findViewById(R.id.imgItaly);
        ImageView norway = convertView.findViewById(R.id.imgNorway);
        ImageView poland = convertView.findViewById(R.id.imgPoland);
        ImageView portugal = convertView.findViewById(R.id.imgPortugal);
        //ImageView scandinavia = (ImageView) convertView.findViewById(R.id.imgScandinavia);
        ImageView spain = convertView.findViewById(R.id.imgSpain);
        ImageView sweden = convertView.findViewById(R.id.imgSweden);
        ImageView switzerland = convertView.findViewById(R.id.imgSwitzerland);
        ImageView uk = convertView.findViewById(R.id.imgUK);
        ImageView us = convertView.findViewById(R.id.imgUS);

        synopsis.setText(nesListItems.getSynopsis());
        publisher.setText(nesListItems.getPublisher());
        genre.setText(nesListItems.getGenre());
        subgenre.setText(nesListItems.getSubgenre());
        gamename.setText(nesListItems.getName()); //sets the textview name with data from name
        developer.setText(nesListItems.getDeveloper());
        year.setText(nesListItems.getYear());
        //Log.d("Pixo", "Item id: " + idforgame);
        gameimage = nesListItems.getImage();
        owned = nesListItems.getOwned();
        carttrue = nesListItems.getCart();
        boxtrue = nesListItems.getBox();
        favourite = nesListItems.getFavourite();
        manualtrue = nesListItems.getManual();
        coverid=context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());
        cover.setImageResource(coverid);

        if (owned == 0){Owned.setVisibility(View.GONE);}

        flagAustralia = nesListItems.getAustralia();
        flagAustria = nesListItems.getAustria();
        flagBenelux = nesListItems.getBenelux();
        flagDenmark = nesListItems.getDenmark();
        flagFinland = nesListItems.getFinland();
        flagFrance = nesListItems.getFrance();
        flagGermany = nesListItems.getGermany();
        flagGreece = nesListItems.getGreece();
        flagIreland = nesListItems.getIreland();
        flagItaly = nesListItems.getItaly();
        flagNorway = nesListItems.getNorway();
        flagPoland = nesListItems.getPoland();
        flagPortugal = nesListItems.getPortugal();
        //flagScandinavia = nesListItems.getScandinavia();
        flagSweden = nesListItems.getSweden();
        flagSwitzerland = nesListItems.getSwitzerland();
        flagSpain = nesListItems.getSpain();
        flagUK = nesListItems.getUK();
        flagUS = nesListItems.getUS();

        /*Log.d("Pixo-flag", "German: "+flagGermany);
        Log.d("Pixo-flag", "UK: "+flagUK);
        Log.d("Pixo-flag", "US: "+flagUS);*/

        if (flagAustralia == 1){australia.setVisibility(View.VISIBLE); }
        if (flagAustria == 1){austria.setVisibility(View.VISIBLE); }
        if (flagBenelux == 1){benelux.setVisibility(View.VISIBLE); }
        if (flagDenmark == 1){denmark.setVisibility(View.VISIBLE); }
        if (flagFrance == 1){france.setVisibility(View.VISIBLE); }
        if (flagGermany == 1){germany.setVisibility(View.VISIBLE); }
        if (flagGreece == 1){greece.setVisibility(View.VISIBLE); }
        if (flagIreland == 1){ireland.setVisibility(View.VISIBLE); }
        if (flagItaly == 1){italy.setVisibility(View.VISIBLE); }
        if (flagPoland == 1){poland.setVisibility(View.VISIBLE); }
        if (flagPortugal == 1){portugal.setVisibility(View.VISIBLE); }
        //if (flagScandinavia == 1){scandinavia.setVisibility(View.VISIBLE); } else{}
        if (flagSpain == 1){spain.setVisibility(View.VISIBLE); }
        if (flagSwitzerland == 1){switzerland.setVisibility(View.VISIBLE); }
        if (flagUS == 1){us.setVisibility(View.VISIBLE); }
        if (flagUK == 1){uk.setVisibility(View.VISIBLE); }

        australia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                            .remove(f)
                            .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "australia"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        austria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "austria"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });
        benelux.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "benelux"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();

            }
        });

        denmark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "denmark"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        finland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "finland"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();

            }
        });

        france.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "france"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();

            }
        });

        germany.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "germany"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();

            }
        });

        greece.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "greece"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();

            }
        });

        ireland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "ireland"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        italy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "italy"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        norway.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "norway"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        poland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "poland"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        portugal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "portugal"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        /*scandinavia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .replace(R.id.container, SpecificCountryFragment.newInstance("", "scandinavia"), "gamesList")
                        .addToBackStack(null)
                        .commit();
            }
        });*/

        spain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "spain"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        sweden.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "sweden"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        switzerland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "switzerland"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "us"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });

        uk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SpecificCountryFragment.newInstance("", "uk"), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
            }
        });



        if(owned == 1){
            if(carttrue == 1){
                cart.setVisibility(View.VISIBLE);
            }
            if(boxtrue == 1){
                box.setVisibility(View.VISIBLE);
            }
            if(manualtrue == 1){
                manual.setVisibility(View.VISIBLE);
            }
        }

        container.addView(convertView);

        return convertView;
    }

    /*private void changeFragment(){
        //Fragment frg = context.g
        getParentFragmentManager().beginTransaction()
                .add(R.id.container, GamesDetailFragment.newInstance(0, arg2), "gamesDetail")
                .addToBackStack(null)
                .commit();
    }*/

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((ScrollView) object);

    }
    @Override
    public void setPrimaryItem(ViewGroup container,
                               int position,
                               Object object){
        nesListItems = gamesList.get(position);
        GamesDetail.idforgame = (nesListItems.getItemId());
        GamesDetail.favourited = (nesListItems.getFavourite());
        GamesDetail.ownedgame = (nesListItems.getOwned());
        GamesDetail.gamesname = (nesListItems.getName());
        GamesDetail.wishlist = (nesListItems.getWishlist());
        GamesDetail.finished = (nesListItems.getFinished());
    }

}
