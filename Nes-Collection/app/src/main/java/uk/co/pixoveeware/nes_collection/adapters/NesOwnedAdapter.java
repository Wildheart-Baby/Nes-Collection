package uk.co.pixoveeware.nes_collection.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.MainActivity;
import uk.co.pixoveeware.nes_collection.R;

/**
 * Created by Wildheart on 11/06/2016.
 */
public class NesOwnedAdapter  extends BaseAdapter {

    public static int screenwidth, showprice;

        static class ViewHolder {
            TextView gamename, ownedcart, ownedbox, ownedmanual, gamecost, gamecost480;
            ImageView cover, Cart, Box, Manual, cartUs, cartPalA, cartPalB, boxUs, boxPalA, boxPalB, manUs, manPalA, manPalB;
            ImageView owned;

            TextView separator;
        }

        Context context; //sets up a variable as context
        ArrayList<GameItems> nesList; //sets up  an array called shoppingList
        String gameimage, cart, box, manual;
        String  palAcart, palBcart, ntscart, palAbox, palBbox, ntscbox, palAmanual, palBmanual, ntscmanual, gamescost, currency, thegamename;
        int palAcartlist, palBcartlist, uscartlist, palAboxlist, palBboxlist, usboxlist, palAmanuallist, palBmanuallist, usmanuallist, l, ownedCart, ownedBox, ownedManual;
        double palAcost, palBcost, ntsccost, thegamecost;

        public NesOwnedAdapter(Context context, ArrayList<GameItems> list) {

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

                convertView = inflater.inflate(R.layout.owned_single_row, null); //use the layout to diaplay the array data


                ViewHolder holder = new ViewHolder();
                holder.Cart = convertView.findViewById(R.id.imgCart);
                holder.Box = convertView.findViewById(R.id.imgBox);
                holder.Manual = convertView.findViewById(R.id.imgManual);
                holder.separator = convertView.findViewById(R.id.lblDivider);
                holder.cover = convertView.findViewById(R.id.imgGameCover);
                holder.gamename = convertView.findViewById(R.id.lblGameName);
                holder.ownedcart = convertView.findViewById(R.id.cart);
                holder.ownedbox = convertView.findViewById(R.id.box);
                holder.ownedmanual = convertView.findViewById(R.id.manual);
                holder.owned = convertView.findViewById(R.id.imgOwned);
                holder.gamecost = convertView.findViewById(R.id.lblCost);
                holder.gamecost480 = convertView.findViewById(R.id.lblCost480);

                holder.cartPalA = convertView.findViewById(R.id.imgPalACart);
                holder.boxPalA = convertView.findViewById(R.id.imgPalABox);
                holder.manPalA = convertView.findViewById(R.id.imgPalAMan);

                holder.cartPalB = convertView.findViewById(R.id.imgPalBCart);
                holder.boxPalB = convertView.findViewById(R.id.imgPalBBox);
                holder.manPalB = convertView.findViewById(R.id.imgPalBMan);

                holder.cartUs = convertView.findViewById(R.id.imgUsCart);
                holder.boxUs = convertView.findViewById(R.id.imgUsBox);
                holder.manUs = convertView.findViewById(R.id.imgUsMan);

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
            palAcost = nesListItems.getPalACost();
            palBcost = nesListItems.getPalBCost();
            ntsccost = nesListItems.getNtscCost();
            currency = nesListItems.getCurrency();
            ownedCart = nesListItems.getCart();
            ownedBox = nesListItems.getBox();
            ownedManual = nesListItems.getManual();

            Log.i("owned-adap", "cart a:"+palAcartlist + " cart b:" + palBcartlist + " cart us:" +
                    uscartlist  + " box a:" +
                    palAboxlist  + " box b:" +
                    palBboxlist  + " box us:" +
                    usboxlist  + " man a:" +
                    palAmanuallist  + " man b:" +
                    palBmanuallist  + " man us:" +
                    usmanuallist);

            if(ownedCart == 0){holder.Cart.setVisibility(View.INVISIBLE);}
            else if (ownedCart == 1){holder.Cart.setVisibility(View.VISIBLE);}
            if(ownedBox == 0){holder.Box.setVisibility(View.INVISIBLE);}
            else if(ownedBox == 1){holder.Box.setVisibility(View.VISIBLE);}
            if(ownedManual == 0){holder.Manual.setVisibility(View.INVISIBLE);}
            else  if(ownedManual == 1){holder.Manual.setVisibility(View.VISIBLE);}

            int blankId = context.getResources().getIdentifier("blank_flag_small", "drawable", context.getPackageName());
            int palAId = context.getResources().getIdentifier("pal_a_smallest", "drawable", context.getPackageName());
            int palBId = context.getResources().getIdentifier("euro_smallest", "drawable", context.getPackageName());
            int usId = context.getResources().getIdentifier("us_smallest", "drawable", context.getPackageName());

            if (palAcartlist == 8783) {
                holder.cartPalA.setImageResource(palAId);
            } else if (palAcartlist == 32573) {
                holder.cartPalA.setImageResource(blankId);
            }
            if (palAboxlist == 8783) {
                holder.boxPalA.setImageResource(palAId);
            } else if (palAboxlist == 32573) {
                holder.boxPalA.setImageResource(blankId);
            }
            if (palAmanuallist == 8783) {
                holder.manPalA.setImageResource(palAId);
            } else if (palAmanuallist == 32573) {
                holder.manPalA.setImageResource(blankId);
            }
            if (palBcartlist == 8783) {
                holder.cartPalB.setImageResource(palBId);
            } else if (palBcartlist == 32573) {
                holder.cartPalB.setImageResource(blankId);
            }
            if (palBboxlist == 8783) {
                holder.boxPalB.setImageResource(palBId);
            } else if (palBboxlist == 32573) {
                holder.boxPalB.setImageResource(blankId);
            }
            if (palBmanuallist == 8783) {
                holder.manPalB.setImageResource(palBId);
            } else if (palBmanuallist == 32573) {
                holder.manPalB.setImageResource(blankId);
            }
            if (uscartlist == 8783) {
                //Log.i("pixo-owned-adapter", "owned cart");
                holder.cartUs.setImageResource(usId);
            } else if (uscartlist == 32573) {
                holder.cartUs.setImageResource(blankId);
                //Log.i("pixo-owned-adapter", "blank cart");
            }
            if (usboxlist == 8783) {
                holder.boxUs.setImageResource(usId);
            } else if (usboxlist == 32573) {
                holder.boxUs.setImageResource(blankId);
            }
            if (usmanuallist == 8783) {
                holder.manUs.setImageResource(usId);
            } else if (usmanuallist == 32573) {
                holder.manUs.setImageResource(blankId);
            }

            if (palAcost > 0.0) {
                gamescost = currency + String.format("%.2f", palAcost);
            } else if (palBcost > 0.0) {
                gamescost = currency + String.format("%.2f", palBcost);
            } else if (ntsccost > 0.0) {
                gamescost = currency + String.format("%.2f", ntsccost);
            } else if (palAcost == 0.0 && palBcost == 0.0 && ntsccost == 0.0) {
                gamescost = currency + "0.00";
            }

            cart = palAcart + palBcart + ntscart;
            box = palAbox + palBbox + ntscbox;
            manual = palAmanual + palBmanual + ntscmanual;
            //cover.setImageURI(Uri.parse(gameimage));

            if (nesListItems.group.equals("no")) {
                holder.separator.setVisibility(View.GONE);
            } else {
                holder.separator.setVisibility(View.VISIBLE);
            }
            holder.separator.setText(nesListItems.getGroup());
            int coverid = context.getResources().getIdentifier(gameimage, "drawable", context.getPackageName());
            holder.cover.setImageResource(coverid);
            thegamename = nesListItems.getName();
            l = thegamename.length();
            if (screenwidth < 600){if (l >30) {thegamename = thegamename.substring(0,27) + "...";}}
            holder.gamename.setText(thegamename); //sets the textview name with data from name
            if (showprice == 1){
                if (screenwidth > 599) {
                    holder.gamecost.setText(gamescost);
                } else if (screenwidth < 600) {
                    holder.gamecost480.setVisibility(View.VISIBLE);
                    holder.gamecost480.setText(gamescost);
                    holder.gamecost.setVisibility(View.GONE);
                }
            }

            if (position % 2 == 0){
                convertView.setBackgroundColor(Color.parseColor("#CAC9C5"));
            } else {
                convertView.setBackgroundColor(Color.parseColor("#C0C0BB"));
            }

            return convertView; //return the convertview and show the listview
        }




    }








