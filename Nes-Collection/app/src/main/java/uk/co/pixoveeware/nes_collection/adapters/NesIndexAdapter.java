package uk.co.pixoveeware.nes_collection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.MainActivity;
import uk.co.pixoveeware.nes_collection.R;

/**
 * Created by Wildheart on 11/10/2018.
 */

public class NesIndexAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView alphabet;
    }

    Context context;
    //ArrayList<SnesItemsIndex> indexList;

    public NesIndexAdapter(Context context, ArrayList<GameItemsIndex> list) {
        this.context = context;//sets up the context for the class
        MainActivity.indexList = list; //sets up a variable as a list
    }

    @Override
    public int getCount() {
        return MainActivity.indexList.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.indexList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameItemsIndex indexListItems = MainActivity.indexList.get(position); //gets the item position from the array

        if (convertView == null) { //if the layout isn't inflated
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //sets up the layout inflater
            convertView = inflater.inflate(R.layout.side_index_item, null); //use the layout to diaplay the array data

            ViewHolder holder = new ViewHolder();


            holder.alphabet = (TextView) convertView.findViewById(R.id.side_list_item);

            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.alphabet.setText(indexListItems.getLetter());
        return convertView; //return the convertview and show the listview
    }
}
