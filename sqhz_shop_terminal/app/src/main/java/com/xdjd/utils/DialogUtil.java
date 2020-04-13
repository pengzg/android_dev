package com.xdjd.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.BankItemBean;
import com.xdjd.view.DrawPadView;
import com.xdjd.view.PaintView;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 自定义仿iOS弹出框效果
 * Created by lijipei on 2016/7/15.
 */
public class DialogUtil {

    public interface MyCustomDialogListener {

        void ok(Dialog dialog,String str);

        void no();

    }

    public interface MyCustomDialogListener2 {

        void ok();

        void no();

    }

    public interface MyCustomDialogListener3 {
        void item(int i);
    }

    /**
     * 显示编辑核销面弹框
     *
     * @param context
     * @param oldPwd 旧的核销密码
     * @param ok
     * @param no
     * @param o
     */
    public static void showEditPwd(Context context, String oldPwd, String ok, String no,
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
        edit.setText(oldPwd);
        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
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
                if (TextUtils.isEmpty(edit.getText())){
                    UIUtils.Toast("请输入核销密码!");
                    return;
                }
                if (edit.getText().length() < 6){
                    UIUtils.Toast("核销密码必须等于或大于6位数");
                    return;
                }

//                dialog.dismiss();
                if (o != null) {
                    o.ok(dialog,edit.getText().toString());
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
        }
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
                                        final MyCustomDialogListener2 o) {
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

    public static void showInputSign(Context context, Resources resources, final OnInputSignListener listener){
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_dialog_inputsign_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tv_sign_sure,tv_sign_reset,tv_sign_cancel;
        final PaintView my_signview = (PaintView) view.findViewById(R.id.my_signview);
        tv_sign_sure = (TextView) view.findViewById(R.id.tv_sign_sure);
        tv_sign_reset= (TextView) view.findViewById(R.id.tv_sign_reset);
        tv_sign_cancel= (TextView) view.findViewById(R.id.tv_sign_cancel);

        tv_sign_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!my_signview.isSign()){
                    UIUtils.Toast("请先进行签名");
                    return;
                }
                Bitmap bitmap = my_signview.getCachebBitmap();
                String imgSign = bitmapToString(bitmap);
                listener.ok(imgSign,bitmap);
                dialog.dismiss();
            }
        });

        tv_sign_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.no();
            }
        });

        tv_sign_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_signview.clear();
            }
        });

        int width = resources.getDisplayMetrics().widthPixels;

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().addContentView(
                view,
                new ViewGroup.LayoutParams(width,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
        showAnimationCustom(view);
    }

    public interface OnInputSignListener{
        void ok(String imgSign,Bitmap bitmap);
        void no();
    }

    //把bitmap转换成String
    public static String bitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        Log.e("d-d", "压缩后的大小=" + b.length);
        LogUtils.e("压缩后的大小",String.format("Size : %s", getReadableFileSize(b.length)));
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
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
    public static void showDialogList(Context context, String titleStr, List<BankItemBean> list, final MyCustomDialogListener3 o) {
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

        List<BankItemBean> list;

        public void setData(List<BankItemBean> list){
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bank_type, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mTvName.setText(list.get(i).getBi_name());
            return view;
        }

        class ViewHolder {
            @BindView(R.id.tv_name)
            TextView mTvName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    private static void showAnimationCustom(final View view) {
        @SuppressLint("ObjectAnimatorBinding")
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
        });
        oo.setInterpolator(new DecelerateInterpolator());
        oo.start();
    }

}
