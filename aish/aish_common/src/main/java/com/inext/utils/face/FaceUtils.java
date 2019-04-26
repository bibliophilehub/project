package com.inext.utils.face;

import com.inext.entity.BorrowUser;
import com.inext.result.JsonResult;
import com.inext.utils.HttpUtil;
import com.inext.utils.JSONUtil;
import com.inext.utils.StringUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * face++ 人脸身份证图片认证工具类
 *
 * @author ttj
 */
@Component
public class FaceUtils {

    @Value("${faceid.app_key}")
    public String appKey;
    @Value("${faceid.app_secret}")
    public String appSecret;
    @Value("${faceid.face_matching}")
    public String url;
    @Value("${faceid.ocr_url}")
    public String url2;

    public Logger logger = LoggerFactory.getLogger(FaceUtils.class);

    public boolean checkBorrowUserImg(BorrowUser bu) throws Exception {
        boolean fls = false;
        Map<String, String> textMap = new HashMap<String, String>();
        //可以设置多个input的name，value
        textMap.put("api_key", appKey);
        textMap.put("api_secret", appSecret);
        // 确定本次比对为“有源比对”或“无源比对”。取值只为“1”或“0”
        textMap.put("comparison_type", "1");
        // 确定待比对图片的类型。取值只为“meglive”、“facetoken”、“raw_image”三者之一
        textMap.put("face_image_type", "raw_image");
        textMap.put("idcard_name", bu.getUserName());
        textMap.put("idcard_number", bu.getUserCardNo());
        /*对验证照作人脸检测时发现有多张脸，是否立即返回错误，或者取最大的一张脸继续比对。本参数取值只能是 “1” 或 "0" （缺省值为“1”）:
        “1”: 立即返回错误码400（MULTIPLE_FACES）
        “0”：取最大脸继续比对
        其他值：返回错误码400（BAD_ARGUMENTS）*/
        textMap.put("fail_when_multiple_faces", "0");

        Map<String, String> fileMap = new HashMap<String, String>();
        //人脸图片地址
        String filePath = bu.getHumanFaceImgUrl();

        fileMap.put("image", filePath.replaceAll("\\\\", "\\/"));
        logger.info("人脸识别参数params" + textMap.toString() + "图片地址：" + fileMap.toString());
        String ret = HttpUtil.formUploadImage2(url, textMap, fileMap, "");
        logger.info("interface" + url + "return info :" + ret);
        if (StringUtils.isNotBlank(ret)) {
            JSONObject result = JSONObject.fromObject(ret);
            if (result.get("error_message") == null || "".equals(StringUtils.getString(result.getString("error_message")))) {
                // 有源比对时，数据源人脸照片与待验证人脸照的比对结果
                Map<String, Object> resultFaceid = (Map<String, Object>) result.get("result_faceid");
                // 	人脸比对接口的返回的误识几率参考值
                //	“1e-3”：误识率为千分之一的置信度阈值；
                //	“1e-4”：误识率为万分之一的置信度阈值；
                //	“1e-5”：误识率为十万分之一的置信度阈值;
                //	“1e-6”：误识率为百万分之一的置信度阈值。
                Map<String, Object> thresholds = (Map<String, Object>) resultFaceid.get("thresholds");
                // 置信度大于误识率率等级万分之一的才被认为人脸认证通过
                if (Float.valueOf(resultFaceid.get("confidence") + "") >= Float.valueOf(thresholds.get("1e-4") + "")) {
                    fls = true;
                } else {
                    throw new Exception("人脸可识别度不高,请检测头像清晰度");
                }

            } else {
                throw new Exception("人脸识别实名认证失败！请检查身份证和头像");
            }
        }
        return fls;
    }

    public JsonResult idcardScanning(HashMap<String, String> map) {

        JsonResult resultCode = new JsonResult("-1", "失败");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("api_key", appKey);
        params.put("api_secret", appSecret);
        // 返回身份证照片合法性检查结果，值只取“0”或“1”。“1”：返回； “0”：不返回。默认“0”。
        params.put("legality", "1");
        Map<String, String> fileMap = new HashMap<String, String>();
        /*图片地址*/
        fileMap.put("image", map.get("filePath"));
        String contentType = "";//image/png
//		System.out.println("请求参数"+params.toString());
        String ret = HttpUtil.formUploadImage(url2, params, fileMap, contentType);
        Map<String, Object> checkResult;
        try {
            checkResult = JSONUtil.parseJSON2Map(ret);
        } catch (Exception e) {
            e.printStackTrace();
            resultCode.setMsg("识别失败,请重新尝试");
            return resultCode;
        }
        if (checkResult.containsKey("error")) {
            resultCode.setMsg("识别失败,请重新尝试");
            return resultCode;
        }
        boolean isIdCardImageFront = "front".equals(checkResult.get("side"));
        // 保存附件信息至数据库
        // 保存一次身份证信息至数据库  但是不更新该用户的人脸识别状态
        // 判断该图片是否为身份证件正面 如果是正面那么将识别出的姓名和身份证号信息保存至
        HashMap<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("isIdCardImageFront", isIdCardImageFront + "");
        if (isIdCardImageFront) {
            String idCard = checkResult.get("id_card_number") + "";
            String userName = checkResult.get("name") + "";
            String gender = checkResult.get("gender") + "";
            resultMap.put("id_card_number", idCard);
            resultMap.put("name", userName);
            resultMap.put("gender", gender);
            resultMap.put("address", checkResult.get("address") + "");
            resultCode.setCode("0");
            resultCode.setMsg("扫描成功");
        }
        resultCode.setParamsMap(resultMap);
        return resultCode;
    }


}
