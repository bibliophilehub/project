package com.inext.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 开发人员：jzhang
 * 创建时间：2017-05-10 上午 11:43
 */
public class FileUtil {

    private static Logger logger = Logger.getLogger(FileUtil.class);

    /**
     * @param response
     * @param filePath //文件完整路径(包括文件名和扩展名)
     * @param fileName //下载后看到的文件名
     * @return 文件名
     */
    public static void fileDownload(HttpServletResponse response, String filePath, String fileName) throws Exception {

        byte[] data = FileUtil.toByteArray2(filePath);
        String agent = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("User-agent");
        // 如果浏览器是IE浏览器，就得进行编码转换
        try {
            if (agent.contains("MSIE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes(), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {//如果出错默认非ie 浏览器
            fileName = new String(fileName.getBytes(), "ISO-8859-1");
        }
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
        response.flushBuffer();
    }

    public static Map handleFileUpload(Map<String, MultipartFile> fileMap) {
        Map params = new HashMap();
        params.put("uploadPathDir", UploadUtils.getRealPath());
        logger.debug("handleFileUpload:" + params);
        return handleFileUpload(fileMap, params, false);
    }

    public static Map handleFileUpload(Map<String, MultipartFile> fileMap, Map fileNames) {
        Map params = new HashMap();
        params.put("uploadPathDir", UploadUtils.getRealPath());
        params.put("fileNames", fileNames);
        return handleFileUpload(fileMap, params, true);

    }

    /**
     * 上传文件
     *
     * @param fileMap 要上传的文件
     * @param params  minSize：文件大小限制的最小值得  单位是KB(可空)
     *                maxSize：文件限制的最大值得  单位是KB(可空)
     *                endName：文件限制的格式(可空)
     *                uploadPathDir：保存的目录  必须是全路径
     *                fileTailName：文件重命名后 名称后面 .之前的字符串(可空)
     *                fileNames：要保存的文件名(可空) key为文件name value为要保存的文件名
     *                isdel  如果文件存在 是否删除 true 删除
     * @return
     */
    public static Map handleFileUpload(Map<String, MultipartFile> fileMap, Map params, boolean isdel) {
        Map map = new HashMap<>();
        String endName = (String) params.get("endName");
        String[] endNameList = null;
        if (endName != null) {
            endNameList = endName.split(",");
        }
        String uploadPathDir = (String) params.get("uploadPathDir");
        //文件重命名后 名称后面 .之前的字符串(类别)
        String fileTailName = params.get("fileTailName") == null ? "" : (String) params.get("fileTailName");
        //要保存的文件名(可空)
        Map fileNames = params.get("fileNames") == null ? new HashMap<>(fileMap.size()) : (Map) params.get("fileNames");
        if (StringUtils.isEmpty(fileTailName)) {
            fileTailName = "";
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowTime = sdf.format(new Date());
        // 保存上传图片文件
        String desDir = uploadPathDir + File.separator;
        // 定义上传路径
        File fileDir = new File(desDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String dateDir = nowTime + File.separator;
        double fileSizeMinLimit = 0;
        double fileSizeMaxLimit = 0;
        try {
            int fileSizeMinKB = (int) params.get("minSize");
            int fileSizeMaxKB = (int) params.get("maxSize");
            fileSizeMinLimit = fileSizeMinKB * 1024;//文件限制的最小值
            fileSizeMaxLimit = fileSizeMaxKB * 1024;//文件限制的最大值
        } catch (Exception e) {

        }
        try {
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile file = entity.getValue();
                if (file == null || file.getSize() == 0) {
                    continue;
                }
                //表单的字段名
                String fieldName = entity.getKey();
                //取上传文件的文件格式
                String myFileName = file.getOriginalFilename();
                String tempEndName = myFileName.substring(myFileName.lastIndexOf("."), myFileName.length()).toLowerCase();
                if (StringUtils.isNotEmpty(endName) && !endName.equals(tempEndName)) {
                    boolean isok = false;
                    for (String string : endNameList) {
                        if (StringUtils.isNotEmpty(string))
                            if (string.equals(tempEndName))
                                isok = true;
                    }
                    if (!isok) {
                        map.put("msg", "文件格式不正确");
                        return map;
                    }
                }
                if (fileSizeMaxLimit > 0) {
                    // 不允许文件大小范围
                    if (file.getSize() < fileSizeMinLimit || file.getSize() > fileSizeMaxLimit) {
                        map.put("msg", "文件大小不在许可范围");
                        return map;
                    }
                }
                //要保存的文件名
                if (fileNames.get(fieldName) == null || StringUtils.isEmpty(fileNames.get(fieldName).toString())) {
                    String defaultFileName = "app";
                    defaultFileName += "_" + System.currentTimeMillis();
                    defaultFileName += fileTailName;
                    fileNames.put(fieldName, defaultFileName);
                }
                String fileName = fileNames.get(fieldName) + tempEndName;
                // 文件保存路径
                File saveFile = new File(desDir, fileName);
                // 转存文件
                if (saveFile.exists()) {
                    if (isdel) {
                        //是就覆盖文件
                        file.transferTo(saveFile);
                    }
                } else {
                    file.transferTo(saveFile);
                }
                map.put(fieldName, file);
                map.put(fieldName + "path", desDir.replace("\\", "/").replace("//", "/") + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "保存文件异常");
        }
        return map;
    }

    /**
     * 删除文件或者文件目录
     *
     * @param path
     */
    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }

    /**
     * 读取到字节数组2
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray2(String filePath) throws IOException {

        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Object> uploadFile(List<MultipartFile> files) {
        Map<String, Object> map = new HashMap<String, Object>();
        String reqPath = null;
        String realFileName = null;
        String suffix = null;
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            realFileName = file.getOriginalFilename();
            /** 构建图片保存的目录 **/
            String filePathDir = UploadUtils.getRelatedPath();
            /** 得到图片保存目录的真实路径 **/
            String fileRealPathDir = UploadUtils.getRealPath();
            /** 获取文件的后缀 **/
            suffix = realFileName.substring(realFileName.lastIndexOf("."));
            String fileImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
            /** 拼成完整的文件保存路径加文件 **/
            String fileName = fileRealPathDir + File.separator + fileImageName;
            File newFile = new File(fileName);
            try {
                FileCopyUtils.copy(file.getBytes(), newFile);
                String resultFilePath = filePathDir + "/" + fileImageName;
                map.put(i + "", resultFilePath);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
