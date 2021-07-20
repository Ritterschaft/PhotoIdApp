package com.ruoqian.brainidphoto.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.longtu.base.util.FileUtils;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.engine.PictureSelectorEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureSelectorUIStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.ruoqian.brainidphoto.R;
import com.ruoqian.brainidphoto.engine.GlideEngine;
import com.ruoqian.brainidphoto.engine.PictureSelectorEngineImp;
import com.ruoqian.lib.activity.BaseActivity;
import com.ruoqian.lib.activity.BaseApplication;
import com.ruoqian.lib.utils.DateUtils;
import com.ruoqian.lib.utils.UriUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IdphotoCameraActivity extends BaseActivity implements IApp {

    private CameraView camera;
    private ImageButton ibtnClose;
    private TextView tvCameraTitle;
    private ImageButton ibtnFlash;
    private ImageView ivAlbum;
    private ImageView ivCamera;
    private ImageView ivSwitch;
    private View viewStatus;

    private String name;

    private String cameraPath;

    private PictureSelectorUIStyle uiStyle;

    private final static int IDPHOTO_ALBUM = 30001;

    /**
     * 选择图片列表
     */
    private List<LocalMedia> selectList = new ArrayList<>();

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_idphoto_camera);
    }

    @Override
    public void initViews() {
        camera = findViewById(R.id.camera);
        ibtnClose = findViewById(R.id.ibtnClose);
        tvCameraTitle = findViewById(R.id.tvCameraTitle);
        ibtnFlash = findViewById(R.id.ibtnFlash);
        ivAlbum = findViewById(R.id.ivAlbum);
        ivCamera = findViewById(R.id.ivCamera);
        ivSwitch = findViewById(R.id.ivSwitch);
        viewStatus = findViewById(R.id.viewStatus);
    }

    @Override
    public void initDatas() {
        name = getIntent().getStringExtra("name");
        if (!StringUtils.isEmpty(name)) {
            tvCameraTitle.setText(name);
        }
        cameraPath = BaseApplication.AppPath + "/camera/";
        if (!FileUtils.isFolderExist(cameraPath)) {
            FileUtils.makeFolders(cameraPath);
        }
        cameraPath = cameraPath + "IMG_" + DateUtils.getCurrentTime() + PictureMimeType.JPEG;
        setStautsHeight(viewStatus);
        intPicture();
    }

    private void intPicture() {
        PictureAppMaster.getInstance().setApp(this);
        uiStyle = PictureSelectorUIStyle.ofSelectTotalStyle();
        uiStyle.picture_statusBarBackgroundColor = ContextCompat.getColor(this, R.color.theme_blue);
        uiStyle.picture_container_backgroundColor = Color.parseColor("#FFFFFF");
        uiStyle.picture_navBarColor = ContextCompat.getColor(this, R.color.theme_blue);
        uiStyle.picture_top_titleBarBackgroundColor = ContextCompat.getColor(this, R.color.theme_blue);
        uiStyle.picture_top_titleRightTextColor = new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF")};
        uiStyle.picture_top_titleTextColor = Color.parseColor("#FFFFFF");
        uiStyle.picture_top_leftBack = com.luck.picture.lib.R.drawable.picture_icon_back;
        uiStyle.picture_top_titleArrowUpDrawable = com.luck.picture.lib.R.drawable.picture_icon_arrow_up;
        uiStyle.picture_top_titleArrowDownDrawable = com.luck.picture.lib.R.drawable.picture_icon_arrow_down;
        uiStyle.picture_album_checkDotStyle = R.drawable.picture_oval;
        uiStyle.picture_check_style = R.drawable.picture_checkbox_blue_selector;
        uiStyle.picture_bottom_previewTextColor = new int[]{Color.parseColor("#9b9b9b"), Color.parseColor("#1977ff")};
        uiStyle.picture_bottom_completeTextColor = new int[]{Color.parseColor("#9b9b9b"), Color.parseColor("#1977ff")};
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        camera.setLifecycleOwner(this);
        ibtnClose.setOnClickListener(this);
        tvCameraTitle.setOnClickListener(this);
        ibtnFlash.setOnClickListener(this);
        ivAlbum.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivSwitch.setOnClickListener(this);
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                // A Picture was taken!
                if (result != null) {
                    result.toFile(new File(cameraPath), fileCallback);
                }
            }

            @Override
            public void onVideoTaken(VideoResult result) {
                // A Video was taken!
            }
        });
    }

    @Override
    public void ResumeDatas() {
        camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnClose:
                finish();
                break;
            case R.id.ivSwitch:
                if (camera.getFacing() == Facing.BACK) {
                    camera.setFacing(Facing.FRONT);
                } else {
                    camera.setFacing(Facing.BACK);
                }
                break;
            case R.id.ibtnFlash:
                if (camera.getFlash() == Flash.OFF) {
                    camera.setFlash(Flash.ON);
                    ibtnFlash.setImageResource(R.mipmap.icon_flash_on);
                } else {
                    camera.setFlash(Flash.OFF);
                    ibtnFlash.setImageResource(R.mipmap.icon_flash_off);
                }
                break;
            case R.id.ivCamera:
                camera.takePicture();
                break;
            case R.id.ivAlbum:
                permissionType = 1;
                initPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
        }
    }

    private void goPictureSelector() {

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
//                .theme(R.style.picture_QQ_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .setPictureUIStyle(uiStyle)
                //.setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //.setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle())// 自定义相册启动退出动画
//                .isWeChatStyle(isWeChatStyle)// 是否开启微信图片选择风格
                .isUseCustomCamera(false)// 是否使用自定义相机
//                .setLanguage(language)// 设置语言，默认中文
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
//                .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                //.isSyncCover(true)// 是否强制从MediaStore里同步相册封面，如果相册封面没显示异常则没必要设置
                //.isCameraAroundState(false) // 是否开启前置摄像头，默认false，如果使用系统拍照 可能部分机型会有兼容性问题
                //.isCameraRotateImage(false) // 拍照图片旋转是否自动纠正
                //.isAutoRotating(false)// 压缩时自动纠正有旋转的图片
//                .isMaxSelectEnabledMask(false)// 选择数到了最大阀值列表是否启用蒙层效果
                //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                //.loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                //.setOutputCameraPath(createCustomCameraOutPath())// 自定义相机输出目录
                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
//                .setCaptureLoadingColor(ContextCompat.getColor(getContext(), R.color.app_color_blue))
                .maxSelectNum(5)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .maxVideoSelectNum(1) // 视频最大选择数量
                //.minVideoSelectNum(1)// 视频最小选择数量
                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                .imageSpanCount(3)// 每行显示个数
                //.queryFileSize() // 过滤最大资源,已废弃
                //.filterMinFileSize(5)// 过滤最小资源，单位kb
                //.filterMaxFileSize()// 过滤最大资源，单位kb
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                .isAndroidQTransform(true)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(false)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，裁剪功能将会失效
                .isDisplayOriginalSize(true)// 是否显示原文件大小，isOriginalImageControl true有效
                .isEditorImage(false)//是否编辑图片
                //.isAutoScalePreviewImage(true)// 如果图片宽度不能充满屏幕则自动处理成充满模式
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                //.bindCustomPermissionsObtainListener(new MyPermissionsObtainCallback())// 自定义权限拦截
                //.bindCustomChooseLimitListener(new MyChooseLimitCallback()) // 自定义选择限制条件Dialog
                //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .isSingleDirectReturn(false)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(true)// 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                //.queryMimeTypeConditions(PictureMimeType.ofWEBP())
                .isEnablePreviewAudio(true) // 是否可播放音频
                .isCamera(false)// 是否显示拍照按钮
                //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG) // 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                .setCameraImageFormat(PictureMimeType.JPEG) // 相机图片格式后缀,默认.jpeg
                .setCameraVideoFormat(PictureMimeType.MP4)// 相机视频格式后缀,默认.mp4
                .setCameraAudioFormat(PictureMimeType.AMR)// 录音音频格式后缀,默认.amr
                .isEnableCrop(false)// 是否裁剪
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                .isCompress(false)// 是否压缩
                //.compressFocusAlpha(true)// 压缩时是否开启透明通道
                //.compressEngine(ImageCompressEngine.createCompressEngine()) // 自定义压缩引擎
                //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
//                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(!cb_hide.isChecked())// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                //.isWebp(false)// 是否显示webp图片,默认显示
                //.isBmp(false)//是否显示bmp图片,默认显示
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                //.freeStyleCropMode(OverlayView.DEFAULT_FREESTYLE_CROP_MODE)// 裁剪框拖动模式
                .isCropDragSmoothToCenter(true)// 裁剪框拖动时图片自动跟随居中
                .circleDimmedLayer(false)// 是否圆形裁剪
                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
//                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                .isOpenClickSound(cb_voice.isChecked())// 是否开启点击声音
//                .selectionData(mAdapter.getData())// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //.videoMinSecond(10)// 查询多少秒以内的视频
                //.videoMaxSecond(15)// 查询多少秒以内的视频
                //.recordVideoSecond(10)//录制视频秒数 默认60s
                //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
//                .cutOutQuality(90)// 裁剪输出质量 默认100
                //.cutCompressFormat(Bitmap.CompressFormat.PNG.name())//裁剪图片输出Format格式，默认JPEG
                .minimumCompressSize(1000)// 小于多少kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                //.forResult(new MyResultCallback(mAdapter));
                .forResult(IDPHOTO_ALBUM);
    }

    @Override
    protected void onPermission() {
        if (permissionType == 1) {
            goPictureSelector();
        }
    }

    private FileCallback fileCallback = new FileCallback() {
        @Override
        public void onFileReady(@Nullable File file) {
            if (file != null) {
                backResult(file.getAbsolutePath());
            }
        }
    };

    private void backResult(String path) {
        intent = new Intent();
        intent.putExtra("cameraPath", path);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        return new PictureSelectorEngineImp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IDPHOTO_ALBUM:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    setAlbumPhoto();
                    break;
            }
        }
    }

    private void setAlbumPhoto() {
        if (FileUtils.isFileExist(cameraPath)) {
            FileUtils.deleteFile(cameraPath);
        }
        if (selectList != null && selectList.size() > 0) {
            for (LocalMedia media : selectList) {
                String imgPath = media.getPath();
                if (!StringUtils.isEmpty(imgPath)) {
                    imgPath = imgPath.startsWith("content://") ? UriUtils.getFileAbsolutePath(this, Uri.parse(imgPath)) : imgPath;
                    if (!StringUtils.isEmpty(imgPath)) {
                        FileUtils.copyFile(imgPath, cameraPath);
                    }
                }
            }
        }
        if (!FileUtils.isFileExist(cameraPath)) {
            ToastUtils.show(this, "选择照片已损坏！");
            return;
        }
        backResult(cameraPath);
    }
}