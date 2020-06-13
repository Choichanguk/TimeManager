package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class planIPTAdapter extends RecyclerView.Adapter<planIPTAdapter.ViewHolder> {
    private ArrayList<RoutinePlanItem> mdata= null;
    private int todayDate;

    public planIPTAdapter(ArrayList<RoutinePlanItem> mdata, int todayDate) {
        this.mdata = mdata;
        this.todayDate = todayDate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view= inflater.inflate(R.layout.item_planipt, parent, false);
        planIPTAdapter.ViewHolder vh =new planIPTAdapter.ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoutinePlanItem item = mdata.get(position);

        if(item.getIptDATE() == todayDate){
            holder.title.setTextColor(Color.RED);
        }



        String strDate = String.valueOf(item.getIptDATE());
        String DATE = String.format("%s.%s.%s", strDate.substring(0,4), strDate.substring(4,6), strDate.substring(6));
        Log.e("DATE", DATE);




        String startH, startM, endH, endM;
        startH = String.format( "%1$02d" , item.getStart_hour());
        startM = String.format( "%1$02d" , item.getStart_minute());
        endH = String.format( "%1$02d" , item.getEnd_hour());
        endM = String.format( "%1$02d" , item.getEnd_minute());

        holder.title.setText(item.getTitle());
        holder.time.setText(startH + ":" + startM + "-" + endH + ":" + endM);
        holder.date.setText(DATE);


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView date;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.PTitle);
            date = itemView.findViewById(R.id.planDate);
            time = itemView.findViewById(R.id.PTime);

        }
    }
}
