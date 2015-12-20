package cn.finalteam.galleryfinal.sample;

import android.content.Context;
import android.content.Intent;
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
import cn.finalteam.galleryfinal.GalleryConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.GalleryTheme;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.sample.loader.GlideImageLoader;
import cn.finalteam.galleryfinal.sample.loader.PicassoImageLoader;
import cn.finalteam.galleryfinal.sample.loader.UILImageLoader;
import cn.finalteam.galleryfinal.sample.loader.XUtils3ImageLoader;
import cn.finalteam.galleryfinal.sample.loader.XUtilsImageLoader;
import cn.finalteam.galleryfinal.widget.HorizontalListView;
import cn.finalteam.toolsfinal.Logger;
import com.baoyz.actionsheet.ActionSheet;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.util.ArrayList;
import java.util.List;
import org.xutils.x;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.rb_uil) RadioButton mRbUil;
    @Bind(R.id.rb_glide) RadioButton mRbGlide;
    @Bind(R.id.rb_picasso) RadioButton mRbPicasso;
    @Bind(R.id.rb_single_select) RadioButton mRbSingleSelect;
    @Bind(R.id.rb_muti_select) RadioButton mRbMutiSelect;
    @Bind(R.id.et_max_size) EditText mEtMaxSize;
    @Bind(R.id.btn_open_gallery) Button mBtnOpenGallery;
    @Bind(R.id.lv_photo) HorizontalListView mLvPhoto;
    @Bind(R.id.cb_edit) CheckBox mCbEdit;
    @Bind(R.id.cb_crop) CheckBox mCbCrop;
    @Bind(R.id.cb_rotate) CheckBox mCbRotate;
    @Bind(R.id.cb_show_camera) CheckBox mCbShowCamera;
    @Bind(R.id.ll_max_size) LinearLayout mLlMaxSize;
    @Bind(R.id.ll_edit) LinearLayout mLlEdit;
    @Bind(R.id.rb_xutils) RadioButton mRbXutils;
    @Bind(R.id.rb_xutils3) RadioButton mRbXutils3;
    @Bind(R.id.et_crop_width) EditText mEtCropWidth;
    @Bind(R.id.et_crop_height) EditText mEtCropHeight;
    @Bind(R.id.ll_crop_size) LinearLayout mLlCropSize;
    @Bind(R.id.cb_crop_square) CheckBox mCbCropSquare;
    @Bind(R.id.rb_theme_default) RadioButton mRbThemeDefault;
    @Bind(R.id.rb_theme_dark) RadioButton mRbThemeDark;
    @Bind(R.id.rb_theme_cyan) RadioButton mRbThemeCyan;
    @Bind(R.id.rb_theme_orange) RadioButton mRbThemeOrange;
    @Bind(R.id.rb_theme_green) RadioButton mRbThemeGreen;
    @Bind(R.id.rb_theme_teal) RadioButton mRbThemeTeal;
    @Bind(R.id.rb_theme_custom) RadioButton mRbThemeCustom;
    @Bind(R.id.cb_crop_replace_source) CheckBox mCbCropReplaceSource;
    @Bind(R.id.cb_rotate_replace_source) CheckBox mCbRotateReplaceSource;
    @Bind(R.id.cb_open_force_crop) CheckBox mCbOpenForceCrop;
    @Bind(R.id.cb_open_force_crop_edit) CheckBox mCbOpenForceCropEdit;
    @Bind(R.id.ll_force_crop) LinearLayout mLlForceCrop;

    private List<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;
    private Button mOpenGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.init("galleryfinal", true);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLvPhoto = (HorizontalListView) findViewById(R.id.lv_photo);
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, mPhotoList);
        mLvPhoto.setAdapter(mChoosePhotoListAdapter);
        x.Ext.init(getApplication());
        mOpenGallery = (Button) findViewById(R.id.btn_open_gallery);
        mRbMutiSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLlMaxSize.setVisibility(View.VISIBLE);
                    mLlForceCrop.setVisibility(View.GONE);
                } else {
                    if ( mCbEdit.isChecked() ) {
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
                //GalleryConfig config = new GalleryConfig.Builder(MainActivity.this)
                //        .mutiSelect()
                //        .mutiSelectMaxSize(8)
                //        .enableEdit()
                //        .enableCrop()
                //        .enableRotate()
                //        .showCamera()
                //        .imageloader(new UILImageLoader())
                //        .cropSquare()
                //        .cropWidth(50)
                //        .cropHeight(50)
                //        //.setTakePhotoFolter(new File(...))
                //        //.setEditPhotoCacheFolder(new File(...))
                //        //.filter(mPhotoList)
                //        .selected(mPhotoList)
                //        .rotateReplaceSource(false)
                //        .cropReplaceSource(false)
                //        .build();
                //GalleryFinal.openGallery(config);

                //配置主题，这个步骤可以放到application中
                if (mRbThemeDefault.isChecked()) {
                    GalleryFinal.init(GalleryTheme.DEFAULT);
                } else if (mRbThemeDark.isChecked()) {
                    GalleryFinal.init(GalleryTheme.DARK);
                } else if (mRbThemeCyan.isChecked()) {
                    GalleryFinal.init(GalleryTheme.CYAN);
                } else if (mRbThemeOrange.isChecked()) {
                    GalleryFinal.init(GalleryTheme.ORANGE);
                } else if (mRbThemeGreen.isChecked()) {
                    GalleryFinal.init(GalleryTheme.GREEN);
                } else if (mRbThemeTeal.isChecked()) {
                    GalleryFinal.init(GalleryTheme.TEAL);
                } else if (mRbThemeCustom.isChecked()) {
                    GalleryTheme theme = new GalleryTheme.Builder()
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
                    GalleryFinal.init(theme);
                }

                GalleryConfig.Builder builder = new GalleryConfig.Builder(MainActivity.this);
                if (mRbUil.isChecked()) {
                    builder.imageloader(new UILImageLoader());
                } else if (mRbXutils.isChecked()) {
                    builder.imageloader(new XUtilsImageLoader(MainActivity.this));
                } else if (mRbXutils3.isChecked()) {
                    builder.imageloader(new XUtils3ImageLoader());
                } else if (mRbGlide.isChecked()) {
                    builder.imageloader(new GlideImageLoader());
                } else {
                    builder.imageloader(new PicassoImageLoader());
                }

                if (mRbSingleSelect.isChecked()) {
                    builder.singleSelect();
                } else {
                    builder.mutiSelect();
                    if (TextUtils.isEmpty(mEtMaxSize.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "请输入MaxSize", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int maxSize = Integer.parseInt(mEtMaxSize.getText().toString());
                    builder.mutiSelectMaxSize(maxSize);
                }

                if (mCbEdit.isChecked()) {
                    builder.enableEdit();
                }

                if (mCbRotate.isChecked()) {
                    builder.enableRotate();
                    if (mCbRotateReplaceSource.isChecked()) {
                        builder.rotateReplaceSource(true);
                    }
                }

                if (mCbCrop.isChecked()) {
                    builder.enableCrop();
                    if (!TextUtils.isEmpty(mEtCropWidth.getText().toString())) {
                        int width = Integer.parseInt(mEtCropWidth.getText().toString());
                        builder.cropWidth(width);
                    }

                    if (!TextUtils.isEmpty(mEtCropHeight.getText().toString())) {
                        int height = Integer.parseInt(mEtCropHeight.getText().toString());
                        builder.cropHeight(height);
                    }

                    if (mCbCropSquare.isChecked()) {
                        builder.cropSquare();
                    }
                    if (mCbCropReplaceSource.isChecked()) {
                        builder.cropReplaceSource(true);
                    }
                    if (mCbOpenForceCrop.isChecked() && mRbSingleSelect.isChecked()) {
                        builder.forceCrop(true);
                        if ( mCbOpenForceCropEdit.isChecked() ) {
                            builder.forceCropEdit(true);
                        }
                    }
                }

                if (mCbShowCamera.isChecked()) {
                    builder.showCamera();
                }

                builder.selected(mPhotoList);//添加过滤集合
                final GalleryConfig config = builder.build();
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
                                switch (index) {
                                    case 0:
                                        GalleryFinal.openGallery(config);
                                        break;
                                    case 1:
                                        GalleryFinal.openCamera(config);
                                        break;
                                    case 2:
                                        GalleryFinal.openCrop(config, "/sdcard/pk1-2.jpg");
                                        break;
                                    case 3:
                                        GalleryFinal.openEdit(config, "/sdcard/pk1-2.jpg");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryFinal.GALLERY_REQUEST_CODE) {
            if (resultCode == GalleryFinal.GALLERY_RESULT_SUCCESS) {
                List<PhotoInfo> photoInfoList = (List<PhotoInfo>) data.getSerializableExtra(GalleryFinal.GALLERY_RESULT_LIST_DATA);
                if (photoInfoList != null) {
                    mPhotoList.addAll(photoInfoList);
                    mChoosePhotoListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public static void initImageLoader(Context context) {
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
        }
        return super.onOptionsItemSelected(item);
    }
}
