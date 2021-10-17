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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.Fragments.GamesDetailFragment;
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

    Menu menu;

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
                
            }
        });

        austria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_austria = 1" + licensed + "";
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Austrian games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "austria");
                v.getContext().startActivity(intent);//start the new screen

            }
        });
        benelux.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_benelux = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Benelux games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "benelux");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        denmark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_denmark = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Danish games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "denmark");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        finland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_finland = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Finnish games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "finland");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        france.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_france = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "French games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "france");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        germany.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_germany = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "German games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "germany");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        greece.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_greece = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Greek games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "greece");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        ireland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_ireland = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Irish games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "ireland");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        italy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_italy = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Italian games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "italy");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        norway.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_norway = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Norwegian games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "norway");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        poland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_poland = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Polish games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "poland");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        portugal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_portugal = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Portuguese games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "portugal");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        /*scandinavia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_scandinavian = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Scandinavian games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "scandinavia");
                v.getContext().startActivity(intent);//start the new screen
            }
        });*/

        spain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_spain = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Spanish games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "spain");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        sweden.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_sweden = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Swedish games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "sweden");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        switzerland.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_switzerland = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Swiss games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "switzerland");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_us = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "US games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "us");
                GamesDetail.listcount ++;
                GamesDetail.gamedetailcount ++;
                v.getContext().startActivity(intent);//start the new screen
                if (GamesDetail.gamedetailcount > 0){((Activity) context).finish();}
            }
        });

        uk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_uk = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "UK games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "uk");
                v.getContext().startActivity(intent);//start the new screen

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
