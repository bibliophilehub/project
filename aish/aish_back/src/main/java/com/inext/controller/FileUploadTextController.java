package com.inext.controller;

import com.inext.enumerate.Status;
import com.inext.result.AjaxResult;
import com.inext.utils.SpringUtils;
import com.inext.utils.UploadUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author ltq 批量上传图片和附件
 */
@SuppressWarnings("restriction")
@Controller
@RequestMapping("backUpload/")
public class FileUploadTextController extends BaseController {
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory
            .getLogger(FileUploadTextController.class);
    private static long maxSize = 1000000;
    private static HashMap<String, String> extMap = new HashMap<String, String>();

    static {
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file",
                "doc,docx,xls,xlsx,ppt,pptx,pdf,htm,html,txt,zip,rar,gz,bz2");
    }

    @PostMapping(value = "/uploadFiles")
    @ResponseBody
    public AjaxResult uploadFiles(HttpServletRequest request, @Value("${web.upload-path}") String dirPath, 
    		@Value("${web.images-loadpath}") String imagesloadPath) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("Filedata");

        String reqPath = null;
        String realFileName = null;
        String suffix = null;
        for (MultipartFile file : files) {
            realFileName = file.getOriginalFilename();
            /** 构建图片保存的目录 **/
            String filePathDir = UploadUtils.getRelatedPath();
            /** 得到图片保存目录的真实路径 **/
            String fileRealPathDir = UploadUtils.getRealPath(dirPath + filePathDir);
            /** 获取文件的后缀 **/
            suffix = realFileName.substring(realFileName.lastIndexOf("."));
            // /**使用UUID生成文件名称**/
            String fileImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
            /** 拼成完整的文件保存路径加文件 **/
            String fileName = fileRealPathDir + File.separator + fileImageName;


            File newFile = new File(fileName);
            FileCopyUtils.copy(file.getBytes(), newFile);

            String resultFilePath = filePathDir + File.separator + fileImageName;
            if (StringUtils.isNotBlank(resultFilePath)) {
                resultFilePath = resultFilePath.replaceAll("\\\\", "/");
            }
            // 返回路径给页面上传
            String path = request.getContextPath();
            String basePath = imagesloadPath+"/" + path + "/";
            
            reqPath = basePath + resultFilePath;
            
            
        }
        // 返回路径给页面上传
        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue(), reqPath);

    }

    @RequestMapping(value = "/uploadFilesCompress")
    public void uploadFilesCompress(HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("Filedata");

        String reqPath = null;
        String realFileName = null;
        String suffix = null;
        for (MultipartFile file : files) {
            realFileName = file.getOriginalFilename();
            /** 构建图片保存的目录 **/
            String filePathDir = UploadUtils.getRelatedPath();
            /** 得到图片保存目录的真实路径 **/
            String fileRealPathDir = UploadUtils.getRealPath();
            /** 获取文件的后缀 **/
            suffix = realFileName.substring(realFileName.lastIndexOf("."));
            // /**使用UUID生成文件名称**/
            String fileImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
            // String fileImageName = multipartFile.getOriginalFilename();
            /** 拼成完整的文件保存路径加文件 **/
            String fileName = fileRealPathDir + File.separator
                    + fileImageName;

            String resultFilePath = filePathDir + "/" + fileImageName;
            File newFile = new File(fileName);
            try {
                //Thumbnails.of(file.getInputStream()).size(SysConstant.IMG_COMPRESS_WIDE,SysConstant.IMG_COMPRESS_HIGH).toFile(newFile);
                FileCopyUtils.copy(file.getBytes(), newFile);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(resultFilePath)) {
                resultFilePath = resultFilePath.replaceAll("\\\\", "/");
            }
            reqPath = resultFilePath;
            // 返回路径给页面上传

        }
        String flag = request.getParameter("flag");
        // 返回路径给页面上传
        String ret = "[{\"filepath\":\"" + reqPath + "\",\"filename\":\""
                + realFileName + "\",\"suffix\":\"" + suffix
                + "\",\"flag\":\"" + flag + "\"}]";
        SpringUtils.renderJson(response, ret);

    }

    @RequestMapping(value = "/uploadFilesEqualsCompress")
    public void uploadFilesEqualsCompress(HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("Filedata");
        String reqPath = null;
        String realFileName = null;
        String suffix = null;
        for (MultipartFile file : files) {
            realFileName = file.getOriginalFilename();
            /** 构建图片保存的目录 **/
            String filePathDir = UploadUtils.getRelatedPath();
            /** 得到图片保存目录的真实路径 **/
            String fileRealPathDir = UploadUtils.getRealPath();
            /** 获取文件的后缀 **/
            suffix = realFileName.substring(realFileName.lastIndexOf("."));
            // /**使用UUID生成文件名称**/
            String fileImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
            // String fileImageName = multipartFile.getOriginalFilename();
            /** 拼成完整的文件保存路径加文件 **/
            String fileName = fileRealPathDir + File.separator
                    + fileImageName;

            String resultFilePath = filePathDir + "/" + fileImageName;
            File newFile = new File(fileName);

            try {
                float outputQuality = 1f;
                if (file.getSize() > (50 * 1024)) {//大于50K
                    outputQuality = 50f / (file.getSize() / 1024f);
                }
                //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
                Thumbnails.of(file.getInputStream()).scale(1f).outputQuality(outputQuality).toFile(newFile);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(resultFilePath)) {
                resultFilePath = resultFilePath.replaceAll("\\\\", "/");
            }
            reqPath = resultFilePath;
            // 返回路径给页面上传

        }
        String flag = request.getParameter("flag");
        // 返回路径给页面上传
        String ret = "[{\"filepath\":\"" + reqPath + "\",\"filename\":\""
                + realFileName + "\",\"suffix\":\"" + suffix
                + "\",\"flag\":\"" + flag + "\"}]";
        SpringUtils.renderJson(response, ret);

    }

    /**
     * 删除物理文件 2013-12-12 cjx
     */
    @RequestMapping(value = "/removeImg", method = RequestMethod.POST)
    public void removeImg(HttpServletRequest request,
                          HttpServletResponse response) {
        String msg = "0";
        try {
            String imgUrlString = request.getParameter("imgUrl");
            if (StringUtils.isNotBlank(imgUrlString)) {
                String fileRealPathDir = request.getSession()
                        .getServletContext().getRealPath(imgUrlString);
                File file = new File(fileRealPathDir);
                if (file.exists()) {
                    file.delete();
                }
                msg = "1";
            }
        } catch (Exception e) {
            log.error("removeImg error ", e);
        }
        SpringUtils.renderJson(response, msg);
    }

    @RequestMapping(value = "/editorImg")
    public void attachUpload(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String ret = "";
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
                .getFile("filedata");
        if (file.getSize() > maxSize) {
            ret = "{\"err\":\"1\",\"msg\":\"上传文件大小超过限制\"}";
            SpringUtils.renderJson(response, ret);
            return;
        }
        String realFileName = file.getOriginalFilename();
        /** 构建图片保存的目录 **/
        String filePathDir = UploadUtils.getRelatedPath();
        /** 得到图片保存目录的真实路径 **/
        String fileRealPathDir = UploadUtils.getRealPath();

        /** 获取文件的后缀 **/
        String suffix = realFileName.substring(realFileName.lastIndexOf("."));
        if (Arrays.<String>asList(extMap.get("image").split(",")).contains(
                realFileName.substring(realFileName.lastIndexOf(".") + 1)
                        .toLowerCase())) {
            // /**使用UUID生成文件名称**/
            String fileImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
            // String fileImageName = multipartFile.getOriginalFilename();
            /** 拼成完整的文件保存路径加文件 **/
            String fileName = fileRealPathDir + File.separator + fileImageName;

            String resultFilePath = filePathDir + "/" + fileImageName;
            File newFile = new File(fileName);
            try {
                FileCopyUtils.copy(file.getBytes(), newFile);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(resultFilePath)) {
                resultFilePath = resultFilePath.replaceAll("\\\\", "/");
            }
            String prefix = request.getContextPath();
            String reqPath = prefix + resultFilePath;
            // 返回路径给页面上传
            ret = "{\"err\":\"\",\"msg\":\"" + reqPath + "\"}";
        } else {
            ret = "{\"err\":\"\",\"msg\":\"上传文件格式错误\"}";
        }
        SpringUtils.renderText(response, ret);

    }
}
