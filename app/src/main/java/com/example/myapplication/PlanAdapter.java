package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
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

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private ArrayList<RoutinePlanItem> mdata = null;
    private Context mContext;
    private PlanAdapter.OnItemClickListener mListener = null; // 리스너 객체를 저장하는 변수

    private int DateInt;    // 유저가 선택한 날짜
    private long now = System.currentTimeMillis();
    private Date date = new Date(now);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    private String DateString = simpleDateFormat.format(date);
    int DateInt2 = Integer.parseInt(DateString);    // 오늘 날짜

    // 커스텀 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    // OnItemClickListener 객체를 전달하는 메서드
    public void setOnItemClickListener(PlanAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }



    public PlanAdapter(Context context, ArrayList<RoutinePlanItem> mdata, int DateInt) {
        this.mdata = mdata;
        this.mContext = context;
        this.DateInt = DateInt;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView ipt;
        TextView time;
        TextView color;
        Button edit;
        Button delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.PTitle);
            ipt = itemView.findViewById(R.id.PItp);
            time = itemView.findViewById(R.id.PTime);
            color = itemView.findViewById(R.id.PColor);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){

                        // 리스너 객체의 메서드 호출
                        if(mListener != null){
                            mListener.onItemClick(v, pos);
                        }

                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){

                        // 리스너 객체의 메서드 호출
                        if(mListener != null){
                            mListener.onItemClick(v, pos);
                        }

                    }
                }
            });


        }
    }

    @NonNull
    @Override
    public PlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view= inflater.inflate(R.layout.item_plan, parent, false);
        PlanAdapter.ViewHolder vh = new  PlanAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.ViewHolder holder, int position) {
        RoutinePlanItem item = mdata.get(position);

        if(DateInt < DateInt2){
            holder.delete.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
        }

        if(item.getIsYES().equals("YES")){
            holder.title.setTextColor(Color.RED);
        }
        else{
            holder.title.setTextColor(Color.BLACK);
        }

        String startH, startM, endH, endM;
        startH = String.format( "%1$02d" , item.getStart_hour());
        startM = String.format( "%1$02d" , item.getStart_minute());
        endH = String.format( "%1$02d" , item.getEnd_hour());
        endM = String.format( "%1$02d" , item.getEnd_minute());

        if (item.getAtt().equals("R")) {
            holder.itemView.setBackgroundResource(R.drawable.border_routine);
        }

        else if (item.getAtt().equals("A")) {
            holder.itemView.setBackgroundResource(R.drawable.border_a);
        }

        else if (item.getAtt().equals("B")) {
            holder.itemView.setBackgroundResource(R.drawable.border_b);
        }

        else if (item.getAtt().equals("C")) {
            holder.itemView.setBackgroundResource(R.drawable.border_c);
        }

        holder.title.setText(item.getTitle());
        holder.time.setText(startH + ":" + startM + "-" + endH + ":" + endM);
        holder.ipt.setText(item.getIpt());
        holder.color.setBackgroundColor(item.getColor());


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
