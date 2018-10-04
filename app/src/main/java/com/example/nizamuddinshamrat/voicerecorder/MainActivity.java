package com.example.nizamuddinshamrat.voicerecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

   /* Button stopPlayBtn,playRecordBtn,stopRecordBtn,recordBtn;
    public static final int PERMISSION_REQUEST_CODE = 1;
    MediaRecorder recorder;
    MediaPlayer mediaPlayer;
    String pathSave = "";*/
   ViewPager viewPager;
   TabLayout tabLayout;
   TabPagerAdapter tabPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestForPermission();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Recorder"));
        tabLayout.addTab(tabLayout.newTab().setText("Saved Records"));

        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(tabPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

      /*  recordBtn = findViewById(R.id.recordBtn);
        stopPlayBtn = findViewById(R.id.stopPlayBtn);
        stopRecordBtn = findViewById(R.id.stopRecordBtn);
        playRecordBtn = findViewById(R.id.playRecordBtn);

        requestForPermission();

        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/"+UUID.randomUUID().toString()+"audio_record_3gp";
        prepareMediaRecorder();*/

    }
    public class TabPagerAdapter extends FragmentPagerAdapter{

        int tabCount;
        public TabPagerAdapter(FragmentManager fm,int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new RecorderFragment();

                case 1:
                    return new SavedRecordFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }


    void requestForPermission(){
        if (ActivityCompat.checkSelfPermission(
                this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO
            },1);
        }
    }
/*

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
    }*/
}
