package com.inext.utils.baofoo;

/**
 * Created by dhl on 2017/5/25.
 */

public class BaseUtils {
    protected static String pfxpath;
    protected static String cutpfxpath;
    protected static String cerpath;

    protected static String version;
    protected static String member_id;
    protected static String auth_terminal_id;

    //新颜
    protected static String xypfxpath;
    protected static String xycerpath;
    protected static String xycername;

    protected static String xydate_type;
    protected static String xymember_id;
    protected static String xyterminal_id;

    static {

        //宝付
        pfxpath = PropertiesUtil.get("file.auth.pfx.name");//商户私钥 认证
        cutpfxpath = PropertiesUtil.get("file.pfx.name");//商户私钥 代扣
        cerpath = PropertiesUtil.get("file.cer.name");//宝付公钥

        version = PropertiesUtil.get("version");
        member_id = PropertiesUtil.get("member.id");//商户号
        auth_terminal_id = PropertiesUtil.get("auth.terminal.id");//终端号

        //新颜
        xypfxpath = PropertiesUtil.get("file.xy.auth.pfx.name");//商户私钥 认证
        xycerpath = PropertiesUtil.get("file.xy.cer.name");//宝付公钥
        xycername = PropertiesUtil.get("xy.cer.name");//宝付密码

        xymember_id = PropertiesUtil.get("xy.member.id");//商户号
        xyterminal_id = PropertiesUtil.get("xy.terminal.id");//终端号
        xydate_type = PropertiesUtil.get("xy.data.type");//数据类型


    }

}
