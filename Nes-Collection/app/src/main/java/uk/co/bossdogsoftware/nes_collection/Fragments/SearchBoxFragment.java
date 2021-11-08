package uk.co.bossdogsoftware.nes_collection.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.activities.Search;
import uk.co.bossdogsoftware.nes_collection.activities.SearchResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchBoxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchBoxFragment extends Fragment {

    String searchterm, fieldname, sql, searchname, wherestatement;
    int titles;
    private SearchListener mListener;

    Spinner field, namesearch;
    TextView gameName, searchTerm, seconddd;
    Button ok, cancel;
    CheckBox all;

    FragmentManager frg;

    ArrayAdapter<CharSequence> adapter, search;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchBoxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchBoxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchBoxFragment newInstance(String param1, String param2) {
        SearchBoxFragment fragment = new SearchBoxFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_box, container, false);
        gameName = v.findViewById(R.id.lblGame);
        field = v.findViewById(R.id.field_name);
        searchTerm = v.findViewById(R.id.txtSearch);//sets up the dialog title
        ok = v.findViewById(R.id.rgnOk);
        cancel = v.findViewById(R.id.rgnCancel);
        all = v.findViewById(R.id.chkAllRegions);
        namesearch = v.findViewById(R.id.field_selection);
        seconddd = v.findViewById(R.id.lblSubGenre);

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.searchArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        field.setAdapter(adapter);

        field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fieldname = (String) field.getSelectedItem();
                setfieldselection(i);
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//read the quantity from the quantity text box and adds one to the total
                if(fieldname.equals("Name")){searchterm = searchTerm.getText().toString().replaceAll("'", ""); }
                mListener.onReceiveSearch(fieldname.toLowerCase(), searchterm);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//read the quantity from the quantity text box and adds one to the total
                Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("searchFragment");
                FragmentManager manager = getActivity().getSupportFragmentManager();

               manager.beginTransaction()
                .remove(f)
               .commit();
                manager.popBackStack();
            }
        });

        fieldname = (String) field.getSelectedItem();
        fieldname = fieldname.toLowerCase();
        searchterm = searchTerm.getText().toString();

        mListener.onReceiveSearch(fieldname, searchterm);
        return v;
    }

    public void setfieldselection(int i) {

        switch (i){
            case 0:
                searchTerm.setEnabled(true);
                gameName.setAlpha(1f);
                searchTerm.setAlpha(1f);
                namesearch.setVisibility(View.INVISIBLE);
                seconddd.setVisibility(View.INVISIBLE);
                break;
            case 1:
                searchTerm.setEnabled(false);
                searchTerm.setAlpha(0.3f);
                gameName.setAlpha(0.3f);
                namesearch.setVisibility(View.VISIBLE);
                seconddd.setVisibility(View.VISIBLE);
                seconddd.setText("Publisher");
                search = ArrayAdapter.createFromResource(getActivity(),
                        R.array.publisherArray, android.R.layout.simple_spinner_item);
                search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                namesearch.setAdapter(search);
                break;
            case 2:
                searchTerm.setEnabled(false);
                searchTerm.setAlpha(0.3f);
                gameName.setAlpha(0.3f);
                namesearch.setVisibility(View.VISIBLE);
                seconddd.setVisibility(View.VISIBLE);
                seconddd.setText("Developer");
                search = ArrayAdapter.createFromResource(getActivity(),
                        R.array.developerArray, android.R.layout.simple_spinner_item);
                search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                namesearch.setAdapter(search);
                break;
            case 3:
                searchTerm.setEnabled(false);
                searchTerm.setAlpha(0.3f);
                gameName.setAlpha(0.3f);
                namesearch.setVisibility(View.VISIBLE);
                seconddd.setVisibility(View.VISIBLE);
                seconddd.setText("Genre");
                search = ArrayAdapter.createFromResource(getActivity(),
                        R.array.genreArray, android.R.layout.simple_spinner_item);
                search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                namesearch.setAdapter(search);
                break;
            case 4:
                searchTerm.setEnabled(false);
                searchTerm.setAlpha(0.3f);
                gameName.setAlpha(0.3f);
                namesearch.setVisibility(View.VISIBLE);
                seconddd.setVisibility(View.VISIBLE);
                seconddd.setText("Sub genre");
                search = ArrayAdapter.createFromResource(getActivity(),
                        R.array.subgenreArray, android.R.layout.simple_spinner_item);
                search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                namesearch.setAdapter(search);
                break;
            case 5:
                searchTerm.setEnabled(false);
                searchTerm.setAlpha(0.3f);
                gameName.setAlpha(0.3f);
                namesearch.setVisibility(View.VISIBLE);
                seconddd.setVisibility(View.VISIBLE);
                seconddd.setText("Year");
                search = ArrayAdapter.createFromResource(getActivity(),
                        R.array.yearArray, android.R.layout.simple_spinner_item);
                search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                namesearch.setAdapter(search);
                break;
            case 6:
                searchTerm.setEnabled(false);
                searchTerm.setAlpha(0.3f);
                gameName.setAlpha(0.3f);
                namesearch.setVisibility(View.INVISIBLE);
                seconddd.setVisibility(View.INVISIBLE);
                break;
        }

        namesearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchterm = (String) namesearch.getSelectedItem();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchListener) {
            mListener = (SearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SearchListener {                                       //the interface connecting the places search activity and the fragment for passing data between the fragment and the activity
        void onReceiveSearch(String searchType, String searchString);
    }
}