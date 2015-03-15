package mx.edu.cicese.alejandro.voicehelper.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.widget.CardScrollAdapter;

import java.util.ArrayList;

/**
 * Created by Alejandro on 2/18/15.
 */
public class CustomCardScrollAdapter extends CardScrollAdapter {
    ArrayList<View> viewArrayList;

    public CustomCardScrollAdapter() {
        this.viewArrayList = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return viewArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return viewArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return viewArrayList.get(position);
    }

    @Override
    public int getPosition(Object item) {

        if (viewArrayList.indexOf(item) >= 0)
            return viewArrayList.indexOf(item);
        else
            return AdapterView.INVALID_POSITION;
    }

    public boolean addView(View view) {
        return viewArrayList.add(view);
    }

}
