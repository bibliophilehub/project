package com.inext.view.result;

import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * @Author: jzhang
 * @Date: 2018-04-14 0014 上午 10:38
 */
public class LogisticsInfoResult extends BaseResult {

    @ApiModelProperty(value = "收件人", readOnly = true, example = "上海主林金融信息服务股份有限公司")
    private String addressee="陈先生收";

    @ApiModelProperty(value = "收件人手机号", readOnly = true, example = "138123123123")
    private String phone="0715-6527323";

    @ApiModelProperty(value = "收件人地址", readOnly = true, example = "上海长宁明基广场D栋6楼")
    private String addr="湖北省嘉鱼县牌洲湾镇陈家坊泰达网络";

    @ApiModelProperty(value = "物品信息", readOnly = true, example = "*1,USB连接线*1,USB电源适配器*1")
    private String resInfo="*1,USB连接线*1,USB电源适配器*1";

    @ApiModelProperty(value = "数据")
    private java.util.Map logisticsCompany;

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Map getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(Map logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getResInfo() {
        return resInfo;
    }

    public void setResInfo(String resInfo) {
        this.resInfo = resInfo;
    }
}
