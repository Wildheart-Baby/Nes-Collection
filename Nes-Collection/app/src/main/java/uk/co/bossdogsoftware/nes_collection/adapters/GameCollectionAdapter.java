package uk.co.bossdogsoftware.nes_collection.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.R;

/**
 * Created by Wildheart on 06/06/2016.
 */
public class GameCollectionAdapter extends BaseAdapter {
    public static int screenwidth;
    static class ViewHolder {
        TextView gamename;
        TextView publisher;
        ImageView cover;
        ImageView owned, genre, subgenre;
        TextView separator;
    }

    Context context; //sets up a variable as context
    ArrayList<AllGameItems> gamesList; //sets up  an array called shoppingList
    String gameimage;
    private static final int TYPE_GAME = 0;
    private static final int TYPE_DIVIDER = 1;

    int ownedgame, l;
    String test, thegamename;

    public GameCollectionAdapter(Context context, ArrayList<AllGameItems> list) {

        this.context = context;//sets up the context for the class
        gamesList = list; //sets up a variable as a list

    }

    @Override
    public int getCount() {
        return gamesList.size();
    } //returns the number of items in the array

    @Override
    public Object getItem(int position) {
        return gamesList.get(position);
    } //gets the position within the list

    @Override
    public long getItemId(int position) {
        return position;
    } //gets the id of the of the item within the list

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllGameItems nesListItems = gamesList.get(position); //gets the item position from the array

        if (convertView == null) { //if the layout isn't inflated
            LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater
            convertView = inflater.inflate(R.layout.single_item, null); //use the layout to diaplay the array data

            ViewHolder holder = new ViewHolder();
            holder.separator = convertView.findViewById(R.id.lblDivider);
            holder.owned = convertView.findViewById(R.id.imgOwned);
            holder.cover = convertView.findViewById(R.id.imgGameCover);
            holder.genre = convertView.findViewById(R.id.imgGenre);
            holder.subgenre = convertView.findViewById(R.id.imgSubgenre);
            holder.gamename = convertView.findViewById(R.id.lblGameName);
            holder.publisher = convertView.findViewById(R.id.lblPublisher);

            convertView.setTag(holder);
    }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (nesListItems.getGroup().equals("no")){ holder.separator.setVisibility(View.GONE); } else { holder.separator.setVisibility(View.VISIBLE); }
        holder.separator.setText("  "+nesListItems.getGroup());
        gameimage = nesListItems.getImage();
        int coverid=context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());
        int ownedid;
        holder.cover.setImageResource(coverid);

        holder.genre.setImageResource(context.getResources().getIdentifier(convertLogoTitle(nesListItems.getGenre()), "drawable", context.getPackageName()));
        holder.subgenre.setImageResource(context.getResources().getIdentifier(convertLogoTitle(nesListItems.getSubgenre()), "drawable", context.getPackageName()));

        thegamename = nesListItems.getName();
        l = thegamename.length();

        holder.gamename.setText(thegamename); //sets the textview name with data from name
        //holder.gamename.setText(nesListItems.getName()); //sets the textview name with data from name
        holder.publisher.setText(nesListItems.getPublisher());

        int partCount = nesListItems.getCart() + nesListItems.getBox() + nesListItems.getManual();

        switch (partCount){
            case 1:
                ownedid=context.getResources().getIdentifier("icon_owned_crown_bronze", "drawable", context.getPackageName());
                holder.owned.setImageResource(ownedid);
                break;
            case 2:
                ownedid=context.getResources().getIdentifier("icon_owned_crown_silver", "drawable", context.getPackageName());
                holder.owned.setImageResource(ownedid);
                break;
            case 3:
                ownedid=context.getResources().getIdentifier("icon_owned_crown_gold", "drawable", context.getPackageName());
                holder.owned.setImageResource(ownedid);
                break;
            default:
                break;
        }




        if (nesListItems.getOwned() == 1){ holder.owned.setVisibility(View.VISIBLE);} else { holder.owned.setVisibility(View.INVISIBLE);}
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#CAC9C5"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#C0C0BB"));
        }


        return convertView; //return the convertview and show the listview
    }

    public String convertLogoTitle(String title){
        title = title.toLowerCase();
        title = title.replaceAll("-","_");
        title = title.replaceAll(" ", "_");
        return title;
    }

}





