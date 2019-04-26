package com.inext.view.result;

import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lisige
 */
@ApiModel("芝麻授权")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ZhimaResult extends BaseResult {

    @ApiModelProperty(value = "授权url 如果未授权的话", example = "alipay://alipays://platformapi/startapp?appId=20000067&url=http://xxxxx")
    private String authUrl;

    @ApiModelProperty(value = "是否授权", example = "0")
    private String isAuth;

    @ApiModelProperty(value = "是否在其它平台已经授权过了", example = "1")
    private String alredayAuth;

    @ApiModelProperty(value = "是否授权", example = "723", hidden = true)
    private String score;

    @ApiModelProperty(value = "获取时间", hidden = true)
    private String scoreLastTime;

}
