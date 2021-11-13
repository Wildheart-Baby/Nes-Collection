package uk.co.bossdogsoftware.nes_collection.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.co.bossdogsoftware.nes_collection.R;
import uk.co.bossdogsoftware.nes_collection.data.statistics.BarChartView;
import uk.co.bossdogsoftware.nes_collection.data.statistics.DataColorSet;
import uk.co.bossdogsoftware.nes_collection.data.statistics.PieChartView;
import uk.co.bossdogsoftware.nes_collection.data.statistics.StatisticsDatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment implements PieChartView.Callback, BarChartView.Callback {

    StatisticsDatabaseHelper dbh;

    TextView PalALabel, PalBLabel, USLabel, CompleteLabel;
    LinearLayout keyContainer, keyContainerBar;
    PieChartView pieChartView;
    BarChartView barChartView;
    String gamesCost, gamesOwned;
    TextView gamesInfo, cost, palAData, palBData, usData, boxedGames, completeGames, looseCarts;
    ConstraintLayout AllData, PalAData, PalBData, UsData;
    int totalNumberOfGames;

    FragmentManager frg;

    String[] GenreNames, RegionNames;
    float[] DataPoints, RegionDataPoint;
    int[] PieColours, RegionBarColours;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         dbh = new StatisticsDatabaseHelper(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        GenreNames = dbh.GenreNames;
        PieColours = dbh.piecolours;
        DataPoints = dbh.datapoints;

        RegionNames = dbh.RegionNames;
        RegionDataPoint = dbh.RegionDataPoints;
        RegionBarColours = dbh.BarChartColours;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_statistics, container, false);
        AllData = v.findViewById(R.id.rlAllGames);
        PalAData = v.findViewById(R.id.rlPalA);
        PalBData = v.findViewById(R.id.rlPalB);
        UsData = v.findViewById(R.id.rlUS);

        PalALabel = v.findViewById(R.id.lblPalATitle);
        PalBLabel = v.findViewById(R.id.lblPalBTitle);
        USLabel = v.findViewById(R.id.lblUSTitle);
        CompleteLabel = v.findViewById(R.id.lblCompleteTitle);
        keyContainer = v.findViewById(R.id.key);
        pieChartView = v.findViewById(R.id.pie_chart);
        keyContainerBar = v.findViewById(R.id.key2);
        barChartView = v.findViewById(R.id.bar_chart);
        cost = v.findViewById(R.id.lblCost);
        gamesInfo = v.findViewById(R.id.lblGamesInfo);
        palAData = v.findViewById(R.id.lblPalA);
        palBData = v.findViewById(R.id.lblPalB);
        usData = v.findViewById(R.id.lblUS);

        boxedGames = v.findViewById(R.id.lblBoxedInfo);
        completeGames = v.findViewById(R.id.lblCompleteInfo);
        looseCarts = v.findViewById(R.id.lblLooseInfo);

        totalNumberOfGames = dbh.OwnedGameCount();
        if(totalNumberOfGames == 0){DisplayData(); AllData.setVisibility(View.GONE);} else {DisplayData();}
        return v;
    }

    public void DisplayData(){
        float totalcost = dbh.totalGamesCost();

        double avgCost = totalcost / totalNumberOfGames;
        //String avgCst = String.valueOf(avgCost);
        String avgCst = String.format("%.2f", avgCost);

        gamesCost = "You have spent " + dbh.getSettings().getCurrency() + String.format("%.2f", dbh.totalGamesCost()) + " " +
                getString(R.string.statsGamescost2) + " "+dbh.getSettings().getCurrency() + avgCst +
                "\n\n" + dbh.ReturnAllPrices("all");


        gamesOwned = getString(R.string.statsGamescost3) + " " + totalNumberOfGames + " " + dbh.GameOrGames() +
                getString(R.string.statsGamescost4) + " " + dbh.CollectionPercentage() +
                getString(R.string.statsGamescost5) + " "+ dbh.RegionSelected() +
                //getString(R.string.statsGamescost6) + " " + dbh.poppublisher +
                getString(R.string.statsGamescost7) + " " + dbh.getPopGenre() +
                getString(R.string.statsGamescost8) + " " + dbh.finishedGames + " " + getString(R.string.statsGamescost9)+
                "\nYou also have " + dbh.ownedBoxes() + " boxes and " + dbh.ownedManuals() + " manuals"+
                "\nYou own " + dbh.top100() + " of the top 100 games";

        cost.setText(gamesCost);

        gamesInfo.setText(gamesOwned);

        if(dbh.palAData()){
            palAData.setText(dbh.GetPalAData());
        } else{
            PalAData.setVisibility(View.GONE);
        }

        if(dbh.palBData()){
            palBData.setText(dbh.GetPalBData());
        } else{
            PalBData.setVisibility(View.GONE);
        }

        if(dbh.USData()){
            usData.setText(dbh.GetUSData());
        } else{
            UsData.setVisibility(View.GONE);
        }

        if (dbh.ShowPieChart()){
            pieChartView.setDataPoints(DataPoints, GenreNames, PieColours);
            // Because this activity is of the type PieChartView.Callback
            pieChartView.setCallback(this);
        }

        if (dbh.ShowBarChart()){
            barChartView.setDataPoints(RegionDataPoint, RegionNames, RegionBarColours);
            // Because this activity is of the type PieChartView.Callback
            barChartView.setCallback(this);
        }

        completeGames.setText(dbh.CompleteInBox());
        boxedGames.setText(dbh.BoxedGames());
        looseCarts.setText(dbh.LooseCarts());
    }

    @Override
    public void onDrawFinished(DataColorSet[] data) {
        // When the chart has finished drawing it will return the colors used
        // and the value along (for our key)
        if (keyContainer.getChildCount() > 0)
            keyContainer.removeAllViews(); // Empty all views if any found

        for (int i = 0; i < data.length; i++) {
            DataColorSet dataColorSet = data[i];

            LinearLayout keyItem = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.chart_key_item, null);
            LinearLayout colorView = keyItem.findViewById(R.id.color);
            TextView name = keyItem.findViewById(R.id.names);
            TextView valueView = keyItem.findViewById(R.id.value);

            colorView.setBackgroundColor(Color.parseColor("#" + dataColorSet.getColor()));
            name.setText("" + dataColorSet.getName() + "  ");
            String pievalue = String.format("%.0f",dataColorSet.getDataValue());
            valueView.setText(pievalue);

            // Add the key to the container
            keyContainer.addView(keyItem, i);
        }

    }

    @Override
    public void onSliceClick(DataColorSet data) {
            frg = getParentFragmentManager();
            frg.beginTransaction()
                    .add(R.id.container, SpecificInformationFragment.newInstance("genre", data.getName(), "owned") , "gamesList")
                    .addToBackStack("gamesList")
                    .commit();

    }

    @Override
    public void onDrawFinishedBar(DataColorSet[] data) {
        // When the chart has finished drawing it will return the colors used
        // and the value along (for our key)


        if (keyContainerBar.getChildCount() > 0)
            keyContainerBar.removeAllViews(); // Empty all views if any found

        for (int i = 0; i < data.length; i++) {
            DataColorSet dataColorSet = data[i];

            LinearLayout keyItem = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.chart_key_item, null);
            LinearLayout colorView = keyItem.findViewById(R.id.color);
            TextView name = keyItem.findViewById(R.id.names);
            TextView valueView = keyItem.findViewById(R.id.value);

            colorView.setBackgroundColor(Color.parseColor("#" + dataColorSet.getColor()));
            name.setText("" + dataColorSet.getName() + "  ");
            String pievalue = String.format("%.2f",dataColorSet.getDataValue())+"%";
            valueView.setText(pievalue);

            // Add the key to the container
            keyContainerBar.addView(keyItem, i);
        }

    }

    /*@Override
    public void onBarClick(DataColorSet data) {
        //frg = getParentFragmentManager();
        //frg.beginTransaction()
        //        .add(R.id.container, SpecificInformationFragment.newInstance("genre", data.getName()), "gamesList")
        //        .commit();
    }*/


}