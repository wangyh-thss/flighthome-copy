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

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.ViewHolder>{
    private List<Flight> list;
    public FlightAdapter(List<Flight> list){
        this.list = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView leave_text;
        private TextView arrive_text;
        private TextView airline_text;
        private TextView leave_iata;
        private TextView arrive_iata;
        private TextView air_num;
        private Button detail;
        private Button save;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            leave_text=itemView.findViewById(R.id.leave_time);
            arrive_text = itemView.findViewById(R.id.arrive_time);
            airline_text= itemView.findViewById(R.id.airline);
            leave_iata = itemView.findViewById(R.id.leave_text);
            arrive_iata = itemView.findViewById(R.id.arrive_text);
            air_num = itemView.findViewById(R.id.airNum);
            detail = itemView.findViewById(R.id.button_detail);
            save = itemView.findViewById(R.id.button_save);

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.flight_iata = air_num.getText().toString();
                    MainActivity.time_dep=leave_text.getText().toString();
                    MainActivity.time_arr=arrive_text.getText().toString();
                    Intent settingsIntent = new Intent(context, FlightInfoActivity.class);
                    context.startActivity(settingsIntent);
                }
            });
        }
    }
    @NonNull
    @Override
    public FlightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_item,parent,false);
        return new FlightAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flight flight = list.get(position);
        holder.leave_text.setText(flight.getLeaveTime());
        holder.arrive_text.setText(flight.getArriveTime());
        holder.air_num.setText(flight.getFlightNum());
        holder.airline_text.setText(flight.getAirline());
        holder.leave_iata.setText(flight.getFromIata());
        holder.arrive_iata.setText(flight.getToIata());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
