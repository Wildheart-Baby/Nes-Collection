package uk.co.bossdogsoftware.nes_collection.Fragments;

import android.content.Intent;
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
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.bossdogsoftware.nes_collection.activities.About;
import uk.co.bossdogsoftware.nes_collection.adapters.NesIndexAdapter;
import uk.co.bossdogsoftware.nes_collection.adapters.NesOwnedAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameItemsIndex;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OwnedGamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnedGamesFragment extends Fragment {

    AllGamesViewModel viewM;
    ListView gamelistView, alphaIndex;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<AllGameItems> gameList;
    ArrayList<GameItemsIndex> indexList;

    FragmentManager frg;

    public OwnedGamesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OwnedGamesFragment newInstance() {
        OwnedGamesFragment fragment = new OwnedGamesFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        frg = getParentFragmentManager();

        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        gameList = viewM.GetGames("owned");
        indexList = viewM.GetIndex("owned");

        SetTitles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_owned_games, container, false);
        gamelistView = v.findViewById(R.id.lvAllGames);
        alphaIndex = v.findViewById(R.id.lvAlphaIndex);

        gamelistView.setAdapter(new NesOwnedAdapter(getContext(), gameList, viewM.ShowPrices()));
        alphaIndex.setAdapter(new NesIndexAdapter(getContext(), indexList));

        alphaIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GameItemsIndex indexListItems = (GameItemsIndex) arg0.getItemAtPosition(arg2);
                int readindexid = indexListItems.getItemid();
                //readindexid = readindexid - 1;
                gamelistView.setSelection(readindexid);
                //gamegalleryview.setSelection(readindexid);
            }
        });

        gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                //GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                getParentFragmentManager().beginTransaction()
                        .add(R.id.container, GamesDetailFragment.newInstance(0, arg2), "gamesDetail")
                        .addToBackStack("gamesDetail")
                        .commit();
            }
        });

        gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, EditGameFragment.newInstance(arg2, viewM.gamesList.get(arg2).getItemId()), "editGame")
                        .addToBackStack(null)
                        .commit();
                return true;//return is equal to true
            }

        });


        // Inflate the layout for this fragment
        return v;
    }

    private void SetTitles(){
        getActivity().setTitle(" " + viewM.RegionTitle("owned"));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(viewM.FragmentSubTitleText("owned", "owned"));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(getContext().getResources().getIdentifier(viewM.regionFlag, "drawable", getContext().getPackageName()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ownedgames, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_ownedgames, menu);
        SetTitles();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Fragment f = frg.findFragmentByTag("gamesList");
                frg.beginTransaction()
                        .remove(f)
                        .commit();
                frg.popBackStack();
                frg.beginTransaction()
                        .add(R.id.container, SettingsFragment.newInstance("", ""), "gamesList")
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_search:
                //Intent intent2 = new Intent(this, Search.class);//opens a new screen when the shopping list is clicked
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