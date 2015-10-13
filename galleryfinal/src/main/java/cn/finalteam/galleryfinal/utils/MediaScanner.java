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

package cn.finalteam.galleryfinal.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * Desction:相册扫描
 * Author:pengjianbo
 * Date:15/7/31 下午11:27
 */
public class MediaScanner {

    private MediaScannerConnection mediaScanConn = null;
    private MusicSannerClient client = null;
    private String filePath = null;
    private String fileType = null;
    private String[] filePaths = null;

    /**
     * 然后调用MediaScanner.scanFile("/sdcard/2.mp3");
     */
    public MediaScanner(Context context) {
        if (client == null) {
            client = new MusicSannerClient();
        }

        if (mediaScanConn == null) {
            mediaScanConn = new MediaScannerConnection(context, client);
        }
    }

    class MusicSannerClient implements MediaScannerConnection.MediaScannerConnectionClient {
        public void onMediaScannerConnected() {
            if (filePath != null) {
                mediaScanConn.scanFile(filePath, fileType);
            }

            if (filePaths != null) {
                for (String file : filePaths) {
                    mediaScanConn.scanFile(file, fileType);
                }
            }

            filePath = null;
            fileType = null;
            filePaths = null;
        }

        public void onScanCompleted(String path, Uri uri) {
            mediaScanConn.disconnect();
        }
    }

    /**
     * 扫描文件标签信息
     * @param filePath 文件路径
     * @param fileType 文件类型
     */

    public void scanFile(String filePath, String fileType) {
        this.filePath = filePath;
        this.fileType = fileType;
        //连接之后调用MusicSannerClient的onMediaScannerConnected()方法
        mediaScanConn.connect();
    }

    /**
     * @param filePaths 文件路径
     * @param fileType 文件类型
     */
    public void scanFile(String[] filePaths, String fileType) {
        this.filePaths = filePaths;
        this.fileType = fileType;
        mediaScanConn.connect();
    }

    public void unScanFile(){
        mediaScanConn.disconnect();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
