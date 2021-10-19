package uk.co.pixoveeware.nes_collection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.activities.HomeScreenActivity;
import uk.co.pixoveeware.nes_collection.models.AllGameItems;
import uk.co.pixoveeware.nes_collection.R;

/**
 * Created by Wildheart on 28/07/2016.
 */
public class NesCollectionImageAdapter extends BaseAdapter {

    static class ViewHolder {
        ImageView cover;
        ImageView owned;
    }

    Context context; //sets up a variable as context
    ArrayList<AllGameItems> nesList; //sets up  an array called shoppingList
    String gameimage;
    int i, listsize, addviews = 0;

    public NesCollectionImageAdapter(Context context, ArrayList<AllGameItems> list) {

        this.context = context;//sets up the context for the class
        nesList = list; //sets up a variable as a list
    }

    @Override
    public int getCount() {
        listsize = HomeScreenActivity.gamesList.size();
        if (listsize % 3 == 0){}
        else if (listsize + 1 % 3 == 0){addviews = 1;}
        else if (listsize + 2 % 3 == 0){addviews = 2;}
        i = 0;
        return HomeScreenActivity.gamesList.size();
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
        AllGameItems nesListItems = nesList.get(position); //gets the item position from the array
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
            int totalItems = nesListItems.cart = nesListItems.box + nesListItems.manual;

            switch (totalItems) {
                case 1:
                    ownedid=context.getResources().getIdentifier("icon_owned_gold2", "drawable", context.getPackageName());
                    holder.owned.setImageResource(ownedid);
                    break;
                case 2:
                    ownedid=context.getResources().getIdentifier("icon_owned_silver2", "drawable", context.getPackageName());
                    holder.owned.setImageResource(ownedid);
                    break;
                case 3:
                    ownedid=context.getResources().getIdentifier("icon_owned_bronze2", "drawable", context.getPackageName());
                    holder.owned.setImageResource(ownedid);
                    break;
            }


            /*if (nesListItems.cart == 1 && nesListItems.box == 1 && nesListItems.manual == 1){
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
        }*/
        i++;
        return convertView; //return the convertview and show the listview
        //if(i >= listsize &&  addviews < 3){return convertView;}
    }

}