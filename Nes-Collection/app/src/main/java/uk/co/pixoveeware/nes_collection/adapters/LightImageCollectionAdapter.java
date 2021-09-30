package uk.co.pixoveeware.nes_collection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

public class LightImageCollectionAdapter extends BaseAdapter {

    static class ViewHolder {
        ImageView cover;
        ImageView owned;
    }

    Context context; //sets up a variable as context
    ArrayList<GameListItems> nesList; //sets up  an array called shoppingList
    String gameimage;
    int i, listsize, addviews = 0;

    public LightImageCollectionAdapter(Context context, ArrayList<GameListItems> list){
        this.context = context;//sets up the context for the class
        nesList = list; //sets up a variable as a list
    }

    @Override
    public int getCount() {
        listsize = nesList.size();
        if (listsize % 3 == 0){}
        else if (listsize + 1 % 3 == 0){addviews = 1;}
        else if (listsize + 2 % 3 == 0){addviews = 2;}
        i = 0;
        return nesList.size();
    }

    @Override
    public Object getItem(int position) {
        return nesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameListItems nesListItems = nesList.get(position); //gets the item position from the array
        NesCollectionImageAdapter.ViewHolder holder;

        if (convertView == null) { //if the layout isn't inflated
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater

            convertView = inflater.inflate(R.layout.single_image_item, null); //use the layout to diaplay the array data

            holder = new NesCollectionImageAdapter.ViewHolder();
            holder.owned = (ImageView) convertView.findViewById(R.id.imgOwned);
            holder.cover = (ImageView) convertView.findViewById(R.id.imgGameCover);

            convertView.setTag(holder);
        } else {
            holder = (NesCollectionImageAdapter.ViewHolder) convertView.getTag();
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
