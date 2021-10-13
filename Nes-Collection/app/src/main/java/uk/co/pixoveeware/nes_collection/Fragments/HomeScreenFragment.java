package uk.co.pixoveeware.nes_collection.Fragments;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.activities.HomeScreenActivity;

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
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        ImageButton AllGames = v.findViewById(R.id.btnAllGames);
        /*ImageButton NeededGames = getView().findViewById(R.id.btnneeded);
        ImageButton OwnedGames = getView().findViewById(R.id.btnowned);

        ImageButton FavouriteGames = getView().findViewById(R.id.btnFavouriteGames);
        ImageButton WishList = getView().findViewById(R.id.btnWishList);
        ImageButton ShelfOrder = getView().findViewById(R.id.btnShelfOrder);

        ImageButton Stats = getView().findViewById(R.id.btnStats);
        ImageButton Finished = getView().findViewById(R.id.btnFinished);
        ImageButton Settings = getView().findViewById(R.id.btnSettings);*/

        AllGames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ChangeFragment(AllGamesFragment.newInstance());
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, AllGamesFragment.newInstance())
                        .commitNow();
            }
        });

        return v;
    }

    public void ChangeFragment(Fragment frag){
        if (getView().findViewById(R.id.container) != null){
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .commitNow();
        }
    }

}