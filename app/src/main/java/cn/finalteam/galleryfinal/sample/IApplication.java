/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.galleryfinal.sample;

import android.app.Application;
import android.graphics.Bitmap;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.imageloader.uil.UILImageLoader;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.GalleryTheme;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/18 下午1:45
 */
public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置主题
        GalleryTheme theme = new GalleryTheme.Builder()
                .build();

        //配置全局的功能配置，在GalleryFinal中局部配置和全局配置项不同是优选局部配置项，
        //eg.全局setEnableCamera(false)而局部setEnableCamera(true)启动GalleryFinal的时候相机是开启状态的
        //在application设置了全局的FunctionConfig，启动GaleryFinal时默认使用全局的FunctionConfig
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new UILImageLoader(), theme)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .build();
        Fresco.initialize(this, config);
    }
}
