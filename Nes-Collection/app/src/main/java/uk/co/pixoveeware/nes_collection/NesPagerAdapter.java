package uk.co.pixoveeware.nes_collection;

import android.app.Activity;
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
import android.widget.FrameLayout;
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
    int owned, carttrue, boxtrue, manualtrue, gameid, editgameid, pos, idforgame, favourite, coverid;
    int flagAustralia, flagAustria, flagBenelux, flagDenmark, flagFinland, flagFrance, flagGermany, flagGreece, flagIreland, flagItaly, flagNorway, flagPoland, flagPortugal, flagScandinavia, flagSpain, flagSweden, flagSwitzerland, flagUK, flagUS ;

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
        FrameLayout Owned = (FrameLayout) convertView.findViewById(R.id.frmOwned);
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
        ImageView denmark = (ImageView) convertView.findViewById(R.id.imgDenmark);
        ImageView france = (ImageView) convertView.findViewById(R.id.imgFrance);
        ImageView finland = (ImageView) convertView.findViewById(R.id.imgFinland);
        ImageView germany = (ImageView) convertView.findViewById(R.id.imgGermany);
        ImageView greece = (ImageView) convertView.findViewById(R.id.imgGreece);
        ImageView ireland = (ImageView) convertView.findViewById(R.id.imgIreland);
        ImageView italy = (ImageView) convertView.findViewById(R.id.imgItaly);
        ImageView norway = (ImageView) convertView.findViewById(R.id.imgNorway);
        ImageView poland = (ImageView) convertView.findViewById(R.id.imgPoland);
        ImageView portugal = (ImageView) convertView.findViewById(R.id.imgPortugal);
        //ImageView scandinavia = (ImageView) convertView.findViewById(R.id.imgScandinavia);
        ImageView spain = (ImageView) convertView.findViewById(R.id.imgSpain);
        ImageView sweden = (ImageView) convertView.findViewById(R.id.imgSweden);
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

        if (flagAustralia == 1){australia.setVisibility(View.VISIBLE); } else{}
        if (flagAustria == 1){austria.setVisibility(View.VISIBLE); } else{}
        if (flagBenelux == 1){benelux.setVisibility(View.VISIBLE); } else{}
        if (flagDenmark == 1){denmark.setVisibility(View.VISIBLE); } else{}
        if (flagFrance == 1){france.setVisibility(View.VISIBLE); } else {}//australia france
        if (flagGermany == 1){germany.setVisibility(View.VISIBLE); }else {}
        if (flagGreece == 1){greece.setVisibility(View.VISIBLE); } else{}
        if (flagIreland == 1){ireland.setVisibility(View.VISIBLE); } else{}
        if (flagItaly == 1){italy.setVisibility(View.VISIBLE); } else{}
        if (flagPoland == 1){poland.setVisibility(View.VISIBLE); } else{}
        if (flagPortugal == 1){portugal.setVisibility(View.VISIBLE); } else{}
        //if (flagScandinavia == 1){scandinavia.setVisibility(View.VISIBLE); } else{}
        if (flagSpain == 1){spain.setVisibility(View.VISIBLE); } else{}
        if (flagSwitzerland == 1){switzerland.setVisibility(View.VISIBLE); } else{}
        if (flagUS == 1){us.setVisibility(View.VISIBLE); } else{}
        if (flagUK == 1){uk.setVisibility(View.VISIBLE); } else {}

        australia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sql = "SELECT * FROM eu where flag_australia = 1" + licensed + "";
                //Log.d("Pixo", sql);
                Intent intent = new Intent(v.getContext(), StatsSearchResults.class);//opens a new screen when the shopping list is clicked
                intent.putExtra("sqlstatement", sql);
                intent.putExtra("pagetitle", "Australian games");
                intent.putExtra("showsub", 1);
                intent.putExtra("flag", "australia");
                v.getContext().startActivity(intent);//start the new screen

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
