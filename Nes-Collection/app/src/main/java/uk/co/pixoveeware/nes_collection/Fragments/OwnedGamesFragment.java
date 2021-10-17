package uk.co.pixoveeware.nes_collection.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.pixoveeware.nes_collection.adapters.NesCollectionAdapter;
import uk.co.pixoveeware.nes_collection.adapters.NesIndexAdapter;
import uk.co.pixoveeware.nes_collection.models.GameItems;
import uk.co.pixoveeware.nes_collection.models.GameItemsIndex;
import uk.co.pixoveeware.nes_collection.models.GameListItems;

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

    ArrayList<GameItems> gameList;
    ArrayList<GameItemsIndex> indexList;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        gameList = viewM.GetGames("owned");
        indexList = viewM.GetIndex("owned");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_owned_games, container, false);
        gamelistView = v.findViewById(R.id.lvAllGames);
        alphaIndex = v.findViewById(R.id.lvAlphaIndex);

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
                        .add(R.id.container, GamesDetailFragment.newInstance(0, arg2))
                        .addToBackStack("gamesDetail")
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


        // Inflate the layout for this fragment
        return v;
    }
}