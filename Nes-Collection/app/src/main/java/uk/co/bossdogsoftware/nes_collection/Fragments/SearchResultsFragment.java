package uk.co.bossdogsoftware.nes_collection.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.bossdogsoftware.nes_collection.adapters.NesCollectionAdapter;
import uk.co.bossdogsoftware.nes_collection.adapters.NesIndexAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameItemsIndex;
import uk.co.bossdogsoftware.nes_collection.models.GameListItems;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends Fragment {

    AllGamesViewModel viewM;
    ListView gamelistView, alphaIndex;

    ArrayList<AllGameItems> gameList;
    ArrayList<GameItemsIndex> indexList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultsFragment newInstance(String param1, String param2) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_results, container, false);
        //searchString("","");
        gamelistView = v.findViewById(R.id.lvAllGames);
        alphaIndex = v.findViewById(R.id.lvAlphaIndex);

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
                        .addToBackStack("gamesDetail"+mParam2)
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

    public void searchString(String searchType, String searchString){
        gameList = viewM.GetSpecificGames(searchType, searchString, "");
        indexList = viewM.GetSpecificGamesIndex(searchType, searchString, "");
        gamelistView.setAdapter(new NesCollectionAdapter(getContext(), gameList));
        alphaIndex.setAdapter(new NesIndexAdapter(getContext(), indexList));
    }
}