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

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private ArrayList<RoutinePlanItem> mdata = null;
    private Context mContext;
    private String deleteDate;

    private RecordAdapter.OnItemClickListener mListener = null; // 리스너 객체를 저장하는 변수


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_record, parent, false);
        RecordAdapter.ViewHolder vh = new RecordAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RoutinePlanItem RPItem = mdata.get(position);

        int sec = RPItem.getTime() % 60;
        int min = (RPItem.getTime() / 60) % 60;
        int hour = (RPItem.getTime() / 3600) % 24;
        String result = String.format("%02d:%02d:%02d",hour, min, sec);

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

        // ---------------------------------------------------------------------------- //

        // 시간 측정 버튼을 눌렀을 때
//        if(RPItem.isClick()){
//            holder.start.setVisibility(View.GONE);
//            holder.stop.setVisibility(View.VISIBLE);
//        }
//        else if(!RPItem.isClick()){
//            holder.start.setVisibility(View.VISIBLE);
//            holder.stop.setVisibility(View.GONE);
//        }
//        else if(RPItem.isFinish()){
//            holder.start.setVisibility(View.GONE);
//            holder.stop.setVisibility(View.GONE);
//            holder.finish.setVisibility(View.GONE);
//            holder.isFinish.setVisibility(View.VISIBLE);
//        }
//        else if(!RPItem.isFinish()){
//            holder.start.setVisibility(View.VISIBLE);
//            holder.stop.setVisibility(View.VISIBLE);
//            holder.finish.setVisibility(View.VISIBLE);
//            holder.isFinish.setVisibility(View.GONE);
//        }


        if(RPItem.isClick() && RPItem.isFinish()){
            holder.start.setVisibility(View.GONE);
            holder.stop.setVisibility(View.GONE);
            holder.finish.setVisibility(View.GONE);
            holder.isFinish.setVisibility(View.VISIBLE);
        }
        else if(!RPItem.isClick() && RPItem.isFinish()){
            holder.start.setVisibility(View.GONE);
            holder.stop.setVisibility(View.GONE);
            holder.finish.setVisibility(View.GONE);
            holder.isFinish.setVisibility(View.VISIBLE);
        }
        else if(!RPItem.isClick() && !RPItem.isFinish()){
            holder.start.setVisibility(View.VISIBLE);
            holder.stop.setVisibility(View.GONE);
            holder.finish.setVisibility(View.VISIBLE);
            holder.isFinish.setVisibility(View.GONE);
        }
        else if(RPItem.isClick() && !RPItem.isFinish()){
            holder.start.setVisibility(View.GONE);
            holder.stop.setVisibility(View.VISIBLE);
            holder.finish.setVisibility(View.VISIBLE);
            holder.isFinish.setVisibility(View.GONE);

        }


//        // plan이 완료했을 때
//        if(RPItem.isFinish()){
//            holder.start.setVisibility(View.GONE);
//            holder.stop.setVisibility(View.GONE);
//            holder.finish.setVisibility(View.GONE);
//            holder.isFinish.setVisibility(View.VISIBLE);
//        }
//        else{
//            holder.start.setVisibility(View.VISIBLE);
//            holder.stop.setVisibility(View.VISIBLE);
//            holder.finish.setVisibility(View.VISIBLE);
//            holder.isFinish.setVisibility(View.GONE);
//        }



            String startH, startM, endH, endM;
            startH = String.format( "%1$02d" , RPItem.getStart_hour());
            startM = String.format( "%1$02d" , RPItem.getStart_minute());
            endH = String.format( "%1$02d" , RPItem.getEnd_hour());
            endM = String.format( "%1$02d" , RPItem.getEnd_minute());

            holder.title.setText(RPItem.getTitle());
            holder.color.setBackgroundColor(RPItem.getColor());
            holder.time.setText(startH + ":" + startM + "-" + endH + ":" + endM);
            holder.timer.setText(result);

            if (RPItem.getAtt().equals("R")) {
                holder.itemView.setBackgroundResource(R.drawable.border_routine);
            }

            else if (RPItem.getAtt().equals("A")) {
                holder.itemView.setBackgroundResource(R.drawable.border_a);
            }

            else if (RPItem.getAtt().equals("B")) {
                holder.itemView.setBackgroundResource(R.drawable.border_b);
            }

            else if (RPItem.getAtt().equals("C")) {
                holder.itemView.setBackgroundResource(R.drawable.border_c);
            }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    // 커스텀 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View itemView, View v, int position);
    }

    // OnItemClickListener 객체를 전달하는 메서드
    public void setOnItemClickListener(RecordAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    public RecordAdapter(Context mContext, ArrayList<RoutinePlanItem> mdata, String deleteDate) {
        this.deleteDate = deleteDate;
        this.mdata = mdata;
        this.mContext = mContext;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView color, title, time, timer, isFinish;
        ImageButton start, stop;
        Button finish;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            color = itemView.findViewById(R.id.color);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            timer = itemView.findViewById(R.id.timer);
            start = itemView.findViewById(R.id.start);
            stop = itemView.findViewById(R.id.stop);
            finish = itemView.findViewById(R.id.finish);
            isFinish = itemView.findViewById(R.id.isFinish);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos =getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(itemView, v, pos);
                        }
                    }
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos =getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(itemView, v, pos);
                        }
                    }
                }
            });

            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos =getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(itemView, v, pos);
                        }
                    }
                }
            });


        }
    }
}
