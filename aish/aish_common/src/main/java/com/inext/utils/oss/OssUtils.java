package com.inext.utils.oss;

import com.aliyun.oss.OSSClient;
import com.inext.constants.AliyunOssContants;
import com.inext.enumerate.Status;
import com.inext.result.ServiceResult;
import com.inext.utils.UploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

/**
 * Created by 李思鸽 on 2017/6/21 0021.
 * 阿里云OSS对象存储SDK调用工具类
 */
public class OssUtils {

    static Logger logger = LoggerFactory.getLogger(OssUtils.class);

    // 创建OSS存储操作对象
    static OSSClient ossClient = new OSSClient(AliyunOssContants.ENDPOINT, AliyunOssContants.ACCESS_KEY_ID, AliyunOssContants.ACCESS_KEY_SECRET);


    /**
     * 上传对象至阿里云OSS
     *
     * @param bytes 对象的byte数字
     * @param name  存储的对象名称
     * @return
     */
    public static ServiceResult ossUpload(byte[] bytes, String name) {
        checkBucketExist(AliyunOssContants.BUCKET_NAME);
        name = UploadUtils.getAliyunOssFileName(name);
        ossClient.putObject(AliyunOssContants.BUCKET_NAME, name,
                new ByteArrayInputStream(bytes));

        return new ServiceResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue(), AliyunOssContants.VISIT_ENDPOINT + name);
    }

    /**
     * 上传对象至阿里云OSS
     *
     * @param bytes 对象的byte数字
     * @param name  存储的对象名称
     * @return 返回图片地址相对路径
     */
    public static String ossUploadPath(byte[] bytes, String name) {
        checkBucketExist(AliyunOssContants.BUCKET_NAME);
        String path = UploadUtils.getAliyunOssFileName(name);
        ossClient.putObject(AliyunOssContants.BUCKET_NAME, path,
                new ByteArrayInputStream(bytes));

        return path;
    }

    /**
     * 验证OSS中是否存在名为#bucketName的bucket,不存在则直接创建
     *
     * @param bucketName
     */
    private static void checkBucketExist(String bucketName) {
        if (!ossClient.doesBucketExist(bucketName)) {
            // 创建Bucket。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            ossClient.createBucket(bucketName);
        }
    }
}
