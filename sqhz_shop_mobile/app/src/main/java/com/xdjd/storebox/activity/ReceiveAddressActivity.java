package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2016/12/1.
 */

public class ReceiveAddressActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    List<AddressInfoActivity> list = new ArrayList<AddressInfoActivity>();
    @BindView(R.id.receive_address_list)
    ListView receiveAddressList;
    @BindView(R.id.delete_address)
    Button deleteAddress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_address);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("收货地址");
        titleBar.setRightText("取消");
        titleBar.setRightTextColor(R.color.color_EC193A);
        /*InitAddress();
        AddressInfoAdapter adapter = new AddressInfoAdapter(ReceiveAddressActivity.this,
                R.layout.address_item, list,this);
        receiveAddressList.setAdapter(adapter);
        receiveAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Fruit fruit = fruitList .get(i);
                startActivity(ModifyReceiveAddress.class);
            }
        });*/
    }

    private void InitAddress() {
        /*AddressInfoActivity hong = new AddressInfoActivity("小洪", "13163307039", "北京");
        list.add(hong);
        AddressInfoActivity hong1 = new AddressInfoActivity("小洪", "13163307039", "北京");
        list.add(hong1);
        AddressInfoActivity hong2 = new AddressInfoActivity("小洪", "13163307039", "北京");
        list.add(hong2);
        AddressInfoActivity hong3 = new AddressInfoActivity("小洪", "13163307039", "北京");
        list.add(hong3);
        AddressInfoActivity hong4 = new AddressInfoActivity("小洪", "13163307039", "北京");
        list.add(hong4);
        AddressInfoActivity hong5 = new AddressInfoActivity("小洪", "13163307039", "北京");
        list.add(hong5);
        AddressInfoActivity hong6 = new AddressInfoActivity("小洪", "13163307039", "北京");
        list.add(hong6);
        AddressInfoActivity hong7 = new AddressInfoActivity("小洪", "13163307039", "北京");
        list.add(hong7);*/
    }



    @OnClick({R.id.delete_address})
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.delete_address:break;
        }
    }
}
