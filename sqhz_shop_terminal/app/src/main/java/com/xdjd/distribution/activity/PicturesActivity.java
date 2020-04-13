package com.xdjd.distribution.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ImagePathBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.bean.UserSignBean;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.event.PicturesEvent;
import com.xdjd.distribution.event.SignClientEvent;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/5
 *     desc   : 自定义相机界面
 *     version: 1.0
 * </pre>
 */

public class PicturesActivity extends BaseActivity implements SurfaceHolder.Callback {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.sfv)
    SurfaceView sfv;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.ll_bottom)
    RelativeLayout mLlBottom;
    @BindView(R.id.ll_del)
    LinearLayout mLlDel;
    @BindView(R.id.rl_img)
    RelativeLayout mRlImg;

    private SurfaceHolder sfh;
    private Camera camera = null;

    private Bitmap bitmap;

    /**
     * 店铺图片
     */
    private String signImg = null;
    private String imagePath;
    private String resultImage;

    private ClientBean clientBean;

    private UserBean userBean;

    private File file;
    // 图片sd地址
    private List<String> drr = new ArrayList<String>();

    // 本地相册路径
    public String folderName = Environment.getExternalStorageDirectory()
            + "/DCIM/Camera/";

    private boolean isCanClick = true;
    private int type;

    @Override
    protected int getContentView() {
        return R.layout.activity_pictures;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        type = getIntent().getIntExtra("type",0);
        userBean = UserInfoUtils.getUser(this);

        clientBean = UserInfoUtils.getClientInfo(this);
        mTitleBar.setTitle("拍照");

        mTitleBar.leftBack(this);
        if(type == 0){
            mTitleBar.setRightText("提交");
        }else{
            mTitleBar.setRightText("保存");
        }
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type == 0){
                    submitPic();
                }else{
                    submitShopImage();
                }
            }
        });

        camera = Camera.open();
        sfh = sfv.getHolder();
        sfh.addCallback(this);

        sfv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(null);
            }
        });
    }

    @OnClick({R.id.btn, R.id.iv, R.id.ll_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv://显示图片
                Intent intent = new Intent(PicturesActivity.this, PhotoActivity.class);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(file.getAbsolutePath());
                intent.putExtra("drr", jsonArray.toString());
                intent.putExtra("isNetwork", 2);
                startActivity(intent);
                break;
            case R.id.ll_del://删除图片
                if (!isCanClick) {
                    showToast("正在提交中...");
                    return;
                }
                signImg = null;
                bitmap = null;
                mIv.setImageResource(R.color.transparent);
                mRlImg.setVisibility(View.GONE);
                break;
            case R.id.btn:
                if (!isCanClick) {
                    showToast("正在提交中...");
                    return;
                }

                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                int screenWidth = display.getWidth();
                int screenHeight = screenHeight = display.getHeight();

                if (camera == null){
                    camera = Camera.open();
                    if (sfh != null) {
                        startPreview();
                    }
                }

                //设置相机的各种参数
                Camera.Parameters parameters = camera.getParameters();

                //自己添加的代码 -- 设置相片拍摄的高宽和屏幕分辨率一样
                parameters.setPictureSize(screenWidth, screenHeight);

                //对焦的方式
                parameters.setFocusMode(Camera.Parameters.ANTIBANDING_AUTO);
                //照片的类型
                parameters.setPictureFormat(ImageFormat.JPEG);

                //                parameters.setPreviewSize(240,320);


                try{
                    //对焦监听
                    camera.autoFocus(mAutoFocusCallback);
                }catch (Exception e){
                }
                break;
        }
    }

    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback(){
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //对焦成功
            if (success) {
                camera.takePicture(null, null, cameraCallBack);
            } else {
                Toast.makeText(PicturesActivity.this, "对焦失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 提交店铺图片
     */
    private void submitPic() {
        if (signImg == null || "".equals(signImg)) {
            showToast("请拍照后再提交");
            return;
        }
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    clientBean.setCc_image(jsonBean.getCc_image());
                    UserInfoUtils.setClientInfo(PicturesActivity.this, clientBean);
                    setResult(Comon.QR_GOODS_RESULT_CODE);
                    finishActivity();
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                if (camera == null) {
                    camera = Camera.open();
                    if (sfh != null) {
                        startPreview();
                    }
                }
                isCanClick = true;
            }
        });
        httpUtil.post(M_Url.updateImg, L_RequestParams.updateImg(userBean.getUserId(), clientBean.getCc_id(), signImg), true);
    }

    /*提交门店照片*/
    private void submitShopImage(){
        AsyncHttpUtil<ImagePathBean> httpUtil = new AsyncHttpUtil<>(PicturesActivity.this, ImagePathBean.class, new IUpdateUI<ImagePathBean>() {
            @Override
            public void updata(ImagePathBean bean) {
                if(bean.getRepCode().equals("00")){
                    resultImage = bean.getImgPath();
//                    Intent intent = new Intent();
//                    intent.putExtra("url",signImg);
//                    intent.putExtra("resultPath",resultImage);
//                    setResult(Comon.ADD_CUSTOMER_RESULT_CODE,intent);

                    EventBus.getDefault().post(new PicturesEvent(resultImage,signImg));
                    finishActivity();
                }else{
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.uploadImage,L_RequestParams.uploadImage(signImg,"3"),false);
    }

    /**
     * 拍照成功回调函数
     */
    private Camera.PictureCallback cameraCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            startPreview();
            LogUtils.e("拍照的大小", data.length + "");
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(0, cameraInfo);
            int degrees = 0;
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0: {
                    degrees = 0;
                    break;
                }
                case Surface.ROTATION_90: {
                    degrees = 90;
                    break;
                }
                case Surface.ROTATION_180: {
                    degrees = 180;
                    break;
                }
                case Surface.ROTATION_270: {
                    degrees = 270;
                    break;
                }
            }
            int displayOrientation;
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                displayOrientation = (cameraInfo.orientation + degrees) % 360;
                displayOrientation = (360 - displayOrientation) % 360;
            } else {
                displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
            }
            camera.setDisplayOrientation(displayOrientation);

            //先验证手机是否有sdcard
            String status = Environment.getExternalStorageState();
            if (!status.equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(getApplicationContext(), "您的sd卡不可用。", Toast.LENGTH_SHORT).show();
                return;
            }
            //存到本地相册
            imagePath = folderName + StringUtils.getDate() + ".png";
            file = new File(imagePath);

            //显示图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            if (bitmap.getWidth() > 720) {
                options.inSampleSize = bitmap.getWidth() / 240;
            } else {
                options.inSampleSize = 1;
            }
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            Matrix matrix = new Matrix();
            matrix.setRotate(displayOrientation);//旋转90度矩阵
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            LogUtils.e("相片高宽", "width:" + bitmap.getWidth() + "--height:" + bitmap.getHeight());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bitmapByte = baos.toByteArray();

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bitmapByte);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null)
                        fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            mIv.setImageBitmap(bitmap);
            mRlImg.setVisibility(View.VISIBLE);

          signImg = bitmapToString(bitmap);
        }
    };


    //把bitmap转换成String
    public String bitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        Log.e("d-d", "压缩后的大小=" + b.length);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 检查设备是否存在照相机
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            camera = Camera.open();
            if (sfh != null) {
                startPreview();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDriver();
        //        if (!isHasSurface) {
        //            sfv.getHolder().removeCallback(this);
        //        }
    }

    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    //创建
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //开始进行图片的预览
        startPreview();
    }

    //改变
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //停止旧的预览,开启新的预览
        //        camera.stopPreview();
        //        startPreview();
    }

    //释放
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //停止预览,释放资源
        //        stopCamera();
    }

    /**
     * 相机预览
     */
    private void startPreview() {
        try {
            //相机与SurfaceView进行绑定
            camera.setPreviewDisplay(sfh);
            //预览的图片旋转
            camera.setDisplayOrientation(90);
            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止拍照释放资源
     */
    private void stopCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


}
