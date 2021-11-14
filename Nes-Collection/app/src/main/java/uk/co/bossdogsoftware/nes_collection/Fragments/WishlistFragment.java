package uk.co.bossdogsoftware.nes_collection.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.ViewModels.AllGamesViewModel;
import uk.co.bossdogsoftware.nes_collection.adapters.GameCollectionAdapter;
import uk.co.bossdogsoftware.nes_collection.adapters.GameIndexAdapter;
import uk.co.bossdogsoftware.nes_collection.models.AllGameItems;
import uk.co.bossdogsoftware.nes_collection.models.GameItemsIndex;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishlistFragment extends Fragment {

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

    public WishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishlistFragment newInstance(String param1, String param2) {
        WishlistFragment fragment = new WishlistFragment();
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
        frg = getParentFragmentManager();
        viewM = new ViewModelProvider(requireActivity()).get((AllGamesViewModel.class));
        gameList = viewM.GetGames("wishlist");
        indexList = viewM.GetIndex("wishlist");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wishlist, container, false);
        gamelistView = v.findViewById(R.id.lvAllGames);
        alphaIndex = v.findViewById(R.id.lvAlphaIndex);
        //GameCollectionAdapter nes = new GameCollectionAdapter(getContext(), gameList);//set up an new list adapter from the arraylist
        gamelistView.setAdapter(new GameCollectionAdapter(getContext(), gameList));
        alphaIndex.setAdapter(new GameIndexAdapter(getContext(), indexList));
        return v;
    }
}