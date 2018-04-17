package com.hik.apigatephonedemo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchRecordingActivity extends AppCompatActivity {
    private String indexcode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recording);
        Intent intent = getIntent();
        indexcode = intent.getStringExtra(MainActivity.INDEXCODE);
        Fragment f = RecordingListFragment.newInstance(indexcode);
        getSupportFragmentManager().beginTransaction().replace(R.id.recordingListFragment,f).commit();
    }
}
