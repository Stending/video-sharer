/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.networkconnect;


import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.view.Menu;

import android.view.View;
import android.widget.Button;


import com.example.android.networkconnect.filechooser.FileExplorerActivity;

/**
 * Sample Activity demonstrating how to connect to the network and fetch raw
 * HTML. It uses a Fragment that encapsulates the network operations on an AsyncTask.
 *
 * This sample uses a TextView to display output.
 */
public class MainActivity extends FragmentActivity  {

    private Button mButtonToDownLoadActivity;
    private Button mButtonToGetStreamingActivity;
    private Button mButtonToBlueToothActivity;

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
                    Intent mIntentFileChoose= new Intent(getApplicationContext(), FileExplorerActivity.class);
                    startActivity(mIntentFileChoose);
                    break;

                case R.id.button_BT:
                    Intent mIntentBT= new Intent(getApplicationContext(), BlueToothActivity.class);
                    startActivity(mIntentBT);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mButtonToDownLoadActivity = (Button) findViewById(R.id.button_DL);
        mButtonToDownLoadActivity.setOnClickListener(clkLstnr);
        mButtonToGetStreamingActivity = (Button) findViewById(R.id.button_STREAM);
        mButtonToGetStreamingActivity.setOnClickListener(clkLstnr);
        mButtonToBlueToothActivity = (Button) findViewById(R.id.button_BT);
        mButtonToBlueToothActivity.setOnClickListener(clkLstnr);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



}
