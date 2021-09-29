package uk.co.pixoveeware.nes_collection.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.GameItems;
import uk.co.pixoveeware.nes_collection.MainActivity;
import uk.co.pixoveeware.nes_collection.R;

/**
 * Created by Wildheart on 06/06/2016.
 */
public class NesCollectionAdapter extends BaseAdapter {
    public static int screenwidth;
    static class ViewHolder {
        TextView gamename;
        TextView publisher;
        ImageView cover;
        ImageView owned;
        TextView separator;
    }

    Context context; //sets up a variable as context
    //ArrayList<GameItems> nesList; //sets up  an array called shoppingList
    String gameimage;
    private static final int TYPE_GAME = 0;
    private static final int TYPE_DIVIDER = 1;

    int ownedgame, l;
    String test, thegamename;

    public NesCollectionAdapter(Context context, ArrayList<GameItems> list) {

        this.context = context;//sets up the context for the class
        MainActivity.nesList = list; //sets up a variable as a list

    }

    @Override
    public int getCount() {
        return MainActivity.nesList.size();
    } //returns the number of items in the array

    @Override
    public Object getItem(int position) {
        return MainActivity.nesList.get(position);
    } //gets the position within the list

    @Override
    public long getItemId(int position) {
        return position;
    } //gets the id of the of the item within the list

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameItems nesListItems = MainActivity.nesList.get(position); //gets the item position from the array

        if (convertView == null) { //if the layout isn't inflated
            LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater
            convertView = inflater.inflate(R.layout.single_item, null); //use the layout to diaplay the array data

            ViewHolder holder = new ViewHolder();
            holder.separator = (TextView) convertView.findViewById(R.id.lblDivider);
            holder.owned = (ImageView) convertView.findViewById(R.id.imgOwned);
            holder.cover = (ImageView) convertView.findViewById(R.id.imgGameCover);
            holder.gamename = (TextView) convertView.findViewById(R.id.lblGameName);
            holder.publisher = (TextView) convertView.findViewById(R.id.lblPublisher);

            convertView.setTag(holder);
    }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (nesListItems.group.equals("no")){ holder.separator.setVisibility(View.GONE); } else { holder.separator.setVisibility(View.VISIBLE); }
        holder.separator.setText(nesListItems.getGroup());
        gameimage = nesListItems.getImage();
        int coverid=context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());
        int ownedid;
        holder.cover.setImageResource(coverid);

        thegamename = nesListItems.getName();
        l = thegamename.length();
        if (MainActivity.width < 600){if (l >30) {thegamename = thegamename.substring(0,27) + "...";}}
        holder.gamename.setText(thegamename); //sets the textview name with data from name
        //holder.gamename.setText(nesListItems.getName()); //sets the textview name with data from name
        holder.publisher.setText(nesListItems.getPublisher());


        if (nesListItems.cart == 1 && nesListItems.box == 1 && nesListItems.manual == 1){
            ownedid=context.getResources().getIdentifier("icon_owned_gold", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 1 && nesListItems.box == 1 && nesListItems.manual == 0){
            ownedid=context.getResources().getIdentifier("icon_owned_silver", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 1 && nesListItems.box == 0 && nesListItems.manual == 1){
        ownedid=context.getResources().getIdentifier("icon_owned_silver", "drawable", context.getPackageName());
        holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 0 && nesListItems.box == 1 && nesListItems.manual == 1){
            ownedid=context.getResources().getIdentifier("icon_owned_silver", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 1 && nesListItems.box == 0 && nesListItems.manual == 0){
        ownedid=context.getResources().getIdentifier("icon_owned_bronze", "drawable", context.getPackageName());
        holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 0 && nesListItems.box == 1 && nesListItems.manual == 0){
            ownedid=context.getResources().getIdentifier("icon_owned_bronze", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 0 && nesListItems.box == 0 && nesListItems.manual == 1){
            ownedid=context.getResources().getIdentifier("icon_owned_bronze", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }
        if (nesListItems.owned == 1){ holder.owned.setVisibility(View.VISIBLE);} else { holder.owned.setVisibility(View.INVISIBLE);}
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#CAC9C5"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#C0C0BB"));
        }


        return convertView; //return the convertview and show the listview
    }

}





