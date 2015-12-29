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

## Updating V1.4.0

* ……


## 截图展示
Demo apk二维码地址：
![DEMO APK](images/gallery_final_qrcode.png)
![](images/functions.jpg)

![](images/gallery_selects.jpg)       ![](images/gallery_edit.jpg)


# GalleryFinal使用方法

## 下载GalleryFinal
通过Gradle抓取:

```gradle
compile 'cn.finalteam:galleryfinal:1.4.0'
compile 'com.android.support:support-v4:23.1.1'
```


## 具体使用
1、通过gradle把GalleryFinal添加到你的项目里

2、选择图片加载器

* **UIL**

```gradle
compile 'cn.finalteam:gf-loader-uil:1.4.0'
```

* **Glide**

```gradle
compile 'cn.finalteam:gf-loader-glide:1.4.0'
```

* **Picasso**

```gradle
compile 'cn.finalteam:gf-loader-picasso:1.4.0'
```

* **fresco**
 
```gradle
compile 'cn.finalteam:gf-loader-fresco:1.4.0'
```

* **xUtils**

```gradle
compile 'cn.finalteam:gf-loader-xutils:1.4.0'
```

* **xUitls2**

见demo


* **自定义**

自定义步骤：

1)、实现ImageLoader接口

2)、在displayImage方法中实现图片加载，**这个需要注意的是一定要禁止缓存到本地和禁止缓存到内存**

3)、设置请求图片目标大小。displayImage方法中已经给出了width和height

4)、设置默认图和请求图片清晰度，建议把图片请求清晰度调整为Bitmap.Config.RGB_565

* ……

3、启动GalleryFinal

在GalleryFinal 1.3.0版本中相册、拍照、裁剪和图片编辑功能可独立使用

* 打开相册（含GalleryFinal所有功能，可通过FunctionConfig配置）

```java
FunctionConfig config = new FunctionConfig.Builder(MainActivity.this)
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
	//.setTakePhotoFolter(new File(...))
	//.setEditPhotoCacheFolder(new File(...))
	//.filter(mPhotoList)
	.selected(mPhotoList)
	.rotateReplaceSource(false)
	.cropReplaceSource(false)
	...//添加其他配置信息
	.build();
GalleryFinal.openGallery(config);	//打开相册
```

* 使用拍照

```java
FunctionConfig config = new FunctionConfig.Builder(MainActivity.this)                
	...//添加其他配置信息
	.build();
GalleryFinal.openCamera(config);
```

* 使用裁剪

```java
FunctionConfig config = new FunctionConfig.Builder(MainActivity.this)
	...//添加其他配置信息
	.build();
GalleryFinal.openCrop(config);
```

* 使用图片编辑

```java
FunctionConfig config = new FunctionConfig.Builder(MainActivity.this)
	...//添加其他配置信息
	.build();
GalleryFinal.openEdit(config);
```

* **FunctionConfig Builder类说明**

```java
mutiSelect()//配置多选
singleSelect()//配置单选
mutiSelectMaxSize(int maxSize)//配置多选数量
enableEdit()//开启编辑功能
enableCrop()//开启裁剪功能
enableRotate()//开启选择功能
showCamera()//开启相机功能
cropWidth(int width)//裁剪宽度
cropHeight(int height)//裁剪高度
cropSquare()//裁剪正方形
selected(List list)//添加已选列表,只是在列表中默认呗选中不会过滤图片
filter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
takePhotoFolter(File file)//配置拍照保存目录，不做配置的话默认是/sdcard/DCIM/GalleryFinal/
editPhotoCacheFolder(File file)//配置编辑（裁剪和旋转）功能产生的cache文件保存目录，不做配置的话默认保存在/sdcard/GalleryFinal/edittemp/
rotateReplaceSource(boolean)//配置选择图片时是否替换原始图片，默认不替换
cropReplaceSource(boolean)//配置裁剪图片时是否替换原始图片，默认不替换
forceCrop(boolean)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
forceCropEdit(boolean)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
imageloader(ImageLoader)//配置图片加载器
```


4、主题的配置

* 建议在你的app的Application这设置主题

* GalleryFinal默认主题为DEFAULT（深蓝色）,GalleryFinal还自带主题：DARK（黑色主题）、CYAN（蓝绿主题）、ORANGE（橙色主题）、GREEN（绿色主题）和TEAL（青绿色主题），当然也支持自定义主题（Custom Theme）,在自定义主题中用户可以配置字体颜色、图标颜色、更换图标、和背景色

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

2)、使用GalleryFinal主题

```java
GalleryFinal.init(GalleryTheme.CYAN);
...
```
**3)、GalleryTheme 主题配置类说明**

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
## V1.4.0
* 对Fresco image loader的支持
* 添加图片预览功能
* 解决jpeg图片编辑提示bug
* 解决GalleryTheme设置方法没有返回Builder的bug
* 主流Imageloader GalleryFinal配置实现
* onActivityForResult改为事件回调形式
* 优化FunctionConfig配置方式
* 增强各手机兼容性
* 对fresco/glide/picasso/uil/xutils添加gradle引入

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
    
    
