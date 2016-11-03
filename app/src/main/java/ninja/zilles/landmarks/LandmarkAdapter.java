package ninja.zilles.landmarks;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static android.R.id.list;

/**
 * Created by zilles on 11/3/16.
 */

public class LandmarkAdapter extends ArrayAdapter<Landmark> {
    // List<Landmark> list;


//    public LandmarkAdapter(Context context, int resource, List<Landmark> objects) {
//        super(context, resource, objects);
////        list = objects;
//    }

    public LandmarkAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.listitem, null);
        }

//        Landmark landmark = list.get(position);
        Landmark landmark = getItem(position);

        TextView name = (TextView)v.findViewById(R.id.landmarkNameTextView);
        TextView type = (TextView)v.findViewById(R.id.landmarkTypeTextView);

        if (name != null) {
            name.setText(landmark.getLandmarkName());
        }
        if (type != null) {
            type.setText(landmark.getTypeOfLandmark());
        }

        if ((position & 1) == 0) {
            v.setBackgroundColor(Color.BLUE); // color);
        } else {
            int orange = Color.rgb(0xff, 0x7f, 0);
            v.setBackgroundColor(orange);
        }

        return v;
    }
}
