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
 * Created by Wildheart on 11/06/2016.
 */
public class NesOwnedAdapter  extends BaseAdapter {

        static class ViewHolder {
            TextView gamename, ownedcart, ownedbox,ownedmanual;
            ImageView cover;
            ImageView owned;
        }


        //private Typeface tf; //sets up the typeface into variable called tf

        Context context; //sets up a variable as context
        ArrayList<NesItems> nesList; //sets up  an array called shoppingList
        String gameimage, cart, box, manual;
        String  palAcart, palBcart, ntscart, palAbox, palBbox, ntscbox, palAmanual, palBmanual, ntscmanual;
        int palAcartlist, palBcartlist, uscartlist, palAboxlist, palBboxlist, usboxlist, palAmanuallist, palBmanuallist, usmanuallist;

        public NesOwnedAdapter(Context context, ArrayList<NesItems> list) {

            this.context = context;//sets up the context for the class
            nesList = list; //sets up a variable as a list

            //this.tf = Typeface.createFromAsset(context.getAssets(), "fonts/bentleyhand.ttf");//sets the font to the tf variable pulled from the fonts folder
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

                convertView = inflater.inflate(R.layout.owned_single_row, null); //use the layout to diaplay the array data


                ViewHolder holder = new ViewHolder();
                holder.cover = (ImageView) convertView.findViewById(R.id.imgGameCover);
                holder.gamename = (TextView) convertView.findViewById(R.id.lblGameName);
                holder.ownedcart = (TextView) convertView.findViewById(R.id.cart);
                holder.ownedbox = (TextView) convertView.findViewById(R.id.box);
                holder.ownedmanual = (TextView) convertView.findViewById(R.id.manual);
                holder.owned = (ImageView) convertView.findViewById(R.id.imgOwned);
                convertView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();

            palAcartlist = nesListItems.getCartPalA();
            palBcartlist = nesListItems.getCartPalB();
            uscartlist = nesListItems.getCartNtsc();
            palAboxlist = nesListItems.getBoxPalA();
            palBboxlist = nesListItems.getBoxPalB();
            usboxlist = nesListItems.getBoxNtsc();
            palAmanuallist = nesListItems.getManualPalA();
            palBmanuallist = nesListItems.getManualPalB();
            usmanuallist = nesListItems.getManualNtsc();
            gameimage = nesListItems.getImage();

            if (palAcartlist == 8783){palAcart = "A "; } else if(palAcartlist == 32573){ palAcart = "";}
            if (palAboxlist == 8783){palAbox = "A "; } else if(palAboxlist == 32573){ palAbox = "";}
            if (palAmanuallist == 8783){palAmanual = "A "; } else if(palAmanuallist == 32573){ palAmanual = "";}
            if (palBcartlist == 8783){palBcart = "B "; } else if(palBcartlist == 32573){ palBcart = "";}
            if (palBboxlist == 8783){palBbox = "B "; } else if(palBboxlist == 32573){ palBbox = "";}
            if (palBmanuallist == 8783){palBmanual = "B "; } else if(palBmanuallist == 32573){ palBmanual = "";}
            if (uscartlist == 8783){ntscart = "US"; } else if(uscartlist == 32573){ ntscart = "";}
            if (usboxlist == 8783){ntscbox = "US"; } else if(usboxlist == 32573){ ntscbox = "";}
            if (usmanuallist == 8783){ntscmanual = "US"; } else if(usmanuallist == 32573){ ntscmanual = "";}

            cart = palAcart +  palBcart +  ntscart;
            box = palAbox +  palBbox +  ntscbox;
            manual = palAmanual +  palBmanual + ntscmanual;
            //cover.setImageURI(Uri.parse(gameimage));
            int coverid=context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());
            holder.cover.setImageResource(coverid);
            holder.gamename.setText(nesListItems.getName()); //sets the textview name with data from name
            //holder.gamename.setText(test);

            holder.ownedcart.setText(cart);
            holder.ownedbox.setText(box);
            holder.ownedmanual.setText(manual);
            if (nesListItems.owned == 1){ holder.owned.setVisibility(View.VISIBLE);}else { holder.owned.setVisibility(View.INVISIBLE);}

            return convertView; //return the convertview and show the listview
        }




    }








