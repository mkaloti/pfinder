package com.example.jbt.placesfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import static android.content.Context.MODE_PRIVATE;


public class CustomAdapter extends ArrayAdapter<places> {
    SharedPreferences sp;

    // A CUSTOMER ADAPTER TO DISPLAY THE LIST ITEMS
    public CustomAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);//TO INFLATE THE LIST ITEMS
        sp = getContext().getSharedPreferences("DATA", MODE_PRIVATE);


        TextView name = (TextView) convertView.findViewById(R.id.title);
        TextView details = (TextView) convertView.findViewById(R.id.details);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);

        places p = getItem(position);
        name.setText(p.getTitle());
        details.setText(p.getDetails());
        if (sp.getBoolean("kilo", true)) {//IF THE SHARED PREFERENCE IS KILO SET TEXT TO KILOMETERS
            distance.setText(p.getDistance() + " kilometers");
        } else {//IF THE SHARED PREFERENCE IS KILO SET TEXT TO MILES
            distance.setText(p.getDistance() + " miles");
        }

        Ion.with(getContext())//TO LOAD THE IMAGE IN THE LIST
                .load(p.getImage().toString())
                .intoImageView(img);


        return convertView;

    }
}
