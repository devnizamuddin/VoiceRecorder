package com.example.nizamuddinshamrat.voicerecorder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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
    public void onBindViewHolder(@NonNull RecordViewHolder holder, final int position) {

        final RecordPOJO recordPOJO = recordPOJOS.get(position);

        holder.recordingNameTv.setText(recordPOJO.getRecordName());
        holder.recordingDurationTv.setText(recordPOJO.getRecordDuration());
        holder.recordingSizeTv.setText(recordPOJO.getRecordSize());
        holder.recordingOptionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view,recordPOJO.getRecordPath(),position);

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

    private void showPopup(View view, final String recordPath, final int position){

        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                File selectedFile = new File(recordPath);

                switch (menuItem.getItemId()){

                    case R.id.rename_menu:

                        if (selectedFile.exists()){
                            createRenameDialog(selectedFile,position);
                        }

                        return true;

                    case R.id.delete_menu:

                        deleteSelectedFile(selectedFile,position);

                       return true;

                    case R.id.share_menu:

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("audio/3gp");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(selectedFile.getAbsolutePath()));
                        context.startActivity(Intent.createChooser(intent,"Share audio clip"));

                        return true;

                    default:
                        return false;

                }

            }
        });
        popupMenu.inflate(R.menu.popup_menu);

        popupMenu.show();

    }

    private void createRenameDialog(final File selectedFile, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.rename_dialog_layout,null,false);

        final EditText nameEt = view.findViewById(R.id.nameEt);
        Button cancel_btn = view.findViewById(R.id.cancel_btn);
        Button ok_btn = view.findViewById(R.id.ok_btn);

        builder.setView(view);
        final AlertDialog alertDialog = builder.show();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString();
                 renameFile(selectedFile,position,name);
                 alertDialog.cancel();
            }
        });


    }

    private void renameFile(File selectedFile, int position, String name) {

        String folder_main = "Sound Recorder";
        File file = new File(Environment.getExternalStorageDirectory(), folder_main);
        file = new File(file.getAbsolutePath(),name);
        boolean rename =selectedFile.renameTo(file);
        if (rename){
            RecordPOJO recordPOJO = recordPOJOS.get(position);
            recordPOJO.setRecordName(name);
            recordPOJOS.set(position,recordPOJO);
            updateRecordList(recordPOJOS);
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Problem", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteSelectedFile(File selectedFile, int position) {

        if (selectedFile.exists()){

            selectedFile.delete();
            recordPOJOS.remove(position);
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
        else {
            Toast.makeText(context, "File not exists", Toast.LENGTH_SHORT).show();
        }

    }
}
