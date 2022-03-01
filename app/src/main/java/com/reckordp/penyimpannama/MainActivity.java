package com.reckordp.penyimpannama;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View vw = getLayoutInflater().inflate(R.layout.mengetik, findViewById(R.id.interaksi));
        ViewGroup.LayoutParams params = vw.getLayoutParams();
        params.width = getWindow().getAttributes().width;
        vw.setLayoutParams(params);
    }
}