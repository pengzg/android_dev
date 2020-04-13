package com.xdjd.storebox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.IntegralGoodsDetailActivity;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.view.verticalslide.CustWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/3/1.
 */

public class VerticalIntegralGoodsWebFragment extends BaseFragment {
    private View progressBar;
    private boolean hasInited = false;

    @BindView(R.id.goods_detail_web)
    CustWebView webview;

    private String goodsDesc = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vertical_web,container ,false);
        progressBar = view.findViewById(R.id.progressbar);
        ButterKnife.bind(this,view);
        return view;
    }

    public void initView() {
        goodsDesc = ((IntegralGoodsDetailActivity)getActivity()).getFragmentDetail().goodsDesc;

        if (goodsDesc!=null && !goodsDesc.equals("")) {
            String star1="&lt;";
            String star2="&gt;";
            String star3="&quot;";
            String getHtml = goodsDesc;
            String htmlstr=getHtml.replaceAll(star1,"<");
            String h2=htmlstr.replaceAll(star2,">");
            String h3=h2.replaceAll(star3,"'");
            webview.loadDataWithBaseURL(null,"<html><head><meta " +
                            "name='viewport' " +
                            "content='width=device-width,initial-scale=1,minimum-scale=1,maximum-scal=1'/>" +
                            "</head><body>"+h3+"</body></html>",
                    "text/html", "UTF-8",null);
        } else {
            String customHtml = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' " +
                    "'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
                    "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='zh-CN' dir='ltr'><head>" +
                    "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /><title></title>" +
                    "<meta name='keywords' content='webview Android'/></head><body style='text-align:center'>没有详情内容！</body></html>";
            webview.loadDataWithBaseURL(null,customHtml,
                    "text/html", "UTF-8",null);
        }

        webview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != webview && !hasInited) {
                    hasInited = true;
                    progressBar.setVisibility(View.GONE);
                    //webview.loadUrl("http://m.zol.com/tuan/");
                }

            }
        },1000);
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
    }
}
