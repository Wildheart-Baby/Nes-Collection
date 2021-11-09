package uk.co.bossdogsoftware.nes_collection.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import uk.co.bossdogsoftware.nes_collection.Fragments.GamesDetailFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.SpecificCountryFragment;
import uk.co.bossdogsoftware.nes_collection.Fragments.SpecificInformationFragment;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.R;

/**
 * Created by Wildheart on 31/07/2016.
 */
public class GamePagerAdapter extends FragmentStatePagerAdapter {

    Context context; //sets up a variable as context
    ArrayList<AllGameItems> gamesList; //sets up  an array called shoppingList
    String gameimage, publisher, synop, gen, subgen, pub, dev, gname, img, theyear, sql;
    int owned, carttrue, boxtrue, manualtrue, gameid, editgameid, pos, idforgame, favourite, coverid, titles;
    int flagAustralia, flagAustria, flagBenelux, flagDenmark, flagFinland, flagFrance, flagGermany, flagGreece, flagIreland, flagItaly, flagNorway, flagPoland, flagPortugal, flagScandinavia, flagSpain, flagSweden, flagSwitzerland, flagUK, flagUS ;

    AllGamesViewModel viewM;
    Menu menu;
    FragmentManager frg;

    AllGameItems gameListItems;

    /*public GamePagerAdapter(Context context, ArrayList<AllGameItems> list) {
        //super();
        this.context = context;//sets up the context for the class
        gamesList = list; //sets up a variable as a list
    }*/

    public GamePagerAdapter(FragmentManager fm, Context context, ArrayList<AllGameItems> list, int title) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;//sets up the context for the class
        gamesList = list; //sets up a variable as a list
        frg = fm;
        titles = title;
        //viewM = new ViewModelProvider(getApplicationContext()).get(AllGamesViewModel.class);
        //        //MenuItem fav = menu.findItem(R.id.action_favourite);
    }

    @Override
    public int getCount() {
        return gamesList.size();
    } //returns the number of items in the array

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        gameListItems = gamesList.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater
        View convertView = inflater.inflate(R.layout.fragment_games_detail, null); //use the layout to diaplay the array data

        //MenuItem own = menu.findItem(R.id.action_edit);
        ConstraintLayout Owned = convertView.findViewById(R.id.frmOwned);
        TextView gamename = convertView.findViewById(R.id.lblGameName);
        ImageView cover = convertView.findViewById(R.id.imgGameCover);
        final TextView genre = convertView.findViewById(R.id.lblGenre);
        final TextView subgenre = convertView.findViewById(R.id.lblSubgenre);
        final TextView publisher = convertView.findViewById(R.id.lblPublisher);
        final TextView developer = convertView.findViewById(R.id.lblDeveloper);
        final TextView year = convertView.findViewById(R.id.lblYear);
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

        synopsis.setText(gameListItems.getSynopsis());
        publisher.setText(gameListItems.getPublisher());
        genre.setText(gameListItems.getGenre());
        subgenre.setText(gameListItems.getSubgenre());
        gamename.setText(gameListItems.getName()); //sets the textview name with data from name
        developer.setText(gameListItems.getDeveloper());
        year.setText(gameListItems.getYear());
        //Log.d("Pixo", "Item id: " + idforgame);
        gameimage = gameListItems.getImage();
        owned = gameListItems.getOwned();
        carttrue = gameListItems.getCart();
        boxtrue = gameListItems.getBox();
        favourite = gameListItems.getFavourite();
        manualtrue = gameListItems.getManual();
        coverid=context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());
        cover.setImageResource(coverid);

        if (owned == 0){Owned.setVisibility(View.GONE);}

        flagAustralia = gameListItems.getAustralia();
        flagAustria = gameListItems.getAustria();
        flagBenelux = gameListItems.getBenelux();
        flagDenmark = gameListItems.getDenmark();
        flagFinland = gameListItems.getFinland();
        flagFrance = gameListItems.getFrance();
        flagGermany = gameListItems.getGermany();
        flagGreece = gameListItems.getGreece();
        flagIreland = gameListItems.getIreland();
        flagItaly = gameListItems.getItaly();
        flagNorway = gameListItems.getNorway();
        flagPoland = gameListItems.getPoland();
        flagPortugal = gameListItems.getPortugal();
        //flagScandinavia = nesListItems.getScandinavia();
        flagSweden = gameListItems.getSweden();
        flagSwitzerland = gameListItems.getSwitzerland();
        flagSpain = gameListItems.getSpain();
        flagUK = gameListItems.getUK();
        flagUS = gameListItems.getUS();

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
                openFragment("australia");
            }
        });

        austria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("austria");
            }
        });
        benelux.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("benelux");
            }
        });

        denmark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("denmark");
            }
        });

        finland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("finland");
            }
        });

        france.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("france");
            }
        });

        germany.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("germany");

            }
        });

        greece.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("greece");

            }
        });

        ireland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("ireland");
            }
        });

        italy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("italy");
            }
        });

        norway.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("norway");
            }
        });

        poland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("poland");
            }
        });

        portugal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("portugal");
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
                openFragment("spain");
            }
        });

        sweden.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("sweden");
            }
        });

        switzerland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("switzerland");
            }
        });

        us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("us");
            }
        });

        uk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment("uk");
            }
        });

        genre.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loadInformation("genre",  genre.getText().toString());
            }
        });

        subgenre.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loadInformation("subgenre",  subgenre.getText().toString());
            }
        });

        publisher.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loadInformation("publisher",  publisher.getText().toString());
            }
        });

        developer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loadInformation("developer",  developer.getText().toString());
            }
        });

        year.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loadInformation("year", year.getText().toString());
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

    public void openFragment(String countrySelection){
        Fragment f = frg.findFragmentByTag("gamesList");
        Fragment g = frg.findFragmentByTag("gamesDetail");
        frg.beginTransaction()
                .remove(f)
                .commit();
        frg.beginTransaction()
                .remove(g)
                .commit();
        frg.popBackStack("gamesDetail",frg.POP_BACK_STACK_INCLUSIVE);
        frg.popBackStack("gamesList",frg.POP_BACK_STACK_INCLUSIVE);
        frg.beginTransaction()
                .add(R.id.container, SpecificCountryFragment.newInstance("", countrySelection), "gamesList")
                .addToBackStack("gamesList")
                .commit();
        Log.d("pixo", "Backstack count:"+frg.getBackStackEntryCount());
    }

    public void loadInformation(String InformationType, String Query){
        Fragment f = frg.findFragmentByTag("gamesList");
        Fragment g = frg.findFragmentByTag("gamesDetail");
        frg.beginTransaction()
                .remove(f)
                .commit();
        frg.beginTransaction()
                .remove(g)
                .commit();
        frg.popBackStack("gamesDetail",frg.POP_BACK_STACK_INCLUSIVE);
        frg.popBackStack("gamesList",frg.POP_BACK_STACK_INCLUSIVE);
        frg.beginTransaction()
                .add(R.id.container, SpecificInformationFragment.newInstance(InformationType, Query), "gamesList")
                .addToBackStack("gamesList")
                .commit();
        Log.d("pixo", "Backstack count:"+frg.getBackStackEntryCount());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((ScrollView) object);

    }
    @Override
    public void setPrimaryItem(ViewGroup container,
                               int position,
                               Object object){
        gameListItems = gamesList.get(position);
        GamesDetailFragment.idforgame = (gameListItems.getItemId());
        GamesDetailFragment.favourited = (gameListItems.getFavourite());
        GamesDetailFragment.ownedgame = (gameListItems.getOwned());
        //GamesDetailFragment.gamesname = (nesListItems.getName());
        GamesDetailFragment.wishlist = (gameListItems.getWishlist());
        GamesDetailFragment.finished = (gameListItems.getFinished());
    }
}
