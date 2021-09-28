package uk.co.pixoveeware.nes_collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Wildheart on 06/06/2016.
 */
public class NesCollectionAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView gamename;
        TextView publisher;
        ImageView cover;
        ImageView owned;
    }


    //private Typeface tf; //sets up the typeface into variable called tf

    Context context; //sets up a variable as context
    ArrayList<NesItems> nesList; //sets up  an array called shoppingList
    String gameimage;
    int ownedgame;

    public NesCollectionAdapter(Context context, ArrayList<NesItems> list) {

        this.context = context;//sets up the context for the class
        nesList = list; //sets up a variable as a list

    }


    @Override
    public int getCount() {
        return nesList.size();
    } //returns the number of items in the array

    @Override
    public Object getItem(int position) {
        return nesList.get(position);
    } //gets the position within the list

    @Override
    public long getItemId(int position) {
        return position;
    } //gets the id of the of the item within the list

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NesItems nesListItems = nesList.get(position); //gets the item position from the array

        if (convertView == null) { //if the layout isn't inflated
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater

            convertView = inflater.inflate(R.layout.single_item, null); //use the layout to diaplay the array data


        ViewHolder holder = new ViewHolder();
        holder.owned = (ImageView) convertView.findViewById(R.id.imgOwned);
        holder.cover = (ImageView) convertView.findViewById(R.id.imgGameCover);
        holder.gamename = (TextView) convertView.findViewById(R.id.lblGameName);
        holder.publisher = (TextView) convertView.findViewById(R.id.lblPublisher);
            convertView.setTag(holder);
    }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        gameimage = nesListItems.getImage();
        int coverid=context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());


        holder.cover.setImageResource(coverid);
        holder.gamename.setText(nesListItems.getName()); //sets the textview name with data from name
        holder.publisher.setText(nesListItems.getPublisher());
        if (nesListItems.owned == 1){ holder.owned.setVisibility(View.VISIBLE);} else { holder.owned.setVisibility(View.INVISIBLE);}
        return convertView; //return the convertview and show the listview
    }




}





