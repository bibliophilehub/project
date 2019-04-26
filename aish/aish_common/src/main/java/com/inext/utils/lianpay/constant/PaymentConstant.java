package com.inext.utils.lianpay.constant;

public class PaymentConstant {

	/** 连连银通公钥（不需替换，这是连连公钥，用于报文加密和连连返回响应报文时验签，不是商户生成的公钥. */
	public static final String PUBLIC_KEY_ONLINE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";

	/** 商户私钥 商户通过openssl工具生成公私钥，公钥通过商户站上传，私钥用于加签，替换下面的值 . */
	public static final String BUSINESS_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALw7Gp6xTLHfViqlDIepR7ArzagcJw2F4sfffpUrJE5arrHtC61OwB7rGce7qjWeMLvffumiAZ7I5yMPMFe3OaGFPIHMpspcOWS9hyChoajczlOdpF56WJqigCle5TUGwQM6BeCET2gDac4NIGgh4miCDNm3eoi0rERcQSq5uHonAgMBAAECgYBSQgIAF7tMgC1HtZkkL/YEJyBYtvTaFAhnEGRhC9lx5G8zkkPoGOIcoOFDR9+6Tsc9Uw5DS2kp2uInkBuwOYIVMvQ67yqvUG/r1kBw4zIbHgGubHaJVBGKVwSlWnhrX2lTS6GLRMmlu5DY2dwYPjCU5zxOo+hOS/GCRw2rZOnMAQJBAPWA91vqJyvffMlf8W35ht53cCksIC/IW34F0aRKK7zWdqIr8FYpO/O1lZ96BA6A5rxqDC8g7ZJqtwf57/nh6BcCQQDER0l6+91MGnCfYJ0Bkvs8JrlIFxVLLm/6TAmmUS/57YdB2uCtiHk+aWIX8E/Rg6kIIL+D2rr9Es0uYRf5JDhxAkEA4ImHs0VBdlr9IqDhi5gAxk+vAbkd5xX0uBpSV1SPFDOCZWiSc/tandeidS1/sFQstH65jhEruBJqPJ1X4s+/GQJBAMAV59KE9GDLK1a+TUlCF78ZQpBLjtpBuvKgQX1TzPjxvUH0u5b4jXdH2OkGlIa9K8o5ilO21vJe2RA/kx+Ok/ECQAgKy2YwG9fL+N+F6Oiq611cw0aBSBuhPnqjcKFTudEiSZRyT41kTxPaySZvIfQ4Pw6KUioi/wZAqZgx0UJImVo=";

	/** 商户号（商户需要替换自己的商户号）. */
	public static final String OID_PARTNER = "201803190001646101";

	/** 实时付款api版本. */
	public static final String API_VERSION = "1.0";

	/** 实时付款签名方式. */
	public static final String SIGN_TYPE = "RSA";

}
