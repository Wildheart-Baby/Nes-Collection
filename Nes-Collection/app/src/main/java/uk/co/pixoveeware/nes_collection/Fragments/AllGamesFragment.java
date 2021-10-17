package uk.co.pixoveeware.nes_collection.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.pixoveeware.nes_collection.activities.AllGames;
import uk.co.pixoveeware.nes_collection.activities.EditOwnedGame;
import uk.co.pixoveeware.nes_collection.activities.GamesDetail;
import uk.co.pixoveeware.nes_collection.adapters.LightCollectionAdapter;
import uk.co.pixoveeware.nes_collection.adapters.LightImageCollectionAdapter;
import uk.co.pixoveeware.nes_collection.adapters.NesCollectionAdapter;
import uk.co.pixoveeware.nes_collection.adapters.NesIndexAdapter;
import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllGamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllGamesFragment extends Fragment {

    AllGamesViewModel viewM;
    ListView gamelistView, alphaIndex;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<GameItems> gameList;
    ArrayList<GameItemsIndex> indexList;

    public AllGamesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AllGamesFragment newInstance() {
        AllGamesFragment fragment = new AllGamesFragment();
        Bundle args = new Bundle();
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
        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        gameList = viewM.GetGames("all");
        indexList = viewM.GetIndex("all");

        SetTitles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_all_games, container, false);
        //viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));

        gamelistView = v.findViewById(R.id.lvAllGames);
        alphaIndex = v.findViewById(R.id.lvAlphaIndex);
        //NesCollectionAdapter nes = new NesCollectionAdapter(getContext(), gameList);//set up an new list adapter from the arraylist
        gamelistView.setAdapter(new NesCollectionAdapter(getContext(), gameList));
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
                        .addToBackStack(null)
                        .commit();
            }
        });

        gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                return true;//return is equal to true
            }

        });

        return v;
    }

    public void loadList(){//the readlist function
        //if(gameList == null){gamesList = viewM.gamesList;}
        //if(indexList == null){indexList = viewM.indexList;}
       // if(viewas == 0){
            NesCollectionAdapter nes = new NesCollectionAdapter(getContext(), gameList);//set up an new list adapter from the arraylist
            //gamegalleryview.setVisibility(View.GONE);
            gamelistView.setAdapter(nes);
            /*NesIndexAdapter nii = new NesIndexAdapter(this, indexList);
            alphaIndex.setAdapter(nii);

            alphaIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    GameItemsIndex indexListItems = (GameItemsIndex) arg0.getItemAtPosition(arg2);
                    readindexid = indexListItems.getItemid();
                    //readindexid = readindexid - 1;
                    gamelistView.setSelection(readindexid);
                    //gamegalleryview.setSelection(readindexid);
                }
            });

            gamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list

                    GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                    readgameid = gameListItems.getItemId();//get the name of the shopping list table
                    readgamename = gameListItems.getName();//get the name of the shopping list table
                    //sql = wherestatement + licensed;
                    sql = "all";
                    Intent intent = new Intent(AllGames.this, GamesDetail.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("gameid", readgameid);//passes the table name to the new screen
                    intent.putExtra("name", readgamename);//passes the table name to the new screen
                    intent.putExtra("position", arg2);
                    intent.putExtra("listposition", arg2);
                    intent.putExtra("sqlstatement", sql);
                    intent.putExtra("gamename", thename);
                    intent.putExtra("gameimage", theimage);
                    intent.putExtra("listType", "all");

                    startActivity(intent);//start the new screen
                }
            });

            gamelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on long press on an item

                    GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                    final Integer itemId = gameListItems.getItemId();//get the item id

                    Intent intent = new Intent(AllGames.this, EditOwnedGame.class);//opens a new screen when the shopping list is clicked
                    intent.putExtra("editgameid", itemId);
                    intent.putExtra("listposition", arg2);
                    startActivity(intent);//start the new screen

                    return true;//return is equal to true
                }

            });*/

        //}
    }

    @Override
    public void onResume(){
        super.onResume();
        SetTitles();
    }

    private void SetTitles(){
        getActivity().setTitle(" " + viewM.RegionTitle("all"));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(viewM.FragmentSubTitleText("all"));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(getContext().getResources().getIdentifier(viewM.regionFlag, "drawable", getContext().getPackageName()));
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

}