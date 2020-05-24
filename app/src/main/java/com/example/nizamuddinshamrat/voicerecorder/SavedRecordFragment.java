package com.example.nizamuddinshamrat.voicerecorder;


import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class SavedRecordFragment extends Fragment implements RecordAdapter.ClickListener {


    //View Item
    private RecyclerView recyclerView;
    private Button playPreviousBtn, playRecordBtn, playNextBtn;
    private TextView playingRecordNameTv;
    private SeekBar seekBar;
    private LinearLayout player_layout;

    //Data item
    private boolean playing = false;
    private ArrayList<RecordPOJO> recordList;
    private int recordListPosition;

    //Other Object
    private MediaPlayer mediaPlayer;
    private RecordAdapter recordAdapter;
    private Runnable runnable;
    private Handler handler;

    public SavedRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_record, container, false);

        //View Item
        recyclerView = view.findViewById(R.id.recordListRv);
        playPreviousBtn = view.findViewById(R.id.playPreviousBtn);
        playRecordBtn = view.findViewById(R.id.playRecordBtn);
        playNextBtn = view.findViewById(R.id.playNextBtn);
        playingRecordNameTv = view.findViewById(R.id.playingRecordNameTv);
        seekBar = view.findViewById(R.id.seekBar);
        player_layout = view.findViewById(R.id.player_layout);

        //Other Object
        handler = new Handler();

        //getting all recording information
        recordList = getAllRecordInformation();

        //Creating ReyclerView
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recordAdapter = new RecordAdapter(getActivity(), recordList, this);
        recyclerView.setAdapter(recordAdapter);


        //Recoding player button start...............
        playRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    //not paling
                    if (String.valueOf(recordListPosition).isEmpty()) {
                        Toast.makeText(getActivity(), "Nothing to Play", Toast.LENGTH_SHORT).show();
                    } else {
                        RecordPOJO recordPOJO = recordList.get(recordListPosition);
                        playRecord(recordPOJO);
                        playRecordBtn.setBackgroundResource(R.drawable.stop_music_button);
                    }
                } else {
                    stopPlaySong();
                }
            }
        });

        playNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!String.valueOf(recordListPosition).isEmpty() && recordListPosition + 1 < recordList.size()) {
                    if (playing) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }

                    recordListPosition = recordListPosition + 1;
                    RecordPOJO recordPOJO = recordList.get(recordListPosition);
                    playRecord(recordPOJO);
                }
            }
        });

        playPreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!String.valueOf(recordListPosition).isEmpty() && recordListPosition > 0) {
                    if (playing) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }

                    recordListPosition = recordListPosition - 1;
                    RecordPOJO recordPOJO = recordList.get(recordListPosition);
                    playRecord(recordPOJO);
                }


            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (playing) {
                        mediaPlayer.seekTo(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //..................... recoding button finished

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ArrayList<RecordPOJO> recordPOJOS = new ArrayList<>();
            recordPOJOS = getAllRecordInformation();
            recordAdapter.updateRecordList(recordPOJOS);
        }
    }

    private ArrayList<RecordPOJO> getAllRecordInformation() {


        ArrayList<File> files = new ArrayList<>();
        files = getAllRecordedFile();

        //Get RecordList Data form Voice Recorder
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        recordList = new ArrayList<>();

        //Add into recordList
        for (File file : files) {
            //Get Duration
            mmr.setDataSource(file.getAbsolutePath());
            int millieSecond = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            String duration = formatTime(millieSecond);
            //get name
            String name = file.getName();
            //get size
            double recordSizeDouble = file.length();
            String recordSize = formatSize(Integer.valueOf((int) recordSizeDouble));

            String path = file.getAbsolutePath();
            RecordPOJO recordPOJO = new RecordPOJO(name, recordSize, duration, path);
            recordList.add(recordPOJO);

        }
        //Release metadata class
        mmr.release();
        return recordList;
    }

    private ArrayList<File> getAllRecordedFile() {

        //Getting all recorded file from file manager
        String folder_main = "Sound Recorder";
        File rootFile = new File(Environment.getExternalStorageDirectory(), folder_main);
        ArrayList<File> files = new ArrayList<>();

        try {
            File[] fileses = rootFile.listFiles();
            for (File file : fileses) {
                files.add(file);
            }


        } catch (Exception E) {

        }
        return files;
    }


    private String formatTime(int millisecond) {

        int sec = Integer.valueOf(millisecond) / 1000;
        if (sec > 59) {
            int minute = (sec / 60);
            int minuteSec = sec - (minute * 60);
            return String.valueOf(minute + "m" + " " + minuteSec + "s");

        } else {
            return String.valueOf(sec + "s");
        }
    }

    private String formatSize(int sizeByte) {

        if (sizeByte > 1023) {
            int kb = sizeByte / 1024;
            if (kb > 1023) {
                int mb = kb / 1024;
                int kbMb = kb - mb * 1024;
                return String.valueOf(mb + "." + kbMb + " Mb");
            } else {
                return String.valueOf(kb + " Kb");
            }
        } else {
            return String.valueOf(sizeByte + " Byte");
        }
    }

    @Override
    public void onClickRecordItem(int position) {

        if (playing) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        player_layout.setVisibility(LinearLayout.VISIBLE);
        recordListPosition = position;
        RecordPOJO recordPOJO = recordList.get(position);
        playRecord(recordPOJO);


    }

    private void playRecord(RecordPOJO recordPOJO) {
        //Play Music

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recordPOJO.getRecordPath());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    mediaPlayer.start();
                    changeSeekBar();
                }
            });
        } catch (Exception e) {

        }
        //Change Layout

        playing = true;
        playingRecordNameTv.setText(recordPOJO.getRecordName());
        playRecordBtn.setBackgroundResource(R.drawable.stop_music_button);
        //After complete Record Play


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                stopPlaySong();
                seekBar.setProgress(0);
            }
        });

    }

    private void changeSeekBar() {


        if (playing) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }

    }

    private void stopPlaySong() {

        mediaPlayer.stop();
        mediaPlayer.release();
        playing = false;
        playRecordBtn.setBackgroundResource(R.drawable.start_play_song_button);
    }


}
