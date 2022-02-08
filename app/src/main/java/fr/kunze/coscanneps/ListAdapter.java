package fr.kunze.coscanneps;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListAdapter extends ArrayAdapter<Balise> {

    private LayoutInflater mInflater = null;

    public ListAdapter(Context context, int resource, List<Balise> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public Balise getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Viewholder holder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.listebalise, null);
            holder = new Viewholder();
            holder.nomBalise = (TextView) convertView.findViewById(R.id.titrebalise);
            holder.latitude = (TextView) convertView.findViewById(R.id.latitude);
            holder.longitude = (TextView) convertView.findViewById(R.id.longitude);

            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }
        Balise b = getItem(position);
        if (b != null) {

            holder.nomBalise.setText(b.getnomBalise());
            holder.latitude.setText("Latitude : " + b.getLatitude());
            holder.longitude.setText("Longitude : " + b.getLongitude());

        }

            return convertView;



    }

}
