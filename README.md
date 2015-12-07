![](images/gallery_final_effect.png)
# GalleryFinal简介
Android自定义相册，实现了拍照、图片选择（单选/多选）、裁剪（单/多裁剪）、旋转、ImageLoader无绑定任由开发者选择

#GalleryFinal intro
GalleryFinal is an Android custom gallery.It can take photo,choose and clip pictures(single/multiple),rotate,ImageLoader unbinding

##为什么要使用GalleryFinal？
* 拍照/选择图片倒立问题
* 市场上各种相机和图片浏览器泛滥导致各种异常问题
* 各种手机兼容性问题引起crash
* 系统Gallery App不能进行多选
* 系统拍照/选择图片/裁剪视乎不太好用
* 系统Gallery App不美观
* ……

# Why GalleryFinal?
* GalleryFinal solved the "photos are upside down" problem
* GalleryFinal solved some Exceptions cause of different camera and image explorer not compatible
* GalleryFinal solved some crash due to different cellphone adapted
* The System Gallery cannot do multiple choice
* The System camera/gallery/slip didn't work well
* The System Gallery is not smart

##更新内容
* UI重改
* 多选图片裁剪
* 所有功能可配置
* 优化图片裁剪
* 解决OOM情况
* 图片手动选择
* 支持汉语和英语

##update
* UI upgrade
* Slip now can choose more than one image
* Every feature was configurable
* Optimize slip
* Solve OOM problem
* Images manual chosen
* Support Chinese and English

##截图展示
Demo apk二维码地址：
![DEMO APK](images/gallery_final_qrcode.png)
![](images/functions.jpg)

![](images/gallery_selects.jpg)       ![](images/gallery_edit.jpg)


#GalleryFinal使用方法

##下载GalleryFinal
通过Gradle抓取:

```gradle
compile 'cn.finalteam:galleryfinal:1.1.0'
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
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .centerCrop()
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
                .into(imageView);
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

* xUtils3

```java
public class GalleryImageLoader implements cn.finalteam.galleryfinal.ImageLoader {
    
    @Override
    public void displayImage(final ImageView imageView, String url) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, options);
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
    
    
    https://github.com/AmineChikhaoui/PhotoEdit