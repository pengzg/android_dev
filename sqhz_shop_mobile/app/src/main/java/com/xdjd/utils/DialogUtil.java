package com.xdjd.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.LoginBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自定义仿iOS弹出框效果
 * Created by lijipei on 2016/7/15.
 */
public class DialogUtil {

    public interface MyCustomDialogListener {

        void ok(String str);

        void no();

    }

    public interface MyCustomDialogListener2 {

        void ok();

        void no();

    }

    public interface MyCustomDialogListener3 {

        void sure();

    }

    public interface MyCustomDialogListener4 {
        void item(int i);
    }

    /**
     * 显示编辑弹出框
     *
     * @param context
     * @param message
     * @param ok
     * @param no
     * @param content
     * @param minNumStr 商品起订量
     * @param gp_addnum 商品增量
     * @param goodStock 商品库存
     * @param o
     */
    public static void showEditCartNum(Context context, final String message, String ok, String no, String content,
                                       final String minNumStr, final String gp_addnum, final String goodStock,final String goods_type,
                                       final MyCustomDialogListener o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_edit_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView title = (TextView) view.findViewById(R.id.dialog_message);
        final EditText edit = (EditText) view.findViewById(R.id.dialog_edit);
        edit.setText(content);
        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line = view.findViewById(R.id.custom_line);
        View line2 = view.findViewById(R.id.dialog_line2);

        if (edit != null) {
            //设置可获得焦点
            edit.setFocusable(true);
            edit.setFocusableInTouchMode(true);
            //请求获得焦点
            edit.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) edit
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(edit, 0);
        }

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText() == null || edit.getText().toString().equals("")) {
                    UIUtils.Toast("请输入购买商品数量");
                    return;
                }
                if (edit.getText().length()>10){
                    UIUtils.Toast("超出限制数量范围");
                    return;
                }
                int minNum = 0;//最小起订量
                int stockNum = 0;//库存
                int addnum = 1;//最小增量
                int goodNum = Integer.parseInt(edit.getText().toString());
                try{
                    minNum = Integer.parseInt(minNumStr);
                    stockNum = Integer.parseInt(goodStock);
                    if(gp_addnum.equals("0")){
                        addnum = Integer.parseInt("1");
                    }else{
                        addnum = Integer.parseInt(gp_addnum);
                    }
                }catch (Exception e){
                    addnum = 1;
                }
                if (goodNum < minNum && goodNum != 0){
                    UIUtils.Toast("小于起订数量");
                    return;
                }
                if ((goodNum > stockNum) && goods_type.equals("1")){
                    UIUtils.Toast("数量超出库存范围");
                    return;
                }
                int editNum = goodNum - minNum;//输入数量小于最小起增量时,进行四舍五入计算加入购物车数量并进行提示
                if (editNum > 0 && (editNum % addnum) != 0){
                    //UIUtils.Toast("每次最少增加"+addnum+"件商品");
                    //return;
                    editNum = goodNum - (editNum % addnum);
                    edit.setText(String.valueOf(editNum));
                    UIUtils.Toast("只支持增量倍数修改,系统为您调整为:"+String.valueOf(editNum));
                }
                dialog.dismiss();
                if (o != null) {
                    o.ok(edit.getText().toString());
                }
            }
        });
        no_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (o != null) {
                    o.no();
                }
            }
        });
        if (ok == null) {
            ok_tx.setVisibility(View.GONE);
            line2.setVisibility(View.INVISIBLE);
        }
        if (no == null) {
            no_tx.setVisibility(View.GONE);
            line2.setVisibility(View.INVISIBLE);
        }
        if (ok == null && no == null) {
            linlin.setVisibility(View.GONE);
            line.setVisibility(View.INVISIBLE);
        }
        title.setText(message);
        if (ok != null) {
            ok_tx.setText(ok);
        }
        if (no != null) {
            no_tx.setText(no);
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().addContentView(
                view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
        showAnimationCustom(view);
    }

    /**
     * 显示键盘
     *
     * @param edit
     */
    public static void showKeyboard(EditText edit) {
        if (edit != null) {
            //设置可获得焦点
            edit.setFocusable(true);
            edit.setFocusableInTouchMode(true);
            //请求获得焦点
            edit.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) edit
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(edit, 0);
        }
    }


    public static void showCustomDialog(Context context, final String message, String ok, String no,
                                        final MyCustomDialogListener2 o) {
        showCustomDialog(context, null, message, ok, no, o);
    }

    public static void showCustomDialog(Context context, String titleStr, final String message, String ok, String no,
                                        final MyCustomDialogListener2 o) {
        showCustomDialog(context, titleStr, message, ok, no, o,true,true);
    }


    /**
     * 显示带标题的提示框
     *
     * @param context
     * @param message
     * @param ok
     * @param no
     * @param o
     */
    public static void showCustomDialog(Context context, String titleStr, final String message, String ok, String no,
                                        final MyCustomDialogListener2 o,boolean isOutside,boolean isCancelable) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        TextView title = (TextView) view.findViewById(R.id.dialog_message);
        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line = view.findViewById(R.id.custom_line);
        View line2 = view.findViewById(R.id.dialog_line2);
        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (o != null) {
                    o.ok();
                }
            }
        });
        no_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (o != null) {
                    o.no();
                }
            }
        });
        if (ok == null) {
            ok_tx.setVisibility(View.GONE);
            line2.setVisibility(View.INVISIBLE);
        }
        if (no == null) {
            no_tx.setVisibility(View.GONE);
            line2.setVisibility(View.INVISIBLE);
        }
        if (ok == null && no == null) {
            linlin.setVisibility(View.GONE);
            line.setVisibility(View.INVISIBLE);
        }

        if (ok != null && no == null){
            ok_tx.setBackgroundColor(UIUtils.getColor(R.color.transparent));
            ok_tx.setTextColor(UIUtils.getColor(R.color.text_black_212121));
        }

        title.setText(message);

        if (titleStr == null) {
            //如果是null,隐藏标题栏
            dialog_title.setVisibility(View.GONE);
        } else if (titleStr.equals("")) {
            //空串默认是--提示
        } else {
            dialog_title.setText(titleStr);
        }
        //        if (dialog_title != null && !dialog_title.getText().equals("")){
        //            dialog_title.setText(titleStr);
        //        }
        if (ok != null) {
            ok_tx.setText(ok);
        }
        if (no != null) {
            no_tx.setText(no);
        }
        // progressDialog.setMessage(msg);

        dialog.setCanceledOnTouchOutside(isOutside);
        dialog.setCancelable(isCancelable);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().addContentView(
                view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
        showAnimationCustom(view);
    }

    /**
     * 只有提示功能的提示框
     *
     * @param context
     * @param message
     * @param no
     */
    public static void showSureDialog(Context context, final String message, String no, final MyCustomDialogListener3 listener) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mysure_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView title = (TextView) view.findViewById(R.id.dialog_message);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line = view.findViewById(R.id.custom_line);

        no_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.sure();
            }
        });

        if (no == null) {
            no_tx.setVisibility(View.GONE);
        }

        title.setText(message);

        if (no != null) {
            no_tx.setText(no);
        }

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().addContentView(
                view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
        showAnimationCustom(view);
    }

    /**
     * 显示版本提示功能框
     *
     * @param context
     * @param updateType string	1.强制更新;2.选择性更新
     * @param message
     * @param ok
     * @param no
     * @param o
     */
    public static void showVersionDialog(Context context, final String updateType, String version, final String message, String ok, String no,
                                         final MyCustomDialogListener2 o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.myversion_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
            }
        });

        TextView title_tv = (TextView) view.findViewById(R.id.title_tv);
        TextView content = (TextView) view.findViewById(R.id.dialog_message);

        content.setMovementMethod(new ScrollingMovementMethod());

        ImageView myversion_img = (ImageView) view.findViewById(R.id.myversion_img);
        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line = view.findViewById(R.id.custom_line);
        View line2 = view.findViewById(R.id.dialog_line2);

        LinearLayout selectLinear = (LinearLayout) view.findViewById
                (R.id.select_linear);
        TextView selectTv = (TextView) view.findViewById(R.id.select_tv);
        TextView versionName = (TextView) view.findViewById(R.id.dialog_version_name);

        versionName.setText("最新版本: V" + version);

        if (updateType.equals("1")) {
            selectLinear.setVisibility(View.GONE);
            selectTv.setVisibility(View.VISIBLE);
            myversion_img.setVisibility(View.VISIBLE);
            selectTv.setText("" +
                    "立即更新");
            title_tv.setText("软件更新提示");
        } else if (updateType.equals("2")) {
            selectLinear.setVisibility(View.VISIBLE);
            selectTv.setVisibility(View.GONE);
            myversion_img.setVisibility(View.VISIBLE);
            title_tv.setText("软件更新提示");
        } else if (updateType.equals("0")) {
            selectLinear.setVisibility(View.GONE);
            selectTv.setVisibility(View.VISIBLE);
            myversion_img.setVisibility(View.GONE);
            selectTv.setText("立即安装");
            title_tv.setText("安装包已准备就绪");
        }

        selectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateType.equals("2") || updateType.equals("0")) {
                    dialog.dismiss();
                }

                if (o != null) {
                    o.ok();
                }
            }
        });

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateType.equals("2")) {
                    dialog.dismiss();
                }
                if (o != null) {
                    o.ok();
                }
            }
        });
        no_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (updateType.equals("2")) {
                    dialog.dismiss();
                }
                if (o != null) {
                    o.no();
                }
            }
        });
        if (ok == null) {
            ok_tx.setVisibility(View.GONE);
            line2.setVisibility(View.INVISIBLE);
        }
        if (no == null) {
            no_tx.setVisibility(View.GONE);
            line2.setVisibility(View.INVISIBLE);
        }
        if (ok == null && no == null) {
            linlin.setVisibility(View.GONE);
            line.setVisibility(View.INVISIBLE);
        }
        content.setText(message);
        if (ok != null) {
            ok_tx.setText(ok);
        }
        if (no != null) {
            no_tx.setText(no);
        }
        // progressDialog.setMessage(msg);

        dialog.setCanceledOnTouchOutside(true);
        if (updateType.equals("1")) {
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().addContentView(
                view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();

        //        showAnimationCustom(view);
    }


    private static void showAnimationCustom(final View view) {
        ObjectAnimator oo = ObjectAnimator.ofFloat(view, "uuu", 0.5f, 1f);
        oo.setDuration(200);
        oo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                view.setScaleX(value);
                view.setScaleY(value);
                view.setAlpha(value);
            }

            ;
        });
        oo.setInterpolator(new DecelerateInterpolator());
        oo.start();
    }


    private static MyAdapter adapter = new MyAdapter();

    /**
     * 数据列表dialog
     *
     * @param context
     * @param titleStr
     * @param list
     * @param o
     */
    public static void showDialogList(Context context, String titleStr, List<LoginBean> list, final MyCustomDialogListener4 o) {
        adapter.setData(list);

        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_dialog_list_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        ListView lvList = (ListView) view.findViewById(R.id.lv_list);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line = view.findViewById(R.id.custom_line);
        View line2 = view.findViewById(R.id.dialog_line2);

        lvList.setAdapter(adapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                o.item(i);
            }
        });

        if (titleStr == null) {
            //如果是null,隐藏标题栏
            dialog_title.setVisibility(View.GONE);
        } else if (titleStr.equals("")) {
            //空串默认是--提示
        } else {
            dialog_title.setText(titleStr);
        }
        //        if (dialog_title != null && !dialog_title.getText().equals("")){
        //            dialog_title.setText(titleStr);
        //        }
        // progressDialog.setMessage(msg);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().addContentView(
                view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
        showAnimationCustom(view);
    }

    public static class MyAdapter extends BaseAdapter {

        List<LoginBean> list;

        public void setData(List<LoginBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_show_dialog_list, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mTvName.setText(list.get(i).getOrgid_nameref());
            if(list.get(i).getOrgid_content() != null && !list.get(i).getOrgid_content().equals("")){
                holder.mTvDesc.setVisibility(View.VISIBLE);
                holder.mTvDesc.setText("("+list.get(i).getOrgid_content()+")");
            }else{
                holder.mTvDesc.setVisibility(View.GONE);
            }
            if(UserInfoUtils.getCompanyId(view.getContext()).equals(list.get(i).getOrgid())){
                holder.mImage.setVisibility(View.VISIBLE);
                holder.mImage.setSelected(true);
            }else{
                holder.mImage.setVisibility(View.INVISIBLE);
            }
            return view;
        }

        class ViewHolder {
            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.tv_desc)
            TextView mTvDesc;
            @BindView(R.id.image)
            ImageView mImage;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
