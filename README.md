![](images/gallery_final_effect.png)
# GalleryFinal简介
Android自定义相册，实现了拍照、图片选择（单选/多选）、裁剪（单/多裁剪）、旋转、缩放、ImageLoader无绑定任由开发者选择

#GalleryFinal intro
GalleryFinal is an Android custom gallery.It can take photo,choose and clip pictures(single/multiple),rotate,zoom,ImageLoader unbinding

##为什么要使用GalleryFinal？
* 拍照/选择图片倒立问题
* 市场上各种相机和图片浏览器泛滥导致各种异常问题
* 各种手机兼容性问题引起crash
* 系统Gallery App不能进行多选
* 系统拍照/选择图片/裁剪视乎不太好用
* 系统Gallery App不美观
* ……

##Update V1.3.0
* 代码设置主题颜色
* 支持对外打开相册
* 支持对外打开编辑
* 支持对外打开相机

##截图展示
Demo apk二维码地址：
![DEMO APK](images/gallery_final_qrcode.png)
![](images/functions.jpg)

![](images/gallery_selects.jpg)       ![](images/gallery_edit.jpg)


#GalleryFinal使用方法

##下载GalleryFinal
通过Gradle抓取:

```gradle
compile 'cn.finalteam:galleryfinal:1.2.7.1'
```

##具体使用
1、通过gradle把GalleryFinal添加到你的项目里

2、选择图片加载器

* UIL

```java
public class UILImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        ImageSize size = new ImageSize(width, height);
        ImageLoader.getInstance().displayImage("file://" + path, imageView, size);
    }

    @Override
    public void clearMemoryCache() {
    }
}
```

* Glide

```java
public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .error(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true)
                //.centerCrop()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
```

* Picasso

```java
public class PicassoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Picasso.with(activity)
                .load(new File(path))
                .placeholder(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .error(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
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

* xUtils3

```java
public class XUtils3ImageLoader implements cn.finalteam.galleryfinal.ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        ImageOptions options = new ImageOptions.Builder()
                .setLoadingDrawableId(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .setFailureDrawableId(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .setConfig(Bitmap.Config.RGB_565)
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

* xUitls

```java
public class XUtilsImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private BitmapUtils bitmapUtils;
    private Drawable defaultImage;

    public XUtilsImageLoader(Context context) {
        bitmapUtils = new BitmapUtils(context);
        defaultImage = context.getResources().getDrawable(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo);
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(defaultImage);
        config.setLoadingDrawable(defaultImage);
        config.setBitmapConfig(Bitmap.Config.RGB_565);
        config.setBitmapMaxSize(new BitmapSize(width, height));
        bitmapUtils.display(imageView, "file://" + path, config);
    }

    @Override
    public void clearMemoryCache() {
    }
}
```

* ……

3、打开Gallery

```java
GalleryConfig config = new GalleryConfig.Builder(MainActivity.this)
                        .mutiSelect()
                        .mutiSelectMaxSize(8)
                        .enableEdit()
                        .enableCrop()
                        .enableRotate()
                        .showCamera()
                        .imageloader(new UILImageLoader())
                        .cropSquare()
                        .cropWidth(50)
                        .cropHeight(50)
                        //.setTakePhotoFolter(new File(...)) //自定义拍照存储目录
                        //.setEditPhotoCacheFolder(new File(...)) //自定义编辑产生的图片缓存目录
                        //.filter(mPhotoList)
                        .selected(mPhotoList)
                        .build();
GalleryFinal.open(config);
```


4、配置GalleryFinal Activity样式

* 在styles.xml中添加

```xml
    <style name="PhotoActivityTheme">
        <item name="colorTheme">@color/colorPrimary</item>
        <item name="colorThemeDark">@color/colorPrimaryDark</item>
    </style>
```
colorTheme为主题色，colorThemeDark为主题加深色

5、如果你还想更深度的定制页面效果可以把资源文件名字定义成Gallery资源名已达到覆盖效果。如有不理解可以联系我。

#混淆配置

```properties
-keep class cn.finalteam.galleryfinal.widget.*{*;}
-keep class cn.finalteam.galleryfinal.widget.crop.*{*;}
```

#更新日志
##V1.2.7.1
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

##V1.2.0
* 提高图片清晰度
* 支持图片手动缩放
* fix权限问题
* 优化图片旋转
* fix二次裁剪问题
* fix多次旋转后图片不清晰问题
* 添加图片选择过滤
* 添加清理缓存
* 提高体验效果和修改UI

##V1.1.0
* UI重改
* 多选图片裁剪
* 所有功能可配置
* 优化图片裁剪
* 解决OOM情况
* 图片手动选择
* 支持汉语和英语

#关于作者
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
    
    
