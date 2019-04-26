package com.inext.qiniu;

import java.io.File;

/**
 * 图片上传接口_七牛服务器
 *
 * @author wangyudong
 */
public interface UploadQn {


    /**
     * 图片上传
     */
    public String uploadImage(File file, String key);

    /**
     * 图片下载
     */
    public String downloadImage(String fileName);

    /**
     * 图片删除
     */
    public String deleteImage(String key);

    public String createKey();


}
