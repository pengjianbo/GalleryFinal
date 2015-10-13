# GalleryFinal简介
Android相册自定义，包括拍照、图片选择（单选/多选）和裁剪。

##为什么要使用GalleryFinal？
* 拍照/选择图片倒立问题
* 市场上各种相机和图片浏览器泛滥和各种异常问题
* 各种手机兼容性问题引起crash
* 系统Gallery App不能进行多选
* 拍照/选择图片/裁剪视乎不太好用
* 系统Gallery App不美观
* ……

#GalleryFinal使用方法

##下载GalleryFinal
通过Gradle抓取:

```gradle
compile 'cn.finalteam:galleryfinal:1.0.0'
```
##截图展示
Demo apk二维码地址：![DEMO APK](images/gallery_final_qrcode.png)

![](images/gallery_final.gif)

##具体使用
1、通过gradle或jar把GalleryFinal添加到你的项目里

2、你的项目必须实现ImageLoader接口（demo中有Universal-Image-Loader实现方法）

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
3、打开相册（拍照、选择图片）

* 单选

```java
GalleryHelper.openGallerySingle(context, crop, new GalleryImageLoader());
```
第一个参数Context，第二个参数是否裁剪，第三个参数图片加载实现类

* 多选

```java
GalleryHelper.openGalleryMuti(context, limit, new GalleryImageLoader());
```
第一个参数Context，第二个参数是选择数量，第三个参数图片加载实现类

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