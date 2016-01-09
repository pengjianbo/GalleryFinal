![](images/gallery_final_effect.png)
# GalleryFinal简介
Android自定义相册，实现了拍照、图片选择（单选/多选）、 裁剪（单/多裁剪）、旋转、ImageLoader无绑定任由开发者选择、功能可配置、主题样式可配置。GalleryFinal为你定制相册。

## 为什么要使用GalleryFinal？
* 拍照/选择图片倒立问题
* 市场上各种相机和图片浏览器泛滥导致各种异常问题
* 各种手机兼容性问题引起crash
* 系统Gallery App不能进行多选
* 系统拍照/选择图片/裁剪视乎不太好用
* 系统Gallery App不美观
* ……

###**注：现支持所有主流的ImageLoader，包括Glide/Fresco/Picasso/UIL等，如果你觉得还不满足，欢迎在issues上提问**

## 截图展示
Demo apk二维码地址：
![DEMO APK](images/gallery_final_qrcode.png)
![](images/functions.jpg)

![](images/gallery_selects.jpg)       ![](images/gallery_edit.jpg)


# GalleryFinal使用方法

## 下载GalleryFinal
通过Gradle抓取:

```gradle
compile 'cn.finalteam:galleryfinal:1.4.4'
compile 'com.android.support:support-v4:23.1.1'
```


# 1.4.4更新内容
* 添加PauseOnScrollListener(对滑动ListView ImageLoader优化)
* 添加关闭动画方法


## 具体使用
1、通过gradle把GalleryFinal添加到你的项目里

2、在你的Application中添加配置GallerFinal

```java
//设置主题
//ThemeConfig.CYAN
ThemeConfig theme = new ThemeConfig.Builder()
        ...
        .build();
//配置功能
FunctionConfig functionConfig = new FunctionConfig.Builder()
        .setEnableCamera(true)
        .setEnableEdit(true)
        .setEnableCrop(true)
        .setEnableRotate(true)
        .setCropSquare(true)
        .setEnablePreview(true)
        ...
        .build();

//配置imageloader
ImageLoader imageloader = new UILImageLoader();       
CoreConfig coreConfig = new CoreConfig.Builder(context, imageloader, theme)
        .setDebug(BuildConfig.DEBUG)
        .setFunctionConfig(functionConfig)
        ...
        .build();
GalleryFinal.init(coreConfig);
```

3、选择图片加载器

* **UIL**

```java
public class UILImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Bitmap.Config mImageConfig;

    public UILImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public UILImageLoader(Bitmap.Config config) {
        this.mImageConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(false)
                .bitmapConfig(mImageConfig)
                .build();
        ImageSize imageSize = new ImageSize(width, height);
        ImageLoader.getInstance().displayImage("file://" + path, new ImageViewAware(imageView), options, imageSize, null, null);
    }

    @Override
    public void clearMemoryCache() {

    }
}
```

* **Glide**

```java
public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true)
                //.centerCrop()
                .into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                        imageView.setTag(R.id.adapter_item_tag_key,request);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) imageView.getTag(R.id.adapter_item_tag_key);
                    }
                });
    }

    @Override
    public void clearMemoryCache() {
    }
}

```

* **Picasso**

```java
public class PicassoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Bitmap.Config mConfig;

    public PicassoImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public PicassoImageLoader(Bitmap.Config config) {
        this.mConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Picasso.with(activity)
                .load(new File(path))
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .config(mConfig)
                .resize(width, height)
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
```

* **fresco**
 
```java
public class FrescoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Context context;

    public FrescoImageLoader(Context context) {
        this(context, Bitmap.Config.RGB_565);
    }

    public FrescoImageLoader(Context context, Bitmap.Config config) {
        this.context = context;
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setBitmapsConfig(config)
                .build();
        Fresco.initialize(context, imagePipelineConfig);
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Resources resources = context.getResources();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources)
                .setFadeDuration(300)
                .setPlaceholderImage(defaultDrawable)
                .setFailureImage(defaultDrawable)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        final DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create(hierarchy, context);
        imageView.setOnImageViewListener(new GFImageView.OnImageViewListener() {
            @Override
            public void onDetach() {
                draweeHolder.onDetach();
            }

            @Override
            public void onAttach() {
                draweeHolder.onAttach();
            }

            @Override
            public boolean verifyDrawable(Drawable dr) {
                if (dr == draweeHolder.getHierarchy().getTopLevelDrawable()) {
                    return true;
                }
                return false;
            }
        });
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build();
        displayImage(uri, new ResizeOptions(width, height), imageView, draweeHolder);
    }

    /**
     * 加载远程图片
     *
     * @param url
     * @param imageSize
     */
    private void displayImage(Uri url, ResizeOptions imageSize, final ImageView imageView, final DraweeHolder<GenericDraweeHierarchy> draweeHolder) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(url)
                .setResizeOptions(imageSize)//图片目标大小
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        final DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.getController())
                .setImageRequest(imageRequest)
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
                        CloseableReference<CloseableImage> imageReference = null;
                        try {
                            imageReference = dataSource.getResult();
                            if (imageReference != null) {
                                CloseableImage image = imageReference.get();
                                if (image != null && image instanceof CloseableStaticBitmap) {
                                    CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                    Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                    if (bitmap != null && imageView != null) {
                                        imageView.setImageBitmap(bitmap);
                                    }
                                }
                            }
                        } finally {
                            dataSource.close();
                            CloseableReference.closeSafely(imageReference);
                        }
                    }
                })
                .setTapToRetryEnabled(true)
                .build();
        draweeHolder.setController(controller);
    }

    @Override
    public void clearMemoryCache() {

    }
}
```

* **xUtils3**

```java
public class XUtilsImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Bitmap.Config mImageConfig;

    public XUtilsImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public XUtilsImageLoader(Bitmap.Config config) {
        this.mImageConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        ImageOptions options = new ImageOptions.Builder()
                .setLoadingDrawable(defaultDrawable)
                .setFailureDrawable(defaultDrawable)
                .setConfig(mImageConfig)
                .setSize(width, height)
                .setCrop(true)
                .setUseMemCache(false)
                .build();
        x.image().bind(imageView, "file://" + path, options);
    }

    @Override
    public void clearMemoryCache() {
    }
}

```

* **xUitls2**

```java
public class XUtils2ImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private BitmapUtils bitmapUtils;

    public XUtils2ImageLoader(Context context) {
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(defaultDrawable);
        config.setLoadingDrawable(defaultDrawable);
        config.setBitmapConfig(Bitmap.Config.RGB_565);
        config.setBitmapMaxSize(new BitmapSize(width, height));
        bitmapUtils.display(imageView, "file://" + path, config);
    }

    @Override
    public void clearMemoryCache() {
    }
}
```


* **自定义**

自定义步骤：

1)、实现ImageLoader接口

2)、在displayImage方法中实现图片加载，**这个需要注意的是一定要禁止缓存到本地和禁止缓存到内存**

3)、设置请求图片目标大小。displayImage方法中已经给出了width和height

4)、设置默认图和请求图片清晰度，建议把图片请求清晰度调整为Bitmap.Config.RGB_565避免出现OOM情况

* ……

4、启动GalleryFinal

在GalleryFinal 1.3.0版本中相册、拍照、裁剪和图片编辑功能可独立使用

**REQUEST_CODE_GALLERY 为请求码**
**functionConfig 为功能配置**
**mOnHanlderResultCallback 为请求回调**

* 单选打开相册

```java
GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
//带配置
GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

```

* 多选打开相册
```java
GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
//带配置
FunctionConfig config = new FunctionConfig.Builder(MainActivity.this)
    .setMutiSelectMaxSize(8)
    .build();
GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

```

* 使用拍照

```java
GalleryFinal.openCamera(REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
//带配置
GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
```

* 使用裁剪

```java
GalleryFinal.openCrop(REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
//带配置
GalleryFinal.openCrop(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
```

* 使用图片编辑

```java
GalleryFinal.openEdit(REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
//带配置
GalleryFinal.openEdit(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
```

* **FunctionConfig Builder类说明**

```java
setMutiSelect(boolean)//配置是否多选
setMutiSelectMaxSize(int maxSize)//配置多选数量
setEnableEdit(boolean)//开启编辑功能
setEnableCrop(boolean)//开启裁剪功能
setEnableRotate(boolean)//开启选择功能
setEnableCamera(boolean)//开启相机功能
setCropWidth(int width)//裁剪宽度
setCropHeight(int height)//裁剪高度
setCropSquare(boolean)//裁剪正方形
setSelected(List)//添加已选列表,只是在列表中默认呗选中不会过滤图片
setFilter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
takePhotoFolter(File file)//配置拍照保存目录，不做配置的话默认是/sdcard/DCIM/GalleryFinal/
setRotateReplaceSource(boolean)//配置选择图片时是否替换原始图片，默认不替换
setCropReplaceSource(boolean)//配置裁剪图片时是否替换原始图片，默认不替换
setForceCrop(boolean)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
setForceCropEdit(boolean)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
setEnablePreview(boolean)//是否开启预览功能
```


* **主题的配置**

1)、GalleryFinal默认主题为DEFAULT（深蓝色）,还自带主题：DARK（黑色主题）、CYAN（蓝绿主题）、ORANGE（橙色主题）、GREEN（绿色主题）和TEAL（青绿色主题），当然也支持自定义主题（Custom Theme）,在自定义主题中用户可以配置字体颜色、图标颜色、更换图标、和背景色

* 设置主题

1)、使用自定义主题

```java
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
        //...其他配置
        .build();
GalleryFinal.init(theme);
```
2)、**ThemeConfig类说明**
```java
setTitleBarTextColor//标题栏文本字体颜色
setTitleBarBgColor//标题栏背景颜色
setTitleBarIconColor//标题栏icon颜色，如果设置了标题栏icon，设置setTitleBarIconColor将无效
setCheckNornalColor//选择框未选颜色
setCheckSelectedColor//选择框选中颜色
setCropControlColor//设置裁剪控制点和裁剪框颜色
setFabNornalColor//设置Floating按钮Nornal状态颜色
setFabPressedColor//设置Floating按钮Pressed状态颜色

setIconBack//设置返回按钮icon
setIconCamera//设置相机icon
setIconCrop//设置裁剪icon
setIconRotate//设置选择icon
setIconClear//设置清楚选择按钮icon（标题栏清除选择按钮）
setIconFolderArrow//设置标题栏文件夹下拉arrow图标
setIconDelete//设置多选编辑页删除按钮icon
setIconCheck//设置checkbox和文件夹已选icon
setIconFab//设置Floating按钮icon
setEditPhotoBgTexture//设置图片编辑页面图片margin外背景
setIconPreview设置预览按钮icon
setPreviewBg设置预览页背景
```

* **CoreConfig配置类**
```java
Builder(Context context, ImageLoader imageLoader, ThemeConfig themeConfig) //构建CoreConfig所需ImageLoader和ThemeConfig
setDebug //debug开关
setEditPhotoCacheFolder(File file)//配置编辑（裁剪和旋转）功能产生的cache文件保存目录，不做配置的话默认保存在/sdcard/GalleryFinal/edittemp/
setTakePhotoFolder设置拍照保存目录，默认是/sdcard/DICM/GalleryFinal/
setFunctionConfig //配置全局GalleryFinal功能
setNoAnimcation//关闭动画
setPauseOnScrollListener//设置imageloader滑动加载图片优化OnScrollListener,根据选择的ImageLoader来选择PauseOnScrollListener
```

5、如果你还想更深度的定制页面效果可以把资源文件名字定义成Gallery资源名已达到覆盖效果。如有不理解可以联系我。


# 权限
```xml
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

# 混淆配置   
```properties
-keep class cn.finalteam.galleryfinal.widget.*{*;}
-keep class cn.finalteam.galleryfinal.widget.crop.*{*;}
-keep class cn.finalteam.galleryfinal.widget.zoonview.*{*;}
```


# 更新日志
## V1.4.4
* 添加PauseOnScrollListener(对滑动ListView ImageLoader优化)
* 添加关闭动画方法

## V1.4.3
* 多选传递maxsize

## V1.4.2
* 添加Android 6.0的支持
* 添加动画或特效

## V1.4.1
* 解决创建文件夹bug

## V1.4.0
* 对Fresco image loader的支持
* 添加图片预览功能
* 解决jpeg图片编辑提示bug
* 解决ThemeConfig设置方法没有返回Builder的bug
* 主流Imageloader GalleryFinal配置实现
* onActivityForResult改为事件回调形式
* 优化FunctionConfig配置方式
* 增强各手机兼容性

## V1.3.0
* 代码设置主题颜色
* 支持对外打开相册
* 支持对外打开编辑
* 支持对外打开裁剪
* 非png和非jpg图片不能编辑
* 解决三星部分机型编辑出现OOM情况
* 添加旋转是否覆盖源文件（默认不覆盖）
* 添加裁剪是否覆盖源文件（默认不覆盖）
* 添加必须裁剪功能

## V1.2.7.1
* 将不存在或已损坏的图片移除

## V1.2.7	
* 取消自动清理缓存
* 解决单选编辑页拍照时返回多张图片bug

## V1.2.6
* 去掉V4包

## V1.2.5
* 自定义缓存目录
* 添加已选集合

## V1.2.4
* 解决多选且不裁剪确认按钮无响应问题

## V1.2.3
* 解决筛选器无效问题

## V1.2.2
* 解决单选拍照问题
* 提高稳定性

## V1.2.0
* 提高图片清晰度
* 支持图片手动缩放
* fix权限问题
* 优化图片旋转
* fix二次裁剪问题
* fix多次旋转后图片不清晰问题
* 添加图片选择过滤
* 添加清理缓存
* 提高体验效果和修改UI

## V1.1.0
* UI重改
* 多选图片裁剪
* 所有功能可配置
* 优化图片裁剪
* 解决OOM情况
* 图片手动选择
* 支持汉语和英语

# 感谢（Thanks）
* 图片裁剪[android-crop](https://github.com/jdamcd/android-crop)
* 图片缩放[PhotoView](https://github.com/chrisbanes/PhotoView)

# 关于作者
* **QQ:**172340021   
* **QQ群:**218801658  
* **Email:**<pengjianbo@finalteam.cn>



License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
    
