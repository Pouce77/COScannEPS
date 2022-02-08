package fr.kunze.coscanneps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListAdapterResultats extends ArrayAdapter<Resultats> {
    private LayoutInflater mInflater = null;

    public ListAdapterResultats(Context context, int resource, List<Resultats> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public Resultats getItem(int position) {
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

        ViewHolder3 holder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.listersultatsadapter, null);
            holder = new ViewHolder3();
            holder.textNomBalise = (TextView) convertView.findViewById(R.id.textNomBalise);
            holder.textTemps=(TextView)convertView.findViewById(R.id.textTemps);
            holder.textVitesse=(TextView)convertView.findViewById(R.id.textVitesse);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder3) convertView.getTag();
        }
        Resultats r = getItem(position);
        if (r != null) {

            holder.textNomBalise.setText(r.getNomBalise());
            holder.textTemps.setText(r.getTempsBalise());
            holder.textVitesse.setText(r.getVitesse()+" km/h");
            if (holder.textNomBalise.getText().toString().matches("Erreur")) {
                holder.textNomBalise.setTextColor(Color.RED);
            }else if (holder.textNomBalise.getText().toString().contains("passée")){
                holder.textNomBalise.setTextColor(Color.RED);
            }else{
                holder.textNomBalise.setTextColor(Color.rgb(0,180,0));
            }

        }

        return convertView;



    }

}
