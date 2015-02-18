package mx.edu.cicese.alejandro.voicehelper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.widget.CardScrollAdapter;

import java.util.ArrayList;

/**
 * Created by Alejandro on 2/18/15.
 */
public class MyCardScrollAdapter extends CardScrollAdapter {
    ArrayList<View> viewArrayList;

    public MyCardScrollAdapter() {
        this.viewArrayList = new ArrayList<View>();
    }
    public MyCardScrollAdapter(ArrayList<View> views) {
        this.viewArrayList = views;
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

        if (viewArrayList.indexOf(item)>=0)
            return viewArrayList.indexOf(item);
        else
            return AdapterView.INVALID_POSITION;
    }

    public boolean addView (View view){
        return viewArrayList.add(view);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
