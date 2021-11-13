package uk.co.bossdogsoftware.nes_collection.Fragments;

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

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.bossdogsoftware.nes_collection.adapters.GameCollectionAdapter;
import uk.co.bossdogsoftware.nes_collection.adapters.GameIndexAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameItemsIndex;
import uk.co.bossdogsoftware.nes_collection.models.GameListItems;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpecificInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecificInformationFragment extends Fragment {

    AllGamesViewModel viewM;
    ListView gamelistView, alphaIndex;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    public SpecificInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment SpecificInformationFragment.
     */
    // TODO: Rename and change types and number of parameters

    ArrayList<AllGameItems> gameList;
    ArrayList<GameItemsIndex> indexList;

    public static SpecificInformationFragment newInstance(String param1, String param2, String param3) {
        SpecificInformationFragment fragment = new SpecificInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
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
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        gameList = viewM.GetSpecificGames(mParam1, mParam2, mParam3);
        indexList = viewM.GetSpecificGamesIndex(mParam1, mParam2, mParam3);
        //SetTitles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_specific_information, container, false);

        gamelistView = v.findViewById(R.id.lvAllGames);
        alphaIndex = v.findViewById(R.id.lvAlphaIndex);
        //GameCollectionAdapter nes = new GameCollectionAdapter(getContext(), gameList);//set up an new list adapter from the arraylist
        gamelistView.setAdapter(new GameCollectionAdapter(getContext(), gameList));
        alphaIndex.setAdapter(new GameIndexAdapter(getContext(), indexList));

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

                GameListItems gameListItems = (GameListItems) arg0.getItemAtPosition(arg2);//get the position of the item on the list
                final Integer itemId = gameListItems.getItemId();//get the item id

                return true;//return is equal to true
            }

        });
        // Inflate the layout for this fragment
        return v;
    }

    private void SetTitles(){
        getActivity().setTitle(" " + mParam1.substring(0, 1).toUpperCase() + mParam1.substring(1) + ": ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(" " + viewM.SpecificTitle(mParam2));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(getContext().getResources().getIdentifier(viewM.convertLogoTitle(mParam1, mParam2), "drawable", getContext().getPackageName()));
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

    /*private String convertLogoTitle(String title){
        switch(title){
            case "Action-Adventure":
                title = "action_adventure";
                break;
            default:
                title = title.toLowerCase();
                break;
        }
        return title;
    }*/
}