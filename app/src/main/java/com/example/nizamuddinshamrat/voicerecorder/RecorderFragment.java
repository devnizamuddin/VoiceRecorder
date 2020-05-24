package com.example.nizamuddinshamrat.voicerecorder;


import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RecorderFragment extends Fragment {

    //view Item
    private Button recordButton;
    private TextView recordUserHint;
    private Chronometer recordingTimeMeter;

    //Data
    private boolean recording = false;
    private File file;

    //other Object
    private Context context;
    private MediaRecorder recorder;


    public RecorderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recording, container, false);

        //Find id form layout
        recordButton = view.findViewById(R.id.recordBtn);
        recordingTimeMeter = view.findViewById(R.id.recordingTimeMeter);
        recordUserHint = view.findViewById(R.id.recordUserHint);


        context = getActivity();


        //Create Folder
        String folder_main = "Sound Recorder";
        file = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!file.exists()) {
            //folder doesn't exist
            file.mkdirs();
        }

        //On Click Record button
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording) {
                    // if recording is not already running
                    //record audio
                    prepareMediaRecorder();
                    try {
                        recorder.prepare();
                        recorder.start();
                        recordingTimeMeter.setBase(SystemClock.elapsedRealtime());

                        //View changes
                        recordingTimeMeter.start();
                        recording = true;
                        recordButton.setBackgroundResource(R.drawable.stop_recording);
                        recordUserHint.setText("Press the button to stop record");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // if recording is already running
                    //record audio by
                    recorder.stop();
                    recorder.release();
                    recordingTimeMeter.stop();

                    //View changing
                    recordingTimeMeter.setBase(SystemClock.elapsedRealtime());
                    recording = false;
                    recordButton.setBackgroundResource(R.drawable.start_recording);
                    recordUserHint.setText("Press the button to stop record");
                }
            }
        });


        return view;
    }

    private void prepareMediaRecorder() {

        //test
        //prepare recorder for record new file
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getRecordingPath());
    }

    private String getRecordingPath() {

        //Get Current Time And date
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss_yyyy MMM dd");
        String currentTime = sdf.format(date);

        String savePath = file.getAbsolutePath()
                + "/" + currentTime + "_voice_recorder_3gp";
        return savePath;
    }

}
