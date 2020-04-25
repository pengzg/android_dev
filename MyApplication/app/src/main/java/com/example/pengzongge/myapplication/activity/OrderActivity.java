package com.example.pengzongge.myapplication.activity;

import android.os.Bundle;

import com.example.pengzongge.myapplication.R;
import com.example.pengzongge.myapplication.base.BaseActivity;

import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        ButterKnife.bind(this);
    }

    private void getOrderList(){
//        OkHttpClicent okHttpClient=new OkHttpClient();

//        Request request=new Request.Builder().url(url).build();
//
//        Call call=okHttpClient.newCall(request);



    }

}
