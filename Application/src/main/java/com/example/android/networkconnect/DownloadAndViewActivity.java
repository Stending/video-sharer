package com.example.android.networkconnect;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.android.networkconnect.filechooser.FileExplorerActivity;

import java.io.File;

public class DownloadAndViewActivity extends FragmentActivity implements DownloadCallback {
    private static String DEBUG = "COUCOU";

    // Reference to the TextView showing fetched data, so we can clear it with a button
    // as necessary.
    private VideoView mDataView;
    private String path = "https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
    private String defaultRepertory = "/data/user/0/com.example.android.networkconnect/cache/";
    private EditText mDLURLtextField ;
    private String tempFilePath;

    private Button mButtonToFileChooser;
    private Button mButtonToShareWithBluetoothActivity;

    private Intent mIntentFileChoose;
    private Intent mIntentShare;
    private View.OnClickListener clkLstnr =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.button_DL:
                    Intent mIntent = new Intent(getApplicationContext(), DownloadAndViewActivity.class);
                    startActivity(mIntent);
                    break;
                case R.id.button_STREAM:
                    Intent mIntentStream = new Intent(getApplicationContext(), GetStreamActivity.class);
                    startActivity(mIntentStream);
                    break;
                case R.id.button_filechooser:

                    startActivity(mIntentFileChoose);
                    break;

                case R.id.button_share_with_paired:
                    Log.d("COUCOU", "starting share activity");
                    startActivity(mIntentShare);
                    break;


            }

        }
    };

    // Keep a reference to the NetworkFragment which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dl_and_view_main);
        // Add playback controls.
        MediaController vidControl = new MediaController(this);
        // Set it to use the VideoView instance as its anchor.
        vidControl.setAnchorView(mDataView);
        // Set it as the media controller for the VideoView object.
        mDataView= (VideoView) findViewById(R.id.video);
        mDataView.setMediaController(vidControl);
        mIntentFileChoose = new Intent(getApplicationContext(), FileExplorerActivity.class);
        mIntentShare = new Intent(getApplicationContext(), ShareWithPairedDevicesActivity.class);
        mDLURLtextField = (EditText)findViewById(R.id.mediaURL);
        mDLURLtextField.setText(path);
//        mDataVideo = (VideoView) findViewById(R.id.video);

        mButtonToFileChooser = (Button) findViewById(R.id.button_filechooser);
        mButtonToFileChooser.setOnClickListener(clkLstnr);

        mButtonToShareWithBluetoothActivity = (Button) findViewById(R.id.button_share_with_paired);
        mButtonToShareWithBluetoothActivity.setOnClickListener(clkLstnr);

        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dl_and_view_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            case R.id.fetch_action:
                mNetworkFragment.setmUrlString(mDLURLtextField.getText().toString());
                startDownload();
                return true;
            // Clear the text and cancel download.
            case R.id.clear_action:
                finishDownloading();
                //File.delete(tempFilePath);
                mDLURLtextField.setText("");
                return true;

            case R.id.back_action:
                Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mIntent);
                return true;

            case R.id.clear_folder_action:
                clearFolder();
                return true;


        }
        return false;
    }

    private void clearFolder() {
        File folder = new File(defaultRepertory);
        for(File entry : folder.listFiles()){
            entry.delete();
        }
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }


    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            Log.d(DEBUG,result);
            tempFilePath = result;
            mDataView.setVideoPath(result);
            mDataView.start();
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                Log.d(DEBUG, String.valueOf(percentComplete));
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Log.d("COUCOU", "entering onResume");
        if(intent.getExtras()!=null){
            Log.d("COUCOU", "Extras not null");
            if(!intent.getStringExtra("GetFilePath").equals("")){
                Log.d("COUCOU", intent.getStringExtra("GetFilePath"));
                Toast.makeText(this, ("You are playing : "+intent.getStringExtra("GetFilePath")), Toast.LENGTH_SHORT).show();
                mDataView.setVideoPath(defaultRepertory+intent.getStringExtra("GetFilePath"));
                mDataView.start();
            }
        }

    }


}
