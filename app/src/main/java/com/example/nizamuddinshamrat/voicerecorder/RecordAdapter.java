package com.example.nizamuddinshamrat.voicerecorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private ArrayList<RecordPOJO> recordPOJOS;
    private ClickListener clickListener;


    public RecordAdapter(Context context, ArrayList<RecordPOJO> recordPOJOS, ClickListener clickListener) {
        this.context = context;
        this.recordPOJOS = recordPOJOS;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.record_single_layout,parent,false);

        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {

        RecordPOJO recordPOJO = recordPOJOS.get(position);

        holder.recordingNameTv.setText(recordPOJO.getRecordName());
        holder.recordingDurationTv.setText(recordPOJO.getRecordDuration());
        holder.recordingSizeTv.setText(recordPOJO.getRecordSize());
        holder.recordingOptionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });


    }

    @Override
    public int getItemCount() {
        return recordPOJOS.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {

        TextView recordingNameTv,recordingDurationTv,recordingSizeTv;
        ImageView recordingOptionTv;

        public RecordViewHolder(View itemView) {
            super(itemView);

            recordingNameTv = itemView.findViewById(R.id.recordingNameTv);
            recordingDurationTv = itemView.findViewById(R.id.recordingDurationTv);
            recordingSizeTv = itemView.findViewById(R.id.recordingSizeTv);
            recordingOptionTv = itemView.findViewById(R.id.recordingOptionTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClickRecordItem(getAdapterPosition());
                }
            });

        }
    }
    void updateRecordList(ArrayList<RecordPOJO>recordPOJOS){
        this.recordPOJOS = recordPOJOS;
        notifyDataSetChanged();
    }
    interface ClickListener{
        void onClickRecordItem(int position);
    }

    private void showPopup(View view){

        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.rename_menu:

                        Toast.makeText(context, "Rename Clicked", Toast.LENGTH_SHORT).show();


                        return true;

                    case R.id.delete_menu:

                        Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                       return true;

                    case R.id.share_menu:

                        return true;

                    default:
                        return false;

                }

            }
        });
        popupMenu.inflate(R.menu.popup_menu);

        popupMenu.show();

    }
}
