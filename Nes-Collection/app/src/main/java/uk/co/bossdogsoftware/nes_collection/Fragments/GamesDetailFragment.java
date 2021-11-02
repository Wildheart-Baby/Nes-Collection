package uk.co.bossdogsoftware.nes_collection.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.bossdogsoftware.nes_collection.adapters.NesPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesDetailFragment extends Fragment {

    AllGamesViewModel viewM;
    //ArrayList<AllGameItems> gameList;
    ViewPager viewPager;
    public static NesPagerAdapter gamesAdapter;
    public static int idforgame, favourited, ownedgame, wishlist, finished, listPos;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private int GameId;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;

    GamesDetailFragment fragment;

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
    public static GamesDetailFragment newInstance(int param1, int param2) {
        GamesDetailFragment fragment = new GamesDetailFragment();
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
        //gameList = viewM.gamesList;
        SetTitles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_games_detail_pager, container, false);

        viewPager = v.findViewById(R.id.pager);
        gamesAdapter = new NesPagerAdapter(this.getParentFragmentManager(), getContext(), viewM.gamesList, viewM.GetTitles());
        viewPager.setAdapter(gamesAdapter);
        viewPager.setCurrentItem(mParam2);
        listPos = viewPager.getCurrentItem();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {getActivity().invalidateOptionsMenu();}
            @Override
            public void onPageSelected(int position) {listPos = viewPager.getCurrentItem();  }
            @Override
            public void onPageScrollStateChanged(int state) {getActivity().invalidateOptionsMenu();}
        });
        // Inflate the layout for this fragment
        return v;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_gamedetails, menu);
        //getActivity().invalidateOptionsMenu();
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        menu.clear();
        inflater.inflate(R.menu.menu_gamedetails, menu);
        return true;
    }*/

    private void SetTitles() {
        getActivity().setTitle("Games Detail");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(null);
        }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gamedetails, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_gamedetails, menu);
        MenuItem fav = menu.findItem(R.id.action_favourite);
        MenuItem own = menu.findItem(R.id.action_edit);
        if(viewM.gamesList.get(listPos).getFavourite() == 1){ fav.setIcon(R.drawable.ic_heart_white_24dp); }else if(favourited == 0){ fav.setIcon(R.drawable.ic_favorite_border_white_24dp);}
        if(viewM.gamesList.get(listPos).getOwned() == 0){own.setIcon(R.drawable.ic_add_game24dp);} else if(ownedgame == 1){own.setIcon(R.drawable.ic_edit_white_24dp);}
        if(viewM.gamesList.get(listPos).getOwned() == 1){ menu.removeItem(R.id.action_wishlist); }
        if(viewM.gamesList.get(listPos).getFavourite() == 1){ menu.removeItem(R.id.action_finishedgame); }
        SetTitles();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editgame();
                return true;

            case R.id.action_favourite:
                favouritegame();
                return true;

            case R.id.action_finishedgame:
                //finishedgame();
                return true;

            case R.id.action_wishlist:
                //wishlist();
                return true;

            case R.id.action_about:

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void editgame(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container, EditGameFragment.newInstance(listPos, viewM.gamesList.get(listPos).getItemId()), "editGame")
                .addToBackStack(null)
                .commit();
    }

    public void favouritegame(){
        viewM.favouriteGame(listPos, viewM.gamesList.get(listPos).getItemId());
        getActivity().invalidateOptionsMenu();
    }

    public void wishlist(){
        viewM.wishList(listPos, viewM.gamesList.get(listPos).getItemId());
        getActivity().invalidateOptionsMenu();
    }

}

