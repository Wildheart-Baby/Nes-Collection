package uk.co.bossdogsoftware.nes_collection.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.activities.About;
import uk.co.bossdogsoftware.nes_collection.activities.HomeScreenActivity;
import uk.co.bossdogsoftware.nes_collection.activities.Search;
import uk.co.bossdogsoftware.nes_collection.activities.Settings;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment HomeScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        SetTitles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        final ImageButton AllGames = v.findViewById(R.id.btnAllGames);
        ImageButton NeededGames = v.findViewById(R.id.btnneeded);
        ImageButton OwnedGames = v.findViewById(R.id.btnowned);
        ImageButton Stats = v.findViewById(R.id.btnStats);
        final ImageButton ShelfOrder = v.findViewById(R.id.btnShelfOrder);
        /*ImageButton FavouriteGames = getView().findViewById(R.id.btnFavouriteGames);
        ImageButton WishList = getView().findViewById(R.id.btnWishList);



        ImageButton Finished = getView().findViewById(R.id.btnFinished);*/
        ImageButton GameSettings = v.findViewById(R.id.btnSettings);

        AllGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ChangeFragment(AllGamesFragment.newInstance());
                getParentFragmentManager().beginTransaction()
                        .add(R.id.container, AllGamesFragment.newInstance(), "gamesList")
                        .addToBackStack("gamesList")
                        .commit();
                //ChangeFragment(NeededGamesFragment.newInstance(), "allGames");
            }
        });

        NeededGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ChangeFragment(AllGamesFragment.newInstance());
                getParentFragmentManager().beginTransaction()
                        .add(R.id.container, NeededGamesFragment.newInstance(), "gamesList")
                        .addToBackStack(null)
                        .commit();
                //ChangeFragment(NeededGamesFragment.newInstance(), "neededGames");
            }
        });

        OwnedGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ChangeFragment(AllGamesFragment.newInstance());
                getParentFragmentManager().beginTransaction()
                        .add(R.id.container, OwnedGamesFragment.newInstance(), "gamesList")
                        .addToBackStack(null)
                        .commit();
                //ChangeFragment(NeededGamesFragment.newInstance(), "neededGames");
            }
        });

        GameSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ChangeFragment(AllGamesFragment.newInstance());
                getParentFragmentManager().beginTransaction()
                        .add(R.id.container, SettingsFragment.newInstance("", ""), "gamesList")
                        .addToBackStack(null)
                        .commit();
                //ChangeFragment(NeededGamesFragment.newInstance(), "neededGames");
            }
        });

        Stats.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getParentFragmentManager().beginTransaction()
                        .add(R.id.container, StatisticsFragment.newInstance("", ""), "gamesList")
                        .addToBackStack(null)
                        .commit();
            }
        });

        ShelfOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getParentFragmentManager().beginTransaction()
                        .add(R.id.container, ShelfOrderFragment.newInstance("", ""), "gamesList")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }

    public void ChangeFragment(Fragment frag, String name){
        if (getView().findViewById(R.id.container) != null){
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .addToBackStack(name)
                    .commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_allgames, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_allgames, menu);
        SetTitles();
    }


    private void SetTitles(){
        getActivity().setTitle("Nes Collection");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                getParentFragmentManager().beginTransaction()
                        .add(R.id.container, SettingsFragment.newInstance("", ""), "gamesList")
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_search:
                //Intent intent2 = new Intent(HomeScreenActivity.this, Search.class);//opens a new screen when the shopping list is clicked
                //startActivity(intent2);//start the new screen
                return true;

            case R.id.action_about:
                Intent intent3 = new Intent(getActivity(), About.class);//opens a new screen when the shopping list is clicked
                startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}