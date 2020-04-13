package com.xdjd.utils.recycleview;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xdjd.storebox.R;


/**
 * 作者： corer   时间： 2015/4/23.
 * 功能：帮助切换错误，数据为空，正在加载的页面
 * 修改：
 */
public class VaryViewHelper {
    /**
     * 切换不同视图的帮助类
     */
    private OverlapViewHelper mViewHelper;
    /**
     * 错误页面
     */
    private View mErrorView;
    /**
     * 正在加载页面
     */
    private View mLoadingView;
    /**
     * 数据为空的页面
     */
    private View mEmptyView;

    private View mEmptyCartView;

    /**
     * 正在加载页面的进度环
     */
//    private ProgressWheel mLoadingProgress;

    private ProgressBar progressBar;

       private VaryViewHelper(){}

    public VaryViewHelper(View view){
        this(new OverlapViewHelper(view));
    }

    private VaryViewHelper(OverlapViewHelper helper){
        this.mViewHelper =helper;
    }


    public void showErrorView(View.OnClickListener listener){
        showErrorView("", listener);
    }


    /**
     * <p>显示错误的页面</p>
     * @param tips  错误页面的提示语
     * @param listener 点击重新加载的监听
     */
    public void showErrorView(String tips, View.OnClickListener listener){
        if (mErrorView ==null){
            mErrorView = mViewHelper.inflate(R.layout.layout_errorview);
            mErrorView.setClickable(true);
        }


        if (!TextUtils.isEmpty(tips)){
            TextView tip= ((TextView) mErrorView.findViewById(R.id.error_tips_show));
            if (tip!=null){
                tip.setText(tips);
            }
        }

        if (listener!=null){
            View refresh=mErrorView.findViewById(R.id.error_refresh);
            if (refresh!=null){
                (refresh).setOnClickListener(listener);
            }
        }

        mViewHelper.showCaseLayout(mErrorView);

//        stopProgressLoading();

    }

    public void showErrorView(View view, View.OnClickListener listener){
        if (mErrorView==null){
            if (view==null){
                mErrorView = mViewHelper.inflate(R.layout.layout_errorview);
            }else {
                mErrorView=view;
            }

            mErrorView.setClickable(true);

        }

        if (listener!=null){
            View refresh=mErrorView.findViewById(R.id.error_refresh);
            if (refresh!=null){
                (refresh).setOnClickListener(listener);
            }
        }
        mViewHelper.showCaseLayout(mErrorView);
    }

    /**
     * 这是一个可以设置自定义的方法，用了这个之后不能调用其他的showLoadingView方法，因为还没写好
     * @param view
     */
    public void showLoadingView(View view){
        if (mLoadingView==null){
            if (view==null){
                mLoadingView = mViewHelper.inflate(R.layout.layout_loadingview);
            }else {
                mLoadingView=view;
            }
            mLoadingView.setClickable(true);
        }
        mViewHelper.showCaseLayout(mLoadingView);
    }

//    public void showLoadingView(){
//        showLoadingView("");
//    }

//    /**
//     * <p>显示正在加载的页面</p>
//     * @param tips 正在加载的提示语
//     */
//    public void showLoadingView(String tips){
//        if (mLoadingView ==null){
//            mLoadingView = mViewHelper.inflate(R.layout.layout_loadingview);
//            mLoadingView.setClickable(true);
//        }
//
//        if (mLoadingProgress ==null){
//            ProgressWheel progressWheel=(ProgressWheel) mLoadingView.findViewById(R.id.loading_progress_show);
//            if (progressWheel!=null){
//                mLoadingProgress =progressWheel;
//            }
//        }
//
//        if (!TextUtils.isEmpty(tips)){
//            TextView tip= (TextView) mLoadingView.findViewById(R.id.loading_tips_show);
//            if (tip!=null){
//                tip.setText(tips);
//            }
//        }
//
//
//        mViewHelper.showCaseLayout(mLoadingView);
//
//        startProgressLoading();
//    }

    /**
     * 显示加载中动画
     */
    public void  showLoadingView(){
        if (mLoadingView ==null){
            mLoadingView = mViewHelper.inflate(R.layout.loading_process_dialog_anim);
            mLoadingView.setClickable(true);
        }
        if (progressBar ==null){
            ProgressBar progressWheel=(ProgressBar) mLoadingView.findViewById(R.id.xlistview_footer_progressbar);
            if (progressWheel!=null){
                progressBar =progressWheel;
            }
        }

        mViewHelper.showCaseLayout(mLoadingView);

//        startProgressLoading();
    }

    public void showEmptyView(){
        showEmptyView("");
    }

    /**
     * 显示数据未空的页面
     * @param tips  数据为空的提示语
     */
    public void showEmptyView(String tips){
        if (mEmptyView ==null){
            mEmptyView = mViewHelper.inflate(R.layout.layout_emptyview);
            mEmptyView.setClickable(true);
        }

        if (!TextUtils.isEmpty(tips)){
            ((TextView) mEmptyView.findViewById(R.id.empty_tips_show)).setText(tips);
        }

        mViewHelper.showCaseLayout(mEmptyView);

//        stopProgressLoading();
    }

    public void showEmptyView( View.OnClickListener listener){
        if (mEmptyView==null){
            if (mEmptyView==null){
                mEmptyView = mViewHelper.inflate(R.layout.layout_emptyview);
            }
            mEmptyView.setClickable(true);
        }

        if (listener!=null){
            if (mEmptyView!=null){
                (mEmptyView).setOnClickListener(listener);
            }
        }
        mViewHelper.showCaseLayout(mEmptyView);
    }

    public void showEmptyView( String tips,View.OnClickListener listener){
        if (mEmptyView==null){
            if (mEmptyView==null){
                mEmptyView = mViewHelper.inflate(R.layout.layout_emptyview);
            }
            mEmptyView.setClickable(true);
        }

        if (!TextUtils.isEmpty(tips)){
            ((TextView) mEmptyView.findViewById(R.id.empty_tips_show)).setText(tips);
        }


        if (listener!=null){
            if (mEmptyView!=null){
                (mEmptyView).setOnClickListener(listener);
            }
        }
        mViewHelper.showCaseLayout(mEmptyView);
    }


    /**
     * <p>显示有数据的页面</p>
     */
    public void showDataView(){
        mViewHelper.restoreLayout();
        //stopProgressLoading();
    }


//    private void stopProgressLoading(){
//        if (mLoadingProgress !=null&& mLoadingProgress.isSpinning()){
//            mLoadingProgress.setInstantProgress(0.0f);
//            mLoadingProgress.stopSpinning();
//        }
//    }
//
//    private void startProgressLoading(){
//        if (mLoadingProgress !=null&&!mLoadingProgress.isSpinning()){
//            mLoadingProgress.spin();
//        }
//    }


    /**
     * 显示购物车数据未空的页面
     * @param tips  数据为空的提示语
     */
    public void showEmptyCartView(String tips,View.OnClickListener listener){
        if (mEmptyCartView ==null){
            mEmptyCartView = mViewHelper.inflate(R.layout.layout_cart_empty);
            mEmptyCartView.setClickable(true);
        }

        if (!TextUtils.isEmpty(tips)){
            ((TextView) mEmptyCartView.findViewById(R.id.empty_cart_tips_show)).setText(tips);
        }

        if (listener!=null){
            View toCart =  mEmptyCartView.findViewById(R.id.go_for_a_stroll);
            if (toCart!=null){
                toCart.setOnClickListener(listener);
            }
        }

        mViewHelper.showCaseLayout(mEmptyCartView);

        //        stopProgressLoading();
    }

    public void releaseVaryView(){
        try {
            mErrorView =null;
            mLoadingView =null;
            mEmptyView =null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
