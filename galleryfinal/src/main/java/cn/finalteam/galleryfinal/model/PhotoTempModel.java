package cn.finalteam.galleryfinal.model;

import java.io.Serializable;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/1/20 0020 20:22
 */
public class PhotoTempModel implements Serializable{
    private int orientation;
    private String sourcePath;

    public PhotoTempModel(String path) {
        sourcePath = path;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
