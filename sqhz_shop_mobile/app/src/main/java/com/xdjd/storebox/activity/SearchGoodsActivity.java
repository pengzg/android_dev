package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/13
 *     desc   : 搜索商品activity
 *     version: 1.0
 * </pre>
 */

public class SearchGoodsActivity extends BaseActivity {
    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;

    private String hint;//搜索框提示语

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_search_goods);
        ButterKnife.bind(this);

//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        String searchStr = getIntent().getStringExtra("searchStr");
        hint = getIntent().getStringExtra("hint");
        if (hint!=null && !"".equals(hint)){
            mEtSearch.setHint(hint);
        }
        mEtSearch.setText(searchStr);
        mEtSearch.setSelection(searchStr.length());

        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    mEtSearch.setSelection(mEtSearch.getText().length());
                    mEtSearch.addTextChangedListener(mTextWatcher);
                    if (mEtSearch.getText().length()>0){
                        mLlClear.setVisibility(View.VISIBLE);
                    }else{
                        mLlClear.setVisibility(View.GONE);
                    }
                }else{
                    mEtSearch.removeTextChangedListener(mTextWatcher);
                }
            }
        });
    }

    @OnClick({R.id.left_layout, R.id.tv_search,R.id.ll_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finishActivity();
                break;
            case R.id.tv_search:
                Intent intent = new Intent();
                intent.putExtra("searchStr",mEtSearch.getText().toString());
                setResult(100,intent);
                finishActivity();
                break;
            case R.id.ll_clear:
                mEtSearch.setText("");
                break;
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length()>0){
                mLlClear.setVisibility(View.VISIBLE);
            }else{
                mLlClear.setVisibility(View.GONE);
            }
        }
    };
}
