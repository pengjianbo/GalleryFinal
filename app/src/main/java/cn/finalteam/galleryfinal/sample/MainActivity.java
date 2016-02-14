package cn.finalteam.galleryfinal.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.sample.listener.GlidePauseOnScrollListener;
import cn.finalteam.galleryfinal.sample.listener.PicassoPauseOnScrollListener;
import cn.finalteam.galleryfinal.sample.listener.UILPauseOnScrollListener;
import cn.finalteam.galleryfinal.sample.loader.FrescoImageLoader;
import cn.finalteam.galleryfinal.sample.loader.GlideImageLoader;
import cn.finalteam.galleryfinal.sample.loader.PicassoImageLoader;
import cn.finalteam.galleryfinal.sample.loader.UILImageLoader;
import cn.finalteam.galleryfinal.sample.loader.XUtils2ImageLoader;
import cn.finalteam.galleryfinal.sample.loader.XUtilsImageLoader;
import cn.finalteam.galleryfinal.widget.HorizontalListView;
import com.baoyz.actionsheet.ActionSheet;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.xutils.x;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;

    @Bind(R.id.rb_uil)
    RadioButton mRbUil;
    @Bind(R.id.rb_glide)
    RadioButton mRbGlide;
    @Bind(R.id.rb_picasso)
    RadioButton mRbPicasso;
    @Bind(R.id.rb_single_select)
    RadioButton mRbSingleSelect;
    @Bind(R.id.rb_muti_select)
    RadioButton mRbMutiSelect;
    @Bind(R.id.et_max_size)
    EditText mEtMaxSize;
    @Bind(R.id.btn_open_gallery)
    Button mBtnOpenGallery;
    @Bind(R.id.lv_photo)
    HorizontalListView mLvPhoto;
    @Bind(R.id.cb_edit)
    CheckBox mCbEdit;
    @Bind(R.id.cb_crop)
    CheckBox mCbCrop;
    @Bind(R.id.cb_rotate)
    CheckBox mCbRotate;
    @Bind(R.id.cb_show_camera)
    CheckBox mCbShowCamera;
    @Bind(R.id.ll_max_size)
    LinearLayout mLlMaxSize;
    @Bind(R.id.ll_edit)
    LinearLayout mLlEdit;
    @Bind(R.id.rb_xutils)
    RadioButton mRbXutils;
    @Bind(R.id.rb_xutils3)
    RadioButton mRbXutils3;
    @Bind(R.id.et_crop_width)
    EditText mEtCropWidth;
    @Bind(R.id.et_crop_height)
    EditText mEtCropHeight;
    @Bind(R.id.ll_crop_size)
    LinearLayout mLlCropSize;
    @Bind(R.id.cb_crop_square)
    CheckBox mCbCropSquare;
    @Bind(R.id.rb_theme_default)
    RadioButton mRbThemeDefault;
    @Bind(R.id.rb_theme_dark)
    RadioButton mRbThemeDark;
    @Bind(R.id.rb_theme_cyan)
    RadioButton mRbThemeCyan;
    @Bind(R.id.rb_theme_orange)
    RadioButton mRbThemeOrange;
    @Bind(R.id.rb_theme_green)
    RadioButton mRbThemeGreen;
    @Bind(R.id.rb_theme_teal)
    RadioButton mRbThemeTeal;
    @Bind(R.id.rb_theme_custom)
    RadioButton mRbThemeCustom;
    @Bind(R.id.cb_crop_replace_source)
    CheckBox mCbCropReplaceSource;
    @Bind(R.id.cb_rotate_replace_source)
    CheckBox mCbRotateReplaceSource;
    @Bind(R.id.cb_open_force_crop)
    CheckBox mCbOpenForceCrop;
    @Bind(R.id.cb_open_force_crop_edit)
    CheckBox mCbOpenForceCropEdit;
    @Bind(R.id.ll_force_crop)
    LinearLayout mLlForceCrop;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rb_fresco)
    RadioButton mRbFresco;
    @Bind(R.id.cb_preview)
    CheckBox mCbPreview;
    @Bind(R.id.cb_no_animation)
    CheckBox mCbNoAnimation;

    private List<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;
    private Button mOpenGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLvPhoto = (HorizontalListView) findViewById(R.id.lv_photo);
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, mPhotoList);
        mLvPhoto.setAdapter(mChoosePhotoListAdapter);
        mOpenGallery = (Button) findViewById(R.id.btn_open_gallery);
        mRbMutiSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLlMaxSize.setVisibility(View.VISIBLE);
                    mLlForceCrop.setVisibility(View.GONE);
                } else {
                    if (mCbEdit.isChecked()) {
                        mLlForceCrop.setVisibility(View.VISIBLE);
                    }
                    mLlMaxSize.setVisibility(View.GONE);
                }
            }
        });
        mCbEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLlEdit.setVisibility(View.VISIBLE);
                } else {
                    mLlEdit.setVisibility(View.GONE);
                }
            }
        });
        mCbCrop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLlCropSize.setVisibility(View.VISIBLE);
                    mCbCropReplaceSource.setVisibility(View.VISIBLE);
                    if (mRbSingleSelect.isChecked()) {
                        mLlForceCrop.setVisibility(View.VISIBLE);
                    }
                } else {
                    mLlCropSize.setVisibility(View.GONE);
                    mCbCropReplaceSource.setVisibility(View.INVISIBLE);
                    mLlForceCrop.setVisibility(View.GONE);
                }
            }
        });
        mCbRotate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCbRotateReplaceSource.setVisibility(View.VISIBLE);
                } else {
                    mCbRotateReplaceSource.setVisibility(View.INVISIBLE);
                }
            }
        });
        mCbOpenForceCrop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCbOpenForceCropEdit.setVisibility(View.VISIBLE);
                } else {
                    mCbOpenForceCropEdit.setVisibility(View.INVISIBLE);
                }
            }
        });
        mOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //公共配置都可以在application中配置，这里只是为了代码演示而写在此处
                ThemeConfig themeConfig = null;

                if (mRbThemeDefault.isChecked()) {
                    themeConfig = ThemeConfig.DEFAULT;
                } else if (mRbThemeDark.isChecked()) {
                    themeConfig = ThemeConfig.DARK;
                } else if (mRbThemeCyan.isChecked()) {
                    themeConfig = ThemeConfig.CYAN;
                } else if (mRbThemeOrange.isChecked()) {
                    themeConfig = ThemeConfig.ORANGE;
                } else if (mRbThemeGreen.isChecked()) {
                    themeConfig = ThemeConfig.GREEN;
                } else if (mRbThemeTeal.isChecked()) {
                    themeConfig = ThemeConfig.TEAL;
                } else if (mRbThemeCustom.isChecked()) {
                    ThemeConfig theme = new ThemeConfig.Builder()
                            .setTitleBarBgColor(Color.rgb(0xFF, 0x57, 0x22))
                            .setTitleBarTextColor(Color.BLACK)
                            .setTitleBarIconColor(Color.BLACK)
                            .setFabNornalColor(Color.RED)
                            .setFabPressedColor(Color.BLUE)
                            .setCheckNornalColor(Color.WHITE)
                            .setCheckSelectedColor(Color.BLACK)
                            .setIconBack(R.mipmap.ic_action_previous_item)
                            .setIconRotate(R.mipmap.ic_action_repeat)
                            .setIconCrop(R.mipmap.ic_action_crop)
                            .setIconCamera(R.mipmap.ic_action_camera)
                            .build();
                    themeConfig = theme;
                }

                FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
                cn.finalteam.galleryfinal.ImageLoader imageLoader;
                PauseOnScrollListener pauseOnScrollListener = null;
                if (mRbUil.isChecked()) {
                    imageLoader = new UILImageLoader();
                    pauseOnScrollListener = new UILPauseOnScrollListener(false, true);
                } else if (mRbXutils.isChecked()) {
                    imageLoader = new XUtils2ImageLoader(MainActivity.this);
                } else if (mRbXutils3.isChecked()) {
                    imageLoader = new XUtilsImageLoader();
                } else if (mRbGlide.isChecked()) {
                    imageLoader = new GlideImageLoader();
                    pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);
                } else if (mRbFresco.isChecked()) {
                    imageLoader = new FrescoImageLoader(MainActivity.this);
                } else {
                    imageLoader = new PicassoImageLoader();
                    pauseOnScrollListener = new PicassoPauseOnScrollListener(false, true);
                }

                boolean muti = false;
                if (mRbSingleSelect.isChecked()) {
                    muti = false;
                } else {
                    muti = true;
                    if (TextUtils.isEmpty(mEtMaxSize.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "请输入MaxSize", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int maxSize = Integer.parseInt(mEtMaxSize.getText().toString());
                    functionConfigBuilder.setMutiSelectMaxSize(maxSize);
                }
                final boolean mutiSelect = muti;

                if (mCbEdit.isChecked()) {
                    functionConfigBuilder.setEnableEdit(true);
                }

                if (mCbRotate.isChecked()) {
                    functionConfigBuilder.setEnableRotate(true);
                    if (mCbRotateReplaceSource.isChecked()) {
                        functionConfigBuilder.setRotateReplaceSource(true);
                    }
                }

                if (mCbCrop.isChecked()) {
                    functionConfigBuilder.setEnableCrop(true);
                    if (!TextUtils.isEmpty(mEtCropWidth.getText().toString())) {
                        int width = Integer.parseInt(mEtCropWidth.getText().toString());
                        functionConfigBuilder.setCropWidth(width);
                    }

                    if (!TextUtils.isEmpty(mEtCropHeight.getText().toString())) {
                        int height = Integer.parseInt(mEtCropHeight.getText().toString());
                        functionConfigBuilder.setCropHeight(height);
                    }

                    if (mCbCropSquare.isChecked()) {
                        functionConfigBuilder.setCropSquare(true);
                    }
                    if (mCbCropReplaceSource.isChecked()) {
                        functionConfigBuilder.setCropReplaceSource(true);
                    }
                    if (mCbOpenForceCrop.isChecked() && mRbSingleSelect.isChecked()) {
                        functionConfigBuilder.setForceCrop(true);
                        if (mCbOpenForceCropEdit.isChecked()) {
                            functionConfigBuilder.setForceCropEdit(true);
                        }
                    }
                }

                if (mCbShowCamera.isChecked()) {
                    functionConfigBuilder.setEnableCamera(true);
                }
                if (mCbPreview.isChecked()) {
                    functionConfigBuilder.setEnablePreview(true);
                }

                functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
                final FunctionConfig functionConfig = functionConfigBuilder.build();


                CoreConfig coreConfig = new CoreConfig.Builder(MainActivity.this, imageLoader, themeConfig)
                        .setFunctionConfig(functionConfig)
                        .setPauseOnScrollListener(pauseOnScrollListener)
                        .setNoAnimcation(mCbNoAnimation.isChecked())
                        .build();
                GalleryFinal.init(coreConfig);

                ActionSheet.createBuilder(MainActivity.this, getSupportFragmentManager())
                        .setCancelButtonTitle("取消(Cancel)")
                        .setOtherButtonTitles("打开相册(Open Gallery)", "拍照(Camera)", "裁剪(Crop)", "编辑(Edit)")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                String path = "/sdcard/pk1-2.jpg";
                                switch (index) {
                                    case 0:
                                        if (mutiSelect) {
                                            GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                        } else {
                                            GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                        }
                                        break;
                                    case 1:
                                        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                                        break;
                                    case 2:
                                        if (new File(path).exists()) {
                                            GalleryFinal.openCrop(REQUEST_CODE_CROP, functionConfig, path, mOnHanlderResultCallback);
                                        } else {
                                            Toast.makeText(MainActivity.this, "图片不存在", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case 3:
                                        if (new File(path).exists()) {
                                            GalleryFinal.openEdit(REQUEST_CODE_EDIT, functionConfig, path, mOnHanlderResultCallback);
                                        } else {
                                            Toast.makeText(MainActivity.this, "图片不存在", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
        initImageLoader(this);
        initFresco();
        x.Ext.init(getApplication());
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                mPhotoList.addAll(resultList);
                mChoosePhotoListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GalleryFinal.GALLERY_REQUEST_CODE) {
//            if (resultCode == GalleryFinal.GALLERY_RESULT_SUCCESS) {
//                List<PhotoInfo> photoInfoList = (List<PhotoInfo>) data.getSerializableExtra(GalleryFinal.GALLERY_RESULT_LIST_DATA);
//                if (photoInfoList != null) {
//                    mPhotoList.addAll(photoInfoList);
//                    mChoosePhotoListAdapter.notifyDataSetChanged();
//                }
//            }
//        }
//    }

    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    private void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .build();
        Fresco.initialize(this, config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_clean_cache) {
            GalleryFinal.cleanCacheFile();
            Toast.makeText(this, "清理成功(Clear success)", Toast.LENGTH_SHORT).show();
        } else {
            //startActivity(new Intent(this, FuncationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
