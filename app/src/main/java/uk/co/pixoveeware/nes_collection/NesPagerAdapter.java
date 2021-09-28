package uk.co.pixoveeware.nes_collection;

import android.content.Context;
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

    Context context; //sets up a variable as context
    ArrayList<NesItems> nesList; //sets up  an array called shoppingList
    String gameimage, synop, gen, subgen, pub, dev, gname, img, theyear;
    int owned, carttrue, boxtrue, manualtrue, gameid, editgameid, pos, idforgame, favourite;

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

        //if(favourite == 1){ fav.setIcon(R.drawable.ic_heart_red_24dp); }
        //if(owned == 0){own.setIcon(R.drawable.ic_add_game24dp);} else if (owned == 1){own.setIcon(R.drawable.ic_edit_white_24dp);}

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
