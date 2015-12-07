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

package cn.finalteam.galleryfinal;

import android.os.Environment;
import java.io.File;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/2 上午11:36
 */
class Consts {
    public static final String TAKE_PHOTO_FOLDER = "GalleryFinal" + File.separator;
    public static final String PHOTO_EDIT_TEMP_DIR = Environment.getExternalStorageDirectory() + "/GalleryFinal/edittemp/";
}
