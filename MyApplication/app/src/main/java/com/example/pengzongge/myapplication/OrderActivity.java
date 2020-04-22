package com.example.pengzongge.myapplication;

import android.os.Bundle;

import com.example.pengzongge.myapplication.base.BaseActivity;

import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        ButterKnife.bind(this);
    }
}
