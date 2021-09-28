package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Wildheart on 31/07/2016.
 */
public class NesPagerAdapter extends PagerAdapter {

    public static String licensed;

    Context context; //sets up a variable as context
    ArrayList<NesItems> nesList; //sets up  an array called shoppingList
    String gameimage, synop, gen, subgen, pub, dev, gname, img, theyear, sql;
    int owned, carttrue, boxtrue, manualtrue, gameid, editgameid, pos, idforgame, favourite;
    int flagAustralia, flagAustria, flagBenelux, flagFrance, flagGermany, flagIreland, flagItaly, flagScandinavia, flagSpain, flagSwitzerland, flagUK, flagUS ;

    Menu menu;

    NesItems nesListItems;

    public NesPagerAdapter(Context context, ArrayList<NesItems> list) {
        this.context = context;//sets up the context for the class
        nesList = list; //sets up a variable as a list
    }

    @Override
    public int getCount() {
        return nesList.size();
    } //returns the number of items in the array

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ScrollView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        nesListItems = nesList.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater
        View convertView = inflater.inflate(R.layout.content_games_detail, null); //use the layout to diaplay the array data

        //MenuItem fav = menu.findItem(R.id.action_favourite);
        //MenuItem own = menu.findItem(R.id.action_edit);

        TextView gamename = (TextView) convertView.findViewById(R.id.lblGameName);
        ImageView cover = (ImageView) convertView.findViewById(R.id.imgGameCover);
        TextView genre = (TextView) convertView.findViewById(R.id.lblGenre);
        TextView subgenre = (TextView) convertView.findViewById(R.id.lblSubgenre);
        TextView publisher = (TextView) convertView.findViewById(R.id.lblPublisher);
        TextView developer = (TextView) convertView.findViewById(R.id.lblDeveloper);
        TextView year = (TextView) convertView.findViewById(R.id.lblYear);
        ImageView cart = (ImageView) convertView.findViewById(R.id.imgCart);
        ImageView box = (ImageView) convertView.findViewById(R.id.imgBox);
        ImageView manual = (ImageView) convertView.findViewById(R.id.imgManual);
        TextView synopsis = (TextView) convertView.findViewById(R.id.lblSynopsis);

        ImageView australia = (ImageView) convertView.findViewById(R.id.imgAustralia);
        ImageView austria = (ImageView) convertView.findViewById(R.id.imgAustria);
        ImageView benelux = (ImageView) convertView.findViewById(R.id.imgBenelux);
        ImageView france = (ImageView) convertView.findViewById(R.id.imgFrance);
        ImageView germany = (ImageView) convertView.findViewById(R.id.imgGermany);
        ImageView ireland = (ImageView) convertView.findViewById(R.id.imgIreland);
        ImageView italy = (ImageView) convertView.findViewById(R.id.imgItaly);
        ImageView scandinavia = (ImageView) convertView.findViewById(R.id.imgScandinavia);
        ImageView spain = (ImageView) convertView.findViewById(R.id.imgScandinavia);
        ImageView switzerland = (ImageView) convertView.findViewById(R.id.imgSwitzerland);
        ImageView uk = (ImageView) convertView.findViewById(R.id.imgUK);
        ImageView us = (ImageView) convertView.findViewById(R.id.imgUS);

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
        int coverid=context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());
        cover.setImageResource(coverid);

        flagAustralia = nesListItems.getAustralia();
        flagAustria = nesListItems.getAustria();
        flagBenelux = nesListItems.getBenelux();
        flagFrance = nesListItems.getFrance();
        flagGermany = nesListItems.getGermany();
        flagIreland = nesListItems.getIreland();
        flagItaly = nesListItems.getItaly();
        flagScandinavia = nesListItems.getScandinavia();
        flagSwitzerland = nesListItems.getSwitzerland();
        flagSpain = nesListItems.getSpain();
        flagUK = nesListItems.getUK();
        flagUS = nesListItems.getUS();

        Log.d("Pixo-flag", "German: "+flagGermany);
        Log.d("Pixo-flag", "UK: "+flagUK);
        Log.d("Pixo-flag", "US: "+flagUS);

        if (flagAustralia == 1){australia.setVisibility(View.VISIBLE); Log.d("Pixo-flag", "setting Autralian flag");} else{}
        if (flagFrance == 1){france.setVisibility(View.VISIBLE); Log.d("Pixo-flag", "setting French flag");} else {}//australia france
        if (flagGermany == 1){germany.setVisibility(View.VISIBLE); Log.d("Pixo-flag", "setting German flag");}else {}
        if (flagUS == 1){us.setVisibility(View.VISIBLE); Log.d("Pixo-flag", "setting US flag");} else{}
        if (flagUK == 1){uk.setVisibility(View.VISIBLE); Log.d("Pixo-flag", "setting UK flag");} else {}

        australia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_australia = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Australian games");
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
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        uk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_uk = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "UK games");
                v.getContext().startActivity(intent);//start the new screen

            }
        });

        if(owned == 1){
            if(carttrue == 1){
                cart.setVisibility(View.VISIBLE);
            }else{}
            if(boxtrue == 1){
                box.setVisibility(View.VISIBLE);
            }else{}
            if(manualtrue == 1){
                manual.setVisibility(View.VISIBLE);
            } else{}
        }

        ((ViewPager) container).addView(convertView);

        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((ScrollView) object);

    }
    @Override
    public void setPrimaryItem(ViewGroup container,
                               int position,
                               Object object){
        nesListItems = nesList.get(position);
        GamesDetail.idforgame = (nesListItems.getItemId());
        GamesDetail.favourited = (nesListItems.getFavourite());
        GamesDetail.ownedgame = (nesListItems.getOwned());
        GamesDetail.gamesname = (nesListItems.getName());
        GamesDetail.wishlist = (nesListItems.getWishlist());
        GamesDetail.finished = (nesListItems.getFinished());
    }



}
