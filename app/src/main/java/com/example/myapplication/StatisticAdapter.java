package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {
    private ArrayList<RoutinePlanItem> mdata = null;
    private String deleteDate;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_statistic, parent, false);
        StatisticAdapter.ViewHolder vh = new StatisticAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RoutinePlanItem RPItem = mdata.get(position);


        if(RPItem.getAtt().equals("R")){
            if(!RPItem.isContainDay() || RPItem.getDeleteDate().contains(deleteDate)){
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
                params.width = 0;
                params.height = 0;
                holder.itemView.setLayoutParams(params);
            }

        }



        // plan이 중요일과에 등록되어 있는 plan일 때
        if(RPItem.getIsYES().equals("YES")){
            holder.title.setTextColor(Color.RED);
        }
        else{
            holder.title.setTextColor(Color.BLACK);
        }



//        // plan이 완료했을 때
//        if(RPItem.isFinish()){
//            holder.start.setVisibility(View.GONE);
//            holder.stop.setVisibility(View.GONE);
//            holder.finish.setVisibility(View.GONE);
//            holder.isFinish.setVisibility(View.VISIBLE);
//        }


        int planMin = (RPItem.getEnd_hour()*60 + RPItem.getEnd_minute()) - (RPItem.getStart_hour()*60 + RPItem.getStart_minute());
        int realMin = (RPItem.getTime() / 60);
        Log.e("Adapter", String.valueOf(realMin));
            holder.title.setText(RPItem.getTitle());
            holder.planTime.setText(planMin + "분");
            holder.realTime.setText(realMin+"분");

            if((planMin - realMin) > 0){
                holder.differ.setTextColor(Color.BLUE);
                holder.differ.setText("-" + Math.abs((planMin - realMin)) + "분");
            }
            else{
                holder.differ.setTextColor(Color.RED);
                holder.differ.setText("+" +  Math.abs((planMin - realMin)) + "분");
            }

        if(RPItem.isFinish()){
            holder.current.setText("완료");
        }
        else if(RPItem.isClick()){
            holder.current.setText("측정 중");
        }
        else{
            holder.current.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }





    public StatisticAdapter(ArrayList<RoutinePlanItem> mdata, String deleteDate) {
        this.deleteDate = deleteDate;
        this.mdata = mdata;


    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, planTime, realTime, differ, current;





        public ViewHolder(@NonNull final View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.title);
            planTime = itemView.findViewById(R.id.planTime);
            realTime = itemView.findViewById(R.id.realTime);
            differ = itemView.findViewById(R.id.differ);
            current = itemView.findViewById(R.id.current);



        }
    }
}
