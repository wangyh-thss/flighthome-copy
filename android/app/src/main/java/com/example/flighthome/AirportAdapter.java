package com.example.flighthome;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.ViewHolder>{
    private List<Airport> list;
    public AirportAdapter(List<Airport> list){
        this.list = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_text;
        private TextView iata_text;
        private Button choose;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            name_text=itemView.findViewById(R.id.airport_name);
            iata_text = itemView.findViewById(R.id.airportiata);
            choose = itemView.findViewById(R.id.button_choose);
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.isArrival){
                        MainActivity.airport_arr = iata_text.getText().toString();
                        MainActivity.airport_arr_name = name_text.getText().toString();
                        Intent settingsIntent = new Intent(context, SearchActivity.class);
                        context.startActivity(settingsIntent);
                    }else{
                        MainActivity.airport_dep = iata_text.getText().toString();
                        MainActivity.airport_dep_name = name_text.getText().toString();
                        Intent settingsIntent = new Intent(context, ArrAirActivity.class);
                        context.startActivity(settingsIntent);
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public AirportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.airport_item,parent,false);
        return new AirportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AirportAdapter.ViewHolder holder, int position) {
        Airport airport = list.get(position);
        holder.name_text.setText(airport.getName());
        holder.iata_text.setText(airport.getIata());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

