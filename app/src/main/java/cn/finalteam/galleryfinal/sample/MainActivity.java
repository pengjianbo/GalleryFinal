package cn.finalteam.galleryfinal.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cn.finalteam.galleryfinal.GalleryHelper;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView mIvResult;
    private Button mBtnOpenGallerySingle;
    private Button mBtnOpenGalleryMuti;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GalleryHelper.openGallerySingle(MainActivity.this, true, new GalleryImageLoader());
            }
        });

        mIvResult = (ImageView) findViewById(R.id.iv_result);
        mBtnOpenGallerySingle = (Button) findViewById(R.id.btn_open_gallery_single);
        mBtnOpenGalleryMuti = (Button) findViewById(R.id.btn_open_gallery_muti);
        mBtnOpenGallerySingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryHelper.openGallerySingle(MainActivity.this, true, new GalleryImageLoader());
            }
        });
        mBtnOpenGalleryMuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryHelper.openGalleryMuti(MainActivity.this, 8, new GalleryImageLoader());
            }
        });
        initImageLoader(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == GalleryHelper.GALLERY_REQUEST_CODE) {
            if ( resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS ) {
                PhotoInfo photoInfo = data.getParcelableExtra(GalleryHelper.RESULT_DATA);
                List<PhotoInfo> photoInfoList = (List<PhotoInfo>) data.getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);

                if ( photoInfo != null ) {
                    ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(), mIvResult);
                }

                if ( photoInfoList != null ) {
                    Toast.makeText(this, "选择了" + photoInfoList.size() + "张", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
