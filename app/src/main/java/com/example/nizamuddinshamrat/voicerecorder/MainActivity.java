package com.example.nizamuddinshamrat.voicerecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button stopPlayBtn,playRecordBtn,stopRecordBtn,recordBtn;
    public static final int PERMISSION_REQUEST_CODE = 1;
    MediaRecorder recorder;
    MediaPlayer mediaPlayer;
    String pathSave = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordBtn = findViewById(R.id.recordBtn);
        stopPlayBtn = findViewById(R.id.stopPlayBtn);
        stopRecordBtn = findViewById(R.id.stopRecordBtn);
        playRecordBtn = findViewById(R.id.playRecordBtn);

        requestForPermission();

        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/"+UUID.randomUUID().toString()+"audio_record_3gp";
        prepareMediaRecorder();

    }
    void prepareMediaRecorder(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(pathSave);
    }

    void requestForPermission(){
        if (ActivityCompat.checkSelfPermission(
                this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO
            },PERMISSION_REQUEST_CODE);
        }
    }

    public void startRecording(View view) {
        try {
            recorder.prepare();
            recorder.start();
            playRecordBtn.setEnabled(false);
            stopPlayBtn.setEnabled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordBtn.setEnabled(false);
        stopRecordBtn.setEnabled(true);
        Toast.makeText(this, "Start Recording.........", Toast.LENGTH_SHORT).show();
    }

    public void stopRecording(View view) {
        recorder.stop();
        playRecordBtn.setEnabled(true);
        stopRecordBtn.setEnabled(false);

    }

    public void playRecord(View view) {
        recordBtn.setEnabled(false);
        stopPlayBtn.setEnabled(true);
        stopRecordBtn.setEnabled(false);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(pathSave);
            mediaPlayer.prepare();
        }
        catch (Exception e){}

        mediaPlayer.start();
        playRecordBtn.setEnabled(false);
    }

    public void stopPlay(View view) {
       recordBtn.setEnabled(true);

        mediaPlayer.stop();
        mediaPlayer.release();
        stopPlayBtn.setEnabled(false);
        prepareMediaRecorder();
    }
}
