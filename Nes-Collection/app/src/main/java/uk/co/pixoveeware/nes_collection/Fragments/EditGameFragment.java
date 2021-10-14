package uk.co.pixoveeware.nes_collection.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.pixoveeware.nes_collection.models.GameItem;
import uk.co.pixoveeware.nes_collection.models.GameItems;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditGameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    int gameid, coverid, palAcart, palAbox, palAmanual, palBcart, palBbox, gamepos,
            palBmanual, uscart, usbox, usmanual, cart, box, manual, owned,
            regionatrue, regionbtrue, regionustrue, favourite, ontheshelf, wishlist, showprice, palaowned, palbowned, usowned;
    String covername, sql, test, currency, PACheck, PBCheck, USCheck;
    Double  PalAcost,PalBcost, UScost, price;

    TextView gamename, CostHdr, PalACurrency, PalBCurrency, USCurrency;
    ImageView cover;
    CheckBox chkpalacart, chkpalabox, chkpalamanual, chkpalbcart, chkpalbbox, chkpalbmanual, chkuscart, chkusbox, chkusmanual, onshelf, BlankChk;
    EditText PalACost, PalBCost, USCost;

    AllGamesViewModel viewM;
    ArrayList<GameItems> gameList;

    GameItem gameDetails;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;

    public EditGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditGameFragment newInstance(String param1, int param2) {
        EditGameFragment fragment = new EditGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        gameDetails = viewM.GetGamesDetails(mParam2);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_game, container, false);
        gamename = v.findViewById(R.id.lblGame);
        cover = v.findViewById(R.id.imgCover);

        chkpalacart = v.findViewById(R.id.chkPalACart);
        chkpalabox = v.findViewById(R.id.chkPalABox);
        chkpalamanual = v.findViewById(R.id.chkPalAmanual);
        chkpalbcart = v.findViewById(R.id.chkPalBCart);
        chkpalbbox = v.findViewById(R.id.chkPalBBox);
        chkpalbmanual = v.findViewById(R.id.chkPalBmanual);
        chkuscart = v.findViewById(R.id.chkUSCart);
        chkusbox = v.findViewById(R.id.chkUSBox);
        chkusmanual = v.findViewById(R.id.chkUSmanual);
        onshelf = v.findViewById(R.id.chkShelf);

        CostHdr = v.findViewById(R.id.lblCost);
        PalACost = v.findViewById(R.id.txtPalAcost);
        PalBCost = v.findViewById(R.id.txtPalBcost);
        USCost = v.findViewById(R.id.txtUScost);
        BlankChk = v.findViewById(R.id.checkBox);

        PalACurrency = v.findViewById(R.id.lblCurrencyPalA);
        PalBCurrency = v.findViewById(R.id.lblCurrencyPalB);
        USCurrency = v.findViewById(R.id.lblCurrencyUS);
        ShowOwnedDetails();
        // Inflate the layout for this fragment
        return v;
    }

    private void ShowOwnedDetails(){

        gamename.setText(gameDetails.getName());
        coverid = getResources().getIdentifier(gameDetails.getImage(), "drawable", getContext().getPackageName());
        cover.setImageResource(coverid);

        if(gameDetails.getPalARelease() == 0){
            chkpalacart.setEnabled(false); chkpalabox.setEnabled(false); chkpalamanual.setEnabled(false);  PalACost.setEnabled(false); PalACost.setText("");
        } else {
            if (gameDetails.pal_a_cart == 8783) { chkpalacart.setChecked(true); } else { chkpalacart.setChecked(false); }
            if (gameDetails.pal_a_box == 8783) { chkpalabox.setChecked(true); } else { chkpalabox.setChecked(false); }
            if (gameDetails.pal_a_manual == 8783) { chkpalamanual.setChecked(true); } else { chkpalamanual.setChecked(false); }
            }

        if (gameDetails.getPalBRelease() == 0) { chkpalbcart.setEnabled(false); chkpalbbox.setEnabled(false); chkpalbmanual.setEnabled(false);  PalBCost.setEnabled(false); PalBCost.setText("");}
        else {
            if (gameDetails.pal_b_cart == 8783) { chkpalbcart.setChecked(true);  } else { chkpalbcart.setChecked(false); }
            if (gameDetails.pal_b_box == 8783) { chkpalbbox.setChecked(true); } else { chkpalbbox.setChecked(false);  }
            if (gameDetails.pal_b_manual == 8783) { chkpalbmanual.setChecked(true); } else { chkpalbmanual.setChecked(false);}
            //if (gameDetails. == 0 ){ chkpalbbox.setEnabled(false); }
        }
        if (gameDetails.getNtscRelease() == 0) { chkuscart.setEnabled(false); chkusbox.setEnabled(false); chkusmanual.setEnabled(false);  USCost.setEnabled(false); USCost.setText("");}
        else {if (uscart == 8783) { chkuscart.setChecked(true); } else { chkuscart.setChecked(false); }
            if (usbox == 8783) { chkusbox.setChecked(true); } else { chkusbox.setChecked(false); }
            if (usmanual == 8783) { chkusmanual.setChecked(true); } else { chkusmanual.setChecked(false);}
            //if (usbox == 0){ chkusbox.setEnabled(false); }
        }


        favourite = gameDetails.getFavourite();
        PalAcost = gameDetails.getPalACost();
        PalBcost = gameDetails.getPalBCost();
        UScost = gameDetails.getNtscCost();


        if (gameDetails.getOnShelf() == 1){onshelf.setChecked(true);} else { onshelf.setChecked(false);}
        if (gameDetails.showPrice == 0){CostHdr.setVisibility(View.GONE); BlankChk.setVisibility(View.GONE);
            PalACurrency.setVisibility(View.GONE); PalBCurrency.setVisibility(View.GONE); USCurrency.setVisibility(View.GONE);
            PalACost.setVisibility(View.GONE); PalBCost.setVisibility(View.GONE); USCost.setVisibility(View.GONE);}
    }

}