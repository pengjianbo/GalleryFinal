package cn.finalteam.galleryfinal.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.sample.loader.GlideImageLoader;
import cn.finalteam.galleryfinal.sample.loader.PicassoImageLoader;
import cn.finalteam.galleryfinal.sample.loader.UILImageLoader;
import cn.finalteam.galleryfinal.sample.loader.XUtils3ImageLoader;
import cn.finalteam.galleryfinal.sample.loader.XUtilsImageLoader;
import cn.finalteam.galleryfinal.widget.HorizontalListView;
import cn.finalteam.toolsfinal.Logger;
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
                } else {
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
                } else {
                    mLlCropSize.setVisibility(View.GONE);
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
                //        .build();
                //GalleryFinal.open(config);
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
                }

                if (mCbShowCamera.isChecked()) {
                    builder.showCamera();
                }

                GalleryConfig config = builder.build();
                GalleryFinal.open(config);
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
}
