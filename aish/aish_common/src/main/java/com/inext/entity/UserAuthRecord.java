package com.inext.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import lombok.Data;

/**
 * app芝麻分日志对象
 *
 * @author ttj
 */
@Data
public class UserAuthRecord implements Serializable {

	
    private Integer id;//`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序列号',
    
	private String user_phone;    //银行四要素之预留手机号码

	private String user_name;   //银行四要素之用户姓名

	private String user_id_no;  //银行四要素之身份证号码
	
	private String user_card_no;  //银行四要素之银行卡号

	private Integer user_id;    //

	private String terminal_no;  //商户订单号

	private String trade_no;   //第三方平台交易号
	
	private Integer c_name;   //第三方平台名称1：新颜 2合利宝

	private Date create_time;  //创建时间
	
	private String return_params;   //返回参数

	private Date update_time;   //更新时间
	
	private String auth_code;  //返回code
	
	private String is_fee;  //是否收费Y收N不收
	
	private Integer user_card_id; // 用户银行名称id

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_id_no() {
		return user_id_no;
	}

	public void setUser_id_no(String user_id_no) {
		this.user_id_no = user_id_no;
	}

	public String getUser_card_no() {
		return user_card_no;
	}

	public void setUser_card_no(String user_card_no) {
		this.user_card_no = user_card_no;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getTerminal_no() {
		return terminal_no;
	}

	public void setTerminal_no(String terminal_no) {
		this.terminal_no = terminal_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public Integer getC_name() {
		return c_name;
	}

	public void setC_name(Integer c_name) {
		this.c_name = c_name;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getReturn_params() {
		return return_params;
	}

	public void setReturn_params(String return_params) {
		this.return_params = return_params;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public String getIs_fee() {
		return is_fee;
	}

	public void setIs_fee(String is_fee) {
		this.is_fee = is_fee;
	}

	public Integer getUser_card_id() {
		return user_card_id;
	}

	public void setUser_card_id(Integer user_card_id) {
		this.user_card_id = user_card_id;
	}
	
	

}
