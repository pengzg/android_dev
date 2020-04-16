package com.bikejoy.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bikejoy.view.EditInputFilter;
import com.bikejoy.view.itemdecoration.LinearItemDecoration;
import com.bikejoy.testdemo.R;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;


/**
 * 自定义仿iOS弹出框效果
 * Created by lijipei on 2016/7/15.
 */
public class DialogUtil {

    public interface MyCustomDialogListener {

        void ok(Dialog dialog, String str);

        void no();

    }

    public interface MyCustomDialogListener2 {

        void ok();

        void no();

    }

    public interface MyCustomDialogListener3 {
        void item(int i);
    }

    public interface MyCustomDialogListener4 {

        void ok(Dialog dialog, String str, String status);

        void no();
    }

    public interface MyCustomDialogListener5 {

        void ok(Dialog dialog, String str1,String str2);

        void no();

    }

    /**
     * 编辑补货商品数量弹框
     *
     * @param context
     * @param totalNum 总容量
     * @param num      补货数量
     * @param ok
     * @param no
     * @param o
     */
    public static void showEditBhNumDialog(Context context, String message, final String totalNum, String num, String ok, String no,
                                           final MyCustomDialogListener o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_edit_bhnum_layout, null);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        TextView title = (TextView) view.findViewById(R.id.dialog_message);
        TextView total_num = (TextView) view.findViewById(R.id.tv_total_num);
        final EditText etNum = (EditText) view.findViewById(R.id.tv_num);
        etNum.setText(num);
        ImageView plus = (ImageView) view.findViewById(R.id.iv_plus);
        ImageView minus = (ImageView) view.findViewById(R.id.iv_minus);

        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line2 = view.findViewById(R.id.dialog_line2);

        final BigDecimal bgTotal = new BigDecimal(totalNum);//总数量
        title.setText(message);
        total_num.setText("总容量:" + totalNum.toString());

        if (etNum != null) {
            //设置可获得焦点
            etNum.setFocusable(true);
            etNum.setFocusableInTouchMode(true);
            //请求获得焦点
            etNum.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) etNum
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etNum, 0);
        }

        etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNum.setSelection(etNum.getText().length());
                }
            }
        });

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNum.getText())) {
                    UIUtils.Toast("请输入补货数量!");
                    return;
                }
                BigDecimal bhNum;//补货数量
                if (!TextUtils.isEmpty(etNum.getText()) && etNum.getText().toString().length() > 0) {
                    bhNum = new BigDecimal(etNum.getText().toString());
                } else {
                    bhNum = BigDecimal.ZERO;
                }

                if (bgTotal.compareTo(bhNum) == -1) {
                    UIUtils.Toast("补货数量不能大于总容量");
                    return;
                }

                o.ok(dialog, etNum.getText().toString());
                dialog.dismiss();
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

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusCalculation(etNum,bgTotal);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusCalculation(etNum);
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
     * 数量添加计算
     *
     * @param et
     */
    private static void plusCalculation(EditText et,BigDecimal bgTotal) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        if (bgTotal.compareTo(bd) == 0 || bd.compareTo(bgTotal) == 1){
            et.setText(bgTotal.toString());
        }else{
            et.setText((bd.intValue() + 1) + "");
        }
        et.setSelection(et.getText().length());
    }

    private static void plusCalculation2(EditText et,BigDecimal bgLimiting) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        if (bgLimiting.intValue()>0 && (bd.intValue()+1)>bgLimiting.intValue()){
            return;
        }
        et.setText((bd.intValue() + 1) + "");
        et.setSelection(et.getText().length());
    }

    /**
     * 数量减少计算
     *
     * @param et
     */
    private static void minusCalculation(EditText et) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            return;
        } else {
            bd = new BigDecimal(et.getText().toString());
            int num = bd.intValue();
            if (num == 0) {
                et.setText("0");
            } else if (num - 1 == 0) {
                et.setText("0");
            } else {
                et.setText(num - 1 + "");
            }
        }
        et.setSelection(et.getText().length());
    }

    /**
     * 显示带编辑框弹框
     *
     * @param context
     * @param isDissmiss true:自动销毁 , false:需要手动销毁
     * @param etStr
     * @param ok
     * @param no
     * @param o
     */
    public static void showEditDialog(Context context, final boolean isDissmiss, String etStr, String ok, String no,
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
        edit.setText(etStr);
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
               /* if (TextUtils.isEmpty(edit.getText())){
                    UIUtils.Toast("请输入购买数量!");
                    return;
                }*/
                if (isDissmiss)
                    dialog.dismiss();
                if (o != null) {
                    o.ok(dialog, edit.getText().toString());
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
     * 显示编辑文本框弹框
     *
     * @param context
     * @param hint
     * @param ok
     * @param no
     * @param o
     */
    public static void showEditTextDialog(Context context, String titleStr, String hint, String ok, String no,
                                          final MyCustomDialogListener o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_edit_text_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView title = (TextView) view.findViewById(R.id.dialog_message);
        final EditText edit = (EditText) view.findViewById(R.id.dialog_edit);
        edit.setHint(hint);
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
                //                dialog.dismiss();
                if (o != null) {
                    o.ok(dialog, edit.getText().toString());
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
        if (titleStr == null) {
            //如果是null,隐藏标题栏
            title.setVisibility(View.GONE);
        } else if (titleStr.equals("")) {
            //空串默认是--提示
        } else {
            title.setText(titleStr);
        }
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
                                        final MyCustomDialogListener2 o, boolean isCancelable) {
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

        title.setMovementMethod(new ScrollingMovementMethod());

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
        showCustomDialog(context, titleStr, message, ok, no,
                o, true);
    }

    /**
     * 公共编辑数量弹框
     * @param context
     * @param titleStr
     * @param content
     * @param num
     * @param limitingNum  限制数量 null或""不进行处理
     * @param limitingStr  限制提示语
     * @param ok
     * @param no
     * @param o
     */
    public static void showEditNumDialog(Context context, String titleStr, String content, String num,
                                         final String limitingNum, final String limitingStr, String ok, String no,
                                         final MyCustomDialogListener o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_edit_num_layout, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_message);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        final EditText etNum = (EditText) view.findViewById(R.id.tv_num);
        etNum.setText(num);
        ImageView plus = (ImageView) view.findViewById(R.id.iv_plus);
        ImageView minus = (ImageView) view.findViewById(R.id.iv_minus);

        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line2 = view.findViewById(R.id.dialog_line2);

        title.setText(titleStr);
        tv_content.setText(content);

        final BigDecimal bgLimiting;//限制数量
        if (limitingNum!=null){
            bgLimiting = new BigDecimal(limitingNum);
        }else{
            bgLimiting = BigDecimal.ZERO;
        }

        if (etNum != null) {
            //设置可获得焦点
            etNum.setFocusable(true);
            etNum.setFocusableInTouchMode(true);
            //请求获得焦点
            etNum.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) etNum
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etNum, 0);
        }

        etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNum.setSelection(etNum.getText().length());
                }
            }
        });

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNum.getText())) {
                    UIUtils.Toast("请输入正确数量!");
                    return;
                }
                BigDecimal bgNum = new BigDecimal(etNum.getText().toString());
                if (bgNum.compareTo(BigDecimal.ZERO)!=1){
                    UIUtils.Toast("请输入正确数量!");
                    return;
                }

                if (limitingNum!=null && limitingNum.length()>0){//进行限制判断
                    BigDecimal bdNum;//输入的数量
                    if (!TextUtils.isEmpty(etNum.getText()) && etNum.getText().toString().length() > 0) {
                        bdNum = new BigDecimal(etNum.getText().toString());
                    } else {
                        bdNum = BigDecimal.ZERO;
                    }

                    if (bgLimiting.compareTo(bdNum) == -1) {
                        UIUtils.Toast(limitingStr);
                        return;
                    }
                }

                o.ok(dialog, etNum.getText().toString());
                dialog.dismiss();
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

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusCalculation2(etNum,bgLimiting);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusCalculation(etNum);
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
     * 领用编辑数量和目的弹框
     * @param context
     * @param titleStr
     * @param content
     * @param num
     * @param limitingNum  限制数量 null或""不进行处理
     * @param limitingStr  限制提示语
     * @param ok
     * @param no
     * @param o
     */
    public static void showEditNumApplyDialog(Context context, String titleStr, String content, String num,
                                         final String limitingNum, final String limitingStr, String ok, String no,
                                         final MyCustomDialogListener5 o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_edit_num_apply_layout, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_message);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        final EditText etReason = (EditText) view.findViewById(R.id.et_content);
        final EditText etNum = (EditText) view.findViewById(R.id.tv_num);
        etNum.setText(num);
        ImageView plus = (ImageView) view.findViewById(R.id.iv_plus);
        ImageView minus = (ImageView) view.findViewById(R.id.iv_minus);

        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line2 = view.findViewById(R.id.dialog_line2);

        etReason.setMovementMethod(new ScrollingMovementMethod());

        title.setText(titleStr);
        tv_content.setText(content);

        final BigDecimal bgLimiting;//限制数量
        if (limitingNum!=null){
            bgLimiting = new BigDecimal(limitingNum);
        }else{
            bgLimiting = BigDecimal.ZERO;
        }

        if (etNum != null) {
            //设置可获得焦点
            etNum.setFocusable(true);
            etNum.setFocusableInTouchMode(true);
            //请求获得焦点
            etNum.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) etNum
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etNum, 0);
        }

        etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNum.setSelection(etNum.getText().length());
                }
            }
        });

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNum.getText())) {
                    UIUtils.Toast("请输入正确数量!");
                    return;
                }
                BigDecimal bgNum = new BigDecimal(etNum.getText().toString());
                if (bgNum.compareTo(BigDecimal.ZERO)!=1){
                    UIUtils.Toast("请输入正确数量!");
                    return;
                }

                if (limitingNum!=null && limitingNum.length()>0){//进行限制判断
                    BigDecimal bdNum;//输入的数量
                    if (!TextUtils.isEmpty(etNum.getText()) && etNum.getText().toString().length() > 0) {
                        bdNum = new BigDecimal(etNum.getText().toString());
                    } else {
                        bdNum = BigDecimal.ZERO;
                    }

                    if (bgLimiting.compareTo(bdNum) == -1) {
                        UIUtils.Toast(limitingStr);
                        return;
                    }
                }

                if (TextUtils.isEmpty(etReason.getText())){
                    UIUtils.Toast("请输入领用目的");
                    return;
                }

                o.ok(dialog, etNum.getText().toString(),etReason.getText().toString());
                dialog.dismiss();
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

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusCalculation2(etNum,bgLimiting);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusCalculation(etNum);
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


    private static EditText etRate01,etRate02;

    public static void showEditAgentProfitRatio(Context context, String titleStr, String recommend1_rate,
                                         final String recommend2_rate, String ok, String no,
                                         final MyCustomDialogListener5 o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_agent_profit_ratio_layout, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_message);

        final EditText etRate1 = (EditText) view.findViewById(R.id.tv_recommend1_rate);
        etRate1.setText(recommend1_rate);
        final EditText etRate2 = (EditText) view.findViewById(R.id.tv_recommend2_rate);
        etRate2.setText(recommend2_rate);

        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        LinearLayout linlin = (LinearLayout) view.findViewById(R.id.linlin);
        View line2 = view.findViewById(R.id.dialog_line2);

        title.setText(titleStr);

        etRate01 =etRate1;
        etRate02 =etRate2;

        editRatioFocus(etRate1);
        editRatioFocus(etRate2);

        InputFilter[] filters = {new EditInputFilter()};
        //限制输入比例过滤
        etRate1.setFilters(filters);
        etRate2.setFilters(filters);

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                o.ok(dialog,etRate1.getText().toString(),etRate2.getText().toString());
                dialog.dismiss();
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

    public static void editRatioFocus(final EditText editText){
        final MyTextWatcher myTextWatcher = new MyTextWatcher(editText);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    editText.addTextChangedListener(myTextWatcher);
                }else{
                    editText.removeTextChangedListener(myTextWatcher);
                }
            }
        });
    }

    public static class MyTextWatcher implements TextWatcher{

        EditText et;
        double MAX_VAULE = 100;

        public MyTextWatcher(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (et == etRate01){
                if (TextUtils.isEmpty(s.toString())){
                    etRate02.setText(MAX_VAULE+"");
                }else{
                    double d1 = Double.parseDouble(s.toString());
                    String str = (MAX_VAULE-d1)+"";
                    etRate02.setText(str);
                }
            }else if (et == etRate02){
                if (TextUtils.isEmpty(s.toString())){
                    etRate01.setText(MAX_VAULE+"");
                }else{
                    double d2 = Double.parseDouble(s.toString());
                    String str = (MAX_VAULE-d2)+"";
                    etRate01.setText(str);
                }
            }
        }
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
            myversion_img.setVisibility(View.VISIBLE);
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
//        if (ok == null) {
//            ok_tx.setVisibility(View.GONE);
//            line2.setVisibility(View.INVISIBLE);
//        }
//        if (no == null) {
//            no_tx.setVisibility(View.GONE);
//            line2.setVisibility(View.INVISIBLE);
//        }
//        if (ok == null && no == null) {
//            linlin.setVisibility(View.GONE);
//        }
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


    /**
     * 显示说明弹框
     *
     * @param context
     * @param message
     */
    public static void showExplainCustomDialog(Context context, String titleStr, final String message) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_explain_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout ll_close = view.findViewById(R.id.ll_close);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        TextView title = (TextView) view.findViewById(R.id.dialog_message);
        title.setMovementMethod(new ScrollingMovementMethod());

        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        title.setText(message);

        if (titleStr == null) {
            //如果是null,隐藏标题栏
            dialog_title.setVisibility(View.GONE);
        } else if (titleStr.equals("")) {
            //空串默认是--提示
        } else {
            dialog_title.setText(titleStr);
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
     * 添加拜访信息
     * @param context
     * @param titleStr
     * @param hint
     */
    public static void showAddNoteDialog(Context context, String titleStr, final String hint, String ok, String no,
                                         final MyCustomDialogListener o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_add_note_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout ll_close = view.findViewById(R.id.ll_close);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        final EditText content = (EditText) view.findViewById(R.id.et_content);
        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        View line = view.findViewById(R.id.custom_line);
        View line2 = view.findViewById(R.id.dialog_line2);
        content.setMovementMethod(new ScrollingMovementMethod());

        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        content.setHint(hint);

        if (titleStr == null) {
            //如果是null,隐藏标题栏
            dialog_title.setVisibility(View.GONE);
        } else if (titleStr.equals("")) {
            //空串默认是--提示
        } else {
            dialog_title.setText(titleStr);
        }

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (o != null) {
                    o.ok(dialog,content.getText().toString());
                }
            }
        });
        no_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (o != null) {
                    dialog.dismiss();
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
            line.setVisibility(View.INVISIBLE);
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
     * 订单审核功能
     *
     * @param context
     * @param titleStr
     * @param hint
     */
    public static void showCheckErpDialog(Context context, String titleStr, final String hint, String ok, String no,
                                          final MyCustomDialogListener4 o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_check_erp_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout ll_close = view.findViewById(R.id.ll_close);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        final EditText content = (EditText) view.findViewById(R.id.et_content);
        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        View line = view.findViewById(R.id.custom_line);
        View line2 = view.findViewById(R.id.dialog_line2);
        final RadioGroup radioGroup = view.findViewById(R.id.radio_group);

        content.setMovementMethod(new ScrollingMovementMethod());

        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        content.setHint(hint);

        if (titleStr == null) {
            //如果是null,隐藏标题栏
            dialog_title.setVisibility(View.GONE);
        } else if (titleStr.equals("")) {
            //空串默认是--提示
        } else {
            dialog_title.setText(titleStr);
        }

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (o != null) {

                    String str = content.getText().toString();
                    String type;
                    if (radioGroup.getCheckedRadioButtonId() == R.id.rb_no) {
                        type = "3";//不通过
                    } else {
                        type = "2";//通过
                    }
                    if ("3" .equals(type) && TextUtils.isEmpty(str) && TextUtils.isEmpty(str.trim())) {
                        UIUtils.Toast("请输入审核说明");
                        return;
                    }
                    o.ok(dialog, str, type);
                }
            }
        });
        no_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (o != null) {
                    dialog.dismiss();
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
            line.setVisibility(View.INVISIBLE);
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
     * 申请备注功能
     *
     * @param context
     * @param titleStr
     * @param hint
     */
    public static void showApplyNoteDialog(Context context, String titleStr, final String hint, String ok, String no,
                                          final MyCustomDialogListener4 o) {
        final Dialog dialog = new Dialog(context, R.style.Translucent_NoTitle);
        final View view = LayoutInflater.from(context).inflate(
                R.layout.mycustom_apply_note_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout ll_close = view.findViewById(R.id.ll_close);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        final EditText content = (EditText) view.findViewById(R.id.et_content);
        TextView ok_tx = (TextView) view.findViewById(R.id.dialog_ok);
        TextView no_tx = (TextView) view.findViewById(R.id.dialog_no);
        View line = view.findViewById(R.id.custom_line);
        View line2 = view.findViewById(R.id.dialog_line2);
        final RadioGroup radioGroup = view.findViewById(R.id.radio_group);

        content.setMovementMethod(new ScrollingMovementMethod());

        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        content.setHint(hint);

        if (titleStr == null) {
            //如果是null,隐藏标题栏
            dialog_title.setVisibility(View.GONE);
        } else if (titleStr.equals("")) {
            //空串默认是--提示
        } else {
            dialog_title.setText(titleStr);
        }

        ok_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (o != null) {

                    String str = content.getText().toString();
                    String type;
                    if (radioGroup.getCheckedRadioButtonId() == R.id.rb_no) {
                        type = "3";//不通过
                    } else {
                        type = "2";//通过
                    }
                    if ("3" .equals(type) && TextUtils.isEmpty(str) && TextUtils.isEmpty(str.trim())) {
                        UIUtils.Toast("请输入审核说明");
                        return;
                    }
                    o.ok(dialog, str, type);
                }
            }
        });
        no_tx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (o != null) {
                    dialog.dismiss();
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
            line.setVisibility(View.INVISIBLE);
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


    //把bitmap转换成String
    public static String bitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        Log.e("d-d", "压缩后的大小=" + b.length);
        LogUtils.e("压缩后的大小", String.format("Size : %s", getReadableFileSize(b.length)));
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
