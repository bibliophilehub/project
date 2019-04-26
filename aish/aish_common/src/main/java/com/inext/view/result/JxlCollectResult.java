package com.inext.view.result;


import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lisige
 */
@ApiModel(value = "数据源采集返回")
public class JxlCollectResult extends BaseResult {

    @ApiModelProperty(value = "令牌", example = "8f7e6a6b829740e98b81a9d632008066")
    private String jxlToken;

    @ApiModelProperty(value = "站点", example = "chinaunicom")
    private String website;

    private String code="0";

    public JxlCollectResult() {
    }

    public JxlCollectResult(String jxlToken, String website) {
        this.jxlToken = jxlToken;
        this.website = website;
    }
    public JxlCollectResult(String jxlToken, String website,String code) {
        this.jxlToken = jxlToken;
        this.website = website;
        this.code = code;
    }

    public String getJxlToken() {
        return jxlToken;
    }

    public void setJxlToken(String jxlToken) {
        this.jxlToken = jxlToken;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
