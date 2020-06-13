package com.example.myapplication;

import android.content.Context;
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

public class RoutineAdapter2 extends RecyclerView.Adapter<RoutineAdapter2.ViewHolder>{

    private ArrayList<RoutinePlanItem> mdata = null;
    private Context mContext;
    private String deleteDate;
    private int DateInt;    // 유저가 선택한 날짜

    private long now = System.currentTimeMillis();
    private Date date = new Date(now);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    private String DateString = simpleDateFormat.format(date);
    int DateInt2 = Integer.parseInt(DateString);    // 오늘 날짜

    public RoutineAdapter2(ArrayList<RoutinePlanItem> mdata, String deleteDate, int DateInt) {
        this.deleteDate = deleteDate;
        this.mdata = mdata;
        this.DateInt = DateInt;
    }

    private RoutineAdapter2.OnItemClickListener mListener = null;

    // 커스텀 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View itemView, View v, int position);
    }

    // OnItemClickListener 객체를 전달하는 메서드
    public void setOnItemClickListener(RoutineAdapter2.OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RoutineAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view= inflater.inflate(R.layout.item_routine2, parent, false);
        RoutineAdapter2.ViewHolder vh =new RoutineAdapter2.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RoutineAdapter2.ViewHolder holder, final int position) {

        RoutinePlanItem item = mdata.get(position);

        if(DateInt < DateInt2) {
            holder.delete.setVisibility(View.GONE);
        }


        if(!item.isContainDay() || item.getDeleteDate().contains(deleteDate)){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
            params.width = 0;
            params.height = 0;
            holder.itemView.setLayoutParams(params);
        }
        else{
            item.setRegister(true);
        }

//        if(item.isDelete()){
//            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
//            param.width = 0;
//            param.height = 0;
//            holder.itemView.setLayoutParams(param);
//        }

        String startH, startM, endH, endM;
        startH = String.format( "%1$02d" , item.getStart_hour());
        startM = String.format( "%1$02d" , item.getStart_minute());
        endH = String.format( "%1$02d" , item.getEnd_hour());
        endM = String.format( "%1$02d" , item.getEnd_minute());


        holder.itemView.setBackgroundResource(R.drawable.border_routine);
        holder.title.setText(item.getTitle());
        holder.time.setText(startH + ":" + startM + "-" + endH + ":" + endM);
        holder.color.setBackgroundColor(item.getColor());
        holder.delete.setTag(holder.getAdapterPosition()); // 뷰홀더의 어댑터 포지션을 태그로 달아준다.
//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = (int)v.getTag(); // 달아뒀던 태그 값을 position 변수에 담는다.
//                RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
//                param.width = 0;
//                param.height = 0;
//                holder.itemView.setLayoutParams(param);
//                mdata.get(position).setDeleteDate(deleteDate); // 일과 등록창에서 루틴 삭제 시, 삭제 날짜를 flag로 달아준다.
////                mdata.get(position).setDelete(true);
//                notifyDataSetChanged();
//
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView color;
        Button delete;



        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.RTitle);

            time = itemView.findViewById(R.id.RTime);
            color = itemView.findViewById(R.id.RColor);
            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){

                        // 리스너 객체의 메서드 호출
                        if(mListener != null){
                            mListener.onItemClick(itemView, v, pos);
                        }

                    }
                }
            });
        }
    }
}
