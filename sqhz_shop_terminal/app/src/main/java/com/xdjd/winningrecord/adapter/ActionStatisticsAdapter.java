package com.xdjd.winningrecord.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.winningrecord.bean.ActivityPeriodStatsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ActionStatisticsAdapter extends BaseAdapter {

    List<ActivityPeriodStatsBean> cjList;//	日抽奖数列表
    List<ActivityPeriodStatsBean> zjList;//	日中奖数列表
    List<ActivityPeriodStatsBean> djList;

    @Override
    public int getCount() {
        return cjList == null?0:cjList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_action_statistics, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvDate.setText(cjList.get(i).getSc_date());
        holder.mTvParticipationNum.setText(cjList.get(i).getStatsNum()+"");
        holder.mTvWinningNum.setText(zjList.get(i).getStatsNum()+"");
        holder.mTvRedeemNum.setText(djList.get(i).getStatsNum()+"");
        return view;
    }

    public void setData(List<ActivityPeriodStatsBean> cjList,List<ActivityPeriodStatsBean> zjList,
            List<ActivityPeriodStatsBean> djList) {
        this.cjList = cjList;
        this.zjList = zjList;
        this.djList = djList;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.tv_participation_num)
        TextView mTvParticipationNum;
        @BindView(R.id.tv_winning_num)
        TextView mTvWinningNum;
        @BindView(R.id.tv_redeem_num)
        TextView mTvRedeemNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
