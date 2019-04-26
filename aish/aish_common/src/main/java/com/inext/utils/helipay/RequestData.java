package com.inext.utils.helipay;

public class RequestData {
	private String trxType;
	private String r1_merchantNo;
	private String r2_orderNumber;
	private String r3_amount;
	private String r4_bankId;
 	private String r5_business;
 	private String r6_timestamp;
 	private String r7_goodsName;
 	private String r8_period;
 	private String r9_periodUnit;
 	private String r10_callbackUrl;
 	private String r11_serverCallbackUrl;
 	private String r12_orderIp;
 	
 	private String r13_cardNo;
 	private String r14_cvv2;
 	private String r15_year;
 	private String r16_month;
 	private String r17_idCardNo;
 	private String r18_payerName;
 	private String r19_payerPhone;
 	private String r20_kuaiType;
 	private String r21_debitOrCredit;
 	private String r22_bind;
 	
 	private String r3_smsVerifyCode;
 	
 	private String r13_bindId;

 	private String sign;
 	
 	private String r4_refundOrderNumber;
 	private String r5_desc;
 	
	public RequestData() {
		super();
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getR1_merchantNo() {
		return r1_merchantNo;
	}

	public void setR1_merchantNo(String r1_merchantNo) {
		this.r1_merchantNo = r1_merchantNo;
	}

	public String getR2_orderNumber() {
		return r2_orderNumber;
	}

	public void setR2_orderNumber(String r2_orderNumber) {
		this.r2_orderNumber = r2_orderNumber;
	}

	public String getR3_amount() {
		return r3_amount;
	}

	public void setR3_amount(String r3_amount) {
		this.r3_amount = r3_amount;
	}

	public String getR4_bankId() {
		return r4_bankId;
	}

	public void setR4_bankId(String r4_bankId) {
		this.r4_bankId = r4_bankId;
	}

	public String getR5_business() {
		return r5_business;
	}

	public void setR5_business(String r5_business) {
		this.r5_business = r5_business;
	}

	public String getR6_timestamp() {
		return r6_timestamp;
	}

	public void setR6_timestamp(String r6_timestamp) {
		this.r6_timestamp = r6_timestamp;
	}

	public String getR7_goodsName() {
		return r7_goodsName;
	}

	public void setR7_goodsName(String r7_goodsName) {
		this.r7_goodsName = r7_goodsName;
	}

	public String getR8_period() {
		return r8_period;
	}

	public void setR8_period(String r8_period) {
		this.r8_period = r8_period;
	}

	public String getR9_periodUnit() {
		return r9_periodUnit;
	}

	public void setR9_periodUnit(String r9_periodUnit) {
		this.r9_periodUnit = r9_periodUnit;
	}

	public String getR10_callbackUrl() {
		return r10_callbackUrl;
	}

	public void setR10_callbackUrl(String r10_callbackUrl) {
		this.r10_callbackUrl = r10_callbackUrl;
	}

	public String getR11_serverCallbackUrl() {
		return r11_serverCallbackUrl;
	}

	public void setR11_serverCallbackUrl(String r11_serverCallbackUrl) {
		this.r11_serverCallbackUrl = r11_serverCallbackUrl;
	}

	public String getR12_orderIp() {
		return r12_orderIp;
	}

	public void setR12_orderIp(String r12_orderIp) {
		this.r12_orderIp = r12_orderIp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getR4_refundOrderNumber() {
		return r4_refundOrderNumber;
	}

	public void setR4_refundOrderNumber(String r4_refundOrderNumber) {
		this.r4_refundOrderNumber = r4_refundOrderNumber;
	}

	public String getR5_desc() {
		return r5_desc;
	}

	public void setR5_desc(String r5_desc) {
		this.r5_desc = r5_desc;
	}

	@Override
	public String toString() {
		return "RequestData [trxType=" + trxType + ", r1_merchantNo=" + r1_merchantNo + ", r2_orderNumber=" + r2_orderNumber + ", r3_amount=" + r3_amount
				+ ", r4_bankId=" + r4_bankId + ", r5_business=" + r5_business + ", r6_timestamp=" + r6_timestamp + ", r7_goodsName=" + r7_goodsName + ", r8_period="
				+ r8_period + ", r9_periodUnit=" + r9_periodUnit + ", r10_callbackUrl=" + r10_callbackUrl + ", r11_serverCallbackUrl=" + r11_serverCallbackUrl
				+ ", r12_orderIp=" + r12_orderIp + ", sign=" + sign + ", r4_refundOrderNumber=" + r4_refundOrderNumber + ", r5_desc=" + r5_desc + "]";
	}

	public String getR13_cardNo() {
		return r13_cardNo;
	}

	public void setR13_cardNo(String r13_cardNo) {
		this.r13_cardNo = r13_cardNo;
	}

	public String getR14_cvv2() {
		return r14_cvv2;
	}

	public void setR14_cvv2(String r14_cvv2) {
		this.r14_cvv2 = r14_cvv2;
	}

	public String getR15_year() {
		return r15_year;
	}

	public void setR15_year(String r15_year) {
		this.r15_year = r15_year;
	}

	public String getR16_month() {
		return r16_month;
	}

	public void setR16_month(String r16_month) {
		this.r16_month = r16_month;
	}

	public String getR17_idCardNo() {
		return r17_idCardNo;
	}

	public void setR17_idCardNo(String r17_idCardNo) {
		this.r17_idCardNo = r17_idCardNo;
	}

	public String getR18_payerName() {
		return r18_payerName;
	}

	public void setR18_payerName(String r18_payerName) {
		this.r18_payerName = r18_payerName;
	}

	public String getR19_payerPhone() {
		return r19_payerPhone;
	}

	public void setR19_payerPhone(String r19_payerPhone) {
		this.r19_payerPhone = r19_payerPhone;
	}

	public String getR20_kuaiType() {
		return r20_kuaiType;
	}

	public void setR20_kuaiType(String r20_kuaiType) {
		this.r20_kuaiType = r20_kuaiType;
	}

	public String getR21_debitOrCredit() {
		return r21_debitOrCredit;
	}

	public void setR21_debitOrCredit(String r21_debitOrCredit) {
		this.r21_debitOrCredit = r21_debitOrCredit;
	}

	public String getR22_bind() {
		return r22_bind;
	}

	public void setR22_bind(String r22_bind) {
		this.r22_bind = r22_bind;
	}

	public String getR3_smsVerifyCode() {
		return r3_smsVerifyCode;
	}

	public void setR3_smsVerifyCode(String r3_smsVerifyCode) {
		this.r3_smsVerifyCode = r3_smsVerifyCode;
	}

	public String getR13_bindId() {
		return r13_bindId;
	}

	public void setR13_bindId(String r13_bindId) {
		this.r13_bindId = r13_bindId;
	}
	

}
