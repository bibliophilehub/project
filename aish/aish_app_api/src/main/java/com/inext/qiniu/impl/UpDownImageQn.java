package com.inext.qiniu.impl;

import com.google.gson.Gson;
import com.inext.qiniu.QnZone;
import com.inext.qiniu.UploadQn;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

@Component("upDownImageQn")
public class UpDownImageQn implements UploadQn {
    private static Logger logger = Logger.getLogger(UpDownImageQn.class);

    @Value("${qn_ak}")
    private String accessKey;

    @Value("${qn_sk}")
    private String secretKey;

    @Value("${qn_bucket}")
    private String bucket;

    @Value("${domainOfBucket}")
    private String domainOfBucket;

    // 配置
    private Configuration config = new Configuration(QnZone.ZONE0.getZone());

    /**
     * 获取认证
     *
     * @param accessKey
     * @param secretKey
     * @return
     */
    public Auth getAuth(String accessKey, String secretKey) {

        return Auth.create(accessKey, secretKey);

    }

    /**
     * 图片上传
     *
     * @param localFilePath上传文件路径
     * @param key                 上传存储名称
     */
    public String uploadImage(File file, String key) {
        String result = "";
        try {
            // 获取上传管理器
            UploadManager um = new UploadManager(config);
            //key = createKey() + file.getName();
            Response response = um.put(file, key, getAuth(accessKey, secretKey).uploadToken(bucket));
            // 上传成功返回存储路径
            result = key;
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("hash:" + putRet.hash + ";key:" + putRet.key);
            logger.info("upload success!");
        } catch (QiniuException e) {
            e.printStackTrace();
            Response r = e.response;
            logger.error(r.toString());
            logger.error("upload failed");
            result = "error";
        }
        return result;
    }

    /**
     * 图片下载
     *
     * @param fileName下载文件名称
     * @param domainOfBucket 访问域名
     */
    public String downloadImage(String fileName) {
        // 下载成功返回访问链接
        String result = "";
        try {
            String encodedFileName = URLEncoder.encode(fileName, "utf-8");
            String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
            result = finalUrl;
            logger.info("get the file url:" + finalUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("get the file url failed", e);
            result = "error";
        }

        return result;
    }

    /**
     * 图片删除 key上传文件key（文件存储创建的路径）
     */
    public String deleteImage(String key) {
        String result = "success";
        try {
            // 空间操作器
            BucketManager bucketManager = new BucketManager(getAuth(accessKey, secretKey), config);
            bucketManager.delete(bucket, key);
            logger.info("delete success");
        } catch (QiniuException e) {
            // 如果遇到异常，说明删除失败
            e.printStackTrace();
            logger.error(e.code());
            logger.error(e.response.toString());
            logger.error("delete failed");
            result = "error";
        }
        return result;
    }

    /**
     * 设置目录层级
     */
    public String createKey() {

        Random random = new Random();

        String firstFolder = "0" + random.nextInt(10); // 一级目录
        int temp = random.nextInt(100); // 二级目录

        String seceondFolder;
        if (temp < 10) {
            seceondFolder = firstFolder + "_0" + temp;
        } else {
            seceondFolder = firstFolder + "_" + temp;
        }

        String thirdFolder; // 三级目录
        int temp1 = random.nextInt(1000);
        if (temp1 < 10) {
            thirdFolder = seceondFolder + "_00" + temp1;
        } else if (temp1 > 9 && temp1 < 100) {
            thirdFolder = seceondFolder + "_0" + temp1;
        } else {
            thirdFolder = seceondFolder + "_" + temp1;
        }

        String url = File.separator + "qhg_" + firstFolder + File.separator + seceondFolder + File.separator + thirdFolder
                + File.separator;
        return url;
    }

    /*
     * public static void main(String[] args) { for(int i = 0 ; i<100 ; i++){
     * System.out.println(new UpDownImageQn().createKey()); } }
     */

}
