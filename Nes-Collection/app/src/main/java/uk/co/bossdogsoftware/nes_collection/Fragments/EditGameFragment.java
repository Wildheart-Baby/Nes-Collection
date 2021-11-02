package uk.co.bossdogsoftware.nes_collection.Fragments;

import android.icu.util.Currency;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModel;

import uk.co.bossdogsoftware.nes_collection.adapters.spinners.PlayedSpinnerAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameItem;
import uk.co.bossdogsoftware.nes_collection.models.spinners.Data;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditGameFragment extends Fragment {

    public Spinner spinner_played;
    public PlayedSpinnerAdapter spinnerAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    int coverid, usbox, manual, favourite;
    String PACheck, PBCheck, USCheck;
    Double  PalAcost,PalBcost, UScost;

    TextView gamename, CostHdr, PalACurrency, PalBCurrency, USCurrency;
    ImageView cover;
    CheckBox chkpalacart, chkpalabox, chkpalamanual, chkpalbcart, chkpalbbox, chkpalbmanual, chkuscart, chkusbox, chkusmanual, onshelf, BlankChk;
    EditText PalACost, PalBCost, USCost;

    AllGamesViewModel viewM;
    ArrayList<AllGameItems> gameList;

    GameItem gameDetails;

    // TODO: Rename and change types of parameters
    private int mParam1;
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
    public static EditGameFragment newInstance(int param1, int param2) {
        EditGameFragment fragment = new EditGameFragment();
        Bundle args = new Bundle();
        args.putInt("ListPos", param1);
        args.putInt("GameId", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt("ListPos");
            mParam2 = getArguments().getInt("GameId");
        }
        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        gameDetails = viewM.GetGameDetails(mParam2);

        getActivity().setTitle("Edit Game");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(null);
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
        //BlankChk = v.findViewById(R.id.checkBox);

        PalACurrency = v.findViewById(R.id.lblCurrencyPalA);
        PalBCurrency = v.findViewById(R.id.lblCurrencyPalB);
        USCurrency = v.findViewById(R.id.lblCurrencyUS);

        spinner_played = v.findViewById(R.id.spinner_condition);
        spinnerAdapter = new PlayedSpinnerAdapter(getActivity(), Data.getConditionList());
        spinner_played.setAdapter(spinnerAdapter);

        ShowOwnedDetails();
        // Inflate the layout for this fragment

        Button ok = v.findViewById(R.id.rgnOk);
        Button cancel = v.findViewById(R.id.rgnCancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writegame();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeEditFragment();
            }
        });

        spinner_played.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gameDetails.gameCondition = (int) spinner_played.getSelectedItem();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });

        return v;
    }


    public void onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_editgame, menu);
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_editgame, menu);
        MenuItem fav = menu.findItem(R.id.action_favourite);
        if(viewM.gamesList.get(mParam1).getFavourite() == 1){ fav.setIcon(R.drawable.ic_heart_red_24dp); }else { fav.setIcon(R.drawable.ic_favorite_border_white_24dp);}

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                //Intent intent3 = new Intent(EditOwnedGame.this, About.class);//opens a new screen when the shopping list is clicked
                //startActivity(intent3);//start the new screen
                return true;

            case R.id.action_favourite:
                favouritegame();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void ShowOwnedDetails(){

        gamename.setText(gameDetails.name);
        coverid = getResources().getIdentifier(gameDetails.image, "drawable", getContext().getPackageName());
        cover.setImageResource(coverid);

        spinner_played.setSelection(gameDetails.gameCondition);

        if(gameDetails.getPalARelease() == 0){
            chkpalacart.setEnabled(false); chkpalabox.setEnabled(false); chkpalamanual.setEnabled(false);  PalACost.setEnabled(false); PalACost.setText("");
        } else {
            if (gameDetails.getCartPalA() == 8783) { chkpalacart.setChecked(true); } else { chkpalacart.setChecked(false); }
            if (gameDetails.getBoxPalA() == 8783) { chkpalabox.setChecked(true); } else { chkpalabox.setChecked(false); }
            if (gameDetails.getManualPalA() == 8783) { chkpalamanual.setChecked(true); } else { chkpalamanual.setChecked(false); }
            }

        if (gameDetails.pal_b_release == 0) { chkpalbcart.setEnabled(false); chkpalbbox.setEnabled(false); chkpalbmanual.setEnabled(false);  PalBCost.setEnabled(false); PalBCost.setText("");}
        else {
            if (gameDetails.getCartPalB() == 8783) { chkpalbcart.setChecked(true);  } else { chkpalbcart.setChecked(false); }
            if (gameDetails.getBoxPalB() == 8783) { chkpalbbox.setChecked(true); } else { chkpalbbox.setChecked(false);  }
            if (gameDetails.getManualPalB() == 8783) { chkpalbmanual.setChecked(true); } else { chkpalbmanual.setChecked(false);}
            //if (gameDetails. == 0 ){ chkpalbbox.setEnabled(false); }
        }
        if (gameDetails.ntsc_release == 0) { chkuscart.setEnabled(false); chkusbox.setEnabled(false); chkusmanual.setEnabled(false);  USCost.setEnabled(false); USCost.setText("");}
        else {
            if (gameDetails.getCartNtsc() == 8783) { chkuscart.setChecked(true); } else { chkuscart.setChecked(false); }
            if (gameDetails.getBoxNtsc() == 8783) { chkusbox.setChecked(true); } else { chkusbox.setChecked(false); }
            if (gameDetails.getManualNtsc() == 8783) { chkusmanual.setChecked(true); } else { chkusmanual.setChecked(false);}
            //if (usbox == 0){ chkusbox.setEnabled(false); }
        }

        Locale deviceLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(deviceLocale);

        if(viewM.ShowPrices() == 1){
            PalACurrency.setText(currency.getSymbol());
            PalBCurrency.setText(currency.getSymbol());
            USCurrency.setText(currency.getSymbol());
        }

        //String deviceLocale = Locale.getDefault().getCountry();




        if(gameDetails.getPalACost() > 0.0){
            PalACost.setText(String.format("%.2f", gameDetails.getPalACost()));
        }

        if(gameDetails.getPalBCost() > 0.0){
            PalBCost.setText(String.format("%.2f", gameDetails.getPalBCost()));
        }

        if(gameDetails.getNtscCost() > 0.0){
            USCost.setText(String.format("%.2f", gameDetails.getNtscCost()));
        }

        favourite = gameDetails.getFavourite();
        PalAcost = gameDetails.getPalACost();
        PalBcost = gameDetails.getPalBCost();
        UScost = gameDetails.getNtscCost();

        if (gameDetails.getOnShelf() == 1){onshelf.setChecked(true);} else { onshelf.setChecked(false);}
        if (gameDetails.showPrice == 0){
            CostHdr.setVisibility(View.INVISIBLE);
            PalACurrency.setVisibility(View.INVISIBLE);
            PalBCurrency.setVisibility(View.INVISIBLE);
            USCurrency.setVisibility(View.INVISIBLE);
            PalACost.setVisibility(View.INVISIBLE);
            PalBCost.setVisibility(View.INVISIBLE);
            USCost.setVisibility(View.INVISIBLE);}
    }

    public void writegame(){
        if (chkpalacart.isChecked()){ gameDetails.pal_a_cart = 8783; gameDetails.cart = 1; gameDetails.pal_a_owned = 1; gameDetails.owned = 1; } else  { gameDetails.pal_a_cart = 32573; }
        if (chkpalabox.isChecked()){ gameDetails.pal_a_box = 8783; gameDetails.box = 1; gameDetails.owned = 1; } else { gameDetails.pal_a_box = 32573; }
        if (chkpalamanual.isChecked()){ gameDetails.pal_a_manual = 8783; gameDetails.manual = 1; gameDetails.owned = 1; } else { gameDetails.pal_a_manual = 32573; }

        if (chkpalbcart.isChecked()){ gameDetails.pal_b_cart = 8783; gameDetails.cart = 1; gameDetails.pal_b_owned = 1; gameDetails.owned = 1; } else { gameDetails.pal_b_cart = 32573; }
        if (chkpalbbox.isChecked()){ gameDetails.pal_b_box = 8783; gameDetails.box = 1; gameDetails.owned = 1; } else { gameDetails.pal_b_box = 32573; }
        if (chkpalbmanual.isChecked()){ gameDetails.pal_b_manual = 8783; gameDetails.manual = 1; gameDetails.owned = 1; } else { gameDetails.pal_b_manual = 32573; }

        if (chkuscart.isChecked()){ gameDetails.ntsc_cart = 8783; gameDetails.cart = 1; gameDetails.ntsc_owned = 1; gameDetails.owned = 1; } else { gameDetails.ntsc_cart = 32573; }
        if (chkusbox.isChecked()){ gameDetails.ntsc_box = 8783; gameDetails.box = 1; gameDetails.owned = 1; } else { gameDetails.ntsc_box = 32573;  }
        if (chkusmanual.isChecked()){ gameDetails.ntsc_manual = 8783; gameDetails.manual = 1; gameDetails.owned = 1; } else { gameDetails.ntsc_manual = 32573; }

        if (onshelf.isChecked()){gameDetails.onShelf = 1;} else { gameDetails.onShelf = 0;}

        if(gameDetails.wishlist == 1){gameDetails.wishlist = 0;}

        if (gameDetails.getCartPalA() == 32573 && gameDetails.getCartPalB() == 32573 && gameDetails.getCartNtsc() == 32573) { gameDetails.setOwned(0);  }
        if (gameDetails.pal_a_box == 32573 && gameDetails.pal_b_box == 32573 && usbox == 32573) { gameDetails.box = 0; }
        if (gameDetails.pal_a_manual == 32573 && gameDetails.pal_b_manual == 32573 && gameDetails.ntsc_manual == 32573) { manual = 0; }


       /* if (cart == 0 && box == 0 && manual == 0) {owned = 0; HomeScreenActivity.gamesList.get(gamepos).setOwned(0);}*/
        PACheck = PalACost.getText().toString().replaceAll("[,]", ".");
        //Log.d("Pixo-cost", PACheck);
        PACheck = PalACost.getText().toString().replaceAll("[^0-9.]", "");
        PBCheck = PalBCost.getText().toString().replaceAll("[^0-9.]", "");
        USCheck = USCost.getText().toString().replaceAll("[^0-9.]", "");
        //USCheck = USCheck.replaceAll("[^0-9.]", "");

        //Log.d("pixo", " " + PACheck + " " + PBCheck + " " + USCheck  );

        if (PACheck.matches("") || !Character.isDigit(PACheck.charAt(0))) {PalAcost = 0.0;}
        else {PalAcost = Double.parseDouble(PACheck);}
        gameDetails.setPalACost(PalAcost);

        if (PBCheck.matches("") || !Character.isDigit(PBCheck.charAt(0))) {PalBcost = 0.0;}
        else {PalBcost = Double.parseDouble(PBCheck);}
        gameDetails.setPalBCost(PalBcost);
        //else {PalBcost = Double.valueOf(PalBCost.getText().toString());}

        if (USCheck.matches("") || !Character.isDigit(USCheck.charAt(0))) {UScost = 0.0;}
        else {UScost = Double.parseDouble(USCheck);}
        gameDetails.setNtscCost(UScost);
        //else {UScost = Double.valueOf(USCost.getText().toString());}

        if (PalAcost > PalBcost && PalAcost > UScost){gameDetails.setGamePrice(PalAcost);}
        else if (PalBcost > PalAcost && PalBcost > UScost){gameDetails.setGamePrice(PalBcost);}
        else if (UScost > PalAcost && UScost > PalBcost){gameDetails.setGamePrice(UScost);}

        if (PACheck.equals("0.00") && PBCheck.equals("0.00") && USCheck.equals("0.00")){gameDetails.setGamePrice(0.00);}
        //gameDetails._id = mParam2;



        viewM.WriteGame(mParam1, gameDetails);
        closeEditFragment();
    }

    public void closeEditFragment(){
        FragmentManager manager = getParentFragmentManager();
        manager.popBackStack();
    }

    public void favouritegame(){
        viewM.favouriteGame(mParam1, mParam2);
        getActivity().invalidateOptionsMenu();
    }

}