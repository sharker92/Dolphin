package com.sharkstech.dolphin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharkstech.dolphin.R;
import com.sharkstech.dolphin.models.Beacons;

import java.util.List;

/**
 * Created by shark on 08/08/2017.
 */

public class BeaconAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private List<Beacons> list;

    public BeaconAdapter(Context context, int layout, List<Beacons> list){
        this.context = context;
        this.layout = layout;
        this.list = list;
    }
    @Override
    public int getCount(){ return list.size(); }

    @Override
    public Beacons getItem(int position) {return list.get(position);}

    @Override
    public long getItemId(int id) { return id;}

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;

        if (convertView == null) {
            // Sólo si está nulo, es decir, primera vez en ser renderizado, inflamos
            // y adjuntamos las referencias del layout en una nueva instancia de nuestro
            // ViewHolder, y lo insertamos dentro del convertView, para reciclar su uso

            convertView = LayoutInflater.from(context).inflate(layout, null);
            holder = new ViewHolder();

            holder.minor = (TextView) convertView.findViewById(R.id.textViewName);
            holder.Rssi = (TextView) convertView.findViewById(R.id.textViewRssi);
            holder.Tx = (TextView) convertView.findViewById(R.id.textViewTx);
            holder.distancia = (TextView) convertView.findViewById(R.id.textViewDst);
            holder.distancia2 = (TextView) convertView.findViewById(R.id.textViewDst2);
            holder.distancia3 = (TextView) convertView.findViewById(R.id.textViewDst3);

            convertView.setTag(holder);

        }else {
            // Obtenemos la referencia que posteriormente pusimos dentro del convertView
            // Y así, reciclamos su uso sin necesidad de buscar de nuevo, referencias con FindViewById
            holder = (ViewHolder) convertView.getTag();
        }

        final Beacons currentBeacon = getItem(position);
        holder.minor.setText(String.valueOf(currentBeacon.getMinor()));
        holder.Rssi.setText(String.valueOf(currentBeacon.getRssi()));
        holder.Tx.setText(String.valueOf(currentBeacon.getTxPower()));
        holder.distancia.setText(String.valueOf(String.format("%.5g%n",currentBeacon.getDistancia())));
        holder.distancia2.setText(String.valueOf(String.format("%.5g%n",currentBeacon.getDistancia2())));
        holder.distancia3.setText(String.valueOf(String.format("%.5g%n",currentBeacon.getDistancia3())));
        return convertView;
    }
    static class ViewHolder {
        private TextView minor;
        private TextView Rssi;
        private TextView Tx;
        private TextView distancia;
        private TextView distancia2;
        private TextView distancia3;
    }

}
