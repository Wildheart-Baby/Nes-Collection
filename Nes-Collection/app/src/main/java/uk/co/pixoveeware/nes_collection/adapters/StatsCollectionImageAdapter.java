package uk.co.pixoveeware.nes_collection.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.activities.HomeScreenActivity;
import uk.co.pixoveeware.nes_collection.models.AllGameItems;
import uk.co.pixoveeware.nes_collection.R;

/**
 * Created by Wildheart on 30/08/2016.
 */
public class StatsCollectionImageAdapter extends BaseAdapter {

    static class ViewHolder {
        ImageView cover;
        ImageView owned;
    }

    Context context; //sets up a variable as context
    ArrayList<AllGameItems> nesList; //sets up  an array called shoppingList
    String gameimage;
    int i, listsize, addviews = 0;

    public StatsCollectionImageAdapter(Context context, ArrayList<AllGameItems> list) {

        this.context = context;//sets up the context for the class
        HomeScreenActivity.gamesList = list; //sets up a variable as a list
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
        return HomeScreenActivity.gamesList.get(position);
    } //gets the position within the list

    @Override
    public long getItemId(int position) {
        return position;
    } //gets the id of the of the item within the list

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllGameItems nesListItems = HomeScreenActivity.gamesList.get(position); //gets the item position from the array
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
        Bitmap image =((BitmapDrawable)holder.cover.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,50/100,byteArrayOutputStream);

        i++;
        return convertView; //return the convertview and show the listview
        //if(i >= listsize &&  addviews < 3){return convertView;}
    }

}
