package com.inext.utils.jxl;

import com.inext.entity.BorrowUser;
import com.inext.entity.BorrowUserJxl;
import com.inext.utils.OkHttpUtils;
import net.sf.json.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 聚信立工具类
 *
 * @author ttj
 */
public class JxlUtils {
    private final static String JXL_TokenUrl = "https://www.juxinli.com/orgApi/rest/v3/applications/";
    private final static String JXL_NAME = "lqkjxx";
    private final static String JXL_BZM = "d2186c0f4ee64cf0951b3923feddfd8c";
    private final static String JXL_token = "9da01ebe2d0342c3b6c1e1fc63cf9262";
    Logger logger = LoggerFactory.getLogger(JxlUtils.class);

    /**
     * @param userName  用户名称
     * @param cardNo    用户身份证号码
     * @param userPhone 用户手机号码
     * @param contacts
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static BorrowUserJxl getJxlToken(BorrowUser bu) throws Exception {
        String value = "{\"selected_website\":[],\"basic_info\":{\"name\"	:\"" + bu.getUserName() + "\",\"id_card_num\":\"" + bu.getUserCardNo()
                + "\",\"cell_phone_num\":\"" + bu.getUserPhone() + "\"},\"skip_mobile\":" + false + ",\"uid\":\"" + bu.getId() + "\",\"contacts\":[]}";
        String jxlUrl = JXL_TokenUrl + JXL_NAME;
        String result = OkHttpUtils.post(jxlUrl, value);
        BorrowUserJxl bj = new BorrowUserJxl();
        try {
            JSONObject jsonObject = JSONObject.fromObject(result);
            if ("65557".equals(jsonObject.getString("code"))) {
                jsonObject = jsonObject.getJSONObject("data");
                bj.setUserId(bu.getId());
                bj.setToken(jsonObject.getString("token"));
            } else if ("65545".equals(jsonObject.getString("code"))) {
                throw new Exception("紧急联系人手机号码不合法");
            } else {
                throw new Exception(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            throw new Exception("解析数据出错");
        }
        return bj;
    }
}
