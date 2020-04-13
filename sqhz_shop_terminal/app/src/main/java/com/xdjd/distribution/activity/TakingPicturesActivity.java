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
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ImagePathBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.bean.UserSignBean;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.event.InventoryPhotoEvent;
import com.xdjd.distribution.event.SignClientEvent;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DialogUtil;
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
import com.xdjd.utils.release.Bimp;
import com.xdjd.view.EaseTitleBar;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
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

public class TakingPicturesActivity extends BaseActivity implements LocationListener, SurfaceHolder.Callback {
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
    @BindView(R.id.gridView_addPicture)
    GridView mGridViewAddPicture;
    @BindView(R.id.publish_linearLayout)
    LinearLayout mPublishLinearLayout;

    private boolean isHasSurface = false;


    private SurfaceHolder sfh;
    private Camera camera = null;

    private Bitmap bitmap;


    private LocationService locationService;
    /**
     * 纬度
     */
    private String latitude = "";
    /**
     * 经度
     */
    private String longtitude = "";
    /**
     * 定位后的地址
     */
    private String address = "";

    /**
     * 扫描客户卡得到客户编码
     */
    private String ccCode = "";

    /**
     * 签到签退图片
     */
    private String signImg = null;
    /**
     * 拍照类型 1.进店拍照 2.离店拍照 3.客户盘点拍照
     */
    private int type;
    private ClientBean clientBean;
    private UserBean userBean;

    private File file;
    private boolean isSignBackOut;
    //是否上传图片,1.是上传图片;2.不是
    private int isUpload = 1;
    //图片集合下标
    private int imgIndex = 0;

    // 上传后返回的图片路径
    private ArrayList<String> pathList = new ArrayList<>();
    // 图片sd地址 上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小�?100KB，失真度不明�?
    private List<String> drr = new ArrayList<>();

    private List<ImagePathBean> imgList = new ArrayList<>();
    private ReleasePictureAdapter adapter;

    // 本地相册路径
    public String folderName = Environment.getExternalStorageDirectory()
            + "/DCIM/Camera/";

    private MyLocationUtil locationUtil;

    private boolean isCanClick = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disProgressDialog(TakingPicturesActivity.this);

            if (msg.what == PublicFinal.LOCATION_SUCCESS) {
                if (imgList == null || imgList.size() == 0) {
                    showToast("请先拍照再提交");
                    return;
                }
                /*Bitmap bm = createWatermark(bitmap, address);
                signImg = bitmapToString(bm);
                disProgressDialog(TakingPicturesActivity.this);*/
                if (type == PublicFinal.SIGN) {
                    sign(clientBean);
                } else if (type == PublicFinal.SIGNOUT) {
                    signOut();
                }
            } else if (isUpload == 1 && msg.what == 100) {
                //上传图片
                Bitmap bm = createWatermark(imgList.get(imgIndex).getBmpImg(), address);
                String signImg = bitmapToString(bm);
                uploadImage(signImg, imgIndex);
            } else if (msg.what == PublicFinal.ERROR) {
                //定位失败
                if (camera == null) {
                    camera = Camera.open();
                    if (sfh != null) {
                        startPreview();
                    }
                }
                isCanClick = true;

                if (isUpload == 1) {
                    imgList.get(imgIndex).setIsUploadSuccess("2");
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_taking_pictures;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        userBean = UserInfoUtils.getUser(this);

        type = getIntent().getIntExtra("type", 1);
        isSignBackOut = getIntent().getBooleanExtra("isSignBackOut", false);
        clientBean = (ClientBean) getIntent().getSerializableExtra("customer");

        latitude = getIntent().getStringExtra("latitude");
        longtitude = getIntent().getStringExtra("longtitude");
        address = getIntent().getStringExtra("address");

        if (type == PublicFinal.SIGN) {
            mTitleBar.setTitle("进店拍照");
        } else if (type == PublicFinal.SIGNOUT) {
            mTitleBar.setTitle("离店拍照");
        } else if (type == PublicFinal.INVENTORY) {
            mTitleBar.setTitle("客户盘点拍照");

            drr = (List<String>) getIntent().getSerializableExtra("drr");
            pathList = (ArrayList<String>) getIntent().getSerializableExtra("pathList");

            if (drr != null) {
                for (int i = 0; i < drr.size(); i++) {
                    ImagePathBean bean = new ImagePathBean();
                    if ("".equals(pathList.get(i))) {
                        bean.setIsUploadSuccess("2");
                    } else {
                        bean.setIsUploadSuccess("1");
                    }
                    bean.setImgPath(pathList.get(i));
                    bean.setSdPath(drr.get(i));
                    imgList.add(bean);
                }
            } else {
                drr = new ArrayList<>();
                pathList = new ArrayList<>();
            }
        }

        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);

        mTitleBar.leftBack(this);
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == PublicFinal.INVENTORY) {
                    inventoryPhotos();
                }
                finishActivity();
            }
        });

        if (type == PublicFinal.INVENTORY) {
            mTitleBar.setRightText("拍照完成");
        } else {
            mTitleBar.setRightText("提交");
        }

        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgList == null || imgList.size() == 0) {
                    if (type == PublicFinal.INVENTORY) {
                        showToast("请先拍照");
                    } else {
                        showToast("请先拍照再提交");
                    }
                    return;
                } else if (isCanClick) {
                    for (int i = 0; i < imgList.size(); i++) {
                        if (!"1".equals(imgList.get(i).getIsUploadSuccess())) {
                            DialogUtil.showCustomDialog(TakingPicturesActivity.this, "提示",
                                    "您有拍照的图片未上传成功,请点击单张图片重新上传", "确定", null, null);
                            return;
                        }
                    }

                    if (type == PublicFinal.INVENTORY) {
                        inventoryPhotos();
                        finishActivity();
                    } else {
                        isUpload = 2;

                        latitude = "";
                        longtitude = "";
                        address = "";
                        locationService.start();
                        isCanClick = false;
                        showProgressDialog(TakingPicturesActivity.this);
                    }
                } else if (!isCanClick) {
                    showToast("正在提交中...");
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

        adapter = new ReleasePictureAdapter();
        mGridViewAddPicture.setAdapter(adapter);
        mGridViewAddPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(TakingPicturesActivity.this, PhotoActivity.class);
                intent.putExtra("ID", arg2);
                JSONArray jsonArray = new JSONArray();
                for (int j = 0; j < drr.size(); j++) {
                    jsonArray.put(drr.get(j));
                }
                intent.putExtra("drr", jsonArray.toString());
                intent.putExtra("isNetwork", 2);
                startActivityForResult(intent, 101);
            }
        });

    }

    @OnClick({R.id.btn, R.id.iv, R.id.ll_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv://显示图片
                Intent intent = new Intent(TakingPicturesActivity.this, PhotoActivity.class);
                intent.putExtra("picPath", file.getAbsolutePath());
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
            case R.id.btn://拍照
                if (!isCanClick) {
                    showToast("正在提交中...");
                    return;
                }

                if (imgList.size() == 5) {
                    showToast("最多只能上传5张图片!");
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

                Camera.Parameters parameters = camera.getParameters();//获取相机参数
                parameters.setPictureSize(screenWidth, screenHeight);//自己添加的代码 -- 设置相片拍摄的高宽和屏幕分辨率一样
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//对焦的方式: FOCUS_MODE_AUTO自动对焦
                parameters.setPictureFormat(ImageFormat.JPEG);//照片的类型

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
                Toast.makeText(TakingPicturesActivity.this, "对焦失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
            Log.e("tag", rotation + "--");
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
            file = new File(folderName + StringUtils.getDate() + ".png");
            //"/sdcard/"+getPackageName()+"/img/" + StringUtils.getDate() + ".png");
            //LogUtils.e("options.inSampleSize1","bitmap.getWidth():"+bitmap.getWidth()+"--"+(bitmap.getWidth() / 240)+"");

            //显示图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            if (bitmap.getWidth() > 720) {
                options.inSampleSize = bitmap.getWidth() / 240;
            } else {
                options.inSampleSize = 1;
            }
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            LogUtils.e("options.inSampleSize2", "bitmap.getWidth():" + bitmap.getWidth() + "--" + (bitmap.getWidth() / 240) + "");

            Matrix matrix = new Matrix();
            matrix.setRotate(displayOrientation);//旋转90度矩阵
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            LogUtils.e("相片高宽", "width:" + bitmap.getWidth() + "--height:" + bitmap.getHeight());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
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

            pathList.add("");
            drr.add(file.getAbsolutePath());
            LogUtils.e("图片路径", file.getPath());

            ImagePathBean bean = new ImagePathBean();
            bean.setBmpImg(bitmap);
            bean.setSdPath(file.getAbsolutePath());
            bean.setIsUploadSuccess("1");
            imgList.add(bean);

            adapter.notifyDataSetChanged();

            imgIndex = imgList.size() - 1;
            showToast("拍照成功");

            if (type == PublicFinal.INVENTORY) {//客户盘点拍照
                if (address != null && !"".equals(address)) {//如果定过位,切地址不为空则不用再次定位
                    Bitmap bm = createWatermark(imgList.get(imgIndex).getBmpImg(), address);
                    String signImg = bitmapToString(bm);
                    uploadImage(signImg, imgList.size() - 1);
                } else {
                    showProgressDialog(TakingPicturesActivity.this);
                    isUpload = 1;
                    locationService.start();
                }
            } else {//访店拍照
                if (latitude != null && latitude.length() > 0) {//是否有经纬度,如果没有则需要重新定位
                    Bitmap bm = createWatermark(imgList.get(imgIndex).getBmpImg(), address);
                    String signImg = bitmapToString(bm);
                    uploadImage(signImg, imgList.size() - 1);
                } else {
                    showProgressDialog(TakingPicturesActivity.this);
                    isUpload = 1;
                    locationService.start();
                }
            }

            //"/storage/emulated/0/DCIM/Camera/IMG_20170818_214726.jpg"
           /* try {
                File compressedImage = new Compressor(TakingPicturesActivity.this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.PNG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(file);
                LogUtils.e("压缩后的图片路径", compressedImage.getPath());
                LogUtils.e("压缩后的大小",String.format("Size : %s", getReadableFileSize(compressedImage.length())));

                Bitmap bm1 = null;
                try {
                    bm1 = Bimp.revitionImageSize(compressedImage.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                };

                pathList.add("");
                drr.add(compressedImage.getAbsolutePath());

                ImagePathBean bean = new ImagePathBean();
                bean.setBmpImg(bm1);
                bean.setSdPath(compressedImage.getAbsolutePath());
                bean.setIsUploadSuccess("1");
                imgList.add(bean);

                adapter.notifyDataSetChanged();

                imgIndex = imgList.size() - 1;
                showToast("拍照成功");

                if (type == PublicFinal.INVENTORY) {//客户盘点拍照
                    if (address != null && !"".equals(address)) {//如果定过位,切地址不为空则不用再次定位
                        Bitmap bm = createWatermark(imgList.get(imgIndex).getBmpImg(), address);
                        String signImg = bitmapToString(bm);
                        uploadImage(signImg, imgList.size() - 1);
                    } else {
                        showProgressDialog(TakingPicturesActivity.this);
                        isUpload = 1;
                        locationService.start();
                    }
                } else {//访店拍照
                    if (latitude != null && latitude.length() > 0) {//是否有经纬度,如果没有则需要重新定位
                        Bitmap bm = createWatermark(imgList.get(imgIndex).getBmpImg(), address);
                        String signImg = bitmapToString(bm);
                        uploadImage(signImg, imgList.size() - 1);
                    } else {
                        showProgressDialog(TakingPicturesActivity.this);
                        isUpload = 1;
                        locationService.start();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e("compreessed","压缩异常了");
            }*/

        }
    };

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    // 为图片target添加水印文字
    // Bitmap target：被添加水印的图片
    // String mark：水印文章
    private Bitmap createWatermark(Bitmap target, String address) {
        int w = target.getWidth();
        int h = target.getHeight();

        LogUtils.e("宽高", "w:" + w + "--h:" + h);

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint p = new Paint();

        // 水印的颜色
        p.setColor(Color.RED);

        // 水印的字体大小
        p.setTextSize(11);

        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(target, 0, 0, p);

        // 在左边的中间位置开始添加水印
        canvas.drawText(StringUtils.getDate(), 0, h - 5, p);//时间

        String cusType = "";
        switch (type) {
            case PublicFinal.SIGN:
                cusType = "(进店照片)";
                break;
            case PublicFinal.SIGNOUT:
                cusType = "(离店照片)";
                break;
            case PublicFinal.INVENTORY:
                cusType = "(盘点照片)";
                break;
        }

        if (address == null) {
            address = "";
        }

        if (address.length() > 20) {//当字符超出20个字,进行换行
            canvas.drawText(address.substring(20, address.length()), 0, h - 21, p);//定位的地址
            canvas.drawText(address.substring(0, 20), 0, h - 38, p);//定位的地址
            canvas.drawText(userBean.getBud_name(), 0, h - 55, p);//业务员
            canvas.drawText(clientBean.getCc_name() + cusType, 0, h - 72, p);//店名
        } else {
            canvas.drawText(address, 0, h - 21, p);//定位的地址
            canvas.drawText(userBean.getBud_name(), 0, h - 38, p);//业务员
            canvas.drawText(clientBean.getCc_name() + cusType, 0, h - 55, p);//店名
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bmp;
    }

    //把bitmap转换成String
    public String bitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        Log.e("d-d", "压缩后的大小=" + b.length);
        LogUtils.e("压缩后的大小",String.format("Size : %s", getReadableFileSize(b.length)));
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

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            if (camera == null) {
                camera = Camera.open();
                if (sfh != null) {
                    startPreview();
                }
            }
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            sfv.getHolder().addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDriver();
        if (!isHasSurface && sfv!=null) {
            sfv.getHolder().removeCallback(this);
        }
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
        if (!isHasSurface) {
            isHasSurface = true;
            //开始进行图片的预览
            if (camera == null) {
                camera = Camera.open();
                if (sfh != null) {
                    startPreview();
                }
            }else{
                startPreview();
            }
        }

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
        stopCamera();
        isHasSurface = false;
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
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (type == PublicFinal.INVENTORY) {
            super.onBackPressed();
            inventoryPhotos();
        }
    }

    /**
     * 签到
     */
    private void sign(final ClientBean bean) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < imgList.size(); i++) {
            sb.append(imgList.get(i).getImgPath());
            if (i != imgList.size())
                sb.append(",");
        }
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    UserInfoUtils.setTaskId(TakingPicturesActivity.this,jsonBean.getTaskId());//客户签到id
                    UserInfoUtils.setClientInfo(TakingPicturesActivity.this, bean);

                    //将客户余额重置,首页重新请求
                    UserInfoUtils.setCustomerBalance(TakingPicturesActivity.this, null);
                    UserInfoUtils.setSafetyArrearsNum(TakingPicturesActivity.this, null);
                    UserInfoUtils.setAfterAmount(TakingPicturesActivity.this, null);

                    if (!TextUtils.isEmpty(bean.getLineId()) && !bean.getLineId().equals(UserInfoUtils.getLineId(TakingPicturesActivity.this))) {
                        UserInfoUtils.setLineId(TakingPicturesActivity.this, bean.getLineId());
                        UserInfoUtils.setLineName(TakingPicturesActivity.this, bean.getLineName());
                    }

                    EventBus.getDefault().post(new SignClientEvent());

                    showToast(jsonBean.getRepMsg());
                    Intent intent = new Intent(TakingPicturesActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
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
        httpUtil.post(M_Url.sign, L_RequestParams.sign(userBean.getUserId(), UserInfoUtils.getLineId(this) + "",
                userBean.getOrgid(), bean.getCc_id(), longtitude, latitude, userBean.getIsScan(), "2", address, sb.toString()), true);
    }

    /**
     * 签退
     */
    private void signOut() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < imgList.size(); i++) {
            sb.append(imgList.get(i).getImgPath());
            if (i != imgList.size())
                sb.append(",");
        }
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(TakingPicturesActivity.this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    showToast(jsonStr.getRepMsg());
                    UserInfoUtils.setTaskId(TakingPicturesActivity.this,null);
                    UserInfoUtils.setClientInfo(TakingPicturesActivity.this, null);
                    UserInfoUtils.setCustomerBalance(TakingPicturesActivity.this, null);
                    UserInfoUtils.setSafetyArrearsNum(TakingPicturesActivity.this, null);
                    UserInfoUtils.setAfterAmount(TakingPicturesActivity.this, null);

                    if (isSignBackOut) {
                        addUserSign();
                    } else {
                        finishActivity();
                    }
                    EventBus.getDefault().post(new SignClientEvent());
                } else {
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.signOut, L_RequestParams.signOut(userBean.getUserId(),
                UserInfoUtils.getLineId(this) + "", userBean.getOrgid(), clientBean.getCc_id(),
                longtitude, latitude, userBean.getIsScan(), "2",  UserInfoUtils.getTaskId(this), sb.toString()), true);
    }

    /**
     * 业务员签退
     */
    private void addUserSign() {
        AsyncHttpUtil<UserSignBean> httpUtil = new AsyncHttpUtil<>(this, UserSignBean.class, new IUpdateUI<UserSignBean>() {

            @Override
            public void updata(UserSignBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (userBean.getBud_id() != null && !"".equals(userBean.getBud_id())) {
                        PushAgent mPushAgent = PushAgent.getInstance(TakingPicturesActivity.this);
                        //退出时解除别名的绑定
                        mPushAgent.removeAlias(userBean.getBud_id(), BaseConfig.Alias_Type,
                                new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean isSuccess, String s) {
                                    }
                                });
                    }
                } else {
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
                blackApp();
            }
        });
        httpUtil.post(M_Url.addUserSign, L_RequestParams.addUserSign(userBean.getUserId(), UserInfoUtils.getLineId(this),
                UserInfoUtils.getCudId(this), "2"), true);
    }

    /**
     * 上传图片
     */
    private void uploadImage(final String imgStr, final int index) {
        AsyncHttpUtil<ImagePathBean> httpUtil = new AsyncHttpUtil<>(this, ImagePathBean.class, new IUpdateUI<ImagePathBean>() {
            @Override
            public void updata(ImagePathBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    pathList.set(index, jsonBean.getImgPath());

                    imgList.get(index).setImgPath(jsonBean.getImgPath());
                    imgList.get(index).setIsUploadSuccess("1");
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(jsonBean.getRepMsg());
                    imgList.get(index).setIsUploadSuccess("2");
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
                try{
                    imgList.get(index).setIsUploadSuccess("2");
                }catch(Exception e){
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.uploadImage, L_RequestParams.uploadImage(imgStr, type == PublicFinal.INVENTORY ? "2" : "1"), true);
    }

    private void blackApp() {
        UserInfoUtils.setCudId(this, "");
        UserInfoUtils.setLoginState(this, "0");

        UserInfoUtils.setLineId(this, "");//清空线路id
        UserInfoUtils.setLineName(this, "");

        finish();
        AppManager.getInstance().finishAllActivity();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(getPackageName());
        AppManager.getInstance().finishAllActivity();
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop(); //停止定位服务

        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        //        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    public void locationSuccess(BDLocation location) {
        latitude = String.valueOf(location.getLatitude());
        longtitude = String.valueOf(location.getLongitude());
        address = location.getAddrStr();
        LogUtils.e("location", "taking" + location.getAddrStr() + location.getLatitude());
        if (isUpload == 1) {//拍照后的定位
            mHandler.sendEmptyMessage(100);
        } else {
            mHandler.sendEmptyMessage(PublicFinal.LOCATION_SUCCESS);
        }
        locationService.stop();
    }

    @Override
    public void locationError(BDLocation location) {
        showToast("定位获取失败,请检查GPS或网络是否打开");
        locationService.stop(); //停止定位服务
        mHandler.sendEmptyMessage(PublicFinal.ERROR);
    }

    /**
     * 盘点拍照操作
     */
    private void inventoryPhotos() {
        EventBus.getDefault().post(new InventoryPhotoEvent(pathList, drr));
    }

    public class ReleasePictureAdapter extends BaseAdapter {

        public int getCount() {
            return imgList == null ? 0 : imgList.size();
        }

        public Object getItem(int arg0) {
            return imgList.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder(convertView);
                holder.mItemGridaImage = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (type == PublicFinal.INVENTORY) {
                Bitmap bm = null;
                try {
                    bm = Bimp.revitionImageSize(imgList.get(position).getSdPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                holder.mItemGridaImage.setImageBitmap(bm);
            } else {
                holder.mItemGridaImage.setImageBitmap(imgList.get(position).getBmpImg());
            }

            if ("1".equals(imgList.get(position).getIsUploadSuccess())) {
                holder.mTvUpload.setVisibility(View.GONE);
            } else {
                holder.mTvUpload.setVisibility(View.VISIBLE);
            }

            holder.mTvUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgIndex = position;
                    if (address != null && !"".equals(address)) {//如果定过位,且地址不为空则不用再次定位
                        Bitmap bm = createWatermark(imgList.get(imgIndex).getBmpImg(), address);
                        String signImg = bitmapToString(bm);
                        uploadImage(signImg, imgIndex);
                    } else {
                        showProgressDialog(TakingPicturesActivity.this);
                        isUpload = 1;
                        locationService.start();
                    }
                }
            });

            holder.mLlDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgList.remove(position);
                    drr.remove(position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.item_grida_image)
            ImageView mItemGridaImage;
            @BindView(R.id.tv_upload)
            TextView mTvUpload;
            @BindView(R.id.ll_del)
            LinearLayout mLlDel;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


}
