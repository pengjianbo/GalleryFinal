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

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/18 下午1:45
 */
public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //建议在application中配置
        //设置主题
//        ThemeConfig theme = ThemeConfig.CYAN
//        ThemeConfig theme = new ThemeConfig.Builder()
//                .build();
//        //配置功能
//        FunctionConfig functionConfig = new FunctionConfig.Builder()
//                .setEnableCamera(true)
//                .setEnableEdit(true)
//                .setEnableCrop(true)
//                .setEnableRotate(true)
//                .setCropSquare(true)
//                .setEnablePreview(true)
//                .build();
//        CoreConfig coreConfig = new CoreConfig.Builder(this, new UILImageLoader(), theme)
//                .setFunctionConfig(functionConfig)
//                .setPauseOnScrollListener(new UILPauseOnScrollListener(false, true))
//                .build();
//        GalleryFinal.init(coreConfig);
    }
}
