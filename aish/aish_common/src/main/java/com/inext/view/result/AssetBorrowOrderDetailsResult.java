package com.inext.view.result;


import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author lisige
 */
@ApiModel("订单")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssetBorrowOrderDetailsResult extends BaseResult {

    @ApiModelProperty(value = "订单ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "设备名称", example = "iphone 7")
    private String deviceModel;

    @ApiModelProperty(value = "申请日", example = "2018-01-01 10:00:00")
    private String dateTime;

    @ApiModelProperty(value = "状态 {1=平台审核中, 5=审核拒绝, 8=已取消, 11=已完成, 2=待打款, 3=待寄出, 7=已违约, 10=已寄出}", example = "1")
    private Integer status;

    @ApiModelProperty(value = "状态展示字符", example = "审核中")
    private String statusStr;

    @ApiModelProperty(value = "预付款 单位(元)", example = "850.00")
    private String perPayMoney;

    @ApiModelProperty(value = "违约金", example = "150.00")
    private String penaltyAmount;

    @ApiModelProperty(value = "银行卡号", example = "6217001210043349537")
    private String userCardCode;

    @ApiModelProperty(value = "所属银行", example = "850.00")
    private String userCardType;

    @ApiModelProperty(value = "违约天数", example = "850.00")
    private String lateDay;

    @ApiModelProperty(value = "履约到期日", example = "2018-04-17 19:56:40")
    private String loanEndTime;

    @ApiModelProperty(value = "履约到期日", example = "2018-04-17 19:56:40")
    private String payEndTime;

    @ApiModelProperty(value = "滞纳金", example = "850.00")
    private String lateMoney;
    private String delaySwitch;
    private List<AssetBorrowOrderStatusHistoryResult> historyList;

}
