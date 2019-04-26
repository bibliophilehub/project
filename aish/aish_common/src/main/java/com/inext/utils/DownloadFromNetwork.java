package com.inext.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Java读取并下载网络文件
 *
 * @author Marydon
 * @createTime 2017年8月18日下午5:24:09
 * @updateTime
 * @Email:marydon2017@163.com
 * @version:1.0.0
 * @referenceLink <a href="http://blog.csdn.net/xb12369/article/details/40543649/"> java 从网络Url中下载文件</a>
 */
public class DownloadFromNetwork {
    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(5 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }

        System.out.println("info:" + url + " download success");

    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        String imgUrl = "http://p6wppefop.bkt.clouddn.com/files/qhg_00/00_16/00_16_001/20180410152939_40ipx5t7r2_appTh.png";
        String imgName = imgUrl.substring(imgUrl.lastIndexOf("/"));
        System.err.println(imgName);
        downLoadFromUrl("http://p6wppefop.bkt.clouddn.com/files/qhg_00/00_16/00_16_001/20180410152939_40ipx5t7r2_appTh.png",
                imgName, "/temp");
    }
}