package com.inext.utils.baofoo;

import com.inext.utils.baofoo.rsa.RsaCodingUtil;
import com.inext.utils.baofoo.util.HttpUtils;
import com.inext.utils.baofoo.util.JXMConvertUtil;
import com.inext.utils.baofoo.util.MapToXMLString;
import com.inext.utils.baofoo.util.SecurityUtil;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @createtime 2016年9月26日下午8:58:24
 */
public class BankCardBinAction {



    public void log(String msg) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\t: " + msg);
    }
}
