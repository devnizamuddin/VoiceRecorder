package com.example.nizamuddinshamrat.voicerecorder;


import android.app.AlertDialog;
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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RecorderFragment extends Fragment {

    private Context context;
    private MediaRecorder recorder;
    private Button recordButton;
    private TextView recordUserHint;
    private Chronometer recordingTimeMeter;
    private boolean recording = false;
    private File f;

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
        String folder_main = "Voice Recorder";
        f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            //folder doesn't exist
            f.mkdirs();
        } else {
            Toast.makeText(context, "Ok" + f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
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
                        recordingTimeMeter.start();
                        recording = true;
                        recordButton.setBackgroundResource(R.drawable.stop_recording);
                        recordUserHint.setBackgroundResource(R.drawable.stop_recording_text);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Start Recording.........", Toast.LENGTH_SHORT).show();
                } else {
                    // if recording is already running
                    //record audio by
                    recorder.stop();
                    recorder.release();
                    recordingTimeMeter.stop();

                    recording = false;
                    recordButton.setBackgroundResource(R.drawable.start_recording);
                    recordUserHint.setBackgroundResource(R.drawable.start_recording_text);
                    Toast.makeText(context, "Recording Finish", Toast.LENGTH_SHORT).show();
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

        String savePath = f.getAbsolutePath()
                + "/" + currentTime + "voice_recorder_3gp";
        return savePath;
    }

}
