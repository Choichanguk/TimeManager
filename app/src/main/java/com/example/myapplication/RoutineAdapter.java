package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.PrecomputedText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {
    private ArrayList<RoutinePlanItem> mdata= null;
    private Context mContext;
    private  OnItemClickListener mListener = null; // 리스너 객체를 저장하는 변수

    // 커스텀 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    // OnItemClickListener 객체를 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


// --------------------------------------------------------------------------------------------------
    public RoutineAdapter(Context context, ArrayList<RoutinePlanItem> mdata) {
        this.mdata = mdata;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view= inflater.inflate(R.layout.item_routine, parent, false);
        RoutineAdapter.ViewHolder vh =new RoutineAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final RoutinePlanItem item = mdata.get(position);
            String startH, startM, endH, endM;
            startH = String.format( "%1$02d" , item.getStart_hour());
            startM = String.format( "%1$02d" , item.getStart_minute());
            endH = String.format( "%1$02d" , item.getEnd_hour());
            endM = String.format( "%1$02d" , item.getEnd_minute());



            holder.itemView.setBackgroundResource(R.drawable.border_routine);


            holder.title.setText(item.getTitle());
            holder.day.setText(item.getDay());
            holder.time.setText(startH + ":" + startM + "-" + endH + ":" + endM);

            holder.color.setBackgroundColor(item.getColor());

            holder.delete.setTag(holder.getAdapterPosition()); // 뷰홀더의 어댑터 포지션을 태그로 달아준다.
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int)v.getTag(); // 달아뒀던 태그 값을 position 변수에 담는다.
//                    oneDayRoutine.remove(position);
                    mdata.remove(position);
                    notifyDataSetChanged();


                }
            });




    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView day;
        TextView time;
        TextView color;
        Button delete;
        Button edit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.RTitle);
            day = itemView.findViewById(R.id.RDay);
            time = itemView.findViewById(R.id.RTime);
            color = itemView.findViewById(R.id.RColor);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

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

        }
    }
}
