package uk.co.pixoveeware.nes_collection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wildheart on 28/07/2016.
 */
public class NesCollectionImageAdapter extends BaseAdapter {

    static class ViewHolder {
        ImageView cover;
        ImageView owned;
    }

    Context context; //sets up a variable as context
    ArrayList<NesItems> nesList; //sets up  an array called shoppingList
    String gameimage;
    int i, listsize, addviews = 0;

    public NesCollectionImageAdapter(Context context, ArrayList<NesItems> list) {

        this.context = context;//sets up the context for the class
        MainActivity.nesList = list; //sets up a variable as a list
    }

    @Override
    public int getCount() {
        listsize = MainActivity.nesList.size();
        if (listsize % 3 == 0){}
        else if (listsize + 1 % 3 == 0){addviews = 1;}
        else if (listsize + 2 % 3 == 0){addviews = 2;}
        i = 0;
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
        NesItems nesListItems = MainActivity.nesList.get(position); //gets the item position from the array
        ViewHolder holder;

        if (convertView == null) { //if the layout isn't inflated
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater

            convertView = inflater.inflate(R.layout.single_image_item, null); //use the layout to diaplay the array data

            holder = new ViewHolder();
            holder.owned = (ImageView) convertView.findViewById(R.id.imgOwned);
            holder.cover = (ImageView) convertView.findViewById(R.id.imgGameCover);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

            gameimage = nesListItems.getImage();
            int coverid = context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());
            holder.cover.setImageResource(coverid);
            int ownedid;

            if (nesListItems.owned == 1) {
                holder.owned.setVisibility(View.VISIBLE);
            } else {
                holder.owned.setVisibility(View.GONE);
            }
        if (nesListItems.cart == 1 && nesListItems.box == 1 && nesListItems.manual == 1){
            ownedid=context.getResources().getIdentifier("icon_owned_gold2", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 1 && nesListItems.box == 1 && nesListItems.manual == 0){
            ownedid=context.getResources().getIdentifier("icon_owned_silver2", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 1 && nesListItems.box == 0 && nesListItems.manual == 1){
            ownedid=context.getResources().getIdentifier("icon_owned_silver2", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 0 && nesListItems.box == 1 && nesListItems.manual == 1){
            ownedid=context.getResources().getIdentifier("icon_owned_silver2", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 1 && nesListItems.box == 0 && nesListItems.manual == 0){
            ownedid=context.getResources().getIdentifier("icon_owned_bronze2", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 0 && nesListItems.box == 1 && nesListItems.manual == 0){
            ownedid=context.getResources().getIdentifier("icon_owned_bronze2", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }else if (nesListItems.cart == 0 && nesListItems.box == 0 && nesListItems.manual == 1){
            ownedid=context.getResources().getIdentifier("icon_owned_bronze2", "drawable", context.getPackageName());
            holder.owned.setImageResource(ownedid);
        }
        i++;
        return convertView; //return the convertview and show the listview
        //if(i >= listsize &&  addviews < 3){return convertView;}
    }

}