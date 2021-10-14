package uk.co.pixoveeware.nes_collection.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.pixoveeware.nes_collection.activities.About;
import uk.co.pixoveeware.nes_collection.activities.EditOwnedGame;
import uk.co.pixoveeware.nes_collection.activities.GamesDetail;
import uk.co.pixoveeware.nes_collection.adapters.NesPagerAdapter;
import uk.co.pixoveeware.nes_collection.models.GameItems;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesDetailFragment extends Fragment {

    AllGamesViewModel viewM;
    ArrayList<GameItems> gameList;
    ViewPager viewPager;
    NesPagerAdapter adapter;
    public static int idforgame, favourited, ownedgame, wishlist, finished;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int GameId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;

    public GamesDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamesDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesDetailFragment newInstance(String param1, int param2) {
        GamesDetailFragment fragment = new GamesDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt("GameId", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt("GameId");
        }

        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        gameList = viewM.gamesList;
        SetTitles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_games_detail, container, false);

        viewPager = v.findViewById(R.id.pager);
        adapter = new NesPagerAdapter(getContext(), gameList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mParam2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {getActivity().invalidateOptionsMenu();}
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {getActivity().invalidateOptionsMenu();}
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_gamedetails, menu);
        getActivity().invalidateOptionsMenu();
    }

    private void SetTitles() {
        getActivity().setTitle("Games Detail");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(null);
        }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem fav = menu.findItem(R.id.action_favourite);
        MenuItem own = menu.findItem(R.id.action_edit);
        if(favourited == 1){ fav.setIcon(R.drawable.ic_heart_white_24dp); }else if(favourited == 0){ fav.setIcon(R.drawable.ic_favorite_border_white_24dp);}
        if(ownedgame == 0){own.setIcon(R.drawable.ic_add_game24dp);} else if(ownedgame == 1){own.setIcon(R.drawable.ic_edit_white_24dp);}

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_edit:
                editgame();
                return true;

            case R.id.action_favourite:
                //favouritegame();
                return true;

            case R.id.action_finishedgame:
                //finishedgame();
                return true;

            case R.id.action_wishlist:
                //wishlist();
                return true;

            case R.id.action_about:
                //Intent intent3 = new Intent(GamesDetail.this, About.class);//opens a new screen when the shopping list is clicked
                //startActivity(intent3);//start the new screen
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void editgame(){
        Log.d("pixo", "Loading edit fragment");
        getParentFragmentManager().beginTransaction()
                .add(R.id.container, EditGameFragment.newInstance("", gameList.get(mParam2)._id))
                .addToBackStack("editGame")
                .commit();
    }
}