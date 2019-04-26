package com.inext.controller;


import com.google.common.collect.Maps;
import com.inext.constants.Constants;
import com.inext.constants.RedisCacheConstants;
import com.inext.entity.BorrowUser;
import com.inext.enumerate.ApiStatus;
import com.inext.qiniu.UploadQn;
import com.inext.result.ApiResult;
import com.inext.result.ApiServiceResult;
import com.inext.result.JsonResult;
import com.inext.service.IBorrowUserService;
import com.inext.util.JpgThumbnail;
import com.inext.util.Uploader;
import com.inext.utils.face.FaceUtils;
import com.inext.view.result.IdCardResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传用户图片信息
 *
 * @author user
 */
@RestController
public class PictureController extends BaseController {
    private static final HashMap<String, String> TypeMap = new HashMap<String, String>();
    private static Logger logger = Logger.getLogger(PictureController.class);

    static {
        TypeMap.put("image", "gif,jpg,jpeg,png,bmp");
        TypeMap.put("flash", "swf,flv");
        TypeMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        TypeMap.put("file", "doc,docx,xls,xlsx,ppt,pptx,htm,html,txt,dwg,pdf");
    }

    @Autowired
    private UploadQn uploadQn;
    @Autowired
    private IBorrowUserService iBorrowUserService;
    @Value("${domainOfBucket}")
    private String domainOfBucket;

    @Autowired
    FaceUtils faceUtils;

    /**
     * 个人信息-身份证信息识别-身份证图片信息保存至数据库 身份证图片保存至服务器磁盘      ""10""
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "图片上传(人脸,身份证正反面)")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "type", value = "图片类型(1:人脸识别;2:身份证正面;3:身份证反面)", dataType = "int", paramType = "query")
    })
    @PostMapping("picture/upload-image")
    public ApiResult<IdCardResult> imageUpload(
            HttpServletRequest request, @RequestParam(value = "attach") MultipartFile file, Integer type)
            throws IOException {
        // 判断是否为空 文件大小是否合适
        if (file.isEmpty()) {
            return new ApiResult(ApiStatus.FAIL.getCode(), "请选择文件");
        }
        if (type == null) {
            return new ApiResult(ApiStatus.FAIL.getCode(), "请选择文件类型");
        }

        long fileSize = 2 * 1024 * 1024;
        // 如果文件大小大于限制
        if (file.getSize() > fileSize) {
            return new ApiResult(ApiStatus.FAIL.getCode(), "图片过大,请选择小于2M的图片");
        }

        BorrowUser borrowUser = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN));
        ApiServiceResult apiServiceResult = new ApiServiceResult();
        switch (type) {
            case 1://人脸识别
                apiServiceResult = recognitionImageUpload(borrowUser.getId(), file);
                break;
            case 2://身份证正面
                apiServiceResult = idCardImageUploadZ(borrowUser.getId(), file);
                break;
            case 3://身份证反面
                apiServiceResult = idCardImageUploadF(borrowUser.getId(), file);
                break;
            default:
                break;
        }

        return new ApiResult<>(apiServiceResult);
    }


    /**
     * 身份证正面
     *
     * @param userId
     * @param file
     * @return
     */
    public ApiServiceResult idCardImageUploadZ(Integer userId, MultipartFile file) throws IOException {
        // 当前登录用户
        BorrowUser bu = iBorrowUserService.getBorrowUserById(userId);
        // 获取文件名字
        String originalFilename = file.getOriginalFilename();

        // 获取文件格式 jpg
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "上传图片格式不符合规范");
        }
        String realPath = File.separator + Constants.FILEPATH;
        // 获取文件类型 jpg
        String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
        // 获取 文件位置
        String filePath = Uploader.getQuickPathname(ext);
        // 拼接全路径
        String fullFilePath = realPath + uploadQn.createKey() + filePath;
        File uploadedFile = new File(fullFilePath);
        FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());

        String path = uploadedFile.getPath();
        if (!path.startsWith("\\")) {
            path = File.separator + path;
        }
        String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
        // 生成APP端的手机缩略图
        JpgThumbnail.getThumbnail(path, newPath, 250, 210);
        String url = newPath.replaceAll("\\\\", "\\/");
        url = url.substring(1, url.length());
        String publicNetworkImgUrl = Constants.HTTP + domainOfBucket + "/" + uploadQn.uploadImage(uploadedFile, url);

        HashMap<String, String> params = Maps.newHashMap();
        params.put("filePath", fullFilePath);
        logger.info(path);

        JsonResult jsonResult = faceUtils.idcardScanning(params);
        if (!jsonResult.isSuccessed()) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "身份证正面图片识别失败");
        }


        Map<String, String> map = jsonResult.getParamsMap();
        String idCard = map.get("id_card_number");
        String userName = map.get("name");

        BorrowUser userCard = iBorrowUserService.getBorrowUserByCardNumber(idCard);
        if (userCard != null) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "上传的身份证图片已被绑定");
        }
        if (StringUtils.isNotEmpty(idCard) && StringUtils.isNotEmpty(userName)) {
            bu.setUserCardNo(idCard);
            bu.setUserName(userName);
        }
        uploadQn.uploadImage(uploadedFile, newPath);

        bu.setCardPositiveImg(publicNetworkImgUrl);
        bu.setCardPositiveImgUrl(publicNetworkImgUrl);
        iBorrowUserService.updateUser(bu);
        //删除压缩前的图片
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        return ApiServiceResult.SUCCESS.setExt(new IdCardResult(publicNetworkImgUrl, idCard, userName));
    }


    //身份证反面
    public ApiServiceResult idCardImageUploadF(Integer userId, MultipartFile file) throws IOException {
        // 当前登录用户
        BorrowUser bu = iBorrowUserService.getBorrowUserById(userId);
        // 获取文件名字
        String originalFilename = file.getOriginalFilename();

        // 获取文件格式 jpg
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "上传图片格式不符合规范");
        }
        String realPath = File.separator + Constants.FILEPATH;
        // 获取文件类型 jpg
        String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
        // 获取 文件位置
        String filePath = Uploader.getQuickPathname(ext);
        // 拼接全路径
        File uploadedFile = new File(realPath + uploadQn.createKey() + filePath);
        FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
        String path = uploadedFile.getPath();
        if (!path.startsWith("\\")) {
            path = File.separator + path;
        }
        String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
        // 生成APP端的手机缩略图
        JpgThumbnail.getThumbnail(path, newPath, 250, 210);
        String url = newPath.replaceAll("\\\\", "\\/");
        url = url.substring(1, url.length());
        String publicNetworkImgUrl = Constants.HTTP + domainOfBucket + "/" + uploadQn.uploadImage(uploadedFile, url);

        bu.setCardAntiImg(publicNetworkImgUrl);
        bu.setCardAntiImgUrl(publicNetworkImgUrl);
        iBorrowUserService.updateUser(bu);
        //删除压缩前的图片
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        return ApiServiceResult.SUCCESS.setExt(new IdCardResult(publicNetworkImgUrl));
    }


    /**
     * 人脸上传
     *
     * @return
     */

    public ApiServiceResult recognitionImageUpload(Integer userId, MultipartFile file) throws IOException {
        BorrowUser bu = iBorrowUserService.getBorrowUserById(userId);

        // 获取文件名字 QQ图片20160224093916.jpg
        String originalFilename = file.getOriginalFilename();

        // 获取文件格式 jpg
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "上传图片格式不符合规范");
        }
        // 获取项目的真实路径
        String realPath = File.separator + Constants.FILEPATH;
        // 获取文件类型
        String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
        logger.info("获取文件类型：" + ext);
        // 获取 生成的目录结构
        String filePath = Uploader.getQuickPathname(ext);

        File uploadedFile = new File(realPath + uploadQn.createKey() + filePath);

        // 将上传的文件内容copy至指定的file文件
        FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
        String path = uploadedFile.getPath();
        if (!path.startsWith("\\")) {
            path = File.separator + path;
        }
        String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
        // 生成APP端的手机缩略图
        JpgThumbnail.getThumbnail(path, newPath, 150, 110);
        //2017-05-19
        String url = newPath.replaceAll("\\\\", "\\/");
        url = url.substring(1, url.length());
        String publicNetworkImgUrl = Constants.HTTP + domainOfBucket + "/" + uploadQn.uploadImage(uploadedFile, url);


        bu.setHumanFaceImg(publicNetworkImgUrl);
        bu.setHumanFaceImgUrl(publicNetworkImgUrl);
        iBorrowUserService.updateUser(bu);
        // 删除压缩前的图片
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        logger.info("上传成功:" + path);
        return ApiServiceResult.SUCCESS.setExt(new IdCardResult(publicNetworkImgUrl));
    }


    @ExceptionHandler
    public ApiResult exceptionHandler(Exception e) {
        logger.error("文件上传异常[" + this.getClass().getName() + "]异常", e);
        return new ApiResult(ApiStatus.ERROR.getCode(), e.getMessage());
    }
}
