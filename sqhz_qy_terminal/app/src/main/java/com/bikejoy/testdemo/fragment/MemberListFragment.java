package com.bikejoy.testdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by lijipei on 2019/10/31.
 * 开发中/正式  客户列表
 */

public class MemberListFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_list, container, false);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {

    }
}
