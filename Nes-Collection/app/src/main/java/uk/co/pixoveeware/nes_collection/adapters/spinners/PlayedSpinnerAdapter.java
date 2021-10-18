package uk.co.pixoveeware.nes_collection.adapters.spinners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.pixoveeware.nes_collection.R;
import uk.co.pixoveeware.nes_collection.models.spinners.Spinner;

public class PlayedSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<Spinner> spinnerList;

    public PlayedSpinnerAdapter(Context context, List<Spinner> spinnerList){
        this.context = context;
        this.spinnerList = spinnerList;
    }

    @Override
    public int getCount() {
        return spinnerList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.spinner_custom, viewGroup, false);

        TextView txtName = rootView.findViewById(R.id.playedName);
        ImageView image = rootView.findViewById(R.id.playedImage);
        rootView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        txtName.setText(spinnerList.get(i).getName());
        image.setImageResource(spinnerList.get(i).getImage());

        return rootView;
    }
}
